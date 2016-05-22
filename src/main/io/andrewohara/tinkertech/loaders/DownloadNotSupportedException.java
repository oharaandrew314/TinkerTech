package io.andrewohara.tinkertech.loaders;

import io.andrewohara.tinkertech.models.ModStub;

@SuppressWarnings("serial")
public class DownloadNotSupportedException extends Exception {
	
	public DownloadNotSupportedException(ModStub modStub) {
		this(modStub, null);
	}
	
	public DownloadNotSupportedException(ModStub modStub, Exception cause) {
		super(modStub + " cannot be downloaded.  The author has not provided a suitable download link.", cause);
	}
}
