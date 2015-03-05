package dst.ass1;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.util.DatabaseHelper;
import dst.ass1.jpa.util.test.TestData;

public abstract class AbstractTest {

	protected EntityManagerFactory emf;
	protected EntityManager em;
	protected ModelFactory modelFactory;
	protected DAOFactory daoFactory;

	protected TestData testData;

	@Before
	public void init() throws Exception {
		emf = AbstractTestSuite.getEmf();
		em = emf.createEntityManager();
		modelFactory = new ModelFactory();
		testData = new TestData(em);
		daoFactory = new DAOFactory(em);
		DatabaseHelper.cleanTables(em);

		setUpDatabase();
	}

	@After
	public void clean() throws Exception {
		DatabaseHelper.cleanTables(em);

		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		em.close();
	}

	protected EntityManager getFreshEntityManager() {
		return emf.createEntityManager();
	}

	protected void setUpDatabase() throws Exception {
	}

	protected List<Long> getLectureIds(List<ILecture> lectures) {
		List<Long> ids = new ArrayList<Long>();

		for (ILecture lecture : lectures)
			ids.add(lecture.getId());

		return ids;
	}

	protected List<Long> getLecturerIds(List<ILecturer> lecturers) {
		List<Long> ids = new ArrayList<Long>();

		for (ILecturer lecturer : lecturers)
			ids.add(lecturer.getId());

		return ids;
	}

	protected List<Long> getClassroomIds(List<IClassroom> list) {
		List<Long> ids = new ArrayList<Long>();

		for (IClassroom o : list)
			ids.add(o.getId());

		return ids;
	}

	protected boolean checkMembership(Long lecturerId, Long platformId,
			Double discount, List<IMembership> memberships) {

		for (IMembership membership : memberships) {
			IMembershipKey memId = membership.getId();
			assertNotNull(memId);

			IMOCPlatform mem = memId.getMOCPlatform();
			assertNotNull(mem);
			Long memIdL = mem.getId();
			assertNotNull(memIdL);

			ILecturer memLecturer = memId.getLecturer();
			assertNotNull(memLecturer);
			Long memLecturerId = memLecturer.getId();
			assertNotNull(memLecturerId);

			Double memDiscount = membership.getDiscount();
			assertNotNull(memDiscount);

			assertNotNull(membership.getRegistration());

			if (memIdL.equals(platformId) && memLecturerId.equals(lecturerId)
					&& memDiscount.equals(discount))
				return true;
		}

		return false;
	}

}
