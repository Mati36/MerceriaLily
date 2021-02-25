package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Predicate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.DialogShow;
import models.PrinterTable;
import models.Producto;
import models.ProductoTableExel;

public class TableViewProductoController extends TableViewController<Producto>{

	@FXML private TableColumn<Producto,String> codEmpresa;
	@FXML private TableColumn<Producto, String> codNegocio;
	@FXML private TableColumn<Producto, String> nombre;
	@FXML private TableColumn<Producto, Number> precioCosto;
	@FXML private TableColumn<Producto, Number> precioCantidad;
	@FXML private TableColumn<Producto, Number> precioVenta;
	@FXML private TableColumn<Producto, LocalDate> create;
	@FXML private TableColumn<Producto, String> detalle;
	private final String TABLE_PRINTER_FXML = "/views/PrinterTableA4.fxml";
	
	
	@FXML 
	public void initialize() {
		
		columnSetTitel(codEmpresa, ProductoTableExel.getRowIdEmpresa());
		columnSetTitel(codNegocio, ProductoTableExel.getRowIdNegocio());
		columnSetTitel(nombre, ProductoTableExel.getRowProducto());
		columnSetTitel(precioCosto, ProductoTableExel.getRowPrecioCosto());
		columnSetTitel(precioCantidad, ProductoTableExel.getRowPrecioCantidad());
		columnSetTitel(precioVenta,ProductoTableExel.getRowPrecioVenta());
		columnSetTitel(detalle,ProductoTableExel.getRowDetalle());
		columnSetTitel(create,ProductoTableExel.getRowUpdateAt());
		
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
	                if ( item == null || empty) {
	                	setText("");
	                	return;
					}
	                Text text = new Text(Producto.dateFormated(item));
	                setGraphic(text);
	          	                
	            }
	        };
	    });
		
		columnWrapp(codEmpresa, 3);
		columnWrapp(codNegocio, 3);
		columnWrapp(nombre, 3);
		columnWrapp(precioCantidad, 3);
		columnWrapp(precioCosto, 3);
		columnWrapp(precioVenta, 3);
		columnWrapp(detalle, 3);
		columnWrapp(create, 3);
		setSelectionModeType(SelectionMode.MULTIPLE);
		
	}
		
	public void searchProducto(String searchText) {
		filteredList.setPredicate(predicateFilter(searchText));
		searchItem(searchText, filteredList);
	} 
	
	private boolean isProducto(String valueProduc, String valueSearch) {
		
		valueProduc = valueProduc.toLowerCase().replaceAll("\\s+", "").trim();
		valueSearch = valueSearch.toLowerCase().replaceAll("\\s+", "").trim();
		
		return valueProduc.startsWith(valueSearch);
	}
	
	private Predicate<Producto> predicateFilter(String searchText) {
		return new Predicate<Producto>() {

			@Override
			public boolean test(Producto prod) {
				String  nameAndDetalle = prod.getNombre().concat(prod.getDetalle());
				return ( isProducto(prod.getIdNegocio(), searchText) || isProducto(prod.getIdEmpresa(), searchText) 
						|| isProducto(prod.getNombre(), searchText)  || isProducto(prod.getDetalle(), searchText) 
						|| isProducto(nameAndDetalle, searchText) );
			}
		};
	}
	
	public void printed(Stage stage) {
		PrinterJob printer = PrinterJob.createPrinterJob();
		
		if (printer.showPrintDialog(stage))
			PrinterTable.printTable(tableProducto, printer);
		printer.endJob();
	}
		
	private <T> String valueToString(T value) {
		if (value instanceof Integer || value instanceof Float || value instanceof Double || value instanceof Long)
			return String.valueOf(value);
		else if (value instanceof LocalDate)
			return Producto.dateFormated((LocalDate) value);
		else return (String) value;
		
	}

	private <T> void columnWrapp(TableColumn<Producto, T> column, int wrappingSize) {
		column.setCellFactory(param -> {
			return new TableCell<Producto, T>() {
	            @Override
	            protected void updateItem(T item, boolean empty) {
	                super.updateItem(item, empty);
		                if (item != null || !empty) {
		                Text text = new Text(valueToString(item));
		                text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(wrappingSize));
		                setGraphic(text);
	                }            
	            }
	        };
	    });
	}
	
}
