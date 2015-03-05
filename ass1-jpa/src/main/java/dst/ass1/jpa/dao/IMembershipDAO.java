package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.IMembership;

public interface IMembershipDAO extends GenericDAO<IMembership> {
	List<IMembership> findByLecturerAndPlatform(ILecturer lecturer, IMOCPlatform platform);
}
