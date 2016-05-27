package io.andrewohara.tinkertech.os;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import io.andrewohara.tinkertech.Main;

public class MacContext implements OSContext {

	@Override
	public Path getStoragePath() {
		Path applicationSupport = Paths.get(System.getenv("HOME"), "Library/Application Support/");
		return applicationSupport.resolve(Main.APP_SLUG);
	}

	@Override
	public Optional<Path> getDataPath() {
		Path dataPath = Paths.get("/Applications/factorio.app");
		if (Files.exists(dataPath)) {
			return Optional.of(dataPath);
		}
		return Optional.empty();
	}
}
