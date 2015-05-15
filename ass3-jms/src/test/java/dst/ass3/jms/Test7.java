package dst.ass3.jms;

import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.log;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
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
 * This test performs the following tasks: Assign 3 Lectures, accept and stream
 * all of them.
 * 
 * <pre>
 * Timing diagram
 * 
 *      0  1  2  3  4  5  6  7  8  9  10 [sec]                                     
 *      |--|--|--|--|--|--|--|--|--|--|-->                                         
 * L1/2 ******************                                                         
 * L3         ******************                                                   
 *      ^     ^     ^        ^        ^                                            
 *      CP1   CP2   CP3      CP4      CP5                                           
 *                                                                                 
 * Each Lecture needs ****************** (=6sec) for streaming                        
 * CP1: Check-Point 1 - Assign lecture1, lecture2                                                           
 * CP2: Check-Point 2 - Assign lecture3                                                                 
 * CP3: Check-Point 3 - 1-3 running                                                                 
 * CP4: Check-Point 4 - 1-2 STREAMED, 3 running                                                    
 * CP5: Check-Point 5 - 3 STREAMED
 * </pre>
 */
public class Test7 extends AbstractJMSTest {

	private AtomicInteger virtualSchoolEventLecture1 = new AtomicInteger(0);
	private AtomicInteger virtualSchoolEventLecture2 = new AtomicInteger(0);
	private AtomicInteger virtualSchoolEventLecture3 = new AtomicInteger(0);

	private AtomicInteger schedulerEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEventLecture1 = new AtomicInteger(0);
	private AtomicInteger schedulerEventLecture2 = new AtomicInteger(0);
	private AtomicInteger schedulerEventLecture3 = new AtomicInteger(0);

	private AtomicInteger classroomEvent = new AtomicInteger(0);
	private AtomicInteger classroomEventLecture1 = new AtomicInteger(0);
	private AtomicInteger classroomEventLecture2 = new AtomicInteger(0);
	private AtomicInteger classroomEventLecture3 = new AtomicInteger(0);

	private long lectureId1 = 70;
	private long lectureId2 = 71;
	private long lectureId3 = 72;
	private long lectureWrapperId1 = -1;
	private long lectureWrapperId2 = -1;
	private long lectureWrapperId3 = -1;

	private String lecture1ClassifiedBy;
	private String lecture2ClassifiedBy;
	private String lecture3ClassifiedBy;
	private long startTime;

	private final int STREAMING_TIME = 6000;

