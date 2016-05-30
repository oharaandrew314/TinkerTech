package io.andrewohara.tinkertech.loaders;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.andrewohara.tinkertech.mediators.WebClient;
import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.TestLoader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public class DownloaderUnitTest {

	private final IMocksControl mocks = EasyMock.createControl();

	private static final ObservableValue<Path> path = new SimpleObjectProperty<Path>(Paths.get("/"));

	private final Executor executor = mocks.createMock(Executor.class);
	private final WebClient webClient = mocks.createMock(WebClient.class);
	private final Downloader testObj = new Downloader(null, executor, webClient, path, path);
	private final Listing listing = TestLoader.loadListing("AdvancedEquipment");

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		mocks.reset();
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
