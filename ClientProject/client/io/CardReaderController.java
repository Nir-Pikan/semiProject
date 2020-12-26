package io;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import entities.DiscountEntity;
import entities.Order;
import entities.ParkEntry;
import entities.ParkEntry.EntryType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mocks.CardReaderSystem;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** a class representing the card reader sending us the entry data */
public class CardReaderController {

	/**
	 * creates {@link CardReaderController}
	 */
	public CardReaderController() {

	}

	// ============================ main is only for demonstration
	// ====================

	/**
	 * method that checks if visitor is allowed to enter to the park
	 * 
	 * @param id the ID of visitor to be checked
	 */
	public void enterVisitor(String id, int numberOfVisitors) {
		// TODO checks if visitor allowed to enter to the park (Roman)

		ServerRequest sr = new ServerRequest(Manager.Order, "GetOrderByVisitorID",
				ServerRequest.gson.toJson(id, String.class));

		String respondString = clientController.client.sendRequestAndResponse(sr);
		Order[] orders = ServerRequest.gson.fromJson(respondString, Order[].class);

		Order currentOrder = null;
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		for (Order order : orders) {
			if (!order.isUsed)
				if (order.visitTime.getTime() <= ( currentTime.getTime()+ TimeUnit.MINUTES.toMillis(30))
						&& order.visitTime.getTime() >= (currentTime.getTime())- TimeUnit.MINUTES.toMillis(30)) {
					currentOrder = order;
					break;
				}

		}
		if (currentOrder == null) {
			PopUp.showInformation("No such order for time", "No such order for time",
					"No such order for time");

		} else {
			if (currentOrder.numberOfVisitors < numberOfVisitors) {

				PopUp.showInformation("No such order for time", "No such order for time",
						"In Order :" + currentOrder.numberOfVisitors + "\nCame to the park : " + numberOfVisitors);
			} else {
				updateEntry(currentOrder, numberOfVisitors);

			}
		}

	}

	private void updateEntry(Order currentOrder, int numberOfVisitors) {

		currentOrder.numberOfVisitors = numberOfVisitors;
		currentOrder.isUsed=true;
		ServerRequest sr = new ServerRequest(Manager.Order, "UpdateOrder",
				ServerRequest.gson.toJson(currentOrder, Order.class));

		EntryType entryType = null;
		switch (currentOrder.type) {
		case PRIVATE:
			entryType = EntryType.Personal;
			break;
		case PRIVATEGROUP:
			entryType = EntryType.PrivateGroup;
			break;
		case GUIDE:
			entryType = EntryType.Group;
			break;
		case FAMILY:
			entryType = EntryType.Subscriber;
			break;

		default:
			break;
		}

		ParkEntry parkEntry = new ParkEntry(entryType, String.valueOf(currentOrder.ownerID), currentOrder.parkSite,
				null, null, numberOfVisitors, 0, false, currentOrder.priceOfOrder);
		//TODO what with the number of subscribers?
		
		ServerRequest sr2 = new ServerRequest(Manager.Entry, "AddNewEntry",
				ServerRequest.gson.toJson(parkEntry, ParkEntry.class));

		String respondString = clientController.client.sendRequestAndResponse(sr);
		String respondString2 = clientController.client.sendRequestAndResponse(sr2);

		if (respondString.equals("Failed to update order")) {
			PopUp.showInformation("Failed to update order", "Failed to update order",
					"Failed to update order in server");
		} else if (respondString.equals("Order updated")) {
			String bodyString = "";
			bodyString += "orderID:" + currentOrder.orderID + "\n";
			bodyString += "numberOfVisitors:" + currentOrder.numberOfVisitors + "\n";
			bodyString += "priceOfOrder:" + currentOrder.priceOfOrder + "\n";
			bodyString += "visitTime:" + currentOrder.visitTime + "\n";

			PopUp.showInformation("Order updated", "Order updated successfully", bodyString);
		}

		if (respondString2.equals("Failed to update Entry exit got Null")) {
			PopUp.showInformation("Failed to update Entry exit got Null", "Failed to update Entry exit got Null",
					"Failed to update Entry exit got Null in server");
		} else if (respondString2.equals("Entry exit was updated successfully ")) {
			String bodyString = "";
			bodyString += "parkID:" + parkEntry.parkID + "\n";
			bodyString += "numberOfVisitors:" + parkEntry.numberOfVisitors + "\n";
			bodyString += "priceOfOrder:" + currentOrder.priceOfOrder + "\n";
			bodyString += "visitTime:" + parkEntry.arriveTime + "\n";

			PopUp.showInformation("Entry updated", "Entry updated successfully", bodyString);
		} else if (respondString2.equals("Failed to update Entry exit")) {
			PopUp.showInformation("Failed to update Entry exit", "Failed to update Entry exit",
					"Failed to update Entry exit server");
		}

	}

	/**
	 * method that checks if visitor is allowed to exit to the park
	 * 
	 * @param id the ID of visitor to be checked
	 */
	public void exitVisitor(String id) {

		ServerRequest sr = new ServerRequest(Manager.Entry, "updateExit", id);

		String respondString = clientController.client.sendRequestAndResponse(sr);

		String isSuccess = respondString;

		if (isSuccess.equals("Entry exit was updated successfully ")) {
			PopUp.showInformation("Success exit", "Success Exit update", "Success Exit update of id :" + id);

		} else {
			PopUp.showInformation("Failed exit", "Failed Exit update", "Failed Exit update of id :" + id);
		}
	}

}
