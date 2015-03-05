package dst.ass1.jpa.util;

public class Constants {

	/* TYPES (CLASSES) */
	public static final String T_CLASSROOM = "Classroom";
	public static final String T_VIRTUALSCHOOL = "VirtualSchool";
	public static final String T_MOCPLATFORM = "MOCPlatform";
	public static final String T_LECTURER = "Lecturer";
	public static final String T_MEMBERSHIP = "Membership";
	public static final String T_LECTURE = "Lecture";
	public static final String T_MODERATOR = "Moderator";
	public static final String T_LECTURESTREAMING = "LectureStreaming";
	public static final String T_METADATA = "Metadata";
	public static final String T_PERSON = "Person";

	/* IDs (FOREIGN KEYS) */
	public static final String I_VIRTUALSCHOOL = T_VIRTUALSCHOOL.toLowerCase()
			+ "_id";
	public static final String I_MOCPLATFORM = T_MOCPLATFORM.toLowerCase()
			+ "_id";
	public static final String I_MODERATOR = T_MODERATOR.toLowerCase() + "_id";
	public static final String I_LECTURER = T_LECTURER.toLowerCase() + "_id";
	public static final String I_MEMBERSHIP = T_MEMBERSHIP.toLowerCase()
			+ "_id";
	public static final String I_CLASSROOM = T_CLASSROOM.toLowerCase() + "_id";
	public static final String I_CLASSROOMS = T_CLASSROOM.toLowerCase()
			+ "s_id";
	public static final String I_STREAMING = T_LECTURESTREAMING.toLowerCase()
			+ "_id";
	public static final String I_STREAMINGS = T_LECTURESTREAMING.toLowerCase()
			+ "s_id";
	public static final String I_LECTURE = T_LECTURE.toLowerCase() + "_id";
	public static final String I_METADATA = T_METADATA + "_id";

	/* MEMBER ATTRIBUTES */
	public static final String M_STUDENTCAPACITY = "studentCapacity";
	public static final String M_COSTSPERSTUDENT = "costsPerStudent";
	public static final String M_LASTUPDATE = "lastUpdate";
	public static final String M_LASTMAINTENANCE = "lastMaintenance";
	public static final String M_NEXTMMAINTENANCE = "nextMaintenance";
	public static final String M_ACTIVATED = "activated";
	public static final String M_ISPAID = "isPaid";
	public static final String M_MODERATOR = "moderator";
	public static final String M_SETTINGS_ORDER = "settings_ORDER";
	public static final String M_REGION = "region";
	public static final String M_COURSE = "course";
	public static final String M_LECTURERNAME = "lecturerName";
	public static final String M_PASSWORD = "password";
	public static final String M_COMPOSEDOF = "composedOf";
	public static final String M_PARTOF = "partOf";

	/* ASSOCIATION NAMES (FOR QUERIES) */
	public static final String A_MOCPLATFORM = "mocPlatform";
	public static final String A_STREAMING = "lectureStreaming";
	public static final String A_STREAMINGS = "lectureStreamings";
	public static final String A_CLASSROOMS = "classrooms";
	public static final String A_VIRTUALSCHOOL = "virtualSchool";
	public static final String A_VIRTUALSCHOOLS = "virtualSchools";
	public static final String A_MEMBERSHIPS = "memberships";
	public static final String A_LECTURE = "lecture";
	public static final String A_LECTURER = "lecturer";
	public static final String A_LECTURES = "lectures";
	public static final String A_METADATA = "metadata";
	public static final String A_COMPOSEDOF = "composedOf";

	/* NAMED QUERIES */
	public static final String Q_ALLFINISHEDLECTURES = "allFinishedLectures";
	public static final String Q_VIRTUALSCHOOLSOFMODERATOR = "virtualschoolsOfModerator";
	public static final String Q_LECTURERSWITHACTIVEMEMBERSHIP = "lecturersWithActiveMembership";
	public static final String Q_MOSTACTIVELECTURER = "mostActiveLecturer";

	/* JOIN TABLES */
	public static final String J_STREAMING_CLASSROOM = "streaming_classroom";
	public static final String J_METADATA_SETTINGS = "Metadata_settings";
	public static final String J_VIRTUALSCHOOL_COMPOSEDOF= "composed_of";

	/* NoSQL part */
	public static final String MONGO_DB_NAME = "dst";
	public static final String COLL_LECTUREDATA = "LectureData";
	public static final String PROP_LECTUREFINISHED = "lecture_finished";

}
