package dst.ass3.event;

/**
 * Constants used for eventing tests.
 */
public class Constants {

	/** Event name for the lecture-wrapper. */
	public static final String EVENT_LECTURE = "LectureWrapper";
	
	/** Event name for lecture assigned events. */
	public static final String EVENT_LECTURE_ASSIGNED = "LectureAssigned";

	/** Event name for lecture streamed events. */
	public static final String EVENT_LECTURE_STREAMED = "LectureStreamed";

	/** Event name for lecture duration events. */
	public static final String EVENT_LECTURE_DURATION = "LectureDuration";

	/** Event name for result of average lecture duration query. */
	public static final String EVENT_AVG_LECTURE_DURATION = "AvgLectureDuration";
	
	/** Property names */
	public static final String EVENT_PROP_LECTURE_ID = "lectureId";
	public static final String EVENT_PROP_TIMESTAMP = "timestamp";
	public static final String EVENT_PROP_DURATION = "duration";

}
