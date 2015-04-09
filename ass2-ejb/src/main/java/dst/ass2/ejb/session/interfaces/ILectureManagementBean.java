package dst.ass2.ejb.session.interfaces;

import java.util.List;

import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.exception.AssignmentException;

public interface ILectureManagementBean {

	/**
	 * Adds a lecture with the given parameters to the temporary 
	 * list if there are enough free student resources left.
	 * 
	 * @param platformId
	 * @param numStudents
	 * @param course
	 * @param settings
	 * @throws AssignmentException
	 *             if the given platform does not exist or if 
	 *             there are not enough free student resources left.
	 */
	public void addLecture(Long platformId, Integer numStudents, String course,
			List<String> settings) throws AssignmentException;

	/**
	 * @return the list of temporarily assigned lectures.
	 */
	public List<AssignmentDTO> getCache();

	/**
	 * Removes temporary assigned lectures.
	 */
	public void removeLecturesForPlatform(Long id);

	/**
	 * Allows the lecturer to login.
	 * 
	 * @param lecturerName
	 * @param password
	 * @throws AssignmentException
	 *             if the lecturer does not exist or the given lecturerName/password
	 *             combination does not match.
	 */
	public void login(String lecturerName, String password)
			throws AssignmentException;

	/**
	 * Final submission of the temporary assigned lectures.
	 * 
	 * @throws AssignmentException
	 *             if the lecturer is not logged in or one of the lectures can't be
	 *             assigned anymore.
	 */
	public void submitAssignments() throws AssignmentException;

}
