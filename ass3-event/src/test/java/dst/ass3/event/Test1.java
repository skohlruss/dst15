package dst.ass3.event;

import static dst.ass3.EventingUtils.ESPER_CHECK_TIMEOUT;
import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

import dst.ass3.AbstractEventTest;
import dst.ass3.model.LectureType;
import dst.ass3.model.ILectureWrapper;
import dst.ass3.model.LifecycleState;

/**
 * Basic Test: Adds an ASSIGNED lecture and then changes the Lecture to Streamed and
 * waits for a LectureDuration Event.
 * 
 */
public class Test1 extends AbstractEventTest {

	private Semaphore sem;

	@Test
	public void test_LectureDurationEvent() {
		final long startTime = System.currentTimeMillis();
		sem = new Semaphore(0);

		ILectureWrapper l1 = EventingFactory.createLectureWrapper(101L, 111L,
				LifecycleState.ASSIGNED, CLASSIFIEDBY, LectureType.PRESENTATION);

		test.initializeAll(new StatementAwareUpdateListener() {

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents,
					EPStatement s, EPServiceProvider p) {

				for (EventBean e : newEvents) {
					String name = e.getEventType().getName();
					if (name.equals(Constants.EVENT_LECTURE_DURATION)) {
						sem.release();
					}
					System.out.println("LISTENER " + e.getEventType().getName()
							+ " " + e.getUnderlying());
				}

			}
		}, false);

		sleep(SHORT_WAIT);
		logCheckpoint(0, startTime);
		test.addEvent(l1);

		logCheckpoint(1, startTime);
		l1.setState(LifecycleState.STREAMED);
		test.addEvent(l1);

		logTimed("checking results", startTime);
		assure(sem, 1, "1 lectureDuration event expected!", ESPER_CHECK_TIMEOUT);

	}
}
