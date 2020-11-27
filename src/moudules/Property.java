package moudules;

import java.util.HashSet;
import java.util.Set;

/**class for properties, when value changed all Listeners(added with addListener()) will be executed*/
public class Property<T> {

	private T val = null;
	private Set<PropertyListener<T>> listeners = new HashSet<>();

	public Property() {

	}

	public Property(T val) {
		this.val = val;
	}
	
	public Property(T val,PropertyListener<T> listener) {
		this();
		AddListener(listener);
		setVal(val);
	}

	public T getVal() {
		return val;
	}

	/**set the property value and run all listeners*/
	public void setVal(T val) {
		T oldVal = this.val;
		this.val = val;
		for (PropertyListener<T> list : listeners) {
			list.onChange(oldVal, val);
		}
	}

	/**add listener to the property, will be execute when setVal is called*/
	public void AddListener(PropertyListener<T> listener) {
		listeners.add(listener);
	}
	/**remove specific listener from the property*/
	public void removeListener(PropertyListener<T> listener) {
		listeners.remove(listener);
	}

	public void clearAllListeners() {
		listeners.clear();
	}
	
	/**call all the listeners with the current value*/
	public void emitChange() {
		for (PropertyListener<T> list : listeners) {
			list.onChange(this.val, this.val);
		}
	}
}
