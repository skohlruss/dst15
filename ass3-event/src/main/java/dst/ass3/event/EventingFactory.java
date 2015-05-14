package dst.ass3.event;

import dst.ass3.model.ILectureWrapper;
import dst.ass3.model.LectureType;
import dst.ass3.model.LifecycleState;
import dst.ass3.dto.LectureWrapperDTO;

/**
 * Factory for instantiating objects used in the eventing tests
 * (interfaces IEventStreaming and ILecture).
 */
public class EventingFactory {


	public static IEventStreaming getInstance() {

		// TODO

		return null;
	}

	public static ILectureWrapper createLectureWrapper(Long id, Long lectureId, LifecycleState state,
			String classifiedBy, LectureType type) {
		return new LectureWrapperDTO(id, lectureId, state, classifiedBy, type);
	}
}
