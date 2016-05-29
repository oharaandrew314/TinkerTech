package io.andrewohara.tinkertech.views;

import java.util.Optional;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public class CustomPropertyItem<T> implements PropertySheet.Item {

	private final String category, name, description;
	private final ObjectProperty<T> property;
	private Class<? extends PropertyEditor<T>> propertyEditor;

	private boolean required = false;

	public CustomPropertyItem(String key, String description, T value) {
		String[] keyParts = key.split("#");
		category = keyParts[0];
		name = keyParts[1];
		this.description = description;
		property = new SimpleObjectProperty<T>(value);
	}

	@Override
	public Class<?> getType() {
		return getValue().getClass();
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public T getValue() {
		return property.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		property.set((T) value);
	}

	@Override
	public Optional<ObservableValue<? extends Object>> getObservableValue() {
		return Optional.of(property);
	}

	public void setPropertyEditorClass(Class<? extends PropertyEditor<T>> propertyEditor) {
		this.propertyEditor = propertyEditor;
	}

	@Override
	public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
		return Optional.ofNullable(propertyEditor);
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isRequired() {
		return required;
	}
}
