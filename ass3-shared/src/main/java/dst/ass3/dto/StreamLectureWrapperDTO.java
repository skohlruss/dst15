package dst.ass3.dto;

import java.io.Serializable;

import dst.ass3.model.ILectureWrapper;
import dst.ass3.model.LectureType;
import dst.ass3.model.LifecycleState;

public class StreamLectureWrapperDTO implements Serializable {

	private static final long serialVersionUID = 2537272216199752190L;
	private Long id;
	private Long lectureId;
	private LifecycleState state;
	private String classifiedBy;
	private LectureType type;

	public StreamLectureWrapperDTO() {
	}

	public StreamLectureWrapperDTO(Long id, Long lectureId, LifecycleState state,
			String classifiedBy, LectureType type) {
		super();
		this.id = id;
		this.lectureId = lectureId;
		this.state = state;
		this.classifiedBy = classifiedBy;
		this.type = type;
	}

	public StreamLectureWrapperDTO(ILectureWrapper lecture) {
		super();
		this.id = lecture.getId();
		this.lectureId = lecture.getLectureId();
		this.state = lecture.getState();
		this.classifiedBy = lecture.getClassifiedBy();
		this.type = lecture.getType();
	}

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
		return "Lecture [id=" + id + ", lectureId=" + lectureId + ", classifiedBy=" + classifiedBy
				+ ", state=" + state + ", type=" + type + "]";
	}

}
