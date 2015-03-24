package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ILectureDAO;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.model.impl.Lecture;
import dst.ass1.jpa.model.impl.LectureStreaming;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class LectureDAO implements ILectureDAO {

    private EntityManager em;

    public LectureDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Hibernate criteria
     */
    @Override
    public List<ILecture> findLecturesForLecturerAndCourse(String lecturerName, String course) {

        Session session = em.unwrap(Session.class);

        Criterion lecturerNameRest = lecturerName != null ? Restrictions.eq("l.lecturerName", lecturerName) : Restrictions.disjunction();
        Criterion courseRest = course != null ? Restrictions.eq("m.course", course) : Restrictions.disjunction();

        List<ILecture> lectures = session.createCriteria(Lecture.class)
                .createAlias("lecturer", "l")
                .createAlias("metadata", "m")
                .add(lecturerNameRest)
                .add(courseRest)
                .list();

        return lectures;
    }

    /**
     * Hibernate criteria - query by example
     *
     * Version properties, identifiers and associations are ignored
     */

    @Override
    public List<ILecture> findLecturesForStatusFinishedStartandFinish(Date start, Date finish) {

        Session session = em.unwrap(Session.class);

        Lecture lecture = new Lecture();
        LectureStreaming lectureStreaming = new LectureStreaming();
        lecture.setLectureStreaming(lectureStreaming);

        lectureStreaming.setStatus(LectureStatus.FINISHED);
        lectureStreaming.setStart(start);
        lectureStreaming.setEnd(finish);

        Example queryByExample = Example.create(lectureStreaming);
        Criteria criteria = session.createCriteria(LectureStreaming.class).add(queryByExample);

        return getLectures(criteria.list());
    }

    private List<ILecture> getLectures(List<ILectureStreaming> streamings) {
        List<ILecture> lectures = new ArrayList<>();
        for (ILectureStreaming streaming : streamings) {
            lectures.add(streaming.getLecture());
        }
        return lectures;
    }

    @Override
    public ILecture findById(Long id) {
        return em.find(Lecture.class, id);
    }

    @Override
    public List<ILecture> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ILecture> cq = cb.createQuery(ILecture.class);
        Root<Lecture> root = cq.from(Lecture.class);

        cq.select(root);
        TypedQuery<ILecture> query = em.createQuery(cq);
        return query.getResultList();
    }
}
