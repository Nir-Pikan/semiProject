package modules;

/**
 * class for functional programming for value change action, for more details
 * see {@link Property}
 * 
 * @author Or Man
 * @version 1.1
 * @since 21/12/2020
 */
public interface PropertyListener<T> {

	/***
	 * Execute this method when the value of the property changed<br>
	 * <br>
	 * WARNING the parameter 'property' need to be used carefully, the use of
	 * setValue can cause loop,use {@link Property#silentSet(T)} instead
	 * 
	 * @param property the {@link Property} that called this action
	 * @param oldVal   the last value of the {@link Property}
	 * @param newVal   the new value of the {@link Property}
	 */
	public abstract void onChange(Property<? extends T> property, T oldVal, T newVal);

}
