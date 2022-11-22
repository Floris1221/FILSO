package com.ale.filso.views.components.customField;

import com.ale.filso.views.components.CustomView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.BigDecimalField;
import org.vaadin.miki.superfields.numbers.SuperBigDecimalField;

import java.math.RoundingMode;

public class CustomBigDecimalField extends SuperBigDecimalField {

    public CustomBigDecimalField(String key, String suffix, boolean haveParent){
        super(key);
        this.withMaximumFractionDigits(2);
        this.withMinimumFractionDigits(2);
        this.withMaximumIntegerDigits(12);
        if (suffix!=null) {
            Div divSuffix = new Div();
            divSuffix.setText(suffix);
            this.setSuffixComponent(divSuffix);
        }
        if(haveParent)
            addListeners();
    }

    public CustomBigDecimalField(String key, String suffix, boolean haveParent, boolean hasAccess){
        super(key);
        this.setReadOnly(!hasAccess);
        this.withMaximumFractionDigits(2);
        this.withMinimumFractionDigits(2);
        this.withMaximumIntegerDigits(12);
        if (suffix!=null) {
            Div divSuffix = new Div();
            divSuffix.setText(suffix);
            this.setSuffixComponent(divSuffix);
        }
        if(haveParent)
            addListeners();
    }

    public CustomBigDecimalField(String key, String suffix, boolean haveParent, int digit){
        super(key);
        this.withMaximumFractionDigits(digit);
        this.withMinimumFractionDigits(digit);
        this.withMaximumIntegerDigits(12);
        if (suffix!=null) {
            Div divSuffix = new Div();
            divSuffix.setText(suffix);
            this.setSuffixComponent(divSuffix);
        }
        if(haveParent)
            addListeners();
    }


    public CustomBigDecimalField(String key, String suffix, boolean haveParent, boolean hasAccess, int digit){
        super(key);
        this.setReadOnly(!hasAccess);
        this.withMaximumFractionDigits(digit);
        this.withMinimumFractionDigits(digit);
        this.withMaximumIntegerDigits(12);
        if (suffix!=null) {
            Div divSuffix = new Div();
            divSuffix.setText(suffix);
            this.setSuffixComponent(divSuffix);
        }
        if(haveParent)
            addListeners();
    }


    private void addListeners() {
        this.addTextSelectionListener(event -> {
            setParentViewDataModified(this);
        });
    }

    private void setParentViewDataModified(Component component) {
        Component parent = component.getParent().orElse(null);
        if (parent instanceof CustomView) ((CustomView) parent).setModified(true);
        else setParentViewDataModified(parent);
    }
}
