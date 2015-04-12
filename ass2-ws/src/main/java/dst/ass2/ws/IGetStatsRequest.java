package dst.ass2.ws;

import dst.ass2.ws.impl.GetStatsRequest;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This interface defines the getters and setters of the 
 * GetStatsRequest Web service request object.
 */
@XmlJavaTypeAdapter(GetStatsRequest.RequestParser.class)
public interface IGetStatsRequest {

	/**
	 * @return maximum number of streamings in the statistics
	 */
	int getMaxStreamings();

	/**
	 * @param maxStreamings maximum number of streamings in the statistics
	 */
	void setMaxStreamings(int maxStreamings);

}
