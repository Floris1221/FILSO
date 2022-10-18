package com.ale.filso.views.components.customField;

import com.ale.filso.models.Dictionary.Dictionary;
import com.ale.filso.models.Dictionary.DictionaryGroup;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;
import java.util.function.Consumer;

public class BufferedEntityFiltering {

    public BufferedEntityFiltering() {
    }

    public Component createTextFilterHeader(Consumer<String> filterChangeConsumer) {
/*        if (labelText!=null) {  // without label
            Label label = new Label(labelText);
            label.getStyle().set("padding-top", "var(--lumo-space-m)")
                    .set("font-size", "var(--lumo-font-size-xs)");
        }*/
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        textField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));

        return layout;
    }

    public Component createComboFilterHeaderDictionaryGroup(
            Consumer<String> filterChangeConsumer, List<DictionaryGroup> dictionaryGroups) {
  /*      if (labelText!=null) {  // without label
            Label label = new Label(labelText);
            label.getStyle().set("padding-top", "var(--lumo-space-m)")
                    .set("font-size", "var(--lumo-font-size-xs)");
        }*/
        ComboBox<DictionaryGroup> comboBox = new ComboBox<>();
        comboBox.setItems(dictionaryGroups);
        comboBox.setItemLabelGenerator(DictionaryGroup::getName);
        comboBox.setRequired(false);
        comboBox.setClearButtonVisible(true);
        comboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        comboBox.setWidthFull();
        comboBox.getStyle().set("max-width", "100%");

        comboBox.addValueChangeListener(                event -> {
            String value = new String("");
            if (event.getValue()!=null)
                value = event.getValue().getName();
            filterChangeConsumer.accept(value);
        });

/*        comboBox.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<DictionaryGroup>, DictionaryGroup>>)
                event -> {
                    String value = new String("");
                    if (event.getValue()!=null)
                        value = event.getValue().getName();
                    filterChangeConsumer.accept(value);
                });    */
        VerticalLayout layout = new VerticalLayout(comboBox);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");

        return layout;
    }

    public Component createComboFilterHeaderDictionary(
            Consumer<String> filterChangeConsumer, List<Dictionary> dictionaries) {
  /*      if (labelText!=null) {  // without label
            Label label = new Label(labelText);
            label.getStyle().set("padding-top", "var(--lumo-space-m)")
                    .set("font-size", "var(--lumo-font-size-xs)");
        }*/
        ComboBox<Dictionary> comboBox = new ComboBox<>();
        comboBox.setItems(dictionaries);
        comboBox.setItemLabelGenerator(Dictionary::getName);
        comboBox.setRequired(false);
        comboBox.setClearButtonVisible(true);
        comboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        comboBox.setWidthFull();
        comboBox.getStyle().set("max-width", "100%");

        comboBox.addValueChangeListener(                event -> {
            String value = new String("");
            if (event.getValue()!=null)
                value = event.getValue().getName();
            filterChangeConsumer.accept(value);
        });

/*        comboBox.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<DictionaryGroup>, DictionaryGroup>>)
                event -> {
                    String value = new String("");
                    if (event.getValue()!=null)
                        value = event.getValue().getName();
                    filterChangeConsumer.accept(value);
                });    */
        VerticalLayout layout = new VerticalLayout(comboBox);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");

        return layout;
    }
}
