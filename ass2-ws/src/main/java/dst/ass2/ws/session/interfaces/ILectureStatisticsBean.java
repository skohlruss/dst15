package dst.ass2.ws.session.interfaces;

import dst.ass2.ws.Constants;
import dst.ass2.ws.IGetStatsRequest;
import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.session.exception.WebServiceException;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * This is the interface of the statistics Web Service.
 */
@WebService(name = Constants.NAME,
    portName = Constants.PORT_NAME,
    targetNamespace = Constants.NAMESPACE,
    serviceName = Constants.SERVICE_NAME)
@SOAPBinding
public interface ILectureStatisticsBean {

    /**
     * Get statistics for a given platform.
     * @param request      The request object with parameters
     * @param platformName The name of the platform
     * @return statistics for the platform with the specified name.
     */
    @WebMethod(operationName = Constants.OP_GET_STATS)
    IGetStatsResponse getStatisticsForPlatform(
            IGetStatsRequest request,
            @WebParam(name = "name", header = true, partName = "name") String platformName)
            throws WebServiceException;
}
