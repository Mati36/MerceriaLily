package model;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DialogShow {
	

	private String content;
	static private Alert alertType;  
	static private boolean resultOption;

	public DialogShow() {	}
	
	private static void start(String titel,String content, AlertType type) {
		alertType = new Alert(type);
		alertType.setTitle(titel);
		alertType.setContentText(content);
		Optional<ButtonType> option = alertType.showAndWait();
		setResultOption(option.get() == ButtonType.OK);
		
	}

	public static final void Error(String titel,String content) {
		start (titel,content, AlertType.ERROR);
	}
	
	public static final void Confirmarion(String titel,String content) {
		 start (titel,content, AlertType.CONFIRMATION);
		 
		 
	}
	public static final void Information(String titel,String content) {
		 start (titel,content, AlertType.INFORMATION);
	}

	public static boolean isResultOption() {
		return resultOption;
	}

	public static void setResultOption(boolean resultOption) {
		DialogShow.resultOption = resultOption;
	}
	
}
