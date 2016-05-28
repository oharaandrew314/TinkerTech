package io.andrewohara.tinkertech.services;

import com.google.common.util.concurrent.Service;

public interface DirectoryWatchService extends Service {
	void addListener(Runnable listener);
}
