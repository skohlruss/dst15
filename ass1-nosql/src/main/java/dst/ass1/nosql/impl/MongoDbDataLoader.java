package dst.ass1.nosql.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.util.Constants;
import dst.ass1.nosql.IMongoDbDataLoader;
import dst.ass1.nosql.MongoTestData;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class MongoDbDataLoader implements IMongoDbDataLoader {

    private static final int MONGO_PORT = 27017;
    private static final String MONGO_HOST = "localhost";
    private static final String MONGO_DATABASE = Constants.MONGO_DB_NAME;

    public static final String MONGO_COLLECTION_LECTURE = Constants.COLL_LECTUREDATA;
    public static final String MONGO_COLLECTION_LECTURE_ID = "lecture_id";
    public static final String MONGO_COLLECTION_LECTURE_FINISHED = Constants.PROP_LECTUREFINISHED;

    private MongoTestData testData = new MongoTestData();
    private EntityManager em;
    private DB db;


    public MongoDbDataLoader(EntityManager em) {
        this.em = em;

        try {
            this.db = new Mongo(MONGO_HOST, MONGO_PORT).getDB(MONGO_DATABASE);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() throws Exception {

        List<ILecture> sqlLectures = loadFromSQL();

        DBCollection col = db.createCollection(MONGO_COLLECTION_LECTURE, new BasicDBObject());
        col.ensureIndex(MONGO_COLLECTION_LECTURE_ID);

        for (ILecture sqlLecture : sqlLectures) {

            BasicDBObject mongoLecture = new BasicDBObject()
                .append(MONGO_COLLECTION_LECTURE_ID, sqlLecture.getId())
                .append(MONGO_COLLECTION_LECTURE_FINISHED, sqlLecture.getLectureStreaming().getEnd().getTime())
                .append(testData.getDataDescription(sqlLecture.getId()), JSON.parse(testData.getData(sqlLecture.getId())));

            col.insert(mongoLecture);
        }
    }

    private List<ILecture> loadFromSQL() {
        Query query = em.createNamedQuery(Constants.Q_ALLFINISHEDLECTURES);
        List<ILecture> result = (List<ILecture>) query.getResultList();
        return result;
    }
}
