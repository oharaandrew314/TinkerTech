package io.andrewohara.tinkertech.config;

import java.nio.file.Path;

import javafx.beans.value.ObservableValue;

public interface Config {
	ObservableValue<Path> downloadsPath();
	ObservableValue<Path> modsPath();
	ObservableValue<Path> gameDataPath();
	boolean isValid();
}
