/**
 * 
 */
package org.bluemix.challenge.ui.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Andreas Höhmann
 */
@SuppressWarnings("serial")
public class MainView extends VerticalLayout implements View {
  
  private final Navigator navigator;

  public MainView(Navigator navigator) {
    super();
    this.navigator = navigator;
    setSizeFull();
    initContent();
  }

  private void initContent() {
    MenuBar menuBar = new MenuBar();
    menuBar.addItem("chart", new MenuBar.Command() {
      
      @Override
      public void menuSelected(MenuItem selectedItem) {
        navigator.navigateTo("chart");
      }
    });
    addComponent(menuBar);
  }

  @Override
  public void enter(ViewChangeEvent event) {
  }

}
