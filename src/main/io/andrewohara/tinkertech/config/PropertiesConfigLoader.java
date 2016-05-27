package io.andrewohara.tinkertech.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.Main;
import io.andrewohara.tinkertech.StartupTask;
import io.andrewohara.tinkertech.os.OSContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;

public class PropertiesConfigLoader implements ConfigLoader, StartupTask {

	private final OSContext osContext;
	private final PropertiesConfig config;

	@Inject
	protected PropertiesConfigLoader(PropertiesConfig config, OSContext osContext) {
		this.config = config;
		this.osContext = osContext;
	}

	@Override
	public void load() throws IOException {
		Properties properties = config.getProperties();
		properties.load(Files.newInputStream(getConfigPath()));

		if (!properties.containsKey(PropertiesConfig.GAME_DATA_KEY)) {
			if (osContext.getDataPath().isPresent()) {
				properties.setProperty(PropertiesConfig.GAME_DATA_KEY, osContext.getDataPath().get().toString());
			} else {
				Path dataPath = askForDataPath();
				properties.setProperty(PropertiesConfig.GAME_DATA_KEY, dataPath.toString());
			}
			save();
		}
	}

	@Override
	public void save() throws IOException {
		config.getProperties().store(Files.newOutputStream(getConfigPath()), null);
	}

	@Override
	public void startup() throws Exception {
		load();
	}

	private Path getConfigPath() {
		return osContext.getStoragePath().resolve("config.properties");
	}

	private Path askForDataPath() throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Welcome to " + Main.APP_NAME);
		alert.setHeaderText("Setup required");
		alert.setContentText("The Factorio AppData folder was not detected.  Please locate it.\nIt should contain a 'mods' folder inside it.");
		alert.showAndWait();

		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Please locate the Factorio Data directory");
		File dataFile = chooser.showDialog(null);
		if (dataFile == null) {
			return null;
		}
		Path dataDir = dataFile.toPath();
		if (Files.exists(dataDir)) {
			return dataDir;
		}
		throw new IOException("Factorio Data directory was not found");
	}
}
