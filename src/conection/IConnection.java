package conection;

import java.sql.Connection;

public interface IConnection {

	void closeConnection();

	Connection getConnection();
	
	void setdbName(String dbName);
	String getdbName();

	void setdbUser(String dbUser);

	String getdbUser();

	void setdbPassword(String dbPassword);

	String getdbPassword();

	void setdbUrl(String dbUrl);

	String getdbUrl();


	
	
	
}
