package dst.ass3.dto;

import java.io.Serializable;

public class NewLectureWrapperDTO implements Serializable {

	private static final long serialVersionUID = 843972285375484461L;
	private Long lectureId;

	public NewLectureWrapperDTO() {
	}

	public NewLectureWrapperDTO(Long lectureId) {
		this.lectureId = lectureId;
	}

	public Long getLectureId() {
		return lectureId;
	}

	public void setLectureId(Long lectureId) {
		this.lectureId = lectureId;
	}

}
