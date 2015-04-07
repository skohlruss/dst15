package dst.ass2.ejb.dao.impl;

import dst.ass2.ejb.dao.IAuditLogDAO;
import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.impl.AuditLog;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 7.4.2015.
 */
public class AuditLogDAO implements IAuditLogDAO {

    private EntityManager em;

    public AuditLogDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public IAuditLog findById(Long id) {
        return em.find(AuditLog.class, id);
    }

    @Override
    public List<IAuditLog> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IAuditLog> cq = cb.createQuery(IAuditLog.class);
        Root<AuditLog> root = cq.from(AuditLog.class);

        cq.select(root);
        TypedQuery<IAuditLog> query = em.createQuery(cq);
        return query.getResultList();
    }
}
