package controllers;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import models.Producto;
import models.ProductoTableExel;

public class TableViewController {


	@FXML private TableView<Producto> tableProducto;
	@FXML private TableColumn<Producto,String> codEmpresa;
	@FXML private TableColumn<Producto, String> codNegocio;
	@FXML private TableColumn<Producto, String> nombre;
	@FXML private TableColumn<Producto, Number> precioCosto;
	@FXML private TableColumn<Producto, Number> precioCantidad;
	@FXML private TableColumn<Producto, Number> precioVenta;
	@FXML private TableColumn<Producto, LocalDate> create;
	@FXML private TableColumn<Producto, String> detalle;
	
	@FXML 
	public void initialize() {
		codEmpresa.setText(ProductoTableExel.getRowIdEmpresa());
		codNegocio.setText(ProductoTableExel.getRowIdNegocio());
		nombre.setText(ProductoTableExel.getRowProducto());
		precioCosto.setText(ProductoTableExel.getRowPrecioCosto());
		precioCantidad.setText(ProductoTableExel.getRowPrecioCantidad());
		precioVenta.setText(ProductoTableExel.getRowPrecioVenta());
		detalle.setText(ProductoTableExel.getRowDetalle());
		create.setText(ProductoTableExel.getRowUpdateAt());
		
		codEmpresa.setCellValueFactory(value -> value.getValue().getIdEmpresaProperty());
		codNegocio.setCellValueFactory(value -> value.getValue().getIdNegocioProperty());
		nombre.setCellValueFactory(value -> value.getValue().getNombreProperty());
		precioCosto.setCellValueFactory(intValue -> intValue.getValue().getPrecioCostoProperty());
		precioCantidad.setCellValueFactory(value -> value.getValue().getPrecioCantidadProperty());
		precioVenta.setCellValueFactory(value -> value.getValue().getPrecioVentaProperty());
		detalle.setCellValueFactory(value -> value.getValue().getDetalleProperty());
		create.setCellValueFactory(value -> value.getValue().getUpdatedAtProperty());
		create.setCellFactory(column -> {
	        return new TableCell<Producto, LocalDate>() {
	            @Override
	            protected void updateItem(LocalDate item, boolean empty) {
	                super.updateItem(item, empty);
	                String value = item == null || empty ? null : Producto.dateFormated(item);
	                setText(value);
	          	                
	            }
	        };
	    });
		
		tableProducto.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//		optionsSelection(tableProducto); //ver que es 
		
	}
	
	public void save() {
		
	}
	
	public void load() {
		
	}
	
	public void setItems(ObservableList<Producto> items) {
		tableProducto.setItems(items);
	}
	
	public ObservableList<Producto> getAllItems() {
		return tableProducto.getItems();
	}
	
	public ArrayList<Producto> getSelectionItems(){
		return new ArrayList<Producto>(tableProducto.getSelectionModel().getSelectedItems());
	}
	
	public Producto getSeletedItem() {
		return tableProducto.getSelectionModel().getSelectedItem();
	}
		
	public int getSelectionItemsSize(){
		return getSelectionItems().size();
	}
	
	public void clearSelection() {
		tableProducto.getSelectionModel().clearSelection();
	}
	
	//  refrescarTabla, 
}
