package dst.ass3.event;

import static dst.ass3.EventingUtils.ESPER_CHECK_TIMEOUT;
import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.assureMin;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
 * Checks Avg Query with a time window of 15sec.
 */
public class Test5 extends AbstractEventTest {

	private Semaphore semDuration;
	private Semaphore semAvg;

	private final int lectureCount = 20;

	private ArrayList<ILectureWrapper> running;

	private Map<Long, Long> idDurationMap;
	private Map<Long, Long> esperDuration;

	private int currentFailCount = 0;
	// We accept a fail-count of 10% due to timing issues. The calculation for
	// the allowed number of fail-counts is as follows:
	// allowedNumberOfFailCounts = lectureCount / acceptedFailCountFactor =
	// = lectureCount / 10 = lectureCount * 0.1
	private final int acceptedFailCountFactor = 10;

	@Test
	public void test_AverageQueryWithWindow() {
		final long startTime = System.currentTimeMillis();
		
		semDuration = new Semaphore(0);
		semAvg = new Semaphore(0);

		/**
		 * store the pair <received time>:<received duration> value to check
		 * calculation of esper avg query
		 */
		esperDuration = new HashMap<Long, Long>();

		running = new ArrayList<ILectureWrapper>();

		/**
		 * store the pair <lectureId>:<starttime> to compare with the duration
		 * field of the LectureDuration event
		 */
		idDurationMap = new HashMap<Long, Long>();

		test.initializeAll(new StatementAwareUpdateListener() {

			@Override
			public synchronized void update(EventBean[] newEvents,
					EventBean[] oldEvents, EPStatement s, EPServiceProvider p) {
				Long duration = null;
				long current = System.currentTimeMillis();
				synchronized (idDurationMap) {
					for (EventBean e : newEvents) {
						// System.out.println("LISTENER:"+
						// e.getEventType().getName() + " " +
						// e.getUnderlying());
						String name = e.getEventType().getName();
						if (name.equals(Constants.EVENT_LECTURE_ASSIGNED)
								|| name.equals(Constants.EVENT_LECTURE_STREAMED)) {
							return;
						}
						if (name.equals(Constants.EVENT_LECTURE_DURATION)) {
							/*
							 * LectureDuration Event received. Compare duration
							 * value with self calculated value in idDurationMap
							 * store esper duration value in esperDuration for
							 * avg calculation
							 */
							duration = EventingUtils.getLong(e, Constants.EVENT_PROP_DURATION);
							Long lectureId = EventingUtils.getLong(e, Constants.EVENT_PROP_LECTURE_ID);

							Long expected = idDurationMap.get(lectureId);

							if (expected < 0) {
								fail("expected < 0 ! " + lectureId);
								return;
							}

							/*
							 * ensureRange will not return control if range is
							 * violated failCount needs to be counted here. So
							 * failure is expected and in case of no failure
							 * failcount value is restored
							 */
							currentFailCount++;
							if (EventingUtils.ensureRange(lectureId + " duration",
									expected, duration, allowedInaccuracy,
									false)) {
								// no failure has occurred so withdraw the
								// "borrowed" failcount
								currentFailCount--;
							}
							esperDuration.put(current, duration);
							System.out.println("Duration " + duration);

							semDuration.release();
						} else {
							/*
							 * Avg Event received. Calculate 15sec time window
							 * and sum up self recorded events from
							 * esperDuration
							 */
							Double avgDuration = EventingUtils.getDouble(e,
									Constants.EVENT_AVG_LECTURE_DURATION);

							long fifteenSecsAgo = current - 15 * 1000 + 100;
							double sum = 0;
							long count = 0;
							for (Long time : esperDuration.keySet()) {
								if (time > fifteenSecsAgo) {
									count++;
									sum += esperDuration.get(time);
								}
							}
							Double expected = sum / count;
							System.out.println("avgDuration: "
									+ avgDuration.longValue() + " expected "
									+ expected.longValue());

							/*
							 * ensureRange will not return control if range is
							 * violated failCount needs to be counted here. So
							 * failure is expected and in case of no failure
							 * failcount value is restored
							 */
							currentFailCount++;
							if (EventingUtils.ensureRange(
									"avg 15 seconds ago does not match",
									expected.longValue(),
									avgDuration.longValue(), 10, false)) {
								// no failure has occurred so withdraw the
								// "borrowed" failcount
								currentFailCount--;
							}
							semAvg.release();
						}
					}
				}

			}
		}, false);

		/*
		 * start adding ASSIGNED events to esper with some randomness (delayed
		 * by factor 0.7). Then start picking random running lectures and streamed
		 * them.
		 */
		for (int i = 0; i < lectureCount; i++) {
			ILectureWrapper tmp = null;
			if (running.size() < lectureCount) {
				tmp = EventingFactory.createLectureWrapper(i + 1L, i + 1L,
						LifecycleState.ASSIGNED, CLASSIFIEDBY, LectureType.PRESENTATION);
				test.addEvent(tmp);
				running.add(tmp);
				synchronized (idDurationMap) {
					idDurationMap.put(tmp.getLectureId(),
							-1 * System.currentTimeMillis());
				}
				System.out.println("add " + tmp.getLectureId());
			}
			EventingUtils.sleepRandom(50 * 1000 / lectureCount); // total < 50sec
			int randomStop = (int) (Math.random() * (running.size() + lectureCount * 0.7));
			if (running.size() - 1 >= randomStop) {
				process(randomStop);
				running.remove(randomStop); // remove from running
			}
		}

		logTimed("stopping running", startTime);
		for (int i = 0; i < running.size(); i++) {
			process(i);
			EventingUtils.sleepRandom(10 * 1000 / running.size()); // delay stop
																	// <= 10sec
		}

		sleep(SHORT_WAIT); // wait for all events
		logTimed("checking results", startTime);

		assure(semDuration, lectureCount, lectureCount
				+ " lectureDuration event expected", ESPER_CHECK_TIMEOUT);

		// accept >= 10 events (esper may emit additional Avg Events even if no
		// new LectureDuration events have been received
		// this happens because the time window moves and some values slide out
		assureMin(semAvg, lectureCount, lectureCount + " avgDuration event expected",
				0, ESPER_CHECK_TIMEOUT, false);

		// we accept a fail-count of 10% due to timing issues
		if (lectureCount / acceptedFailCountFactor < currentFailCount) {
			fail("fail count too high! " + currentFailCount + "/" + lectureCount);
		} else {
			logTimed("done, failcount OK: " + currentFailCount + "/"
					+ lectureCount, startTime);
		}
	}

	/**
	 * switches a Lecture to streamed stores the time it was running (to compare
	 * later with esper events) and notify esper
	 * 
	 * @param index
	 */
	private void process(int index) {
		ILectureWrapper tmp = running.get(index);
		tmp.setState(LifecycleState.STREAMED);
		synchronized (idDurationMap) {
			idDurationMap.put(
					tmp.getLectureId(),
					idDurationMap.get(tmp.getLectureId())
							+ System.currentTimeMillis());
			System.out.println("\nstop " + tmp.getLectureId() + " Duration:"
					+ idDurationMap.get(tmp.getLectureId()));
			test.addEvent(tmp);
			sleep(100); // short wait for esper events
		}
	}
}
