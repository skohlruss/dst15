package dst.ass2.ejb;

import static dst.ass2.ejb.util.EJBUtils.lookup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.NoSuchEJBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.util.test.TestData;
import dst.ass2.EJBBaseTest;
import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.LectureManagementBean;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.ILectureManagementBean;

public class Test_LectureManagementBean extends EJBBaseTest {

	private final String course1 = "course1";
	private final String course2 = "course2";

	private ILectureManagementBean lectureManagementBean;

	@Before
	public void setUp() throws Exception {
		testingBean.insertTestData();
		managementBean.clearPriceCache();
		lectureManagementBean = lookup(ctx, LectureManagementBean.class);
		// remove all lectures from db
		removeLecturesFromDB(false);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCache() {
		// cache should be empty
		List<AssignmentDTO> cache = lectureManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(0, cache.size());
	}

	@Test
	public void testLogin_With_CorrectCredentials() throws Exception {
		lectureManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1);
	}

	@Test(expected = AssignmentException.class)
	public void testLogin_With_InvalidCredentials() throws Exception {
		lectureManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1 + "1");
		fail("Login with invalid credentials passed. Expected: "
				+ AssignmentException.class.getName());
	}

	@Test
	public void testAdd_And_RemoveLectures() throws Exception {
		// get the ids from the database
		List<Long> ids = getPlatformIdsFromDB();
		assertEquals(2, ids.size());

		// add 2 lectures
		lectureManagementBean.addLecture(ids.get(0), 1, course1,
				new ArrayList<String>());
		lectureManagementBean.addLecture(ids.get(0), 2, course2,
				new ArrayList<String>());

		// there should be 2 lectures in the cache
		List<AssignmentDTO> cache = lectureManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isLectureInCache(new AssignmentDTO(ids.get(0), 1, course1,
				new ArrayList<String>(), null), cache));
		assertTrue(isLectureInCache(new AssignmentDTO(ids.get(0), 2, course2,
				new ArrayList<String>(), null), cache));

		// remove lectures for incorrect value
		lectureManagementBean.removeLecturesForPlatform(Long.MAX_VALUE);

		// cache should stay the same
		cache = lectureManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isLectureInCache(
				new AssignmentDTO(ids.get(0), 1, course1,
						new ArrayList<String>(), new ArrayList<Long>()), cache));
		assertTrue(isLectureInCache(
				new AssignmentDTO(ids.get(0), 2, course2,
						new ArrayList<String>(), new ArrayList<Long>()), cache));

		// remove lectures
		lectureManagementBean.removeLecturesForPlatform(ids.get(0));

		// cache should be empty
		cache = lectureManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(0, cache.size());
	}

	@Test
	public void testAddLectures_With_Login_And_Check_Discarded()
			throws Exception {

		// get the ids from the database
		List<Long> ids = getPlatformIdsFromDB();
		assertEquals(2, ids.size());

		managementBean_addPrices();

		lectureManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1);

		List<String> settings = new ArrayList<String>();
		settings.add("setting1");
		settings.add("setting2");
		lectureManagementBean.addLecture(ids.get(0), 2, course1, settings);

		List<String> settings2 = new ArrayList<String>();
		settings2.add("setting3");
		lectureManagementBean.addLecture(ids.get(1), 6, course2, settings2);

		// check cache
		List<AssignmentDTO> cache = lectureManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isLectureInCache(
				new AssignmentDTO(ids.get(0), 2, course1,
						settings, new ArrayList<Long>()), cache));
		assertTrue(isLectureInCache(
				new AssignmentDTO(ids.get(1), 6, course2,
						settings2, new ArrayList<Long>()), cache));

		lectureManagementBean.submitAssignments();

		// there have to be 2 lectures in total in the db
		List<ILecture> lectures = daoFactory.getLectureDAO().findAll();

		assertNotNull(lectures);
		assertEquals(2, lectures.size());

		assertTrue(checkLectureInList(course1, TestData.N_ENT8_1, settings,
				lectures));
		assertTrue(checkLectureInList(course2, TestData.N_ENT8_1, settings2,
				lectures));

		// check if the bean was discarded after submitAssignments() was
		// called successfully!
		try {
			lectureManagementBean.getCache();
			fail(NoSuchEJBException.class.getName() + " expected!");
		} catch (NoSuchEJBException e) {
			// Expected
		}
	}

	@Test
	public void testAddLecture_With_Login_AnotherLecturer() throws Exception {

		// get the ids from the database
		List<Long> ids = getPlatformIdsFromDB();
		assertEquals(2, ids.size());

		managementBean_addPrices();

		lectureManagementBean.login(TestData.N_ENT8_2, TestData.PW_ENT8_2);

		List<String> settings1 = new ArrayList<String>();
		settings1.add("s1");
		settings1.add("s2");
		settings1.add("s3");
		settings1.add("s4");

		final String course3 = "course3";

		lectureManagementBean.addLecture(ids.get(0), 2, course3, settings1);

		// check cache
		List<AssignmentDTO> cache = lectureManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(1, cache.size());
		assertTrue(isLectureInCache(
				new AssignmentDTO(ids.get(0), 2, course3,
						settings1, new ArrayList<Long>()), cache));

		lectureManagementBean.submitAssignments();

		// there has to be 1 lecture in total in the db
		List<ILecture> lectures = daoFactory.getLectureDAO().findAll();

		assertNotNull(lectures);
		assertEquals(1, lectures.size());

		assertTrue(checkLectureInList(course3, TestData.N_ENT8_2, settings1,
				lectures));
	}

	@Test
	public void testAddLecture_Without_Login() {
		try {

			// get the ids from the database
			List<Long> ids = getPlatformIdsFromDB();
			assertEquals(2, ids.size());

			managementBean.addPrice(0, new BigDecimal(50));

			List<String> settings1 = new ArrayList<String>();
			settings1.add("s1");
			settings1.add("s2");
			lectureManagementBean.addLecture(ids.get(0), 2, course1, settings1);

			// try to submit assignments => without log in
			lectureManagementBean.submitAssignments();

			fail("Assignments got submitted without login. "
					+ "AssignmentException expected!");
		} catch (Exception e) {
			// Expected Exception
		}

		// there shouldn't be any lectures in the db
		assertEquals(0, daoFactory.getLectureDAO().findAll().size());
	}

	@Test
	public void testAddLecture_Without_Login2() throws Exception {

		// get the ids from the database
		List<Long> ids = getPlatformIdsFromDB();
		assertEquals(2, ids.size());

		managementBean_addPrices();

		List<String> settings1 = new ArrayList<String>();
		settings1.add("s1");
		settings1.add("s2");
		lectureManagementBean.addLecture(ids.get(0), 2, course1, settings1);

		List<String> settings2 = new ArrayList<String>();
		settings2.add("s1");
		lectureManagementBean.addLecture(ids.get(1), 6, course2, settings2);

		// check cache
		List<AssignmentDTO> cache = lectureManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isLectureInCache(
				new AssignmentDTO(ids.get(0), 2, course1,
						settings1, new ArrayList<Long>()), cache));
		assertTrue(isLectureInCache(
				new AssignmentDTO(ids.get(1), 6, course2,
						settings2, new ArrayList<Long>()), cache));

		// try to submit assignments => without log in
		try {
			lectureManagementBean.submitAssignments();
			fail("Assignments got submitted without login. AssignmentException expected!");
		} catch (AssignmentException e) {
			// Expected Exception
		}

		// check cache => should not be cleared
		cache = lectureManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isLectureInCache(
				new AssignmentDTO(ids.get(0), 2, course1,
						settings1, new ArrayList<Long>()), cache));
		assertTrue(isLectureInCache(
				new AssignmentDTO(ids.get(1), 6, course2,
						settings2, new ArrayList<Long>()), cache));

		// there shouldn't be any lectures in the db
		assertEquals(0, daoFactory.getLectureDAO().findAll().size());

		// login
		lectureManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1);
		// successfully submit Assignments
		lectureManagementBean.submitAssignments();

		// check the database here

		// there have to be 2 lectures in total in the db
		List<ILecture> lectures = daoFactory.getLectureDAO().findAll();
		System.err.println(lectures.size());
		assertNotNull(lectures);
		assertEquals(2, lectures.size());

		assertTrue(checkLectureInList(course1, TestData.N_ENT8_1, settings1,
				lectures));
		assertTrue(checkLectureInList(course2, TestData.N_ENT8_1, settings2,
				lectures));
	}

	@Test
	public void testAddLecture_With_WrongId() throws Exception {

		long nonExisting = Long.MAX_VALUE;

		// try to add a lecture for non-existing id.
		try {
			lectureManagementBean.addLecture(nonExisting, 3, "course5",
					new ArrayList<String>());
			fail("ID is not available in DB. AssignmentException expected!");
		} catch (AssignmentException e) {
			// expected Exception
		}

		// cache should be empty
		List<AssignmentDTO> cache = lectureManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(0, cache.size());

		// also db should be empty
		assertEquals(0, daoFactory.getLectureDAO().findAll().size());
	}

	private boolean checkLectureInList(String course, String lecturerName,
			List<String> settings, List<ILecture> lectures) {

		for (ILecture lecture : lectures) {
			assertNotNull(lecture.getMetadata());
			String lectureCourse = lecture.getMetadata().getCourse();
			assertNotNull(lectureCourse);
			assertNotNull(lecture.getLecturer());
			String lectureLecturerName = lecture.getLecturer()
					.getLecturerName();
			assertNotNull(lectureLecturerName);
			List<String> lectureSettings = lecture.getMetadata().getSettings();
			assertNotNull(lectureSettings);
			assertNotNull(lecture.getLectureStreaming());
			assertNotNull(lecture.getLectureStreaming().getStart());

			if (lectureCourse.equals(course)
					&& lectureLecturerName.equals(lecturerName)
					&& lectureSettings.size() == settings.size()
					&& lectureSettings.containsAll(settings))
				return true;
		}

		return false;
	}

	private boolean isLectureInCache(AssignmentDTO assignmentDTO,
			List<AssignmentDTO> cache) {
		for (AssignmentDTO cached : cache) {
			Long id1 = cached.getPlatformId();
			Integer num1 = cached.getNumStudents();
			List<String> list1 = cached.getSettings();
			String string1 = cached.getCourse();
			List<Long> list2 = cached.getClassroomIds();

			assertNotNull(id1);
			assertNotNull(num1);
			assertNotNull(list1);
			assertNotNull(string1);
			assertNotNull(list2);
			assertTrue(list2.size() > 0);

			if (id1.equals(assignmentDTO.getPlatformId())
					&& num1.equals(assignmentDTO.getNumStudents())
					&& list1.equals(assignmentDTO.getSettings())
					&& string1.equals(assignmentDTO.getCourse())) {
				return true;
			}

		}
		return false;
	}
}
