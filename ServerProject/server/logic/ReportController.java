package logic;

import modules.IController;
import modules.ServerRequest;

public class ReportController implements IController
{

	private OrderController order;
	private EntryController entry;
	private ParkController park;
	
	
	public ReportController(OrderController order, EntryController entry, ParkController park) {
		super();
		this.order = order;
		this.entry = entry;
		this.park = park;
		// TODO Auto-generated constructor stub
	}



	@Override
	public String handleRequest(ServerRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
