package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.model.IVirtualSchool;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
@DiscriminatorValue("m")
@NamedQueries({
        @NamedQuery(name = Constants.Q_VIRTUALSCHOOLSOFMODERATOR,
                query = "select m from Moderator as m"
        )
})
public class Moderator extends Person implements IModerator {

    @OneToMany(mappedBy = "moderator", targetEntity = VirtualSchool.class)
    private List<IVirtualSchool> virtualSchools = new ArrayList<>();

    @Override
    public List<IVirtualSchool> getAdvisedVirtualSchools() {
        return virtualSchools;
    }

    @Override
    public void setAdvisedVirtualSchools(List<IVirtualSchool> virtualSchools) {
        this.virtualSchools = virtualSchools;
    }

    @Override
    public void addAdvisedVirtualSchool(IVirtualSchool virtualSchool) {
        this.virtualSchools.add(virtualSchool);
    }
}
