package io.andrewohara.tinkertech.views;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogErrorHandler implements ErrorHandler {

	@Override
	public void handleError(Exception e) {
		e.printStackTrace();

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(e.getClass().getSimpleName());
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}
}
