package dst.ass3.jms;

import dst.ass3.jms.classroom.impl.Classroom;
import dst.ass3.jms.scheduler.IScheduler;
import dst.ass3.jms.scheduler.impl.Scheduler;
import dst.ass3.jms.virtualschool.IVirtualSchool;
import dst.ass3.jms.classroom.IClassroom;
import dst.ass3.jms.virtualschool.impl.VirtualSchool;
import dst.ass3.model.LectureType;

public class JMSFactory {

    public static IVirtualSchool createVirtualSchool(String name) {
        return new VirtualSchool(name);
    }

    public static IClassroom createClassroom(String name, String virtualSchool,
            LectureType type) {
        return new Classroom(name, virtualSchool, type);
    }

    public static IScheduler createScheduler() {
        return new Scheduler();
    }
}
