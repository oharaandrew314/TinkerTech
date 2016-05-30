package io.andrewohara.tinkertech.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Consumer;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.os.OSContext;
import io.andrewohara.tinkertech.views.ConfigController;

public class PropertiesConfigLoader implements ConfigLoader {

	private final OSContext osContext;
	private final EditableConfig config;
	private final ConfigController configController;

	private final Properties properties = new Properties();

	@Inject
	protected PropertiesConfigLoader(EditableConfig config, OSContext osContext, ConfigController configController) {
		this.config = config;
		this.osContext = osContext;
		this.configController = configController;

		config.gameDataPath().addListener((prop, oldValue, newValue) -> {
			properties.setProperty("gameData", newValue.toString());
		});
	}

	@Override
	public void load() throws IOException {
		try (InputStream is = Files.newInputStream(getConfigPath())) {
			properties.load(is);
		}

		try {
			Path path = Paths.get(properties.getProperty("gameData"));
			config.setGameDataPath(path);
		} catch (Exception e) {
			osContext.getDataPath().ifPresent(path -> {
				config.setGameDataPath(path);
			});
		}

		while (!config.isValid()) {
			if (configController.show()) {
				break;
			}
		}
	}

	@Override
	public void save() throws IOException {
		try (OutputStream os = Files.newOutputStream(getConfigPath())) {
			properties.store(os, null);
		}
	}

	private Path getConfigPath() {
		return osContext.getStoragePath().resolve("config.properties");
	}

	@Override
	public void updateConfig(Consumer<EditableConfig> configActions) throws IOException {
		configActions.accept(config);
		save();
	}
}
