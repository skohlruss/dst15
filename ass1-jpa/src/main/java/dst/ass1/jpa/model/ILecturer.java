package dst.ass1.jpa.model;

import java.util.List;

public interface ILecturer extends IPerson {

	public String getLecturerName();

	public void setLecturerName(String lecturerName);

	public byte[] getPassword();

	public void setPassword(byte[] password);

	public String getAccountNo();

	public void setAccountNo(String accountNo);

	public String getBankCode();

	public void setBankCode(String bankCode);

	public List<ILecture> getLectures();

	public void setLectures(List<ILecture> lectures);

	public void addLecture(ILecture lecture);

	public List<IMembership> getMemberships();

	public void setMemberships(List<IMembership> memberships);

	public void addMembership(IMembership membership);

}