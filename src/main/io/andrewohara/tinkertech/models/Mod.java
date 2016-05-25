package io.andrewohara.tinkertech.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;

public class Mod extends AbstractModStub {

	private final Path path;

	public Mod(JSONObject root, Path path) {
		super(root);
		this.path = path;
	}

	@Override
	public Version getVersion() {
		return Version.valueOf(root.getString("version"));
	}

	public void delete() throws IOException {
		Files.delete(path);
	}
}
