package Exeptions;

import models.DialogShow;

public class AppExeption extends RuntimeException {
	
	public AppExeption(String content) {
		super(content);
		DialogShow.Error("Error Aplication", content);
	}
	
	public AppExeption(String title,String content) {
		super(content);
		DialogShow.Error(title, content);
	}
	
	
}
