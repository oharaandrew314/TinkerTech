package io.andrewohara.tinkertech.mediators;

import java.io.IOException;
import java.io.InputStream;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.Release;

import static org.easymock.EasyMock.*;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.*;

public class FactorioModsMediatorDownloadTest {
	
	private IMocksControl mocks = EasyMock.createControl();
	
	private FactorioModsMediator testObj;
	
	private WebClient webClient;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup() {
		webClient = mocks.createMock(WebClient.class);
		testObj = new FactorioModsMediator(webClient);
	}

	@Test
	public void FactorioModsMediator_download_noDownloadUrl() throws IOException {
		String url = null;
		Listing listing = new Listing(null){
			@Override
			public Release getLatestRelease() {
				return new Release(null, null){
					@Override public String getDownloadUrl() { return null; }
				};
			}
		};
		
		// Set expectations
		expect(webClient.getZipStream(url)).andThrow(new IOException());
		thrown.expect(IOException.class);
		
		mocks.replay();
		testObj.download(listing);
		mocks.verify();
	}
	
	@Test
	public void FactorioModsMediator_download() throws IOException {
		String url = "url";
		
		Listing listing = new Listing(null) {
			@Override public Release getLatestRelease() {
				return new Release(null, null) {
					@Override public String getDownloadUrl() { return url; }
				};
			}
		};
		
		// Set expectations
		InputStream inputStream = mocks.createMock(InputStream.class);
		expect(webClient.getZipStream(url)).andReturn(inputStream);
		
		mocks.replay();
		assertThat(testObj.download(listing), is(inputStream));
		mocks.verify();
	}
}
