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
 * This test performs the following tasks: ASSIGN, CLASSIFY as PRESENTATION and perform INFO
 * 2 times.
 * 
 * <pre>
 * Timing diagram
 * 
 *    0  1  2  3  4 [sec]                                                                 
 *    |--|--|--|--|-->                                                                    
 * L1 ###################...                                                              
 * I1       *                                                                             
 * I2       *                                                                             
 *    ^     ^     ^                                                                       
 *    CP1   CP2   CP3                                                                      
 *                                                                                        
 * ##: waiting for processing (no computer listener = computer discards message)
 * 
 * L1: lecture1
 * I1,I2: Info Request 1,2
 * CP1: Check-Point 1 - assign lecture1, VirtualSchool accepts as PRESENTATION                                               
 * CP2: Check-Point 2 - request info 2x [Scheduler = ASSIGN]                                               
 * CP3: Check-Point 3 - info finished  [Scheduler = INFO, INFO]
 * </pre>
 */
public class Test3 extends AbstractJMSTest {

	private AtomicInteger virtualSchoolEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEvent = new AtomicInteger(0);

	private long lectureId1 = 30;
	private long lectureWrapperId1 = -1;

	private String classifiedBy = null;
	private long startTime;

	private Semaphore sem;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_AssignTypeAndInfo() {
		sem = new Semaphore(0);

		vs1.start();
		vs2.start();
		scheduler.start();
		c2.start();
		c4.start();

		IVirtualSchoolListener virtualSchoolListener1 = new IVirtualSchoolListener() {
			@Override
			public LectureWrapperDecideResponse decideLecture(
					ClassifyLectureWrapperDTO lectureWrapper, String virtualSchoolName) {
				logTimed("** virtualSchool " + virtualSchoolName + " lectureWrapper: " + lectureWrapper,
						startTime);

				virtualSchoolEvent.incrementAndGet();

				assertNotNull("lectureWrapper.lectureId = null", lectureWrapper.getLectureId());
				assertNotNull("lectureWrapper.id = null", lectureWrapper.getId());

				assertEquals("lectureId wrong", lectureId1, lectureWrapper.getLectureId()
						.longValue());

				log("SETTING ID " + lectureWrapper.getId());
				lectureWrapperId1 = lectureWrapper.getId();

				classifiedBy = VIRTUALSCHOOL1_NAME;
				assertEquals("reported virtualSchool wrong", classifiedBy, virtualSchoolName);

				sem.release();
				return new LectureWrapperDecideResponse(
						LectureWrapperResponse.ACCEPT, LectureType.PRESENTATION);
			}
		};

		IVirtualSchoolListener virtualSchoolListener2 = new IVirtualSchoolListener() {
			@Override
			public LectureWrapperDecideResponse decideLecture(
					ClassifyLectureWrapperDTO lectureWrapper, String virtualSchoolName) {
				logTimed("** virtualSchool " + virtualSchoolName + " lectureWrapper: " + lectureWrapper,
						startTime);

				virtualSchoolEvent.incrementAndGet();

				assertNotNull("lectureWrapper.lectureId = null", lectureWrapper.getLectureId());
				assertNotNull("lectureWrapper.id = null", lectureWrapper.getId());

				assertEquals("lectureId wrong", lectureId1, lectureWrapper.getLectureId()
						.longValue());

				log("SETTING ID " + lectureWrapper.getId());
				lectureWrapperId1 = lectureWrapper.getId();

				classifiedBy = VIRTUALSCHOOL2_NAME;
				assertEquals("reported virtualSchool wrong", classifiedBy, virtualSchoolName);

				sem.release();
				return new LectureWrapperDecideResponse(
						LectureWrapperResponse.ACCEPT, LectureType.PRESENTATION);
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, LectureWrapperDTO lectureWrapper) {
				logTimed("** scheduler: type=" + type + " lectureWrapper: " + lectureWrapper,
						startTime);

				sleep(SHORT_WAIT); // wait short time for updated lectureId

				assertEquals("lectureId in server response DTO wrong " + lectureId1,
						lectureId1, lectureWrapper.getLectureId().longValue());
				assertEquals("lectureWrapperId in server response DTO wrong"
						+ schedulerEvent, lectureWrapperId1, lectureWrapper.getId().longValue());

				switch (schedulerEvent.get()) {
				case 0:
					// ASSIGN for lectureId1
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event != ASSIGNED",
							LifecycleState.ASSIGNED, lectureWrapper.getState());
					assertEquals("1st event lecture type != UNCLASSIFIED",
							LectureType.UNCLASSIFIED, lectureWrapper.getType());
					break;
				case 1:
					// INFO 1
					assertEquals("2nd event of wrong type", InfoType.INFO, type);
					assertEquals("2nd event != ASSIGNED",
							LifecycleState.READY_FOR_STREAMING,
							lectureWrapper.getState());
					assertEquals("2nd event lecture type != PRESENTATION",
							LectureType.PRESENTATION, lectureWrapper.getType());
					assertNotNull("2nd classified by == null" + classifiedBy,
							lectureWrapper.getClassifiedBy());
					assertEquals("2nd classified by != " + classifiedBy, classifiedBy,
							lectureWrapper.getClassifiedBy());
					break;
				case 2:
					// INFO 2
					assertEquals("3rd event of wrong type", InfoType.INFO, type);
					assertEquals("3rd event != ASSIGNED",
							LifecycleState.READY_FOR_STREAMING,
							lectureWrapper.getState());
					assertEquals("3rd event lecture type != PRESENTATION",
							LectureType.PRESENTATION, lectureWrapper.getType());
					assertNotNull("3rd classified by == null" + classifiedBy,
							lectureWrapper.getClassifiedBy());
					assertEquals("3rd classified by != " + classifiedBy, classifiedBy,
							lectureWrapper.getClassifiedBy());
					break;
				default:
					fail("only 3 events expected");
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

		vs1.setVirtualSchoolListener(virtualSchoolListener1);
		vs2.setVirtualSchoolListener(virtualSchoolListener2);
		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + lectureId1 + "...");
		scheduler.assign(lectureId1);

		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);
		assure(sem,
				2,
				"did not get 2 events (Scheduler: assign, VirtualSchool: classify) in time",
				DEFAULT_CHECK_TIMEOUT);

		assertEquals("wrong count of virtualSchool events ", 1, virtualSchoolEvent.get());
		assertEquals("wrong count of scheduler events ", 1,
				schedulerEvent.get());

		log("Executing info 2x " + lectureWrapperId1 + "...");
		scheduler.info(lectureWrapperId1);
		scheduler.info(lectureWrapperId1);

		// ---------------- CP3 ------------------------
		logCheckpoint(3, startTime);
		assure(sem, 2, "did not get 2 events (Scheduler: info, info) in time",
				DEFAULT_CHECK_TIMEOUT);

		assertEquals("wrong count of scheduler events ", 3,
				schedulerEvent.get());
		assertEquals("wrong count of virtualSchool events ", 1, virtualSchoolEvent.get());
	}

	@After
	public void shutdown() {
		// disable all listeners
		vs1.setVirtualSchoolListener(null);
		vs2.setVirtualSchoolListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down " + AbstractJMSTest.VIRTUALSCHOOL1_NAME + "...");
		vs1.stop();
		log("shutting down " + AbstractJMSTest.VIRTUALSCHOOL2_NAME + "...");
		vs2.stop();
		log("shutting down " + AbstractJMSTest.CLASSROOM2_NAME + "...");
		c2.stop();
		log("shutting down " + AbstractJMSTest.CLASSROOM4_NAME + "...");
		c4.stop();
		log("shutting down Scheduler...");
		scheduler.stop();
		
		super.shutdown();
	}

}
