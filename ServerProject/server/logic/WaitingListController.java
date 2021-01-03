package logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.mysql.cj.protocol.ServerSession;

import entities.Order;
import entities.Order.IdType;
import entities.Order.OrderStatus;
import io.DbController;
import javafx.application.Platform;
import modules.IController;
import modules.PeriodicallyRunner;
import modules.ServerRequest;
import modules.WakeableThread;


public class WaitingListController implements IController {
	private OrderController order; 
	private MessageController messageC;
	
	private DbController dbController;
	private Map<Integer,WakeableThread> currentWaitingCancelation;//the order who are we waiting for
	private Map<WakeableThread,Order> currentCancelation;//the canceled order of the thread 

	
	private static int AvgVisitTime = 4; //TODO change to park visit Time
	
	
	public WaitingListController(OrderController order, MessageController messageC) {
		super();
		this.order = order;
		this.messageC = messageC;
		dbController = DbController.getInstance();
		currentWaitingCancelation = new HashMap<Integer, WakeableThread>();
		currentCancelation = new HashMap<WakeableThread, Order>();
		createTable();
		
		order.canceled.getAddProperty().AddListener((obs,oldVal,newVal)->{
			 WakeableThread t = new WakeableThread(()->{
				 Thread cur = Thread.currentThread();
				 if(!(cur instanceof WakeableThread)) 
					 return;
				 WakeableThread myThread = (WakeableThread )cur;
				 handelCancel(newVal, myThread);
				 currentCancelation.remove(myThread);//finish with the cancellation
				 order.canceled.remove(newVal);
				});
			 t.start();
			 currentCancelation.put(t, newVal);
			});
		InitWaitingListCleaner();
	}

	private void InitWaitingListCleaner() {
		PeriodicallyRunner.runEveryDayAt(23, 00, ()->{
			PreparedStatement pstmt = dbController.getPreparedStatement("DELETE FROM waitingList WHERE visitTime < ?;");
			
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			try {
				pstmt.setTimestamp(1, now);
				pstmt.executeUpdate();
			} catch (SQLException e) {
			}
		});
	}
	
	/**
	 * Create waitingList table in DB if not exists
	 */
	private void createTable() {
		// please consider that type (enum value) name was changed from IDtype to type,
		// hope no bug is will be found
		boolean isCreated = dbController.createTable("waitingList(parkSite varchar(20),numberOfVisitors int,orderID int,"
				+ "priceOfOrder FLOAT, email varchar(20),phone varchar(20), type ENUM('PRIVATE','PRIVATEGROUP','GUIDE','FAMILY'),"
				+ "orderStatus ENUM('CANCEL','IDLE','CONFIRMED','WAITINGLIST','WAITINGLISTMASSAGESENT'),"
				+ "visitTime TIMESTAMP(1), timeOfOrder TIMESTAMP(1), isUsed BOOLEAN,ownerID varchar(20),"
				+ "numberOfSubscribers int, primary key(orderID));");
		if (isCreated)
			System.out.println("Table has been created");
	}
	
