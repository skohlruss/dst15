package dst.ass1.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.DatabaseHelper;

public class Test_1b02 extends AbstractTest {

	@Test
	public void testLecturerLecturernameConstraint() throws SQLException,
			ClassNotFoundException {
		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			ILecturer ent8 = daoFactory.getLecturerDAO().findById(
					testData.entity8_1Id);
			ent8.setLecturerName(dst.ass1.jpa.util.test.TestData.N_ENT8_2);
			em.persist(ent8);
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
	public void testLecturerLecturernameConstraintJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_LECTURER, Constants.M_LECTURERNAME,
				false, em));
	}

	@Test
	public void testLecturerNotNullConstraint() {
		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			ILecturer ent8 = daoFactory.getLecturerDAO().findById(
					testData.entity8_1Id);
			ent8.setLecturerName(null);
			em.persist(ent8);
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
	public void testLecturerNotNullConstraintJdbc()
			throws ClassNotFoundException, SQLException {
		assertFalse(DatabaseHelper.isNullable(Constants.T_LECTURER, Constants.M_LECTURERNAME,
				em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
