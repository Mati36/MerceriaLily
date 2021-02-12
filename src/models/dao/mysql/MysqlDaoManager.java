package models.dao.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import models.dao.IDaoManager;
import models.dao.IProductoDao;

public class MysqlDaoManager implements IDaoManager {

	private Connection connection;
	IProductoDao productoDao = null;
		
	public MysqlDaoManager(String host, String user, String password, String database) throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database,user,password); 
	}
	
	@Override
	public IProductoDao getProductoDao() {
		
		if (productoDao == null) 
			productoDao = new MysqlProductoDao(connection);
		
		return productoDao;
	}
	
}
