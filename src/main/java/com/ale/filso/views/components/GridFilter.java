package com.ale.filso.views.components;

import com.ale.filso.models.Dictionary.Dictionary;

/**
 * Klasa abstrakcyjna filtrowania gridów w aplikacji
 */
public abstract class GridFilter {

    /**
     * metoda porównawcza na filtrowanie po polu typu String
     *
     * @param value
     * @param searchTerm
     * @return
     */
    protected boolean matches(String value, String searchTerm) {
        if (value==null) value="";
        return searchTerm == null || searchTerm.isEmpty() || value
                .toLowerCase().contains(searchTerm.toLowerCase());
    }

    /**
     * metoda porównawcza na filtrowanie po polu typu Dictionary - po Id
     *
     * @param entityDictionary
     * @param searchTerm
     * @return
     */
    protected boolean matches(Dictionary entityDictionary, Integer searchTerm) {
        if (searchTerm==null) return true;
        if (entityDictionary==null) return false;
        return searchTerm.equals(entityDictionary.getId());

    }

}
