/**
 * 
 */
package org.bluemix.challenge.ui.view;

import org.bluemix.challenge.backend.Customer;
import org.bluemix.challenge.backend.DummyDataService;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification.Type;

/**
 * @author Andreas Höhmann
 */
@SuppressWarnings("serial")
public class CustomerGridView extends VerticalLayout implements View {
  
  public CustomerGridView() {
    super();
    setMargin(true);
    initContent();
  }

  @Override
  public void enter(ViewChangeEvent event) {
    // later
  }

  private void initContent() {
    
    // goal 1
    final BeanItemContainer<Customer> beanItemContainer = new BeanItemContainer<Customer>(Customer.class);
    beanItemContainer.addAll(DummyDataService.createDemoService().findAll());
    ComboBox combobox = new ComboBox(null, beanItemContainer);
    addComponent(combobox);

    // goal 2
    final Grid grid = new Grid(null, beanItemContainer);
    addComponent(grid);

    // goal 3
    grid.setSizeFull();
    grid.setSelectionMode(SelectionMode.SINGLE);
    grid.addSelectionListener(new SelectionListener() {

      @Override
      public void select(SelectionEvent event) {
        // Q: why no type safety here?
        // Object first = Iterables.getFirst(event.getSelected(), null);
        // Q: not better - why no type safety here?
        // Object first = Iterables.getFirst(grid.getSelectedRows(), null);
        Optional<Customer> selectedCustomer = FluentIterable.from(event.getSelected()).transform(new Function<Object, Customer>() {

          @Override
          public Customer apply(Object input) {
            // Q: why can not know grid about the concreted used container data
            // source?
            // here I got java.lang.ClassCastException:
            // com.vaadin.data.util.BeanItem cannot be cast to
            // org.bluemix.challenge.backend.Customer
            // return (Customer) grid.getContainerDataSource().getItem(input);
            // but this works?!
            return beanItemContainer.getItem(input).getBean();
          }
        }).first();
        if (selectedCustomer.isPresent()) {
          Notification n = new Notification("You selected " + selectedCustomer.get(), Type.TRAY_NOTIFICATION);
          n.setDelayMsec(2000);
          n.show(getUI().getPage());
        }
      }
    });
  }

}
