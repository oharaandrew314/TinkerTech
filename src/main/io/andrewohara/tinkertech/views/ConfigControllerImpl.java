package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.controlsfx.control.PropertySheet;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import io.andrewohara.tinkertech.config.ConfigLoader;
import io.andrewohara.tinkertech.views.properties.PathPropertyEditor;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ConfigControllerImpl implements ConfigController {

	private final ConfigLoader configLoader;
	private final ErrorHandler errorHandler;
	private final ObservableValue<Path> modsPath;

	@Inject
	protected ConfigControllerImpl(ConfigLoader configLoader, ErrorHandler errorHandler, @Named("modsPath") ObservableValue<Path> modsPath) {
		this.configLoader = configLoader;
		this.errorHandler = errorHandler;
		this.modsPath = modsPath;
	}

	@Override
	public boolean show() {
		PropertySheet propertySheet = new PropertySheet();
		propertySheet.setSearchBoxVisible(false);
		propertySheet.setModeSwitcherVisible(false);

		CustomPropertyItem<Path> gameDataItem = new CustomPropertyItem<>("Paths#Game Data Path", "Path to the Factorio user data.", modsPath.getValue().getParent());
		gameDataItem.setRequired(true);
		gameDataItem.setPropertyEditorClass(PathPropertyEditor.class);
		propertySheet.getItems().add(gameDataItem);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Settings");
		alert.setGraphic(null);
		alert.setHeaderText(null);
		alert.getDialogPane().setContent(propertySheet);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.OK) {
			// Validate properties
			if (gameDataItem.isRequired() && !Files.exists(gameDataItem.getValue())) {
				return false;
			}

			// Save properties
			try {
				configLoader.updateConfig(config -> {
					config.setGameDataPath(gameDataItem.getValue());
				});
				return true;
			} catch (IOException e) {
				errorHandler.handleError(e);
			}
		}
		return false;
	}
}
