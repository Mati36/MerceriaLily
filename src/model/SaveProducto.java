package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import conection.MysqlProductoDao;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SaveProducto extends ExelFile {
	
	// llamaria a MySql y ProductoExel para guardar el archivo
	

	// columnas string
	private static final String ROW_ID_NEGOCIO = "Codigo Negocio";
	private static final String ROW_ID_EMPRESA = "Codigo Empresa";
	private static final String ROW_NOMBRE_PRODUCTO = "Nombre Producto";
	private static final String ROW_PRECIO_COSTO = "Precio Costo";
	private static final String ROW_PRECIO_VENTA = "Precio Venta";
	private static final String ROW_PRECIO_CANTIDAD = "Precio Cantidad";
	private static final String ROW_RECARGO= "Recargo";
				
	// index columnas
	private static final int INDEX_ID_EMPRESA = 0;
	private static final int INDEX_ID_NEGOCIO = 1;
	private static final int INDEX_NOMBRE_PRODUCTO = 2;
	private static final int INDEX_PRECIO_COSTO = 3;
	private static final int INDEX_PRECIO_VENTA = 4;
	private static final int INDEX_PRECIO_CANTIDAD = 5;
	private static final int INDEX_RECARGO = 6;
	private static final int INDEX_LAST = 7;
	
	private final static String[] ROW_NAME = {ROW_ID_EMPRESA,ROW_ID_NEGOCIO,ROW_NOMBRE_PRODUCTO,ROW_PRECIO_VENTA,
												ROW_PRECIO_CANTIDAD,ROW_PRECIO_COSTO,ROW_RECARGO};
	
	
	private final static String FILE_NAME = "MerceriaLili"; 
	private final static String FILEPRINT_NAME = "MerceriaLili"; 
	private static ProductoExel productoExel;
	private MysqlProductoDao mysqlProductoDao;
	
	
	public static String getRowIdNegocio() {
		return ROW_ID_NEGOCIO;
	}

	public static String getRowIdEmpresa() {
		return ROW_ID_EMPRESA;
	}

	public static String getRowNombreProducto() {
		return ROW_NOMBRE_PRODUCTO;
	}

	public static String getRowPrecioCosto() {
		return ROW_PRECIO_COSTO;
	}

	public static String getRowPrecioVenta() {
		return ROW_PRECIO_VENTA;
	}

	public static String getRowPrecioCantidad() {
		return ROW_PRECIO_CANTIDAD;
	}

	public static String getRowRecargo() {
		return ROW_RECARGO;
	}

	public static int getIndexIdEmpresa() {
		return INDEX_ID_EMPRESA;
	}

	public static int getIndexIdNegocio() {
		return INDEX_ID_NEGOCIO;
	}

	public static int getIndexNombreProducto() {
		return INDEX_NOMBRE_PRODUCTO;
	}

	public static int getIndexPrecioCosto() {
		return INDEX_PRECIO_COSTO;
	}

	public static int getIndexPrecioVenta() {
		return INDEX_PRECIO_VENTA;
	}

	public static int getIndexPrecioCantidad() {
		return INDEX_PRECIO_CANTIDAD;
	}

	public static int getIndexRecargo() {
		return INDEX_RECARGO;
	}

	public static int getIndexLast() {
		return INDEX_LAST;
	}

	public static String[] getRowName() {
		return ROW_NAME;
	}

	
	/// seguir por aca, esta clase deveria pasar los datos de la tabla a ProductosExel y llamar a mySql
	public static void exelSave(ObservableList<Producto> tableList) throws IOException {
		productoExel.exelSave(tableList, INDEX_LAST,FILE_NAME);
	}
	
	public static void exelLoad(ObservableList<Producto> tableList) throws InvalidFormatException, FileNotFoundException, IOException {
		productoExel.loadExel(tableList, INDEX_LAST, FILE_NAME);
		
	}
	
	public static void printSave(ObservableList<Producto> tableList) throws IOException {
		productoExel.exelSave(tableList, INDEX_LAST - 2,FILEPRINT_NAME);
	}
	
	public static void printLoad(ObservableList<Producto> tableList) throws InvalidFormatException, FileNotFoundException, IOException {
		productoExel.loadExel(tableList, INDEX_LAST - 2, FILEPRINT_NAME);
	}
	
	private boolean dialogAlert(String titel, String content, Alert alertType) {
		 DialogAlert dialogAlert = new DialogAlert(content, titel, alertType) ;
		return  dialogAlert.getResultOption();
	}
}
