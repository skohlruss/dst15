package dst.ass2.ejb.session;

import java.util.List;

import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.ILectureManagementBean;

public class LectureManagementBean implements ILectureManagementBean {

	// TODO

	@Override
	public void addLecture(Long platformId, Integer numStudents, String course,
			List<String> settings) throws AssignmentException {
		// TODO
	}

	@Override
	public void login(String lecturerName, String password)
			throws AssignmentException {
		// TODO
	}

	@Override
	public void removeLecturesForPlatform(Long platformId) {
		// TODO
	}

	@Override
	public void submitAssignments() throws AssignmentException {
		// TODO
	}

	@Override
	public List<AssignmentDTO> getCache() {
		// TODO
		return null;
	}

}
