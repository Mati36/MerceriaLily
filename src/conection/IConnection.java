package conection;

import java.sql.Connection;

public interface IConnection {

	void closeConnection();

	Connection getConnection();
	
	public String getInstance(); 

	public String getDriverUrl(); 

	public String getHost();

	public String getDatabase();

	public String getUser();

	public String getPassword();

	public String getUrlConnection();

	public void setConnection(Connection connection);
	
}
