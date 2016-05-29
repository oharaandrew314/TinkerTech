package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.controlsfx.control.PropertySheet;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.config.ConfigLoader;
import io.andrewohara.tinkertech.views.properties.PathPropertyEditor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ConfigControllerImpl implements ConfigController {

	private final Config config;
	private final ConfigLoader configLoader;
	private final ErrorHandler errorHandler;

	@Inject
	protected ConfigControllerImpl(Config config, ConfigLoader configLoader, ErrorHandler errorHandler) {
		this.config = config;
		this.configLoader = configLoader;
		this.errorHandler = errorHandler;
	}

	@Override
	public boolean show() {
		PropertySheet propertySheet = new PropertySheet();
		propertySheet.setSearchBoxVisible(false);
		propertySheet.setModeSwitcherVisible(false);

		CustomPropertyItem<Path> gameDataItem = new CustomPropertyItem<>("Paths#Game Data Path", "Path to the Factorio user data.", config.getModsPath().getParent());
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
