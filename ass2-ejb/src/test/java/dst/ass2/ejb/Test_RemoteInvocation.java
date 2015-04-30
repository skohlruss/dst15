package dst.ass2.ejb;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.util.DatabaseHelper;
import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.ILectureManagementBean;
import dst.ass2.ejb.session.interfaces.ITestingBean;
import org.apache.openejb.api.LocalClient;
import org.junit.*;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static dst.ass2.ejb.util.EJBUtils.lookup;

/**
 * Created by pavol on 30.4.2015.
 *
 * http://openejb.apache.org/examples-trunk/telephone-stateful/README.html
 * http://tomee.apache.org/clients.html
 */
@LocalClient
public class Test_RemoteInvocation {

    @PersistenceContext
    private EntityManager em;
    protected DAOFactory daoFactory;

    private static EJBContainer ejbContainer;
    private static InitialContext initialContext;

    @BeforeClass
    public static void setUpEnv() throws NamingException {

        System.setProperty("openejb.validation.output.level", "VERBOSE");
        System.setProperty("openejb.jpa.auto-scan", "true");
        System.setProperty("openejb.embedded.initialcontext.close", "DESTROY");

        Properties containerProp = new Properties();
//        core miesto client
        containerProp.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
        containerProp.setProperty("openejb.embedded.remotable", "true");
        // define data-source
        containerProp.put("dst_ds", "new://Resource?type=DataSource");
        containerProp.put("dst_ds.JdbcDriver", "org.h2.Driver");
        containerProp.put("dst_ds.JdbcUrl",
                "jdbc:h2:/tmp/database/dst;AUTO_SERVER=TRUE;MVCC=true");
        containerProp.put("dst_ds.UserName", "");
        containerProp.put("dst_ds.Password", "");
        containerProp.put("dst_ds.JtaManaged", "true");

        System.out.println("\n\nCreating container\n\n");
        ejbContainer = EJBContainer.createEJBContainer(containerProp);
        System.out.println("\n\nContainer created\n\n");

        /**
         * Getting remote context
         */
        System.out.println("\n\nContext initializing\n\n");
        Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
        /**
         * EBJ protocol - OEJP/4.6
         */
        properties.setProperty(Context.PROVIDER_URL, "ejbd://localhost:4201");
        /**
         * Http protocol
         */
//        properties.setProperty(Context.PROVIDER_URL, "http://localhost:4204/ejb");
        initialContext = new InitialContext(properties);
    }

    @Before
    public void before() throws NamingException {
        ejbContainer.getContext().bind("inject", this);
        daoFactory = new DAOFactory(em);
    }

    @After
    public void after() throws Exception {
        DatabaseHelper.cleanTables(em);
    }

    @AfterClass
    public static void closeContainer() {
        if (initialContext != null) {
            try {
                initialContext.close();
            } catch (NamingException e) {
            }
        }

        if (ejbContainer != null) {
            ejbContainer.close();
        }
    }

    @Test
    public void testingBean() throws NamingException {

        ITestingBean testingBean = (ITestingBean)initialContext.lookup("TestingBeanRemote");
//        ITestingBean testingBean = (ITestingBean)initialContext.lookup("java:global/ass2-ejb/TestingBean");

        System.out.println("\n\nTest started\n");
        testingBean.insertTestData();
        System.out.println("\n\nTest end\n");
    }

    @Test
    public void lecturerManagementBean() throws NamingException, AssignmentException {
        ITestingBean testingBean = (ITestingBean)initialContext.lookup("TestingBeanRemote");
        ILectureManagementBean lectureManagementBean = (ILectureManagementBean)initialContext.lookup("LectureManagementBeanRemote");
        testingBean.insertTestData();

        List<Long> ids = getPlatformIdsFromDB();
        List<String> settings = new ArrayList<String>();
        settings.add("setting1");
        settings.add("setting2");

        lectureManagementBean.addLecture(ids.get(0), 2, "course1", settings);
        lectureManagementBean.addLecture(ids.get(1), 6, "course2", settings);
        List<AssignmentDTO> assignmentDTOs = lectureManagementBean.getCache();
    }


    protected List<Long> getPlatformIdsFromDB() {
        List<IMOCPlatform> platforms = daoFactory.getPlatformDAO().findAll();
        List<Long> ids = new ArrayList<Long>();
        for (IMOCPlatform platform : platforms) {
            ids.add(platform.getId());
        }
        return ids;
    }
}
