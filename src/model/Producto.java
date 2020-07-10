package model;

import java.time.LocalDate;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Producto {
	
	// 1 modelo del producto preguntar a mi viejo como representa el producto 
	// 2 tengo que hacer una funcion que cuando ingrese el precio de costo, automaticamente se actualize el precio de venta 
	// 2 nesecito un recargo y mostrarlo  para el paso anterior
	// 3 hacer un metodo para comprobar que el producto es correcto en todos sus campos 
	private int IVA = 21;
	private final SimpleStringProperty nombre;
	private final SimpleStringProperty idEmpresa;
	private final SimpleStringProperty idNegocio;
	private final SimpleDoubleProperty precioCosto;
	private final SimpleDoubleProperty precioCantidad;
	private final SimpleDoubleProperty precioVenta;
	private final SimpleDoubleProperty recargo;
	private final SimpleStringProperty detalle;
	private final ObjectProperty<LocalDate> createdAt;
	private final ObjectProperty<LocalDate> updatedAt;

	
	
	public Producto() {
		
		this.nombre = new SimpleStringProperty();
		this.idEmpresa = new SimpleStringProperty();
		this.idNegocio = new SimpleStringProperty();
		this.precioCosto = new SimpleDoubleProperty();
		this.precioCantidad = new SimpleDoubleProperty();
		this.precioVenta = new SimpleDoubleProperty();
		this.recargo = new SimpleDoubleProperty();
		this.detalle = new SimpleStringProperty();
		this.createdAt =  new SimpleObjectProperty<>(null);
		this.updatedAt = new SimpleObjectProperty<>(null);
		
		
	}
	
	public Producto( String nombre, String idEmpresa, String idNegocio, String detalle, Double precioCosto, Double precioVenta, Double precioCantidad, Double recargo, LocalDate createdAt) {
				
		this.nombre = new SimpleStringProperty(nombre);
		this.idEmpresa = new SimpleStringProperty(idEmpresa);
		this.idNegocio = new SimpleStringProperty(idNegocio);
		this.precioCosto = new SimpleDoubleProperty(precioCosto);
		this.precioCantidad = new SimpleDoubleProperty(precioCantidad);
		this.precioVenta = new SimpleDoubleProperty(precioVenta);
		this.recargo = new SimpleDoubleProperty(recargo);
		this.createdAt =  new SimpleObjectProperty<>(createdAt);
		this.updatedAt = new SimpleObjectProperty<>(createdAt);
		this.detalle = new SimpleStringProperty(detalle);
		
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
	public final void setDetalle(String value) { detalle.set(value);}
	public final String getDetalle() { return detalle.get();}
	
	public LocalDate getCreatedAt() { return createdAt.get();	}
	public void setCreatedAt(LocalDate value) { this.createdAt.set(value);}
	public LocalDate getUpdateAt() { return updatedAt.get(); }
	public void setUpdatedAt(LocalDate value) { this.updatedAt.set(value); }

	// propiedad
	public SimpleStringProperty getNombreProperty() { return nombre;}
	public SimpleStringProperty getIdEmpresaProperty() {return idEmpresa;}
	public SimpleStringProperty getIdNegocioProperty() {return idNegocio;}
	public SimpleDoubleProperty getPrecioCostoProperty() {return precioCosto;}
	public SimpleDoubleProperty getPrecioCantidadProperty() {return precioCantidad;}
	public SimpleDoubleProperty getPrecioVentaProperty() {return precioVenta;} 
	public SimpleDoubleProperty getRecargoProperty() {return recargo;}
	public SimpleStringProperty getDetalleProperty() { return detalle;	}
	public ObjectProperty<LocalDate> getCreatedAtProperty(){ return createdAt; }
	public ObjectProperty<LocalDate> getUpdatedAtProperty(){ return updatedAt; }

	public boolean isEmpty() {
		return  emptyString(this.getIdEmpresaProperty().get()) || emptyString(this.getIdNegocioProperty().get())
				|| emptyString(this.getNombreProperty().get());
		//		|| emptyDouble(this.getPrecioCostoProperty())
		//		|| emptyDouble(this.getPrecioCantidadProperty()) || emptyDouble(this.getPrecioVentaProperty())
	}
	
	private boolean emptyString(String string) {
		return string == null || string.length() <= 0;
	}
	
	private boolean emptyDouble(SimpleDoubleProperty number) {
		return number == null || number.get() < 0;
	}
	
	public Double calularPrecioVenta() {
		Double temp = calcularIva(getPrecioCosto());
		temp += (temp * recargo.get()) /100;
		return  temp; 
	}
	
	private Double calcularIva (Double precio) {
		return precio + (precio * IVA) / 100;
	}

	@Override
	public boolean equals(Object obj) {
		Producto producto = (Producto) obj;
		return producto.getIdNegocio() == this.getIdNegocio();
	}
	
}
