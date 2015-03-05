package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.model.IMOCPlatform;

public interface IClassroomDAO extends GenericDAO<IClassroom> {
	List<IClassroom> findByPlatform(IMOCPlatform platform);
}
