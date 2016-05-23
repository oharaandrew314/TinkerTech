package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.net.URL;
import java.util.function.Function;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import io.andrewohara.tinkertech.models.ModStub;
import javafx.fxml.FXMLLoader;

public class GuiModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@Named("mainPane")
	public FXMLLoader provideMainPane(MainPaneController mainPaneController) {
		FXMLLoader loader = load("mainPane");
		loader.setController(mainPaneController);
		return loader;
	}

	@Provides
	@Named("modsPane")
	public FXMLLoader provideModsPane(ModsPaneController modsPaneController) throws IOException {
		FXMLLoader loader = load("modsPane");
		loader.setController(modsPaneController);
		return loader;
	}

	@Provides
	@Named("listingPane")
	public FXMLLoader provideListingPane(ListingPaneController listingPaneController) throws IOException {
		FXMLLoader loader = load("listingPane");
		loader.setController(listingPaneController);
		return loader;
	}

	@Provides
	public Function<ModStub, FXMLLoader> modStubViewSupplier() {
		return modStub -> {
			FXMLLoader loader = load("modStub");
			loader.setController(new ModStubController(modStub));
			return loader;
		};
	}

	private FXMLLoader load(String fileName) {
		String filePath = getClass().getPackage().getName().replace('.', '/') + '/' + fileName + ".fxml";
		URL url = getClass().getClassLoader().getResource(filePath);
		return new FXMLLoader(url);
	}
}
