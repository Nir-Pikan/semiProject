package util;

import java.util.Observable;
import java.util.Observer;

/**
 * Observable of event notify. only send the give event to the observers
 * @author User
 *
 */
public class EventNotifier extends Observable{

	/**
	 * notify all the observers about this event
	 * @param event the event to send
	 */
	public void emit(Object event) {
		this.setChanged();
		this.notifyObservers(event);
	}
	
}
