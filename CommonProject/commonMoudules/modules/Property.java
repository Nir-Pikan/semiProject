package modules;

import java.util.HashSet;
import java.util.Set;

/**
 * a class for properties, when value changed all Listeners
 * <p>
 * (added with {@link #AddListener(PropertyListener)})
 * <p>
 * will be executed.
 * 
 * @author Or Man
 * @version 1.1
 * @since 21/12/2020
 */
public class Property<T> {

	private T val = null;
	private Set<PropertyListener<T>> listeners = new HashSet<>();

	/***
	 * Creates {@link Property} with 'null' as initial value
	 * <p>
	 * and without listeners.
	 */
	public Property() {

	}

	/***
	 * Creates {@link Property} with initial value
	 * <p>
	 * and without listeners.
	 * 
	 * @param val the value to set
	 */
	public Property(T val) {
		this.val = val;
	}

	/***
	 * Creates {@link Property} with initial value and sets listener.
	 * <p>
	 * The listener fires for this initialization
	 * 
	 * @param val      the value to set
	 * @param listener {@link PropertyListener} that execute operation when value
	 *                 change
	 */
	public Property(T val, PropertyListener<T> listener) {
		this();
		AddListener(listener);
		setVal(val);
	}

	/**
	 * return the value of this {@link Property}
	 * 
	 * @return the value in the property or null if not initialized
	 */
	public T getVal() {
		return val;
	}

	/**
	 * set the {@link Property} value and run all listeners
	 * 
	 * @param val Value to set
	 */
	public void setVal(T val) {
		T oldVal = this.val;
		this.val = val;
		for (PropertyListener<T> list : listeners) {
			list.onChange(this, oldVal, val);
		}
	}

	/**
	 * set the {@link Property} value without running all listeners
	 * 
	 * @param val Value to set
	 */
	public void silentSet(T val) {
		this.val = val;
	}

	/**
	 * add listener to the {@link Property}, will be execute when setVal is called
	 * 
	 * @param listener {@link PropertyListener} that execute operation when value
	 *                 change
	 */
	public void AddListener(PropertyListener<T> listener) {
		listeners.add(listener);
	}

	/**
	 * remove specific listener from the {@link Property}
	 * 
	 * @param listener {@link PropertyListener} that execute operation when value
	 *                 change
	 */
	public void removeListener(PropertyListener<T> listener) {
		listeners.remove(listener);
	}

	/** remove all the listeners from the {@link Property} */
	public void clearAllListeners() {
		listeners.clear();
	}

	/**
	 * call all the listeners with the current value,
	 * <p>
	 * both oldVal and newVal will be the current value
	 */
	public void emitChange() {
		for (PropertyListener<T> list : listeners) {
			list.onChange(this, this.val, this.val);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Property<?>) {
			Property<?> pro = (Property<?>) obj;
			return val.equals(pro.getVal());
		}
		return val.equals(obj);
	}

}
