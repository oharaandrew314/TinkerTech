package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.net.URL;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.loaders.ModLoader;
import io.andrewohara.tinkertech.mediators.FactorioModsMediator;
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
	
	public Node loadModStubView(ModStub modStub) throws IOException {
			FXMLLoader loader = load("modStub");
			loader.setController(new ModStubController(config, errorHandler, factorioModsMediator, downloader, modStub));
			return loader.load();
	}
	
	public Parent loadModsPane() throws IOException {
		FXMLLoader loader = load("modsPane");
		loader.setController(new ModsPaneController((mod) -> {
			try {
				return loadModStubView(mod);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}, modLoader, errorHandler));
		return loader.load();
	}
	
	private FXMLLoader load(String fileName) {
		URL url = ViewLoader.class.getClassLoader().getResource("io/andrewohara/tinkertech/views/" + fileName + ".fxml");
		return new FXMLLoader(url);
	}
}
