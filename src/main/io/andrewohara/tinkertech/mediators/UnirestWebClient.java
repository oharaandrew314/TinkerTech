package io.andrewohara.tinkertech.mediators;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestWebClient implements WebClient {
	
	private static final Collection<String> ZIP_CONTENT_TYPES = Arrays.asList("application/zip", "application/octet-stream");

	@Override
	public JSONObject getJsonObject(String url) throws IOException {
		try {
			return Unirest.get(url).asJson().getBody().getObject();
		} catch (UnirestException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public JSONArray getJsonArray(String url) throws IOException {
		try {
			return Unirest.get(url).asJson().getBody().getArray();
		} catch (UnirestException e) {
			throw new IOException(e);
		}
	}

	@Override
	public InputStream getZipStream(String url) throws IOException {
		try {
			HttpResponse<InputStream> response = Unirest.get(url).asBinary();
			
			String contentType = response.getHeaders().get("Content-Type").get(0);
			if (!ZIP_CONTENT_TYPES.contains(contentType)) {
				throw new IOException("Unsupported zip content type of " + contentType);
			}
			return response.getBody();
		} catch (UnirestException e) {
			throw new IOException(e);
		}
	}
}
