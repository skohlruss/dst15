package dst.ass2.ejb;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass2.EJBBaseTest;

public class Test_TimerService extends EJBBaseTest {

	private final int sleepingTime = 30 * 1000;
	private DAOFactory daoFactory;

	@Before
	public void setUp() {
		testingBean.insertTestData();
		daoFactory = new DAOFactory(em);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testTimerService() {
		try {
			Thread.sleep(sleepingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int finished = 0;
		for (ILectureStreaming streamings : daoFactory
				.getLectureStreamingDAO().findAll()) {
			if (streamings.getStatus() == LectureStatus.FINISHED)
				finished++;
		}
		
		assertEquals(4, finished);
	}

}
