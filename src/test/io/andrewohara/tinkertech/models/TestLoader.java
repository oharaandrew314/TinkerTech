package io.andrewohara.tinkertech.models;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import io.andrewohara.tinkertech.models.Listing;

public class TestLoader {
	
	public static Listing loadListing(String name) {
		try {
			InputStream is = TestLoader.class.getClassLoader().getResourceAsStream("io/andrewohara/tinkertech/models/listing/" + name + ".json");
			String json = IOUtils.toString(is);
			return new Listing(new JSONObject(json));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Mod loadMod(String name) {
		try {
			InputStream is = TestLoader.class.getClassLoader().getResourceAsStream("io/andrewohara/tinkertech/models/mod/" + name + ".json");
			String json = IOUtils.toString(is);
			return new Mod(new JSONObject(json));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
