package dst.ass1.jpa.model;

import java.util.Date;
import java.util.List;

public interface IVirtualSchool {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public Date getLastMaintenance();

	public void setLastMaintenance(Date lastMaintenance);

	public Date getNextMaintenance();

	public void setNextMaintenance(Date nextMaintenance);

	public List<IVirtualSchool> getComposedOf();

	public void setComposedOf(List<IVirtualSchool> composedOf);

	public void addComposedOf(IVirtualSchool virtualSchool);

	public List<IVirtualSchool> getPartOf();

	public void setPartOf(List<IVirtualSchool> partOf);

	public void addPartOf(IVirtualSchool virtualSchool);

	public List<IClassroom> getClassrooms();

	public void setClassrooms(List<IClassroom> classrooms);

	public void addClassroom(IClassroom classroom);

	public IModerator getModerator();

	public void setModerator(IModerator moderator);

	public IMOCPlatform getMOCPlatform();

	public void setMOCPlatform(IMOCPlatform platform);

}