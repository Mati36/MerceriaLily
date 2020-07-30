package view;

import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.print.attribute.standard.PageRanges;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import Exeptions.AppExeption;
import Exeptions.ExelExeption;
import app.Main;
import conection.MysqlProductoDao;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DialogShow;
import model.PrinterTable;
import model.Producto;
import model.ProductoExel;
import model.ProductoTableExel;

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
		optionsSelection(tableProducto);
		
		
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
		TableViewSelectionModel<Producto> selectionItems = tableProducto.getSelectionModel();
		int selectionSize = selectionItems.getSelectedItems().size(); 
		if (selectionSize == 1 ) {
			Producto productoSelect = selectionItems.getSelectedItem();
			app.setOnClickConfirmation(false);
			if (productoSelect != null) {
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
		}
		else if (selectionSize > 1) 
			new AppExeption("No se puede editar mas de un producto al mismo tiempo");
		else 
			new AppExeption("No selecciono ningun producto");
	}
	
	@FXML
	public void delProducto() {
		
		ArrayList<Producto> selectItems = new ArrayList<Producto>(tableProducto.getSelectionModel().getSelectedItems());
		
		for (Producto producto : selectItems) {
							
			DialogShow.Confirmarion("" , "¿ Quieres eliminar el producto "
					+producto.getNombre()+" con el codigo de negocio "+producto.getIdNegocio()+" ?");
					
			if (DialogShow.isResultOption()) {
					
				if (mysqlProductoDao.isConnectionSql()) {
					mysqlProductoDao.delete(producto);
					refrshTable();
				}
				else 
					getMainApp().getListProducto().remove(producto);
			
			}
				
		}
	}
	
	@FXML
	public void searchProducto() {
	
			String searchText = txfSearch.getText().trim();
			filterId(searchText);
			
			if (filteredList.isEmpty())
				filterNoId(searchText);
								
			SortedList<Producto> sortedList = new SortedList<>(filteredList);
	        sortedList.comparatorProperty().bind(tableProducto.comparatorProperty());
	        
	        tableProducto.setItems(sortedList);
	        tableProducto.getSelectionModel().selectFirst();
	        
			
		} 

	
	private boolean isProducto(String valueProduc, String valueSearch) {
		
		valueProduc = valueProduc.toLowerCase().replaceAll("\\s+", "").trim();
		valueSearch = valueSearch.toLowerCase().replaceAll("\\s+", "").trim();
		
		return valueProduc.startsWith(valueSearch);
	}
	
	private void filterId(String searchText) {
		
		filteredList.setPredicate(prod -> isProducto(prod.getIdNegocio(), searchText));
		
		if (filteredList.isEmpty())
			filterIdEmpresa(searchText);
	}
	
	private void filterIdEmpresa(String searchText) {
		filteredList.setPredicate(prod -> isProducto(prod.getIdEmpresa(), searchText)); 
		
	}

	private void filterNoId(String searchText) {
		filteredList.setPredicate(prod -> {
			String  nameAndDetalle = prod.getNombre().concat(prod.getDetalle());
			return ( isProducto(nameAndDetalle, searchText) 
					|| isProducto(prod.getNombre(), searchText) 
					|| isProducto(prod.getDetalle(), searchText) );
	      
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
	
	@FXML
	public void printTable() {
		PrinterJob printer = PrinterJob.createPrinterJob();
		
		if (printer.showPrintDialog(stage))
			PrinterTable.printTable(tableProducto, printer);
		printer.endJob();
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
				
		return  save ? jFile.showSaveDialog(stage) : jFile.showOpenDialog(stage);
			
	}

	private void optionsSelection(TableView<Producto> table) {
		
		
		tableProducto.setRowFactory(new Callback<TableView<Producto>, TableRow<Producto>>() {
		    @Override
		    public TableRow<Producto> call(TableView<Producto> tableView2)
		    {
		        final TableRow<Producto> row = new TableRow<>();

		        row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		            @Override
		            public void handle(MouseEvent event)
		            {
		                final int index = row.getIndex();

		                if (index >= 0 && index < tableProducto.getItems().size())
		                {
		                	TableViewSelectionModel<Producto> selectionItems = tableProducto.getSelectionModel();		                	
		                	ArrayList<Integer> indices = new ArrayList<Integer>( selectionItems.getSelectedIndices());
		                	
		                    if(indices.indexOf(index) >= 0) {
		                    	selectionItems.clearSelection(index);
		                    	indices.remove((Integer) index);
		                    }
		                    else {
		                    	indices.add(index);
		                    }
		                    
		                   for (Integer item : indices) {
		                	   if (!selectionItems.isSelected(item))
		                    		selectionItems.select(item);
		                   }
		                   event.consume();
		                }
		            }
		        });
		        return row;
		    }
		});
	}
}
