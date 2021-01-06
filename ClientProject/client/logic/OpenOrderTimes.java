package logic;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import entities.Order;
import entities.ParkNameAndTimes;
import io.clientController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/**
 * utility class for orders that cannot be accepted.
 * @see {@link #askForWaitingListAndShowOptions(Order)}
 *
 */
public class OpenOrderTimes {

	/**
	 * given {@link Order} find all the available times to the Order 
	 * @param ord the {@link Order} to deal with
	 * @return list of available times for this Order
	 */
	private static List<LocalTime> getOpenOrderTimes(Order ord) {
		Timestamp origin = ord.visitTime;
		ParkNameAndTimes times = clientController.client.openingTimes.get(ord.parkSite);
		List<LocalTime> l = new ArrayList<>();
		LocalDate visitDate = ord.visitTime.toLocalDateTime().toLocalDate();
		for(int i = times.openTime;i<times.closeTime;i++) {
			LocalTime currentTestingTime = LocalTime.of(i, 0);
			ord.visitTime = Timestamp.valueOf(visitDate.atTime(currentTestingTime));
			String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Order,"IsOrderAllowed",ServerRequest.gson.toJson(ord,Order.class)));
			if(response.equals("Order can be placed")) {
				l.add(currentTestingTime);
			}
		}
		ord.visitTime = origin;
		return l;
	}
	
	/**
	 * ask the user is he want to enter the waiting list.<br>
	 * if he want add his order to the waiting list, and navigate to home(return null)<br>
	 * if he want to see another time in the same day is shows the available times, let the user to choose the times.<br>
	 * if cancel button clicked return null.<br>
	 * else return the order with the visit time changed to the choosen time
	 * 
	 * @param ord the {@link Order} to deal with
	 * @return null if the operation failed(exited by user or no open times), {@link Order} with the visit time changed to the choosen time
	 */
	public static Order askForWaitingListAndShowOptions(Order ord) {
		ButtonType waiting = new ButtonType("Add to Waiting List", ButtonData.YES);
		ButtonType showTimes = new ButtonType("Show Available Times", ButtonData.NO);
		
		Alert a = new PopUp(AlertType.CONFIRMATION,"Would you like to enter to the waiting list or see other times for your order?",waiting,showTimes,ButtonType.CLOSE);
		a.setTitle("The Park Is Full");
		a.setHeaderText("The park is full");
		((Button)a.getDialogPane().lookupButton(waiting)).setPrefWidth(200);
		ButtonType returned = a.showAndWait().orElse(ButtonType.CLOSE);
		if(returned == ButtonType.CLOSE) {
			return null;
		}else if(returned == waiting) {
			clientController.client.sendRequestAndResponse(new ServerRequest(Manager.WaitingList,"addToWaitingList",ServerRequest.gson.toJson(ord,Order.class)));
			PopUp.showInformation("WaitingList", "Waiting List", "Yout order has been added to the waiting list");
			Navigator.instance().clearHistory();
			return null;
		}else if(returned == showTimes) {
			List<LocalTime> available = getOpenOrderTimes(ord);
			if(available.size() == 0) {
				PopUp.showError("WaitingList", "Waiting List", "No available time in "+ord.visitTime.toLocalDateTime().toLocalDate()+ " try different day");
				return null;
			}
			PopUp p = new PopUp(AlertType.CONFIRMATION);
			p.setTitle("Available Order Times");
			p.setHeaderText("Available Order Times");
			ComboBox<LocalTime> alternateTimes = new ComboBox<LocalTime>();
			alternateTimes.getItems().addAll(available);
			p.getDialogPane().setContent(new VBox(10,
					new Label("Available times for your order at "+ord.visitTime.toLocalDateTime().toLocalDate()),
					new HBox(10,
							new Label("Please choose your time: "),
							alternateTimes)));
			if(p.showAndWait().get() == ButtonType.OK) {
				ord.visitTime = Timestamp .valueOf(ord.visitTime.toLocalDateTime().toLocalDate().atTime(alternateTimes.getValue()));//TODO times == null
				return ord;
			}
		}
		return null;
	}
}
