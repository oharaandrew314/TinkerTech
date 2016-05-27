package io.andrewohara.tinkertech;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;

import io.andrewohara.tinkertech.models.Version;
import io.andrewohara.tinkertech.views.ErrorHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends GuiceApplication {

	public static final String
	APP_SLUG = "TinkerTech",
	APP_NAME = "Tinker Tech - Factorio Mod Manager",
	AUTHOR = "Andrew O'Hara";

	public static final Version VERSION = Version.valueOf("0.1.0");

	public static final URL HELP_URL, MAIL_URL;
	static {
		try {
			HELP_URL = new URL("https://github.com/oharaandrew314/TinkerTech/wiki");
			MAIL_URL = new URL("mailto:tinkertech@andrewohara.io");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Inject ErrorHandler errorHandler;
	@Inject private GuiceFXMLLoader fxmlLoader;
	@Inject Set<StartupTask> startupTasks;
	@Inject Set<ShutdownTask> shutdownTasks;

	@Override
	public void init(List<Module> modules) throws Exception {
		modules.add(new MainModule());
		modules.add(OSModule.create());
	}

	@Override
	public void start(Stage stage) throws Exception {
		startupTasks.forEach(task -> {
			try {
				task.startup();
			} catch (Exception e) {
				errorHandler.handleError(e);
			}
		});

		Parent parent = fxmlLoader.load(getClass().getResource("views/mainPane.fxml")).getRoot();
		Scene scene = new Scene(parent, 800, 600);
		stage.setTitle(APP_NAME);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		shutdownTasks.forEach(task -> {
			try {
				task.shutdown();
			} catch (Exception e) {
				errorHandler.handleError(e);
			}
		});

		super.stop();
		System.exit(0);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		launch(args);
	}
}
