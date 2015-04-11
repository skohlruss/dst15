package dst.ass2.ejb.dao.impl;

import dst.ass1.jpa.dao.impl.GenericDAOImpl;
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
public class AuditLogDAO extends GenericDAOImpl<IAuditLog> implements IAuditLogDAO {

    private EntityManager em;

    public AuditLogDAO(EntityManager em) {
        super(em, AuditLog.class);

        this.em = em;
    }
}
