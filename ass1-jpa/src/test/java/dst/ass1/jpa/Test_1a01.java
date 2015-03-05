package dst.ass1.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_1a01 extends AbstractTest {
	
	@Test
	public void testModelFactory() {
		assertNotNull(modelFactory.createAddress());
		assertNotNull(modelFactory.createModerator());
		assertNotNull(modelFactory.createVirtualSchool());
		assertNotNull(modelFactory.createClassroom());
		assertNotNull(modelFactory.createMetadata());
		assertNotNull(modelFactory.createLectureStreaming());
		assertNotNull(modelFactory.createPlatform());
		assertNotNull(modelFactory.createLecture());
		assertNotNull(modelFactory.createMembership());
		assertNotNull(modelFactory.createLecturer());
	}

	@Test
	public void testDAOFactory() {
		DAOFactory daoDummy = new DAOFactory(em);

		assertNotNull(daoDummy.getModeratorDAO());
		assertNotNull(daoDummy.getVirtualSchoolDAO());
		assertNotNull(daoDummy.getClassroomDAO());
		assertNotNull(daoDummy.getMetadataDAO());
		assertNotNull(daoDummy.getLectureStreamingDAO());
		assertNotNull(daoDummy.getPlatformDAO());
		assertNotNull(daoDummy.getLectureDAO());
		assertNotNull(daoDummy.getMembershipDAO());
		assertNotNull(daoDummy.getLecturerDAO());
	}

	@Test
	public void testEntities() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			EntityTransaction tx = em.getTransaction();

			if (tx.isActive()) {
				try {
					tx.rollback();
				} catch (Exception e1) {
				}
			}
			
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
