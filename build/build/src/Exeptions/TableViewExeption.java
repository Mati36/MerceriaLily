package Exeptions;

import model.DialogShow;

public class TableViewExeption extends RuntimeException {
	
	public TableViewExeption(String content) {
		super(content);
		DialogShow.Error("Error en la tabla ", content);
	}
	
	public TableViewExeption(String title,String content) {
		super(content);
		DialogShow.Error(title, content);
	}
	
}
