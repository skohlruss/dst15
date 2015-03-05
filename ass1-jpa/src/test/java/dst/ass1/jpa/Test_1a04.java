package dst.ass1.jpa;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.DatabaseHelper;
import dst.ass1.jpa.util.test.TestData;

public class Test_1a04 extends AbstractTest {

	@Test
	public void testEntity3Constraint() {
		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			// Entity #3
			IClassroom ent3_1 = daoFactory.getClassroomDAO().findById(
					testData.entity3_1Id);
			ent3_1.setName(TestData.N_ENT3_2);
			em.persist(ent3_1);
			em.flush();

		} catch (PersistenceException e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				isConstraint = true;
			}

		} finally {
			tx.rollback();
		}

		assertTrue(isConstraint);

	}

	@Test
	public void testEntity3ConstraintJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_CLASSROOM, "name", false, em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}

}
