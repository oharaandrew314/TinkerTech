package io.andrewohara.tinkertech.models;

import java.nio.file.Path;

import org.json.JSONObject;

import io.andrewohara.tinkertech.config.Config;

public abstract class AbstractModStub implements ModStub {
	
	protected JSONObject root;
	
	protected AbstractModStub(JSONObject root) {
		this.root = root;
	}
	
	@Override
	public String getName() {
		return root.getString("name");
	}
	
	@Override
	public String getTitle() {
		return root.getString("title");
	}
	
	@Override
	public String getAuthor() {
		return root.getString("author");
	}

	@Override
	public String getDescription() {
		return root.getString("description");
	}
	
	@Override
	public Path getDownloadPath(Config config) {
		String fileName = String.format("%s_%s.zip", getName(), getVersion());
		return config.getModsPath().resolve(fileName);
	}
}
