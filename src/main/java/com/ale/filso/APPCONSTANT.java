package com.ale.filso;

import org.springframework.security.core.parameters.P;

import javax.servlet.http.PushBuilder;

public class APPCONSTANT {
    public static final String APP_NAME = "FILSO";

    //Rout
    public static final String ROUTE_BREW_DETAILS = "brew/%s";
    public static final String ROUTE_BREW_SEARCH = "brewHouseSearch";

    public static final String ROUTE_DICTIONARY_SEARCH_VIEW = "dictionarySearch";

    public static final Integer PRODUCT_TYPE = 1;
    public static final Integer UNIT_OF_MEASURE = 2;
}
