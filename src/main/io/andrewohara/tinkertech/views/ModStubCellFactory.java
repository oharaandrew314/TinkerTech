package io.andrewohara.tinkertech.views;

import java.io.IOException;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.models.ModStub;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ModStubCellFactory<T extends ModStub> implements Callback<ListView<T>, ListCell<T>> {

	private final ErrorHandler errorHandler;

	@Inject
	public ModStubCellFactory(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
	public ListCell<T> call(ListView<T> param) {
		return new ListCell<T>() {
			private ModStubController controller;
			private Node node;

			{
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("modStub.fxml"));
					node = loader.load();
					controller = loader.getController();
				} catch (IOException e) {
					errorHandler.handleError(e);
					throw new RuntimeException(e);
				}
			}

			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					controller.setModStub(item);
					setGraphic(node);
				}
			}
		};
	}
}
