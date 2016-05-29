package io.andrewohara.tinkertech.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.andrewohara.tinkertech.os.OSContext;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

@Singleton
public class PropertiesConfig implements EditableConfig {

	public static final String GAME_DATA_KEY = "gameData";

	private final Properties properties = new Properties();
	private final OSContext osContext;

	private ObjectProperty<Path> modsPath = new SimpleObjectProperty<>();

	@Inject
	public PropertiesConfig(OSContext osContext) {
		this.osContext = osContext;
	}

	@Override
	public Path getModsPath() {
		return Paths.get(properties.getProperty(GAME_DATA_KEY), "mods");
	}

	@Override
	public Path getDownloadPath() {
		return osContext.getStoragePath().resolve("downloads");
	}

	@Override
	public void setGameDataPath(Path path) {
		modsPath.set(path);
		properties.setProperty(GAME_DATA_KEY, path.toString());
	}

	public Properties getProperties() {
		return properties;
	}

	@Override
	public boolean isValid() {
		return StringUtils.isNotEmpty(properties.getProperty(GAME_DATA_KEY));
	}

	@Override
	public ObservableValue<Path> modsPath() {
		return modsPath;
	}
}
