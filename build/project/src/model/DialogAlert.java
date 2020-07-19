package model;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class DialogAlert {

	private String content;
	private Alert alertType; 
	private String titel;
	private String headerText;
	private StageStyle stageStyle;
	private boolean resultOption;
	
	public DialogAlert(String content, String titel,StageStyle stageStyle, String headerText, Alert alertType,boolean resultOption) {
		this.content = content;
		this.alertType = alertType;
		this.titel = titel;
		this.headerText = headerText;
		this.stageStyle = stageStyle;
		this.resultOption = resultOption;
		start();
	}
		

	public DialogAlert(String content, String titel,Alert alertType, StageStyle stageStyle) {
		
		this.content = content;
		this.alertType = alertType;
		this.titel = titel;
		this.headerText = null;
		this.stageStyle = stageStyle;
		this.resultOption = false;
		start();
	}
	
	public DialogAlert(String content, String titel,Alert alertType) {
		
		this.content = content;
		this.alertType = alertType;
		this.titel = titel;
		this.headerText = null;
		this.stageStyle = StageStyle.UTILITY;
		this.resultOption = false;
		start();
	}

	protected void start() {
		alertType.setTitle(titel);
		alertType.setHeaderText(headerText);
		alertType.initStyle(stageStyle);
		alertType.setContentText(content);
		Optional<ButtonType> resultOption = alertType.showAndWait();
		this.resultOption = resultOption.get() == ButtonType.OK;
	}
	
	public Alert getalertType() {
		return alertType;
	}


	public void setalertType(Alert alertType) {
		this.alertType = alertType;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public StageStyle getStageStyle() {
		return stageStyle;
	}

	public void setStageStyle(StageStyle stageStyle) {
		this.stageStyle = stageStyle;
	}

	public boolean getResultOption() {
		return resultOption;
	}

	public void setResultOption(boolean resultOption) {
		this.resultOption = resultOption;
	}
	
	
}
