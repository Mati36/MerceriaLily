package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import exeptions.AppExeption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Producto;

public class ListProductoController implements Serializable {

	private transient ObservableList<Producto> listProducto;
	
	
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
	
	public void save(File file) {
		 try {
	            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
	            oos.writeObject(new ArrayList<Producto>(listProducto));
	            oos.close();
	        } catch (IOException e) {
	            throw new AppExeption("Archivo dañado error al guardar "+e.getMessage());
	        }
			 
	}
	
	public void load(File file) {
		
		if (!file.exists()) {
			listProducto = FXCollections.observableArrayList();
			return;
		}
		
		try {
			ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath()));
            List<Producto> list = (List<Producto>) ois.readObject();
            listProducto = FXCollections.observableArrayList(list);
        }  catch (IOException | ClassNotFoundException e) {
        	throw new AppExeption("Archivo dañado error al leer "+e.getMessage());
        }
	}
	
}
