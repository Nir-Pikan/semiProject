package modules;


/** class for functional programming for value change action, for more details see {@link Property}
 * @author Or Man
 * @version 1.1*/ 
public abstract class PropertyListener<T> {

	public PropertyListener() {
	}
	
	/***
	 * Execute this method when the value of the property changed
	 * @apiNote WARNING the parameter 'property' need to be used carefully, the use of setValue can cause loop,use {@link Property#silentSet(Object)} instead
	 * @param property the {@link Property} that called this action
	 * @param oldVal the last value of the property
	 * @param newVal the new value of the property
	 */
	public abstract void onChange(Property<? extends T> property ,T oldVal,T newVal);

}