	private void handelCancel(Order canceled,WakeableThread thread){
		canceled.orderStatus = OrderStatus.SEMICANCELED;
		order.UpdateOrder(canceled);
		Order nextWaiting;
		while((nextWaiting = getNextOrder(canceled))!=null) {
			final Order orderToNotify = nextWaiting;
			Platform.runLater(()->{notifyWaitingOrder(orderToNotify);});
			
			if(thread.sleepUntillWoken(TimeUnit.HOURS, 1)) {
				//if woken by user interaction
				if(!currentCancelation.containsKey(thread))
					return;//exit if finished with this cancellation
				
			}else {
				//if user did not response
				deleteFromWaitingList(nextWaiting);
				currentWaitingCancelation.remove(nextWaiting.orderID);
			}
			
		}
		
		canceled.orderStatus = OrderStatus.CANCEL;
		order.UpdateOrder(canceled);
	}
	
	
	private boolean AddNewOrderTowaitingList(Order ord) {
		Platform.runLater(()->{notifyNewOrder(ord);});
		PreparedStatement ps = dbController
				.getPreparedStatement("INSERT INTO waitingList VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		try {
			ps.setString(1, ord.parkSite);
			ps.setInt(2, ord.numberOfVisitors);
			ps.setLong(3, ord.orderID);
			ps.setFloat(4, ord.priceOfOrder);
			ps.setString(5, ord.email);
			ps.setString(6, ord.phone);
			ps.setString(7, ord.type.toString()); // can be stored as enum ?
			ps.setString(8, Order.OrderStatus.WAITINGLIST.toString());
			ps.setTimestamp(9, ord.visitTime);
			ps.setTimestamp(10, ord.timeOfOrder);
			ps.setBoolean(11, ord.isUsed);
			ps.setString(12, ord.ownerID);
			ps.setInt(13, ord.numberOfSubscribers);


			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void notifyWaitingOrder(Order nextWaiting) {
		messageC.SendEmailAndSMS(nextWaiting.email, nextWaiting.phone,
				"Your Order No: "+nextWaiting.orderID+ " can be accepted,\n"//content
						+ " Please Go to GoNature and approve/cancel this order."
						+ "This offer is for one hour only!",
						"Order "+nextWaiting.orderID+" out if waiting list");//subject
		
	}
	
	private void notifyNewOrder(Order nextWaiting) {
		messageC.SendEmailAndSMS(nextWaiting.email, nextWaiting.phone,
				"Your Order Added to the waiting list:\n" +
						"OrderId: " + nextWaiting.orderID +"\n" +
									"visit time: " + nextWaiting.visitTime.toLocalDateTime().toLocalDate() + " " + nextWaiting.visitTime.toLocalDateTime().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME) + "\n" +
									"number of visitors: " + nextWaiting.numberOfVisitors + "\n" +
									"Price: "+ nextWaiting.priceOfOrder+"\n" + 
									"Thank for using GoNature",
						"Order "+nextWaiting.orderID+" out if waiting list");//subject
		
	}

	private void deleteFromWaitingList(Order order) {
		PreparedStatement pstmt = dbController.getPreparedStatement("DELETE FROM waitingList WHERE orderID = ?;");
		try {
			pstmt.setInt(1, order.orderID);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to delete order");
		}
		
	}

	/**
	 * get next and set to messasge sent
	 * @param canceled
	 * @return
	 */
	private synchronized Order getNextOrder(Order canceled) {
		Timestamp minimum = Timestamp.valueOf(canceled.visitTime.toLocalDateTime().minusSeconds(TimeUnit.HOURS.toSeconds(AvgVisitTime-1)));
		Timestamp maximum = Timestamp.valueOf(canceled.visitTime.toLocalDateTime().plusSeconds(TimeUnit.HOURS.toSeconds(AvgVisitTime)));
		 PreparedStatement ps = dbController.getPreparedStatement("SELECT * FROM waitingList WHERE timestamp(visitTime) >= timestamp( ? ) " + 
				" and  timestamp(visitTime) <= timestamp( ? ) AND parkSite = ? And orderStatus = 'WAITINGLIST' ORDER BY timeOfOrder ASC;");
		
		List<Order> list = new ArrayList<>();
		try {
			ps.setTimestamp(1, minimum);
			ps.setTimestamp(2, maximum);
			ps.setString(3, canceled.parkSite);

			ResultSet res = ps.executeQuery();
			if (res.next()) {
				list.add(new Order(res.getString(1), res.getInt(2), res.getInt(3), res.getFloat(4), res.getString(5),
						res.getString(6), IdType.valueOf(res.getString(7)), OrderStatus.valueOf(res.getString(8)),
						res.getTimestamp(9), res.getTimestamp(10), res.getBoolean(11), res.getString(12),res.getInt(13)));
			}
			res.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(Order o : list) {
			if(order.IsOrderAllowedWaitingList(o,canceled.numberOfVisitors))
			{//setting the Order to sent message
			dbController.sendUpdate("UPDATE waitingList SET orderStatus='WAITINGLISTMASSAGESENT' WHERE orderID = "+o.orderID+";");
				return o;
		}
		}
		return null;
	}
	
	private void acceptWaitingOrder(int orderId) {
		//set canceled Order as canceled 
				WakeableThread waitingT = currentWaitingCancelation.get(orderId);
				Order canceled = currentCancelation.get(waitingT);
				canceled.orderStatus = OrderStatus.CANCEL;
				order.UpdateOrder(canceled);
				
				Order acceptedOrder = getwaitingOrder(orderId);
				
		acceptedOrder.orderStatus = OrderStatus.IDLE;
				order.AddNewOrder(acceptedOrder);//TODO Or check if the time is after the approval time and set to confirmed if passed
				deleteFromWaitingList(acceptedOrder);
				
			currentWaitingCancelation.remove(orderId);
		waitingT.wake();
	}

	private Order getwaitingOrder(int orderId) {
		ResultSet res = dbController.sendQuery("SELECT * FROM waitingList WHERE orderID = \"" + orderId + "\";");
		
		Order o = null;
		try {
			if (res.next()) {
				o = new Order(res.getString(1), res.getInt(2), res.getInt(3), res.getFloat(4), res.getString(5),
						res.getString(6), IdType.valueOf(res.getString(7)), OrderStatus.valueOf(res.getString(8)),
						res.getTimestamp(9), res.getTimestamp(10), res.getBoolean(11), res.getString(12),res.getInt(13));
			}
			res.close();
			return o;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String handleRequest(ServerRequest request) {
		switch(request.job) {
		case "acceptWaitingOrder":
			acceptWaitingOrder(Integer.parseInt(request.data));
			return "accepted";
		case "GetOrderByID":
			try {
			Order o = getwaitingOrder(Integer.parseInt(request.data));
			if(o == null )
				return request.data + " not found";
			return ServerRequest.gson.toJson(o, Order.class);
			}catch (NumberFormatException e) {
				return request.data + " not found";
			}
		case "cancelWaitingOrder":
			deleteFromWaitingList(getwaitingOrder(Integer.parseInt(request.data)));
			WakeableThread waitingT = currentWaitingCancelation.get(Integer.parseInt(request.data));
			if(waitingT!= null)
				waitingT.wake();
			return "canceled";
		case "addToWaitingList":
			AddNewOrderTowaitingList(ServerRequest.gson.fromJson(request.data, Order.class));
			return "added";
		default:
			return "unsupported request";
		}
		
	}

}
