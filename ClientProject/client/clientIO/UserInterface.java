package clientIO;

import entities.Subscriber;
import entities.Worker;
import modules.Property;
import modules.ServerRequest;

public interface UserInterface {

	/**
	 * @return the logedInWorker
	 */
	Property<Worker> getLogedInWorker();

	/**
	 * @param logedInWorker the logedInWorker to set
	 */
	void setLogedInWorker(Property<Worker> logedInWorker);

	/**
	 * @return the logedInSubscriber
	 */
	Property<Subscriber> getLogedInSubscriber();

	/**
	 * @param logedInSubscriber the logedInSubscriber to set
	 */
	void setLogedInSubscriber(Property<Subscriber> logedInSubscriber);

	/**
	 * @return the visitorID
	 */
	Property<String> getVisitorID();

	/**
	 * @param visitorID the visitorID to set
	 */
	void setVisitorID(Property<String> visitorID);

}