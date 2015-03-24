package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
public class LectureStreaming implements ILectureStreaming {

    @Id
    @GeneratedValue
    private Long id;
    private Date start;
    private Date end;
    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    @OneToOne(mappedBy = "lectureStreaming", targetEntity = Lecture.class)
    private ILecture lecture;
    @ManyToMany(targetEntity = Classroom.class)
    @JoinTable(name = Constants.J_STREAMING_CLASSROOM)
    private List<IClassroom> classrooms;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Date getStart() {
        return start;
    }

    @Override
    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public Date getEnd() {
        return end;
    }

    @Override
    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public LectureStatus getStatus() {
        return lectureStatus;
    }

    @Override
    public void setStatus(LectureStatus status) {
        this.lectureStatus = status;
    }

    @Override
    public List<IClassroom> getClassrooms() {
        return classrooms;
    }

    @Override
    public void setClassrooms(List<IClassroom> list) {
        this.classrooms = list;
    }

    @Override
    public void addClassroom(IClassroom classroom) {
        this.classrooms.add(classroom);
    }

    @Override
    public ILecture getLecture() {
        return lecture;
    }

    @Override
    public void setLecture(ILecture lecture) {
        this.lecture = lecture;
    }
}
