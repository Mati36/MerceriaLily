package conection;


import java.sql.*;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import Exeptions.ExelExeption;
import Exeptions.SqlExeptionAlert;
import javafx.collections.ObservableList;
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
			new SqlExeptionAlert("Error al crear tabla "+ProductoTableSql.getNameTable()+" \n msg: " + 
									e.getMessage()+"\n sql: "+ProductoTableSql.create());
			
		}		
		
		close(preparedStatement, connection);
	}
	
	private void dropTable() {
		PreparedStatement preparedStatement = conectar(ProductoTableSql.drop());
		try {
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			new SqlExeptionAlert("Error al borra la tabla "+ProductoTableSql.getNameTable()+" \n" + e.getMessage());
			
		}		
		
		close(preparedStatement, connection);
	} 
	
	public void insert(Producto producto) {
		PreparedStatement start = conectar(ProductoTableSql.insert()); 
		
		if (start != null) {
			try {
				setProductoSql(start, producto);
				if (start.executeUpdate() == 0)
					new SqlExeptionAlert("No se pudo ingresar el producto "+producto.getNombre()+
							" codigo de negocio "+producto.getIdNegocio()+" en la base de datos "+ ProductoTableSql.getNameTable()+"\n");
					
			}catch (SQLException e) { 
				new SqlExeptionAlert("Fatal: No se pudo ingresar el producto "+producto.getNombre()+
					" codigo de negocio "+producto.getIdNegocio()+" en la base de datos "+ ProductoTableSql.getNameTable()+"\n"+
						e.getMessage()+"\n Consulta:"+ ProductoTableSql.insert());
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
				new SqlExeptionAlert("No se puede actualizar el producto "+producto.getNombre()+
						" codigo de negocio "+producto.getIdNegocio()+" en la base de datos "+ ProductoTableSql.getNameTable());
										
		} catch (SQLException e) {
			new SqlExeptionAlert("Fatal: Imposible no se pudo actualizar el producto "+producto.getNombre()+
				" codigo de negocio "+producto.getIdNegocio()+" de la base de datos "+ ProductoTableSql.getNameTable()+"\n"+e.getMessage() 
				+"\n Consulta:"+ ProductoTableSql.update());
		}
		
		close(start, connection);
	}
	
	public void delete(Producto producto)  {
		PreparedStatement start = conectar(ProductoTableSql.delete());
		try {
			start.setString(1, producto.getIdNegocio());
			if (start.executeUpdate() == 0) 
				new SqlExeptionAlert("No se encuentra el producto "+producto.getNombre()+
						" codigo de negocio "+producto.getIdNegocio()+" en la base de datos "+ProductoTableSql.getNameTable());
					
		} catch (SQLException e) {
			new SqlExeptionAlert("Fatal: Imposible obtener el producto "+producto.getNombre()+
				" codigo de negocio "+producto.getIdNegocio()+" de la base de datos "+ 
					ProductoTableSql.getNameTable()+"\n"+e.getMessage()+"\n Consulta:"+ ProductoTableSql.delete());
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
					new SqlExeptionAlert("El producto con el codigo de negocio "+idNegocio+" no fue encontrado");
					
			} catch (SQLException e) {
				String productName = producto != null ? producto.getNombre() :"-";
				new SqlExeptionAlert("No se pudo obtener el producto "+ productName+
										" codigo de negocio "+idNegocio+"\n "+e.getMessage());
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
			if (getProductoSql(producto.getIdNegocio()) == null) 
				insert(producto);
		
		}
	}
	
	private void setProductoSql(PreparedStatement start,Producto producto) throws SQLException {
		start.setString(ProductoTableSql.getIndexIdEmpresa(), producto.getIdEmpresa());
		start.setString(ProductoTableSql.getIndexIdNegocio(), producto.getIdNegocio());
		start.setString(ProductoTableSql.getIndexNombreProducto(), producto.getNombre());
		start.setDouble(ProductoTableSql.getIndexPrecioCosto(), producto.getPrecioCosto());
		start.setDouble(ProductoTableSql.getIndexPrecioVenta(), producto.getPrecioVenta());
		start.setDouble(ProductoTableSql.getIndexRecargo(), producto.getRecargo()); 
		start.setDouble(ProductoTableSql.getIndexPrecioCantidad(), producto.getPrecioCantidad());
		start.setString(ProductoTableSql.getIndexDetalle(), producto.getDetalle());
		start.setDate(ProductoTableSql.getIndexCreatedAt(), Date.valueOf(producto.getCreatedAt()));
		start.setDate(ProductoTableSql.getIndexUpdatedAt(), Date.valueOf(producto.getUpdateAt()));
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
		prod.setDetalle(rs.getString(ProductoTableSql.getIndexDetalle()));
		prod.setCreatedAt(rs.getDate(ProductoTableSql.getIndexCreatedAt()).toLocalDate());
		prod.setUpdatedAt(rs.getDate(ProductoTableSql.getIndexUpdatedAt()).toLocalDate());
		return prod;
	}
	
	private PreparedStatement conectar(String action) {
		mysqlConnection.runConnection();
		connection = mysqlConnection.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(action);
		} catch (SQLException e) {
			new SqlExeptionAlert("Error al ingresar en la base de datos" + ProductoTableSql.getNameTable() + "\n" + e.getMessage());
		}
		return  preparedStatement;
	}
	
	public void close(PreparedStatement preparedStatement, Connection connection) {
		try {
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			new SqlExeptionAlert("Error al cerrar la base de datos" + ProductoTableSql.getNameTable() +"\n" + e.getMessage());
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
					new ExelExeption("NO se pudo guardar la base de datos en archivo exel");
				}
		}
		else
			throw new SQLException("No se puede obtener todos los productos");
		
	}
	
	private void cargarExelHoja(Sheet sheet, int i, Producto producto) {
		Row row = sheet.createRow(i);
		// el codigo de negocio seria el codigo empresa 
		row.createCell(0).setCellValue(producto.getIdEmpresa());
		row.createCell(1).setCellValue(producto.getIdNegocio());
		row.createCell(2).setCellValue(producto.getNombre());
		row.createCell(3).setCellValue(producto.getPrecioVenta());
		row.createCell(4).setCellValue(producto.getPrecioCantidad());
		row.createCell(5).setCellValue(producto.getDetalle());
		row.createCell(6).setCellValue(producto.getCreatedAt());
		row.createCell(7).setCellValue(producto.getUpdateAt());
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

	
}
