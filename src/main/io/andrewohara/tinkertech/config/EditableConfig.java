package io.andrewohara.tinkertech.config;

import java.nio.file.Path;

public interface EditableConfig extends Config {
	void setGameDataPath(Path path);
}
