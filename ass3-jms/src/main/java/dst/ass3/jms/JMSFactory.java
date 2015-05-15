package dst.ass3.jms;

import dst.ass3.jms.scheduler.IScheduler;
import dst.ass3.jms.virtualschool.IVirtualSchool;
import dst.ass3.jms.classroom.IClassroom;
import dst.ass3.model.LectureType;

public class JMSFactory {
	
	public static IVirtualSchool createVirtualSchool(String name) {
		// TODO
		return null;
	}

	public static IClassroom createClassroom(String name, String virtualSchool,
			LectureType type) {
		// TODO
		return null;
	}

	public static IScheduler createScheduler() {
		// TODO
		return null;
	}

}
