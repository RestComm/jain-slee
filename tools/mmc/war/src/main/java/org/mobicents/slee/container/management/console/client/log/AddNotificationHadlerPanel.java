/*
 * Mobicents: The Open Source VoIP Middleware Platform
 *
 * Copyright 2003-2006, Mobicents, 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package org.mobicents.slee.container.management.console.client.log;

import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.CommonControl;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.UserInterface;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author baranowb
 *
 */
public class AddNotificationHadlerPanel extends Composite implements CommonControl {

	
	
	private String loggerName = null;

	private ListPanel options = new ListPanel();

	//private TextBox _levelBox = new TextBox();

	private ListBox _levelList=new ListBox();
	
	//private TextBox _nameBox = new TextBox();

	private TextBox _formaterClassNameBox = new TextBox();

	private TextBox _filterClassNameBox = new TextBox();

	private TextBox _entriesNumberBox = new TextBox();

	private LoggerDetailsPanel shower;


	public AddNotificationHadlerPanel(String loggerName,LoggerDetailsPanel shower) {
		super();
		this.loggerName = loggerName;
		this.shower=shower;
		// TODO DO THIS IN CSS
		// this._levelBox.setSize("40px", "10px");
		// this._nameBox.setSize("40px", "10px");
		// this._formaterClassNameBox.setSize("40px", "10px");
		// this._filterClassNameBox.setSize("40px", "10px");
		// this._hostBox.setSize("40px", "10px");
		// this._portBox.setSize("40px", "10px");
		// this._levelBox.setMaxLength(10);
		// this._nameBox.setMaxLength(20);
		// this._formaterClassNameBox.setMaxLength(80);
		// this._filterClassNameBox.setMaxLength(80);
		// this._hostBox.setMaxLength(80);
		// this._portBox.setMaxLength(10);

		ListPanel inner = new ListPanel();
		Hyperlink createLink = new Hyperlink("Create Handler", null);


		inner.setCell(0, 2, new Label("Handler Level:"));
		inner.setCell(0, 3, _levelList);
		inner.setCell(1, 0, new Label("Formatter class:"));
		inner.setCell(1, 1, _formaterClassNameBox);
		inner.setCell(1, 2, new Label("Filter class:"));
		inner.setCell(1, 3, _filterClassNameBox);
		inner.setCell(0, 0, new Label("Number of entries:"));
		inner.setCell(0, 1, _entriesNumberBox);
	

		for(int i=0;i<LogTreeNode._LEVELS.length;i++)
		{
			_levelList.addItem(LogTreeNode._LEVELS[i], LogTreeNode._LEVELS[i]);
			
		}
		_levelList.setSelectedIndex(0);
		
		
		//This leaves a lot of place in the right - to be filled with params for filter and formatter!!!!
		// options.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		
		
		
		options.setCell(0, 0, inner);
		options.setCell(1, 0, createLink);

		options.setCellAlignment(1,0,HasVerticalAlignment.ALIGN_MIDDLE,HasHorizontalAlignment.ALIGN_CENTER);
		
		createLink.addClickListener(new AddNotificationHandlerClickListener());
		
		initWidget(options);
	}

	public void addStyleName(String style) {
		options.addStyleName(style);
	}

	public void emptyTable() {
		options.emptyTable();
	}

	public String getStyleName() {
		return options.getStyleName();
	}

	public void setStyleName(String style) {
		options.setStyleName(style);
	}

	// We dont need those :]
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.console.client.common.CommonControl#onHide()
	 */
	public void onHide() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.console.client.common.CommonControl#onInit()
	 */
	public void onInit() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.console.client.common.CommonControl#onShow()
	 */
	public void onShow() {
		// TODO Auto-generated method stub

	}
	
	private class AddNotificationHandlerClickListener implements ClickListener {
		public void onClick(Widget arg0) {
			int numberOfEntries = -1;
			try {
				if(_entriesNumberBox.getText().equals(""))
				{
					ServerConnection.logServiceAsync.getDefaultNotificationInterval(new AsyncCallback(){

						public void onFailure(Throwable t) {
							UserInterface.getLogPanel().error("Failed to add notification handler due to:\n"+t.getMessage());
						}

						public void onSuccess(Object arg0) {
							performAdd(((Integer)arg0).intValue());
							
						}});
					return;
				}
				numberOfEntries = Integer.valueOf(_entriesNumberBox.getText()).intValue();
			} catch (Exception e) {
				UserInterface.getLogPanel().error("Cant parse port due:" + e.getMessage());
				return;
			}
			
			performAdd(numberOfEntries);
			
		}
	}
	
	private void performAdd(int numberOfEntries)
	{
		ServerConnection.logServiceAsync.addNotificationHandler(loggerName, numberOfEntries, _levelList.getItemText(_levelList.getSelectedIndex()),_formaterClassNameBox.getText() ,_filterClassNameBox.getText(), new AsyncCallback() {

			public void onFailure(Throwable t) {
				UserInterface.getLogPanel().error("Failed to add notification handler due to:\n"+t.getMessage());
			}

			public void onSuccess(Object arg0) {
				shower.onHide();
				shower.onShow();

			}
		});

	}
	
}
