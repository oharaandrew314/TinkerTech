package io.andrewohara.tinkertech.models;

import org.json.JSONObject;

import io.andrewohara.tinkertech.version.Version;

public class Release {

	private final Listing listing;
	private final JSONObject root;
	
	public Release(Listing listing, JSONObject root) {
		this.listing = listing;
		this.root = root;
	}
	
	public Version getVersion() {
		String version = root.getString("version");
		return Version.valueOf(version);
	}
	
	public String getDownloadUrl() {
		JSONObject file = getFile();
		return file.isNull("url") ? null : file.getString("url");
	}
	
	public String getMirrorUrl() {
		JSONObject file = getFile();
		return file.isNull("mirror") ? null : file.getString("mirror");
	}
	
	private JSONObject getFile() {
		return (JSONObject) root.getJSONArray("files").get(0);
	}
	
	public String getFilename() {
		return String.format("%s_%s.zip", listing.getName(), getVersion());
	}
}
