package view;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import Exeptions.AppExeption;
import Exeptions.ExelExeption;
import Exeptions.TableViewExeption;
import app.Main;
import conection.MysqlProductoDao;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.DialogShow;
import model.Producto;
import model.ProductoExel;



public class ControllerPrincipal {

	@FXML private TableView<Producto> tableProducto;
	@FXML private TableColumn<Producto,String> codEmpresa;
	@FXML private TableColumn<Producto, String> codNegocio;
	@FXML private TableColumn<Producto, String> nombre;
	@FXML private TableColumn<Producto, Number> precioCosto;
	@FXML private TableColumn<Producto, Number> precioCantidad;
	@FXML private TableColumn<Producto, Number> precioVenta;
	@FXML private TableColumn<Producto, LocalDate> create;
	@FXML private TableColumn<Producto, String> detalle;
	@FXML private SplitPane splitPane; 
	@FXML private TextField txfSearch;
	
	private Main app; // la aplicacion principal o ventana principal
	private MysqlProductoDao mysqlProductoDao;
	private Stage stage;
	private Stage stageEditProducto; 
	private FilteredList<Producto> filteredList;
	private ProductoExel productoExel;
	
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
		productoExel = new ProductoExel();
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
		create.setCellValueFactory(value -> value.getValue().getUpdatedAtProperty());
		detalle.setCellValueFactory(value -> value.getValue().getDetalleProperty());

	}
	
	public void setMainApp(Main mainApp) { // se llama de main 
		this.app = mainApp;
		startTable();
		tableProducto.setItems(app.getListProducto());
		filteredList = new FilteredList<Producto>(tableProducto.getItems(), p -> true);
		
	}
	 
	@FXML
	public  void addProducto()  { // interactua con editar producto
		Producto prod = new Producto();
		try {
			app.mostrarEditProducto(prod, stageEditProducto); // muestra la ventana de editar producto
//			app.mostrarEditProducto(cargarProducto(), stageEditProducto); // muestra la ventana de editar producto
			if (app.isOnClickConfirmation()) { 
				prod.setCreatedAt(LocalDate.now());
				prod.setUpdatedAt(LocalDate.now());
				getMysqlProductoDao().insert(prod); // lo ingresa a la base de datos
//				getMysqlProductoDao().insert(cargarProducto()); // lo ingresa a la base de datos
				refrshTable();
				//tableProducto.getItems().add(prod);
			}
			
		} catch (IOException e) {
			new AppExeption("No es posible mostrar la ventada editar producto "+e.getMessage());
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
					productoSelect.setUpdatedAt(LocalDate.now());
					mysqlProductoDao.update(productoSelect,idNegocio);
					refrshTable();
				}
			} catch (IOException e) {
				new AppExeption("No es posible mostrar la ventada editar producto "+e.getMessage());
			}
		}
		else 
			new AppExeption("No selecciono ningun producto");
			
		
	}
	
	@FXML
	public void delProducto() {
		int selectIndex = tableProducto.getSelectionModel().getSelectedIndex();
		Producto productoSelect = tableProducto.getSelectionModel().getSelectedItem();
		
		if (selectIndex >= 0 && productoSelect !=null) {
			
			DialogShow.Confirmarion("" , "¿ Quieres eliminar el producto "
					+productoSelect.getNombre()+" con el codigo de negocio "+productoSelect.getIdNegocio()+" ?");
					
			if (DialogShow.isResultOption()) {
			
				mysqlProductoDao.delete(productoSelect);
				refrshTable();
				//tableProducto.getItems().remove(selectIndex);
			}
				
		}
		else 
			new AppExeption("No selecciono ningun producto");
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
			            if (pers.getNombre().toLowerCase().indexOf(typedText) !=-1 ) 
			            	return true;
			           
			            return false;
			        });
		   
			        SortedList<Producto> sortedList = new SortedList<>(filteredList);
			        sortedList.comparatorProperty().bind(tableProducto.comparatorProperty());
			        tableProducto.setItems(sortedList);
			        tableProducto.getSelectionModel().select(0);
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
		try {
			mysqlProductoDao.mostrarProductoTabla(tableProducto.getItems());
		} catch (SQLException e) {
			new TableViewExeption("No es posible mostar o actualizar la tabla "+e.getMessage());
		}
		//filteredList.addAll(tableProducto.getItems()); // no hace falta la idea era refrescar la tabla
	}
	
	
	@FXML // falta optimizar 
	public void saveExel() {
		File file = fileSelection("Guardar",JFileChooser.SAVE_DIALOG);
		try {
			if (file != null)
				productoExel.saveExel(tableProducto.getItems(),file);
		} catch (InvalidFormatException | IOException e) {
			new ExelExeption("No es podible guardar el archivo exel \n"+e.getMessage());
			
		}

	}

	@FXML
	public void loadExel() {
		File file = fileSelection("Abrir", JFileChooser.OPEN_DIALOG);
		try {
			if (file != null)
				productoExel.loadExel(tableProducto.getItems(),file);
			
		} catch (InvalidFormatException | IOException e) {
			new ExelExeption("No es podible cargar el archivo exel \n"+e.getMessage());
			
		}
	}
	
	@FXML
	public void printExel() {
		try {
			productoExel.printExelSave(tableProducto.getItems(),fileSelection("Guardar", JFileChooser.SAVE_DIALOG));
			
		} catch (InvalidFormatException | IOException e) {
			new ExelExeption("No es podible cargar el archivo exel \n"+e.getMessage());
			
		}
	}

	public void startTable() {
		try {
			mysqlProductoDao.mostrarProductoTabla(app.getListProducto());
		} catch (SQLException e) {
			loadExel();
			new TableViewExeption("No es posible mostar o actualizar la tabla "+e.getMessage());
			
		}
		finally {
			//loadExel();
		}
	}	
	
	private File fileSelection(String title, int action) {
		String defaultDyrectory = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+"\\MerceriaLili";
		File file = new File(defaultDyrectory);
		file.mkdir();
		JFileChooser jFile = new JFileChooser(file);
		jFile.setDialogTitle(title);
		jFile.setDialogType(action);
		jFile.setSelectedFile(new File("productos.xlsx"));
		jFile.setFileFilter(new FileNameExtensionFilter("exel file", "xlsx"));
		if (jFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				return jFile.getSelectedFile();
		return null;
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
	
	private Producto  cargarProducto() {
		return new Producto("PuebaFecha","B02", "B2","Boton", 5.0,0.0, 5.0, 10.0,LocalDate.now());
	}
}
