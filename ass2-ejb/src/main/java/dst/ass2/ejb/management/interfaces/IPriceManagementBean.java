package dst.ass2.ejb.management.interfaces;

import java.math.BigDecimal;

public interface IPriceManagementBean {

	/**
	 * @param nrOfHistoricalLectures
	 * @return the price for the given number of historical lectures. If there was
	 *         no price for this number of lectures specified it returns 0.
	 */
	public BigDecimal getPrice(Integer nrOfHistoricalLectures);

	/**
	 * Creates a price-step for the given number of historical lectures.
	 * 
	 * @param nrOfHistoricalLectures
	 * @param price
	 */
	public void setPrice(Integer nrOfHistoricalLectures, BigDecimal price);

	/**
	 * Clears the cached price-steps.
	 */
	public void clearCache();

}
