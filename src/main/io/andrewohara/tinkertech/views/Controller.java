package io.andrewohara.tinkertech.views;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {
	
	@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Platform.isFxApplicationThread()) {
            init();
        } else {
            Platform.runLater(this::init);
        }
    }
	
	protected abstract void init();
}
