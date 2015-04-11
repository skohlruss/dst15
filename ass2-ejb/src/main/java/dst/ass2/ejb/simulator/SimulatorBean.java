package dst.ass2.ejb.simulator;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.dao.ILectureStreamingDAO;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.LectureStatus;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Singleton
public class SimulatorBean {

    @PersistenceContext(name = "dst")
    private EntityManager em;

    private DAOFactory daoFactory;
    private ILectureStreamingDAO lectureStreamingDAO;

    @PostConstruct
    public void postConstruct() {
        this.daoFactory = new DAOFactory(em);
        this.lectureStreamingDAO = daoFactory.getLectureStreamingDAO();
    }

    @Schedule(second = "*/5", hour = "*", minute = "*")
    public void simulate() {

        List<ILectureStreaming> lectureStreamings = lectureStreamingDAO.findByEndDateIsNull();
        for (ILectureStreaming lectureStreaming: lectureStreamings) {
            if (lectureStreaming.getStart().compareTo(new Date()) < 0) {

                lectureStreaming.setEnd(new Date());
                lectureStreaming.setStatus(LectureStatus.FINISHED);
                em.persist(lectureStreaming);
            }
        }
    }
}
