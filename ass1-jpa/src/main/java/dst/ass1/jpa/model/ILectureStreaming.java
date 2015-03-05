package dst.ass1.jpa.model;

import java.util.Date;
import java.util.List;

public interface ILectureStreaming {

	public Long getId();

	public void setId(Long id);

	public Date getStart();

	public void setStart(Date start);

	public Date getEnd();

	public void setEnd(Date end);

	public LectureStatus getStatus();

	public void setStatus(LectureStatus status);

	public List<IClassroom> getClassrooms();

	public void setClassrooms(List<IClassroom> list);

	public void addClassroom(IClassroom classroom);

	public ILecture getLecture();

	public void setLecture(ILecture lecture);

}