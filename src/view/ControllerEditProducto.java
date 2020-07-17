package view;

import java.net.URL;
import java.util.ResourceBundle;

import Exeptions.ProductoExeption;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Producto;

public class ControllerEditProducto implements Initializable {

	@FXML
	private TextField txtNombre ;
	@FXML
	private TextField txtIdEmpresa;

	@FXML
	private TextField txtIdNegocio;

	@FXML
	private TextField txtPrecioCosto;
	
	@FXML
	private TextField txtPrecioCantidad;
	
	@FXML
	private TextField txtPrecioVenta;

	@FXML
	private TextField txtRecargo;
	
	@FXML
	private TextField txtDetalle;
	
	@FXML
	private TextField txtcantidad;
	
	@FXML
	private Button btnAceptar;

	@FXML
	private Button btnCancelar;

	@FXML 
	private CheckBox checkIva;
	
	private Producto producto;
	
	private boolean isOnClickAceptar  = false; // no tiene nungun uso por ahora
	
	private ControllerPrincipal controllerPrincipal; // se usa para acceder a la tabla y al la base de datos que estan ControllerPrincipal
	
	private Stage dialogStage;
	
	
	public ControllerEditProducto() {
		this.txtNombre = new TextField();
		this.txtIdEmpresa = new TextField();
		this.txtIdNegocio = new TextField();
		this.txtPrecioCosto = new TextField();
		this.txtPrecioCantidad = new TextField();
		this.txtPrecioVenta = new TextField();
		this.txtRecargo = new TextField();
		this.txtDetalle = new TextField();
		this.txtcantidad = new TextField();
		this.checkIva = new CheckBox();
		this.btnAceptar = new Button();
		this.btnCancelar = new Button();
		this.controllerPrincipal = null;
		
	}

	@Override 
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setControllerPrincipal(ControllerPrincipal controllerPrincipal) {
		this.controllerPrincipal = controllerPrincipal;
	}
	
	public void setProducto(Producto producto) {
		this.producto = producto;
		inicio();
		
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
	
	@FXML
	public void clickAceptar() {
		isOnClickAceptar = true;
		cargarDatosProductos();
		// llena la tabla con los productos de la base de datos
		if (isOnClickAceptar)
			controllerPrincipal.getMainApp().closeEditProducto(); // cierra esta ventana 
	}
	
	@FXML
	public void clickCancelar() {
		controllerPrincipal.getMainApp().closeEditProducto();
	}

	
	@FXML // actualiza el precio de venta
	public void keyReleased() {
		String precioCosto = txtPrecioCosto.getText().trim();
		String recargo = txtRecargo.getText().trim();
			
		if (!precioCosto.isEmpty() && !recargo.isEmpty()) {
			producto.setPrecioCosto(stringToDouble(precioCosto,"PrecioCosto"));
			producto.setRecargo(stringToDouble(recargo,"Recargo"));
			calculaPrecioVenta();
		}
		else 
			txtPrecioVenta.setText(Double.toString(0.0));
	}
	
	// se llama cuado hago click en aceptar
	private void cargarDatosProductos() { 
		producto.setIdEmpresa(txtIdEmpresa.getText().toUpperCase().trim());
		producto.setIdNegocio(txtIdNegocio.getText().toUpperCase().trim());
		producto.setNombre(txtNombre.getText().toUpperCase().trim());
		
		String precioCantidad = txtPrecioCantidad.getText().trim();
		
		producto.setPrecioCosto(stringToDouble(txtPrecioCosto.getText(),"PrecioCosto"));
		producto.setRecargo(stringToDouble(txtRecargo.getText(),"Recargo"));
		producto.setPrecioVenta(stringToDouble(txtPrecioVenta.getText(), "Precio venta"));
		producto.setPrecioCantidad(stringToDouble(precioCantidad,"Precio de costo"));
		producto.setDetalle(txtDetalle.getText().toUpperCase().trim());
			
	}	
	
	public void inicio() {
				
		if (!producto.isEmpty()) {
			txtIdEmpresa.setText(producto.getIdEmpresa());
			txtIdNegocio.setText(producto.getIdNegocio());
			txtNombre.setText(producto.getNombre());
			txtPrecioCosto.setText(Double.toString(producto.getPrecioCosto()));
			txtRecargo.setText(Double.toString(producto.getRecargo()));
			txtPrecioCantidad.setText(Double.toString(producto.getPrecioCantidad()));
			txtPrecioVenta.setText(Double.toString(producto.getPrecioVenta()));
			txtDetalle.setText(producto.getDetalle());
			checkIva.setSelected(producto.isIva(producto.getPrecioVenta()));
			txtcantidad.setText("1");
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
	
	@FXML
	public void onCheckIva() {
		producto.setIva(this.checkIva.isSelected());  
		calculaPrecioVenta();
	}

	private void calculaPrecioVenta() {
		int cantidad = 1;
		if (!txtcantidad.getText().isEmpty()) {
			cantidad = Integer.valueOf(txtcantidad.getText());
			if (cantidad > 1) {
				txtPrecioCantidad.setText(Double.toString(producto.precioVentaCantidad()));
				txtPrecioVenta.setText(Double.toString(producto.precioVentaPorUnidad(cantidad)));
			}
			else 
				txtPrecioVenta.setText(Double.toString(producto.precioVentaPorUnidad(cantidad)));
		}	
	}
}
