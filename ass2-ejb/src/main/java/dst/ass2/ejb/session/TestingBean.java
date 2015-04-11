package dst.ass2.ejb.session;

import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst.ass1.jpa.util.test.TestData;
import dst.ass2.ejb.session.interfaces.ITestingBean;

@Stateless
public class TestingBean implements ITestingBean {

    @PersistenceContext(name = "dst")
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
