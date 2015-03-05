package dst.ass1.jpa;

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

public class Test_1b01 extends AbstractTest {

	@Test
	public void testLecturerAccountNoBankCodeConstraint() throws SQLException,
			ClassNotFoundException {

		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			ILecturer ent8 = daoFactory.getLecturerDAO().findById(
					testData.entity8_1Id);
			ent8.setAccountNo("account2");
			ent8.setBankCode("bank2");
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
	public void testLecturerAccountNoBankCodeConstraintJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_LECTURER, "accountNo", false,
				em));
		assertTrue(DatabaseHelper.isIndex(Constants.T_LECTURER, "bankCode", false,
				em));
		assertTrue(DatabaseHelper.isComposedIndex(Constants.T_LECTURER,
				"accountNo", "bankCode", em));

		assertTrue(DatabaseHelper.isNullable(Constants.T_LECTURER, "accountNo", em));
		assertTrue(DatabaseHelper.isNullable(Constants.T_LECTURER, "bankCode", em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
