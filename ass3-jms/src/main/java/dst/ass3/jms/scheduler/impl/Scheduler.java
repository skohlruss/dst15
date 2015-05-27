package dst.ass3.jms.scheduler.impl;

import dst.ass3.dto.InfoLectureWrapperDTO;
import dst.ass3.dto.LectureWrapperDTO;
import dst.ass3.dto.NewLectureWrapperDTO;
import dst.ass3.jms.scheduler.IScheduler;
import org.apache.openejb.api.LocalClient;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * Created by pavol on 26.5.2015.
 */
@LocalClient
public class Scheduler implements IScheduler, MessageListener {

    @Resource(name = "serverQueue")
    private Queue serverQueue;
    @Resource(name = "schedulerQueue")
    private Queue schedulerQueue;

    @Resource
    private ConnectionFactory connectionFactory;
    private Connection connection;
    /**
     * A client uses a MessageConsumer object to receive messages from a destination
     */
    private MessageConsumer messageConsumer;

    private ISchedulerListener schedulerListener;


    @Override
    public void start() {
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageConsumer = session.createConsumer(schedulerQueue);
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
            connection.stop();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void assign(long lectureId) {

        NewLectureWrapperDTO newLectureWrapperDTO = new NewLectureWrapperDTO(lectureId);

        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ObjectMessage objectMessage = session.createObjectMessage(newLectureWrapperDTO);
            session.createProducer(serverQueue).send(objectMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void info(long lectureWrapperId) {
        InfoLectureWrapperDTO infoLectureWrapperDTO = new InfoLectureWrapperDTO(lectureWrapperId);

        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ObjectMessage objectMessage = session.createObjectMessage(infoLectureWrapperDTO);
            session.createProducer(serverQueue).send(objectMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSchedulerListener(ISchedulerListener listener) {
        this.schedulerListener = listener;
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

            LectureWrapperDTO lectureWrapperDTO = (LectureWrapperDTO) objectInMessage;

            if (message.getStringProperty("action").equals("new")) {
                schedulerListener.notify(ISchedulerListener.InfoType.CREATED, lectureWrapperDTO);
            }
            if (message.getStringProperty("action").equals("info")) {
                schedulerListener.notify(ISchedulerListener.InfoType.INFO, lectureWrapperDTO);
            }
            if (message.getStringProperty("action").equals("deny")) {
                schedulerListener.notify(ISchedulerListener.InfoType.DENIED, lectureWrapperDTO);
            }
            if (message.getStringProperty("action").equals("stream")) {
                schedulerListener.notify(ISchedulerListener.InfoType.STREAMED, lectureWrapperDTO);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
