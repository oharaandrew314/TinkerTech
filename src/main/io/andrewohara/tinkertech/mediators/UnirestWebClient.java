package io.andrewohara.tinkertech.mediators;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestWebClient implements WebClient {

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
			String expectedContentType = "application/zip";
			if (!contentType.equals(expectedContentType)) {
				throw new IOException(String.format("Expected %s, was %s", expectedContentType, contentType));
			}
			return response.getBody();
		} catch (UnirestException e) {
			throw new IOException(e);
		}
	}
}
