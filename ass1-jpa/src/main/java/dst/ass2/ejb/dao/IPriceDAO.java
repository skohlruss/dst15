package dst.ass2.ejb.dao;

import dst.ass1.jpa.dao.GenericDAO;
import dst.ass2.ejb.model.IPrice;

import java.util.List;

public interface IPriceDAO extends GenericDAO<IPrice> {

    List<IPrice> findAllOrderByNumberOfHistoricalLectures();
}
