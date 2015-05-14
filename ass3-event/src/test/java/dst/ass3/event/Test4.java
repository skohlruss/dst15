package dst.ass3.event;

import static dst.ass3.EventingUtils.ESPER_CHECK_TIMEOUT;
import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.fail;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

import dst.ass3.AbstractEventTest;
import dst.ass3.EventingUtils;
import dst.ass3.model.LectureType;
import dst.ass3.model.ILectureWrapper;
import dst.ass3.model.LifecycleState;

/**
 * Checks LectureDuration query.
 * 
 * Timing:
 * 
 * <pre>
 *  2s   2s  2s  2s
 * |---|---|---|---|---|---|
 * ^   ^   ^       ^
 * l1  l3  l1      l2
 * l2
 * l3
 * l4
 * </pre>
 */
public class Test4 extends AbstractEventTest {

	private Semaphore semDuration;

	private long[] lectureWrapperIds = { 401L, 402L, 403L, 404L };
	private long[] lectureIds = { 411, 412, 413, 414 };

	@Test
	public void test_LectureDurationQuery() {

		semDuration = new Semaphore(0);

		ILectureWrapper l1 = EventingFactory.createLectureWrapper(lectureWrapperIds[0],
				lectureIds[0], LifecycleState.ASSIGNED, CLASSIFIEDBY, LectureType.PRESENTATION);
		ILectureWrapper l2 = EventingFactory.createLectureWrapper(lectureWrapperIds[1],
				lectureIds[1], LifecycleState.ASSIGNED, CLASSIFIEDBY, LectureType.PRESENTATION);
		ILectureWrapper l3 = EventingFactory.createLectureWrapper(lectureWrapperIds[2],
				lectureIds[2], LifecycleState.ASSIGNED, CLASSIFIEDBY, LectureType.PRESENTATION);
		ILectureWrapper l4 = EventingFactory.createLectureWrapper(lectureWrapperIds[3],
				lectureIds[3], LifecycleState.ASSIGNED, CLASSIFIEDBY, LectureType.PRESENTATION);

		test.initializeAll(new StatementAwareUpdateListener() {

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents,
					EPStatement s, EPServiceProvider p) {
				Long duration = null;
				for (EventBean e : newEvents) {
					System.out.println("LISTENER:" + e.getEventType().getName()
							+ " " + e.getUnderlying());
					String name = e.getEventType().getName();
					if (name.equals(Constants.EVENT_LECTURE_DURATION)) {
						EventingUtils.ensureLectureId("Duration", e, lectureIds);
						duration = EventingUtils.getLong(e, Constants.EVENT_PROP_DURATION);
						long lectureId = EventingUtils.getLong(e, Constants.EVENT_PROP_LECTURE_ID);

						if (lectureId == lectureIds[0]) {
							EventingUtils.ensureRange("Duration " + lectureId,
									4000, duration, allowedInaccuracy);
							semDuration.release();
						} else if (lectureId == lectureIds[1]) {
							EventingUtils.ensureRange("Duration " + lectureId,
									8000, duration, allowedInaccuracy);
							semDuration.release();
						} else if (lectureId == lectureIds[2]) {
							EventingUtils.ensureRange("Duration " + lectureId,
									2000, duration, allowedInaccuracy);
							semDuration.release();
						} else {
							fail("unknown lectureId! Not expected " + lectureId);
						}

					}
				}

			}
		}, false);

		sleep(SHORT_WAIT); // wait for setup
		final long startTime = System.currentTimeMillis();
		logCheckpoint(0, startTime);

		test.addEvent(l1);
		test.addEvent(l2);
		test.addEvent(l3);
		test.addEvent(l4);

		sleep(2000); // fixed 2sec see description
		logCheckpoint(1, startTime);

		l3.setState(LifecycleState.STREAMED);
		test.addEvent(l3);

		sleep(2000); // fixed 2sec see description
		logCheckpoint(2, startTime);

		l1.setState(LifecycleState.STREAMED);
		test.addEvent(l1);

		sleep(2000); // fixed 2sec see description
		sleep(2000); // fixed 2sec see description
		logCheckpoint(3, startTime);

		l2.setState(LifecycleState.STREAMED);
		test.addEvent(l2);

		// t4 never reaches streamed

		logTimed("checking results", startTime);
		assure(semDuration, 3, "3 lectureDuration event expected",
				ESPER_CHECK_TIMEOUT);

	}
}
