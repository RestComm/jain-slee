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

import java.util.ArrayList;

import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.CommonControl;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.UserInterface;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class AddGenericHadlerPanel extends Composite implements CommonControl {

	private String loggerName = null;

	private ListPanel options = new ListPanel();

	// private TextBox _levelBox = new TextBox();

	private ListBox _levelList = new ListBox();

	private TextBox _nameBox = new TextBox();

	private TextBox _handlerClassName = new TextBox();

	private TextBox _formaterClassNameBox = new TextBox();

	private TextBox _filterClassNameBox = new TextBox();

	private ArrayList paramTypes = new ArrayList();

	private ArrayList paramBoxes = new ArrayList();

	private ListPanel paramsList = null;

	private LoggerDetailsPanel shower;

	public AddGenericHadlerPanel(String loggerName, LoggerDetailsPanel shower) {
		super();
		this.loggerName = loggerName;
		this.shower = shower;

		ListPanel inner = new ListPanel();
		Hyperlink createLink = new Hyperlink("Create Handler", null);
		createLink.addClickListener(new CreateGenericHandlerClickListener());
		inner.setCell(0, 0, new Label("Handler Name:"));
		inner.setCell(0, 1, _nameBox);
		inner.setCell(0, 2, new Label("Handler Level:"));
		inner.setCell(0, 3, _levelList);
		inner.setCell(1, 0, new Label("Formatter class:"));
		inner.setCell(1, 1, _formaterClassNameBox);
		inner.setCell(1, 2, new Label("Filter class:"));
		inner.setCell(1, 3, _filterClassNameBox);
		inner.setCell(2, 0, new Label("Handler Class:"));
		inner.setCell(2, 1, _handlerClassName);
		inner.setCell(2, 2, new Label(""));
		inner.setCell(2, 3, new Label(""));

		for (int i = 0; i < LogTreeNode._LEVELS.length; i++) {
			_levelList.addItem(LogTreeNode._LEVELS[i], LogTreeNode._LEVELS[i]);

		}
		_levelList.setSelectedIndex(0);

		// This leaves a lot of place in the right - to be filled with params
		// for filter and formatter!!!!
		// options.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		buildParamList();
		options.setCell(0, 0, inner);
		// options.setCell(1, 0, paramsList);
		options.setCell(2, 0, createLink);

		options.setCellAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE, HasHorizontalAlignment.ALIGN_CENTER);

		options.setWidth("100%");
		options.setHeight("100%");
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
	 * @see
	 * org.mobicents.slee.container.management.console.client.common.CommonControl
	 * #onHide()
	 */
	public void onHide() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.console.client.common.CommonControl
	 * #onInit()
	 */
	public void onInit() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.console.client.common.CommonControl
	 * #onShow()
	 */
	public void onShow() {
		// TODO Auto-generated method stub

	}

	private void buildParamList() {
		paramsList = new ListPanel();
		// Now part with constructor params
		Hyperlink addParam = new Hyperlink("Add", null);
		Hyperlink removeParam = new Hyperlink("Remove", null);

		addParam.addClickListener(new ClickListener() {

			public void onClick(Widget arg0) {
				TextBox paramType = new TextBox();
				TextBox value = new TextBox();
				// paramsList.setCell(paramTypes.size(), 1, paramType);
				paramTypes.add(paramType);
				// paramsList.setCell(paramBoxes.size(), 2, value);
				paramBoxes.add(value);
				buildParamList();

			}
		});

		removeParam.addClickListener(new ClickListener() {

			public void onClick(Widget arg0) {
				if (paramTypes.size() > 0) {

					paramBoxes.remove(paramBoxes.size() - 1);
					paramTypes.remove(paramTypes.size() - 1);
					buildParamList();
				}

			}
		});
		// Small trick
		FlexTable ft = new FlexTable();
		ft.setWidget(0, 0, addParam);
		ft.setText(0, 1, "/");
		ft.setWidget(0, 2, removeParam);

		paramsList.setHeader(0, ft);
		paramsList.setHeader(1, "Type");
		paramsList.setHeader(2, "Value");
		paramsList.setColumnWidth(0, "8%");
		paramsList.setColumnWidth(1, "46%");
		paramsList.setColumnWidth(2, "46%");

		// Rebuild boxes
		for (int i = 0; i < paramTypes.size(); i++) {
			paramsList.setCell(i, 1, (Widget) paramTypes.get(i));
			paramsList.setCell(i, 2, (Widget) paramBoxes.get(i));
			paramsList.setCellText(i, 0, "#" + (i + 1));
		}
		options.setCell(1, 0, paramsList);
	}

	private class CreateGenericHandlerClickListener implements ClickListener {

		public void onClick(Widget arg0) {

			ServerConnection.logServiceAsync.addHandler(loggerName, _nameBox.getName(), _handlerClassName.getName(), _formaterClassNameBox.getText(),
					_filterClassNameBox.getText(), (String[]) paramTypes.toArray(new String[paramTypes.size()]), (String[]) paramBoxes.toArray(new String[paramBoxes.size()]),
					new AsyncCallback() {

						public void onFailure(Throwable t) {
							UserInterface.getLogPanel().error("Failed to add notification handler due to:\n" + t.getMessage());
						}

						public void onSuccess(Object arg0) {
							shower.onHide();
							shower.onShow();

						}
					});

		}

	}
}
