package dst.ass3.jms;

import dst.ass3.dto.ClassifyLectureWrapperDTO;
import dst.ass3.dto.InfoLectureWrapperDTO;
import dst.ass3.dto.LectureWrapperDTO;
import dst.ass3.dto.NewLectureWrapperDTO;
import dst.ass3.model.ILectureWrapper;
import dst.ass3.model.LectureType;
import dst.ass3.model.LectureWrapper;
import dst.ass3.model.LifecycleState;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by pavol on 26.5.2015.
 *
 * mappedName - will listen on specific queue
 */
@MessageDriven(mappedName = "serverQueue")
public class Server implements MessageListener {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource(mappedName = "schedulerQueue")
    private Queue schedulerQueue;
    @Resource(mappedName = "virtualSchoolQueue")
    private Queue virtualSchoolQueue;
    @Resource(mappedName = "classroomTopic")
    private Topic classroomTopic;

    @Resource
    private ConnectionFactory connectionFactory;
    private Connection connection;

    @PostConstruct
    public void postConstruct() {
        try {
            connection = connectionFactory.createConnection();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        if (!(message instanceof ObjectMessage)) {
            return;
        }

        try {
            Object objectInMessage = ((ObjectMessage) message).getObject();

            /**
             * Route messages
             */
            if (objectInMessage instanceof NewLectureWrapperDTO) {
                newLecture((NewLectureWrapperDTO) objectInMessage);
            }

            if (objectInMessage instanceof InfoLectureWrapperDTO) {
                infoLecture((InfoLectureWrapperDTO) objectInMessage);
            }

            if (objectInMessage instanceof ClassifyLectureWrapperDTO) {
                classifyLecture((ClassifyLectureWrapperDTO) objectInMessage);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    private void newLecture(NewLectureWrapperDTO newLectureWrapperDTO) throws JMSException {
        ILectureWrapper entity = new LectureWrapper();
        entity.setLectureId(newLectureWrapperDTO.getLectureId());
        entity.setState(LifecycleState.ASSIGNED);
        entity.setType(LectureType.UNCLASSIFIED);
        entityManager.persist(entity);

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        ObjectMessage objectMessage = session.createObjectMessage(new LectureWrapperDTO(entity));
        objectMessage.setStringProperty("action", "new");
        session.createProducer(virtualSchoolQueue).send(objectMessage);
        session.createProducer(schedulerQueue).send(objectMessage);
    }

    private void infoLecture(InfoLectureWrapperDTO infoLectureWrapperDTO) throws JMSException {
        ILectureWrapper lectureWrapper = getLecture(infoLectureWrapperDTO.getLectureWrapperId());

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        ObjectMessage objectMessage = session.createObjectMessage(new LectureWrapperDTO(lectureWrapper));
        objectMessage.setStringProperty("action", "info");
        session.createProducer(schedulerQueue).send(objectMessage);
    }

    private void classifyLecture(ClassifyLectureWrapperDTO classifyLectureWrapperDTO) throws JMSException {
        ILectureWrapper lectureWrapper = getLecture(classifyLectureWrapperDTO.getId());

        lectureWrapper.setState(classifyLectureWrapperDTO.getState());
        lectureWrapper.setClassifiedBy(classifyLectureWrapperDTO.getClassifiedBy());
        lectureWrapper.setType(classifyLectureWrapperDTO.getType()); // set or not?

        if (classifyLectureWrapperDTO.getState() == LifecycleState.READY_FOR_STREAMING) {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ObjectMessage objectMessage = session.createObjectMessage(new LectureWrapperDTO(lectureWrapper));
            session.createProducer(classroomTopic).send(objectMessage);

        } else  {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ObjectMessage objectMessage = session.createObjectMessage(new LectureWrapperDTO(lectureWrapper));
            objectMessage.setStringProperty("action", "deny");
            session.createProducer(schedulerQueue).send(objectMessage);
        }
    }

    /**
     * Return lecture wrapper from DB
     * @param lectureId
     * @return
     */
    private ILectureWrapper getLecture(Long lectureId) {
        return entityManager.find(LectureWrapper.class, lectureId);
    }
}
