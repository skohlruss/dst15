package dst.ass3.jms.virtualschool;

import dst.ass3.dto.ClassifyLectureWrapperDTO;
import dst.ass3.model.LectureType;

public interface IVirtualSchool {
	/**
	 * Starts a VirtualSchool
	 */
	void start();

	/**
	 * Stops the VirtualSchool and cleans all resources (e.g.: close session,
	 * connection, etc.). Keep in mind that Listeners may be sleeping when stop
	 * is requested. Be sure to interrupt them and discard the results they
	 * might return, because the system is stopping already.
	 */
	void stop();

	/**
	 * Sets the Listener. Only one Listener should be in use at any
	 * time. Be sure to handle cases where messages are received but no
	 * listener is yet set (discard the message). The listeners may block
	 * forever, so be sure to interrupt them in stop().
	 * 
	 * @param listener
	 */
	void setVirtualSchoolListener(IVirtualSchoolListener listener);

	interface IVirtualSchoolListener {
		enum LectureWrapperResponse {
			ACCEPT, DENY
		}

		class LectureWrapperDecideResponse {
			public LectureWrapperResponse resp;
			public LectureType type;

			public LectureWrapperDecideResponse(LectureWrapperResponse resp,
					LectureType type) {
				this.resp = resp;
				this.type = type;
			}
		}

		/**
		 * Decide on the given lecture wrapper.
		 * 
		 * @param lectureWrapper
		 *            the lecture to decide
		 * @param virtualSchoolName
		 *            name of the VirtualSchool executing this listener
		 * @return ACCEPT + Type | DENY
		 */
		LectureWrapperDecideResponse decideLecture(ClassifyLectureWrapperDTO lectureWrapper, String virtualSchoolName);
	}
}
