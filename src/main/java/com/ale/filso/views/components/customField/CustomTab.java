package com.ale.filso.views.components.customField;

import com.vaadin.flow.component.tabs.Tab;

public class CustomTab extends Tab {

    public CustomTab(String key) {
        super();
        this.setLabel(getTranslation(key));
        this.setId(key);
    }
}
