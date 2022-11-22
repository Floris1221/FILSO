package com.ale.filso.views.components;

import java.text.DecimalFormat;

public class CustomDecimalFormat extends DecimalFormat {

    public CustomDecimalFormat(int digit){
        super();
        setGroupingSize(3);
        setMaximumFractionDigits(digit);
        setMinimumFractionDigits(digit);
    }

    public CustomDecimalFormat(){
        super();
        setGroupingSize(3);
        setMaximumFractionDigits(2);
        setMinimumFractionDigits(2);
    }
}
