package app;


import java.io.IOException;
import Exeptions.AppExeption;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.DialogAlert;
import model.Producto;
import view.ControllerEditProducto;
import view.ControllerPrincipal;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	
	private ObservableList<Producto> listProducto = FXCollections.observableArrayList();
	private AnchorPane layout = new AnchorPane();
	private Stage primaryStage;
	private Stage stageEditProducto;
	ControllerPrincipal controllerPrincipal;
	private boolean onClickConfirmation;
	
	@Override
	public void start(Stage primaryStage) {
	
		try {
			this.primaryStage = primaryStage; 
			this.primaryStage.setTitle("Merceria Lili");
			this.primaryStage.setMaximized(true);
			this.primaryStage.getIcons().add( new Image(getClass().getResourceAsStream("/icon/icon.png")));
			mostrarProducto();
			
		} catch (Exception e) {
			new AppExeption("Error al inicar la app, \n"+e.getMessage()+"\n"+e.getCause());
			
		}
	}

	@Override
	public void stop() throws Exception {
		controllerPrincipal.closeClcik();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void mostrarProducto() throws IOException {
		FXMLLoader viewLoader = new FXMLLoader();
		viewLoader.setLocation(getClass().getResource("/view/Principal.fxml"));
		
		if(viewLoader.getLocation() != null) {
			
			layout = (AnchorPane) viewLoader.load();
			
			Scene scene = new Scene(layout);
//			scene.getStylesheets().add(getClass().getResource("bootstrap3.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			controllerPrincipal = viewLoader.getController();
			controllerPrincipal.setStage(primaryStage);
			controllerPrincipal.setMainApp(this);
		}
		else 
			throw new IOException("RootLayout.fxml No encontrado");
	}
	
	public void mostrarEditProducto(Producto prod, Stage stageEditProducto) throws IOException { // es llamado de ControllerPricipal
		FXMLLoader viewLoader = new FXMLLoader();
		viewLoader.setLocation(getClass().getResource("/view/EditProducto.fxml"));
		
		if( viewLoader.getLocation() != null) {
			AnchorPane layout = (AnchorPane) viewLoader.load();
			this.stageEditProducto = stageEditProducto;
			Scene seceneEditProducto = new Scene(layout);
			this.stageEditProducto.setScene(seceneEditProducto);
			ControllerEditProducto controllerEditProducto = viewLoader.getController();
			controllerEditProducto.setProducto(prod);
			controllerEditProducto.setControllerPrincipal(controllerPrincipal);
			controllerEditProducto.setDialogStage(stageEditProducto);
			stageEditProducto.setResizable(false);
			stageEditProducto.showAndWait(); 
			onClickConfirmation = controllerEditProducto.getIsOnclickAceptar();
		}
		else
			throw new IOException("RootLayout.fxml No encontrado");
	}
	
	public ObservableList<Producto> getListProducto() {
		return listProducto;
	}
	
	public void closeEditProducto() {
		stageEditProducto.close();
	}


	public boolean isOnClickConfirmation() {
		return onClickConfirmation;
	}


	public void setOnClickConfirmation(boolean onClickConfirmation) {
		this.onClickConfirmation = onClickConfirmation;
	}


	public Stage getPrimaryStage() {
		return primaryStage;
	}


	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}


	public Stage getStageEditProducto() {
		return stageEditProducto;
	}


	public void setStageEditProducto(Stage stageEditProducto) {
		this.stageEditProducto = stageEditProducto;
	}

}
