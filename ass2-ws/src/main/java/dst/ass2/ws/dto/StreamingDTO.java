package dst.ass2.ws.dto;

import java.util.Date;

public class StreamingDTO {

	private Date startDate;
	private Date endDate;
	private int numStudents;

	public StreamingDTO() {
	}

	public StreamingDTO(Date startDate, Date endDate, int numStudents) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.numStudents = numStudents;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getNumStudents() {
		return numStudents;
	}

	public void setNumStudents(int numStudents) {
		this.numStudents = numStudents;
	}

	@Override
	public String toString() {
		return startDate.toString() + " -- " + endDate.toString() + " ("
				+ numStudents + ")";
	}

}
