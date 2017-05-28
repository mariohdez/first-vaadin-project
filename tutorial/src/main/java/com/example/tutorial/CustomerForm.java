package com.example.tutorial;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerForm extends FormLayout {
	private TextField firstName = new TextField( "First Name" );
	private TextField lastName = new TextField( "Last Name" );
	private TextField email = new TextField( "E-mail" );
	private NativeSelect status = new NativeSelect( "Status" ); 
	private PopupDateField birthDate = new PopupDateField( "BirthDate" );
	private Button saveButton = new Button( "Save" );
	private Button deleteButton = new Button( "Delete" );
	
	private CustomerService customerSerive = CustomerService.getInstance();
	private Customer currentCustomer;
	private MyUI myUI;
	
	public CustomerForm( MyUI myUI ) {
		this.myUI = myUI;
		status.addItems( CustomerStatus.values() );
		saveButton.setStyleName( ValoTheme.BUTTON_PRIMARY );
		saveButton.setClickShortcut( KeyCode.ENTER );
		
		saveButton.addClickListener( clickListenerEvent -> {
			save();
		} );
		deleteButton.addClickListener( clickListenerEvent -> {
			delete();
		} );
		
		setSizeUndefined();
		HorizontalLayout buttonHorizontalLayout = new HorizontalLayout();
		buttonHorizontalLayout.setSpacing( true );
		buttonHorizontalLayout.addComponents( saveButton, deleteButton );
		addComponents( firstName, lastName, email, status, birthDate, buttonHorizontalLayout );
	}
	
	public void setCustomer( Customer customer ) {
		this.currentCustomer = customer;
		BeanFieldGroup.bindFieldsUnbuffered( customer, this );
		deleteButton.setVisible( customer.isPersisted() );
		setVisible( true );
		firstName.selectAll();
	}
	
	private void save() {
		customerSerive.save( currentCustomer );
		myUI.updateList();
		setVisible( false );
	}
	
	private void delete() {
		customerSerive.delete( currentCustomer );
		myUI.updateList();
		setVisible( false );
	}
	
}
