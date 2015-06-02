package dst.ass3.event.impl;

import com.espertech.esper.client.*;
import dst.ass3.dto.LectureWrapperDTO;
import dst.ass3.event.Constants;
import dst.ass3.event.IEventStreaming;
import dst.ass3.model.ILectureWrapper;
import dst.ass3.model.LifecycleState;

/**
 * Created by pavol on 27.5.2015.
 */
public class EventStreaming implements IEventStreaming {

    private EPServiceProvider epServiceProvider;
    private StdoutListener stdoutListener = new StdoutListener();

    @Override
    public void initializeAll(StatementAwareUpdateListener listener, boolean debug) {
        /**
         * Initialization
         */
        Configuration configuration = new Configuration();
        configuration.addEventType(Constants.EVENT_LECTURE, LectureWrapperDTO.class);
        configuration.addImport(LifecycleState.class);

        if (debug) {
            configuration.getEngineDefaults().getLogging().setEnableExecutionDebug(true);
            configuration.getEngineDefaults().getLogging().setEnableQueryPlan(false);
            configuration.getEngineDefaults().getLogging().setEnableTimerDebug(false);
        }

        epServiceProvider = EPServiceProviderManager.getDefaultProvider(configuration);
        EPAdministrator epAdministrator = epServiceProvider.getEPAdministrator();

        /**
         * Creating new types
         * http://www.espertech.com/esper/release-5.2.0/esper-reference/html/epl_clauses.html#epl_createschema
         */
        epAdministrator.createEPL(String.format("create schema %s as (%s long, %s long)",
                Constants.EVENT_LECTURE_ASSIGNED,
                Constants.EVENT_PROP_LECTURE_ID,
                Constants.EVENT_PROP_TIMESTAMP));
        epAdministrator.createEPL(String.format("create schema %s as (%s long, %s long)",
                Constants.EVENT_LECTURE_STREAMED,
                Constants.EVENT_PROP_LECTURE_ID,
                Constants.EVENT_PROP_TIMESTAMP));
        epAdministrator.createEPL(String.format("create schema %s as (%s long, %s long)",
                Constants.EVENT_LECTURE_DURATION,
                Constants.EVENT_PROP_LECTURE_ID,
                Constants.EVENT_PROP_DURATION));

        /**
         * Create queries
         */
        epAdministrator.createEPL(String.format("insert into %s(%s, %s) select lectureId, current_timestamp as %s from %s(state=LifecycleState.ASSIGNED) ",
                Constants.EVENT_LECTURE_ASSIGNED,
                Constants.EVENT_PROP_LECTURE_ID,
                Constants.EVENT_PROP_TIMESTAMP,
                Constants.EVENT_PROP_TIMESTAMP,
                Constants.EVENT_LECTURE));
        epAdministrator.createEPL(String.format("insert into %s(%s, %s) select lectureId, current_timestamp from %s(state=LifecycleState.STREAMED)",
                Constants.EVENT_LECTURE_STREAMED,
                Constants.EVENT_PROP_LECTURE_ID,
                Constants.EVENT_PROP_TIMESTAMP,
                Constants.EVENT_LECTURE));
        epAdministrator.createEPL(String.format("insert into %s(%s, %s) select assigned.lectureId, streamed.timestamp-assigned.timestamp " +
                        "from %4$s.win:length(10000) as assigned, %5$s.win:length(10000) as streamed " +
                        "where assigned.%2$s = streamed.%2$s",
                Constants.EVENT_LECTURE_DURATION,
                Constants.EVENT_PROP_LECTURE_ID,
                Constants.EVENT_PROP_DURATION,
                Constants.EVENT_LECTURE_ASSIGNED,
                Constants.EVENT_LECTURE_STREAMED));

        /**
         *
         */
        EPStatement duration = epAdministrator.createEPL(String.format("select * from %s",
                Constants.EVENT_LECTURE_DURATION));
        epAdministrator.createEPL(String.format("select * from %s",
                Constants.EVENT_LECTURE_STREAMED))
            .addListener(listener);
        epAdministrator.createEPL(String.format("select * from %s",
                Constants.EVENT_LECTURE_ASSIGNED))
            .addListener(listener);

        EPStatement avgDuration = epAdministrator.createEPL(String.format("select avg(%1$s) as %2$s from %3$s.win:time(15 sec)",
                Constants.EVENT_PROP_DURATION,
                Constants.EVENT_AVG_LECTURE_DURATION,
                Constants.EVENT_LECTURE_DURATION));

        EPStatement statusChanged = epAdministrator.createEPL(String.format("select * " +
                        "from pattern [every lec=%1$s(state=LifecycleState.READY_FOR_STREAMING) -> " +
                        " %1$s(%2$s=lec.%2$s and state=LifecycleState.STREAMING_NOT_POSSIBLE) ->  " +
                        " %1$s(%2$s=lec.%2$s and state=LifecycleState.READY_FOR_STREAMING) -> " +
                        " %1$s(%2$s=lec.%2$s and state=LifecycleState.STREAMING_NOT_POSSIBLE) -> " +
                        " %1$s(%2$s=lec.%2$s and state=LifecycleState.READY_FOR_STREAMING) -> " +
                        " %1$s(%2$s=lec.%2$s and state=LifecycleState.STREAMING_NOT_POSSIBLE)] as lecture " +
                        "group by lec.lectureId " +
                        "having (count(lecture) - 1) %% 3 = 0 ",
                Constants.EVENT_LECTURE,
                Constants.EVENT_PROP_LECTURE_ID));

        duration.addListener(listener);
        avgDuration.addListener(listener);
        statusChanged.addListener(listener);

        duration.addListener(stdoutListener);
    }

    private class StdoutListener implements StatementAwareUpdateListener {
        @Override
        public void update(EventBean[] newEvents, EventBean[] oldEvents,
                           EPStatement statement, EPServiceProvider epServiceProvider) {

            for (EventBean eventBean: newEvents){
                System.out.println(eventBean);
            }
        }
    }

    @Override
    public void addEvent(ILectureWrapper lectureWrapper) {
        epServiceProvider.getEPRuntime().sendEvent(new LectureWrapperDTO(lectureWrapper));
    }

    @Override
    public void close() {
        epServiceProvider.destroy();
    }
}
