package es.mhp.example;

import javax.servlet.annotation.WebServlet;

import es.mhp.example.samples.MainScreen;
import es.mhp.example.samples.authentication.AccessControl;
import es.mhp.example.samples.authentication.BasicAccessControl;
import es.mhp.example.samples.authentication.LoginScreen;
import es.mhp.example.samples.authentication.LoginScreen.LoginListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
public class SpringVaadinCRUDUI extends UI {

    private AccessControl accessControl = new BasicAccessControl();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("My");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, () -> showMainView() ));
        } else {
            showMainView();
        }
    }

    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(SpringVaadinCRUDUI.this));
        getNavigator().navigateTo(getNavigator().getState());
    }

    public static SpringVaadinCRUDUI get() {
        return (SpringVaadinCRUDUI) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    @WebServlet(urlPatterns = "/*", name = "SpringVaadinCRUDUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = SpringVaadinCRUDUI.class, productionMode = false)
    public static class SpringVaadinCRUDUIServlet extends VaadinServlet {
    }
}
