package dst.ass2.ejb.management;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.model.IPrice;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * todo performance - locks
 */
@Singleton
public class PriceManagementBean implements IPriceManagementBean {

    @PersistenceContext(name = "dst")
    private EntityManager em;

    private ModelFactory modelFactory = new ModelFactory();
    private DAOFactory daoFactory;
    private List<IPrice> prices;


    @PostConstruct
    public void postConstruct() {
        this.daoFactory = new DAOFactory(em);
        this.prices = daoFactory.getPriceDAO().findAllOrderByNumberOfHistoricalLectures();
    }

    @Override
    public BigDecimal getPrice(Integer nrOfHistoricalLectures) {

        for (IPrice price: prices) {
            if (price.getNrOfHistoricalLectures().compareTo(nrOfHistoricalLectures) >= 0) {
                return price.getPrice();
            }
        }

        return new BigDecimal(0.0);
    }

    @Override
    @Lock(LockType.WRITE)
    public void setPrice(Integer nrOfHistoricalLectures, BigDecimal price) {

        Iterator<IPrice> iterator = prices.iterator();
        while (iterator.hasNext()) {
            IPrice priceIterator = iterator.next();
            // store to DB only if historical lectures are the same but price not
            if (priceIterator.getNrOfHistoricalLectures().equals(nrOfHistoricalLectures)) {
                if (priceIterator.getPrice().compareTo(price) != 0) {
                    em.merge(priceIterator);
                }

                return;
            }
        }

        IPrice newPrice = modelFactory.createPrice();
        newPrice.setNrOfHistoricalLectures(nrOfHistoricalLectures);
        newPrice.setPrice(price);
        em.persist(newPrice);
        prices.add(newPrice);
    }

    @Override
    public void clearCache() {
        prices.clear();
    }
}
