/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.management.console.client.profiles;

import java.util.Iterator;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.PropertiesInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Mohamed Salah
 * @author <a href="mailto:msrashed100@gmail.com"> Mohamed Salah </a>
 */
public class ProfileAttributesPanel extends Composite {

	private VerticalPanel rootPanel = new VerticalPanel();

	private FlexTable propertiesTable = new FlexTable();

	private FlexTable buttonPanel = new FlexTable();

	private Button editButton = new Button("Edit");

	private Button saveButton = new Button("Save & Commit");

	private Button cancelButton = new Button("Cancel");

	private String nameWidth = "100px";

	final private int READ_MODE = 0;

	final private int WRITE_MODE = 1;

	private int mode = READ_MODE;

	private PropertiesInfo propertiesInfo;

	private ProfileAttributesListener listener;

	public ProfileAttributesPanel(PropertiesInfo propertiesInfo, ProfileAttributesListener listener) {
		super();

		initWidget(rootPanel);

		rootPanel.setWidth("100%");

		propertiesTable.setStyleName("common-PropertiesPanel");
		propertiesTable.setCellSpacing(0);
		propertiesTable.setCellPadding(2);
		setNameWidth("100px");

		buttonPanel.setCellSpacing(0);
		buttonPanel.setCellPadding(2);
		
		editButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onEditButton();
			}
		});
		
		
		
		buttonPanel.setWidget(0, 0, editButton);
		buttonPanel.setWidget(0, 1, saveButton);
		buttonPanel.setWidget(0, 2, cancelButton);

		rootPanel.add(propertiesTable);
		rootPanel.add(buttonPanel);

		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onSaveButton();
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onCancelButton();
			}
		});

		this.propertiesInfo = propertiesInfo;
		this.listener = listener;
		refreshGUI();
		
	}

	private void refreshGUI() {
		Iterator<String> iterator = propertiesInfo.keySet().iterator();
		int row = 0;
		while (iterator.hasNext()) {
			String name = (String) iterator.next();
			String value = propertiesInfo.getProperty(name);
			if (mode == READ_MODE) {
				add(row, name, new Label(value));
			} else {
				TextBox textBox = new TextBox();
				textBox.setTextAlignment(TextBoxBase.ALIGN_LEFT);
				textBox.setText(value);
				textBox.setWidth("100%");
				
				add(row, name, textBox);
			}
			row++;
		}

		if (mode == READ_MODE) {
			editButton.setEnabled(true);
			saveButton.setEnabled(false);
			cancelButton.setEnabled(false);
		} else {
			editButton.setEnabled(false);
			saveButton.setEnabled(true);
			cancelButton.setEnabled(true);
		}
	}

	private String getValue(int row) {
		return ((HasText) propertiesTable.getWidget(row, 1)).getText();
	}

	private String getName(int row) {
		return ((HasText) propertiesTable.getWidget(row, 0)).getText();
	}

	public void refreshPropertiesData() {
		int size = propertiesInfo.size();

		for (int row = 0; row < size; row++) {
			String name = getName(row);
			String value = getValue(row);
			propertiesInfo.setProperty(name, value);
		}
	}

	public void setMode(int mode) {
		this.mode = mode;
		refreshGUI();
	}

	private void setRowStyle(int row) {
		propertiesTable.getCellFormatter().setStyleName(row, 0, "common-PropertiesPanel-name");
		propertiesTable.getCellFormatter().setStyleName(row, 1, "common-PropertiesPanel-value");
		propertiesTable.getRowFormatter().setVerticalAlign(row, HasVerticalAlignment.ALIGN_MIDDLE);

		if (row != 0) {
			propertiesTable.getCellFormatter().addStyleName(row, 0, "common-PropertiesPanel-notfirst");
			propertiesTable.getCellFormatter().addStyleName(row, 1, "common-PropertiesPanel-notfirst");
		}

		if (row % 2 != 0) {
			propertiesTable.getCellFormatter().addStyleName(row, 0, "common-PropertiesPanel-even");
			propertiesTable.getCellFormatter().addStyleName(row, 1, "common-PropertiesPanel-even");
		}

		propertiesTable.getCellFormatter().setWidth(row, 0, nameWidth);
		propertiesTable.getCellFormatter().setWidth(row, 1, "");
	}

	private void add(int row, String name, Widget value) {
		if (value == null) {
			add(row, name, new Label("-"));
			return;
		}
		Label nameLabel = new Label(name);
		nameLabel.setWordWrap(false);
		propertiesTable.setWidget(row, 0, nameLabel);
		propertiesTable.setWidget(row, 1, value);
		setRowStyle(row);
	}

	public void setNameWidth(String nameWidth) {
		this.nameWidth = nameWidth;
	}

	public void onEditButton() {
		setMode(WRITE_MODE);
	}

	public void onSaveButton() {
		refreshPropertiesData();

		setMode(READ_MODE);

		if (listener != null){
			listener.onSaveProperties(propertiesInfo);
		}
	}

	public void onCancelButton() {
		setMode(READ_MODE);
		if (listener != null)
			listener.onCancelProperties(propertiesInfo);
	}
	public void onEditError(){
		setMode(WRITE_MODE);
	}

	public void showCreationPanel() {
		setMode(WRITE_MODE);
		editButton.setVisible(false);
	}
	public void showEditPanel() {
		editButton.setVisible(true);
	}

}
