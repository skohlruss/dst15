package dst.ass1.jpa.model;

import java.io.Serializable;

import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;
import dst.ass2.ejb.model.IPrice;

public class ModelFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	public IAddress createAddress() {
		// TODO
		return null;
	}

	public IModerator createModerator() {
		// TODO
		return null;
	}

	public IVirtualSchool createVirtualSchool() {
		// TODO
		return null;
	}

	public IClassroom createClassroom() {
		// TODO
		return null;
	}

	public IMetadata createMetadata() {
		// TODO
		return null;
	}

	public ILectureStreaming createLectureStreaming() {
		// TODO
		return null;
	}

	public IMOCPlatform createPlatform() {
		// TODO
		return null;
	}

	public ILecture createLecture() {
		// TODO
		return null;
	}

	public IMembership createMembership() {
		// TODO
		return null;
	}

	public IMembershipKey createMembershipKey() {
		// TODO
		return null;
	}

	public ILecturer createLecturer() {
		// TODO
		return null;
	}

	/*
	 * Please note that the following methods are not needed for assignment 1,
	 * but will later be used in assignment 2. Hence, you do not have to
	 * implement it for the first submission.
	 */

	public IAuditLog createAuditLog() {
		// TODO
		return null;
	}

	public IAuditParameter createAuditParameter() {
		// TODO
		return null;
	}

	public IPrice createPrice() {
		// TODO
		return null;
	}

}
