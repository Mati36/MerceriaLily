package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import models.DialogShow;
import models.ExelFile;
import models.Producto;
import models.ProductoExel;

public class ListProductoController implements Serializable {

	private transient ObservableList<Producto> listProducto;
	
	
	public ListProductoController(ObservableList<Producto> listProducto) {	
		this.listProducto = listProducto; 
	}
	
	public ListProductoController() {	
		listProducto = FXCollections.observableArrayList();
	}

	public void add(Producto prod) {
		if (listProducto == null)
			return;
		else if (existItem(prod)) {
			new DialogShow("", "El producto con el codigo negocio: "+prod.getIdNegocio()+" ya se encuentra en la lista",AlertType.INFORMATION).show();
			return;
		}
		
		listProducto.add(prod);
		sort();
		
	}
	
	public void del(Producto prod) {
		if (listProducto == null) return; 
		
		listProducto.remove(prod);
		sort();
	}
	
	public void removItemsSelection(Collection<Producto> itemsSelection) {
		listProducto.removeAll(itemsSelection);
	}
	public void edit(String idNegocio,Producto prod) {
		if (!prod.getIdNegocio().equals(idNegocio)) sort();
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
	
	public void save(File file){
		if(file == null) return;
        ObjectOutputStream oos;
		try {
			if (file.getPath().endsWith(ExelFile.extensionExel)) { 
				importExel(file);
				
			}
			oos = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
			oos.writeObject(new ArrayList<Producto>(listProducto));
		    oos.close();
		} catch (IOException | InvalidFormatException e) {
			DialogShow.Error("Error fatal", "No se puede guardar el archivo "+e.getMessage());
		}
   	}
	
	public void load(File file){
		if(file == null || !file.exists()) return;
		
		try {
			if (file.getPath().endsWith(ExelFile.extensionExel)) 
				exportExel(file);
			
			ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath()));
			ArrayList<Producto> list;
			list = (ArrayList<Producto>) ois.readObject();
			addAllItems(list);
		} catch (ClassNotFoundException | IOException | InvalidFormatException e) {
			DialogShow.Error("Error fatal", "No se puede cargar el archivo \n"+e.getMessage());
		}
	}
	
	private Comparator<Producto> compartorProducto() {
		return new Comparator<Producto>() {

			@Override
		 public int compare(Producto o1, Producto o2) {
				return o1.compareTo(o2);
			}
		};
	}
	
	private boolean existItem(Producto producto) {
		for (Producto p : listProducto) {
			if (p.compareTo(producto) == 0)
				return true;
		}
		return false;
	}
	
	private void sort() {
		if (listProducto != null) 
			listProducto.sort(compartorProducto().reversed());
	}
	
	public void addAllItems(ArrayList<Producto> list) {
		if(!listProducto.isEmpty() && !list.isEmpty()) listProducto.clear();
		listProducto.addAll(list);
	}
	
	public void exportExel(File file) throws InvalidFormatException, FileNotFoundException, IOException {
		new ProductoExel().loadExel(this, file);
	}
	
	public void importExel(File file) throws InvalidFormatException, IOException {
		new ProductoExel().saveExel(this, file);
	}
}
