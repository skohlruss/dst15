package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.DatabaseHelper;

public class Test_1a09 extends AbstractTest {

	@Test
	public void testEntity5Entity4Association() {
		List<ILecture> ent5_list = daoFactory.getLectureDAO().findAll();

		assertNotNull(ent5_list);
		assertEquals(4, ent5_list.size());

		ILecture ent5_1 = daoFactory.getLectureDAO().findById(
				testData.entity5_1Id);
		ILecture ent5_2 = daoFactory.getLectureDAO().findById(
				testData.entity5_2Id);
		ILecture ent5_3 = daoFactory.getLectureDAO().findById(
				testData.entity5_3Id);
		ILecture ent5_4 = daoFactory.getLectureDAO().findById(
				testData.entity5_4Id);

		assertEquals(testData.entity5_1Id, ent5_1.getId());
		assertEquals(testData.entity4_1Id, ent5_1.getLectureStreaming().getId());

		assertEquals(testData.entity5_2Id, ent5_2.getId());
		assertEquals(testData.entity4_2Id, ent5_2.getLectureStreaming().getId());

		assertEquals(testData.entity5_3Id, ent5_3.getId());
		assertEquals(testData.entity4_3Id, ent5_3.getLectureStreaming().getId());

		assertEquals(testData.entity5_4Id, ent5_4.getId());
		assertEquals(testData.entity4_4Id, ent5_4.getLectureStreaming().getId());

		List<ILectureStreaming> list = daoFactory.getLectureStreamingDAO()
				.findAll();

		assertNotNull(list);
		assertEquals(4, list.size());

		ILectureStreaming ent4_1 = daoFactory.getLectureStreamingDAO()
				.findById(testData.entity4_1Id);
		ILectureStreaming ent4_2 = daoFactory.getLectureStreamingDAO()
				.findById(testData.entity4_2Id);
		ILectureStreaming ent4_3 = daoFactory.getLectureStreamingDAO()
				.findById(testData.entity4_3Id);
		ILectureStreaming ent4_4 = daoFactory.getLectureStreamingDAO()
				.findById(testData.entity4_4Id);

		assertEquals(testData.entity4_1Id, ent4_1.getId());
		assertEquals(LectureStatus.FINISHED, ent4_1.getStatus());
		assertEquals(testData.entity5_1Id, ent4_1.getLecture().getId());

		assertEquals(testData.entity4_2Id, ent4_2.getId());
		assertEquals(LectureStatus.SCHEDULED, ent4_2.getStatus());
		assertEquals(testData.entity5_2Id, ent4_2.getLecture().getId());

		assertEquals(testData.entity4_3Id, ent4_3.getId());
		assertEquals(LectureStatus.SCHEDULED, ent4_3.getStatus());
		assertEquals(testData.entity5_3Id, ent4_3.getLecture().getId());

		assertEquals(testData.entity4_4Id, ent4_4.getId());
		assertEquals(LectureStatus.SCHEDULED, ent4_4.getStatus());
		assertEquals(testData.entity5_4Id, ent4_4.getLecture().getId());
	}

	@Test
	public void testEntity5OptionalConstraint() {
		ILecture ent5 = daoFactory.getLectureDAO().findById(
				testData.entity5_1Id);
		assertNotNull(ent5);

		boolean isConstraint = false;
		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			ent5.setLectureStreaming(null);
			em.persist(ent5);
			tx.commit();
		} catch (Exception e) {
			if (e.getCause().getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				isConstraint = true;
			}
		}

		assertTrue(isConstraint);
	}

	@Test
	public void testEntity5Entity4AssociationJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_LECTURE,
				Constants.I_STREAMING, em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
