package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.LectureStatus;

public interface ILectureStreamingDAO extends GenericDAO<ILectureStreaming> {
	List<ILectureStreaming> findByStatus(LectureStatus status);
}
