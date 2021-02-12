package controllers;

import java.io.IOException;

import exeptions.AppExeption;
import exeptions.TableViewExeption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.DialogShow;
import models.Producto;

public class PrincipalController {
	
	final String TABLE_VIEW_FXML = "/views/TableView.fxml";
	final String EDIT_PRODUCTO_FXML ="/views/EditProducto.fxml";
	private ListProductoController listProductoController;
	private EditProductoController editProductoController;
	private TableViewController tableViewController;
	@FXML private BorderPane tableLayout;
	@FXML private SplitPane splitPane; 
	@FXML private Pane pane;
	
	
	@FXML 
	public void initialize() {
		listProductoController = new ListProductoController(FXCollections.observableArrayList());
		Pane table = TableViewLoad();
		tableViewController.setItems(listProductoController.getListProducto());
		tableLayout.setCenter(table);
			
	}

	@FXML
	public void closeClcik() {
				
	}
	
	@FXML 
	public void saveExel() {
		
	}
	
	@FXML 
	public void saveAs() {
		
	}
	
	@FXML 
	public void loadExel() {
		
	}
	
	@FXML 
	public void printTable() {
		
	}

	@FXML 
	public void addProducto() { // intentar que no guarde el mismo elemento
		Producto producto = new Producto();
		showEditProducto(producto);
		if (editProductoController.isOnClickAceptar()) {
			listProductoController.add(producto);
		}
	}
	
	@FXML 
	public void editProducto() {
		if (tableViewController.getSelectionItemsSize() == 1) {
			Producto item = tableViewController.getSeletedItem();
			if (item == null) 
				throw new AppExeption("Item vacio");
			String idNegocio = item.getIdNegocio();
			showEditProducto(item);
			if (editProductoController.isOnClickAceptar()) {
				item.updateDate();
				// conecta a sql
			}
			
		}
		else if (tableViewController.getSelectionItemsSize() > 1) {
			DialogShow.Warning("Advertencia", "No se puede editar mas de un elemento");
		}
	}
	
	@FXML 
	public void delProducto() {
		int selectionSize = tableViewController.getSelectionItemsSize();
		DialogShow dialogShow = new DialogShow(AlertType.CONFIRMATION);
		dialogShow.setTitle("Eliminar");
				
		if (selectionSize > 1) 
			dialogShow.setContent("¿ Esta seguro que quiere eliminar "+selectionSize+" productos seleccionados?");
		else {
			Producto producto = tableViewController.getSeletedItem();
			dialogShow.setContent("¿ Quieres eliminar el producto "
					+producto.getNombre()+" con el codigo de negocio "+producto.getIdNegocio()+" ?");
		}
		
		dialogShow.show();
		
		if (dialogShow.isCancelButton()) { 
			tableViewController.clearSelection();
			return;
		}
		System.out.println(tableViewController.getSelectionItemsSize());	
		for (Producto prod : tableViewController.getSelectionItems()) {
			if (dialogShow.isOkButton()) {
				listProductoController.del(prod);
				// sql
				
			} 
		}
		
		tableViewController.clearSelection();
				
	}

	@FXML 
	public void searchProducto() {
		
	}
	
	
	// unir estas funciones 
	private Pane TableViewLoad() {
		FXMLLoader tableFxmlLoader = new FXMLLoader(getClass().getResource(TABLE_VIEW_FXML));
		Pane pane = null;
		try {
			pane = tableFxmlLoader.load();
			this.tableViewController = tableFxmlLoader.getController(); 
		} catch (IOException e) {
			throw new AppExeption("Error al cargar ", TABLE_VIEW_FXML);
		}
		return pane;
	}
	
	private void showEditProducto(Producto producto) {
		FXMLLoader editProductoFxml = new FXMLLoader(getClass().getResource(EDIT_PRODUCTO_FXML));
		
		try {
			AnchorPane layout = (AnchorPane) editProductoFxml.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(layout));
			this.editProductoController = editProductoFxml.getController();
			this.editProductoController.setProducto(producto);
			this.editProductoController.setStage(stage);
			stage.showAndWait();
		} catch (AppExeption | IOException e) {
			throw new AppExeption("Error al cargar ", EDIT_PRODUCTO_FXML);
		}  
		
	}
	
}
