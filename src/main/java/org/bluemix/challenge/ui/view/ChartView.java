/**
 * 
 */
package org.bluemix.challenge.ui.view;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Multimaps.index;
import static org.bluemix.challenge.backend.DummyDataService.createDemoService;
import static org.joda.time.Years.yearsBetween;

import java.util.List;

import org.bluemix.challenge.backend.Customer;
import org.bluemix.challenge.backend.Customer.Gender;
import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Ordering;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author Andreas Höhmann
 */
@SuppressWarnings("serial")
public class ChartView extends HorizontalLayout implements View {
  
  public ChartView() {
    super();
    setSizeFull();
    initContent();
  }

  private void initContent() {
    addComponent(ageChart());
    addComponent(genderChart());
  }

  private Chart genderChart() {

    Chart chart = new Chart(ChartType.PIE);
    chart.setWidth("400px");
    chart.setHeight("600px");
    
    Configuration conf = chart.getConfiguration();
    conf.setTitle("Men vs Women");
    
    List<Customer> customers = createDemoService().findAll();
    final ImmutableListMultimap<Gender, Customer> customersPerGender = index(customers, calculateGender());
    
    final DataSeries series = new DataSeries();
    int men = customersPerGender.asMap().get(Gender.Male).size();
    int women = customersPerGender.asMap().get(Gender.Female).size();
    int other = customersPerGender.asMap().get(Gender.Other).size();
    series.add(new DataSeriesItem("Men", men*100/customers.size()));
    series.add(new DataSeriesItem("Women", women*100/customers.size()));
    series.add(new DataSeriesItem("Other", other*100/customers.size()));
    conf.setSeries(series);

    chart.drawChart(conf);
    
    return chart;
  }

  private static Function<Customer, Gender> calculateGender() {
    return new Function<Customer, Customer.Gender>() {

      @Override
      public Gender apply(Customer input) {
        return input.getGender();
      }
    };
  }

  private Chart ageChart() {
    
    Chart chart = new Chart(ChartType.BAR);
    chart.setWidth("400px");
    chart.setHeight("600px");
            
    Configuration conf = chart.getConfiguration();
    conf.setTitle("People's age");
    
    final DateTime now = new DateTime();
    final ImmutableListMultimap<Number, Customer> customersPerAge = index(createDemoService().findAll(), calculateAge(now));
    final ImmutableList<Number> agesSorted = from(customersPerAge.asMap().keySet()).toSortedList(Ordering.usingToString());
    final ImmutableList<Number> countPerAge = from(agesSorted).transform(countPerAge(customersPerAge)).toList();
    final ImmutableList<String> ages = from(agesSorted).transform(ages()).toList();
    
    final ListSeries series = new ListSeries("Number of people with this age");
    series.setData(countPerAge.toArray(new Number[]{}));
    conf.addSeries(series);
    XAxis xaxis = new XAxis();
    xaxis.setCategories(ages.toArray(new String[]{}));
    xaxis.setTitle("Age");
    conf.addxAxis(xaxis);
    YAxis yaxis = new YAxis();
    yaxis.setTitle("Count");
    conf.addyAxis(yaxis);
    return chart;
  }

  private Function<Number, String> ages() {
    return new Function<Number, String>(){

      @Override
      public String apply(Number input) {
        return String.valueOf(input);
      }};
  }

  private Function<Number, Number> countPerAge(final ImmutableListMultimap<Number, Customer> map) {
    return new Function<Number, Number>(){

      @Override
      public Number apply(Number input) {
        return map.asMap().get(input).size();
      }};
  }

  private Function<Customer, Number> calculateAge(final DateTime now) {
    return new Function<Customer, Number>(){

      @Override
      public Number apply(Customer input) {
        return yearsBetween(new DateTime(input.getBirthDate()), now).getYears();
      }};
  }

  @Override
  public void enter(ViewChangeEvent event) {
  }

}
