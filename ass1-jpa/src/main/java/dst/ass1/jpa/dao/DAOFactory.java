package dst.ass1.jpa.dao;

import javax.persistence.EntityManager;

import dst.ass1.jpa.dao.impl.ClassroomDAO;
import dst.ass1.jpa.dao.impl.*;
import dst.ass2.ejb.dao.IAuditLogDAO;
import dst.ass2.ejb.dao.IPriceDAO;

public class DAOFactory {

    /*
     * HINT: When using the org.hibernate.Session in your DAOs you can extract
     * it from the EntityManager reference with e.g.,
     * em.unwrap(org.hibernate.Session.class). Do not store this
     * org.hibernate.Session in your DAOs, but unwrap it every time you actually
     * need it.
     */

    private EntityManager em;

    public DAOFactory(EntityManager em) {
        this.em = em;
    }

    public IMOCPlatformDAO getPlatformDAO() {
        return new MOCPlatformDAO(em);
    }

    public IModeratorDAO getModeratorDAO() {
        return new ModeratorDAO(em);
    }

    public IVirtualSchoolDAO getVirtualSchoolDAO() {
        return new VirtualSchoolDAO(em);
    }

    public IClassroomDAO getClassroomDAO() {
        return new ClassroomDAO(em);
    }

    public IMetadataDAO getMetadataDAO() {
        return new MetadataDAO(em);
    }

    public ILectureStreamingDAO getLectureStreamingDAO() {
        return new LectureStreamingDAO(em);
    }

    public ILectureDAO getLectureDAO() {
        return new LectureDAO(em);
    }

    public IMembershipDAO getMembershipDAO() {
        return new MembershipDAO(em);
    }

    public ILecturerDAO getLecturerDAO() {
        return new LecturerDAO(em);
    }

    /*
     * Please note that the following methods are not needed for assignment 1,
     * but will later be used in assignment 2. Hence, you do not have to
     * implement it for the first submission.
     */

    public IAuditLogDAO getAuditLogDAO() {
        // TODO
        return null;
    }

    public IPriceDAO getPriceDAO() {
        // TODO
        return null;
    }

}
