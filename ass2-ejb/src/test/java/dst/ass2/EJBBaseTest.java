package dst.ass2;

import static dst.ass2.ejb.util.EJBUtils.lookup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.apache.openejb.api.LocalClient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.util.DatabaseHelper;
import dst.ass1.jpa.util.test.TestData;
import dst.ass2.ejb.session.GeneralManagementBean;
import dst.ass2.ejb.session.LectureManagementBean;
import dst.ass2.ejb.session.TestingBean;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.IGeneralManagementBean;
import dst.ass2.ejb.session.interfaces.ILectureManagementBean;
import dst.ass2.ejb.session.interfaces.ITestingBean;

@LocalClient
public class EJBBaseTest {

	protected static Context ctx;
	protected static EJBContainer ejbContainer;

	@PersistenceContext
	protected EntityManager em;

	@Resource
	protected UserTransaction userTransaction;

	protected DAOFactory daoFactory;

	protected ITestingBean testingBean;
	protected IGeneralManagementBean managementBean;

	@BeforeClass
	public static void startContainer() throws Exception {

		System.setProperty("openejb.validation.output.level", "VERBOSE");
		System.setProperty("openejb.jpa.auto-scan", "true");
		System.setProperty("openejb.embedded.initialcontext.close", "DESTROY");

		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.LocalInitialContextFactory");

		// define data-source
		p.put("dst_ds", "new://Resource?type=DataSource");
		p.put("dst_ds.JdbcDriver", "org.h2.Driver");
		p.put("dst_ds.JdbcUrl",
				"jdbc:h2:/tmp/database/dst;AUTO_SERVER=TRUE;MVCC=true");
		p.put("dst_ds.UserName", "");
		p.put("dst_ds.Password", "");
		p.put("dst_ds.JtaManaged", "true");

		ejbContainer = EJBContainer.createEJBContainer(p);
		ctx = ejbContainer.getContext();
	}

	@Before
	public void init() throws Exception {
		ctx.bind("inject", this);

		// clean database
		DatabaseHelper.cleanTables(em);

		testingBean = lookup(ctx, TestingBean.class);
		managementBean = lookup(ctx, GeneralManagementBean.class);

		daoFactory = new DAOFactory(em);
	}

	@AfterClass
	public static void closeContainer() {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}

		if (ejbContainer != null) {
			ejbContainer.close();
		}
	}

	protected void managementBean_clearPrices() {
		managementBean.clearPriceCache();
	}

	protected void managementBean_addPrices() {
		managementBean.addPrice(0, new BigDecimal(50));
		managementBean.addPrice(1, new BigDecimal(45));
		managementBean.addPrice(2, new BigDecimal(40));
		managementBean.addPrice(4, new BigDecimal(35));
		managementBean.addPrice(10, new BigDecimal(30));
		managementBean.addPrice(20, new BigDecimal(20));
		managementBean.addPrice(100, new BigDecimal(15));
	}

	protected void removeLecturesFromDB(boolean onlyScheduled) throws Exception {
		userTransaction.begin();
		for (ILecture lecture : daoFactory.getLectureDAO().findAll()) {
			if (onlyScheduled) {
				if (lecture.getLectureStreaming().getStatus()
						.equals(LectureStatus.SCHEDULED))
					em.remove(lecture);
			} else
				em.remove(lecture);
		}
		userTransaction.commit();
	}

	protected List<Long> getPlatformIdsFromDB() {
		List<IMOCPlatform> platforms = daoFactory.getPlatformDAO().findAll();
		List<Long> ids = new ArrayList<Long>();
		for (IMOCPlatform platform : platforms) {
			ids.add(platform.getId());
		}
		return ids;
	}

	protected void addLecturesForLecturer1(List<Long> ids)
			throws NamingException, AssignmentException {
		ILectureManagementBean lectureManagementBean = lookup(ctx,
				LectureManagementBean.class);

		lectureManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1);

		List<String> settings1 = new ArrayList<String>();
		settings1.add("s1");
		settings1.add("s2");
		lectureManagementBean.addLecture(ids.get(0), 2, "course1", settings1);

		List<String> settings2 = new ArrayList<String>();
		settings2.add("s1");
		lectureManagementBean.addLecture(ids.get(1), 6, "course2", settings2);

		lectureManagementBean.submitAssignments();
	}

	protected void addLecturesForLecturer2(List<Long> ids)
			throws NamingException, AssignmentException {
		ILectureManagementBean lectureManagementBean = lookup(ctx,
				LectureManagementBean.class);

		lectureManagementBean.login(TestData.N_ENT8_2, TestData.PW_ENT8_2);

		List<String> params3 = new ArrayList<String>();
		params3.add("s1");
		params3.add("s2");
		params3.add("s3");
		params3.add("s4");
		lectureManagementBean.addLecture(ids.get(0), 2, "course3",
				params3);

		List<String> params5 = new ArrayList<String>();

		lectureManagementBean.addLecture(ids.get(0), 3, "course4",
				params5);

		lectureManagementBean.submitAssignments();
	}

}
