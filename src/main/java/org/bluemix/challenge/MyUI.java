package org.bluemix.challenge;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 */
@Theme("mytheme")
@Widgetset("org.bluemix.challenge.MyAppWidgetset")
public class MyUI extends UI {

	@Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout(); 
        layout.setMargin(true);
        setContent(layout);

        final Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	final Property<String> name = new ObjectProperty<String>("", String.class);
            	final TextField textField = new TextField("What's your name?", name);
            	textField.focus();
            	textField.setRequired(true);
            	final Window nameInputWindow = new Window();
            	nameInputWindow.setModal(true);
            	nameInputWindow.setClosable(false);
            	nameInputWindow.setResizable(false);
            	FormLayout content = new FormLayout(textField);
				content.setMargin(true);
				nameInputWindow.setContent(content);
            	nameInputWindow.center();
            	nameInputWindow.setWidth(400, Unit.PIXELS);
				addWindow(nameInputWindow);
				textField.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						nameInputWindow.close();
						layout.addComponent(new Label(name.getValue() + "! Thank you for clicking"));
					}
				});
            };
        });
        layout.addComponent(button);
    } 

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
