package dst.ass2.ws;

import dst.ass2.ws.dto.StatisticsDTO;
import dst.ass2.ws.impl.GetStatsResponse;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This interface defines the getters and setters of the 
 * GetStatsResponse Web service response object.
 */
@XmlJavaTypeAdapter(GetStatsResponse.ResponseParser.class)
public interface IGetStatsResponse {

	StatisticsDTO getStatistics();


	void setStatistics(StatisticsDTO statistics);
}
