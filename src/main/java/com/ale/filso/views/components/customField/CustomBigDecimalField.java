package com.ale.filso.views.components.customField;

import com.ale.filso.views.components.CustomView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.BigDecimalField;

import java.math.RoundingMode;

public class CustomBigDecimalField extends BigDecimalField {

    public CustomBigDecimalField(String key, String suffix){
        super(key);
        setPattern(calcPattern(1));
        setPreventInvalidInput(true);
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

    private String calcPattern(final int descendants){
        String pattern = "[0-9]*";
        if (descendants > 0){
            pattern += "(,|.)?";
        }
        pattern+="[0-9]{0,"+descendants+"}";
        return pattern;
    }

    private void setParentViewDataModified(Component component) {
        Component parent = component.getParent().orElse(null);
        if (parent instanceof CustomView) ((CustomView) parent).setModified(true);
        else setParentViewDataModified(parent);
    }
}
