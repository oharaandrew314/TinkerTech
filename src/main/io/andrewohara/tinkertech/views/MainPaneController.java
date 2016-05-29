package io.andrewohara.tinkertech.views;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;

public class MainPaneController {

	@FXML HBox mainPane;

	private final Application application;
	private final ErrorHandler errorHandler;
	private final ConfigController configController;

	@Inject
	protected MainPaneController(Application application, ErrorHandler errorHandler, ConfigController configController) {
		this.application = application;
		this.errorHandler = errorHandler;
		this.configController = configController;
	}

	@FXML protected void handleExit(ActionEvent event) {
		try {
			application.stop();
		} catch (Exception e) {
			errorHandler.handleError(e);
		}
	}

	@FXML protected void handleSettings(ActionEvent event) {
		configController.show();
	}

	@FXML protected void handleAbout(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText(String.format("%s by %s - v%s", Main.APP_NAME, Main.AUTHOR, Main.VERSION));
		alert.show();
	}

	@FXML protected void handleContact(ActionEvent event) {
		try {
			Desktop.getDesktop().mail(Main.MAIL_URL.toURI());
		} catch (IOException | URISyntaxException e) {
			errorHandler.handleError(e);
		}
	}

	@FXML protected void handleHelp(ActionEvent event) {
		try {
			Desktop.getDesktop().browse(Main.HELP_URL.toURI());
		} catch (IOException | URISyntaxException e) {
			errorHandler.handleError(e);
		}
	}
}
