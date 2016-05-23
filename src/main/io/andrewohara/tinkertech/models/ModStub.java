package io.andrewohara.tinkertech.models;

import io.andrewohara.tinkertech.version.Version;

public interface ModStub {
	String getName();
	String getTitle();
	String getAuthor();
	Version getVersion();
	String getDescription();
}
