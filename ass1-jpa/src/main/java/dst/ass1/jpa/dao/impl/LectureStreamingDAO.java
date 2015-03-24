package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ILectureStreamingDAO;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.model.impl.LectureStreaming;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class LectureStreamingDAO implements ILectureStreamingDAO {

    private EntityManager em;

    public LectureStreamingDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ILectureStreaming> findByStatus(LectureStatus status) {
        return null;
    }

    @Override
    public ILectureStreaming findById(Long id) {
        return em.find(LectureStreaming.class, id);
    }

    @Override
    public List<ILectureStreaming> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ILectureStreaming> cq = cb.createQuery(ILectureStreaming.class);
        Root<LectureStreaming> root = cq.from(LectureStreaming.class);

        cq.select(root);
        TypedQuery<ILectureStreaming> query = em.createQuery(cq);
        return query.getResultList();
    }
}
