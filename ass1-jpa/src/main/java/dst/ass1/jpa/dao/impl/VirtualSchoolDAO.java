package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IVirtualSchoolDAO;
import dst.ass1.jpa.model.IVirtualSchool;
import dst.ass1.jpa.model.impl.VirtualSchool;

import javax.persistence.EntityManager;

/**
 * Created by pavol on 24.3.2015.
 */
public class VirtualSchoolDAO extends GenericDAOImpl<IVirtualSchool> implements IVirtualSchoolDAO {

    public VirtualSchoolDAO(EntityManager em) {
        super(em, VirtualSchool.class);
    }
}
