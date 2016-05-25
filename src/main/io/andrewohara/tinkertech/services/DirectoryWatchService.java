package io.andrewohara.tinkertech.services;

import java.io.IOException;

public interface DirectoryWatchService {

	void addListener(Runnable listener);
	void cancel() throws IOException;
}
