package view;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFileChooser;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import app.Main;
import app.Producto;
import conection.MysqlProductoDao;
import controller.DialogAlert;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class ControllerPrincipal {

	@FXML private TableView<Producto> tableProducto;
	@FXML private TableColumn<Producto,String> codEmpresa;
	@FXML private TableColumn<Producto, String> codNegocio;
	@FXML private TableColumn<Producto, String> nombre;
	@FXML private TableColumn<Producto, Number> precioCosto;
	@FXML private TableColumn<Producto, Number> precioCantidad;
	@FXML private TableColumn<Producto, Number> precioVenta;
	@FXML private SplitPane splitPane; 
	@FXML private TextField txfSearch;
	
	private Main app; // la aplicacion principal o ventana principal
	private MysqlProductoDao mysqlProductoDao;
	private Stage stage;
	private Stage stageEditProducto; 
	private FilteredList<Producto> filteredList;
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public ControllerPrincipal() {
		splitPane = new SplitPane();
		txfSearch= new TextField();
		stageEditProducto = new Stage();
		mysqlProductoDao = new MysqlProductoDao(); // para comunicarse con la base de datos
	
		
	}

	@FXML
    private void initialize() { // inicia la tabla
		codEmpresa.setCellValueFactory(value -> value.getValue().getIdEmpresaProperty());
		codNegocio.setCellValueFactory(value -> value.getValue().getIdNegocioProperty());
		nombre.setCellValueFactory(value -> value.getValue().getNombreProperty());
		precioCosto.setCellValueFactory(intValue -> intValue.getValue().getPrecioCostoProperty());
		precioCantidad.setCellValueFactory(value -> value.getValue().getPrecioCantidadProperty());
		precioCantidad.setCellValueFactory(value -> value.getValue().getPrecioCantidadProperty());
		precioVenta.setCellValueFactory(value -> value.getValue().getPrecioVentaProperty());
		//txfSearch.setText("Ingrese el codigo del producto");
	}
	
	public void setMainApp(Main mainApp) { // se llama de main 
		this.app = mainApp;
		mysqlProductoDao.mostrarProductoTabla(app.getListProducto());
		tableProducto.setItems(app.getListProducto());
		filteredList = new FilteredList<Producto>(tableProducto.getItems(), p -> true);
		
	}
	 
	@FXML
	public  void addProducto()  { // interactua con editar producto
		Producto prod = new Producto();
		try {
			app.mostrarEditProducto(prod, stageEditProducto); // muestra la ventana de editar producto
						
			if (app.isOnClickConfirmation()) { 
				getMysqlProductoDao().insert(prod); // lo ingresa a la base de datos
				refrshTable();
				//tableProducto.getItems().add(prod);
			}
			
		} catch (IOException e) {
			dialogAlert("Error","Error al Mostrar ventana de editar producto"+e.getMessage(), new Alert(AlertType.ERROR));
		}
	}
	
	@FXML
	public void editProducto() {
		int selectIndex = tableProducto.getSelectionModel().getSelectedIndex();
		Producto productoSelect = tableProducto.getSelectionModel().getSelectedItem();
		app.setOnClickConfirmation(false);
		
		if (selectIndex >= 0 && productoSelect != null) {
			String idNegocio = productoSelect.getIdNegocio();
			try {
				app.mostrarEditProducto(productoSelect,stageEditProducto);
				
				if (app.isOnClickConfirmation()) {
					mysqlProductoDao.update(productoSelect,idNegocio);
					refrshTable();
				}
			} catch (IOException e) {
				System.out.println("Error al Mostrar ventana de editar producto"+e.getMessage());
			}
		}
		else {
			dialogAlert("Error","Error al eliminar no selecciono niungun producto", new Alert(AlertType.ERROR));
		}
	}
	
	@FXML
	public void delProducto() {
		int selectIndex = tableProducto.getSelectionModel().getSelectedIndex();
		Producto productoSelect = tableProducto.getSelectionModel().getSelectedItem();
		
		if (selectIndex >= 0 && productoSelect !=null) {
			
			boolean confirmation= dialogAlert("Confirmation", "¿ Quieres eliminar el producto de Nombre: "+productoSelect.getNombre()
											+" y Codigo: "+productoSelect.getIdNegocio()+" ?", new Alert(AlertType.CONFIRMATION));
			if (confirmation) {
				try {
					mysqlProductoDao.delete(productoSelect);
					refrshTable();
					//tableProducto.getItems().remove(selectIndex);
				} catch (Exception e) {
					dialogAlert("Error","Error al eliminar en base de datos,"+e.getMessage()+" "+e.getStackTrace(), new Alert(AlertType.ERROR));
					
				}
			}
				
		}
		else 
			dialogAlert("Error","Error al eliminar no selecciono niungun producto", new Alert(AlertType.ERROR));
	}
	
	@FXML
	public void searchProducto() {
	
		txfSearch.setText(null);
		txfSearch.textProperty()
				.addListener((obsevable, oldvalue, newvalue) -> {
			        filteredList.setPredicate(pers -> {
			
			            if (newvalue == null || newvalue.isEmpty()) 
			                return true;
			            String typedText = newvalue.toLowerCase();
			            if (pers.getIdNegocio().toLowerCase().indexOf(typedText) != -1) 
			            	return true;
			            return false;
			        });
		   
			        SortedList<Producto> sortedList = new SortedList<>(filteredList);
			        sortedList.comparatorProperty().bind(tableProducto.comparatorProperty());
			        tableProducto.setItems(sortedList);
				});
	} 
	
	@FXML
	public void serchOnMouseclicked() {
		txfSearch.setText(null);
	}
	
	@FXML
	public void serchExitedMouse() {
		txfSearch.setText("Ingrese el codigo del producto");
	}
	
	@FXML
	public void closeClcik() {
		app.getPrimaryStage().close();
	}
	
	public Main getMainApp () {
		return this.app;
	}
	
	public MysqlProductoDao getMysqlProductoDao() {
		return this.mysqlProductoDao;
	}

	public TableView<Producto> getTableProducto() {
		return tableProducto;
	}
	
	private void refrshTable() {
		mysqlProductoDao.mostrarProductoTabla(tableProducto.getItems());
		//filteredList.addAll(tableProducto.getItems()); // no hace falta la idea era refrescar la tabla
	}
	
	
	@FXML // falta optimizar 
	public void saveExel() {
		JFileChooser jFileChooser = new JFileChooser("Nuevo.xls");
		jFileChooser.setDialogTitle("Guardar archivo");;
		int result = jFileChooser.showSaveDialog(null);
		
		if (result == jFileChooser.APPROVE_OPTION) {
			try {
				String path = jFileChooser.getSelectedFile().getPath();
				Workbook book = new XSSFWorkbook();
				Sheet sheet = book.createSheet("Productos");
				Row row = sheet.createRow(0);
				rowSheetCreate(row);
				
				if(!path.endsWith(".xls")) 	
					path+=".xls";
				
				mysqlProductoDao.mySqlToExelLoad(sheet);
				
				FileOutputStream newFile = new FileOutputStream(path);
				book.write(newFile);
				
				newFile.close();
				book.close();
			
			} catch (IOException | SQLException e) {
				dialogAlert("Error", "Error al guardar Archivo, "+e.getMessage(), new Alert(AlertType.ERROR));
			}
		}	
	}
	
	@FXML
	public void loadExel() {
		
	}
	
	private boolean dialogAlert(String titel, String content, Alert alertType) {
		 DialogAlert dialogAlert = new DialogAlert(content, titel, alertType) ;
		return  dialogAlert.getResultOption();
	}
	
	
	private void rowSheetCreate(Row row) {
		row.createCell(0).setCellValue("Codigo Empresa");
		row.createCell(1).setCellValue("Codigo Negocio");
		row.createCell(2).setCellValue("Nombre Producto");
		row.createCell(3).setCellValue("Precio Costo");
		row.createCell(4).setCellValue("Precio Venta");
		row.createCell(5).setCellValue("Precio Cantidad");
	}
	
	// metodos de test
	private void mostrarProductoConsola(String string, Producto prod) { 
		System.out.println(string);
		System.out.println("Nombre: "+prod.getNombre());
		System.out.println("Id empresa "+prod.getIdEmpresa());
		System.out.println("Id negocio "+prod.getIdNegocio());
		System.out.println("precio costo "+prod.getPrecioCosto());
		System.out.println("precio venta "+prod.getPrecioVenta());
		System.out.println("precio cantidad "+prod.getPrecioCantidad());
		System.out.println();
	}
	
}
