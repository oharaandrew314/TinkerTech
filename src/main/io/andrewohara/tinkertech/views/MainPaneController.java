package io.andrewohara.tinkertech.views;

import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class MainPaneController extends Controller {

	@FXML HBox mainPane;

	private final FXMLLoader listingPaneLoader, modsPaneLoader;
	private final Application application;
	private final ErrorHandler errorHandler;

	@Inject
	protected MainPaneController(@Named("listingPane") FXMLLoader listingPaneLoader, @Named("modsPane") FXMLLoader modsPaneLoader, Application application, ErrorHandler errorHandler) {
		this.listingPaneLoader = listingPaneLoader;
		this.modsPaneLoader = modsPaneLoader;
		this.application = application;
		this.errorHandler = errorHandler;
	}

	@Override
	protected void init() {
		try {
			mainPane.getChildren().add(listingPaneLoader.load());
			mainPane.getChildren().add(modsPaneLoader.load());
		} catch (IOException e) {
			errorHandler.handleError(e);
			throw new RuntimeException(e);
		}
	}

	@FXML protected void handleExit(ActionEvent event) {
		try {
			application.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
