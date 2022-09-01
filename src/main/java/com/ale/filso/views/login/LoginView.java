package com.ale.filso.views.login;


import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;


import java.util.Locale;

import static com.ale.filso.APPCONSTANT.APP_NAME;


@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver, HasDynamicTitle {

    public LoginView() {
        VaadinSession.getCurrent().setLocale(new Locale("pl", "PL"));
        setAction("login");
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        LoginI18n.Header i18nHeader = i18n.getHeader();
        i18nHeader.setTitle(APP_NAME);

        i18nHeader.setDescription(getTranslation("loginView.i18n.desc"));

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle(getTranslation("loginView.i18n.title"));
        i18nForm.setUsername(getTranslation("loginView.i18n.username"));
        i18nForm.setPassword(getTranslation("loginView.i18n.password"));
        i18nForm.setSubmit(getTranslation("loginView.i18n.submit"));
        i18nForm.setForgotPassword(getTranslation("loginView.i18n.forgotPassword"));
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle(getTranslation("loginView.i18nErrorMessage.title"));
        i18nErrorMessage.setMessage(getTranslation("loginView.i18nErrorMessage.message"));
        i18n.setErrorMessage(i18nErrorMessage);

        i18n.setAdditionalInformation(getTranslation("loginView.i18n.additionalInformation"));

        //todo forget Password
        //addForgotPasswordListener(event -> { UI.getCurrent().navigate(PasswordForgotView.class);});

        setI18n(i18n);
        setOpened(true);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(event.getLocation().getQueryParameters().getParameters()
                .containsKey("error")) { setError(true);
        }
    }

    @Override
    public String getPageTitle() {
        return getTranslation("app.title.LoginView");
    }
}
