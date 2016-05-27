package io.andrewohara.tinkertech.os;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import io.andrewohara.tinkertech.Main;

public class LinuxContext implements OSContext {

	@Override
	public Path getStoragePath() {
		return Paths.get(System.getenv("HOME"), ".config", Main.APP_SLUG);
	}

	@Override
	public Optional<Path> getDataPath() {
		return Optional.empty();
	}
}
