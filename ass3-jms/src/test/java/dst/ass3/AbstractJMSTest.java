package dst.ass3;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import dst.ass3.jms.JMSFactory;
import dst.ass3.jms.classroom.IClassroom;
import dst.ass3.jms.scheduler.IScheduler;
import dst.ass3.jms.virtualschool.IVirtualSchool;
import dst.ass3.model.LectureType;

public abstract class AbstractJMSTest {

	protected static final String CLASSROOM1_NAME = "c1";
	protected static final String CLASSROOM2_NAME = "c2";
	protected static final String CLASSROOM3_NAME = "c3";
	protected static final String CLASSROOM4_NAME = "c4";

	protected static final String VIRTUALSCHOOL1_NAME = "vs1";
	protected static final String VIRTUALSCHOOL2_NAME = "vs2";

	protected IScheduler scheduler;
	protected IVirtualSchool vs2;
	protected IVirtualSchool vs1;
	protected IClassroom c4;
	protected IClassroom c3;
	protected IClassroom c2;
	protected IClassroom c1;

	protected EJBContainer ejbContainer;
	protected Context ctx;

	/**
	 * Time to wait for semaphores to reach required value
	 */
	public int DEFAULT_CHECK_TIMEOUT = 5;

	public void init() {
		this.c1 = JMSFactory.createClassroom(CLASSROOM1_NAME,
				VIRTUALSCHOOL1_NAME, LectureType.INTERACTIVE);
		this.c2 = JMSFactory.createClassroom(CLASSROOM2_NAME,
				VIRTUALSCHOOL1_NAME, LectureType.PRESENTATION);
		this.c3 = JMSFactory.createClassroom(CLASSROOM3_NAME,
				VIRTUALSCHOOL2_NAME, LectureType.INTERACTIVE);
		this.c4 = JMSFactory.createClassroom(CLASSROOM4_NAME,
				VIRTUALSCHOOL2_NAME, LectureType.PRESENTATION);

		this.vs1 = JMSFactory.createVirtualSchool(VIRTUALSCHOOL1_NAME);
		this.vs2 = JMSFactory.createVirtualSchool(VIRTUALSCHOOL2_NAME);

		this.scheduler = JMSFactory.createScheduler();

		System.setProperty("openejb.validation.output.level", "VERBOSE");
		System.setProperty("openejb.embedded.initialcontext.close", "DESTROY");

		try {
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

			ctx.bind("inject", scheduler);

			ctx.bind("inject", vs1);
			ctx.bind("inject", vs2);
                        
			ctx.bind("inject", c1);
			ctx.bind("inject", c2);
			ctx.bind("inject", c3);
			ctx.bind("inject", c4);
		} catch (Exception e) {
			e.printStackTrace();

			throw new RuntimeException(e);
		}

		System.out.println("******************************"
				+ this.getClass().getCanonicalName()
				+ "******************************");
	}

	public void shutdown() {
		if(ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
		
		if (ejbContainer != null)
			ejbContainer.close();
	}

}
