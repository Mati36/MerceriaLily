package models.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import models.Producto;
import models.dao.IProductoDao;

public class MysqlProductoDao implements IProductoDao{

	private Connection connection;
	
	public MysqlProductoDao(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Producto producto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Producto producto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mod(Producto producto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ObservableList<Producto> getList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Producto getItem(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void closeConnection() throws SQLException {
		if (connection != null) 
			connection.close();
	}
}
