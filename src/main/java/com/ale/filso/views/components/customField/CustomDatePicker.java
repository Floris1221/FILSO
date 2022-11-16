package com.ale.filso.views.components.customField;

import com.ale.filso.views.components.CustomView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.server.VaadinSession;

import java.util.Arrays;

public class CustomDatePicker extends DatePicker {

    private boolean startUserInteraction=false;

    public CustomDatePicker(String key){
        super(key);
        setDatePickerLocale(VaadinSession.getCurrent().getLocale().getLanguage());
        addListeners();
    }

    public CustomDatePicker(String key, boolean hasAccess){
        super(key);
        setReadOnly(!hasAccess);
        setDatePickerLocale(VaadinSession.getCurrent().getLocale().getLanguage());
        addListeners();
    }

    private void addListeners() {
        this.addAttachListener(event -> startUserInteraction=true);
        this.addValueChangeListener(event -> { if (startUserInteraction ) {
            setParentViewDataModified(this);
        }} );
    }

    private void setDatePickerLocale(String locale) {
        if (locale.equals("en")) {      // locale en
            this.setI18n(new DatePickerI18n().setWeek("week")
                    .setCalendar("calendar").setClear("clear")
                    .setToday("today").setCancel("cancel").setFirstDayOfWeek(1)
                    .setMonthNames(Arrays.asList("january", "february", "march",
                            "april", "may", "june", "july", "august",
                            "september", "october", "november", "december"))
                    .setWeekdays(Arrays.asList("monday", "tuesday", "wednesday",
                            "thursday", "friday", "saturday", "sunday"))
                    .setWeekdaysShort(Arrays.asList("sun", "mon", "tue", "wed", "thu", "fri",
                            "sat")));
        } else {                        // locale default pl
            this.setI18n(new DatePickerI18n().setWeek("tydzień")
                    .setCalendar("kalendarz").setClear("wyczyść")
                    .setToday("dziś").setCancel("anuluj").setFirstDayOfWeek(1)
                    .setMonthNames(Arrays.asList("styczeń", "luty", "marzec",
                            "kwiecień", "maj", "czerwiec", "lipiec", "sierpień",
                            "wrzesień", "październik", "listopad", "grudzień"))
                    .setWeekdays(Arrays.asList("poniedziałek", "wtorek", "środa",
                            "czwartek", "piątek", "sobota", "niedziela"))
                    .setWeekdaysShort(Arrays.asList("niedz", "pon", "wt", "śr", "czw", "pt",
                            "sob")));
        }
    }


    private void setParentViewDataModified(Component component) {
        Component parent = component.getParent().orElse(null);
        if (parent instanceof CustomView) ((CustomView) parent).setModified(true);
        else setParentViewDataModified(parent);
    }
}
