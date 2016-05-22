package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.net.URL;
import java.util.function.Function;
import java.util.function.Supplier;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.loaders.ModLoader;
import io.andrewohara.tinkertech.mediators.FactorioModsMediator;
import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.Mod;
import io.andrewohara.tinkertech.models.ModStub;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public class ViewLoader {
	
	private final Config config;
	private final ErrorHandler errorHandler;
	private final ModLoader modLoader;
	private final FactorioModsMediator factorioModsMediator;
	private final Downloader downloader;
	
	public ViewLoader(Config config, ErrorHandler errorHandler, ModLoader modLoader, FactorioModsMediator factorioModsMediaitor, Downloader downloader) {
		this.config = config;
		this.errorHandler = errorHandler;
		this.modLoader = modLoader;
		this.factorioModsMediator = factorioModsMediaitor;
		this.downloader = downloader;
	}
	
	public Parent loadMainPane() throws IOException {
		FXMLLoader loader = load("mainPane");
		
		Supplier<Parent> listingPaneSupplier = () -> {
			try {
				return loadListingPane();
			} catch (Exception e) {
				errorHandler.handleError(e);
				throw new RuntimeException(e);
			}
		};
		
		Supplier<Parent> modsPaneSupplier = () -> {
			try {
				return loadModsPane();
			} catch (Exception e) {
				errorHandler.handleError(e);
				throw new RuntimeException(e);
			}
		};
		
		loader.setController(new MainPaneController(listingPaneSupplier, modsPaneSupplier));
		return loader.load();
	}
	
	public Node loadModStubView(ModStub modStub) throws IOException {
			FXMLLoader loader = load("modStub");
			loader.setController(new ModStubController(modStub));
			return loader.load();
	}
	
	public Parent loadModsPane() throws IOException {
		FXMLLoader loader = load("modsPane");
		Function<Mod, Node> modNodeFunction = (mod) -> {
			try {
				return loadModStubView(mod);
			} catch (IOException e) {
				errorHandler.handleError(e);
				throw new RuntimeException(e);
			}
		};
		loader.setController(new ModsPaneController(config, modNodeFunction, modLoader, errorHandler, factorioModsMediator, downloader));
		return loader.load();
	}
	
	public Parent loadListingPane() throws IOException {
		FXMLLoader loader = load("listingPane");
		Function<Listing, Node> listingViewSupplier = listing -> {
			try {
				return loadModStubView(listing);
			} catch (IOException e) {
				errorHandler.handleError(e);
				return null;
			}
		};
		
		loader.setController(new ListingPaneController(factorioModsMediator, errorHandler, downloader, listingViewSupplier));
		return loader.load();
	}
	
	private FXMLLoader load(String fileName) {
		URL url = ViewLoader.class.getClassLoader().getResource("io/andrewohara/tinkertech/views/" + fileName + ".fxml");
		return new FXMLLoader(url);
	}
}
