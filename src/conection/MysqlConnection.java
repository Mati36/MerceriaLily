package conection;

import java.sql.*;
import Exeptions.SqlExeptionAlert;

public class MysqlConnection  implements IConnection{

	private  Connection connection;
	private final String instance = "mercerialili"; 
	private final String driverUrl = "jdbc:mysql://";
	private final String host = "35.223.91.98/";
	private final String database = "productos";
	private final String user = "lopezmatias36";
	private final String password = "RiverGallardo10";
		
	private final String urlConnection = driverUrl+host+database+"?user="+user+"&password="+password;
	
	public MysqlConnection() {	}
	
	
	public void runConnection(){ // no conecta a la bd 
		try {
			
			connection = DriverManager.getConnection(urlConnection);

		} catch (SQLException e) {
			String msg = "Error al conectar en la base de datos = "+database+" \n\n mensaje: ";
			new SqlExeptionAlert(msg+e.getMessage());
		}
		
	}

	@Override
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			String msg = "Error al cerrar la base de datos "+database+", mensaje \n";
			new SqlExeptionAlert(msg+e.getMessage());
		}
				
	}

	@Override
	public Connection getConnection() { return this.connection;	}


	@Override
	public String getInstance() {return instance;	}

	@Override
	public String getDriverUrl() {return driverUrl;	}

	@Override
	public String getHost() {return host;	}

	@Override
	public String getDatabase() {return database;	}

	@Override
	public String getUser() {return user;	}

	@Override
	public String getPassword() {return password;	}

	@Override
	public String getUrlConnection() {return urlConnection;	}

	@Override
	public void setConnection(Connection connection) {this.connection = connection;	}
	
}
