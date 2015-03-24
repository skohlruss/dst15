package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IClassroomDAO;
import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.impl.Classroom;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class ClassroomDAO implements IClassroomDAO {

    private EntityManager em;

    public ClassroomDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<IClassroom> findByPlatform(IMOCPlatform platform) {
        return null;
    }

    @Override
    public IClassroom findById(Long id) {
        return em.find(Classroom.class, id);
    }

    @Override
    public List<IClassroom> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IClassroom> cq = cb.createQuery(IClassroom.class);
        Root<IClassroom> root = cq.from(IClassroom.class);

        cq.select(root);
        TypedQuery<IClassroom> query = em.createQuery(cq);
        return query.getResultList();
    }
}
