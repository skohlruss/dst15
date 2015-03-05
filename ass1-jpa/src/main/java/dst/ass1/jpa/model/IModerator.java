package dst.ass1.jpa.model;

import java.util.List;

public interface IModerator extends IPerson{
	
	public List<IVirtualSchool> getAdvisedVirtualSchools();

	public void setAdvisedVirtualSchools(List<IVirtualSchool> virtualSchools);

	public void addAdvisedVirtualSchool(IVirtualSchool virtualSchool);

}
