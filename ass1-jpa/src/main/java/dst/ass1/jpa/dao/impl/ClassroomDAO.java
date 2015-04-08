package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IClassroomDAO;
import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.impl.Classroom;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class ClassroomDAO extends GenericDAOImpl<IClassroom> implements IClassroomDAO {

    public ClassroomDAO(EntityManager em) {
        super(em, Classroom.class);
    }

    /**
     * TODO ask - VirtualSchool partOf, composedOf - recursive join
     */
    @Override
    public List<IClassroom> findByPlatform(IMOCPlatform platform) {
        TypedQuery<IClassroom> query =
                getEm().createQuery("SELECT distinct classroom FROM MOCPlatform platform " +
                               "left join platform.virtualSchools vs " +
                               "left join vs.classrooms classroom " +
                               "where platform.id = :id",
                        IClassroom.class);

        query.setParameter("id", platform.getId());

        return query.getResultList();
    }
}
