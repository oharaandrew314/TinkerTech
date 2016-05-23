package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.loaders.DownloadNotSupportedException;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.mediators.Mediator;
import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.ModStub;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ListingPaneController {

	@FXML TextField searchField;
	@FXML ListView<Node> listings;

	private final Mediator mediator;
	private final ErrorHandler errorHandler;
	private final Function<ModStub, FXMLLoader> stubSupplier;
	private final Downloader downloader;

	private Map<Node, Listing> backrefs = new HashMap<>();

	@Inject
	protected ListingPaneController(Mediator mediator, ErrorHandler errorHandler, Downloader downloader, Function<ModStub, FXMLLoader> stubSupplier) {
		this.mediator = mediator;
		this.errorHandler = errorHandler;
		this.downloader = downloader;
		this.stubSupplier = stubSupplier;
	}

	@FXML protected void handleSearch(ActionEvent event) {
		listings.getItems().clear();
		backrefs.clear();
		try {
			String query = searchField.getText();
			mediator.search(query)
			.map(listing -> {
				try {
					Node node = stubSupplier.apply(listing).load();
					backrefs.put(node, listing);
					return node;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}).forEach(node -> {
				listings.getItems().add(node);
			});
		} catch(IOException e) {
			errorHandler.handleError(e);
		}
	}

	@FXML protected void handleDownload(ActionEvent event) {
		Node selected = listings.getSelectionModel().getSelectedItem();
		Listing listing = backrefs.get(selected);

		try {
			downloader.download(listing);
		} catch (DownloadNotSupportedException e) {
			errorHandler.handleError(e);
		}
	}
}
