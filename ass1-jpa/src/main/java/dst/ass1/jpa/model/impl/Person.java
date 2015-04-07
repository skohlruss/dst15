package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IPerson;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public class Person implements IPerson, Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;

    @Embedded
    private IAddress address;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public IAddress getAddress() {
        return address;
    }

    @Override
    public void setAddress(IAddress address) {
        this.address = address;
    }
}
