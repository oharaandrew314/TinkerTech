package io.andrewohara.tinkertech.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.concurrent.Executor;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.mediators.WebClient;
import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.TestLoader;

import static org.easymock.EasyMock.*;

public class DownloaderUnitTest {
	
	private final IMocksControl mocks = EasyMock.createControl();
	
	private final Config config = mocks.createMock(Config.class);
	private final Executor executor = mocks.createMock(Executor.class);
	private final WebClient webClient = mocks.createMock(WebClient.class);
	private final Downloader testObj = new Downloader(null, executor, config, webClient);
	private final Listing listing = TestLoader.loadListing("AdvancedEquipment");
	
	@Rule
	public final ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup() {
		mocks.reset();
		expect(config.getDownloadPath()).andStubReturn(Paths.get("/"));
		expect(config.getModsPath()).andStubReturn(Paths.get("/"));
	}
	
	@Test
	public void Downloader_download_nullListing() throws DownloadNotSupportedException {
		Listing listing = null;

		thrown.expect(NullPointerException.class);
		testObj.download(listing);
	}
	
	@Test
	public void Downloader_download_unavailableSize() throws DownloadNotSupportedException, IOException {
		// Set expectations
		InputStream stream = mocks.createMock(InputStream.class);
		expect(stream.available()).andThrow(new IOException());
		expect(webClient.getZipStream(listing.getLatestRelease().getMirrorUrl())).andReturn(stream);
		executor.execute(anyObject(Runnable.class));
		
		mocks.replay();
		testObj.download(listing);
		mocks.verify();
	}
	
	@Test
	public void Downloader_download_validAvailableSize() throws IOException, DownloadNotSupportedException {
		// Set expectations
		InputStream stream = mocks.createMock(InputStream.class);
		expect(stream.available()).andReturn(1024);
		expect(webClient.getZipStream(listing.getLatestRelease().getMirrorUrl())).andReturn(stream);
		executor.execute(anyObject(Runnable.class));
		
		mocks.replay();
		testObj.download(listing);
		mocks.verify();
	}
}
