package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.loaders.DownloadNotSupportedException;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.loaders.ModLoader;
import io.andrewohara.tinkertech.mediators.Mediator;
import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.Mod;
import javafx.application.Platform;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ModsPaneController extends Controller {

	private final ModLoader modLoader;
	private final ErrorHandler errorHandler;
	private final Mediator mediator;
	private final Downloader downloader;
	private final ModStubCellFactory<Mod> modStubCellFactory;

	@FXML ListView<Mod> modsList;

	@Inject
	protected ModsPaneController(
			ModLoader modLoader, ErrorHandler errorHandler, Mediator mediator, Downloader downloader,
			ModStubCellFactory<Mod> modStubCellFactory) {
		this.modLoader = modLoader;
		this.errorHandler = errorHandler;
		this.mediator = mediator;
		this.downloader = downloader;
		this.modStubCellFactory = modStubCellFactory;
	}

	@Override
	protected void init() {
		modsList.setCellFactory(modStubCellFactory);

		// Listen for modLoader changes
		modLoader.mods().addListener((Change<? extends Mod> change) -> {
			Platform.runLater(() -> {
				modsList.getItems().clear();
				modsList.getItems().addAll(change.getList());
			});
		});

		// Load initial mods from modLoader
		try {
			modsList.getItems().setAll(modLoader.listMods().collect(Collectors.toList()));
		} catch (IOException e) {
			errorHandler.handleError(e);
		}
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
