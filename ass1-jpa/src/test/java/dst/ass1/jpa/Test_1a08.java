package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.DatabaseHelper;

public class Test_1a08 extends AbstractTest {

	@Test
	public void testLecturerEntity5Association() {
		ILecturer ent8 = daoFactory.getLecturerDAO().findById(
				testData.entity8_1Id);
		assertNotNull(ent8);
		assertNotNull(ent8.getLectures());

		List<Long> ent5Ids = getLectureIds(ent8.getLectures());

		assertEquals(3, ent5Ids.size());

		assertTrue(ent5Ids.contains(testData.entity5_1Id));
		assertTrue(ent5Ids.contains(testData.entity5_3Id));
		assertTrue(ent5Ids.contains(testData.entity5_4Id));

		ILecture ent5_1 = daoFactory.getLectureDAO().findById(
				testData.entity5_1Id);
		ILecture ent5_2 = daoFactory.getLectureDAO().findById(
				testData.entity5_2Id);

		assertNotNull(ent5_1);
		assertNotNull(ent5_2);

		assertEquals(testData.entity8_1Id,
				ent5_1.getLecturer().getId());
		assertEquals(testData.entity8_2Id,
				ent5_2.getLecturer().getId());
	}

	@Test
	public void testLecturerEntity5AssociationJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_LECTURE,
				Constants.I_LECTURER, em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
