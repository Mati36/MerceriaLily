package controllers;

import javafx.collections.ObservableList;
import models.Producto;

public class ListProductoController {

	private  ObservableList<Producto> listProducto;
	
	
	public ListProductoController(ObservableList<Producto> listProducto) {	
		this.listProducto = listProducto; 
	}
	
	public ListProductoController() {	}

	public void add(Producto prod) {
		if (listProducto != null) 
			listProducto.add(prod);
		
	}
	
	public void del(Producto prod) {
		if (listProducto != null) 
		listProducto.remove(prod);
	}
	
	public void edit() {
		
	}

	public ObservableList<Producto> getListProducto() {
		return listProducto;
	}

	public void setListProducto(ObservableList<Producto> listProducto) {
		this.listProducto = listProducto;
	}
	
	public void getSize() {
		listProducto.size();
	}
	
}
