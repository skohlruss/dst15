package dst.ass1.jpa.model;

public interface ILecture {

	public Long getId();

	public void setId(Long id);
	
	public Integer getStreamingTime();

	public void setStreamingTime(Integer streamingTime);

	public Integer getAttendingStudents();

	public void setAttendingStudents(Integer students);

	public boolean isPaid();

	public void setPaid(boolean isPaid);

	public IMetadata getMetadata();

	public void setMetadata(IMetadata metadata);

	public ILecturer getLecturer();

	public void setLecturer(ILecturer lecturer);

	public ILectureStreaming getLectureStreaming();

	public void setLectureStreaming(ILectureStreaming streaming);

}