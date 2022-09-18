package com.ale.filso.views.components;

import com.vaadin.flow.component.Component;

/**
 * interface for tab view
 */
public interface HasTabs {
    /**
     * In tab child - view we can modify data. If data are changed then only current tab is enabled.
     *
     * @param isDataModifiedInTab
     * @param component
     */
    public void setAnotherTabsEnabled(boolean isDataModifiedInTab, Component component);
}
