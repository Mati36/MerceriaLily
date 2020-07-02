package Exeptions;

import javax.annotation.processing.FilerException;

import model.DialogShow;

public class ExelExeption extends FilerException{

	public ExelExeption(String error) {
		super(error);
		DialogShow.Error("Error Exel",error);
	}
	
	public ExelExeption	(String title,String content) {
		super(content);
		DialogShow.Error(title, content);
	}
	
}
