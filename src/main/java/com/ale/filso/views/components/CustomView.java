package com.ale.filso.views.components;

import com.ale.filso.models.Dictionary.Dictionary;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.components.Enums.ButtonType;
import com.ale.filso.views.components.customField.CustomButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;

import java.util.Map;

public class CustomView extends Div implements BeforeEnterObserver, BeforeLeaveObserver{

    protected boolean isDataModified;
    protected UserAuthorization userAuthorization;

    protected CustomButton saveButton = new CustomButton(ButtonType.SAVE, true);
    protected CustomButton cancelButton = new CustomButton(ButtonType.CANCEL, true);


    protected CustomView(UserAuthorization userAuthorization){
        this.userAuthorization = userAuthorization;

        saveButton.setEnabled(false);
    }

    protected void navigateTo(Class<? extends Component> navigationTarget, Map<String, String> parameters){
        UI.getCurrent().navigate(navigationTarget, new RouteParameters(parameters));
    }

    protected void callUrl(String myEditEntityUrl, Integer objectId) {
        UI.getCurrent().access(() -> {
            UI.getCurrent().getPage()
                    .executeJs("window.location.href = \""+userAuthorization.getAppUrl()
                            +String.format(myEditEntityUrl, objectId)+ "\""); });
    }

    protected void callUrlWithOutParam(String myEditEntityUrl) {
        UI.getCurrent().access(() -> {
            UI.getCurrent().getPage()
                    .executeJs("window.location.href = \""+userAuthorization.getAppUrl()
                            +myEditEntityUrl+ "\""); });
    }



    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {

    }


    public boolean isModified() {
        return isDataModified;
    }


    public void setModified(boolean isModified) {
        if (isModified!=isDataModified) {   // do only if changes in view
            isDataModified=isModified;
            saveButton.setEnabled(isModified);
        }
    }


    /**
     * get name from entity dictionary
     *
     * @param dictionary
     * @return
     */
    protected String getDictName(Dictionary dictionary) {
        return dictionary == null ? "" : dictionary.getName();
    }

    /**
     * get shortName from entity dictionary
     *
     * @param dictionary
     * @return
     */
    protected String getDictShortName(Dictionary dictionary) {
        return dictionary == null ? "" : dictionary.getShortName();
    }

}
