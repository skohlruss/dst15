package dst.ass2.ws.impl;

import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.dto.StatisticsDTO;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by pavol on 12.4.2015.
 */
public class GetStatsResponse implements IGetStatsResponse {

    private StatisticsDTO statisticsDTO;


    public GetStatsResponse() {
    }

    public GetStatsResponse(StatisticsDTO stat) {
        this.statisticsDTO = stat;
    }

    @Override
    public StatisticsDTO getStatistics() {
        return statisticsDTO;
    }

    public void setStatistics(StatisticsDTO statistics) {
        this.statisticsDTO = statistics;
    }


    public static class ResponseParser extends XmlAdapter<GetStatsResponse, IGetStatsResponse> {

        @Override
        public IGetStatsResponse unmarshal(GetStatsResponse v) throws Exception {
            return v;
        }

        @Override
        public GetStatsResponse marshal(IGetStatsResponse v) throws Exception {
            return (GetStatsResponse) v;
        }
    }
}
