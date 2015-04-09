package dst.ass2.ejb.dto;

import java.io.Serializable;
import java.util.List;

public class AssignmentDTO implements Serializable {

	private static final long serialVersionUID = -6468125387570684307L;
	private Long platformId;
	private Integer numStudents;
	private String course;
	private List<String> settings;
	private List<Long> classroomIds;

	public AssignmentDTO(Long platformId, Integer numStudents,
			String course, List<String> settings, List<Long> classroomIds) {
		super();
		this.platformId = platformId;
		this.numStudents = numStudents;
		this.course = course;
		this.settings = settings;
		this.classroomIds = classroomIds;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public Integer getNumStudents() {
		return numStudents;
	}

	public void setNumStudents(Integer numStudents) {
		this.numStudents = numStudents;
	}

	public List<String> getSettings() {
		return settings;
	}

	public void setSettings(List<String> settings) {
		this.settings = settings;
	}

	public List<Long> getClassroomIds() {
		return classroomIds;
	}

	public void setClassroomIds(List<Long> classroomIds) {
		this.classroomIds = classroomIds;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classroomIds == null) ? 0 : classroomIds.hashCode());
		result = prime * result
				+ ((platformId == null) ? 0 : platformId.hashCode());
		result = prime * result
				+ ((numStudents == null) ? 0 : numStudents.hashCode());
		result = prime * result
				+ ((settings == null) ? 0 : settings.hashCode());
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssignmentDTO other = (AssignmentDTO) obj;
		if (classroomIds == null) {
			if (other.classroomIds != null)
				return false;
		} else if (!classroomIds.equals(other.classroomIds))
			return false;
		if (platformId == null) {
			if (other.platformId != null)
				return false;
		} else if (!platformId.equals(other.platformId))
			return false;
		if (numStudents == null) {
			if (other.numStudents != null)
				return false;
		} else if (!numStudents.equals(other.numStudents))
			return false;
		if (settings == null) {
			if (other.settings != null)
				return false;
		} else if (!settings.equals(other.settings))
			return false;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AssignmentDTO [platformId=" + platformId + ", numStudents="
				+ numStudents + ", course=" + course + ", settings=" + settings
				+ ", classroomIds=" + classroomIds + "]";
	}

}
