package io.andrewohara.tinkertech.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import io.andrewohara.tinkertech.mediators.WebClient;
import io.andrewohara.tinkertech.models.Download;
import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.views.ErrorHandler;
import javafx.beans.value.ObservableValue;

public class Downloader {

	private final ObservableValue<Path> downloadsPath, modsPath;
	private final ErrorHandler errorHandler;
	private final Executor downloadExecutor;
	private final WebClient webClient;

	@Inject
	protected Downloader(
			ErrorHandler errorHandler, Executor downloadExecutor, WebClient webClient,
			@Named("modsPath") ObservableValue<Path> modsPath, @Named("downloadsPath") ObservableValue<Path> downloadsPath) {
		this.errorHandler = errorHandler;
		this.downloadExecutor = downloadExecutor;
		this.webClient = webClient;

		this.downloadsPath = downloadsPath;
		this.modsPath = modsPath;
	}

	public Download download(Listing listing) throws DownloadNotSupportedException {
		InputStream stream = getDownloadStream(listing);

		// Get size of download
		int totalBytes = -1;
		try {
			totalBytes = stream.available();
		} catch (IOException e) {
			// Do nothing.  Use indeterminate totalBytes
		}

		Path downloadPath = downloadsPath.getValue().resolve(listing.getLatestRelease().getFilename());
		Path destinationPath = modsPath.getValue().resolve(listing.getLatestRelease().getFilename());

		RunnableDownload download = new RunnableDownload(listing, stream, downloadPath, destinationPath, totalBytes);
		downloadExecutor.execute(download);
		return download;


	}

	private InputStream getDownloadStream(Listing listing) throws DownloadNotSupportedException {
		IOException exception = null;
		final List<String> urls = Arrays.asList(
				listing.getLatestRelease().getDownloadUrl(),
				listing.getLatestRelease().getMirrorUrl());
		for (String url : urls) {
			if (url != null) {
				try {
					return webClient.getZipStream(url);
				} catch (IOException e) {
					exception = e;
				}
			}
		}

		throw new DownloadNotSupportedException(listing, exception);
	}

	private class RunnableDownload implements Runnable, Download {

		private static final int BUFFER_SIZE = 128;

		private final Listing listing;
		private final InputStream inputStream;
		private final Path tempPath, destPath;
		private final int totalBytes;
		private final byte[] buffer = new byte[BUFFER_SIZE];

		private int currentBytes = 0;
		private boolean complete = false;

		public RunnableDownload(Listing listing, InputStream inputStream, Path tempPath, Path destPath, int totalBytes) {
			this.listing = listing;
			this.inputStream = inputStream;
			this.tempPath = tempPath;
			this.destPath = destPath;
			this.totalBytes = totalBytes;
		}

		@Override
		public double getProgress() {
			return (double) currentBytes / totalBytes;
		}

		@Override
		public boolean isComplete() {
			return complete;
		}

		@Override
		public Listing getListing() {
			return listing;
		}

		@Override
		public int getDownloadSize() {
			return totalBytes;
		}

		@Override
		public void run() {
			try {
				Files.createDirectories(tempPath.getParent());
				try (
						InputStream is = inputStream;
						OutputStream os = Files.newOutputStream(tempPath);
						) {
					while(!complete) {
						int bytesRead = is.read(buffer);
						complete = bytesRead < 1;
						if (!complete) {
							os.write(buffer, 0, bytesRead);
							currentBytes += bytesRead;
						}
					}
					Files.move(tempPath, destPath, StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
				errorHandler.handleError(e);
			}
		}
	}
}
