package com.ale.filso.views.components;

import com.ale.filso.views.components.Enums.ButtonType;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;

public class CustomButton extends Button{
    private boolean hasAccess;

    public CustomButton(ButtonType buttonType, boolean hasAccess){
        this.hasAccess = hasAccess;
        customizeButton(buttonType);
        setVisible(hasAccess);
    }




    private void customizeButton(ButtonType buttonType) {
        if (buttonType == ButtonType.CANCEL) this.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        else this.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        String key = "";
        if (buttonType == ButtonType.DETAILS) {
            key = new String("app.button.details");
            this.setText(getTranslation(key));
            this.setIcon(new Icon("vaadin", "edit"));
        } else if (buttonType == ButtonType.ADD) {
            key = new String("app.button.add");
            this.setText(getTranslation(key));
            this.setIcon(new Icon("vaadin", "plus"));
        } else if (buttonType == ButtonType.SAVE) {
            key = new String("app.button.save");
            this.setText(getTranslation(key));
            this.setIcon(new Icon("lumo", "checkmark"));
        }
        this.setId(key);
    }
}
