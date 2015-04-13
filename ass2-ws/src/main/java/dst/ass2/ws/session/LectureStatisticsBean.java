package dst.ass2.ws.session;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.dao.ILectureStreamingDAO;
import dst.ass1.jpa.dao.IMOCPlatformDAO;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass2.ws.Constants;
import dst.ass2.ws.IGetStatsRequest;
import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.dto.StatisticsDTO;
import dst.ass2.ws.impl.GetStatsResponse;
import dst.ass2.ws.session.exception.WebServiceException;
import dst.ass2.ws.session.interfaces.ILectureStatisticsBean;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.soap.Addressing;
import java.util.List;


@Stateless
@Addressing
@WebService(name = Constants.NAME,
            portName = Constants.PORT_NAME,
            targetNamespace = Constants.NAMESPACE,
            serviceName = Constants.SERVICE_NAME)
public class LectureStatisticsBean implements ILectureStatisticsBean {

    @PersistenceContext(name = "dst")
    private EntityManager em;

    private ILectureStreamingDAO lectureStreamingDAO;
    private IMOCPlatformDAO platformDAO;


    @PostConstruct
    public void postConstruct() {
        DAOFactory daoFactory = new DAOFactory(em);
        this.lectureStreamingDAO = daoFactory.getLectureStreamingDAO();
        this.platformDAO = daoFactory.getPlatformDAO();
    }

    @Action(input = Constants.NAMESPACE + "/" + Constants.SERVICE_NAME + "/getStatisticsForPlatformRequest",
            output = Constants.NAMESPACE + "/" + Constants.SERVICE_NAME +  "/getStatisticsForPlatformResponse",
            fault = {
                    @FaultAction(className = WebServiceException.class,
                            value = Constants.NAMESPACE + "/" + Constants.SERVICE_NAME + "/GetStatisticsForPlatform/WebServiceException")
            })
    @WebMethod(operationName = Constants.OP_GET_STATS)
    public IGetStatsResponse getStatisticsForPlatform(
            IGetStatsRequest request,
            @WebParam(name = "name", header = true, partName = "name") String name)
            throws WebServiceException {

        /**
         * Throw exception if platform is not found
         */
        List<IMOCPlatform> platform = platformDAO.findByName(name);
        if (platform.size() == 0) {
            throw new WebServiceException("Platform not found request name = " + name);
        }

        System.out.println("name = " + name);
        System.out.println("request, max streamings = " + request.getMaxStreamings());

        /**
         * Create statistics
         */
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setName(name);
        List<ILectureStreaming> streamings = lectureStreamingDAO.findFinishedByPlatformName(name, request.getMaxStreamings());
        for (ILectureStreaming streaming: streamings) {

            statisticsDTO.addStreaming(streaming);
        }

        return new GetStatsResponse(statisticsDTO);
    }
}
