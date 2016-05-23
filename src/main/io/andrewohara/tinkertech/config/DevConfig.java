package io.andrewohara.tinkertech.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DevConfig implements Config {
	@Override public Path getDownloadPath() { return Paths.get("C:/users/Andrew/Desktop/temp"); }
	@Override public Path getModsPath() { return Paths.get("C:/users/Andrew/Desktop/mods"); }
}
