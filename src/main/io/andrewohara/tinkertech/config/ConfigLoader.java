package io.andrewohara.tinkertech.config;

import java.io.IOException;
import java.util.function.Consumer;

public interface ConfigLoader {
	void save() throws IOException;
	void load() throws IOException;
	void updateConfig(Consumer<EditableConfig> configActions) throws IOException;
}
