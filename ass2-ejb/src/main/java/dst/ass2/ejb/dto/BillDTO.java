package dst.ass2.ejb.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BillDTO implements Serializable {
	private static final long serialVersionUID = 1577495607705041680L;

	private List<BillPerLecture> bills;
	private BigDecimal totalPrice;
	private String lecturerName;

	public String getLecturerName() {
		return lecturerName;
	}

	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}

	public List<BillPerLecture> getBills() {
		return bills;
	}

	public void setBills(List<BillPerLecture> bills) {
		this.bills = bills;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public class BillPerLecture implements Serializable {
		private static final long serialVersionUID = 8656991004468599034L;

		private Long lectureId;
		private Integer numberOfClassrooms;
		private BigDecimal setupCosts;
		private BigDecimal streamingCosts;
		private BigDecimal lectureCosts;

		public Integer getNumberOfClassrooms() {
			return numberOfClassrooms;
		}

		public void setNumberOfClassrooms(Integer numberOfClassrooms) {
			this.numberOfClassrooms = numberOfClassrooms;
		}

		public BigDecimal getStreamingCosts() {
			return streamingCosts;
		}

		public void setStreamingCosts(BigDecimal streamingCosts) {
			this.streamingCosts = streamingCosts;
		}

		public BigDecimal getSetupCosts() {
			return setupCosts;
		}

		public void setSetupCosts(BigDecimal setupCosts) {
			this.setupCosts = setupCosts;
		}

		public BigDecimal getLectureCosts() {
			return lectureCosts;
		}

		public void setLectureCosts(BigDecimal lectureCosts) {
			this.lectureCosts = lectureCosts;
		}

		public Long getLectureId() {
			return lectureId;
		}

		public void setLectureId(Long lectureId) {
			this.lectureId = lectureId;
		}

		@Override
		public String toString() {
			return "BillPerLecture [lectureId=" + lectureId
					+ ", numberOfClassrooms=" + numberOfClassrooms
					+ ", setupCosts=" + setupCosts + ", streamingCosts="
					+ streamingCosts + ", lectureCosts=" + lectureCosts + "]";
		}

	}

	@Override
	public String toString() {
		return "BillDTO [bills=" + bills + ", totalPrice=" + totalPrice
				+ ", lecturerName=" + lecturerName + "]";
	}

}
