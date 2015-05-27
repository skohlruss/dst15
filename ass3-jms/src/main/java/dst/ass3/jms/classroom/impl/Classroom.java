package dst.ass3.jms.classroom.impl;

import dst.ass3.dto.LectureWrapperDTO;
import dst.ass3.dto.StreamLectureWrapperDTO;
import dst.ass3.jms.classroom.IClassroom;
import dst.ass3.model.ILectureWrapper;
import dst.ass3.model.LectureType;
import dst.ass3.model.LifecycleState;
import org.apache.openejb.api.LocalClient;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * Created by pavol on 26.5.2015.
 */
@LocalClient
public class Classroom implements IClassroom, MessageListener {

    @Resource(mappedName = "serverQueue")
    private Queue serverQueue;
    @Resource(mappedName = "classroomTopic")
    private Topic classroomTopic;

    @Resource
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private MessageConsumer messageConsumer;

    private String id;
    private String name;
    private String virtualSchool;
    private LectureType lectureType;
    private IClassroomListener classroomListener;


    public Classroom(String name, String virtualSchool, LectureType type) {
        this.id = name + "." + virtualSchool;
        this.name = name;
        this.virtualSchool = virtualSchool;
        this.lectureType = type;
    }

    @Override
    public void start() {
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID(id);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageConsumer = session.createDurableSubscriber(classroomTopic, id,
                    "classifiedBy='" + virtualSchool + "' AND lectureType='" + lectureType.toString() + "'", false);
            messageConsumer.setMessageListener(this);
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            messageConsumer.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setClassroomListener(IClassroomListener listener) {
        this.classroomListener = listener;
    }

    @Override
    public void onMessage(Message message) {
        if (!(message instanceof ObjectMessage)) {
            return;
        }

        try {
            Object objectInMessage = ((ObjectMessage) message).getObject();
            if (!(objectInMessage instanceof LectureWrapperDTO)) {
                return;
            }

            StreamLectureWrapperDTO streamLectureWrapperDTO =
                    new StreamLectureWrapperDTO((ILectureWrapper) objectInMessage);

            classroomListener.waitTillStreamed(streamLectureWrapperDTO,
                    name,
                    streamLectureWrapperDTO.getType(),
                    streamLectureWrapperDTO.getClassifiedBy());

            streamLectureWrapperDTO.setState(LifecycleState.STREAMED);
            /**
             * send message to server
             */
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ObjectMessage objectMessage = session.createObjectMessage(streamLectureWrapperDTO);
            session.createProducer(serverQueue).send(objectMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
