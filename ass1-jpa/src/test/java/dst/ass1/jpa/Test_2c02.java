package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_2c02 extends AbstractTest {

	@Test
	public void testFindLecturesForStatusFinishedStartandFinish1() {
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			
			ILecture ent5 = daoFactory.getLectureDAO().findById(
					testData.entity5_1Id);
			ent5.getLectureStreaming().setStatus(LectureStatus.SCHEDULED);

			em.persist(ent5);

			List<ILecture> list = daoFactory.getLectureDAO()
					.findLecturesForStatusFinishedStartandFinish(null, null);
			boolean check = list == null || list.size() == 0;
			assertTrue(check);
		} finally {
			if (tx != null) {
				tx.rollback();
			}
		}
	}

	@Test
	public void testFindLecturesForStatusFinishedStartandFinish2() {
		List<ILecture> list = daoFactory.getLectureDAO()
				.findLecturesForStatusFinishedStartandFinish(null, null);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(testData.entity5_1Id, list.get(0).getId());
	}

	@Test
	public void testFindLecturesForStatusFinishedStartandFinish3() {
		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			ILecture ent5 = daoFactory.getLectureDAO().findById(
					testData.entity5_1Id);
			ent5.getLectureStreaming().setStatus(LectureStatus.FINISHED);
			ent5.getLectureStreaming().setStart(createDate(2012, 1, 20));
			ent5.getLectureStreaming().setEnd(createDate(2012, 11, 30));

			em.persist(ent5);

			tx.commit();

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

		List<ILecture> lectures = daoFactory.getLectureDAO()
				.findLecturesForStatusFinishedStartandFinish(
						createDate(2012, 1, 1), createDate(2012, 12, 31));
		assertNotNull(lectures);
		assertEquals(0, lectures.size());

		lectures = daoFactory.getLectureDAO()
				.findLecturesForStatusFinishedStartandFinish(
						createDate(2012, 1, 1), createDate(2012, 10, 1));
		assertNotNull(lectures);
		assertEquals(0, lectures.size());

		lectures = daoFactory.getLectureDAO()
				.findLecturesForStatusFinishedStartandFinish(
						createDate(2012, 10, 1), createDate(2012, 12, 1));
		assertNotNull(lectures);
		assertEquals(0, lectures.size());

		lectures = daoFactory.getLectureDAO()
				.findLecturesForStatusFinishedStartandFinish(
						createDate(2012, 1, 20), createDate(2012, 11, 30));
		assertEquals(1, lectures.size());
		assertEquals(testData.entity5_1Id, lectures.get(0).getId());

		lectures = daoFactory.getLectureDAO()
				.findLecturesForStatusFinishedStartandFinish(
						createDate(2012, 1, 20), null);
		assertEquals(1, lectures.size());
		assertEquals(testData.entity5_1Id, lectures.get(0).getId());

		lectures = daoFactory.getLectureDAO()
				.findLecturesForStatusFinishedStartandFinish(null,
						createDate(2012, 11, 30));
		assertEquals(1, lectures.size());
		assertEquals(testData.entity5_1Id, lectures.get(0).getId());

	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}

	private Date createDate(int year, int month, int day) {

		String temp = year + "/" + month + "/" + day;
		Date date = null;

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			date = formatter.parse(temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;

	}

}
