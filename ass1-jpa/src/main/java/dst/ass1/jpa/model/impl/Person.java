package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IPerson;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by pavol on 24.3.2015.
 *
 * Inheritance
 * SINGLE_TABLE - everything is stored in one table - good performance
 *                cannot be used because eg. lecturer's constraints
 *                will be propagated to Moderator entity (unique bankCode..)
 *
 * JOIN_TABLE - super class attributes are stored in separate table - join every time
 *              slow solution, and we don't need to work with superclass
 *              query any class join are required
 *
 * TABLE_PER_CLASS - dedicated table for every class, all attributes are stored (class + super class)
 *
 * MappedSupperClass - as table per class but do not allow query, persist, relationship to super class
 *
 */
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@DiscriminatorColumn(name = "type") // for SINGLE_TABLE
@MappedSuperclass
public abstract class Person implements IPerson, Serializable {

    @Id
    @GeneratedValue
    protected Long id;
    protected String firstName;
    protected String lastName;

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
