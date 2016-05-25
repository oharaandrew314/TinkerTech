package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.loaders.DownloadNotSupportedException;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.mediators.Mediator;
import io.andrewohara.tinkertech.models.Listing;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ListingPaneController extends Controller {

	@FXML TextField searchField;
	@FXML ListView<Listing> listings;

	private final Mediator mediator;
	private final ErrorHandler errorHandler;
	private final Downloader downloader;
	private final ModStubCellFactory<Listing> modstubCellFactory;

	@Inject
	protected ListingPaneController(Mediator mediator, ErrorHandler errorHandler, Downloader downloader, ModStubCellFactory<Listing> modStubCellFactory) {
		this.mediator = mediator;
		this.errorHandler = errorHandler;
		this.downloader = downloader;
		this.modstubCellFactory = modStubCellFactory;
	}

	@Override
	protected void init() {
		listings.setCellFactory(modstubCellFactory);
	}

	@FXML protected void handleSearch(ActionEvent event) {
		try {
			String query = searchField.getText();
			listings.getItems().setAll(mediator.search(query).collect(Collectors.toList()));
		} catch(IOException e) {
			errorHandler.handleError(e);
		}
	}

	@FXML protected void handleDownload(ActionEvent event) {
		Listing listing = listings.getSelectionModel().getSelectedItem();

		try {
			downloader.download(listing);
		} catch (DownloadNotSupportedException e) {
			errorHandler.handleError(e);
		}
	}
}
