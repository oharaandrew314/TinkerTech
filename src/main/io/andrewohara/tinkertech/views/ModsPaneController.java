package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.loaders.DownloadNotSupportedException;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.loaders.ModLoader;
import io.andrewohara.tinkertech.mediators.Mediator;
import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.Mod;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ModsPaneController extends Controller {

	private final Config config;
	private final ModLoader modLoader;
	private final ErrorHandler errorHandler;
	private final Mediator mediator;
	private final Downloader downloader;
	private final ModStubCellFactory<Mod> modStubCellFactory;

	private final WatchService directoryWatcher;

	@FXML ListView<Mod> modsList;

	@Inject
	protected ModsPaneController(
			Config config, ModLoader modLoader, ErrorHandler errorHandler, Mediator mediator, Downloader downloader,
			WatchService directoryWatcher, ModStubCellFactory<Mod> modStubCellFactory) {
		this.config = config;
		this.modLoader = modLoader;
		this.errorHandler = errorHandler;
		this.mediator = mediator;
		this.downloader = downloader;
		this.directoryWatcher = directoryWatcher;
		this.modStubCellFactory = modStubCellFactory;
	}

	private void refresh() {
		try {
			modsList.getItems().setAll(modLoader.listMods().collect(Collectors.toList()));
		} catch (IOException e) {
			errorHandler.handleError(e);
		}
	}

	@Override
	protected void init() {
		modsList.setCellFactory(modStubCellFactory);

		try {
			Path modsPath = config.getModsPath();
			modsPath.register(
					directoryWatcher,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE
					);
		} catch (IOException e) {
			errorHandler.handleError(e);
		}

		// Begin a thread to wait for changes in the mods folder
		// If the directory changes, the mods list will be reloaded
		new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
						WatchKey key = directoryWatcher.take();
						key.pollEvents();
						Platform.runLater(() -> { refresh(); });
						key.reset();
					} catch (InterruptedException e) {
						// Do nothing
					}
				}
			}
		}.start();

		refresh();
	}

	@FXML protected void handleUpdate(ActionEvent event) {
		try {
			Listing listing = mediator.getListing(getSelected());
			downloader.download(listing);
		} catch (DownloadNotSupportedException e) {
			errorHandler.handleError(e);
		}
	}

	@FXML protected void handleDelete(ActionEvent event) {
		try {
			getSelected().delete();
		} catch (IOException e) {
			errorHandler.handleError(e);
		}
	}

	private Mod getSelected() {
		return modsList.getSelectionModel().getSelectedItem();
	}
}
