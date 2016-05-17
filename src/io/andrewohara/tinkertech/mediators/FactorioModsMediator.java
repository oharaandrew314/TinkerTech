package io.andrewohara.tinkertech.mediators;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;

public class FactorioModsMediator {

	public static final String LIST_URL = "http://api.factoriomods.com/mods";

	private final JsonParser parser;

	public FactorioModsMediator(JsonParser initParser) {
		parser = initParser;
	}

	public List<String> search(String query) throws IOException {
		GetRequest request = Unirest.get(LIST_URL);
		if (!StringUtils.isEmpty(query)) {
			request.queryString("q", query);
		}

		List<String> titles = new LinkedList<String>();
		for (Object listingObj : parser.parse(request.getUrl()).getArray()) {
			JSONObject listing = (JSONObject) listingObj;
			titles.add(listing.getString("title"));
		}
		return titles;
	}
}
