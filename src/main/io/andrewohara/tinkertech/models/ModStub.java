package io.andrewohara.tinkertech.models;

import java.nio.file.Path;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.version.Version;

public interface ModStub {
	String getName();
	String getTitle();
	String getAuthor();
	Version getVersion();
	String getDescription();
	Path getDownloadPath(Config config);
}
