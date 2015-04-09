package dst.ass2.ws.session;

import dst.ass2.ws.IGetStatsRequest;
import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.session.exception.WebServiceException;
import dst.ass2.ws.session.interfaces.ILectureStatisticsBean;

public class LectureStatisticsBean implements ILectureStatisticsBean {

	public IGetStatsResponse getStatisticsForPlatform(
			IGetStatsRequest request, 
			String name) throws WebServiceException {
		// TODO
		return null;
	}

}
