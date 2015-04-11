package dst.ass1.jpa.dao;

import dst.ass1.jpa.model.ILecture;

import java.util.Date;
import java.util.List;

public interface ILectureDAO extends GenericDAO<ILecture> {

    List<ILecture> findLecturesForLecturerAndCourse(String lecturerName, String course);
    List<ILecture> findLecturesForStatusFinishedStartandFinish(Date start, Date finish);

    List<ILecture> findNotPayedLectures(String lecturerName);
    Integer findNumberOfPayedLectures(String lecturerName);
}
