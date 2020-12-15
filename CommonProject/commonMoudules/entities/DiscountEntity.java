package entities;

import java.sql.Timestamp;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class DiscountEntity {
	
	public String DiscountID;
	public float DiscountValue;
	public Timestamp StartTime,EndTime;
	private LocalDate startDateShow,endDateShow;
	public boolean IsApproved;
	
	public DiscountEntity(String discountID, float discountValue, Timestamp startTime, Timestamp endTime,
			boolean isApproved) {
		DiscountID = discountID;
		DiscountValue = discountValue;
		setStartTime(startTime);
		setEndTime( endTime);
		IsApproved = isApproved;

	}


	public String getDiscountID() {
		return DiscountID;
	}
	
	public float getDiscountValue() {
		return Math.round(DiscountValue *10000)/100f;
	}
	
	public void setDiscountID(String discountID) {
		DiscountID = discountID;
	}

	public void setStartTime(Timestamp startTime) {
		StartTime = startTime;
		startDateShow = startTime.toLocalDateTime().toLocalDate();
	}


	public void setEndTime(Timestamp endTime) {
		EndTime = endTime;
		endDateShow =endTime.toLocalDateTime().toLocalDate();
	}


	public LocalDate getStartDateShow() {
		return startDateShow;
	}


	public LocalDate getEndDateShow() {
		return endDateShow;
	}
	
}