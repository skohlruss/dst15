package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.GenericDAO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 7.4.2015.
 */
public class GenericDAOImpl<T> implements GenericDAO<T> {

    private Class clazz;
    private EntityManager em;

    public GenericDAOImpl(EntityManager em, Class clazz) {
        this.em = em;
        this.clazz = clazz;
    }

    @Override
    public T findById(Long id) {
        return (T) em.find(clazz, id);
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);

        cq.select(root);
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }

    protected EntityManager getEm() {
        return em;
    }
}
