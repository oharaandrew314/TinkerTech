package io.andrewohara.tinkertech.views;

import java.io.IOException;
import java.util.function.Function;

import io.andrewohara.tinkertech.loaders.ModLoader;
import io.andrewohara.tinkertech.models.Mod;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;

public class ModsPaneController extends Controller {
	
	private final Function<Mod, Node> modViewLoader;
	private final ModLoader modLoader;
	private final ErrorHandler errorHandler;
	
	@FXML ListView<Node> modsList;
	
	public ModsPaneController(Function<Mod, Node> modViewLoader, ModLoader modLoader, ErrorHandler errorHandler) {
		this.modViewLoader = modViewLoader;
		this.modLoader = modLoader;
		this.errorHandler = errorHandler;
	}

	@Override
	protected void init() {
		try {
			modLoader
				.listMods()
				.map(mod -> { return modViewLoader.apply(mod); })
				.forEach(node -> { modsList.getItems().add(node); });
		} catch (IOException e) {
			errorHandler.handleError(e);
		}
	}
}
