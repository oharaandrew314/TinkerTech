package io.andrewohara.tinkertech.views;

import com.google.inject.Inject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class MainPaneController {

	@FXML HBox mainPane;

	private final Application application;
	private final ErrorHandler errorHandler;

	@Inject
	protected MainPaneController(Application application, ErrorHandler errorHandler) {
		this.application = application;
		this.errorHandler = errorHandler;
	}

	@FXML protected void handleExit(ActionEvent event) {
		try {
			application.stop();
		} catch (Exception e) {
			errorHandler.handleError(e);
		}
	}
}
