package io.andrewohara.tinkertech.views;

import java.net.URL;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import javafx.fxml.FXMLLoader;

public class GuiModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@Named("mainPane")
	public FXMLLoader provideMainPane(Injector injector) {
		FXMLLoader loader = load("mainPane");
		loader.setControllerFactory(injector::getInstance);
		return loader;
	}

	private FXMLLoader load(String fileName) {
		String filePath = getClass().getPackage().getName().replace('.', '/') + '/' + fileName + ".fxml";
		URL url = getClass().getClassLoader().getResource(filePath);
		return new FXMLLoader(url);
	}
}
