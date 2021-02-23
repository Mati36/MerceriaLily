package exeptions;

import java.io.IOException;

import models.DialogShow;

public class FileExeption extends IOException {
	
	public FileExeption(String content) {
		super(content);
		DialogShow.Error("Error Aplication", content);
	}
	
	public FileExeption(String title,String content) {
		super(content);
		DialogShow.Error(title, content);
	}
	
	
}
