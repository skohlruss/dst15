package dst.ass1;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.PersistenceUtil;
import dst.ass1.jpa.util.DatabaseHelper;
import dst.ass1.nosql.IMongoDbDataLoader;
import dst.ass1.nosql.IMongoDbQuery;
import dst.ass1.nosql.MongoDbFactory;

public abstract class AbstractNoSQLTest {

	private EntityManagerFactory emf;

	protected MongoDbFactory mongoDbFactory;
	protected EntityManager em;
	protected DAOFactory daoFactory;
	protected IMongoDbDataLoader mongoDbDataLoader;
	protected IMongoDbQuery mongoDbQuery;

	@Before
	public void setUp() throws Exception {
		emf = Persistence.createEntityManagerFactory(PersistenceUtil.PERSISTENCE_UNIT_NAME);
		em = emf.createEntityManager();
		daoFactory = new DAOFactory(em);

		DatabaseHelper.cleanTables(em);

		(new NoSQLTestData(em)).insertTestData();

		Mongo mongo = new Mongo();
		DB db = mongo.getDB(Constants.MONGO_DB_NAME);
		DBCollection collection = db.getCollection(Constants.COLL_LECTUREDATA);
		collection.drop();
		mongo.close();

		mongoDbFactory = new MongoDbFactory();
		mongoDbDataLoader = mongoDbFactory.createDataLoader(em);

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		List<ILecture> list = daoFactory.getLectureDAO().findAll();
		for (ILecture ent5 : list) {
			ent5.getLectureStreaming().setEnd(new Date());
			Thread.sleep(1000);
			ent5.getLectureStreaming().setStatus(LectureStatus.FINISHED);
			em.persist(ent5);
		}

		tx.commit();

		mongoDbDataLoader.loadData();
	}

	@After
	public void tearDown() throws Exception {
		DatabaseHelper.cleanTables(em);
		
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		em.clear();
		em.close();
		emf.close();

		Mongo mongo = new Mongo();
		DB db = mongo.getDB(Constants.MONGO_DB_NAME);
		DBCollection collection = db.getCollection(Constants.COLL_LECTUREDATA);
		collection.drop();
		mongo.close();
	}

}
