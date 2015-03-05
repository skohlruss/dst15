package dst.ass1.jpa;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.DatabaseHelper;

public class Test_2d extends AbstractTest {

	@Test
	public void testLecturerPasswordIdx() throws SQLException,
			ClassNotFoundException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_LECTURER, "password", true,
				em));
	}
}
