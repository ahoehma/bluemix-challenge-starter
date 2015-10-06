package org.bluemix.challenge;

import java.util.concurrent.Callable;

import javax.servlet.annotation.WebServlet;

import org.bluemix.challenge.ui.view.ChartView;
import org.bluemix.challenge.ui.view.CustomerGridView;
import org.bluemix.challenge.ui.view.LoginView;
import org.bluemix.challenge.ui.view.MainView;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("mytheme")
@Widgetset("org.bluemix.challenge.MyAppWidgetset")
@PreserveOnRefresh
public class MyUI extends UI {

  private Navigator navigator;

  @Override
  protected void init(VaadinRequest vaadinRequest) {

    // Create a navigator to control the views
    navigator = new Navigator(this, this);

    // Create and register the views
    navigator.addView("main", new MainView(navigator));
    navigator.addView("grid", CustomerGridView.class);
    navigator.addView("login", LoginView.class);
    navigator.addView("chart", ChartView.class);
    
    navigator.addView("", new LoginView().onLoginSuccessful(new Callable<Void>() {

      @Override
      public Void call() throws Exception {
        final Notification notification = new Notification("Login successful.", Type.TRAY_NOTIFICATION);
        notification.setDelayMsec(2000);
        notification.show(getPage());
        navigator.navigateTo("main");
        return null;
      }
    }).onLoginError(new Callable<Void>() {

      int errorCount = 0;

      @Override
      public Void call() throws Exception {
        if (errorCount <= 3) {
          final Notification notification = new Notification("Login incorrect. Please try again.", Type.TRAY_NOTIFICATION);
          notification.setDelayMsec(2000);
          notification.show(getPage());
        } else {
          final Notification notification = new Notification("Try login 'vaadin' with password 'bluemix' ;-)", Type.TRAY_NOTIFICATION);
          notification.setDelayMsec(2000);
          notification.show(getPage());
        }
        errorCount++;
        return null;
      }
    }));
  }

  @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
  @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
  public static class MyUIServlet extends VaadinServlet {
    // nop
  }
}
