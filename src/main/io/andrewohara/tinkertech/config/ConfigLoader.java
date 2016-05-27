package io.andrewohara.tinkertech.config;

import java.io.IOException;

public interface ConfigLoader {
	void save() throws IOException;
	void load() throws IOException;
}
