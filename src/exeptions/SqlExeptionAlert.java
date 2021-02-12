package exeptions;

import java.sql.SQLException;

import models.DialogShow;

public class SqlExeptionAlert extends SQLException{
	
	
	public SqlExeptionAlert(String exeption) {
		super(exeption);
		DialogShow.Error("Error Sql",exeption);
	}
	
	public SqlExeptionAlert(String title,String content) {
		super(content);
		DialogShow.Error(title, content);
	}

}
