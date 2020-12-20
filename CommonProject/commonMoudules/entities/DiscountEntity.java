package entities;

import java.sql.Timestamp;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * a class containing the data of a single discount
 */
public class DiscountEntity {

	public String DiscountID;
	public float DiscountValue;
	public Timestamp StartTime, EndTime;
	private LocalDate startDateShow, endDateShow;
	public boolean IsApproved;

	/**
	 * Creates a {@link DiscountEntity}
	 * 
	 * @param discountID    String
	 * @param discountValue float
	 * @param startTime     Timestamp
	 * @param endTime       Timestamp
	 */
	public DiscountEntity(String discountID, float discountValue, Timestamp startTime, Timestamp endTime,
			boolean isApproved) {
		DiscountID = discountID;
		DiscountValue = discountValue;
		setStartTime(startTime);
		setEndTime(endTime);
		IsApproved = isApproved;

	}

	public String getDiscountID() {
		return DiscountID;
	}

	public float getDiscountValue() {
		return Math.round(DiscountValue * 10000) / 100f;
	}

	/** sets the discount's discountID */
	public void setDiscountID(String discountID) {
		DiscountID = discountID;
	}

	/** sets the discount's startTime */
	public void setStartTime(Timestamp startTime) {
		StartTime = startTime;
		startDateShow = startTime.toLocalDateTime().toLocalDate();
	}

	/** sets the discount's endTime */
	public void setEndTime(Timestamp endTime) {
		EndTime = endTime;
		endDateShow = endTime.toLocalDateTime().toLocalDate();
	}

	public LocalDate getStartDateShow() {
		return startDateShow;
	}

	public LocalDate getEndDateShow() {
		return endDateShow;
	}

}