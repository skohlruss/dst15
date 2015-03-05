package dst.ass1.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.DatabaseHelper;

public class TestTablesSetup extends AbstractTest {

	@Test
	public void testJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isTable(Constants.J_METADATA_SETTINGS, em));
	}

	@Test
	public void testTables1Jdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isTable(Constants.T_MEMBERSHIP, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_MEMBERSHIP,
				Constants.I_MOCPLATFORM, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_MEMBERSHIP,
				Constants.I_LECTURER, em));
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_MOCPLATFORM,
				Constants.I_MEMBERSHIP, em));
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_LECTURER,
				Constants.I_MEMBERSHIP, em));

	}

	@Test
	public void testTables2Jdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isTable(Constants.J_STREAMING_CLASSROOM, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.J_STREAMING_CLASSROOM,
				Constants.I_STREAMINGS, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.J_STREAMING_CLASSROOM,
				Constants.I_CLASSROOMS, em));
	}

	@Test
	public void testTables3Jdbc() throws ClassNotFoundException,
			SQLException {
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_MOCPLATFORM,
				Constants.I_VIRTUALSCHOOL, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_VIRTUALSCHOOL,
				Constants.I_MOCPLATFORM, em));
		assertTrue(DatabaseHelper.isIndex(Constants.T_VIRTUALSCHOOL,
				Constants.I_MOCPLATFORM, true, em));
	}

	@Test
	public void testTables4Jdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_CLASSROOM,
				Constants.I_VIRTUALSCHOOL, em));
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_VIRTUALSCHOOL,
				Constants.I_CLASSROOM, em));
		assertTrue(DatabaseHelper.isIndex(Constants.T_CLASSROOM,
				Constants.I_VIRTUALSCHOOL, true, em));
	}

	@Test
	public void testTables5Jdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_VIRTUALSCHOOL,
				Constants.I_MODERATOR, true, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_VIRTUALSCHOOL,
				Constants.I_MODERATOR, em));
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_MODERATOR,
				Constants.I_VIRTUALSCHOOL, em));
	}

	@Test
	public void testTables6Jdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isColumnInTableWithType(Constants.T_LECTURER, Constants.M_PASSWORD,
				"VARBINARY", "16", em));
	}

}
