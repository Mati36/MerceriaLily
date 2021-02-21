package exeptions;

import models.DialogShow;

public class ProductoExeption extends Exception {
	
	public ProductoExeption(String content) {
		super(content);
		DialogShow.Error("Error Producto", content);
	}
	
	public ProductoExeption(String title,String content) {
		super(content);
		DialogShow.Error(title, content);
	}
}
