package io.andrewohara.tinkertech.services;

public interface DirectoryWatchService {
	void addListener(Runnable listener);
}
