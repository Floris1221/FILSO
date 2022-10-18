package com.ale.filso.views.components.customField;

import com.ale.filso.views.components.CustomView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;

public class CustomComboBox<T> extends ComboBox<T> {

    private boolean startUserInteraction=false;

    public CustomComboBox(String key){
        super(key);
        addListeners();
    }

    private void addListeners() {
        this.addAttachListener(event -> startUserInteraction=true);
        this.addSelectedItemChangeListener(event -> { if (startUserInteraction) {
            setParentViewDataModified(this);
        }} );
    }


    private void setParentViewDataModified(Component component) {
        Component parent = component.getParent().orElse(null);
        if (parent instanceof CustomView) ((CustomView) parent).setModified(true);
        else setParentViewDataModified(parent);
    }
}
