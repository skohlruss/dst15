package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IMOCPlatformDAO;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.impl.MOCPlatform;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class MOCPlatformDAO implements IMOCPlatformDAO {

    private EntityManager em;

    public MOCPlatformDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<IMOCPlatform> findByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IMOCPlatform> cq = cb.createQuery(IMOCPlatform.class);
        Root<MOCPlatform> root = cq.from(MOCPlatform.class);

        cq.select(root);
        cq.where(cb.equal(root.get("name"), name));
        TypedQuery<IMOCPlatform> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public IMOCPlatform findById(Long id) {
        return em.find(MOCPlatform.class, id);
    }

    @Override
    public List<IMOCPlatform> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IMOCPlatform> cq = cb.createQuery(IMOCPlatform.class);
        Root<MOCPlatform> root = cq.from(MOCPlatform.class);

        cq.select(root);
        TypedQuery<IMOCPlatform> query = em.createQuery(cq);
        return query.getResultList();
    }
}
