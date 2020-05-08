package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import com.mysql.fabric.xmlrpc.base.Value;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;


public class ProductoTableExel {
	
	
	private final static String FILE_NAME = "MerceriaLili"; 
	private final static String FILE_PRINT_NAME = "MerceriaLiliPrint";
	private final static String SHEET_NAME = "Porductos";
	private final static ProductoExel productoExel = null; 
	//private static ExelFile exelFile = null;
	// columnas string
	private static final String ROW_ID_NEGOCIO = "Codigo Negocio";
	private static final String ROW_ID_EMPRESA = "Codigo Empresa";
	private static final String ROW_PRODUCTO = "Nombre Producto";
	private static final String ROW_PRECIO_COSTO = "Precio de Costo";
	private static final String ROW_PRECIO_VENTA = "Precio de Venta";
	private static final String ROW_PRECIO_CANTIDAD = "Precio x Cantidad";
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
	
	private final static String[] ROW_NAME = {ROW_ID_EMPRESA,ROW_ID_NEGOCIO,ROW_PRODUCTO,ROW_PRECIO_VENTA,
												ROW_PRECIO_CANTIDAD,ROW_PRECIO_COSTO,ROW_RECARGO};
	
	
//	public ProductoTableExel() {
//		exelFile = new ExelFile();
//	}
	
	public static void createTable(ExelFile exelFile,XSSFWorkbook book) throws IOException {
		
		exelFile.createSheet(book,SHEET_NAME);
		exelFile.addRow(SHEET_NAME, 0);
		
		for (int i = 0; i < ROW_NAME.length; i++)
			exelFile.addCellAndValue(exelFile.getRow(SHEET_NAME, 0), i, ROW_NAME[i]);
				
		book.close();	// ver si al cerran no escribe 
	}
	
	
	public static void dropTable() {
		
	}
	
	/// estos metodos tendria que pasarlos a Producto exel 
	public static String getFileName() {return FILE_NAME;	}

	public static String getFilePrintName() {return FILE_PRINT_NAME;	}

	public static String getSheetName() {return SHEET_NAME;	}

	public static String getRowProducto() {return ROW_PRODUCTO;	}
	
	public static String getRowIdNegocio() {return ROW_ID_NEGOCIO;}

	public static String getRowIdEmpresa() {return ROW_ID_EMPRESA;}

	public static String getRowNombreProducto() {return ROW_PRODUCTO;}

	public static String getRowPrecioCosto() {return ROW_PRECIO_COSTO;}

	public static String getRowPrecioVenta() {return ROW_PRECIO_VENTA;}

	public static String getRowPrecioCantidad() {return ROW_PRECIO_CANTIDAD;}

	public static String getRowRecargo() {return ROW_RECARGO;}

	public static int getIndexIdEmpresa() {return INDEX_ID_EMPRESA;}

	public static int getIndexIdNegocio() {return INDEX_ID_NEGOCIO;	}

	public static int getIndexNombreProducto() {return INDEX_NOMBRE_PRODUCTO;}

	public static int getIndexPrecioCosto() {return INDEX_PRECIO_COSTO;	}

	public static int getIndexPrecioVenta() {return INDEX_PRECIO_VENTA;	}

	public static int getIndexPrecioCantidad() {return INDEX_PRECIO_CANTIDAD;	}

	public static int getIndexRecargo() {return INDEX_RECARGO;	}

	public static int getIndexLast() {return INDEX_LAST;}

	public static String[] getRowName() {return ROW_NAME;	}
		
		
	
	
}
