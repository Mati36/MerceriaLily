package view;

import java.net.URL;
import java.util.ResourceBundle;
import app.Producto;
import controller.DialogAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
	private Button btnAceptar;

	@FXML
	private Button btnCancelar;

	private Producto producto;
	
	private boolean isOnClickAceptar  = false; // no tiene nungun uso por ahora
	
	private ControllerPrincipal controllerPrincipal; // se usa para acceder a la tabla y al la base de datos que estan ControllerPrincipal
	
	private Stage dialogStage;
	
	
	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public ControllerEditProducto() {
		this.txtNombre = new TextField();
		this.txtIdEmpresa = new TextField();
		this.txtIdNegocio = new TextField();
		this.txtPrecioCosto = new TextField();
		this.txtPrecioCantidad = new TextField();
		this.txtPrecioVenta = new TextField();
		this.txtRecargo = new TextField();
		this.btnAceptar = new Button();
		this.btnCancelar = new Button();
		this.controllerPrincipal = null;
		
	}

	@Override 
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void setControllerPrincipal(ControllerPrincipal controllerPrincipal) {
		this.controllerPrincipal = controllerPrincipal;
	}
	
	public void setProducto(Producto producto) {
		this.producto = producto;
		System.out.println(!producto.isEmpty());
		inicio();
		
	}
	
	@FXML
	public void clickAceptar() {
		isOnClickAceptar = true;
		textFieldInToProdructo();
		// llena la tabla con los productos de la base de datos
		if (isOnClickAceptar)
			controllerPrincipal.getMainApp().closeEditProducto(); // cierra esta ventana 
	}
	
	@FXML
	public void clickCancelar() {
		controllerPrincipal.getMainApp().closeEditProducto();
		
	}

	private void textFieldInToProdructo() {
		// carga el producto con los datos ingresados
		//mostrarProductoConsola(producto); // para test
		producto.setIdEmpresa(txtIdEmpresa.getText().trim());
		producto.setIdNegocio(txtIdNegocio.getText().trim());
		producto.setNombre(txtNombre.getText().trim());
	
		String precioCantidad = txtPrecioCantidad.getText().trim();
		producto.setPrecioCantidad(stringToDouble(precioCantidad,"Precio de costo"));
		//producto.setPrecioVenta(producto.calularPrecioVenta());
		
	}	
	
	private void mostrarProductoConsola(Producto prod) { // de test 
		System.out.println(txtIdEmpresa.getText().trim());
		System.out.println(txtIdNegocio.getText().trim());
		System.out.println(txtNombre.getText().trim());
		System.out.println(Double.parseDouble(txtPrecioCosto.getText().trim()));
		//System.out.println(Double.parseDouble(txtPrecioVenta.getText().trim()));
		System.out.println(Double.parseDouble(txtPrecioCantidad.getText().trim()));
		
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
		} 
		else {
			txtIdEmpresa.setText("");
			txtIdNegocio.setText("");
			txtNombre.setText("");
			txtPrecioCosto.setText("");
			txtRecargo.setText("");
			txtPrecioCantidad.setText("");
			txtPrecioVenta.setText("0.0");
			//cargarProducto();
		}
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
	
	private void cargarProducto() {
		txtIdEmpresa.setText("ms 39/0");
		txtIdNegocio.setText("A-01");
		txtNombre.setText("Ahujas");
		txtPrecioCosto.setText("5");
		txtPrecioCantidad.setText("20");
		
	}
	
	@FXML // actualiza el precio de venta
	public void keyReleased() {
		String precioCosto = txtPrecioCosto.getText().trim();
		String recargo = txtRecargo.getText().trim();
			
		if (!precioCosto.isEmpty() && !recargo.isEmpty()) {
			producto.setPrecioCosto(stringToDouble(precioCosto,"PrecioCosto"));
			producto.setRecargo(stringToDouble(recargo,"Recargo"));
			double precioVenta  = producto.calularPrecioVenta();	
			txtPrecioVenta.setText(Double.toString(precioVenta));
		}else 
			txtPrecioVenta.setText(Double.toString(0.0));
		
				
	}
	
	private  Double stringToDouble(String number,String dato) {
		Double num = 0.0;
		
		try {
			 num = Double.parseDouble(number);
			
		} catch (Exception e) {
			errorDialog("Error el dato "+dato+" no es un numero ");
			System.out.println("Error el dato no es un numero "+e.getMessage());
			this.isOnClickAceptar = false;
		}
		return num;
	}
	
	private boolean errorDialog(String content) {
		DialogAlert dialogAlert = new DialogAlert(content, "Error",new Alert(AlertType.ERROR));
		return dialogAlert.getResultOption();
	}
}
