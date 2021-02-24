package controllers;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class TableViewController<T> {

	@FXML
	protected TableView<T> tableProducto;
	protected FilteredList<T> filteredList;
	
	
	public void setItems(ObservableList<T> items) {
		this.tableProducto.setItems(items);
		this.filteredList = new FilteredList<T>(items, p -> true);
	}
	
	public ObservableList<T> getAllItems() {
		return tableProducto.getItems();
	}
	
	public ArrayList<T> getSelectionItems(){
		return new ArrayList<T>(tableProducto.getSelectionModel().getSelectedItems());
	}
	
	public T getSeletedItem() {
		return this.tableProducto.getSelectionModel().getSelectedItem();
	}
		
	public int getSelectionItemsSize(){
		return getSelectionItems().size();
	}
	
	public void clearSelection() {
		this.tableProducto.getSelectionModel().clearSelection();
	}
	
	public void setFilter(FilteredList<T> filtered) {
		this.filteredList = filtered;
	}
	
	public void setSelectionModeType(SelectionMode value) {
		tableProducto.getSelectionModel().setSelectionMode(value);
	}
	
	public void searchItem(String searchText, FilteredList<T> filter) {
		
		if (searchText.isEmpty()) {
			this.tableProducto.getSelectionModel().clearSelection();
			return;
		}
		
		SortedList<T> sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(this.tableProducto.comparatorProperty());
        this.tableProducto.setItems(sortedList);
        this.tableProducto.getSelectionModel().selectFirst();

	} 
	
	protected <S> void columnSetTitel(TableColumn<T, S> column, String value) {
		column.setText(value);
	}
	
	protected <S> void textcolumnWrapp(TableColumn<T, S> column, Text text,double padding) {
		text.wrappingWidthProperty().bind(column.widthProperty().subtract(padding));
	}

}