package dst.ass2.ws.session.interfaces;

import dst.ass2.ws.IGetStatsRequest;
import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.session.exception.WebServiceException;

/**
 * This is the interface of the statistics Web Service.
 */
public interface ILectureStatisticsBean {

	/**
	 * Get statistics for a given platform.
	 * @param request      The request object with parameters
	 * @param platformName The name of the platform
	 * @return statistics for the platform with the specified name.
	 */
	IGetStatsResponse getStatisticsForPlatform(
			IGetStatsRequest request, 
			String platformName) throws WebServiceException;

}
