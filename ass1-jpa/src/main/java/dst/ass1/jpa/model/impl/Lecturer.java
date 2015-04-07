package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.util.Constants;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
@DiscriminatorValue("l")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accountNo", "bankCode"})
})
@NamedQueries({
        @NamedQuery(name = Constants.Q_LECTURERSWITHACTIVEMEMBERSHIP,
                query = "select l from Membership as mem" +
                        " left join mem.id.lecturer l" +
                        " left join mem.id.mocPlatform moc" +
                        " where moc.name = :name " +
                        " and :minNr <= " +
                        " (select count(distinct lect) " +
                        "   from Lecture lect " +
                        "   left join lect.lectureStreaming.classrooms classroom " +
                        "   where l = lect.lecturer and classroom.virtualSchool.mocPlatform = moc)"
        ),
        @NamedQuery(name = Constants.Q_MOSTACTIVELECTURER,
                query = "select l from Lecturer as l" +
                        " where (select max(lecturer.lectures.size) " +
                        "        from Lecturer as lecturer) = l.lectures.size" +
                        " and l.lectures.size > 0"
        )
})
public class Lecturer extends Person implements ILecturer, Serializable {

    @Column(unique = true, nullable = false)
    private String lecturerName;
    @Index(name = "password_index")
    @Column(columnDefinition = "VARBINARY(16)")
    private byte[] password;
    private String accountNo;
    private String bankCode;

    @OneToMany(mappedBy = "id.lecturer",targetEntity = Membership.class)
    private List<IMembership> memberships = new ArrayList<>();
    @OneToMany(mappedBy = "lecturer", targetEntity = Lecture.class)
    private List<ILecture> lectures = new ArrayList<>();


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
