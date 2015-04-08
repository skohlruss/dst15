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
public class MOCPlatformDAO extends GenericDAOImpl<IMOCPlatform> implements IMOCPlatformDAO {

    public MOCPlatformDAO(EntityManager em) {
        super(em, MOCPlatform.class);
    }

    @Override
    public List<IMOCPlatform> findByName(String name) {
        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<IMOCPlatform> cq = cb.createQuery(IMOCPlatform.class);
        Root<MOCPlatform> root = cq.from(MOCPlatform.class);

        cq.select(root);
        cq.where(cb.equal(root.get("name"), name));
        TypedQuery<IMOCPlatform> query = getEm().createQuery(cq);
        return query.getResultList();
    }
}
