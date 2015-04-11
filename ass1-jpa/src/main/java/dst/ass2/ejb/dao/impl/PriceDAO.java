package dst.ass2.ejb.dao.impl;

import dst.ass1.jpa.dao.impl.GenericDAOImpl;
import dst.ass2.ejb.dao.IPriceDAO;
import dst.ass2.ejb.model.IPrice;
import dst.ass2.ejb.model.impl.Price;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 7.4.2015.
 */
public class PriceDAO extends GenericDAOImpl<IPrice> implements IPriceDAO {

    private EntityManager em;


    public PriceDAO(EntityManager em) {
        super(em, Price.class);

        this.em = em;
    }

    @Override
    public List<IPrice> findAllOrderByNumberOfHistoricalLectures() {
        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<IPrice> cq = cb.createQuery(IPrice.class);
        Root<Price> root = cq.from(Price.class);

        cq.select(root);
        cq.orderBy(cb.asc(root.get("nrOfHistoricalLectures")));

        TypedQuery<IPrice> query = em.createQuery(cq);
        return query.getResultList();
    }
}
