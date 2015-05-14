package dst.ass3;

import org.junit.After;
import org.junit.Before;

import com.espertech.esper.client.EventBean;

import dst.ass3.event.EventingFactory;
import dst.ass3.event.IEventStreaming;
import dst.ass3.model.LectureType;
import dst.ass3.model.LifecycleState;

public abstract class AbstractEventTest {

	protected final String CLASSIFIEDBY = "vs1";
	protected final int allowedInaccuracy = 500;

	protected IEventStreaming test;

	@Before
	public void setup() {
		System.out.println("******************************"
				+ this.getClass().getCanonicalName()
				+ "******************************");

		test = EventingFactory.getInstance();
	}

	@After
	public void shutdown() {
		test.close();
	}

	protected LectureType getLectureType(EventBean e) {
		try {
			return (LectureType) e.get("type");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	protected LifecycleState getLectureStatus(EventBean e) {
		try {
			return (LifecycleState) e.get("state");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
