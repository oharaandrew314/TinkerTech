package io.andrewohara.tinkertech.models;

import org.json.JSONObject;

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
	public String toString() {
		return String.format("%s v%s", getTitle(), getVersion());
	}
}
