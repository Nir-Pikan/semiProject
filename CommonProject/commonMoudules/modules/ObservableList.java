package modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * base implementation of ObservableList, support add and remover Properties
 * @param <T> type of {@link ArrayList}
 */
public class ObservableList<T> extends ArrayList<T> {

	/**
	 * serial number for Serialization
	 */
	private static final long serialVersionUID = -6452835837038080965L;

	private Property<T> addProperty = new Property<T>();
	private Property<T> removeProperty = new Property<T>();

	/**
	 * creates empty List
	 * @see {@link ArrayList#ArrayList()}
	 */
	public ObservableList() {
		super();
	}

	/**
	 * Creates List from given Collection
	 * @param arg0 {@link Collection} to add
	 * @see {@link ArrayList#ArrayList(Collection)}
	 */
	public ObservableList(Collection<? extends T> arg0) {
		super(arg0);
	}

	/**
	 * Creates List from given Collection
	 * @param arg0 {@link Collection} to add
	 * @see {@link ArrayList#ArrayList(int)}
	 */
	public ObservableList(int arg0) {
		super(arg0);
	}

	/**
	 * @return {@link Property} for adding items
	 */
	public Property<T> getAddProperty() {
		return addProperty;
	}

	/**
	 * @return {@link Property} for removing items
	 */
	public Property<T> getRemoveProperty() {
		return removeProperty;
	}

	/**
	 * @param addProperty the addProperty to set
	 */
	private void setAddProperty(Property<T> addProperty) {
		this.addProperty = addProperty;
	}

	/**
	 * @param removeProperty the removeProperty to set
	 */
	private void setRemoveProperty(Property<T> removeProperty) {
		this.removeProperty = removeProperty;
	}

	@Override
	public void add(int index, T element) {
		addProperty.setVal(element);
		super.add(index, element);
	}

	@Override
	public boolean add(T e) {
		addProperty.setVal(e);
		return super.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		for (T obj : c)
			addProperty.setVal(obj);
		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		for (T obj : c)
			addProperty.setVal(obj);
		return super.addAll(index, c);
	}

	@Override
	public void clear() {
		for (T obj : this)
			removeProperty.setVal(obj);
		super.clear();
	}

	@Override
	public T remove(int index) {
		T removed = super.remove(index);
		removeProperty.setVal(removed);
		return removed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		if (super.remove(o)) {
			removeProperty.setVal((T) o);
			return true;
		}
		return false;

	}

	@Override
	public boolean removeAll(Collection<?> c) {
		ArrayList<T> old = new ArrayList<T>(this);
		if (super.removeAll(c)) {
			old.removeAll(this);
			for (T obj : old)
				removeProperty.setVal(obj);
			return true;
		}
		return false;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		ObservableList<T> ret = new ObservableList<T>(super.subList(fromIndex, toIndex));
		ret.setAddProperty(addProperty);
		ret.setRemoveProperty(removeProperty);
		return ret;
	}

}
