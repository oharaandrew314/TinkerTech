package io.andrewohara.tinkertech.models;

import io.andrewohara.tinkertech.version.Version;

public class Dependency {

	private final String name;
	private final Version version;

	public Dependency(String name, Version version) {
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public Version getVersion() {
		return version;
	}
}
