package dst.ass1.jpa.model;

import dst.ass1.jpa.model.impl.*;
import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;
import dst.ass2.ejb.model.IPrice;

import java.io.Serializable;

public class ModelFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	public IAddress createAddress() {
		return new Address();
	}

	public IModerator createModerator() {
		return new Moderator();
	}

	public IVirtualSchool createVirtualSchool() {
		return new VirtualSchool();
	}

	public IClassroom createClassroom() {
		return new Classroom();
	}

	public IMetadata createMetadata() {
		return new Metadata();
	}

	public ILectureStreaming createLectureStreaming() {
		return new LectureStreaming();
	}

	public IMOCPlatform createPlatform() {
		return new MOCPlatform();
	}

	public ILecture createLecture() {
		return new Lecture();
	}

	public IMembership createMembership() {
		return new Membership();
	}

	public IMembershipKey createMembershipKey() {
		return new MembershipKey();
	}

	public ILecturer createLecturer() {
		return new Lecturer();
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
