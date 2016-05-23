package io.andrewohara.tinkertech.views;

import io.andrewohara.tinkertech.models.ModStub;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ModStubController extends Controller {

	private final ModStub modStub;

	@FXML Label title, description;
	@FXML Text author, version;

	public ModStubController(ModStub modStub) {
		this.modStub = modStub;
	}

	@Override
	protected void init() {
		title.setText(modStub.getTitle());
		author.setText(modStub.getAuthor());
		version.setText("v" + modStub.getVersion());
		description.setText(modStub.getDescription());
	}
}
