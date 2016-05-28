package io.andrewohara.tinkertech.services;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.LinkedList;
import java.util.List;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.andrewohara.tinkertech.config.Config;

@Singleton
public class DirectoryWatchServiceImpl extends AbstractExecutionThreadService implements DirectoryWatchService {

	private final Config config;

	private WatchService watchService;
	private final List<Runnable> listeners = new LinkedList<>();

	@Inject
	protected DirectoryWatchServiceImpl(Config config) {
		this.config = config;
	}

	@Override
	protected void startUp() throws IOException {
		watchService = FileSystems.getDefault().newWatchService();
		config.getModsPath().register(
				watchService,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_MODIFY,
				StandardWatchEventKinds.ENTRY_DELETE
				);
	}

	@Override
	public void run() {
		try {
			while(true) {
				try {
					WatchKey key = watchService.take();
					key.pollEvents();
					listeners.forEach(Runnable::run);
					key.reset();
				} catch (InterruptedException e) {
					// Do nothing
				}
			}
		} catch (ClosedWatchServiceException e) {
			// Do nothing
		}
	}

	@Override
	public void addListener(Runnable listener) {
		listeners.add(listener);
	}

	@Override
	public void shutDown() throws IOException {
		if (watchService != null) {
			watchService.close();
		}
	}
}
