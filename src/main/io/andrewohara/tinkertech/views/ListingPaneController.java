package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.andrewohara.tinkertech.loaders.DownloadNotSupportedException;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.mediators.FactorioModsMediator;
import io.andrewohara.tinkertech.models.Listing;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ListingPaneController {
	
	@FXML TextField searchField;
	@FXML ListView<Node> listings;
	
	private final FactorioModsMediator mediator;
	private final ErrorHandler errorHandler;
	private final Function<Listing, Node> stubSupplier;
	private final Downloader downloader;
	
	private Map<Node, Listing> backrefs = new HashMap<>();
	
	public ListingPaneController(FactorioModsMediator mediator, ErrorHandler errorHandler, Downloader downloader, Function<Listing, Node> stubSupplier) {
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
					Node node = stubSupplier.apply(listing);
					backrefs.put(node, listing);
					return node;
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
