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
import dst.ass3.dto.StreamLectureWrapperDTO;
import dst.ass3.dto.ClassifyLectureWrapperDTO;
import dst.ass3.dto.LectureWrapperDTO;
import dst.ass3.jms.scheduler.IScheduler.ISchedulerListener;
import dst.ass3.jms.virtualschool.IVirtualSchool.IVirtualSchoolListener;
import dst.ass3.jms.classroom.IClassroom.IClassroomListener;
import dst.ass3.model.LectureType;
import dst.ass3.model.LifecycleState;

/**
 * This test performs the following tasks: ASSIGN, CLASSIFY as PRESENTATION and STREAM it.
 * 
 * <pre>
 * Timing diagram
 * 
 *    0  1  2  3  4                                                                       
 *    |--|--|--|--|-->                                                                    
 * L1 *******                                                                             
 * I1          *                                                                           
 *    ^     ^  ^  ^                                                                       
 *    CP1   CP2+3 CP4                                                                      
 *                                                                                        
 * **: Running                                                                            
 *                                                                                        
 * L1: takes 2sec to finish                                                               
 * I1: InfoRequest for LectureWrapper 1                                                              
 *                                                                                        
 * CP1: Check-Point 1 - Assign Lecture 1                                                                       
 * CP2: Check-Point 2 - Wait till lecture 1 has finished                         
 * CP3: Check-Point 3 - Lecture1 should have finished so send INFO Request                                                    
 * CP4: Check-Point 4 - Info Request completed [Scheduler = ASSIGN, STREAMED, INFO events]
 * </pre>
 */
public class Test5 extends AbstractJMSTest {

	private AtomicInteger virtualSchoolEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEvent = new AtomicInteger(0);
	private AtomicInteger streamed = new AtomicInteger(0);

	private long lectureId1 = 50;
	private long lectureWrapperId1 = -1;

	private String classifiedBy = null;
	private long startTime;

	private Semaphore sem;
	private Semaphore semClassroom;

