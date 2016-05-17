package io.andrewohara.tinkertech.mediators;

import java.io.IOException;

import com.mashape.unirest.http.JsonNode;

public interface JsonParser {
	JsonNode parse(String url) throws IOException;
}
