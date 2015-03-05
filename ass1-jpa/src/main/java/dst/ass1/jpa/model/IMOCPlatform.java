package dst.ass1.jpa.model;

import java.math.BigDecimal;
import java.util.List;

public interface IMOCPlatform {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public String getUrl();

	public void setUrl(String url);

	public BigDecimal getCostsPerStudent();

	public void setCostsPerStudent(BigDecimal costsPerStudent);

	public List<IMembership> getMemberships();

	public void setMemberships(List<IMembership> memberships);
	
	public void addMembership(IMembership membership);

	public List<IVirtualSchool> getVirtualSchools();

	public void setVirtualSchools(List<IVirtualSchool> virtualSchools);

	public void addVirtualSchool(IVirtualSchool virtualSchool);

}