package io.andrewohara.tinkertech.services;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.andrewohara.tinkertech.ShutdownTask;
import io.andrewohara.tinkertech.StartupTask;
import io.andrewohara.tinkertech.config.Config;

@Singleton
public class DirectoryWatchServiceImpl extends Thread implements DirectoryWatchService, StartupTask, ShutdownTask {

	private final Config config;

	private WatchService watchService;
	private final List<Runnable> listeners = new LinkedList<>();

	@Inject
	protected DirectoryWatchServiceImpl(Config config) {
		this.config = config;
	}

	@Override
	public synchronized void startup() throws IOException {
		watchService = FileSystems.getDefault().newWatchService();
		config.getModsPath().register(
				watchService,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_MODIFY,
				StandardWatchEventKinds.ENTRY_DELETE
				);
		start();
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
	public void shutdown() throws IOException {
		if (watchService != null) {
			watchService.close();
		}
	}
}
