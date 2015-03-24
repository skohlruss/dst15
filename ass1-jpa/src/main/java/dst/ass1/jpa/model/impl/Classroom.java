package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.IVirtualSchool;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class Classroom implements IClassroom {

    private Long id;
    @Size(min = 5, max = 25)
    private String name;
    private Integer studentCapacity;
    @Pattern(regexp = "[A-Z]{3}-[A-Z]{3}@[0-9]{4,}")
    private String region;
    @Past
    private Date activated;
    @Past
    private Date lastUpdate;

    private List<ILectureStreaming> lectureStreamings = new ArrayList<>();
    private IVirtualSchool virtualSchool;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getStudentCapacity() {
        return studentCapacity;
    }

    @Override
    public void setStudentCapacity(Integer studentCapacity) {
        this.studentCapacity = studentCapacity;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public Date getActivated() {
        return activated;
    }

    @Override
    public void setActivated(Date activated) {
        this.activated = activated;
    }

    @Override
    public Date getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public IVirtualSchool getVirtualSchool() {
        return virtualSchool;
    }

    @Override
    public void setVirtualSchool(IVirtualSchool virtualSchool) {
        this.virtualSchool = virtualSchool;
    }

    @Override
    public List<ILectureStreaming> getLectureStreamings() {
        return lectureStreamings;
    }

    @Override
    public void setLectureStreamings(List<ILectureStreaming> streamings) {
        this.lectureStreamings = streamings;
    }

    @Override
    public void addLectureStreaming(ILectureStreaming streaming) {
        this.lectureStreamings.add(streaming);
    }
}
