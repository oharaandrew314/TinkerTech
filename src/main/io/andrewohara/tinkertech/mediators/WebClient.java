package io.andrewohara.tinkertech.mediators;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

public interface WebClient {
	JSONObject getJsonObject(String url) throws IOException;
	JSONArray getJsonArray(String url) throws IOException;
	InputStream getZipStream(String url) throws IOException;
}
