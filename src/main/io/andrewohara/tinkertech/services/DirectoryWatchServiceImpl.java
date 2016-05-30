package io.andrewohara.tinkertech.services;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import io.andrewohara.tinkertech.views.ErrorHandler;
import javafx.beans.value.ObservableValue;

@Singleton
public class DirectoryWatchServiceImpl extends AbstractExecutionThreadService implements DirectoryWatchService {

	private final ObservableValue<Path> modsPath;
	private final ErrorHandler errorHandler;

	private Optional<WatchService> watchService = Optional.empty();
	private final List<Runnable> listeners = new LinkedList<>();

	@Inject
	protected DirectoryWatchServiceImpl(@Named("modsPath") ObservableValue<Path> modsPath, ErrorHandler errorHandler) {
		this.modsPath = modsPath;
		this.errorHandler = errorHandler;
	}

	@Override
	protected void startUp() throws IOException {
		modsPath.addListener((prop, oldValue, newValue) -> {
			try {
				register();
			} catch (IOException e) {
				errorHandler.handleError(e);
			}
		});

		register();
	}

	private void register() throws IOException {
		close();

		if (Files.exists(modsPath.getValue())) {
			watchService = Optional.of(FileSystems.getDefault().newWatchService());
			modsPath.getValue().register(
					watchService.get(),
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE
					);
		}
	}

	private void close() throws IOException {
		if (watchService.isPresent()) {
			watchService.get().close();
		}
	}

	@Override
	public void run() {
		try {
			while(true) {
				try {
					WatchKey key = watchService.get().take();
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
	protected void triggerShutdown() {
		try {
			close();
		} catch (IOException e) {
			errorHandler.handleError(e);
		}
	}
}
