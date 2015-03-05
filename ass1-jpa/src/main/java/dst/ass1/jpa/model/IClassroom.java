package dst.ass1.jpa.model;

import java.util.Date;
import java.util.List;

public interface IClassroom {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public Integer getStudentCapacity();

	public void setStudentCapacity(Integer studentCapacity);

	public String getRegion();

	public void setRegion(String region);

	public Date getActivated();

	public void setActivated(Date activated);

	public Date getLastUpdate();

	public void setLastUpdate(Date lastUpdate);

	public IVirtualSchool getVirtualSchool();

	public void setVirtualSchool(IVirtualSchool virtualSchool);

	public List<ILectureStreaming> getLectureStreamings();

	public void setLectureStreamings(List<ILectureStreaming> streamings);

	public void addLectureStreaming(ILectureStreaming streaming);

}