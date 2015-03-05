package dst.ass1.jpa.dao;

import javax.persistence.EntityManager;

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
		// TODO
		return null;
	}

	public IModeratorDAO getModeratorDAO() {
		// TODO
		return null;
	}

	public IVirtualSchoolDAO getVirtualSchoolDAO() {
		// TODO
		return null;
	}

	public IClassroomDAO getClassroomDAO() {
		// TODO
		return null;
	}

	public IMetadataDAO getMetadataDAO() {
		// TODO
		return null;
	}

	public ILectureStreamingDAO getLectureStreamingDAO() {
		// TODO
		return null;
	}

	public ILectureDAO getLectureDAO() {
		// TODO
		return null;
	}

	public IMembershipDAO getMembershipDAO() {
		// TODO
		return null;
	}

	public ILecturerDAO getLecturerDAO() {
		// TODO
		return null;
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
