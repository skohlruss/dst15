package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.ILectureDAO;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_2c01 extends AbstractTest {

	@Test
	public void findLecturesForLecturerAndCourse1() {
		ILectureDAO ent5_Dao = daoFactory.getLectureDAO();
		List<ILecture> ent5_list = ent5_Dao.findLecturesForLecturerAndCourse(
				dst.ass1.jpa.util.test.TestData.N_ENT8_1, TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(1, ent5_list.size());

		List<Long> ids = getLectureIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_1Id));
	}

	@Test
	public void findLecturesForLecturerAndCourse2() {
		ILectureDAO ent5_Dao = daoFactory.getLectureDAO();
		List<ILecture> ent5_list = ent5_Dao.findLecturesForLecturerAndCourse(
				dst.ass1.jpa.util.test.TestData.N_ENT8_1, "invalid_course");
		assertNotNull(ent5_list);
		assertEquals(0, ent5_list.size());
	}

	@Test
	public void findLecturesForLecturerAndCourse3() {
		ILectureDAO ent5_Dao = daoFactory.getLectureDAO();
		List<ILecture> ent5_list = ent5_Dao.findLecturesForLecturerAndCourse(
				dst.ass1.jpa.util.test.TestData.N_ENT8_2, TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(0, ent5_list.size());
	}

	@Test
	public void findLecturesForLecturerAndCourse4() {
		ILectureDAO ent5_Dao = daoFactory.getLectureDAO();
		List<ILecture> ent5_list = ent5_Dao.findLecturesForLecturerAndCourse(
				dst.ass1.jpa.util.test.TestData.N_ENT8_2, null);
		assertNotNull(ent5_list);
		assertEquals(1, ent5_list.size());

		List<Long> ids = getLectureIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_2Id));
	}

	@Test
	public void findLecturesForLecturerAndCourse5() {
		ILectureDAO ent5_Dao = daoFactory.getLectureDAO();
		List<ILecture> ent5_list = ent5_Dao.findLecturesForLecturerAndCourse(
				"lecturer5", TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(0, ent5_list.size());
	}

	@Test
	public void findLecturesForLecturerAndCourse6() {
		ILectureDAO ent5_Dao = daoFactory.getLectureDAO();
		List<ILecture> ent5_list = ent5_Dao.findLecturesForLecturerAndCourse(
				null, TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(1, ent5_list.size());

		List<Long> ids = getLectureIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_1Id));
	}

	@Test
	public void findLecturesForLecturerAndCourse7() {
		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			ILecture ent5 = daoFactory.getLectureDAO().findById(
					testData.entity5_3Id);
			ent5.getMetadata().setCourse(TestData.N_ENT6_1);

			em.persist(ent5);

			tx.commit();

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

		ILectureDAO ent5_Dao = daoFactory.getLectureDAO();
		List<ILecture> ent5 = ent5_Dao.findLecturesForLecturerAndCourse(
				dst.ass1.jpa.util.test.TestData.N_ENT8_1, TestData.N_ENT6_1);
		assertNotNull(ent5);
		assertEquals(2, ent5.size());

		List<Long> ids = getLectureIds(ent5);
		assertTrue(ids.contains(testData.entity5_1Id));
		assertTrue(ids.contains(testData.entity5_3Id));
	}

	@Test
	public void findLecturesForLecturerAndCourse8() {
		ILectureDAO ent5_Dao = daoFactory.getLectureDAO();
		List<ILecture> ent5_list = ent5_Dao.findLecturesForLecturerAndCourse(
				null, null);
		assertNotNull(ent5_list);
		assertEquals(4, ent5_list.size());

		List<Long> ids = getLectureIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_1Id));
		assertTrue(ids.contains(testData.entity5_2Id));
		assertTrue(ids.contains(testData.entity5_3Id));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
