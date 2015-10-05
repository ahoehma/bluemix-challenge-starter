/**
 * 
 */
package org.bluemix.challenge.ui.view;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.gwt.thirdparty.guava.common.base.Predicates.not;
import static com.google.gwt.thirdparty.guava.common.base.Predicates.or;
import static java.lang.String.valueOf;

import org.bluemix.challenge.backend.Customer;
import org.bluemix.challenge.backend.Customer.Gender;
import org.bluemix.challenge.backend.DummyDataService;
import org.bluemix.challenge.ui.design.DashboardDesign;

import com.google.common.base.Predicate;
import com.google.gwt.thirdparty.guava.common.base.Predicates;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author Andreas Höhmann
 */
@SuppressWarnings("serial")
public class DashboardView extends DashboardDesign implements View {

  public DashboardView() {
    super();
    initContent();
  }

  @Override
  public void enter(ViewChangeEvent event) {
    // later
  }

  private void initContent() {
    final DummyDataService demoService = DummyDataService.createDemoService();
    men.setValue(valueOf(size(filter(demoService.findAll(), isMen()))));
    women.setValue(valueOf(size(filter(demoService.findAll(), isWomen()))));
    other.setValue(valueOf(size(filter(demoService.findAll(), isOther()))));
  }

  private static Predicate<Customer> isMen() {
    return new Predicate<Customer>() {

      @Override
      public boolean apply(Customer input) {
        return input.getGender() == Gender.Male;
      }
    };
  }
  
  private static Predicate<Customer> isOther() { 
    return new Predicate<Customer>() {
      
      @Override
      public boolean apply(Customer input) {
        return input.getGender() == null || input.getGender() == Gender.Other;
      }
    };
  }

  private static Predicate<Customer> isWomen() {
    return new Predicate<Customer>() {

      @Override
      public boolean apply(Customer input) {
        return input.getGender() == Gender.Female;
      }
    };
  }

}
