package io.andrewohara.tinkertech.views;

import java.util.function.Supplier;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

public class MainPaneController extends Controller {
	
	@FXML HBox mainPane;
	
	private final Supplier<Parent> listingPaneSupplier, modsPaneSupplier;
	
	public MainPaneController(Supplier<Parent> listingPaneSupplier, Supplier<Parent> modsPaneSupplier) {
		this.listingPaneSupplier = listingPaneSupplier;
		this.modsPaneSupplier = modsPaneSupplier;
	}

	@Override
	protected void init() {
		mainPane.getChildren().addAll(listingPaneSupplier.get(), modsPaneSupplier.get());
	}
}
