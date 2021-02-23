package models;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DialogShow{
	
	private String content;
	private String title;
	static private Alert alertType;  
	static private boolean resultOption;
	static private Optional<ButtonType> buttonOptions;
	
	public DialogShow(AlertType type) {	
		alertType = new Alert(type);
	}
	
	public DialogShow(String titel,String content, AlertType type) {	
		alertType = new Alert(type);
		alertType.setTitle(titel);
		alertType.setContentText(content);
	}
	
	private static void start(String titel,String content, AlertType type) {
		alertType = new Alert(type);
		alertType.setTitle(titel);
		alertType.setContentText(content);
		alertType.show();	
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

	public static final void Warning(String titel,String content) {
		 start (titel,content, AlertType.INFORMATION);
	}
	public static boolean isResultOption() {
		return resultOption;
	}

	public static void setResultOption(boolean resultOption) {
		DialogShow.resultOption = resultOption;
	}

	public static Alert getAlertType() {
		return alertType;
	}

	public static void setAlertType(Alert alertType) {
		DialogShow.alertType = alertType;
	}
	
	public void show() {
		
		if (alertType != null) 
			buttonOptions = alertType.showAndWait();
	
	}
	
	public ButtonType buttonType() {
		
		if(buttonOptions != null)
			return buttonOptions.get();
		
		return null;
	}
	
	public boolean isOkButton() {
		return buttonType() == ButtonType.OK;
	}
	
	public boolean isCancelButton() {
		return buttonType() == ButtonType.CANCEL;
	}
	
	public boolean isYesButton() {
		return buttonType().getButtonData() == ButtonType.YES.getButtonData();
	}
	
	public static Optional<ButtonType> getButtonOptions() {
		return buttonOptions;
	}
	
	public static void setButtonOptions(Optional<ButtonType> buttonOptions) {
		DialogShow.buttonOptions = buttonOptions;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		alertType.setContentText(content);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		alertType.setTitle(title);
	}
	
	
	
}
