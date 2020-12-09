package logic;

import modules.IController;
import modules.ServerRequest;


public class WaitingListController implements IController {
	private OrderController order; 
	private MessageController messageC;
	
	

	public WaitingListController(OrderController order, MessageController messageC) {
		super();
		this.order = order;
		this.messageC = messageC;
		// TODO Auto-generated constructor stub
	}



	@Override
	public String handleRequest(ServerRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
