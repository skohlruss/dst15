package dst.ass2.ejb.model.impl;

import dst.ass2.ejb.model.IPrice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by pavol on 7.4.2015.
 */
@Entity
public class Price implements IPrice, Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private Integer nrOfHistoricalLectures;
    private BigDecimal price;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Integer getNrOfHistoricalLectures() {
        return nrOfHistoricalLectures;
    }

    @Override
    public void setNrOfHistoricalLectures(Integer nrOfHistoricalLectures) {
        this.nrOfHistoricalLectures = nrOfHistoricalLectures;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
