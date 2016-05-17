package io.andrewohara.tinkertech.mediators;

import java.io.IOException;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestJsonParser implements JsonParser {

	@Override
	public JsonNode parse(String url) throws IOException {
		try {
			return Unirest.get(url).asJson().getBody();
		} catch (UnirestException e) {
			throw new IOException(e);
		}
	}
}
