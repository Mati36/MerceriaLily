package models;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Producto implements Externalizable,Comparable<Producto>  {

	private transient int IVA = 21;
	private final transient SimpleStringProperty nombre;
	private final transient SimpleStringProperty idEmpresa;
	private final transient SimpleStringProperty idNegocio;
	private final transient SimpleDoubleProperty precioCosto;
	private final transient SimpleDoubleProperty precioCantidad;
	private final transient SimpleDoubleProperty precioVenta;
	private final transient SimpleDoubleProperty recargo;
	private final transient SimpleStringProperty detalle;
	private final transient ObjectProperty<LocalDate> createdAt;
	private final transient ObjectProperty<LocalDate> updatedAt;
	private transient boolean isIva;
	private transient final int DECIMAL_LIMIT = 2;
	private transient static final String DATE_FORMAT = "dd/MM/yy";
	
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
	public final String getNombre() { return stringNullFormat(nombre); }
	public final void setIdEmpresa(String value) { idEmpresa.set(value);}
	
	public final String getIdEmpresa() { return stringNullFormat(idEmpresa); }
	public final void setIdNegocio(String value) {idNegocio.set(value); }
	public final String getIdNegocio() {return stringNullFormat(idNegocio);}
	
	public final void setPrecioCosto(Double value) { precioCosto.set(value);}
	public final Double getPrecioCosto() {return precioCosto.get();}
	
	public final void setPrecioCantidad( Double value) { precioCantidad.set(value);}
	public final Double getPrecioCantidad() {return precioCantidad.get();}
	
	public final void setPrecioVenta(Double value) { precioVenta.set(value);}
	public final Double getPrecioVenta() {return precioVenta.get();}
	
	public final void setRecargo(Double value) { recargo.set(value);}
	public final Double getRecargo() {return recargo.get();}
	
	public final void setDetalle(String value) { detalle.set(value);}
	public final String getDetalle() { return stringNullFormat(detalle);}
	
	public LocalDate getCreatedAt() { return  dateFormat(createdAt.get());	}
	public void setCreatedAt(LocalDate value) { this.createdAt.set(value);	}
	public void updateCreatedAt() { this.createdAt.set(LocalDate.now());	}
	
	public LocalDate getUpdateAt() { return dateFormat(updatedAt.get()); }
	public void setUpdatedAt(LocalDate value) { this.updatedAt.set(value); }
	public void UpdatedAt() { this.updatedAt.set(LocalDate.now()); }
	
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
	
	}
	
	private boolean emptyString(String string) {
		return string == null || string.length() <= 0;
	}
	
	private String stringNullFormat(SimpleStringProperty value) {
		return value != null ? value.getValueSafe() : "";
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
		
	private double rondedDecimal(Double num,int decimalLimit) {
		return new BigDecimal(num).setScale(decimalLimit, RoundingMode.UP).doubleValue();
	}
	
	private double roundedNum(Double num) {
		return (double) Math.round(num);
	}
	
	public static LocalDate dateFormat(LocalDate date) { // creo no hace falta
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
			return LocalDate.parse(dateFormated(date), formatter);
			
		} catch (Exception e) {
			
			return date;
		}
				
	}
	
	public static String dateFormated(LocalDate date) { // creo que no hace falta
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return date.format(formatter);
	}
	
	public void updateDate() {
		updateCreatedAt();
		UpdatedAt();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		 out.writeUTF(nombre.getValueSafe());
		 out.writeUTF(idEmpresa.getValueSafe());
		 out.writeUTF(idNegocio.getValueSafe());
		 out.writeUTF(detalle.getValueSafe());
		 out.writeDouble(precioCantidad.get());
		 out.writeDouble(precioCosto.get());
		 out.writeDouble(precioVenta.get());
		 out.writeDouble(recargo.get());
		 out.writeUTF(updatedAt.getValue().toString());
		 out.writeUTF(createdAt.getValue().toString());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		nombre.set(in.readUTF());
		idEmpresa.set(in.readUTF());
		idNegocio.set(in.readUTF());
		detalle.set(in.readUTF());
		precioCantidad.set(in.readDouble());
		precioCosto.set(in.readDouble());
		precioVenta.set(in.readDouble());
		recargo.set(in.readDouble());
		updatedAt.set(stringToLocalDate(in.readUTF()));
		createdAt.set(stringToLocalDate(in.readUTF()));
	}
	
	public LocalDate stringToLocalDate(String date) {
		String format = date;
		
		if (format.isEmpty() || format == null )
			format = ("2001-01-12T00:00"); // ver como devolver algo que no sea una fecha
		return LocalDate.parse(format);

	}

	@Override
	public int compareTo(Producto value) {
		int value_idNegocio = stringToASCII(value.getIdNegocio());
		int self_idNegocio = stringToASCII(this.idNegocio.getValueSafe());
				
		if (value_idNegocio < self_idNegocio) 
			return -1;
		else if (value_idNegocio > self_idNegocio) 
			return 1;
		
		return 0;
	}
	
	private int stringToASCII(String value) { // mejorar lo que hace es pasar a ACII todos los caracteres de una cadena
		value = value.replaceAll("\\s+", "").trim();
		if (value.isEmpty()) return 0;
		String v = "";
		for (int i = 0; i < value.length(); i++) { 
			char c =value.charAt(i);
			v += String.valueOf(Integer.valueOf((int) c));
		}
		return Integer.valueOf(v);
	}
}
