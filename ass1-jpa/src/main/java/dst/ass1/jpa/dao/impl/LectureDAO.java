package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ILectureDAO;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.model.impl.Lecture;
import dst.ass1.jpa.model.impl.LectureStreaming;
import dst.ass1.jpa.model.impl.Lecturer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class LectureDAO extends GenericDAOImpl<ILecture> implements ILectureDAO {

    public LectureDAO(EntityManager em) {
        super(em, Lecture.class);
    }

    /**
     * Hibernate criteria
     */
    @Override
    public List<ILecture> findLecturesForLecturerAndCourse(String lecturerName, String course) {

        Session session = getEm().unwrap(Session.class);

        Criterion lecturerNameRest = lecturerName != null ? Restrictions.eq("l.lecturerName", lecturerName) : Restrictions.disjunction();
        Criterion courseRest = course != null ? Restrictions.eq("m.course", course) : Restrictions.disjunction();

        List<ILecture> lectures = session.createCriteria(Lecture.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
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
     * http://java.dzone.com/articles/hibernate-query-example-qbe
     * Version properties, identifiers and associations are ignored
     */
    @Override
    public List<ILecture> findLecturesForStatusFinishedStartandFinish(Date start, Date finish) {

        Session session = getEm().unwrap(Session.class);

        Lecture lecture = new Lecture();
        LectureStreaming lectureStreaming = new LectureStreaming();
        lecture.setLectureStreaming(lectureStreaming);

        lectureStreaming.setStatus(LectureStatus.FINISHED);
        lectureStreaming.setStart(start);
        lectureStreaming.setEnd(finish);

        Example queryByExample = Example.create(lectureStreaming);
        Criteria criteria = session.createCriteria(LectureStreaming.class).add(queryByExample).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return getLectures(criteria.list());
    }

    @Override
    public List<ILecture> findNotPayedLectures(String lecturerName) {
        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<ILecture> cq = cb.createQuery(ILecture.class);
        Root<Lecture> root = cq.from(Lecture.class);

        Join<Lecture, Lecturer> lecturer = root.join("lecturer");

        Predicate paidPredicate = cb.isFalse(root.<Boolean>get("isPaid"));
        Predicate lecturerNamePredicate = cb.equal(lecturer.get("lecturerName"), lecturerName);

        cq.select(root);
        cq.where(cb.and(paidPredicate, lecturerNamePredicate));
        TypedQuery<ILecture> query = getEm().createQuery(cq);
        return query.getResultList();
    }

    @Override
    public Integer findNumberOfPayedLectures(String lecturerName) {
        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Lecture> root = cq.from(Lecture.class);

        Join<Lecture, Lecturer> lecturer = root.join("lecturer");

        Predicate paidPredicate = cb.isTrue(root.<Boolean>get("isPaid"));
        Predicate lecturerNamePredicate = cb.equal(lecturer.get("lecturerName"), lecturerName);

        cq.select(cb.count(root));
        cq.where(cb.and(paidPredicate, lecturerNamePredicate));
        TypedQuery<Long> query = getEm().createQuery(cq);
        return query.getSingleResult().intValue();
    }

    private List<ILecture> getLectures(List<ILectureStreaming> streamings) {
        List<ILecture> lectures = new ArrayList<>();
        for (ILectureStreaming streaming : streamings) {
            lectures.add(streaming.getLecture());
        }
        return lectures;
    }
}
