package dst.ass3.jms;

import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.log;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass3.AbstractJMSTest;
import dst.ass3.dto.ClassifyLectureWrapperDTO;
import dst.ass3.dto.LectureWrapperDTO;
import dst.ass3.jms.scheduler.IScheduler.ISchedulerListener;
import dst.ass3.jms.virtualschool.IVirtualSchool.IVirtualSchoolListener;
import dst.ass3.model.LectureType;
import dst.ass3.model.LifecycleState;

/**
 * This test performs the following tasks: ASSIGN and DENY.
 * 
 * <pre>
 * Timing diagram                                                                         
 *                                                                                        
 *    0  1  2 [sec]                                                                       
 *    |--|--|-->                                                                          
 * L1 D                                                                                   
 *    ^     ^                                                                             
 *    CP1   CP2                                                                            
 *                                                                                        
 * D: Lecture denied by VirtualSchool                                                              
 *                                                                                        
 * L1: lecture1                                                                              
 *                                                                                        
 * CP1: Check-Point 1 - Assign lecture1
 * CP2: Check-Point 2 - [Scheduler = ASSIGNED, DENIED] [VirtualSchool = DENIED]
 * </pre>
 */
public class Test1 extends AbstractJMSTest {

	private AtomicInteger virtualSchoolEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEvent = new AtomicInteger(0);

	private long lectureId = 10;

	private long lectureWrapperId = -1;

	private long startTime;

	private Semaphore sem;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_AssignAndDeny() {
		sem = new Semaphore(0);
		vs1.start();
		vs2.start();
		scheduler.start();
		c1.start();

		IVirtualSchoolListener virtualSchoolListener = new IVirtualSchoolListener() {
			@Override
			public LectureWrapperDecideResponse decideLecture(ClassifyLectureWrapperDTO lectureWrapper,
					String virtualSchoolName) {
				logTimed("** virtualSchool " + virtualSchoolName + " lectureWrapper: " + lectureWrapper,
						startTime);
				virtualSchoolEvent.incrementAndGet();

				assertEquals("only 1 raised event expected", 1,
						virtualSchoolEvent.get());

				assertNotNull("lectureWrapper.lectureId = null", lectureWrapper.getLectureId());
				assertEquals("lectureId wrong", lectureId, lectureWrapper.getLectureId().longValue());

				assertNotNull("lectureWrapper.id = null", lectureWrapper.getId());

				log("SETTING ID " + lectureWrapper.getId());
				lectureWrapperId = lectureWrapper.getId();
				sem.release();
				return new LectureWrapperDecideResponse(LectureWrapperResponse.DENY, null);
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, LectureWrapperDTO lectureWrapper) {
				logTimed("** scheduler: type=" + type + " lectureWrapper: " + lectureWrapper,
						startTime);

				sleep(SHORT_WAIT); // wait short time for updated lectureId

				assertEquals("lectureId in server response DTO wrong "
						+ schedulerEvent, lectureId, lectureWrapper.getLectureId().longValue());
				assertEquals("lectureWrapperId in server response DTO wrong"
						+ schedulerEvent, lectureWrapperId, lectureWrapper.getId().longValue());

				switch (schedulerEvent.get()) {
				case 0:
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event != ASSIGNED",
							LifecycleState.ASSIGNED, lectureWrapper.getState());
					assertEquals("1st event lecture type != UNCLASSIFIED",
							LectureType.UNCLASSIFIED, lectureWrapper.getType());
					break;
				case 1:
					assertEquals("2nd event of wrong type", InfoType.DENIED,
							type);
					assertEquals("2nd event != STREAMING_NOT_POSSIBLE",
							LifecycleState.STREAMING_NOT_POSSIBLE,
							lectureWrapper.getState());
					assertEquals("2nd event lecture type != UNCLASSIFIED",
							LectureType.UNCLASSIFIED, lectureWrapper.getType());
					break;
				default:
					fail("only 2 events expected");
					break;
				}
				schedulerEvent.incrementAndGet();
				sem.release();
			}
		};

		sleep(SHORT_WAIT); // Wait for old messages being discarded.
		startTime = new Date().getTime();

		// ---------------- CP1 ------------------------
		logCheckpoint(1, startTime);

		vs1.setVirtualSchoolListener(virtualSchoolListener);
		vs2.setVirtualSchoolListener(virtualSchoolListener);

		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + lectureId + "...");
		scheduler.assign(lectureId);

		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);
		assure(sem,
				3,
				"did not get 3 events (Scheduler: create, denied; VirtualSchool: classify) in time",
				DEFAULT_CHECK_TIMEOUT);
		assertEquals("wrong count of scheduler events ", 2,
				schedulerEvent.get());
		assertEquals("wrong count of virtualSchool events ", 1, virtualSchoolEvent.get());
	}

	@After
	public void shutdown() {
		// disable all listeners
		vs1.setVirtualSchoolListener(null);
		vs2.setVirtualSchoolListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down " + AbstractJMSTest.CLASSROOM1_NAME + "...");
		c1.stop();
		log("shutting down " + AbstractJMSTest.VIRTUALSCHOOL1_NAME + "...");
		vs1.stop();
		log("shutting down " + AbstractJMSTest.VIRTUALSCHOOL2_NAME + "...");
		vs2.stop();
		log("shutting down Scheduler...");
		scheduler.stop();
		
		super.shutdown();
	}
}
