package dst.ass2.ws.impl;

import dst.ass2.ws.IGetStatsRequest;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by pavol on 12.4.2015.
 */
public class GetStatsRequest implements IGetStatsRequest {

    private int streamings;


    @Override
    public int getMaxStreamings() {
        return streamings;
    }

    @Override
    public void setMaxStreamings(int maxStreamings) {
        this.streamings = maxStreamings;
    }

    public static class RequestParser extends XmlAdapter<GetStatsRequest, IGetStatsRequest> {

        @Override
        public IGetStatsRequest unmarshal(GetStatsRequest v) throws Exception {
            return v;
        }

        @Override
        public GetStatsRequest marshal(IGetStatsRequest v) throws Exception {
            return (GetStatsRequest) v;
        }
    }
}
