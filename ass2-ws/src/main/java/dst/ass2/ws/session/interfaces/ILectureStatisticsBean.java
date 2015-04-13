package dst.ass2.ws.session.interfaces;

import dst.ass2.ws.Constants;
import dst.ass2.ws.IGetStatsRequest;
import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.session.exception.WebServiceException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;

/**
 * This is the interface of the statistics Web Service.
 */
@WebService(name = Constants.NAME,
    portName = Constants.PORT_NAME,
    targetNamespace = Constants.NAMESPACE,
    serviceName = Constants.SERVICE_NAME)
/**
 * Default configuration
 *
 * style - DOCUMENT(def) || RPC
 *       - http://java.globinch.com/enterprise-java/web-services/soap-binding-document-rpc-style-web-services-difference/
 *       - RPC style web service uses the names of the method and its parameters to generate XML structures that represent
 *          a method’s call stack. While document style indicates that the SOAP body contains a XML document which can be
 *          validated against pre-defined XML schema document
 *
 * use - LITERAL(def) || ENCODED
 *       - ENCODED - data are serialized according to a schema
 *
 * parameterStyle - WRAPPED(def) || BARE
 *
 * DOCUMENT/ENCODED - do not use, nobody follows it
 *
 */
//@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@SOAPBinding
public interface ILectureStatisticsBean {

    /**
     * Get statistics for a given platform.
     * @param request      The request object with parameters
     * @param platformName The name of the platform
     * @return statistics for the platform with the specified name.
     */
    @Action(input = Constants.NAMESPACE + "/" + Constants.SERVICE_NAME + "/getStatisticsForPlatformRequest",
            output = Constants.NAMESPACE + "/" + Constants.SERVICE_NAME +  "/getStatisticsForPlatformResponse",
            fault = {
                    @FaultAction(className = WebServiceException.class,
                            value = Constants.NAMESPACE + "/" + Constants.SERVICE_NAME + "/GetStatisticsForPlatform/WebServiceException")
            })
    @WebMethod(operationName = Constants.OP_GET_STATS)
    IGetStatsResponse getStatisticsForPlatform(
            IGetStatsRequest request,
            @WebParam(name = "name", header = true, partName = "name") String platformName)
            throws WebServiceException;
}
