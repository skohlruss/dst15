package dst.ass3.dto;

import java.io.Serializable;

public class InfoLectureWrapperDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long lectureWrapperId;

	public InfoLectureWrapperDTO() {
	}

	public InfoLectureWrapperDTO(Long lectureWrapperId) {
		super();
		this.lectureWrapperId = lectureWrapperId;
	}

	public Long getLectureWrapperId() {
		return lectureWrapperId;
	}

	public void setLectureWrapperId(Long lectureWrapperId) {
		this.lectureWrapperId = lectureWrapperId;
	}

}
