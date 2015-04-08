package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IMembershipDAO;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.impl.Membership;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class MembershipDAO extends GenericDAOImpl<IMembership> implements IMembershipDAO {


    public MembershipDAO(EntityManager em) {
        super(em, Membership.class);
    }

    /**
     * TODO - ask if lecturer or platform could be null
     * TODO - test lecturer and platform null
     */
    @Override
    public List<IMembership> findByLecturerAndPlatform(ILecturer lecturer, IMOCPlatform platform) {
        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<IMembership> cq = cb.createQuery(IMembership.class);
        Root<Membership> root = cq.from(Membership.class);


        Predicate lecturerPredicate = cb.disjunction();
        Predicate platformPredicate = cb.disjunction();

        if (lecturer != null) {
            if (lecturer.getId() != null) {
                lecturerPredicate = cb.equal(root.get("id").get("lecturer").get("id"), lecturer.getId());
            } else if (lecturer.getLecturerName() != null) {
                lecturerPredicate = cb.equal(root.get("id").get("lecturer").get("lecturerName"), lecturer.getLecturerName());
            }
        }
        if (platform != null) {
            if (platform.getId() != null) {
                platformPredicate = cb.equal(root.get("id").get("mocPlatform").get("id"), platform.getId());
            } else if (platform.getName() != null) {
                platformPredicate = cb.equal(root.get("id").get("mocPlatform").get("name"), platform.getName());
            }
        }

        cq.select(root);
        cq.where(cb.and(lecturerPredicate, platformPredicate));
        TypedQuery<IMembership> query = getEm().createQuery(cq);
        return query.getResultList();
    }
}
