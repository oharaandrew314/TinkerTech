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

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.views.ErrorHandler;

public class DirectoryWatchServiceImpl extends Thread implements DirectoryWatchService {

	private final WatchService watchService;
	private final List<Runnable> listeners = new LinkedList<>();

	@Inject
	protected DirectoryWatchServiceImpl(ErrorHandler errorHandler, Config config) {
		try {
			watchService = FileSystems.getDefault().newWatchService();

			config.getModsPath().register(
					watchService,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE
					);
		} catch (IOException e) {
			errorHandler.handleError(e);
			throw new RuntimeException(e);
		}
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
	public void cancel() throws IOException {
		watchService.close();
	}
}
