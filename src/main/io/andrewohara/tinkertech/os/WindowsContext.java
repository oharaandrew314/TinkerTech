package io.andrewohara.tinkertech.os;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import io.andrewohara.tinkertech.Main;

public class WindowsContext implements OSContext {

	@Override
	public Path getStoragePath() {
		return getAppDataPath().resolve(Main.APP_SLUG);
	}

	@Override
	public Optional<Path> getDataPath() {
		Path dataPath = getAppDataPath().resolve("Factorio");
		if (Files.exists(dataPath)) {
			return Optional.of(dataPath);
		}
		return Optional.empty();
	}

	private Path getAppDataPath() {
		return Paths.get(System.getenv("AppData"));
	}
}
