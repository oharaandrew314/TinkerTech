package io.andrewohara.tinkertech.views;

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPane.fxml"));
		loader.setControllerFactory(injector::getInstance);
		return loader;
	}
}
