package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Constants.Q_ALLFINISHEDLECTURES,
                query = "select l from Lecture as l" +
                        " where l.lectureStreaming.lectureStatus = dst.ass1.jpa.model.LectureStatus.FINISHED"),
})
public class Lecture implements ILecture, Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Transient
    private Integer streamingTime;
    @Transient
    private Integer attendingStudents;
    private boolean isPaid;

    @OneToOne(targetEntity = Metadata.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "metadata_id", unique = true)
    private IMetadata metadata;
    @OneToOne(targetEntity = LectureStreaming.class, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "lectureStreaming_id")
    private ILectureStreaming lectureStreaming;
    @ManyToOne(targetEntity = Lecturer.class)
    @JoinColumn(name = "lecturer_id")
    private ILecturer lecturer;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Integer getStreamingTime() {
        return streamingTime;
    }

    @Override
    public void setStreamingTime(Integer streamingTime) {
        this.streamingTime = streamingTime;
    }

    @Override
    public Integer getAttendingStudents() {
        return attendingStudents;
    }

    @Override
    public void setAttendingStudents(Integer students) {
        this.attendingStudents = students;
    }

    @Override
    public boolean isPaid() {
        return isPaid;
    }

    @Override
    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    @Override
    public IMetadata getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(IMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public ILecturer getLecturer() {
        return lecturer;
    }

    @Override
    public void setLecturer(ILecturer lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public ILectureStreaming getLectureStreaming() {
        return lectureStreaming;
    }

    @Override
    public void setLectureStreaming(ILectureStreaming streaming) {
        this.lectureStreaming = streaming;
    }
}
