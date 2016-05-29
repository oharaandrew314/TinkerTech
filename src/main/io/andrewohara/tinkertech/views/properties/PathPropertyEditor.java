package io.andrewohara.tinkertech.views.properties;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.property.editor.PropertyEditor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;

import io.andrewohara.tinkertech.views.CustomPropertyItem;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;

public class PathPropertyEditor implements PropertyEditor<Path> {

	private final ValidationSupport validation = new ValidationSupport();

	private final HBox root = new HBox();
	private final TextField pathPreview = new TextField();

	private final DirectoryChooser chooser = new DirectoryChooser();
	private final CustomPropertyItem<Path> item;

	private Path path;

	@SuppressWarnings("unchecked")
	public PathPropertyEditor(Item item) {
		pathPreview.setPrefWidth(300);
		Button button = new Button("...");
		button.setOnAction(this::handleChoose);
		root.getChildren().addAll(pathPreview, button);

		validation.registerValidator(pathPreview, Validator.createEmptyValidator("Path is required"));
		validation.setValidationDecorator(new GraphicValidationDecoration());

		this.item = (CustomPropertyItem<Path>) item;
		setValue((Path) item.getValue());
	}

	@Override
	public Node getEditor() {
		return root;
	}

	@Override
	public Path getValue() {
		return path;
	}

	@Override
	public void setValue(Path value) {
		this.path = value;
		if (Files.exists(value)) {
			chooser.setInitialDirectory(value.toFile());
			pathPreview.setText(value.toString());
		}
	}

	public void handleChoose(ActionEvent event) {
		File newFile = chooser.showDialog(null);
		if (newFile != null) {
			Path newPath = newFile.toPath();
			if (Files.exists(newPath)) {
				setValue(newPath);
				item.setValue(newPath);
			}
		}
	}

	public void setTitle(String title) {
		chooser.setTitle(title);
	}
}
