package dst.ass3.event;

import static dst.ass3.EventingUtils.ESPER_CHECK_TIMEOUT;
import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Use pattern matching facilities of EPL to detect lectures, which have 3 times
 * attempted to run but failed to be executed (i.e., switched 3 times between
 * the status READY FOR STREAMING and the status STREAMING NOT POSSIBLE).
 * 
 * LOAD Test length: 19 checkpoints
 */
public class Test7 extends AbstractEventTest {

	private Semaphore semSwitches;

	private HashMap<Long, Long> switches;
	private ArrayList<ILectureWrapper> running;

	@Test
	public void test_PatternMatchingQueryComplex() {
		final long startTime = System.currentTimeMillis();

		semSwitches = new Semaphore(0);

		running = new ArrayList<ILectureWrapper>();
		switches = new HashMap<Long, Long>();

		test.initializeAll(new StatementAwareUpdateListener() {

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents,
					EPStatement s, EPServiceProvider p) {
				try {
					for (EventBean e : newEvents) {
						String name = e.getEventType().getName();
						if (name.equals(Constants.EVENT_LECTURE_ASSIGNED)
								|| name.equals(Constants.EVENT_LECTURE_STREAMED)
								|| name.equals(Constants.EVENT_LECTURE_DURATION)) {
							return;
						}

						List<EventBean> eventBeans = EventingUtils
								.getAliasedEventBeans(e);

						for (EventBean lecture : eventBeans) {
							if (lecture != null) {
								LifecycleState state = getLectureStatus(lecture);

								if (state == null
										|| !state
												.equals(LifecycleState.READY_FOR_STREAMING))
									continue;

								System.out.println("lecture id "
										+ lecture.getUnderlying());

								Long lectureId = EventingUtils.getLong(lecture,
										Constants.EVENT_PROP_LECTURE_ID);
								assertTrue(switches.get(lectureId) >= 6L); // 3
																		// cycles
																		// =
																		// 6
																		// switches
								semSwitches.release();

								break;
							}
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, false);

		int total = 200;
		for (int i = 0; i < total; i++) {

			if (i % 10 == 0) {
				// print checkpoint info
				logCheckpoint((i / 10), startTime);
			}

			ILectureWrapper tmp = null;
			if (running.size() < total && Math.random() < 0.2) {
				tmp = EventingFactory.createLectureWrapper(running.size() + 1L,
						i + 1L, LifecycleState.STREAMING_NOT_POSSIBLE, "tf1",
						LectureType.PRESENTATION);
				test.addEvent(tmp);
				running.add(tmp);
				switches.put(tmp.getLectureId(), 0L);
			}
			int randomFlip = (int) (Math.random() * (running.size()));
			if (running.size() - 1 >= randomFlip && i > total / 10) {
				// start flipping only after total/10 rounds
				flipStatus(randomFlip);
			}
		}

		sleep(SHORT_WAIT); // wait for all events

		int expected = 0;
		for (Long val : switches.values()) {
			expected += val / 6;
		}
		logTimed("checking results: expecting " + expected + " switch events",
				startTime);
		assure(semSwitches, expected, expected
				+ " lectureDuration events expected!", ESPER_CHECK_TIMEOUT);
	}

	/**
	 * change from STREAMING_NOT_POSSIBLE -> READY_FOR_STREAMING and vice
	 * versa also tracks the number of switches in {@code this.switches}
	 * 
	 * @param index
	 *            the index in the running list
	 */
	private void flipStatus(int index) {
		ILectureWrapper tmp = running.get(index);

		if (tmp.getState() == LifecycleState.STREAMING_NOT_POSSIBLE) {
			tmp.setState(LifecycleState.READY_FOR_STREAMING);
		} else {
			tmp.setState(LifecycleState.STREAMING_NOT_POSSIBLE);
		}

		synchronized (switches) {
			switches.put(tmp.getLectureId(), switches.get(tmp.getLectureId()) + 1L);
		}
		System.out.println("" + tmp.getLectureId() + " : "
				+ switches.get(tmp.getLectureId()));
		test.addEvent(tmp);
	}
}
