package conection;

import java.sql.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.DialogAlert;
import model.Producto;
import model.ProductoTableSql;


public class MysqlProductoDao   {

	
	final private MysqlConnection mysqlConnection = new MysqlConnection();
	private Connection connection;	
	
	public MysqlProductoDao() {		
		createTable();
	}
	
	// solucionar
	private void createTable() {
		PreparedStatement preparedStatement = conectar(ProductoTableSql.create());
		try {
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			errorDialog("Error al crear tabla "+e.getMessage());
		}		
		
		close(preparedStatement, connection);
	}
	
	private void dropTable() {
		PreparedStatement preparedStatement = conectar(ProductoTableSql.drop());
		try {
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			errorDialog("Error al crear tabla "+e.getMessage());
		}		
		
		close(preparedStatement, connection);
	} 
	
	public void insert(Producto producto) {
		PreparedStatement start = conectar(ProductoTableSql.insert()); 
		
		if (start != null) {
			try {
				setProductoSql(start, producto);
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
		PreparedStatement start = conectar(ProductoTableSql.update());
		// para probar que los valores son distintos
		try {
			setProductoSql(start, producto);
			start.setString(ProductoTableSql.getIndexLast(),idNegocio); // busca en la Bd, para que funcione, el ultimo dato es el que usamos para seleccionar el elemento en la base de datos
			
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
		PreparedStatement start = conectar(ProductoTableSql.delete());
		try {
			start.setString(1, producto.getIdNegocio());
			if (start.executeUpdate() == 0) 
				errorDialog("Error al ejecutar delete sql, no se puedo eliminar, "+producto.getNombre());
			
		} catch (SQLException e) {
			errorDialog("Error al eliminar en base de datos,"+e.getMessage());
		}
		
		close(start, connection);
	}
	
	public Producto getProductoSql(String idNegocio) {
		PreparedStatement start = conectar(ProductoTableSql.get_one());
		ResultSet result = null;
		Producto producto = null;
		if (start != null) {
			try {
				start.setString(ProductoTableSql.getIndexIdNegocio(), idNegocio);
				result = start.executeQuery();
				if (result.next())
					producto = getProductoSql(result);
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
		
	public void mostrarProductoTabla(ObservableList<Producto> table) throws SQLException {
		
		table.clear(); // limpia la tabla (la tabla esta en main)
		PreparedStatement start = conectar(ProductoTableSql.get_all());
		ResultSet resultSet = null;
		if (start != null) {
			resultSet = start.executeQuery();
			while (resultSet.next()) 
				table.add(getProductoSql(resultSet)); 
		}
			
		close(start, connection);
	}

	public void savedSql(ObservableList<Producto> table) {
		
		for (Producto producto : table) {
			if (getProductoSql(producto.getIdNegocio()) == null) {
				insert(producto);
			}
			
		}
	}
	
	private void setProductoSql(PreparedStatement start,Producto producto) throws SQLException {
		start.setString(ProductoTableSql.getIndexIdEmpresa(),producto.getIdEmpresa());
		start.setString(ProductoTableSql.getIndexIdNegocio(),producto.getIdNegocio());
		start.setString(ProductoTableSql.getIndexNombreProducto(),producto.getNombre());
		start.setDouble(ProductoTableSql.getIndexPrecioCosto(),producto.getPrecioCosto());
		start.setDouble(ProductoTableSql.getIndexPrecioVenta(),producto.getPrecioVenta());
		start.setDouble(ProductoTableSql.getIndexRecargo(),producto.getRecargo()); 
		start.setDouble(ProductoTableSql.getIndexPrecioCantidad(),producto.getPrecioCantidad()); 
	}
	
	private Producto getProductoSql(ResultSet rs) throws SQLException {
		Producto prod  = new Producto();
		prod.setIdEmpresa(rs.getString( ProductoTableSql.getIndexIdEmpresa() ));
		prod.setIdNegocio(rs.getString(ProductoTableSql.getIndexIdNegocio()));
		prod.setNombre(rs.getString(ProductoTableSql.getIndexNombreProducto()));
		prod.setPrecioCosto(rs.getDouble(ProductoTableSql.getIndexPrecioCosto()));
		prod.setPrecioVenta(rs.getDouble(ProductoTableSql.getIndexPrecioVenta()));
		prod.setPrecioCantidad(rs.getDouble(ProductoTableSql.getIndexPrecioCantidad()));
		prod.setRecargo(rs.getDouble(ProductoTableSql.getIndexRecargo()));
		return prod;
	}
	
	private PreparedStatement conectar(String action) {
		mysqlConnection.runConnection();
		connection = mysqlConnection.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(action);
		} catch (SQLException e) {
			errorDialog("Error al ingresar a la base de dato, "+e.getMessage());
//			System.out.println("Error a conectar a la base de tatos "+e.getMessage());
		}
		return  preparedStatement;
	}
	
	public void close(PreparedStatement preparedStatement, Connection connection) {
		try {
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			errorDialog("Error al cerrar base de dato "+e.getMessage());
			//System.out.println("Error al cerrar base de datos "+e.getMessage());			
		}
		
	}

	// cargar la hoja
	public void mySqlToExelSave(Sheet sheet) throws SQLException {
		
		PreparedStatement start = conectar(ProductoTableSql.get_all());
		ResultSet resultSet = start.executeQuery();
		if (resultSet != null) {
			
			for (int i = 1; resultSet.next(); i++)
				try {
					cargarExelHoja(sheet,i,getProductoSql(resultSet));
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
		row.createCell(0).setCellValue(producto.getIdEmpresa());
		row.createCell(1).setCellValue(producto.getIdNegocio());
		row.createCell(2).setCellValue(producto.getNombre());
		row.createCell(3).setCellValue(producto.getPrecioVenta());
		row.createCell(4).setCellValue(producto.getPrecioCantidad());
		
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
		errorDialog("Error al Conectar en la base de datos, "+mysqlConnection.getdbName());
	}

	
	
}
