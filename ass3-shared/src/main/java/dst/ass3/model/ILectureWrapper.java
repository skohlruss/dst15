package dst.ass3.model;

public interface ILectureWrapper {

	public Long getId();

	public void setId(Long id);

	public Long getLectureId();

	public void setLectureId(Long lectureId);

	public LifecycleState getState();

	public void setState(LifecycleState status);

	public String getClassifiedBy();

	public void setClassifiedBy(String classifiedBy);

	public LectureType getType();

	public void setType(LectureType type);
}
