package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.loaders.DownloadNotSupportedException;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.loaders.ModLoader;
import io.andrewohara.tinkertech.mediators.FactorioModsMediator;
import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.Mod;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;

public class ModsPaneController extends Controller {
	
	private final Config config;
	private final Function<Mod, Node> modViewLoader;
	private final ModLoader modLoader;
	private final ErrorHandler errorHandler;
	private final FactorioModsMediator mediator;
	private final Downloader downloader;
	
	private final WatchService directoryWatcher;
	private final Map<Node, Mod> backrefs = new HashMap<>();
	
	@FXML ListView<Node> modsList;
	
	public ModsPaneController(Config config, Function<Mod, Node> modViewLoader, ModLoader modLoader, ErrorHandler errorHandler, FactorioModsMediator mediator, Downloader downloader) throws IOException {
		this.config = config;
		this.modViewLoader = modViewLoader;
		this.modLoader = modLoader;
		this.errorHandler = errorHandler;
		this.mediator = mediator;
		this.downloader = downloader;
		
		directoryWatcher = FileSystems.getDefault().newWatchService();
	}
	
	private void refresh() {
		backrefs.clear();
		modsList.getItems().clear();
		try {
			modLoader
				.listMods()
				.map(mod -> {
					Node node = modViewLoader.apply(mod);
					backrefs.put(node, mod);
					return node;
					})
				.forEach(node -> {modsList.getItems().add(node); });
		} catch (IOException e) {
			errorHandler.handleError(e);
		}
	}

	@Override
	protected void init() {
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
			getSelected().delete(config);
		} catch (IOException e) {
			errorHandler.handleError(e);
		}
	}
	
	private Mod getSelected() {
		Node selectedNode = modsList.getSelectionModel().getSelectedItem();
		return backrefs.get(selectedNode);
	}
}
