package io.andrewohara.tinkertech.models;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import io.andrewohara.tinkertech.version.Version;

public class Listing extends AbstractModStub {

	public Listing(JSONObject root) {
		super(root);
	}

	public Release getLatestRelease() {
		return getReleases().get(0);
	}

	private List<Release> getReleases() {
		List<Release> releases = new LinkedList<>();
		for (Object release : root.getJSONArray("releases")) {
			releases.add(new Release(this, (JSONObject) release));
		}
		return releases;
	}

	@Override
	public Version getVersion() {
		return getLatestRelease().getVersion();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Listing) {
			return getName().equals(((Listing)obj).getName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
}
