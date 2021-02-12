package models.dao;

import javafx.collections.ObservableList;

public interface IDao <E,T> {

	public void insert(E producto);
	public void delete(E producto);
	public void mod(E producto);
	public ObservableList<E> getList();
	public E getItem(T id);
	
}
