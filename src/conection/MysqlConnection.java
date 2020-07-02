package conection;

import java.sql.*;
import Exeptions.SqlExeptionAlert;

public class MysqlConnection  implements IConnection{
	
	private  Connection connection;
	private String dbName = "mercerialili";
	private String dbUser = "root";
	private String dbPassword = "";
	private final String URL_HOSTING = "jdbc:mysql://localhost/"; 
//	private String dbUrl = URL_HOSTING+dbName+"?autoReconnect=true&useSSL=false";
	private String dbUrl = URL_HOSTING+dbName;
	
	public MysqlConnection() {	}
	
	public void runConnection(){ // no conecta a la bd 
		try {
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (SQLException e) {
			String msg = "Error al conectar en la base de datos = "+dbName+"\n User= "+dbUser+"\n Url= "+dbUrl+"\n mensaje: \n";
			new SqlExeptionAlert(msg+e.getMessage());
		}
		
	}

	@Override
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			String msg = "Error al cerrar la base de datos "+dbName+", mensaje \n";
			new SqlExeptionAlert(msg+e.getMessage());
		}
				
	}

	@Override
	public Connection getConnection() { return this.connection;	}

	@Override
	public final void setdbName(String dbName) { this.dbName = dbName; }

	
	@Override
	public String getdbName() { return this.dbName; }

	@Override
	public void setdbUser(String dbUser) {this.dbUser = dbUser;	}

	@Override
	public String getdbUser() { return this.dbUser; }

	@Override
	public void setdbPassword(String dbPassword) { this.dbPassword = dbPassword; }

	@Override
	public String getdbPassword() { return this.dbPassword; }

	@Override
	public void setdbUrl(String dbUrl) { this.dbUrl = dbUrl; }

	@Override
	public String getdbUrl() { return this.dbUrl;	}
	
	
}
