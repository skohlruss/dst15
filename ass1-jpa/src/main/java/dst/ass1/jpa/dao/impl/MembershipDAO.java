package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IMembershipDAO;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMOCPlatform;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class MembershipDAO implements IMembershipDAO {

    private EntityManager em;

    public MembershipDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public IMembership findById(Long id) {
        return em.find(IMembership.class, id);
    }

    @Override
    public List<IMembership> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IMembership> cq = cb.createQuery(IMembership.class);
        Root<IMembership> root = cq.from(IMembership.class);

        cq.select(root);
        TypedQuery<IMembership> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<IMembership> findByLecturerAndPlatform(ILecturer lecturer, IMOCPlatform platform) {
        return null;
    }
}
