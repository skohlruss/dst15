package dst.ass3.jms.virtualschool.impl;

import dst.ass3.dto.ClassifyLectureWrapperDTO;
import dst.ass3.dto.LectureWrapperDTO;
import dst.ass3.jms.virtualschool.IVirtualSchool;
import dst.ass3.model.ILectureWrapper;
import dst.ass3.model.LifecycleState;
import org.apache.openejb.api.LocalClient;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * Created by pavol on 26.5.2015.
 */
@LocalClient
public class VirtualSchool implements IVirtualSchool, MessageListener {

    @Resource(mappedName = "serverQueue")
    private Queue serverQueue;
    @Resource(mappedName = "virtualSchoolQueue")
    private Queue virtualSchoolQueue;

    @Resource
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private MessageConsumer messageConsumer;

    private final String name;
    private IVirtualSchoolListener virtualSchoolListener;


    public VirtualSchool(String name) {
        this.name = name;
    }

    @Override
    public void start() {
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID(name);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            messageConsumer = session.createConsumer(virtualSchoolQueue);
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
    public void setVirtualSchoolListener(IVirtualSchoolListener listener) {
        this.virtualSchoolListener = listener;
    }

    @Override
    public void onMessage(Message message) {
        if (!(message instanceof ObjectMessage)) {
            return;
        }

        try {
            Object objectInMessage = ((ObjectMessage)message).getObject();
            if (!(objectInMessage instanceof LectureWrapperDTO)) {
                return;
            }

            ClassifyLectureWrapperDTO classifyLectureWrapperDTO = new ClassifyLectureWrapperDTO((ILectureWrapper) objectInMessage);
            classifyLectureWrapperDTO.setClassifiedBy(name);

            IVirtualSchoolListener.LectureWrapperDecideResponse response = virtualSchoolListener.decideLecture(classifyLectureWrapperDTO, name);
            if (response.resp == IVirtualSchoolListener.LectureWrapperResponse.ACCEPT) {
                classifyLectureWrapperDTO.setType(response.type);
                classifyLectureWrapperDTO.setState(LifecycleState.READY_FOR_STREAMING);
            } else {
                classifyLectureWrapperDTO.setState(LifecycleState.STREAMING_NOT_POSSIBLE);
            }

            /**
             * send message to server
             */
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ObjectMessage objectMessage = session.createObjectMessage(classifyLectureWrapperDTO);
            session.createProducer(serverQueue).send(objectMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
