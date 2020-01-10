package conection;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnection {
	
	public void runConnection();
	public void closeConnection();
	public Connection getConnection();
	public void setNameBd(String nameBd);
	public String getNameBd();
	public void setUserBd(String UserBd);
	public String getUserBd();
	public void setUserPasswordBd(String userPasswordBd);
	public String getUserPasswordBd();
	public void seturlBd(String urlBd);
	public String geturlBd();
}
