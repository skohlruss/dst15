package dst.ass3.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LectureWrapper implements ILectureWrapper {

	private Long id;
	private Long lectureId;
	private LifecycleState state;
	private String classifiedBy;
	private LectureType type;

	public LectureWrapper() {
	}

	public LectureWrapper(Long lectureId, LifecycleState state, String classifiedBy,
			LectureType type) {
		super();
		this.lectureId = lectureId;
		this.state = state;
		this.classifiedBy = classifiedBy;
		this.type = type;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLectureId() {
		return lectureId;
	}

	public void setLectureId(Long lectureId) {
		this.lectureId = lectureId;
	}

	public LifecycleState getState() {
		return state;
	}

	public void setState(LifecycleState state) {
		this.state = state;
	}

	public String getClassifiedBy() {
		return classifiedBy;
	}

	public void setClassifiedBy(String classifiedBy) {
		this.classifiedBy = classifiedBy;
	}

	public LectureType getType() {
		return type;
	}

	public void setType(LectureType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "LectureWrapper [id=" + id + ", lectureId=" + lectureId + ", status=" + state
				+ ", classifiedBy=" + classifiedBy + ", type=" + type + "]";
	}

}
