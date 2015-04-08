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
public class LecturerDAO extends GenericDAOImpl<ILecturer> implements ILecturerDAO {

    public LecturerDAO(EntityManager em) {
        super(em, Lecturer.class);
    }

    @Override
    public List<ILecturer> findByName(String lecturerName) {
        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<ILecturer> cq = cb.createQuery(ILecturer.class);
        Root<Lecturer> root = cq.from(Lecturer.class);

        Predicate idPredicate = cb.equal(root.get("lecturerName"), lecturerName);
        cq.select(root);
        cq.where(cb.and(idPredicate));

        TypedQuery<ILecturer> query = getEm().createQuery(cq);
        return query.getResultList();
    }
}
