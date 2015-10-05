/**
 * 
 */
package org.bluemix.challenge.ui.view;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.concurrent.Callable;

import org.bluemix.challenge.ui.design.LoginDesign;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * @author Andreas Höhmann
 */
@SuppressWarnings("serial")
public class LoginView extends LoginDesign implements View {
  
  private Callable<Void> loginSuccessfulCallback;
  private Callable<Void> loginErrorCallback;

  private final Button.ClickListener signInClickListener = new ClickListener() {

    @Override
    public void buttonClick(ClickEvent event) {
      handleLoginData();
    }
  };
  
  private final ShortcutListener onEnterListener = new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null){

    @Override
    public void handleAction(Object sender, Object target) {
      handleLoginData();
    }
  };
  
  private void handleLoginData() {
    if (loginSuccessfulCallback == null || loginErrorCallback == null) {
      return;
    }
    if (isNullOrEmpty(login.getValue()) || isNullOrEmpty(password.getValue())) {
      return;
    }
    if (equal("vaadin", login.getValue()) && equal("bluemix", password.getValue())) {
      try {
        loginSuccessfulCallback.call();
      } catch (Exception ignore) {
      }
    } else {
      try {
        loginErrorCallback.call();
        login.clear();
        password.clear();
        login.focus();
      } catch (Exception ignore) {
      }
    }
  }

  
  public LoginView() {
    super();
    signIn.addClickListener(signInClickListener);
    addShortcutListener(onEnterListener);
    login.focus();
  }

  @Override
  public void enter(ViewChangeEvent event) {
    // later
  }

  public final LoginView onLoginSuccessful(final Callable<Void> loginSuccessfulCallback) {
    this.loginSuccessfulCallback = loginSuccessfulCallback;
    return this;
  }

  public final LoginView onLoginError(Callable<Void> loginErrorCallback) {
    this.loginErrorCallback = loginErrorCallback;
    return this;
  }
}
