package models;


import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProductoTableExel{
	
	
	private final static String FILE_NAME = "MerceriaLili"; 
	private final static String FILE_PRINT_NAME = "MerceriaLiliPrint";
	private final static String SHEET_NAME = "Porductos";

	// columnas string
	private static final String ROW_ID_NEGOCIO = "Codigo Negocio";
	private static final String ROW_ID_EMPRESA = "Codigo Empresa";
	private static final String ROW_PRODUCTO = "Producto";
	private static final String ROW_PRECIO_COSTO = "Precio de Costo";
	private static final String ROW_PRECIO_VENTA = "Precio de Venta";
	private static final String ROW_PRECIO_CANTIDAD = "Precio x Cantidad";
	private static final String ROW_RECARGO= "Recargo";
	static private final String ROW_DETALLE= "Detalle";
	static private final String ROW_CREATED_AT= "Creado";
	static private final String ROW_UPDATE_AT= "Ultima Modificacion";
	static private final String ROW_LAST = ROW_UPDATE_AT; 
	
	static final String ROW_ID_NEGOCIO_ABBREVIATED = "C.N";
	static final String ROW_ID_EMPRESA_ABBREVIATED = "C.E";
	static final String ROW_PRODUCTO_ABBREVIATED = "Producto";
	static final String ROW_PRECIO_COSTO_ABBREVIATED = "P.C";
	static final String ROW_PRECIO_VENTA_ABBREVIATED = "P.v";
	static final String ROW_PRECIO_CANTIDAD_ABBREVIATED = "P.x.C";
	static final String ROW_RECARGO_ABBREVIATED = "Recargo";
	static final String ROW_DETALLE_ABBREVIATED = "Detalle";
	static final String ROW_UPDATE_AT_ABBREVIATED= "F. Mod";
	
	// index columnas
	private static final int INDEX_ID_EMPRESA = 0;
	private static final int INDEX_ID_NEGOCIO = 1;
	private static final int INDEX_NOMBRE_PRODUCTO = 2;
	private static final int INDEX_PRECIO_COSTO = 3;
	private static final int INDEX_PRECIO_VENTA = 4;
	private static final int INDEX_PRECIO_CANTIDAD = 5;
	private static final int INDEX_RECARGO = 6;
	private static final int INDEX_DETALLE = 7;
	private static final int INDEX_CREATED_AT = 8;
	private static final int INDEX_UPDATED_AT = 9;
	private static final int INDEX_LAST = 10;

	
	private final static String[] ROW_NAME = {	ROW_ID_EMPRESA,
												ROW_ID_NEGOCIO,
												ROW_PRODUCTO,
												ROW_PRECIO_VENTA,
												ROW_PRECIO_CANTIDAD,
												ROW_PRECIO_COSTO,
												ROW_RECARGO,
												ROW_DETALLE,
												ROW_CREATED_AT,
												ROW_UPDATE_AT
											};

	public static void createTable(ExelFile exel, XSSFWorkbook book) throws IOException {
		
		exel.createSheet(book,SHEET_NAME);
		exel.addRow(SHEET_NAME);
		for (int i = 0; i < ROW_NAME.length; i++)
			exel.addCellAndValue(exel.getRow(SHEET_NAME, 0), i, ROW_NAME[i]);
		
	}
	
	public static void createPrintTable(ExelFile exel, XSSFWorkbook book) throws IOException {
		
		exel.createSheet(book,SHEET_NAME);
		exel.addRow(SHEET_NAME);
		for (int i = 0; i < ROW_NAME.length; i++) {
			if ( !ignoreCell(i) ) 
				exel.addCellAndValue(exel.getRow(SHEET_NAME, 0), i, ROW_NAME[i]);
		}
			
		
	}
	
	public static void dropTable() {
		
	}
	
	private static boolean ignoreCell(int indexCell) {
		return indexCell == INDEX_RECARGO && indexCell == INDEX_PRECIO_COSTO;
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

	public static String getRowDetalle() {
		return ROW_DETALLE;
	}

	public static String getRowCreatedAt() {
		return ROW_CREATED_AT;
	}

	public static String getRowUpdateAt() {
		return ROW_UPDATE_AT;
	}

	public static String getRowLast() {
		return ROW_LAST;
	}

	public static int getIndexDetalle() {
		return INDEX_DETALLE;
	}

	public static int getIndexCreatedAt() {
		return INDEX_CREATED_AT;
	}

	public static int getIndexUpdatedAt() {
		return INDEX_UPDATED_AT;
	}
		
		
	
	
}
