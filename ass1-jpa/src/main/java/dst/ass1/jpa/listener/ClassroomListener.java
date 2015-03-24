package dst.ass1.jpa.listener;

import dst.ass1.jpa.model.IClassroom;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class ClassroomListener {

    @PrePersist
    void onPrePersist(Object o) {

        IClassroom classroom = (IClassroom) o;

        Date now = new Date();
        classroom.setActivated(now);
        classroom.setLastUpdate(now);
    }

    @PreUpdate
    void onPreUpdate(Object o) {

        IClassroom classroom = (IClassroom) o;

        classroom.setLastUpdate(new Date());
    }
}
