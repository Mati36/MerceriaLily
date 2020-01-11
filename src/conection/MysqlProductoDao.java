package conection;

import java.sql.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import app.Producto;
import controller.DialogAlert;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MysqlProductoDao  {

	final private String NAME_TABLE ="productos";
	// columnas string
	final private String COL_ID_NEGOCIO = "idNegocio";
	final private String COL_ID_EMPRESA = "idEmpresa";
	final private String COL_NOMBRE_PRODUCTO = "nombreProducto";
	final private String COL_PRECIO_COSTO = "precioCosto";
	final private String COL_PRECIO_VENTA = "precioVenta";
	final private String COL_RECARGO= "recargo";
	final private String COL_PRECIO_CANTIDAD = "precioCantidad";
	
	// index columnas
	final private int INDEX_ID_NEGOCIO = 1;
	final private int INDEX_ID_EMPRESA = 2;
	final private int INDEX_NOMBRE_PRODUCTO = 3;
	final private int INDEX_PRECIO_COSTO = 4;
	final private int INDEX_PRECIO_VENTA = 5;
	final private int INDEX_PRECIO_CANTIDAD = 6;
	final private int INDEX_RECARGO = 7;
	final private int INDEX_LAST = 8;
	
	final private String INSERT ="INSERT Into "+NAME_TABLE+" ( "+COL_ID_NEGOCIO+","+COL_ID_EMPRESA+","+COL_NOMBRE_PRODUCTO+","+COL_PRECIO_COSTO
												+","+COL_PRECIO_VENTA+","+COL_RECARGO+","+COL_PRECIO_CANTIDAD+" ) VALUES (?,?,?,?,?,?,?);"; 
	
	final private String UPDATE ="UPDATE "+NAME_TABLE+" SET "+COL_ID_NEGOCIO+"=?,"+COL_ID_EMPRESA+"=?,"+COL_NOMBRE_PRODUCTO+"=?,"+COL_PRECIO_COSTO
											+"=?,"+COL_PRECIO_VENTA+"=?,"+COL_RECARGO+"=?,"+COL_PRECIO_CANTIDAD+"=? WHERE " +COL_ID_NEGOCIO+"=?" ; 
	
	final private String DELETE = "DELETE FROM "+NAME_TABLE+" WHERE "+"idNegocio"+"=?";
	final private String GETALL = "SELECT* FROM "+NAME_TABLE;
	final private String GETONE = "SELECT* FROM "+NAME_TABLE+" WHERE "+COL_ID_NEGOCIO+"=?";
	
	final private MysqlConnection mysqlConnection = new MysqlConnection();
	private Connection connection;	
	
	public MysqlProductoDao() {		
	}

	public void insert(Producto producto) {
		PreparedStatement start = conectar(INSERT); 
		
		if (start != null) {
			try {
				productoToResultSEt(start, producto);
				if (start.executeUpdate() == 0)
					errorDialog("Error puede que no se actualizo ");
			}catch (SQLException e) { 
				errorDialog("Error al ingresar los datos a la base de datos, "+e.getMessage());
				//System.out.println("Error al ingresar los datos a la base de datos, "+e.getMessage());
			}
			
		} 
		close(start, connection);		
	}
		

	public void update(Producto producto, String idNegocio) {
		PreparedStatement start = conectar(UPDATE);
		// para probar que los valores son distintos
		try {
			productoToResultSEt(start, producto);
			start.setString(INDEX_LAST,idNegocio); // busca en la Bd, para que funcione, el ultimo dato es el que usamos para seleccionar el elemento en la base de datos
			
			if (start.executeUpdate() == 0) 
				errorDialog("Error puede que no se actualizo");
				//System.out.println("Error puede que no se actualizo");
			
		} catch (SQLException e) {
			errorDialog("Error al ingresar los datos a la base de datos, "+e.getMessage());
			System.out.println("Error al ingresar los datos a la base de datos, "+e.getMessage());
		}
		
		close(start, connection);
	}
	
	public void delete(Producto producto)  {
		PreparedStatement start = conectar(DELETE);
		try {
			start.setString(INDEX_ID_NEGOCIO, producto.getIdNegocio());
			if (start.executeUpdate() == 0) 
				errorDialog("Error al ejecutar delete sql, no se puedo eliminar, "+producto.getNombre());
			
		} catch (SQLException e) {
			errorDialog("Error al eliminar en base de datos,"+e.getMessage());
		}
		
		close(start, connection);
	}
	
	public Producto getProductoSql(String idNegocio) {
		PreparedStatement start = conectar(GETONE);
		ResultSet result = null;
		Producto producto = null;
		if (start != null) {
			try {
				start.setString(INDEX_ID_NEGOCIO, idNegocio);
				result = start.executeQuery();
				if (result.next())
					producto = resultSetToProducto(result);
				else 
					errorDialog("Error codigo incorrecto, vuelva a intentarlo");
					//System.out.println("Error codigo incorrecto");
			
			} catch (SQLException e) {
				errorDialog("Error al obtener datos");
				System.out.println("Error al obtener datos");
			}
			
		}
		close(start, connection);
		return producto;
	}
		
	public void mostrarProductoTabla(ObservableList<Producto> table) {
		table.clear(); // limpia la tabla (la tabla esta en main)
		PreparedStatement start = conectar(GETALL);
		ResultSet resultSet = null;
		if (start != null) {
			try {
				resultSet = start.executeQuery();
				while (resultSet.next()) 
					llenarTabla(resultSet,table); 
				
			} catch (SQLException e) {
				errorDialog("Error al ejecutar el resultSet "+e.getMessage());
				System.out.println("Error al ejecutar el resultSet "+e.getMessage());
			}
			
		}
			
		close(start, connection);
	}

	private void llenarTabla(ResultSet resultSet,ObservableList<Producto> table) {
		try {
			table.add(resultSetToProducto(resultSet)); // agrega los productos a la tabla
		} catch (SQLException e) {
			errorDialog("Error al llenar el producto "+e.getMessage());
			System.out.println("Error al llenar el producto "+e.getMessage());
		}
		
	}
	
	private void productoToResultSEt(PreparedStatement start,Producto producto) throws SQLException {
		start.setString(INDEX_ID_NEGOCIO,producto.getIdNegocio());
		start.setString(INDEX_ID_EMPRESA,producto.getIdEmpresa());
		start.setString(INDEX_NOMBRE_PRODUCTO,producto.getNombre());
		start.setDouble(INDEX_PRECIO_COSTO,producto.getPrecioCosto());
		start.setDouble(INDEX_PRECIO_VENTA,producto.getPrecioVenta());
		start.setDouble(INDEX_RECARGO,producto.getRecargo()); 
		start.setDouble(INDEX_PRECIO_CANTIDAD,producto.getPrecioCantidad()); 
	}
	
	private Producto resultSetToProducto(ResultSet rs) throws SQLException {
		Producto prod  = new Producto();
		prod.setIdEmpresa(rs.getString(INDEX_ID_NEGOCIO));
		prod.setIdNegocio(rs.getString(INDEX_ID_EMPRESA));
		prod.setNombre(rs.getString(INDEX_NOMBRE_PRODUCTO));
		prod.setPrecioCosto(rs.getDouble(INDEX_PRECIO_COSTO));
		prod.setPrecioVenta(rs.getDouble(INDEX_PRECIO_VENTA));
		prod.setPrecioCantidad(rs.getDouble(INDEX_PRECIO_CANTIDAD));
		prod.setRecargo(rs.getDouble(INDEX_RECARGO));
		return prod;
	}
	
	private PreparedStatement conectar(String action) {
		mysqlConnection.runConnection();
		connection = mysqlConnection.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(action);
		} catch (SQLException e) {
			errorDialog("Error al ingresar los datos a la base de datos, "+e.getMessage());
//			System.out.println("Error a conectar a la base de tatos "+e.getMessage());
		}
		return  preparedStatement;
	}
	
	public void close(PreparedStatement preparedStatement, Connection connection) {
		try {
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			errorDialog("Error al cerrar base de datos "+e.getMessage());
			//System.out.println("Error al cerrar base de datos "+e.getMessage());			
		}
		
	}

	// cargar la hoja
	public void mySqlToExelLoad(Sheet sheet) throws SQLException {
		
		PreparedStatement start = conectar(GETALL);
		ResultSet resultSet = start.executeQuery();
		if (resultSet != null) {
			
			for (int i = 1; resultSet.next(); i++)
				try {
					cargarExelHoja(sheet,i,resultSetToProducto(resultSet));
				} catch (SQLException e) {
					errorDialog("Error al guardar Archivo, "+e.getMessage());
					
				}
		}
		else
			throw new SQLException("Error resultSet ");
		
	}
	
	private void cargarExelHoja(Sheet sheet, int i, Producto producto) {
		Row row = sheet.createRow(i);
		// el codigo de negocio seria el codigo empresa 
		row.createCell(INDEX_ID_NEGOCIO - 1).setCellValue(producto.getIdEmpresa());
		row.createCell(INDEX_ID_EMPRESA - 1).setCellValue(producto.getIdNegocio());
		row.createCell(INDEX_NOMBRE_PRODUCTO  - 1).setCellValue(producto.getNombre());
		row.createCell(INDEX_PRECIO_COSTO - 1).setCellValue(producto.getPrecioCosto());
		row.createCell(INDEX_PRECIO_VENTA - 1).setCellValue(producto.getPrecioVenta());
		row.createCell(INDEX_PRECIO_CANTIDAD - 1).setCellValue(producto.getPrecioCantidad());
		
	}

	// metodo de test 
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
	
	private boolean errorDialog(String content) {
		DialogAlert dialogAlert = new DialogAlert(content, "Error MySql", new Alert(AlertType.ERROR));
		return dialogAlert.getResultOption();
	}
	
	private void errorConectar() {
		errorDialog("Error al Conectar en la base de datos, "+mysqlConnection.getNameBd());
	}
	
}
