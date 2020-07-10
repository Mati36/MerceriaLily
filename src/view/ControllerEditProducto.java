package view;

import java.net.URL;
import java.util.ResourceBundle;

import Exeptions.ProductoExeption;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
	private Button btnAceptar;

	@FXML
	private Button btnCancelar;

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
			txtPrecioVenta.setText(Double.toString(producto.calularPrecioVenta()));
		}
		else 
			txtPrecioVenta.setText(Double.toString(0.0));
	}
	
	// se llama cuado hago click en aceptar
	private void cargarDatosProductos() { 
		producto.setIdEmpresa(txtIdEmpresa.getText().trim());
		producto.setIdNegocio(txtIdNegocio.getText().trim());
		producto.setNombre(txtNombre.getText().trim());
		String precioCantidad = txtPrecioCantidad.getText().trim();
		producto.setPrecioCosto(stringToDouble(txtPrecioCosto.getText(),"PrecioCosto"));
		producto.setRecargo(stringToDouble(txtRecargo.getText(),"Recargo"));
		producto.setPrecioVenta(stringToDouble(txtPrecioVenta.getText(), "Precio venta"));
		producto.setPrecioCantidad(stringToDouble(precioCantidad,"Precio de costo"));
		producto.setDetalle(txtDetalle.getText().trim());
			
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
		} 
		else {
			txtIdEmpresa.setText("");
			txtIdNegocio.setText("");
			txtNombre.setText("");
			txtPrecioCosto.setText("");
			txtRecargo.setText("");
			txtPrecioCantidad.setText("");
			txtPrecioVenta.setText("0.0");
			txtDetalle.setText("");
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
		
	// metodos de test
	private void mostrarProductoConsola(String string, Producto prod) { 
		System.out.println(string);
		System.out.println("Nombre: "+prod.getNombre());
		System.out.println("Id empresa "+prod.getIdEmpresa());
		System.out.println("Id negocio "+prod.getIdNegocio());
		System.out.println("precio costo "+prod.getPrecioCosto());
		System.out.println("precio venta "+prod.getPrecioVenta());
		System.out.println("precio cantidad "+prod.getPrecioCantidad());
		System.out.println();
	}
	
	private void cargarProducto() {
		txtIdEmpresa.setText("ms 39/0");
		txtIdNegocio.setText("A-01");
		txtNombre.setText("Ahujas");
		txtPrecioCosto.setText("5");
		txtPrecioCantidad.setText("20");
	}
		
	
}
