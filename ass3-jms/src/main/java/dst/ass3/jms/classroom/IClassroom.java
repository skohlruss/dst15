package dst.ass3.jms.classroom;

import dst.ass3.dto.StreamLectureWrapperDTO;
import dst.ass3.model.LectureType;

public interface IClassroom {
	/**
	 * Starts a Classroom
	 */
	void start();

	/**
	 * Stops the Classroom and cleans all resources (e.g.: close session,
	 * connection, etc.).
	 */
	void stop();

	/**
	 * Sets the Listener. This listener simulates the execution of the
	 * given lecture wrapper. Only one Listener should be in use at any time. Be sure to
	 * handle cases where messages are received but no listener is yet set
	 * (discard the message).
	 * 
	 * @param listener
	 */
	void setClassroomListener(IClassroomListener listener);

	interface IClassroomListener {
		/**
		 * Waits until the given Lecture wrapper has been streamed. You should call this
		 * method ASYNC (in a new Thread) because it may return after a long
		 * time.
		 * 
		 * @param lectureWrapper
		 *            the lecture to simulate execution
		 * @param classroomName
		 *            the name of the Classroom calling this listener
		 * @param acceptedType
		 *            the type this Classroom accepts
		 * @param virtualSchoolName
		 *            the name of the VirtualSchool this Classroom belongs too
		 */
		void waitTillStreamed(StreamLectureWrapperDTO lectureWrapper, String classroomName,
				LectureType acceptedType, String virtualSchoolName);
	}
}
