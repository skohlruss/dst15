package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ILecturerDAO;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.impl.Lecturer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class LecturerDAO implements ILecturerDAO {

    private EntityManager em;

    public LecturerDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ILecturer> findByName(String lecturerName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ILecturer> cq = cb.createQuery(ILecturer.class);
        Root<Lecturer> root = cq.from(Lecturer.class);

        Predicate idPredicate = cb.equal(root.get("lecturerName"), lecturerName);
        cq.select(root);
        cq.where(cb.and(idPredicate));

        TypedQuery<ILecturer> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public ILecturer findById(Long id) {
        return em.find(Lecturer.class, id);
    }

    @Override
    public List<ILecturer> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ILecturer> cq = cb.createQuery(ILecturer.class);
        Root<Lecturer> root = cq.from(Lecturer.class);

        cq.select(root);
        TypedQuery<ILecturer> query = em.createQuery(cq);
        return query.getResultList();
    }
}
