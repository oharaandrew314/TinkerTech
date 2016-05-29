package io.andrewohara.tinkertech.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.os.OSContext;
import io.andrewohara.tinkertech.views.ConfigController;

public class PropertiesConfigLoader implements ConfigLoader {

	private final OSContext osContext;
	private final PropertiesConfig config;
	private final ConfigController configController;

	@Inject
	protected PropertiesConfigLoader(PropertiesConfig config, OSContext osContext, ConfigController configController) {
		this.config = config;
		this.osContext = osContext;
		this.configController = configController;
	}

	@Override
	public void load() throws IOException {
		try (InputStream is = Files.newInputStream(getConfigPath())) {
			config.getProperties().load(is);
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
			config.getProperties().store(os, null);
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
