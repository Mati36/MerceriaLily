package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Producto {
	
	// 1 modelo del producto preguntar a mi viejo como representa el producto 
	// 2 tengo que hacer una funcion que cuando ingrese el precio de costo, automaticamente se actualize el precio de venta 
	// 2 nesecito un recargo y mostrarlo  para el paso anterior
	// 3 hacer un metodo para comprobar que el producto es correcto en todos sus campos 
	private final SimpleStringProperty nombre;
	private final SimpleStringProperty idEmpresa;
	private final SimpleStringProperty idNegocio;
	private final SimpleDoubleProperty precioCosto;
	private final SimpleDoubleProperty precioCantidad;
	private final SimpleDoubleProperty precioVenta;
	private final SimpleDoubleProperty recargo;
	
	public Producto() {
		this.nombre = new SimpleStringProperty();
		this.idEmpresa = new SimpleStringProperty();
		this.idNegocio = new SimpleStringProperty();
		this.precioCosto = new SimpleDoubleProperty();
		this.precioCantidad = new SimpleDoubleProperty();
		this.precioVenta = new SimpleDoubleProperty();
		this.recargo = new SimpleDoubleProperty();
	}
	
	public Producto( String nombre, String idEmpresa, String idNegocio, Double precioCosto, Double precioVenta, Double precioCantidad, Double recargo) {
		
		this.nombre = new SimpleStringProperty(nombre);
		this.idEmpresa = new SimpleStringProperty(idEmpresa);
		this.idNegocio = new SimpleStringProperty(idNegocio);
		this.precioCosto = new SimpleDoubleProperty(precioCosto);
		this.precioCantidad = new SimpleDoubleProperty(precioCantidad);
		this.precioVenta = new SimpleDoubleProperty(precioVenta);
		this.recargo = new SimpleDoubleProperty(recargo);	
	}	
	
	
	public final void setNombre(String value) { nombre.set(value);}
	public final String getNombre() { return nombre.get();}
	public final void setIdEmpresa(String value) { idEmpresa.set(value);}
	public final String getIdEmpresa() { return idEmpresa.get();}
	public final void setIdNegocio(String value) {idNegocio.set(value); }
	public final String getIdNegocio() {return idNegocio.get();}
	public final void setPrecioCosto(Double value) { precioCosto.set(value);}
	public final Double getPrecioCosto() {return precioCosto.get();}
	public final void setPrecioCantidad( Double value) { precioCantidad.set(value);}
	public final Double getPrecioCantidad() {return precioCantidad.get();}
	public final void setPrecioVenta(Double value) { precioVenta.set(value);}
	public final Double getPrecioVenta() {return precioVenta.get();}
	public final void setRecargo(Double value) { recargo.set(value);}
	public final Double getRecargo() {return recargo.get();}
	
	// propiedad
	public SimpleStringProperty getNombreProperty() { return nombre;}
	public SimpleStringProperty getIdEmpresaProperty() {return idEmpresa;}
	public SimpleStringProperty getIdNegocioProperty() {return idNegocio;}
	public SimpleDoubleProperty getPrecioCostoProperty() {return precioCosto;}
	public SimpleDoubleProperty getPrecioCantidadProperty() {return precioCantidad;}
	public SimpleDoubleProperty getPrecioVentaProperty() {return precioVenta;} 
	public SimpleDoubleProperty getRecargoProperty() {return recargo;}

	public boolean isEmpty() {
		return  emptyStringProperty(this.getIdEmpresaProperty().get()) || emptyStringProperty(this.getIdNegocioProperty().get()) 
				|| emptyStringProperty(this.getNombreProperty().get()) || emptyDoubleProperty(this.getPrecioCostoProperty())
				|| emptyDoubleProperty(this.getPrecioCantidadProperty()) || emptyDoubleProperty(this.getPrecioVentaProperty());
	}
	
	private boolean emptyStringProperty(String string) {
		return string == null || string.length() <= 0;
	}
	
	private boolean emptyDoubleProperty(SimpleDoubleProperty number) {
		return number == null || number.get() < 0;
	}
	
	public Double calularPrecioVenta() {
		Double temp = (precioCosto.get() * recargo.get()) / 100; 
		
		return  temp+precioCosto.get(); 
	}

	@Override
	public boolean equals(Object obj) {
		Producto producto = (Producto) obj;
		return producto.getIdNegocio() == this.getIdNegocio();
	}
	
}
