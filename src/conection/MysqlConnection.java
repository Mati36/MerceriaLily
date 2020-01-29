package conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.DialogAlert;

public class MysqlConnection  implements IConnection{
	
	private Connection connection;
	private String nameBd = "mercerialili";
	private String userBd = "root";
	private String userPasswordBd ="";
	private String urlBd="jdbc:mysql://localhost:3308/"+nameBd+"?autoReconnect=true&useSSL=false";
	
	
	
	public MysqlConnection(Connection connection, String nameBd, String userBd, String userPasswordBd, String urlBd) {
		this.connection = connection;
		this.nameBd = nameBd;
		this.userBd = userBd;
		this.userPasswordBd = userPasswordBd;
		this.urlBd = urlBd;
	}

	public MysqlConnection() {}
	

	public void runConnection() {
		try {
			connection = DriverManager.getConnection(urlBd,userBd,userPasswordBd);
		} catch (SQLException e) {
			errorDialog("Error al conectar en la base de datos "+nameBd+", mensaje "+e.getMessage());
			//System.out.println("Error al conectar en la base de datos "+nameBd+", mensaje "+e.getMessage());
		}
		
	}

	@Override
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			errorDialog("Error al cerrar la base de datos "+nameBd+", mensaje "+e.getMessage());
			//System.out.println("Error al cerrar la base de datos"+nameBd);
		}
				
	}

	@Override
	public Connection getConnection() {
		
		return this.connection;
	}

	@Override
	public void setNameBd(String nameBd) {
		this.nameBd = nameBd;
		
	}

	@Override
	public String getNameBd() {
		
		return this.nameBd;
	}

	@Override
	public void setUserBd(String userBd) {
		this.userBd = userBd;
		
	}

	@Override
	public String getUserBd() {
		
		return this.userBd;
	}

	@Override
	public void setUserPasswordBd(String userPasswordBd) {
		this.userPasswordBd = userPasswordBd;
		
	}

	@Override
	public String getUserPasswordBd() {
		
		return this.userPasswordBd;
	}

	@Override
	public void seturlBd(String urlBd) {
		this.urlBd = urlBd;
		
	}

	@Override
	public String geturlBd() {

		return this.urlBd;
	}

	private boolean errorDialog(String content,String titel) {
		DialogAlert dialogAlert = new DialogAlert(content, titel, new Alert(AlertType.ERROR));
		return dialogAlert.getResultOption();
	}
	private boolean errorDialog(String content) {
		DialogAlert dialogAlert = new DialogAlert(content, "Error de MySql", new Alert(AlertType.ERROR));
		return dialogAlert.getResultOption();
	}
	
}
