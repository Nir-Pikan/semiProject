package gui;

import java.sql.Timestamp;
import java.time.LocalDate;

import entities.DiscountEntity;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the AddDiscount page controller */
public class AddDiscountController implements GuiController {

	@FXML
	private DatePicker dateStartDate;

	@FXML
	private DatePicker dateEndDate;

	@FXML
	private TextField txtDiscountId;

	@FXML
	private TextField txtDiscountValue;


	@Override
	public void init() {
		   Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
	            @Override
	            public DateCell call(final DatePicker param) {
	                return new DateCell() {
	                    @Override
	                    public void updateItem(LocalDate item, boolean empty) {
	                        super.updateItem(item, empty); 
	                        LocalDate today = LocalDate.now();
	                        if(dateEndDate.getValue()!=null)
	                        setDisable(empty || item.compareTo(today) < 0 || item.compareTo(dateEndDate.getValue().minusDays(1))>0);
	                        else {
	                        	setDisable(empty || item.compareTo(today) < 0);
							}
	                    }

	                };
	            }

	        };
	        dateStartDate.setDayCellFactory(callB);
	        Callback<DatePicker, DateCell> callB2 = new Callback<DatePicker, DateCell>() {
	            @Override
	            public DateCell call(final DatePicker param) {
	                return new DateCell() {
	                    @Override
	                    public void updateItem(LocalDate item, boolean empty) {
	                        super.updateItem(item, empty); 
	                      	LocalDate startDate;
	                        if(dateStartDate.getValue()!=null)
	                        	 startDate = dateStartDate.getValue();
	                        else {
								startDate=LocalDate.now();
							}
	                        setDisable(empty || item.compareTo(startDate.plusDays(1)) < 0);
	                    }

	                };
	            }

	        };
	        dateEndDate.setDayCellFactory(callB2);
	}

  /** submits a discount according to AddDiscount page fields */
	@FXML
	void submitDiscount(ActionEvent event) {

		try {

			String disID = txtDiscountId.getText();
			String disVal = txtDiscountValue.getText();
			LocalDate startDate = dateStartDate.getValue();
			LocalDate endDate = dateEndDate.getValue();

			txtDiscountId.getStyleClass().remove("error");
			txtDiscountValue.getStyleClass().remove("error");
			dateStartDate.getStyleClass().remove("error");
			dateEndDate.getStyleClass().remove("error");

			String bodyMsg = "";
			if (disID.isEmpty()) {
				bodyMsg += "The ID of the discount is Empty\n";
				txtDiscountId.getStyleClass().add("error");
			}
			if (disVal.isEmpty()) {
				bodyMsg += "The Value of the discount is Empty\n";
				txtDiscountValue.getStyleClass().add("error");
			}
			if (startDate == null) {
				bodyMsg += "The Start Date of the discount is Empty\n";
				dateStartDate.getStyleClass().add("error");
			}
			if (endDate == null) {
				bodyMsg += "The End Date of the discount is Empty\n";
				dateEndDate.getStyleClass().add("error");
			}
			if (!bodyMsg.isEmpty()) {
				PopUp.showError("Missing Fields", "Missing Fields", bodyMsg);
				return;
			}
			// Validate correct discount range
			float discountValue = Float.valueOf(disVal);

			if (discountValue < 0 || discountValue > 100) {
				txtDiscountValue.setText("");
				PopUp.showInformation("Out Of Range Value", "Discount Value Out Of Range",
						"Please Enter Value between 0 to 100");
				return;
			}

			Timestamp startTimeDiscount = Timestamp.valueOf(startDate.atTime(0, 0, 0).minusHours(3).minusMinutes(30));
			// removing offset of local time minus 03:30:00.0
			Timestamp endTimeDiscount = Timestamp.valueOf(endDate.atTime(0, 0, 0).minusHours(3).minusMinutes(30));
			// removing offset of local time minus 03:30:00.0 // offset

			if (endTimeDiscount.getTime() <= startTimeDiscount.getTime()) {
				PopUp.showInformation("Invalid Value", "Invalid Discount Dates Input",
						"The end date should be at least 1 day long");
				return;
			}

			DiscountEntity discountEntity = new DiscountEntity(disID, discountValue / 100, startTimeDiscount,
					endTimeDiscount, false);

			ServerRequest sr = new ServerRequest(Manager.Discount, "AddNewDiscount",
					ServerRequest.gson.toJson(discountEntity, DiscountEntity.class));


			String respond = clientController.client.sendRequestAndResponse(sr);
			if (respond.equals("Failed to add Discount") || respond.equals("")) {
				PopUp.showInformation("Server Error", "Servere Request Failed",
						"The server was unsuccessful to add new discount \nOr Discount ID already exist");
				return;
			} else if (respond.equals("Discount was added successfully")) {
				txtDiscountId.setText("");
				txtDiscountValue.setText("");
				dateStartDate.getEditor().clear();
				dateEndDate.getEditor().clear();
				PopUp.showInformation("New Discount Added Successfully", "New Discount Added Successfully",
						"Discount Added Successfully");
			}

		} catch (NumberFormatException e) {
			PopUp.showInformation("Invalid Value", "Invalid Discount Value Input",
					"Please Enter Value between 0 to 100");
			return;
		} catch (IllegalArgumentException e) {
			dateStartDate.getEditor().clear();
			dateEndDate.getEditor().clear();
			PopUp.showInformation("Invalid Value", "Invalid Discount Dates Input", "Please Enter Currect date");
			return;
		}
	}
}