	private final int STREAMING_TIME = 2000;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_AssignTypeAndStream2() {
		sem = new Semaphore(0);
		semClassroom = new Semaphore(0);
		vs1.start();
		scheduler.start();
		c2.start();

		IVirtualSchoolListener virtualSchoolListener = new IVirtualSchoolListener() {
			@Override
			public LectureWrapperDecideResponse decideLecture(
					ClassifyLectureWrapperDTO lectureWrapper, String virtualSchoolName) {
				logTimed("** virtualSchool " + virtualSchoolName + " classroom: "
						+ lectureWrapper, startTime);

				assertNotNull("lectureWrapper.lectureId = null",
						lectureWrapper.getLectureId());
				assertNotNull("lectureWrapper.id = null", lectureWrapper.getId());

				assertEquals("lectureId wrong", lectureId1, lectureWrapper.getLectureId()
						.longValue());

				log("SETTING ID " + lectureWrapper.getId());
				lectureWrapperId1 = lectureWrapper.getId();
				classifiedBy = virtualSchoolName;

				virtualSchoolEvent.incrementAndGet();
				sem.release();

				return new LectureWrapperDecideResponse(
						LectureWrapperResponse.ACCEPT, LectureType.PRESENTATION);
			}
		};

		IClassroomListener classroomListener = new IClassroomListener() {
			@Override
			public void waitTillStreamed(StreamLectureWrapperDTO lectureWrapper,
					String classroomName, LectureType acceptedType,
					String virtualSchoolName) {
				logTimed("** classroom " + classroomName + " lectureWrapper: "
						+ lectureWrapper, startTime);

				assertEquals("virtualSchoolName", classifiedBy, virtualSchoolName);
				assertEquals("classroomName", CLASSROOM2_NAME, classroomName);
				assertEquals("lecturetype", LectureType.PRESENTATION,
						acceptedType);

				sleep(STREAMING_TIME); // simulate streaming

				streamed.incrementAndGet();

				semClassroom.release();
				assertEquals("classroom listener - too many events", 1,
						streamed.get());
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, LectureWrapperDTO lectureWrapper) {
				logTimed("** scheduler: type=" + type + " lectureWrapper: "
						+ lectureWrapper, startTime);
				sleep(SHORT_WAIT); // wait short time for updated lectureId

				assertEquals("lectureId in server response DTO wrong " + lectureId1,
						lectureId1, lectureWrapper.getLectureId().longValue());

				assertEquals("lectureWrapperId in server response DTO wrong"
						+ schedulerEvent, lectureWrapperId1, lectureWrapper.getId()
						.longValue());

				switch (schedulerEvent.get()) {
				case 0:
					// ASSIGN
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event != ASSIGNED",
							LifecycleState.ASSIGNED, lectureWrapper.getState());
					assertEquals("1st event lecture type != UNCLASSIFIED",
							LectureType.UNCLASSIFIED, lectureWrapper.getType());
					break;
				case 1:
					// STREAMED
					assertEquals("2nd event of wrong type", InfoType.STREAMED,
							type);
					assertEquals("2nd event != ASSIGNED",
							LifecycleState.STREAMED, lectureWrapper.getState());
					assertEquals("2nd event lecture type ", LectureType.PRESENTATION,
							lectureWrapper.getType());
					assertNotNull("2nd classified by == null " + classifiedBy,
							lectureWrapper.getClassifiedBy());
					assertEquals("2nd classified by != " + classifiedBy, classifiedBy,
							lectureWrapper.getClassifiedBy());
					break;
				case 2:
					// INFO
					assertEquals("3rd event of wrong type", InfoType.INFO, type);
					assertEquals("3rd event != ASSIGNED",
							LifecycleState.STREAMED, lectureWrapper.getState());
					assertEquals("3rd event lecture type ", LectureType.PRESENTATION,
							lectureWrapper.getType());
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

		vs1.setVirtualSchoolListener(virtualSchoolListener);
		scheduler.setSchedulerListener(schedulerListener);
		c2.setClassroomListener(classroomListener);

		log("Assigning " + lectureId1 + "...");
		scheduler.assign(lectureId1);

		assure(sem,
				2,
				"did not get 2 events (Scheduler: create; VirtualSchool: classify) in time",
				DEFAULT_CHECK_TIMEOUT);
		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);

		assertEquals("wrong count of virtualSchool events ", 1,
				virtualSchoolEvent.get());
		assertEquals("wrong count of scheduler events ", 1,
				schedulerEvent.get());

		assure(semClassroom,
				1,
				"did not get 1 event (Classroom: finished streaming) in time",
				DEFAULT_CHECK_TIMEOUT + STREAMING_TIME / 1000);
		assure(sem, 1, "did not get 1 event (Scheduler: streamed) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP3 ------------------------
		logCheckpoint(3, startTime);
		assertEquals("wrong count of classroom events", 1, streamed.get());
		assertEquals("wrong count of scheduler events ", 2,
				schedulerEvent.get());
		assertEquals("wrong count of virtualSchool events ", 1,
				virtualSchoolEvent.get());

		log("Executing info " + lectureWrapperId1 + "...");
		scheduler.info(lectureWrapperId1);

		assure(sem, 1, "did not get 1 event (Scheduler: info) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP4 ------------------------
		logCheckpoint(4, startTime);

		assertEquals("wrong count of scheduler events ", 3,
				schedulerEvent.get());
		assertEquals("wrong count of virtualSchool events ", 1,
				virtualSchoolEvent.get());
	}

	@After
	public void shutdown() {
		// disable all listeners
		vs1.setVirtualSchoolListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down " + AbstractJMSTest.CLASSROOM2_NAME + "...");
		c2.stop();
		log("shutting down " + AbstractJMSTest.VIRTUALSCHOOL1_NAME + "...");
		vs1.stop();
		log("shutting down Scheduler...");
		scheduler.stop();

		super.shutdown();
	}

}
