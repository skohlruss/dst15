package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ILectureDAO;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.impl.Lecture;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    @Override
    public List<ILecture> findLecturesForLecturerAndCourse(String lecturerName, String course) {
        return null;
    }

    @Override
    public List<ILecture> findLecturesForStatusFinishedStartandFinish(Date start, Date finish) {
        return null;
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
