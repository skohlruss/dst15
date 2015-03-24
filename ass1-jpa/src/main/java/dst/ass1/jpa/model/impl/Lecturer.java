package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMembership;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
@DiscriminatorValue("l")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accountNo", "bankCode"})
})
public class Lecturer extends Person implements ILecturer{

    @Column(unique = true, nullable = false)
    private String lecturerName;
    @Column(columnDefinition = "VARBINARY(16)")
    private byte[] password;
    private String accountNo;
    private String bankCode;

    @OneToMany(mappedBy = "id.lecturer",targetEntity = Membership.class)
    private List<IMembership> memberships;
    @OneToMany(mappedBy = "lecturer", targetEntity = Lecture.class)
    private List<ILecture> lectures;


    @Override
    public String getLecturerName() {
        return lecturerName;
    }

    @Override
    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    @Override
    public byte[] getPassword() {
        return password;
    }

    @Override
    public void setPassword(byte[] password) {
        this.password = password;
    }

    @Override
    public String getAccountNo() {
        return accountNo;
    }

    @Override
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Override
    public String getBankCode() {
        return bankCode;
    }

    @Override
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public List<ILecture> getLectures() {
        return lectures;
    }

    @Override
    public void setLectures(List<ILecture> lectures) {
        this.lectures = lectures;
    }

    @Override
    public void addLecture(ILecture lecture) {
        this.lectures.add(lecture);
    }

    @Override
    public List<IMembership> getMemberships() {
        return memberships;
    }

    @Override
    public void setMemberships(List<IMembership> memberships) {
        this.memberships = memberships;
    }

    @Override
    public void addMembership(IMembership membership) {
        this.memberships.add(membership);
    }
}
