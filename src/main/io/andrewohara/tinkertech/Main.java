package io.andrewohara.tinkertech;

import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import io.andrewohara.tinkertech.views.GuiModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static final String APP_NAME = "Tinker Tech - Factorio Mod Manager";

	public static void main(String[] args) throws IOException, InterruptedException {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Injector injector = Guice.createInjector(new GuiModule(), new MainModule());

		FXMLLoader mainPaneLoader = injector.getInstance(Key.get(FXMLLoader.class, Names.named("mainPane")));

		Scene scene = new Scene(mainPaneLoader.load(), 800, 600);
		stage.setTitle(APP_NAME);
		stage.setScene(scene);
		stage.show();
	}
}
