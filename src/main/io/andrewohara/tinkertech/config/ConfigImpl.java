package io.andrewohara.tinkertech.config;

import java.nio.file.Files;
import java.nio.file.Path;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.andrewohara.tinkertech.os.OSContext;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

@Singleton
public class ConfigImpl implements EditableConfig {

	private final ObjectProperty<Path>
	gameDataPath = new SimpleObjectProperty<>(),
	modsPath = new SimpleObjectProperty<>(),
	downloadsPath;

	@Inject
	public ConfigImpl(OSContext osContext) {
		downloadsPath = new SimpleObjectProperty<>(osContext.getStoragePath().resolve("downloads"));
	}

	@Override
	public ObservableValue<Path> modsPath() {
		return modsPath;
	}

	@Override
	public ObservableValue<Path> downloadsPath() {
		return downloadsPath;
	}

	@Override
	public ObservableValue<Path> gameDataPath() {
		return gameDataPath;
	}

	@Override
	public void setGameDataPath(Path path) {
		gameDataPath.set(path);
		modsPath.set(path.resolve("mods"));
	}

	@Override
	public boolean isValid() {
		return Files.exists(gameDataPath.get());
	}
}
