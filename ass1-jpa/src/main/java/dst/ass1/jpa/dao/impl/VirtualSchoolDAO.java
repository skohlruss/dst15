package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IVirtualSchoolDAO;
import dst.ass1.jpa.model.IVirtualSchool;
import dst.ass1.jpa.model.impl.VirtualSchool;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class VirtualSchoolDAO implements IVirtualSchoolDAO {

    private EntityManager em;

    public VirtualSchoolDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public IVirtualSchool findById(Long id) {
        return em.find(VirtualSchool.class, id);
    }

    @Override
    public List<IVirtualSchool> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IVirtualSchool> cq = cb.createQuery(IVirtualSchool.class);
        Root<VirtualSchool> root = cq.from(VirtualSchool.class);

        cq.select(root);
        TypedQuery<IVirtualSchool> query = em.createQuery(cq);
        return query.getResultList();
    }
}
