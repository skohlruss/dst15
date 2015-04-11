package dst.ass2.ejb.session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.dao.IClassroomDAO;
import dst.ass1.jpa.dao.ILecturerDAO;
import dst.ass1.jpa.dao.IMOCPlatformDAO;
import dst.ass1.jpa.model.*;
import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.interceptor.AuditInterceptor;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.ILectureManagementBean;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.interceptor.Interceptor;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
@Interceptors({AuditInterceptor.class})
public class LectureManagementBean implements ILectureManagementBean {

    @PersistenceContext(name = "dst")
    private EntityManager em;

    private DAOFactory daoFactory;
    private ModelFactory modelFactory;
    private ILecturerDAO lecturerDAO;
    private ILecturer lecturer;

    List<AssignmentDTO> assignmentDTOs = new ArrayList<>();
    private IClassroomDAO classroomDAO;
    private IMOCPlatformDAO mocPlatformDAO;


    @PostConstruct
    public void postConstruct() {
        this.daoFactory = new DAOFactory(em);
        this.modelFactory = new ModelFactory();
        this.lecturerDAO = daoFactory.getLecturerDAO();
        this.classroomDAO = daoFactory.getClassroomDAO();
        this.mocPlatformDAO = daoFactory.getPlatformDAO();
    }

    @Override
    public void addLecture(Long platformId, Integer numStudents, String course,
            List<String> settings) throws AssignmentException {

        AssignmentDTO assignmentDTO = new AssignmentDTO(platformId, numStudents, course, settings, new ArrayList<Long>());

        IMOCPlatform mocPlatform = mocPlatformDAO.findById(platformId);
        if (mocPlatform == null) {
            throw new AssignmentException("Platform not found id = " + platformId);
        }

        List<IClassroom> classrooms = getFreeClassrooms(platformId, Collections.EMPTY_SET);
        Integer requiredStudents = numStudents;
        Iterator<IClassroom> iterator = classrooms.iterator();
        while (iterator.hasNext()) {
            IClassroom classroom = iterator.next();

            // subtract required capacity
            requiredStudents -= classroom.getStudentCapacity();
            assignmentDTO.getClassroomIds().add(classroom.getId());
            iterator.remove();

            if (requiredStudents <= 0) {
                break;
            }
        }

        if (requiredStudents > 0) {
//            TODO - Ask custom exception?
            throw new AssignmentException("There are no enough classrooms for this lecture :"
                    + course + " , number of students = " + numStudents);
        }

        assignmentDTOs.add(assignmentDTO);
    }

    @Override
    public void login(String lecturerName, String password)
            throws AssignmentException {

        List<ILecturer> lecturers = lecturerDAO.findByName(lecturerName);
        if (lecturers.isEmpty()) {
            throw new AssignmentException("Lecturer not found " + lecturerName);
        }

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] enteredPasswordMd5 = md5.digest(password.getBytes());
        if (!Arrays.equals(lecturers.get(0).getPassword(), enteredPasswordMd5)) {
            throw new AssignmentException("Wrong password for " + lecturerName);
        }

        lecturer = lecturers.get(0);
        System.out.println(lecturerName + " successfully logged in!");
    }

    @Override
    public void removeLecturesForPlatform(Long platformId) {

        Iterator<AssignmentDTO> iterator = assignmentDTOs.iterator();

        while (iterator.hasNext()) {
            AssignmentDTO assignmentDTO = iterator.next();

            if (assignmentDTO.getPlatformId().equals(platformId)) {
                iterator.remove();
            }
        }
    }

    @Override
    @Remove(retainIfException = true)
    public void submitAssignments() throws AssignmentException {

        // lecturer has to been logged in before
        if (lecturer == null) {
            throw new AssignmentException("User was not logged in");
        }

        Set<Long> usedClassrooms = new HashSet<>();

        for (AssignmentDTO assignmentDTO: assignmentDTOs) {

            /**
             * New Entities to store
             */
            IMetadata metadata = createMetadata(assignmentDTO);
            ILectureStreaming lectureStreaming = createLectureStreaming();
            ILecture lecture = createLecture(metadata, lectureStreaming);

            Integer requiredStudents = assignmentDTO.getNumStudents();
            List<IClassroom> classrooms = getFreeClassrooms(assignmentDTO.getPlatformId(), usedClassrooms);
            Iterator<IClassroom> iterator = classrooms.iterator();
            while (iterator.hasNext()) {

                // subtract required capacity
                IClassroom classroom = iterator.next();
                requiredStudents -= classroom.getStudentCapacity();

                // remove classroom
                lectureStreaming.addClassroom(classroom);
                usedClassrooms.add(classroom.getId());
                iterator.remove();

                if (requiredStudents <= 0) {
                    break;
                }
            }

            if (requiredStudents >= 0) {
                throw new AssignmentException("There were no free classroom for lecture:" + metadata.getCourse());
            }

            em.persist(lecture);
        }
    }

    @Override
    public List<AssignmentDTO> getCache() {
        return assignmentDTOs;
    }

    private List<IClassroom> getFreeClassrooms(Long platformId, Set<Long> usedClassrooms) {

        IMOCPlatform mocPlatform = modelFactory.createPlatform();
        mocPlatform.setId(platformId);

        List<IClassroom> classrooms = classroomDAO.findByPlatform(mocPlatform);

        Iterator<IClassroom> iterator = classrooms.iterator();
        while (iterator.hasNext()) {
            IClassroom classroom = iterator.next();

            boolean freeClassroom = true;
            for (ILectureStreaming lectureStreaming: classroom.getLectureStreamings()) {

                if (usedClassrooms.contains(classroom.getId()) ||
                    lectureStreaming.getStatus() == LectureStatus.SCHEDULED ||
                    lectureStreaming.getStatus() == LectureStatus.STREAMING) {

                    freeClassroom = false;
                    break;
                }
            }

            if (!freeClassroom) {
                iterator.remove();
            }
        }

        return classrooms;
    }

    private ILecture createLecture(IMetadata metadata, ILectureStreaming streaming) {
        ILecture lecture = modelFactory.createLecture();
        lecture.setLectureStreaming(streaming);
        lecture.setMetadata(metadata);
        lecture.setLecturer(lecturer);

        return lecture;
    }

    private IMetadata createMetadata(AssignmentDTO assignmentDTO) {
        IMetadata metadata = modelFactory.createMetadata();
        metadata.setSettings(assignmentDTO.getSettings());
        metadata.setCourse(assignmentDTO.getCourse());

        return metadata;
    }

    private ILectureStreaming createLectureStreaming() {
        ILectureStreaming lectureStreaming = modelFactory.createLectureStreaming();
        lectureStreaming.setStatus(LectureStatus.STREAMING);
        lectureStreaming.setStart(new Date());

        return lectureStreaming;
    }
}
