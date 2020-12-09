
package logic;

import modules.IController;
import modules.ServerRequest;


public class EntryController implements IController {
	private ParkController park;
	private MessageController messageC;
	private SubscriberController subscriber;
	private DiscountController discount;
	
	
	public EntryController(ParkController park, MessageController messageC, SubscriberController subscriber,
			DiscountController discount) {
		this.park = park;
		this.messageC = messageC;
		this.subscriber = subscriber;
		this.discount = discount;
		// TODO Auto-generated constructor stub
	}



	@Override
	public String handleRequest(ServerRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
