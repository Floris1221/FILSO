package com.ale.filso.views.components.customField;

import com.ale.filso.views.components.CustomView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;

public class CustomNumberField extends NumberField {

    public CustomNumberField(String key){
        super(key);
        addListeners();
    }

    private void addListeners() {
        this.addInputListener(event -> {
            setParentViewDataModified(this);
        });
    }


    private void setParentViewDataModified(Component component) {
        Component parent = component.getParent().orElse(null);
        if (parent instanceof CustomView) ((CustomView) parent).setModified(true);
        else setParentViewDataModified(parent);
    }
}
