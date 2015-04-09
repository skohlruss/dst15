package dst.ass2.ejb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.model.IVirtualSchool;
import dst.ass2.EJBBaseTest;

public class Test_TestingBean extends EJBBaseTest {

	private DAOFactory daoFactory;

	@Before
	public void setUp() {
		daoFactory = new DAOFactory(em);
		testingBean.insertTestData();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testEntity1() {
		List<IMOCPlatform> ent1List = daoFactory.getPlatformDAO().findAll();
		assertEquals(2, ent1List.size());
	}

	@Test
	public void testEntity2() {
		List<IVirtualSchool> ent2List = daoFactory.getVirtualSchoolDAO()
				.findAll();
		assertEquals(3, ent2List.size());
	}

	@Test
	public void testEntity3() {
		List<IClassroom> ent3List = daoFactory.getClassroomDAO().findAll();
		assertEquals(5, ent3List.size());
	}

	@Test
	public void testEntity4() {
		List<ILectureStreaming> ent4List = daoFactory.getLectureStreamingDAO()
				.findAll();
		assertEquals(4, ent4List.size());
	}

	@Test
	public void testEntity5() {
		List<ILecture> ent5List = daoFactory.getLectureDAO().findAll();
		assertEquals(4, ent5List.size());
	}

	@Test
	public void testEntity6() {
		List<IMetadata> ent6List = daoFactory.getMetadataDAO().findAll();
		assertEquals(4, ent6List.size());
	}

	@Test
	public void testEntity7() {
		List<IModerator> ent7List = daoFactory.getModeratorDAO().findAll();
		assertEquals(3, ent7List.size());
	}

	@Test
	public void testEntity8() {
		List<ILecturer> ent8List = daoFactory.getLecturerDAO().findAll();
		assertEquals(2, ent8List.size());
	}

	@Test
	public void testMembership() {
		int discount1 = 0;
		int discount2 = 0;
		int discount3 = 0;
		for (IMembership membership : daoFactory.getMembershipDAO().findAll()) {
			if (membership.getDiscount() == 0.1)
				discount1++;
			if (membership.getDiscount() == 0.2)
				discount2++;
			if (membership.getDiscount() == 0.3)
				discount3++;
		}

		assertEquals(1, discount1);
		assertEquals(1, discount2);
		assertEquals(1, discount3);
	}
}
