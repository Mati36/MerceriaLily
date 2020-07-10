package model;

public class ProductoTableSql {
	
	// esta clase tiene el modelo de la tabla sql
	static private final String NAME_TABLE ="productos";
	//static private final String PIMARY_KEY = "Codigo_Negocio";
	static private final String ROW_ID_NEGOCIO = "Codigo_Negocio";
	static private final String ROW_ID_EMPRESA = "Codigo_Empresa";
	static private final String ROW_NOMBRE = "Nombre_Producto";
	static private final String ROW_PRECIO_COSTO = "Precio_Costo";
	static private final String ROW_PRECIO_VENTA = "Precio_Venta";
	static private final String ROW_PRECIO_CANTIDAD = "Precio_Cantidad";
	static private final String ROW_RECARGO= "Recargo";
	static private final String ROW_DETALLE= "Detalle";
	static private final String ROW_CREATED_AT= "CreatedAt";
	static private final String ROW_UPDATE_AT= "UpdatedAt";
	static private final String ROW_LAST = ROW_UPDATE_AT; 
				
	// index columnas
	static private final int INDEX_ID_EMPRESA = 1;
	static private final int INDEX_ID_NEGOCIO = 2;
	static private final int INDEX_NOMBRE_PRODUCTO = 3;
	static private final int INDEX_PRECIO_COSTO = 4;
	static private final int INDEX_PRECIO_VENTA = 5;
	static private final int INDEX_PRECIO_CANTIDAD = 6;
	static private final int INDEX_RECARGO = 7;
	static private final int INDEX_DETALLE = 8;
	static private final int INDEX_CREATED_AT = 9;
	static private final int INDEX_UPDATED_AT = 10;
	static private final int INDEX_LAST = 11;
	
	private final static  String[] ROW_NAME = {	ROW_ID_EMPRESA,
												ROW_ID_NEGOCIO,
												ROW_NOMBRE,
												ROW_PRECIO_VENTA,
												ROW_PRECIO_CANTIDAD,
												ROW_PRECIO_COSTO,
												ROW_RECARGO,
												ROW_DETALLE,
												ROW_CREATED_AT,
												ROW_UPDATE_AT
											  };
	
	
	public static String create() {
		
		String table = "CREATE TABLE IF NOT EXISTS "+ NAME_TABLE+"( "
							+ROW_ID_EMPRESA+" VARCHAR(255) NOT NULL, "
							+ROW_ID_NEGOCIO+" VARCHAR(255) NOT NULL, "
							+ROW_NOMBRE+" VARCHAR(255) NOT NULL," 
							+ROW_PRECIO_VENTA+" DOUBLE, "
							+ROW_PRECIO_CANTIDAD+" DOUBLE, " 
							+ROW_PRECIO_COSTO+" DOUBLE, "
							+ROW_RECARGO+" DOUBLE, "
							+ROW_DETALLE+" VARCHAR(255), "
							+ROW_CREATED_AT+" DATE NOT NULL,"
							+ROW_UPDATE_AT+" DATE NOT NULL,"
							+"PRIMARY KEY ("+ROW_ID_NEGOCIO+") "
						+ " );";	
		return table;
	}
	
	public static String drop() {
		return "drop table"+ NAME_TABLE; 
	}
	
	public static String addColunm(String name,String value, String retrictionl) {
		return "ALTER TABLE "+NAME_TABLE+"ADD "+name+" "+value+" "+retrictionl+";";
	} 
// ver aca, no ingresa datos		
	public static String insert() { // el ultimo le pone una coma capaz no ande 
		String insert = "INSERT INTO "+ NAME_TABLE +"( ";
		
		for (String row : ROW_NAME) 
			insert+= row.equals(ROW_LAST) ? row : row+", "; 
				
		insert+= " ) VALUES ( ";
		
		for (String row : ROW_NAME) 
			insert+= row.equals(ROW_LAST) ? " ? " : " ?, ";
		
		
		return insert + " );";
	}
	
	public static String update() {
		String update = "UPDATE "+NAME_TABLE+" SET ";
		
		for (String row : ROW_NAME)  
			update += row += row.equals(ROW_LAST) ? " = ? ": " = ?, " ;
				
		update +=" WHERE "+ROW_ID_NEGOCIO+" = ?;";
			
		return update;
	} 
	
	public static String delete() {return "DELETE FROM "+NAME_TABLE+" WHERE " +ROW_ID_NEGOCIO+" =? "; }
	
	public static String get_all() {return "SELECT* FROM "+NAME_TABLE; }
	
	public static String get_one() {return "SELECT* FROM "+NAME_TABLE+" WHERE "+ROW_ID_NEGOCIO+"=?"; }
	
	public static String getRowIdNegocio() {return ROW_ID_NEGOCIO;}

	public static String getRowIdEmpresa() {return ROW_ID_EMPRESA;}

	public static String getRowNombreProducto() {return ROW_NOMBRE;}

	public static String getRowPrecioCosto() {return ROW_PRECIO_COSTO;}

	public static String getRowPrecioVenta() {return ROW_PRECIO_VENTA;}

	public static String getRowPrecioCantidad() {return ROW_PRECIO_CANTIDAD;}

	public static String getRowRecargo() {return ROW_RECARGO;}

	public static int getIndexIdEmpresa() {return INDEX_ID_EMPRESA;}

	public static int getIndexIdNegocio() {return INDEX_ID_NEGOCIO;}

	public static int getIndexNombreProducto() {return INDEX_NOMBRE_PRODUCTO;}

	public static int getIndexPrecioCosto() {return INDEX_PRECIO_COSTO;}

	public static int getIndexPrecioVenta() {return INDEX_PRECIO_VENTA;}

	public static int getIndexPrecioCantidad() {return INDEX_PRECIO_CANTIDAD;}

	public static int getIndexRecargo() {return INDEX_RECARGO;}

	public static int getIndexLast() {return INDEX_LAST;}

	
	
	public static String getRowNombre() {
		return ROW_NOMBRE;
	}

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

	public static String[] getRowName() {return ROW_NAME;}

	public static String getNameTable() {return NAME_TABLE;	}


}
