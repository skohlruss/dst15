package dst.ass2.ejb.management;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.model.IPrice;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The javax.ejb.Singleton annotation is used to specify that the enterprise bean
 * implementation class is a singleton session bean:
 *
 * Singletons that use bean-managed concurrency allow full concurrent access to all the
 * business and timeout methods in the singleton. The developer of the singleton is
 * responsible for ensuring that the state of the singleton is synchronized across all
 * clients. Developers who create singletons with bean-managed concurrency are allowed
 * to use the Java programming language synchronization primitives, such as synchronization
 * and volatile, to prevent errors during concurrent access.
 *
 * There is no @Lock annotation on the class or business method, so the default
 * of @Lock(WRITE) is applied to the only business method.
 *
 * Concurrency - container management or bean management
 *
 * Implicit is container management - optimistic write
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
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
    @Lock(LockType.WRITE)
    public void clearCache() {
        prices.clear();
    }
}
