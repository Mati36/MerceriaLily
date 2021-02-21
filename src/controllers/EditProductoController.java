package controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import exeptions.ProductoExeption;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.stage.Stage;
import models.Producto;

public class EditProductoController {

	@FXML private JFXTextField txtNombre ;
	@FXML private JFXTextField txtIdEmpresa;
	@FXML private JFXTextField txtIdNegocio;
	@FXML private JFXTextField txtPrecioCosto;
	@FXML private JFXTextField txtPrecioCantidad;
	@FXML private JFXTextField txtPrecioVenta;
	@FXML private JFXTextField txtRecargo;
	@FXML private JFXTextField txtDetalle;
	@FXML private JFXTextField txtcantidad;
	@FXML private Button btnAceptar;
	@FXML private Button btnCancelar;
	@FXML private JFXCheckBox checkIva;
	private Producto producto;
	private boolean isOnClickAceptar  = false; // no tiene nungun uso por ahora
	private Stage stage;
	
	
	@FXML 
	public void initialize() {
						
	}
	
	
	public EditProductoController() {		
		this.txtNombre = new JFXTextField();
		this.txtIdEmpresa = new JFXTextField();
		this.txtIdNegocio = new JFXTextField();
		this.txtPrecioCosto = new JFXTextField();
		this.txtPrecioCantidad = new JFXTextField();
		this.txtPrecioVenta = new JFXTextField();
		this.txtRecargo = new JFXTextField();
		this.txtDetalle = new JFXTextField();
		this.txtcantidad = new JFXTextField();
		this.checkIva = new JFXCheckBox();
		this.btnAceptar = new Button();
		this.btnCancelar = new Button();
		

	}
	
	public void setProducto(Producto producto) {
		this.producto = producto;
		inicio();
		
	}
	
	public void inicio() {
		validate();
		
		if (!producto.isEmpty()) {
			setTxtValue(txtIdEmpresa, producto.getIdEmpresa());
			setTxtValue(txtIdNegocio, producto.getIdNegocio());
			setTxtValue(txtNombre, producto.getNombre());
			setTxtValue(txtPrecioCosto,  Double.toString(producto.getPrecioCosto()));
			setTxtValue(txtRecargo, Double.toString(producto.getRecargo()));
			setTxtValue(txtPrecioCantidad, Double.toString(producto.getPrecioCantidad()));
			setTxtValue(txtPrecioVenta, Double.toString(producto.getPrecioVenta()));
			setTxtValue(txtDetalle, producto.getDetalle());
			checkIva.setSelected(producto.isIva(producto.getPrecioVenta()));
			setTxtValue(txtcantidad, "1");
			
		} 
		else {
			txtIdEmpresa.setText("");
			txtIdNegocio.setText("");
			txtNombre.setText("");
			txtPrecioCosto.setText("0.0");
			txtRecargo.setText("0.0");
			txtPrecioCantidad.setText("0.0");
			txtPrecioVenta.setText("0.0");
			txtDetalle.setText("");
			txtcantidad.setText("1");
		}
	}
		
	// se llama cuado hago click en aceptar
	private void cargarDatosProductos() { 
		producto.setIdEmpresa(getTxtValue(txtIdEmpresa).toUpperCase());
		producto.setIdNegocio(getTxtValue(txtIdNegocio).toUpperCase());
		producto.setNombre(getTxtValue(txtNombre).toUpperCase());
		
		String precioCantidad = getTxtValue(txtPrecioCantidad);
		
		producto.setPrecioCosto(stringToDouble(getTxtValue(txtPrecioCosto),"PrecioCosto"));
		producto.setRecargo(stringToDouble(getTxtValue(txtRecargo),"Recargo"));
		producto.setPrecioVenta(stringToDouble(getTxtValue(txtPrecioVenta), "Precio venta"));
		producto.setPrecioCantidad(stringToDouble(precioCantidad,"Precio de costo"));
		producto.setDetalle(getTxtValue(txtDetalle).toUpperCase());
		producto.updateDate();	
	}	
	
	@FXML
	public void clickAceptar() {
				
		if (txtNombre.validate() && txtIdNegocio.validate()) {
			isOnClickAceptar = true;
			cargarDatosProductos();
			stage.close();
		}
		
	}
	
	@FXML
	public void clickCancelar() {
		isOnClickAceptar = false;
		stage.close();
	}

	
	@FXML // actualiza el precio de venta
	public void keyReleased() {
		String precioCosto = getTxtValue(txtPrecioCosto);	
		String recargo = getTxtValue(txtRecargo);
			
		if (!precioCosto.isEmpty() && !recargo.isEmpty()) {
			producto.setPrecioCosto(stringToDouble(precioCosto,"PrecioCosto"));
			producto.setRecargo(stringToDouble(recargo,"Recargo"));
			calculaPrecioVenta();
		}
		else 
			setTxtValue(txtPrecioVenta, Double.toString(0.0));
			
	}
	
	@FXML
	public void onCheckIva() {
		producto.setIva(this.checkIva.isSelected());  
		calculaPrecioVenta();
	}
	
	public boolean getIsOnclickAceptar() {
		return this.isOnClickAceptar;
		
	}

	public boolean isOnClickAceptar() {
		return isOnClickAceptar;
	}

	public void setOnClickAceptar(boolean isOnClickAceptar) {
		this.isOnClickAceptar = isOnClickAceptar;
	}
	
	
	private  Double stringToDouble(String number,String dato) {
		Double num = 0.0;
		
		try {
			 num = Double.parseDouble(number.trim());
			
		} catch (Exception e) {
			new ProductoExeption("El dato "+dato+" no es de tipo numerico");
			
			this.isOnClickAceptar = false;
		}
		return num;
	}
	
	
	private void calculaPrecioVenta() {
		int cantidad = 1;
		String precioCantidad = getTxtValue(txtcantidad); 
		if (!precioCantidad.isEmpty()) {
			cantidad = Integer.valueOf(precioCantidad);
			if (cantidad > 1) {
				setTxtValue(txtPrecioCantidad, Double.toString(producto.precioVentaCantidad()));
				setTxtValue(txtPrecioVenta, Double.toString(producto.precioVentaPorUnidad(cantidad)));
				
			}
			else
				setTxtValue(txtPrecioVenta, Double.toString(producto.precioVentaPorUnidad(cantidad)));
		}	
	}
	
	private String removeManyBlanks(String text) {
		
		return text.replaceAll("( )+", " ").trim();
	}

	private String getTxtValue(JFXTextField JFXTextField) {
		return removeManyBlanks(JFXTextField.getText());
	}

	private void setTxtValue(JFXTextField JFXTextField,String value) {
		JFXTextField.setText(removeManyBlanks(value));
	}	
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	private void validate() { //ver como desplazar el texto al medio del imput
		RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Campo Obligatorio");
		
		txtNombre.getValidators().add(requiredFieldValidator);
		txtIdNegocio.getValidators().add(requiredFieldValidator);
		txtNombre.focusedProperty().addListener((observableValue,oldValue,newValue) ->{
			if(!newValue) txtNombre.validate();
			else txtNombre.resetValidation();
		});
		txtIdNegocio.focusedProperty().addListener((observableValue,oldValue,newValue) ->{
			if(!newValue) txtIdNegocio.validate();
			else txtIdNegocio.resetValidation();
		});
	}
}
