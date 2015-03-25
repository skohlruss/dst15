package dst.ass1.nosql.impl;

import com.mongodb.*;
import dst.ass1.jpa.util.Constants;
import dst.ass1.nosql.IMongoDbQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class MongoDbQuery implements IMongoDbQuery {

    private DB db;

    public MongoDbQuery(DB db) {
        this.db = db;
    }

    @Override
    public Long findFinishedByLectureId(Long id) {
        DBCollection col = db.getCollection(Constants.COLL_LECTUREDATA);

        BasicDBObject queryByExample = new BasicDBObject()
            .append(MongoDbDataLoader.MONGO_COLLECTION_LECTURE_ID, id);

        DBCursor cursor = col.find(queryByExample);
        Long finished = null;
        while (cursor.hasNext()) {
            finished = Long.valueOf(cursor.next().get(Constants.PROP_LECTUREFINISHED).toString());
        }

        return finished;
    }

    @Override
    public List<Long> findFinishedGt(Long time) {
        DBCollection col = db.getCollection(Constants.COLL_LECTUREDATA);

        BasicDBObject queryByExample = new BasicDBObject()
                .append(Constants.PROP_LECTUREFINISHED, new BasicDBObject("$gt", time));

        DBCursor cursor = col.find(queryByExample, new BasicDBObject().append(MongoDbDataLoader.MONGO_COLLECTION_LECTURE_ID, 1));

        List<Long> ids = new ArrayList<>();
        while (cursor.hasNext()) {
            ids.add(Long.parseLong(cursor.next().get(MongoDbDataLoader.MONGO_COLLECTION_LECTURE_ID).toString()));
        }

        return ids;
    }

    @Override
    public List<DBObject> mapReduceStreaming() {
        DBCollection col = db.getCollection(Constants.COLL_LECTUREDATA);

        String map = "function(){for(var prop in this) if(prop === 'logs' || prop === 'matrix' || prop === 'alignment_block'){emit(prop, 1)}}";
        String reduce = "function(key, values){return values.length}";
//        String reduce1 =   "\"function(key, values) { var n = 0; for(var i = 0; i < values.length; i++) { n += values[i] } return n; }\"";

        MapReduceOutput mapReduced = col.mapReduce(map, reduce, null, MapReduceCommand.OutputType.INLINE, null);
        List<DBObject> result = new ArrayList<DBObject>();

        for (DBObject dbObject : mapReduced.results()) {
            result.add(dbObject);
        }

        return result;
    }
}
