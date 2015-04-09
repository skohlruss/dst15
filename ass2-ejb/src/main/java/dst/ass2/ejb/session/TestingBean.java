package dst.ass2.ejb.session;

import java.security.NoSuchAlgorithmException;

import javax.persistence.EntityManager;

import dst.ass1.jpa.util.test.TestData;
import dst.ass2.ejb.session.interfaces.ITestingBean;

// TODO add annotation
public class TestingBean implements ITestingBean {

	// TODO add annotation
	private EntityManager em;
	
	private TestData testData; 

	@Override
	public void insertTestData() {
		System.out.println("Started");

		testData = new TestData(em);
		try {
			testData.insertTestData_withoutTransaction();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		System.out.println("Finished");
	}

}
