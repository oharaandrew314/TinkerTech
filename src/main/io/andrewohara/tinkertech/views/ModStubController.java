package io.andrewohara.tinkertech.views;

import java.io.IOException;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.loaders.Downloader;
import io.andrewohara.tinkertech.mediators.FactorioModsMediator;
import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.Mod;
import io.andrewohara.tinkertech.models.ModStub;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ModStubController extends Controller {
	
	private final Config config;
	private final ErrorHandler errorHandler;
	private final ModStub modStub;
	private final FactorioModsMediator factorioModsMediator;
	private final Downloader downloader;
	
	@FXML Label title, description;
	@FXML Text author, version;
	
	public ModStubController(Config config, ErrorHandler errorHandler, FactorioModsMediator factorioModsMediator, Downloader downloader, ModStub modStub) {
		this.config = config;
		this.errorHandler = errorHandler;
		this.modStub = modStub;
		this.factorioModsMediator = factorioModsMediator;
		this.downloader = downloader;
	}
	
	@Override
 	protected void init() {
		title.setText(modStub.getTitle());
		author.setText(modStub.getAuthor());
		version.setText("v" + modStub.getVersion());
		description.setText(modStub.getDescription());
	}
	
	@FXML protected void handleUpdate(ActionEvent event) {
		try {
			if (modStub instanceof Mod) {
				Mod mod = (Mod) modStub;
				Listing listing = factorioModsMediator.getListing(mod);
				downloader.download(listing);
			}
		} catch (IOException e) {
			errorHandler.handleError(e);
		}
	}
	
	@FXML protected void handleDelete(ActionEvent event) {
		try {
			if (modStub instanceof Mod) {
				((Mod)modStub).delete(config);
			}
		} catch (IOException e) {
			errorHandler.handleError(e);
		}
	}
}
