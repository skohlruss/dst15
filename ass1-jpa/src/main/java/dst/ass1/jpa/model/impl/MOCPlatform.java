package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IVirtualSchool;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
public class MOCPlatform implements IMOCPlatform {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    private String url;
    private BigDecimal costPerStudent;

    @OneToMany(mappedBy = "id.mocPlatform", targetEntity = Membership.class)
    private List<IMembership> memberships = new ArrayList<>();
    @OneToMany(mappedBy = "mocPlatform", targetEntity = VirtualSchool.class)
    private List<IVirtualSchool> virtualSchools = new ArrayList<>();


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public BigDecimal getCostsPerStudent() {
        return costPerStudent;
    }

    @Override
    public void setCostsPerStudent(BigDecimal costsPerStudent) {
        this.costPerStudent = costsPerStudent;
    }

    @Override
    public List<IMembership> getMemberships() {
        return memberships;
    }

    @Override
    public void setMemberships(List<IMembership> memberships) {
        this.memberships = memberships;
    }

    @Override
    public void addMembership(IMembership membership) {
        this.memberships.add(membership);
    }

    @Override
    public List<IVirtualSchool> getVirtualSchools() {
        return virtualSchools;
    }

    @Override
    public void setVirtualSchools(List<IVirtualSchool> virtualSchools) {
        this.virtualSchools = virtualSchools;
    }

    @Override
    public void addVirtualSchool(IVirtualSchool virtualSchool) {
        this.virtualSchools.add(virtualSchool);
    }
}
