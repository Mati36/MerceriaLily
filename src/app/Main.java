package app;

import java.io.IOException;
import controllers.PrincipalController;
import exeptions.AppExeption;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	
	private AnchorPane layout = new AnchorPane(); // no se
	private Stage primaryStage;

	PrincipalController controllerPrincipal; 

	
	@Override
	public void start(Stage primaryStage) {
	
		try {
			this.primaryStage = primaryStage; 
			this.primaryStage.setTitle("Merceria Lily");
			this.primaryStage.setMaximized(true);
			this.primaryStage.getIcons().add( new Image(getClass().getResourceAsStream("/icon/icon.png")));
			closeApplication(primaryStage);
			mostrarProducto();
			
		} catch (Exception e) {
			new AppExeption("Error al inicar la app, \n"+e.getMessage()+"\n"+e.getCause());
			
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void closeApplication(Stage stage) {
		if (stage != null) {
		
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent event) {
					controllerPrincipal.closeClcik();
					primaryStage.close();
					Platform.exit();
					
				}
			});
			
		}
		
	}
	
	
	public void mostrarProducto() throws IOException { // Re escribir
		FXMLLoader viewLoader = new FXMLLoader(getClass().getResource("/views/Principal.fxml"));
				
		if(viewLoader.getLocation() != null) {
			
			layout = (AnchorPane) viewLoader.load();
			
			Scene scene = new Scene(layout);
//			scene.getStylesheets().add(getClass().getResource("bootstrap3.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			controllerPrincipal = viewLoader.getController();
//			controllerPrincipal.setStage(primaryStage);
		
		}
		else 
			throw new IOException("RootLayout.fxml No encontrado");
	}
	

	public Stage getPrimaryStage() {
		return primaryStage;
	}


	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}


	
}
