package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.IMembershipKey;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by pavol on 24.3.2015.
 */
@Embeddable
public class MembershipKey implements IMembershipKey, Serializable {

    @ManyToOne(targetEntity = Lecturer.class)
    private ILecturer lecturer;
    @ManyToOne(targetEntity = MOCPlatform.class)
    private IMOCPlatform mocPlatform;

    @Override
    public ILecturer getLecturer() {
        return lecturer;
    }

    @Override
    public void setLecturer(ILecturer lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public IMOCPlatform getMOCPlatform() {
        return mocPlatform;
    }

    @Override
    public void setMOCPlatform(IMOCPlatform platform) {
        this.mocPlatform = platform;
    }
}
