package com.ale.filso.views.components.customDialogs;

import com.ale.filso.models.Dictionary.Dictionary;
import com.vaadin.flow.component.dialog.Dialog;

public abstract class CustomDialog extends Dialog {


    CustomDialog(String title){
        setHeaderTitle(title);
    }

    protected abstract void createView();

    //Same like in CustomView
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
