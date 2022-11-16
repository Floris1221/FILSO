package com.ale.filso.views.components.customField;

import com.ale.filso.views.components.CustomView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.IntegerField;

public class CustomIntegerField extends IntegerField {

    public CustomIntegerField(String key, boolean hasAccess){
        super(key);
        setReadOnly(!hasAccess);
        addListeners();
    }

    public CustomIntegerField(String key, String suffix, boolean hasAccess){
        super(key);
        setReadOnly(!hasAccess);
        if (suffix!=null) {
            Div divSuffix = new Div();
            divSuffix.setText(suffix);
            this.setSuffixComponent(divSuffix);
        }
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
