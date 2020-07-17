package view;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import Exeptions.AppExeption;
import Exeptions.ExelExeption;
import app.Main;
import conection.MysqlProductoDao;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
	private final String documentDirectory = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+"/MerceriaLili";
	private final String defaultFileDirectory = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+"/MerceriaLili/default/productos.xlsx";
	
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
		tableProducto.setItems(app.getListProducto());
		filteredList = new FilteredList<Producto>(tableProducto.getItems(), p -> true);
		startTable();
	}
	 
	@FXML
	public  void addProducto()  { 
		Producto prod = new Producto();
		try {
			app.mostrarEditProducto(prod, stageEditProducto); // muestra la ventana de editar producto
			
			if (app.isOnClickConfirmation()) { 
				prod.setCreatedAt(LocalDate.now());
				prod.setUpdatedAt(LocalDate.now());
				
				if (mysqlProductoDao.isConnectionSql()) { // lo ingresa a la base de datos
					getMysqlProductoDao().createTable();
					getMysqlProductoDao().insert(prod); 
					refrshTable();
				}
				else 
					app.getListProducto().add(prod);
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
					if (mysqlProductoDao.isConnectionSql()) {
						mysqlProductoDao.update(productoSelect,idNegocio);
						refrshTable();
					}
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
				if (mysqlProductoDao.isConnectionSql()) {
					mysqlProductoDao.delete(productoSelect);
					refrshTable();
				}
				else 
					getMainApp().getListProducto().remove(productoSelect);
				
			}
				
		}
		else 
			new AppExeption("No selecciono ningun producto");
	}
	
	@FXML
	public void searchProducto() {

		txfSearch.textProperty()
				 .addListener(p -> {
					String searchText = txfSearch.getText();
					filterId(searchText);
					
					if (filteredList.isEmpty())
						filterNoId(searchText);
										
					SortedList<Producto> sortedList = new SortedList<>(filteredList);
			        sortedList.comparatorProperty().bind(tableProducto.comparatorProperty());
			        
			        tableProducto.setItems(sortedList);
			        tableProducto.getSelectionModel().select(0);
				 });
			
		} 

	
	private boolean isProducto(String valueProduc, String valueSearch) {
		
		valueProduc = valueProduc.toLowerCase().replaceAll("\\s+", "").trim();
		valueSearch = valueSearch.toLowerCase().replaceAll("\\s+", "").trim();
		
		return valueProduc.startsWith(valueSearch);
	}
	
	private void filterId(String searchText) {
		filteredList.setPredicate(prod -> {
			
			if (isProducto(prod.getIdNegocio(), searchText)) 
				return true;
			
			return false;
		});
		
		if (filteredList.isEmpty())
			filterIdEmpresa(searchText);
	}
	
	private void filterIdEmpresa(String searchText) {
			filteredList.setPredicate(prod -> {
					
				if (isProducto(prod.getIdEmpresa(), searchText)) 
					return true;
				
				return false;
			});
	}

	private void filterNoId(String searchText) {
		filteredList.setPredicate(prod -> {
			String  nameAndDetalle = prod.getNombre().concat(prod.getDetalle());
			
			if (isProducto(nameAndDetalle, searchText))
	        		return true;
			else if (isProducto(prod.getNombre(), searchText)) 
	        		return true;
			else if (isProducto(prod.getDetalle(), searchText))		
	        		return true;
			
			return false;
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
		if(mysqlProductoDao.isConnectionSql())
			mysqlProductoDao.mostrarProductoTabla(tableProducto.getItems());
	
	}
	
	
	@FXML
	public void saveExel() {
		File file = new File(defaultFileDirectory);
		file.getParentFile().mkdir();
		
		try {
			
			if (file != null)
				productoExel.saveExel(app.getListProducto(),file);
		} catch (InvalidFormatException | IOException e) {
			new ExelExeption("No es podible guardar el archivo exel \n"+e.getMessage());
			
		}

	}
	
	@FXML
	public void saveAs() {
		File file = fileSelection("Guardar",JFileChooser.SAVE_DIALOG,true);
		try {
			if (file != null)
				productoExel.saveExel(app.getListProducto(),file);
		} catch (InvalidFormatException | IOException e) {
			new ExelExeption("No es podible guardar el archivo exel \n"+e.getMessage());
			
		}

	}
	
	@FXML
	public void load() {
		
		File file = new File(defaultFileDirectory);
		
		if (file.exists()) {
			try {
				if (file != null)
					productoExel.loadExel(app.getListProducto(),file);
				
			} catch (InvalidFormatException | IOException e) {
				new ExelExeption("No es podible cargar el archivo exel \n"+e.getMessage());
				
			}
		}
	}
	
	@FXML
	public void loadExel() {
		File file = fileSelection("Abrir", JFileChooser.OPEN_DIALOG,false);
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
			productoExel.printExelSave(tableProducto.getItems(),fileSelection("Guardar", JFileChooser.SAVE_DIALOG,true));
			
		} catch (InvalidFormatException | IOException e) {
			new ExelExeption("No es podible cargar el archivo exel \n"+e.getMessage());
			
		}
	}

	public void startTable() {
		if (mysqlProductoDao.isConnectionSql())
			mysqlProductoDao.mostrarProductoTabla(app.getListProducto());
		else
			load();
	}
		
	private File fileSelection(String title, int action,boolean save) {
		
		File file = new File(documentDirectory);
		file.mkdir();
		FileChooser jFile = new FileChooser();
		jFile.setTitle(title);
		jFile.setInitialDirectory(file);
		jFile.getExtensionFilters().addAll(
		         new ExtensionFilter("Exel Files", "*.xlsx"));
		javafx.stage.Window wd = this.stage.getOwner();
		
		return  save ? jFile.showSaveDialog(wd) : jFile.showOpenDialog(wd);
			
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
