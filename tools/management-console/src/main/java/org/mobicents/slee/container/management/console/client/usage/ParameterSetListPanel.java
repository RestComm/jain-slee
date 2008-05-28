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
package org.mobicents.slee.container.management.console.client.usage;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.Title;
import org.mobicents.slee.container.management.console.client.components.ComponentsServiceAsync;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentTypeInfo;
import org.mobicents.slee.container.management.console.client.components.info.SbbInfo;
import org.mobicents.slee.container.management.console.client.components.info.ServiceInfo;
import org.mobicents.slee.container.management.console.client.services.ServicesServiceAsync;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ParameterSetListPanel extends Composite {

	private UsageServiceAsync usageService = ServerConnection.usageServiceAsync;

	private ServicesServiceAsync servicesService = ServerConnection.servicesServiceAsync;

	ComponentsServiceAsync componentsService = ServerConnection.componentsService;

	private BrowseContainer browseContainer;

	private ControlContainer rootPanel = new ControlContainer();

	private ListBox serviceListBox = new ListBox();

	private ComponentInfo[] serviceInfos;

	private ListBox sbbListBox = new ListBox();

	private ComponentInfo[] sbbInfos;

	private String[] parameterSets;

	private ListPanel parameterSetsPanel = new ListPanel();

	private TextBox createParameterSetTextBox = new TextBox();

	private Button createParameterSetButton = new Button("Create");

	private Button resetAllUsageParameters = new Button(
			"Reset all usage parameters");

	public ParameterSetListPanel(BrowseContainer browseContainer) {
		super();
		this.browseContainer = browseContainer;

		initWidget(rootPanel);
		
		serviceListBox.setWidth("300px");
		sbbListBox.setWidth("300px");

		serviceListBox.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				onServiceListBoxChange();
			}
		});

		sbbListBox.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				onSbbListBoxChange();
			}
		});

		parameterSetsPanel.setHeader(1, "Name");
		parameterSetsPanel.setHeader(2, "Actions");
		parameterSetsPanel.setColumnWidth(1, "100%");

		rootPanel.setWidget(0, 0, new Title("Select a Service and a SBB:", Title.TITLE_LEVEL_2));

		ControlContainer serviceAndSbbSelectPanel = new ControlContainer();
		serviceAndSbbSelectPanel.setText(0, 0, "Service");
		serviceAndSbbSelectPanel.setWidget(0, 1, serviceListBox);
		serviceAndSbbSelectPanel.getCellFormatter().setWidth(0, 1, "100%");
		serviceAndSbbSelectPanel.setText(1, 0, "SBB");
		serviceAndSbbSelectPanel.setWidget(1, 1, sbbListBox);
		serviceAndSbbSelectPanel.getCellFormatter().setWidth(1, 1, "100%");

		resetAllUsageParameters.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onResetAllUsageParameters();
			}
		});

		ControlContainer parameterSetControlsPanel = new ControlContainer();
		parameterSetControlsPanel.setText(0, 0, "New parameter set");
		parameterSetControlsPanel.getCellFormatter().setWordWrap(0, 0, false);
		parameterSetControlsPanel.setWidget(0, 1, createParameterSetTextBox);
		parameterSetControlsPanel.setWidget(0, 2, createParameterSetButton);
		parameterSetControlsPanel.setWidget(0, 3, new Label());
		parameterSetControlsPanel.getCellFormatter().setWidth(0, 3, "100%");
		parameterSetControlsPanel.setWidget(0, 4, resetAllUsageParameters);


		createParameterSetButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onCreateParameterSet(createParameterSetTextBox.getText());
			}
		});

		rootPanel.setWidget(1, 0, serviceAndSbbSelectPanel);
		rootPanel.setWidget(2, 0, new Title("Parameter Sets", Title.TITLE_LEVEL_2));
		rootPanel.setWidget(4, 0, parameterSetControlsPanel);

		refreshData();
	}

	private void onResetAllUsageParameters() {
		ServerCallback callback = new ServerCallback(this) {
			public void onSuccess(Object result) {
				Logger.info("All parameters reset for SBB "
						+ getSelectedSbbInfo().getName() + " within service "
						+ getSelectedServiceInfo().getName());
			}
		};
		Logger.info("Resetting all parameters "
				+ getSelectedServiceInfo().getID() + " - "
				+ getSelectedSbbInfo().getID());
		usageService.resetAllUsageParameters(getSelectedServiceInfo().getID(),
				getSelectedSbbInfo().getID(), callback);
	}

	private void onCreateParameterSet(final String name) {
		if (name == null || name.length() == 0) {
			Logger.error("Name not defined");
			return;
		}

		ServerCallback callback = new ServerCallback(this) {
			public void onSuccess(Object result) {
				createParameterSetTextBox.setText("");
				refreshParameterSets();
				Logger.info("Parameter set " + name + " created for SBB "
						+ getSelectedSbbInfo().getName() + " within service "
						+ getSelectedServiceInfo().getName());
			}
		};

		usageService.createUsageParameterSet(getSelectedServiceInfo().getID(),
				getSelectedSbbInfo().getID(), name, callback);
	}

	private void refreshData() {
		refreshServiceListBox();
	}

	private void refreshParameterSetControls() {
		if (parameterSets == null) {
			createParameterSetTextBox.setEnabled(false);
			createParameterSetButton.setEnabled(false);
			resetAllUsageParameters.setEnabled(false);
		} else {
			createParameterSetTextBox.setEnabled(true);
			createParameterSetButton.setEnabled(true);
			resetAllUsageParameters.setEnabled(true);
		}
	}

	private void refreshServiceListBox() {
		ServerCallback serviceListCallback = new ServerCallback(this) {
			public void onSuccess(Object result) {
				serviceInfos = (ComponentInfo[]) result;
				serviceListBox.clear();
				for (int i = 0; i < serviceInfos.length; i++)
					serviceListBox.addItem(serviceInfos[i].getName());

				onServiceListBoxChange();
			}
		};
		componentsService.getComponentInfos(new ComponentTypeInfo(
				ComponentTypeInfo.SERVICE, 0), serviceListCallback);
	}

	private void onServiceListBoxChange() {
		refreshSbbListBox();
	}

	private void refreshSbbListBox() {
		ServerCallback sbbListCallback = new ServerCallback(this) {
			public void onSuccess(Object result) {
				sbbInfos = (ComponentInfo[]) result;
				sbbListBox.clear();
				for (int i = 0; i < sbbInfos.length; i++)
					sbbListBox.addItem(sbbInfos[i].getName());

				onSbbListBoxChange();
			}
		};

		servicesService.getSbbsWithinService(getSelectedServiceInfo().getID(),
				sbbListCallback);
	}

	private void onSbbListBoxChange() {
		refreshParameterSets();
	}

	private void refreshParameterSets() {

		ComponentInfo serviceInfo = getSelectedServiceInfo();
		ComponentInfo sbbInfo = getSelectedSbbInfo();

		ServerCallback callback = new ServerCallback(this) {
			public void onFailure(Throwable caught) {
				parameterSets = null;
				fillParameterSets();
				super.onFailure(caught);
			}

			public void onSuccess(Object result) {
				parameterSets = (String[]) result;
				fillParameterSets();
			}
		};

		usageService.getParameterSets(serviceInfo.getID(), sbbInfo.getID(),
				callback);

	}

	private void fillParameterSets() {
		if (parameterSets == null) {
			rootPanel.setWidget(3, 0, new Label("(No parameter set defined)"));
			refreshParameterSetControls();
			return;
		}

		parameterSetsPanel.emptyTable();

		for (int i = 0; i < parameterSets.length; i++) {
			parameterSetsPanel.setCell(i, 0, new Image(
					"images/usage.parameterset.gif"));
			final String parameterSet = parameterSets[i];

			Hyperlink nameLink;
			if (parameterSet.length() == 0)
				nameLink = new Hyperlink("default", "default");
			else
				nameLink = new Hyperlink(parameterSet, parameterSet);
			nameLink.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					onParameterSet(parameterSet);
				}
			});
			parameterSetsPanel.setCell(i, 1, nameLink);

			if (parameterSet.length() != 0) {
				Hyperlink removeLink = new Hyperlink("remove", "remove");
				removeLink.addClickListener(new ClickListener() {
					public void onClick(Widget sender) {
						onRemoveParameterSet(parameterSet);
					}
				});
				parameterSetsPanel.setCell(i, 2, removeLink);
			}
		}

		rootPanel.setWidget(3, 0, parameterSetsPanel);
		refreshParameterSetControls();
	}

	private void onParameterSet(String name) {
		ParameterSetPanel parameterSetPanel = new ParameterSetPanel(browseContainer,
				getSelectedServiceInfo(), getSelectedSbbInfo(), name);
		if (name.length() == 0)
			browseContainer.add("default", parameterSetPanel);
		else
			browseContainer.add(name, parameterSetPanel);
	}

	private void onRemoveParameterSet(final String name) {

		ServerCallback callback = new ServerCallback(this) {
			public void onSuccess(Object result) {
				Logger.info("Parameter set " + name + " removed for SBB "
						+ getSelectedSbbInfo().getName() + " within service "
						+ getSelectedServiceInfo().getName());
				refreshParameterSets();
			}
		};

		usageService.removeUsageParameterSet(getSelectedServiceInfo().getID(),
				getSelectedSbbInfo().getID(), name, callback);
	}

	private SbbInfo getSelectedSbbInfo() {
		int iSelectedSbb = sbbListBox.getSelectedIndex();
		if (iSelectedSbb == -1)
			return null;

		return (SbbInfo) sbbInfos[iSelectedSbb];
	}

	private ServiceInfo getSelectedServiceInfo() {
		int iSelectedService = serviceListBox.getSelectedIndex();
		if (iSelectedService == -1)
			return null;

		return (ServiceInfo) serviceInfos[iSelectedService];
	}

}
