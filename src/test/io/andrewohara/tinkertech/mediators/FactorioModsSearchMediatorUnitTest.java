package io.andrewohara.tinkertech.mediators;

import static io.andrewohara.tinkertech.mediators.FactorioModsMediator.LIST_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.easymock.IMocksControl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.andrewohara.tinkertech.models.Listing;

import static org.easymock.EasyMock.*;

@RunWith(Parameterized.class)
public class FactorioModsSearchMediatorUnitTest {

	@Rule
	public ExpectedException thrown= ExpectedException.none();

	private static final IMocksControl mocks = createControl();
	
	private final FactorioModsMediator testObj;
	private final WebClient webClient;

	private final String searchTerm, expectedUrl, json;
	private final Set<Listing> expectedListings;

	@Parameters(name="{index}: Search: {0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{ "nullQuery, emptyResult", null, LIST_URL, "[]", Arrays.asList() },
			{ "emptyQuery, emptyResult", "", LIST_URL, "[]", Arrays.asList() },
			{ "nullQuery, singleResult", null, LIST_URL, "[ { 'name': 'Foo' } ]", createListings("Foo") },
			{ "nullQuery, twoResults", null, LIST_URL, "[ { 'name': 'Foo' }, { 'name': 'Bar' } ]", createListings("Foo", "Bar") },
			{ "goodQuery, emptyResult", "foo", LIST_URL + "?q=foo", "[]", Arrays.asList() }
		});
	}
	
	private static List<Listing> createListings(String... names) {
		return Arrays.asList(names)
				.stream()
				.map(name -> { return new Listing(new JSONObject("{ 'name': '" + name + "'}")); })
				.collect(Collectors.toList());
	}

	public FactorioModsSearchMediatorUnitTest(String name, String initSearchTerm, String initExpectedUrl, String initJson, Collection<Listing> initExpectedListings) throws IOException {
		searchTerm = initSearchTerm;
		expectedUrl = initExpectedUrl;
		json = initJson;
		expectedListings = new LinkedHashSet<>(initExpectedListings);
		
		mocks.reset();
		webClient = mocks.createMock(WebClient.class);
		testObj = new FactorioModsMediator(webClient);
	}

	@Test(expected=IOException.class)
	public void FactorioModsMediator_search_ioError() throws IOException {
		expect(webClient.getJsonArray(FactorioModsMediator.LIST_URL)).andThrow(new IOException());
		
		mocks.replay();
		testObj.search(null);
		mocks.verify();
	}

	@Test
	public void performTest() throws IOException {
		expect(webClient.getJsonArray(expectedUrl)).andReturn(new JSONArray(json));
		
		mocks.replay();
		assertThat(testObj.search(searchTerm).collect(Collectors.toSet()), equalTo(expectedListings));
		mocks.verify();
	}
}
