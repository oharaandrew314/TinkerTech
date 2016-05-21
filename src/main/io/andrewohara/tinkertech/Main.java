package io.andrewohara.tinkertech;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.loaders.ModLoader;
import io.andrewohara.tinkertech.mediators.FactorioModsMediator;
import io.andrewohara.tinkertech.mediators.UnirestWebClient;
import io.andrewohara.tinkertech.mediators.WebClient;
import io.andrewohara.tinkertech.views.ErrorHandler;
import io.andrewohara.tinkertech.views.ViewLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static final String APP_NAME = "Tinker Tech - Factorio Mod Manager";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Config config = new Config() {
			@Override public Path getDownloadPath() { return Paths.get("C:/users/Andrew/Desktop/temp"); }
			@Override public Path getModsPath() { return Paths.get("C:/users/Andrew/Desktop/mods"); }
		};
		
		WebClient webClient = new UnirestWebClient();
		ErrorHandler errorHandler = new ErrorHandler();
		ModLoader modLoader = new ModLoader(new ErrorHandler(), config);
		FactorioModsMediator factorioModsMediator = new FactorioModsMediator(webClient);
		Downloader downloader = new Downloader(errorHandler, Executors.newSingleThreadExecutor(), config, webClient);
		ViewLoader viewLoader = new ViewLoader(config, errorHandler, modLoader, factorioModsMediator, downloader);
		
		Parent root = viewLoader.loadModsPane();
		
		Scene scene = new Scene(root, 800, 600);
		stage.setTitle(APP_NAME);
		stage.setScene(scene);
		stage.show();
	}
}
