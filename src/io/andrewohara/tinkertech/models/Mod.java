package io.andrewohara.tinkertech.models;

import java.util.Set;

import io.andrewohara.tinkertech.version.Version;

public class Mod extends Dependency {

	private final String title;
	private final Set<Dependency> dependencies;

	public Mod(String name, Version version, String title, Set<Dependency> dependencies) {
		super(name, version);
		this.title = title;
		this.dependencies = dependencies;
	}

	public String getTitle() {
		return title;
	}
}
