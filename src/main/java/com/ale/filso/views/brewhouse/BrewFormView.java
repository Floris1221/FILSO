package com.ale.filso.views.brewhouse;

import com.ale.filso.models.Brew.Brew;
import com.ale.filso.models.Brew.BrewService;
import com.ale.filso.models.User.Role;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.components.CustomFormLayoutView;
import com.ale.filso.views.components.customField.CustomBigDecimalField;
import com.ale.filso.views.components.customField.CustomIntegerField;
import com.ale.filso.views.components.customField.CustomTextField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.HashMap;

import static com.ale.filso.APPCONSTANT.ROUTE_BREW_DETAILS;


public class BrewFormView extends CustomFormLayoutView<Brew> {

    private BrewDetailsView view;

    protected BrewFormView(BrewDetailsView view) {
        super(view.getUserAuthorization(), view.getEntity(), new Binder<>(Brew.class));
        this.view = view;
        createPanel();
    }



    @Override
    protected void createButtonLayout() {
        cancelButton.addClickListener(e -> clearForm());

        saveButton.addClickListener( e-> {
            try {
                boolean newRecord = entity.getId() == null;
                binder.writeBean(entity);
                entity = view.getService().update(entity);
                clearForm();

                Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                if(newRecord) {
                    callUrl(ROUTE_BREW_DETAILS, entity.getId());
                }
                else setModified(false);

            } catch (ValidationException ex) {
                Notification.show(getTranslation("app.message.saveError")).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
                Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        buttonLayout.add(saveButton, cancelButton);
    }

    @Override
    protected void createFormLayout() {

        CustomTextField nameField = new CustomTextField(getTranslation("models.brew.name"), userAuthorization.hasRole(Role.ADMIN));
        binder.forField(nameField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Brew::getName, Brew::setName);

        //todo make uniq number (chceck if duplicate)
        CustomIntegerField numberField = new CustomIntegerField(getTranslation("models.brew.number"), userAuthorization.hasRole(Role.ADMIN));
        numberField.setMin(0);
        binder.forField(numberField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Brew::getNumber, Brew::setNumber);

        CustomBigDecimalField assumedBlgField = new CustomBigDecimalField(getTranslation("models.brew.assumedBlg"), "%", true,
                userAuthorization.hasRole(Role.ADMIN));
        binder.forField(assumedBlgField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Brew::getAssumedBlg, Brew::setAssumedBlg);

        CustomIntegerField assumedAmountField = new CustomIntegerField(getTranslation("models.brew.assumedAmountField"), "l",
                userAuthorization.hasRole(Role.ADMIN));
        assumedAmountField.setMin(0);
        binder.forField(assumedAmountField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Brew::getAssumedAmount, Brew::setAssumedAmount);


        //add all fields to layout
        formLayout.add(nameField, numberField, assumedBlgField, assumedAmountField);

        clearForm();
    }

}
