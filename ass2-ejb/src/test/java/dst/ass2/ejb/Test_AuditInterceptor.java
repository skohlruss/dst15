package dst.ass2.ejb;

import static dst.ass2.ejb.util.EJBUtils.lookup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.jpa.util.test.TestData;
import dst.ass2.EJBBaseTest;
import dst.ass2.ejb.dto.AuditLogDTO;
import dst.ass2.ejb.session.LectureManagementBean;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.ILectureManagementBean;

public class Test_AuditInterceptor extends EJBBaseTest {

	private static final String METHOD_SUBMIT_ASSIGNMENTS = "submitAssignments";
	private static final String METHOD_ADD_LECTURE = "addLecture";
	private static final String METHOD_LOGIN = "login";

	@Before
	public void setUp() {
		testingBean.insertTestData();
		managementBean.clearPriceCache();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testAuditLog() throws Exception {

		// get all available ids from db
		List<Long> ids = getPlatformIdsFromDB();

		assertEquals(2, ids.size());

		// add prices to management bean
		managementBean_addPrices();

		// add lectures
		addLecturesForLecturer1(ids);

		// check the produced audit logs
		List<AuditLogDTO> auditLogs = managementBean.getAuditLogs();
		assertEquals(4, auditLogs.size());

		List<AuditLogDTO> temp = getAuditLogsForMethod(METHOD_LOGIN, auditLogs);
		assertEquals(1, temp.size());
		AuditLogDTO audit = temp.get(0);

		assertNotNull(audit.getParameters());
		assertEquals(2, audit.getParameters().size());
		assertNotNull(audit.getInvocationTime());

		temp = getAuditLogsForMethod(METHOD_ADD_LECTURE, auditLogs);
		assertEquals(2, temp.size());

		for (AuditLogDTO auditLogDTO : temp) {
			assertNotNull(auditLogDTO.getParameters());
			assertEquals(4, auditLogDTO.getParameters().size());
			assertNotNull(auditLogDTO.getInvocationTime());
		}

		temp = getAuditLogsForMethod(METHOD_SUBMIT_ASSIGNMENTS, auditLogs);
		assertEquals(1, temp.size());
		audit = temp.get(0);

		assertNotNull(audit.getParameters());
		assertEquals(0, audit.getParameters().size());
		assertNotNull(audit.getInvocationTime());
	}

	@Test
	public void testAuditLog_With_Exception() throws Exception {
		// get all available ids from db
		List<Long> ids = getPlatformIdsFromDB();

		assertEquals(2, ids.size());

		// add prices to management bean
		managementBean_addPrices();

		ILectureManagementBean lectureManagementBean = lookup(ctx,
				LectureManagementBean.class);

		lectureManagementBean.login(TestData.N_ENT8_2, TestData.PW_ENT8_2);

		// add a lecture which needs more units than available and therefore
		// triggers an AssignmentException
		try {
			List<String> params = new ArrayList<String>();

			int maxCapacity = Integer.MAX_VALUE;

			lectureManagementBean.addLecture(ids.get(0), maxCapacity, "course5",
					params);

			fail("Wrong calculation @ LectureManagementBean, "
					+ "not enough capacity for lecture. The test cannot be completed!");
		} catch (AssignmentException e) {
			// expected Exception - nothing to do
		}

		List<AuditLogDTO> auditLogs = managementBean.getAuditLogs();
		assertEquals(2, auditLogs.size());

		List<AuditLogDTO> temp = getAuditLogsForResult(
				AssignmentException.class.getName(), auditLogs);
		assertEquals(1, temp.size());
	}

	public List<AuditLogDTO> getAuditLogsForMethod(String method,
			List<AuditLogDTO> audits) {
		List<AuditLogDTO> ret = new ArrayList<AuditLogDTO>();

		for (AuditLogDTO audit : audits) {
			if (method.equals(audit.getMethod()))
				ret.add(audit);
		}

		return ret;
	}

	public List<AuditLogDTO> getAuditLogsForResult(String result,
			List<AuditLogDTO> audits) {
		List<AuditLogDTO> ret = new ArrayList<AuditLogDTO>();

		for (AuditLogDTO audit : audits) {
			if (audit.getResult() != null
					&& audit.getResult().startsWith(result))
				ret.add(audit);
		}

		return ret;
	}

}
