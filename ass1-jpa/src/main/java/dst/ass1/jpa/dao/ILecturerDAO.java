package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.ILecturer;

public interface ILecturerDAO extends GenericDAO<ILecturer> {
	List<ILecturer> findByName(String lecturerName);
}
