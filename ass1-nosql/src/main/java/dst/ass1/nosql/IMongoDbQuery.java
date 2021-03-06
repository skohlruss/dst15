package dst.ass1.nosql;

import java.util.List;

import com.mongodb.DBObject;

public interface IMongoDbQuery {

	public Long findFinishedByLectureId(Long id);

	public List<Long> findFinishedGt(Long time);

	public List<DBObject> mapReduceStreaming();

}
