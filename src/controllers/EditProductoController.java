package controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
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
		if (!number.matches("\\d")) {
			return 0.0;
		}
		return Double.parseDouble(number.trim());
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
		RequiredFieldValidator required = new RequiredFieldValidator("Campo Obligatorio");
		RegexValidator numberValid = new RegexValidator("Introduzca un numero valido");
		String regexDouble = "[0-9]+(\\.[0-9]+)|[0-9]+";
		numberValid.setRegexPattern(regexDouble);
		
		txtNombre.getValidators().add(required);
		txtIdNegocio.getValidators().add(required);
		txtPrecioCantidad.getValidators().add(numberValid);
		txtPrecioCosto.getValidators().add(numberValid);
		txtPrecioVenta.getValidators().add(numberValid);
		txtRecargo.getValidators().add(numberValid);
		isValid(txtNombre);
		isValid(txtIdNegocio);
		isValid(txtPrecioCantidad);
		isValid(txtPrecioCosto);
		isValid(txtPrecioVenta);
		isValid(txtRecargo);	
	}
	
	private void isValid(JFXTextField textField) {
		textField.focusedProperty().addListener((observableValue,oldValue,newValue) ->{
			if(!newValue) textField.validate();
			else textField.resetValidation();
		});
	}
}
