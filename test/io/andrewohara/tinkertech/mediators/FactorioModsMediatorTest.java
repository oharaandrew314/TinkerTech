package io.andrewohara.tinkertech.mediators;

import static io.andrewohara.tinkertech.mediators.FactorioModsMediator.LIST_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mashape.unirest.http.JsonNode;

@RunWith(Parameterized.class)
public class FactorioModsMediatorTest {

	@Rule
	public ExpectedException thrown= ExpectedException.none();

	private final String searchTerm, expectedUrl, json;
	private final Set<String> expectedTitles;

	@Parameters(name="{index}: Search: {0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{ "nullQuery, emptyResult", null, LIST_URL, "[]", Arrays.asList() },
			{ "emptyQuery, emptyResult", "", LIST_URL, "[]", Arrays.asList() },
			{ "nullQuery, singleResult", null, LIST_URL, "[ { 'title': 'Foo' } ]", Arrays.asList("Foo") },
			{ "nullQuery, twoResults", null, LIST_URL, "[ { 'title': 'Foo' }, { 'title': 'Bar' } ]", Arrays.asList("Foo", "Bar") },
			{ "goodQuery, emptyResult", "foo", LIST_URL + "?q=foo", "[]", Arrays.asList() }
		});
	}

	public FactorioModsMediatorTest(String name, String initSearchTerm, String initExpectedUrl, String initJson, Collection<String> initExpectedTitles) throws IOException {
		searchTerm = initSearchTerm;
		expectedUrl = initExpectedUrl;
		json = initJson;
		expectedTitles = new LinkedHashSet<>(initExpectedTitles);
	}

	@Test(expected=IOException.class)
	public void FactorioModsMediator_search_ioError() throws IOException {
		FactorioModsMediator testObj = new FactorioModsMediator(url -> {
			assertThat(url, is(FactorioModsMediator.LIST_URL));
			throw new IOException();
		} );
		testObj.search(null);
	}

	@Test
	public void performTest() throws IOException {
		FactorioModsMediator testObj = new FactorioModsMediator(url -> {
			assertThat(url, is(expectedUrl));
			return new JsonNode(json);
		});
		assertThat(new LinkedHashSet<String>(testObj.search(searchTerm)), equalTo(expectedTitles));
	}
}
