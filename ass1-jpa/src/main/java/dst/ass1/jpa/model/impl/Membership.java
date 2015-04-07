package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
public class Membership implements IMembership, Serializable {

    @EmbeddedId
    private MembershipKey id;
    private Date registration;
    private Double discount;

    @Override
    public IMembershipKey getId() {
        return id;
    }

    @Override
    public void setId(IMembershipKey id) {
        this.id = (MembershipKey)id;
    }

    @Override
    public Date getRegistration() {
        return registration;
    }

    @Override
    public void setRegistration(Date registration) {
        this.registration = registration;
    }

    @Override
    public Double getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