	private Semaphore sem;
	private Semaphore semClassroom;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_TypeAssignClassifyAndStream() {
		sem = new Semaphore(0);
		semClassroom = new Semaphore(0);

		vs1.start();
		vs2.start();
		scheduler.start();
		c1.start();
		c2.start();
		c3.start();
		c4.start();

		IVirtualSchoolListener virtualSchoolListener = new IVirtualSchoolListener() {
			@Override
			public LectureWrapperDecideResponse decideLecture(ClassifyLectureWrapperDTO lectureWrapper,
					String virtualSchoolName) {
				logTimed("** virtualSchool " + virtualSchoolName + " lecture: " + lectureWrapper,
						startTime);
				if (lectureWrapper.getLectureId() == lectureId1) {
					virtualSchoolEventLecture1.incrementAndGet();
					lectureWrapperId1 = lectureWrapper.getId();
					lecture1ClassifiedBy = virtualSchoolName;
					return new LectureWrapperDecideResponse(LectureWrapperResponse.ACCEPT,
							LectureType.PRESENTATION);
				}
				if (lectureWrapper.getLectureId() == lectureId2) {
					virtualSchoolEventLecture2.incrementAndGet();
					lectureWrapperId2 = lectureWrapper.getId();
					lecture2ClassifiedBy = virtualSchoolName;
					return new LectureWrapperDecideResponse(LectureWrapperResponse.ACCEPT,
							LectureType.INTERACTIVE);
				}
				if (lectureWrapper.getLectureId() == lectureId3) {
					virtualSchoolEventLecture3.incrementAndGet();
					lectureWrapperId3 = lectureWrapper.getId();
					lecture3ClassifiedBy = virtualSchoolName;
					return new LectureWrapperDecideResponse(LectureWrapperResponse.ACCEPT,
							LectureType.INTERACTIVE);
				}

				fail("virtualSchool Events - unknown type");
				return new LectureWrapperDecideResponse(LectureWrapperResponse.DENY, null);
			}
		};

		IClassroomListener classroomListener = new IClassroomListener() {
			@Override
			public void waitTillStreamed(StreamLectureWrapperDTO lectureWrapper,
					String classroomName, LectureType acceptedType,
					String virtualSchoolName) {
				logTimed("** classroom " + classroomName + " lecture: " + lectureWrapper,
						startTime);

				if (lectureWrapper.getId() == lectureWrapperId1) {
					classroomEventLecture1.incrementAndGet();
					assertEquals("classroomListener 1 lectureWrapperId", lectureWrapperId1, lectureWrapper
							.getId().longValue());
					assertEquals("classroomListener 1 lectureId", lectureId1, lectureWrapper
							.getLectureId().longValue());
					assertEquals("classroomListener 1 type",
							LectureType.PRESENTATION, lectureWrapper.getType());
					assertEquals("classroomListener 1 classifiedby", lecture1ClassifiedBy,
							lectureWrapper.getClassifiedBy());
				}
				if (lectureWrapper.getId() == lectureWrapperId2) {
					classroomEventLecture2.incrementAndGet();
					assertEquals("classroomListener 2 lectureWrapperId", lectureWrapperId2, lectureWrapper
							.getId().longValue());
					assertEquals("classroomListener 2 lectureId", lectureId2, lectureWrapper
							.getLectureId().longValue());
					assertEquals("classroomListener 2 type",
							LectureType.INTERACTIVE, lectureWrapper.getType());
					assertEquals("classroomListener 2 classifiedby", lecture2ClassifiedBy,
							lectureWrapper.getClassifiedBy());
				}
				if (lectureWrapper.getId() == lectureWrapperId3) {
					classroomEventLecture3.incrementAndGet();
					assertEquals("classroomListener 3 lectureWrapperId", lectureWrapperId3, lectureWrapper
							.getId().longValue());
					assertEquals("classroomListener 3 lectureId", lectureId3, lectureWrapper
							.getLectureId().longValue());
					assertEquals("classroomListener 3 type",
							LectureType.INTERACTIVE, lectureWrapper.getType());
					assertEquals("classroomListener 3 classifiedby", lecture3ClassifiedBy,
							lectureWrapper.getClassifiedBy());
				}

				classroomEvent.incrementAndGet();
				sem.release();

				sleep(STREAMING_TIME);

				semClassroom.release();
				logTimed("finish " + classroomName, startTime);
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, LectureWrapperDTO lectureWrapper) {
				logTimed("** scheduler: type=" + type + " lecture: " + lectureWrapper,
						startTime);
				sleep(SHORT_WAIT); // wait short time for updated lectureWrapperId
				if (lectureWrapper.getLectureId() == lectureId1) {
					assertEquals("lectureWrapperId in server response DTO wrong"
							+ schedulerEvent, lectureWrapperId1, lectureWrapper.getId().longValue());
					schedulerEventLecture1.incrementAndGet();
				}
				if (lectureWrapper.getLectureId() == lectureId2) {
					assertEquals("lectureWrapperId in server response DTO wrong Lecture2 "
							+ schedulerEvent, lectureWrapperId2, lectureWrapper.getId().longValue());
					schedulerEventLecture2.incrementAndGet();
				}
				if (lectureWrapper.getLectureId() == lectureId3) {
					assertEquals("lectureWrapperId in server response DTO wrong Lecture3 "
							+ schedulerEvent, lectureWrapperId3, lectureWrapper.getId().longValue());
					schedulerEventLecture3.incrementAndGet();
				}

				switch (schedulerEvent.get()) {
				case 0:
					// ASSIGN for lectureId1 / lectureId2
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event wrong", LifecycleState.ASSIGNED,
							lectureWrapper.getState());
					assertEquals("1st event lecture type wrong",
							LectureType.UNCLASSIFIED, lectureWrapper.getType());
					break;
				case 1:
					// ASSIGN for lectureId1 / lectureId2
					assertEquals("2nd event of wrong type", InfoType.CREATED,
							type);
					assertEquals("2nd event wrong", LifecycleState.ASSIGNED,
							lectureWrapper.getState());
					assertEquals("2nd event lecture type wrong",
							LectureType.UNCLASSIFIED, lectureWrapper.getType());
					break;
				case 2:
					// ASSIGN for lectureId3
					assertEquals("3rd event of wrong type", InfoType.CREATED,
							type);
					assertEquals("3rd event wrong", LifecycleState.ASSIGNED,
							lectureWrapper.getState());
					assertEquals("3rd event lecture type wrong",
							LectureType.UNCLASSIFIED, lectureWrapper.getType());
					break;
				case 3:
					// STREAMED 1-2
					assertEquals("4th event of wrong type", InfoType.STREAMED,
							type);
					assertEquals("4th event wrong", LifecycleState.STREAMED,
							lectureWrapper.getState());
					assertNotSame("4th event lecture type wrong",
							LectureType.UNCLASSIFIED, lectureWrapper.getType());
					break;
				case 4:
					// STREAMED 1-2
					assertEquals("5th event of wrong type", InfoType.STREAMED,
							type);
					assertEquals("5th event wrong", LifecycleState.STREAMED,
							lectureWrapper.getState());
					assertNotSame("5th event lecture type wrong",
							LectureType.UNCLASSIFIED, lectureWrapper.getType());
					break;
				case 5:
					// STREAMED 3
					assertEquals("6th event of wrong type", InfoType.STREAMED,
							type);
					assertEquals("6th wrong lecture", lectureId3, lectureWrapper.getLectureId()
							.longValue());
					assertEquals("6th event wrong", LifecycleState.STREAMED,
							lectureWrapper.getState());
					assertEquals("6th event lecture type ",
							LectureType.INTERACTIVE, lectureWrapper.getType());
					assertEquals("6th event classified by", lecture3ClassifiedBy,
							lectureWrapper.getClassifiedBy());
					break;
				default:
					fail("only 6 events expected");
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

		c1.setClassroomListener(classroomListener);
		c2.setClassroomListener(classroomListener);
		c3.setClassroomListener(classroomListener);
		c4.setClassroomListener(classroomListener);

		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + lectureId1 + "...");
		scheduler.assign(lectureId1);
		log("Assigning " + lectureId2 + "...");
		scheduler.assign(lectureId2);

		assure(sem,
				4,
				"did not receive 4 events (Scheduler: create, create; Classroom: pre-processing, pre-processing) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);

		assertEquals("wrong count of scheduler events ", 2,
				schedulerEvent.get());
		assertEquals("wrong count of scheduler 1 events ", 1,
				schedulerEventLecture1.get());
		assertEquals("wrong count of scheduler 2 events ", 1,
				schedulerEventLecture2.get());
		assertEquals("wrong count of classroom 1 events ", 1,
				classroomEventLecture1.get());
		assertEquals("wrong count of classroom 2 events ", 1,
				classroomEventLecture2.get());
		assertEquals("wrong count of classroom 3 events ", 0,
				classroomEventLecture3.get());

		sleep(2000); // 2 sec delayed startup
		scheduler.assign(lectureId3);

		assure(sem, 2, "did not receive 2 event (Scheduler: create; Classroom: pre-processing) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP3 ------------------------
		logCheckpoint(3, startTime);

		assertEquals("wrong count of classroom events ", 3, classroomEvent.get());

		assertEquals("wrong count of scheduler events ", 3,
				schedulerEvent.get());
		assertEquals("wrong count of scheduler 1 events ", 1,
				schedulerEventLecture1.get());
		assertEquals("wrong count of scheduler 2 events ", 1,
				schedulerEventLecture2.get());
		assertEquals("wrong count of scheduler 3 events ", 1,
				schedulerEventLecture3.get());
		assertEquals("wrong count of classroom 1 events ", 1,
				classroomEventLecture1.get());
		assertEquals("wrong count of classroom 2 events ", 1,
				classroomEventLecture2.get());
		assertEquals("wrong count of classroom 3 events ", 1,
				classroomEventLecture3.get());

		// Lecture 1 and 2 need some time to finish
		assure(semClassroom,
				2,
				"did not receive 2 events (Classroom: finished streaming, finished streaming) in time",
				DEFAULT_CHECK_TIMEOUT + STREAMING_TIME / 1000);
		assure(sem,
				2,
				"did not receive 2 events (Scheduler: streaming, streamed) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP4 ------------------------
		logCheckpoint(4, startTime);

		assertEquals("wrong count of scheduler events ", 5,
				schedulerEvent.get());
		assertEquals("wrong count of scheduler 1 events ", 2,
				schedulerEventLecture1.get());
		assertEquals("wrong count of scheduler 2 events ", 2,
				schedulerEventLecture2.get());
		assertEquals("wrong count of scheduler 3 events ", 1,
				schedulerEventLecture3.get());

		assertEquals("wrong count of virtualSchool Lecture1 events ", 1,
				virtualSchoolEventLecture1.get());
		assertEquals("wrong count of virtualSchool Lecture2 events ", 1,
				virtualSchoolEventLecture2.get());
		assertEquals("wrong count of virtualSchool Lecture3 events ", 1,
				virtualSchoolEventLecture3.get());

		assertEquals("wrong count of classroom events ", 3, classroomEvent.get());
		assertEquals("wrong count of classroom 1 events ", 1,
				classroomEventLecture1.get());
		assertEquals("wrong count of classroom 2 events ", 1,
				classroomEventLecture2.get());
		assertEquals("wrong count of classroom 3 events ", 1,
				classroomEventLecture3.get());

		assure(semClassroom,
				1,
				"did not receive 1 event (classroom: finished streaming) in time",
				DEFAULT_CHECK_TIMEOUT + STREAMING_TIME / 1000);

		assure(sem, 1,
				"did not receive 1 event (Scheduler: streamed) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP5 ------------------------
		logCheckpoint(5, startTime);
		assertEquals("wrong count of scheduler events ", 6,
				schedulerEvent.intValue());
		assertEquals("wrong count of scheduler 1 events ", 2,
				schedulerEventLecture1.get());
		assertEquals("wrong count of scheduler 2 events ", 2,
				schedulerEventLecture2.get());
		assertEquals("wrong count of scheduler 3 events ", 2,
				schedulerEventLecture3.get());

		assertEquals("wrong count of virtualSchool Lecture1 events ", 1,
				virtualSchoolEventLecture1.get());
		assertEquals("wrong count of virtualSchool Lecture2 events ", 1,
				virtualSchoolEventLecture2.get());
		assertEquals("wrong count of virtualSchool Lecture3 events ", 1,
				virtualSchoolEventLecture3.get());

		assertEquals("wrong count of classroom events ", 3, classroomEvent.get());
		assertEquals("wrong count of classroom 1 events ", 1,
				classroomEventLecture1.get());
		assertEquals("wrong count of classroom 2 events ", 1,
				classroomEventLecture2.get());
		assertEquals("wrong count of classroom 3 events ", 1,
				classroomEventLecture3.get());

	}

	@After
	public void shutdown() {
		// disable all listeners
		vs1.setVirtualSchoolListener(null);
		vs2.setVirtualSchoolListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down " + AbstractJMSTest.CLASSROOM1_NAME + "...");
		c1.stop();
		log("shutting down " + AbstractJMSTest.CLASSROOM2_NAME + "...");
		c2.stop();
		log("shutting down " + AbstractJMSTest.CLASSROOM3_NAME + "...");
		c3.stop();
		log("shutting down " + AbstractJMSTest.CLASSROOM4_NAME + "...");
		c4.stop();
		log("shutting down " + AbstractJMSTest.VIRTUALSCHOOL1_NAME + "...");
		vs1.stop();
		log("shutting down " + AbstractJMSTest.VIRTUALSCHOOL2_NAME + "...");
		vs2.stop();
		log("shutting down Scheduler...");
		scheduler.stop();

		super.shutdown();
	}

}
