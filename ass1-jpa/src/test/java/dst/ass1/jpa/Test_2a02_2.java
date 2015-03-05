package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_2a02_2 extends AbstractTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testQuery() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			ModelFactory modelFactory = new ModelFactory();

			ILecturer ent8 = daoFactory.getLecturerDAO().findById(
					testData.entity8_2Id);
			assertNotNull(ent8);

			IMetadata ent6_1 = modelFactory.createMetadata();
			ent6_1.setCourse("ctx_env");
			ent6_1.addSetting("param");
			em.persist(ent6_1);

			ILectureStreaming ent4_1 = modelFactory.createLectureStreaming();
			ent4_1.setStart(new Date(System.currentTimeMillis() - 18000000));
			ent4_1.setEnd(new Date());
			ent4_1.setStatus(LectureStatus.SCHEDULED);

			IClassroom ent3_1 = daoFactory.getClassroomDAO().findById(
					testData.entity3_1Id);
			assertNotNull(ent3_1);
			ent4_1.addClassroom(ent3_1);
			ent3_1.addLectureStreaming(ent4_1);

			ILecture ent5 = modelFactory.createLecture();
			ent5.setAttendingStudents(2);
			ent5.setStreamingTime(0);
			ent5.setMetadata(ent6_1);
			ent5.setLectureStreaming(ent4_1);
			ent5.setLecturer(ent8);
			ent8.addLecture(ent5);

			em.persist(ent4_1);
			em.persist(ent3_1);
			em.persist(ent5);
			em.persist(ent8);

			tx.commit();

			Query query = em.createNamedQuery(Constants.Q_MOSTACTIVELECTURER);

			List<ILecturer> result = (List<ILecturer>) query.getResultList();
			assertNotNull(result);
			assertEquals(1, result.size());

			List<Long> ids = getLecturerIds(result);

			assertTrue(ids.contains(testData.entity8_1Id));

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
