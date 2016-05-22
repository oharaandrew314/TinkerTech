package io.andrewohara.tinkertech.models;

import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONObject;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.version.Version;

public class Mod extends AbstractModStub {
	
	public Mod(JSONObject root) {
		super(root);
	}
	
	@Override
	public Version getVersion() {
		return Version.valueOf(root.getString("version"));
	}
	
	public void delete(Config config) throws IOException {
		Files.delete(getDownloadPath(config));
	}
}
