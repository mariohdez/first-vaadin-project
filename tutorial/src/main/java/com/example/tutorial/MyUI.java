package com.example.tutorial;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import org.atmosphere.cpr.MetaBroadcaster.ThirtySecondsCache;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
	
	private CustomerService customerService = CustomerService.getInstance();
	private Grid grid = new Grid();
	private TextField filterTextField = new TextField();
	private Button clearFilteredTextButton = new Button( FontAwesome.TIMES );
	private CustomerForm customerForm = new CustomerForm( this );
	
    @SuppressWarnings("serial")
	@Override
    protected void init(VaadinRequest vaadinRequest) {
    		VerticalLayout verticalLayout = new VerticalLayout();
    		CssLayout filteringCssLayout = new CssLayout();
    		HorizontalLayout main = new HorizontalLayout();
    		filteringCssLayout.setStyleName( ValoTheme.LAYOUT_COMPONENT_GROUP );
    		filterTextField.setInputPrompt( "Filter by name" );
    		
    		clearFilteredTextButton.addClickListener( clickListenerEvent -> {
    			filterTextField.clear();
    			updateList();
    		} );
    		
    		filterTextField.addTextChangeListener( textChangeEvent -> {
    			grid.setContainerDataSource( new BeanItemContainer<>( Customer.class, 
    					customerService.findAll( textChangeEvent.getText() ) ) );
    		} );
    		
    		grid.setColumns( "firstName", "lastName", "email" );
    		
    		filteringCssLayout.addComponents( filterTextField, clearFilteredTextButton );
    		
    		verticalLayout.setMargin( true );
    		verticalLayout.setSpacing( true );
    		main.setSpacing( true );
    		main.setSizeFull();
    		grid.setSizeFull();
    		main.addComponents( grid, customerForm );
    		main.setExpandRatio( grid, 1 );
    		verticalLayout.addComponents( filteringCssLayout,  main );
    		updateList();
    		setContent( verticalLayout );
    		customerForm.setVisible( false );
    		grid.addSelectionListener( selectionListenerEvent -> {
    			if ( selectionListenerEvent.getSelected().isEmpty() ) {
    				customerForm.setVisible( false );
    			} else {
    				Customer customer = (Customer) selectionListenerEvent.getSelected().iterator().next();
    				customerForm.setCustomer( customer );
    				customerForm.setVisible( true );
    			}
    		} );
    }

	public void updateList() {
		List<Customer> customers = customerService.findAll( filterTextField.getValue() );
		grid.setContainerDataSource( new BeanItemContainer<>( Customer.class, customers ) );
	}
	
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
