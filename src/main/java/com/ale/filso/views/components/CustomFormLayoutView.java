package com.ale.filso.views.components;

import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;


public abstract class CustomFormLayoutView<E> extends CustomView{

    // editing row - bean in edit panel
    protected E entity;
    protected Binder<E> binder;
    protected FormLayout formLayout = new FormLayout();
    protected HorizontalLayout buttonLayout = new HorizontalLayout();

    protected CustomFormLayoutView(UserAuthorization userAuthorization, E entity, Binder<E> binder) {
        super(userAuthorization);
        this.entity = entity;
        this.binder = binder;
    }



    public void createPanel() {

        this.setClassName("flex flex-col");
        this.setSizeFull();

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");

        formLayout.addClassName("form-layout-view");
        editorDiv.add(formLayout);

        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);

        createFormLayout();
        createButtonLayout();

        this.add(buttonLayout);
        this.add(editorDiv);


    }

    protected abstract void createButtonLayout();

    protected abstract void createFormLayout();


    protected void clearForm() {
        binder.readBean(entity);
        setModified(false);
    }

}
