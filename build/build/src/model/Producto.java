package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Producto {

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
	private boolean isIva;
	private final int DECIMAL_LIMIT = 2;
	private static final String DATE_FORMAT = "dd/MM/yy";
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
	public LocalDate getCreatedAt() { return  dateFormat(createdAt.get());	}
	public void setCreatedAt(LocalDate value) { this.createdAt.set(value);	}
	public LocalDate getUpdateAt() { return dateFormat(updatedAt.get()); }
	public void setUpdatedAt(LocalDate value) { this.updatedAt.set(value); }
	public void setIva(boolean isIva) { this.isIva = isIva;	}
	public boolean getIsIva() {return isIva;	}
	
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
		return   emptyString(this.getIdNegocioProperty().get())
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
	
	public Double precioVentaCantidad() {
		Double temp = getIsIva() ? calcularIva(getPrecioCosto()) : getPrecioCosto();
		temp = calcularRecargo(temp);
		
		return roundedNum(rondedDecimal(temp, DECIMAL_LIMIT)); 
	}
	
	public Double precioVentaPorUnidad(int cantidad) {
		
		if (cantidad > 1 ) {
			double precio = precioVentaCantidad() / cantidad;
			return roundedNum(rondedDecimal(precio, DECIMAL_LIMIT)); 
		
		}
		return precioVentaCantidad();
	}
	
	private Double calcularIva (Double precio) {
		return precio + (precio * IVA) / 100;
	}
	
	private Double calcularRecargo(Double precio) {
		return precio += ( precio * recargo.get()) /100;
	}
	
	public boolean isIva(Double num) {
		Double precio = calcularRecargo(calcularIva(getPrecioCosto()));
		return num != 0 && precio !=0
				&& ( num == roundedNum(rondedDecimal(precio,DECIMAL_LIMIT)) 
					|| num == rondedDecimal(precio, DECIMAL_LIMIT) 
					|| num == precio );
		
	}
	@Override
	public boolean equals(Object obj) {
		Producto producto = (Producto) obj;
		return producto.getIdNegocio() == this.getIdNegocio();
	}
	
	private double rondedDecimal(Double num,int decimalLimit) {
		return new BigDecimal(num).setScale(decimalLimit, RoundingMode.UP).doubleValue();
	}
	
	private double roundedNum(Double num) {
		return (double) Math.round(num);
	}
	
	public static LocalDate dateFormat(LocalDate date) {
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
			return LocalDate.parse(dateFormated(date), formatter);
			
		} catch (Exception e) {
			
			return date;
		}
		
			
	}
	
	public static String dateFormated(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return date.format(formatter);
	}
}
