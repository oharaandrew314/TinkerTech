package io.andrewohara.tinkertech.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.andrewohara.tinkertech.os.OSContext;

@Singleton
public class PropertiesConfig implements Config {

	public static final String GAME_DATA_KEY = "gameData";

	private final Properties properties = new Properties();
	private final OSContext osContext;

	@Inject
	protected PropertiesConfig(OSContext osContext) {
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

	public Properties getProperties() {
		return properties;
	}
}
