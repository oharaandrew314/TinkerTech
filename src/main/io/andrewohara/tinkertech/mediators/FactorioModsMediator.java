package io.andrewohara.tinkertech.mediators;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;

import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.Mod;

public class FactorioModsMediator {

	public static final String LIST_URL = "http://api.factoriomods.com/mods";

	private final WebClient webClient;

	public FactorioModsMediator(WebClient webClient) {
		this.webClient = webClient;
	}

	public List<String> search(String query) throws IOException {
		GetRequest request = Unirest.get(LIST_URL);
		if (!StringUtils.isEmpty(query)) {
			request.queryString("q", query);
		}

		List<String> titles = new LinkedList<String>();
		for (Object listingObj : webClient.getJsonArray(request.getUrl())) {
			JSONObject listing = (JSONObject) listingObj;
			titles.add(listing.getString("title"));
		}
		return titles;
	}
	
	public Stream<Listing> search2(String query) throws IOException {
		GetRequest request = Unirest.get(LIST_URL);
		if (!StringUtils.isEmpty(query)) {
			request.queryString("q", query);
		}
		
		JSONArray jsonArray = webClient.getJsonArray(request.getUrl());
		return StreamSupport.stream(jsonArray.spliterator(), false)
				.map(listingObject -> new Listing((JSONObject) listingObject));
	}
	
	public InputStream download(Listing listing) throws IOException {
		return webClient.getZipStream(listing.getLatestRelease().getDownloadUrl());
	}
	
	public Listing getListing(Mod mod) {
		try {
			String url = LIST_URL + "/" + URLEncoder.encode(mod.getName(), "UTF-8");
			return new Listing(webClient.getJsonObject(url));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
