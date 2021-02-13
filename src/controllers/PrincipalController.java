package controllers;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import exeptions.AppExeption;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.DialogShow;
import models.Producto;

public class PrincipalController {
	
	private final String TABLE_VIEW_FXML = "/views/TableView.fxml";
	private final String EDIT_PRODUCTO_FXML ="/views/EditProducto.fxml";
	private final String DOCUMENT_DIRECTORY = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+"/MerceriaLili";
	private final String DEFAUL_FILE = "lista_producto.dat";
	private ListProductoController listProductoController;
	private EditProductoController editProductoController;
	private TableViewController tableViewController;
	@FXML private BorderPane tableLayout;
	@FXML private SplitPane splitPane; 
	@FXML private Pane pane;
	
	
	@FXML 
	public void initialize() {
		listProductoController = new ListProductoController();
		Pane table = TableViewLoad();
		listProductoController.load(new File(DOCUMENT_DIRECTORY+"/"+DEFAUL_FILE));
		tableViewController.setItems(listProductoController.getListProducto());
		tableLayout.setCenter(table);
			
	}

	@FXML
	public void closeClcik() {
		DialogShow dialogShow = new DialogShow(AlertType.CONFIRMATION);
		dialogShow.setTitle("Guardar");
		dialogShow.setContent("¿Quieres Guardar antes de salir?");
		dialogShow.show();
		if (dialogShow.isOkButton()){
			File file = new File(DOCUMENT_DIRECTORY+"/"+DEFAUL_FILE);
			if (file.exists()) 
				listProductoController.save(file);
		}
			
	}
	
	@FXML 
	public void save() {
		listProductoController.save(new File(DOCUMENT_DIRECTORY+"/"+DEFAUL_FILE));
	}
	
	@FXML 
	public void saveAs() {
		File file = fileSelection("Guardar",JFileChooser.SAVE_DIALOG,true);
		listProductoController.save(file);
	}
	
	@FXML 
	public void importFile() {
		File file = fileSelection("Abrir", JFileChooser.OPEN_DIALOG,false);
		listProductoController.load(file);
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
		
		listProductoController.removItemsSelection(tableViewController.getSelectionItems());
		//sql
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
	
private File fileSelection(String title, int action,boolean save) {
		
		File file = new File(DOCUMENT_DIRECTORY);
		file.mkdir();
		FileChooser jFile = new FileChooser();
		jFile.setTitle(title);
		jFile.setInitialDirectory(file);
		jFile.getExtensionFilters().addAll(
		         new ExtensionFilter("Tipo dato", "*.dat"));
		Stage stage = new Stage();		
		return  save ? jFile.showSaveDialog(stage) : jFile.showOpenDialog(stage);
			
	}
	
}
