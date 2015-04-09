package dst.ass2.ejb.session.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

import dst.ass2.ejb.dto.AuditLogDTO;
import dst.ass2.ejb.dto.BillDTO;

public interface IGeneralManagementBean {

	/**
	 * Helper method to add a price-step using the IPriceManagementBean
	 * 
	 * @param nrOfHistoricalLectures
	 * @param price
	 */
	public void addPrice(Integer nrOfHistoricalLectures, BigDecimal price);

	/**
	 * Retrieves the bill for the given lecturer.
	 * 
	 * @param lecturername
	 * @return the bill as BillDTO
	 * @throws Exception
	 */
	public Future<BillDTO> getBillForLecturer(String lecturerName) throws Exception;

	/**
	 * Retrieves all created audits.
	 * 
	 * @return list of audits
	 */
	public List<AuditLogDTO> getAuditLogs();

	/**
	 * Helper method to clear the cache of the IPriceManagementBean
	 */
	public void clearPriceCache();

}
