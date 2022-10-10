package com.ale.filso.views.office;

import com.ale.filso.models.Dictionary.Dictionary;
import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.Dictionary.DictionaryGroup;
import com.ale.filso.models.User.Role;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.components.CustomGridView;
import com.ale.filso.views.components.Enums.ButtonType;
import com.ale.filso.views.components.customField.CustomButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.dao.OptimisticLockingFailureException;

import javax.annotation.security.RolesAllowed;

@Route(value = "dictionary/:id?", layout = MainLayout.class)
@PageTitle("Szczegóły słownika")
@RolesAllowed("admin")
public class DictionaryDetailsView extends CustomGridView<Dictionary> implements BeforeEnterObserver {

    Dialog dialog = new Dialog();
    Binder<Dictionary> binder;
    Dictionary entity;
    DictionaryGroup dictionaryGroup;
    protected Integer id;
    DictionaryCache dictionaryCache;

    protected DictionaryDetailsView(AuthenticatedUser authenticatedUser, DictionaryCache dictionaryCache) {
        super(authenticatedUser, new Grid<>(Dictionary.class, false), new Dictionary());
        binder =  new Binder<>(Dictionary.class);
        entity = new Dictionary();
        this.dictionaryCache = dictionaryCache;

    }

    @Override
    protected void createButtonsPanel() {
        addButtonToTablePanel(ButtonType.ADD, authenticatedUser.hasRole(Role.ADMIN))
                .addClickListener(event -> detailsAction());
    }

    @Override
    protected void createGrid() {

        grid.addColumn(Dictionary::getId).setKey("col1")
                .setHeader("Id.").setFlexGrow(1);

        grid.addColumn(Dictionary::getName).setKey("col2")
                .setHeader(getTranslation("models.dictionary.name")).setFlexGrow(2);

        grid.addColumn(Dictionary::getShortName).setKey("col3")
                        .setHeader(getTranslation("models.dictionary.shortName")).setFlexGrow(2);

        setResizeableSortableGrid(null,null);

        createSearchField();
    }

    @Override
    protected void updateGridDataListWithSearchField(String filterText) {
        grid.setItems(dictionaryCache.findByGroup(id));
    }

    private void detailsAction() {
        entity.setDictionaryGroup(id);
        dialog.open();
    }

    private void createDialog() {
        dialog.setHeaderTitle(getTranslation("app.title.dictionary.new"));

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        //Button saveButton = createSaveButton(dialog);
        saveButton = new CustomButton(ButtonType.SAVE, true);
        cancelButton = new CustomButton(ButtonType.CANCEL, true);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
    }


    private VerticalLayout createDialogLayout() {
        TextField nameField = new TextField(getTranslation("models.dictionary.name"));
        binder.forField(nameField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Dictionary::getName, Dictionary::setName);

        TextField shortNameField = new TextField(getTranslation("models.dictionary.shortName"));
        binder.forField(shortNameField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Dictionary::getShortName, Dictionary::setShortName);

        binder.readBean(entity);

        VerticalLayout dialogLayout = new VerticalLayout(nameField,
                shortNameField);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        try {
            id = Integer.valueOf(event.getRouteParameters().get("id").get());
            dictionaryGroup = getEditedObjectById(id);
            createView();
            createDialog();
            buttonsDialogActions();
        }catch (Exception e){
            Notification.show(getTranslation("app.message.routeError")).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    protected DictionaryGroup getEditedObjectById(Integer id) {
        return dictionaryCache.findDictionaryGroupById(id);
    }

    protected void buttonsDialogActions(){
        saveButton.addClickListener(e -> {
            try {
                binder.writeBean(entity);
                entity = dictionaryCache.update(entity);

                Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                grid.getListDataView().addItem(entity);
                grid.getListDataView().refreshAll();
                grid.scrollToEnd();
                grid.select(entity);

                dialog.close();

                entity = new Dictionary();
                binder.readBean(entity);


            } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
                Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException ex) {
                throw new RuntimeException(ex);
            }
        });

        cancelButton.addClickListener(e -> dialog.close());
    }
}
