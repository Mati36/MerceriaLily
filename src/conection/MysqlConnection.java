package conection;

import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.DialogAlert;

public class MysqlConnection  implements IConnection{
	
	private  Connection connection;
	private String dbName = "mercerialili";
	private String dbUser = "root";
	private String dbPassword = "";
	private final String URL_HOSTING = "jdbc:mysql://localhost/"; 
//	private String dbUrl = URL_HOSTING+dbName+"?autoReconnect=true&useSSL=false";
	private String dbUrl = URL_HOSTING+dbName;
	
	public MysqlConnection() {	}
	
	public void runConnection() { // no conecta a la bd 
		try {
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (SQLException e) {
			errorDialog("Error al conectar en la base de datos = "+dbName+"\n User= "+dbUser+"\n Url= "+dbUrl+"\n mensaje: "+e.getMessage());
			
		}
		
	}

	@Override
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			errorDialog("Error al cerrar la base de datos "+dbName+", mensaje "+e.getMessage());
			//System.out.println("Error al cerrar la base de datos"+dbName);
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

	// por ahora no sirve 
	@SuppressWarnings("unused")
//	private void createDataBase() throws SQLException {
//		System.out.println("en sql");
//		runConnection();
//		
//		String creteTable = "CREATE DATABASE IF NOT EXISTS "+dbName+";";
//		PreparedStatement preparedStatement = getConnection().prepareStatement(creteTable);
//		preparedStatement.executeUpdate();
//		preparedStatement.close();
//		closeConnection();
//	}
	
	private boolean errorDialog(String content,String titel) {
		DialogAlert dialogAlert = new DialogAlert(content, titel, new Alert(AlertType.ERROR));
		return dialogAlert.getResultOption();
	}
	
	private boolean errorDialog(String content) {
		DialogAlert dialogAlert = new DialogAlert(content, "Error de MySql", new Alert(AlertType.ERROR));
		return dialogAlert.getResultOption();
	}
	
}
