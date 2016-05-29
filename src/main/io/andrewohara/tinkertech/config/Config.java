package io.andrewohara.tinkertech.config;

import java.nio.file.Path;

import javafx.beans.value.ObservableValue;

public interface Config {
	Path getDownloadPath();
	Path getModsPath();
	ObservableValue<Path> modsPath();
	boolean isValid();
}
