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

package org.mobicents.slee.container.management.console.client.usage;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.common.Title;
import org.mobicents.slee.container.management.console.client.components.info.SbbInfo;
import org.mobicents.slee.container.management.console.client.components.info.ServiceInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ParameterSetPanel extends Composite {
  private UsageServiceAsync usageService = ServerConnection.usageServiceAsync;

  private ServiceInfo serviceInfo;

  private SbbInfo sbbInfo;

  private String parameterSetName;

  private boolean isDefault;

  private ControlContainer rootPanel = new ControlContainer();

  private ListPanel counterTypeUsageParametersPanel = new ListPanel();

  private ListPanel sampleTypeUsageParametersPanel = new ListPanel();

  public ParameterSetPanel(BrowseContainer browseContainer, ServiceInfo serviceInfo, SbbInfo sbbInfo, String parameterSetName) {
    super();
    this.serviceInfo = serviceInfo;
    this.sbbInfo = sbbInfo;
    this.parameterSetName = parameterSetName;

    if (parameterSetName.length() == 0)
      isDefault = true;
    else
      isDefault = false;

    initWidget(rootPanel);

    PropertiesPanel propertiesPanel = new PropertiesPanel();
    propertiesPanel.add("Service", serviceInfo.getName());
    propertiesPanel.add("SBB", sbbInfo.getName());
    if (isDefault)
      propertiesPanel.add("Parameter set name", "default");
    else
      propertiesPanel.add("Parameter set name", parameterSetName);

    Hyperlink refreshLink = new Hyperlink("refresh", "refresh");
    refreshLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        refreshUsageParameters();
      }
    });

    ControlContainer counterTypeTitlePanel = new ControlContainer();
    counterTypeTitlePanel.setWidget(0, 0, new Image("images/usage.parameterset.gif"));
    counterTypeTitlePanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_BOTTOM);
    counterTypeTitlePanel.setWidget(0, 1, new Title("Counter type usage parameters", Title.TITLE_LEVEL_2));
    counterTypeTitlePanel.getCellFormatter().setWidth(0, 1, "100%");
    counterTypeTitlePanel.setWidget(0, 2, new Image("images/refresh.gif"));
    counterTypeTitlePanel.setWidget(0, 3, refreshLink);

    counterTypeUsageParametersPanel.setHeader(1, "Name");
    counterTypeUsageParametersPanel.setHeader(2, "Value");
    counterTypeUsageParametersPanel.setHeader(3, "Actions");
    counterTypeUsageParametersPanel.setColumnWidth(2, "100%");

    ControlContainer sampleTypeTitlePanel = new ControlContainer();
    sampleTypeTitlePanel.setWidth("");
    sampleTypeTitlePanel.setWidget(0, 0, new Image("images/usage.parameterset.gif"));
    sampleTypeTitlePanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_BOTTOM);
    sampleTypeTitlePanel.setWidget(0, 1, new Title("Sample type usage parameters", Title.TITLE_LEVEL_2));

    sampleTypeUsageParametersPanel.setHeader(1, "Name");
    sampleTypeUsageParametersPanel.setHeader(2, "Minimum");
    sampleTypeUsageParametersPanel.setHeader(3, "Mean");
    sampleTypeUsageParametersPanel.setHeader(4, "Maximum");
    sampleTypeUsageParametersPanel.setHeader(5, "Sample count");
    sampleTypeUsageParametersPanel.setHeader(6, "Actions");
    sampleTypeUsageParametersPanel.setColumnWidth(5, "100%");

    Button resetAllUsageParametersButton = new Button("Reset all usage parameters");
    resetAllUsageParametersButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        onResetAllUsageParameters();
      }
    });

    rootPanel.setWidget(0, 0, propertiesPanel);
    rootPanel.setWidget(1, 0, counterTypeTitlePanel);
    rootPanel.setWidget(2, 0, counterTypeUsageParametersPanel);
    rootPanel.setWidget(3, 0, sampleTypeTitlePanel);
    rootPanel.setWidget(4, 0, sampleTypeUsageParametersPanel);
    rootPanel.setWidget(5, 0, resetAllUsageParametersButton);

    rootPanel.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);

    refreshUsageParameters();
  }

  private void refreshUsageParameters() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        counterTypeUsageParametersPanel.emptyTable();
        sampleTypeUsageParametersPanel.emptyTable();
        UsageParameterInfo[] usageParameterInfos = (UsageParameterInfo[]) result;
        for (int i = 0; i < usageParameterInfos.length; i++) {
          final UsageParameterInfo usageParameterInfo = usageParameterInfos[i];
          Hyperlink resetLink = new Hyperlink("reset", "reset");

          if (GWT.getTypeName(usageParameterInfo).equals("org.mobicents.slee.container.management.console.client.usage.CounterTypeUsageParameterInfo")) {
            CounterTypeUsageParameterInfo counterTypeSBBUsageParameterInfo = (CounterTypeUsageParameterInfo) usageParameterInfo;
            int row = counterTypeUsageParametersPanel.getRowCount();
            counterTypeUsageParametersPanel.setCell(row, 0, new Image("images/usage.parameter.gif"));
            counterTypeUsageParametersPanel.setCellText(row, 1, counterTypeSBBUsageParameterInfo.getName());
            counterTypeUsageParametersPanel.setCellText(row, 2, Long.toString((counterTypeSBBUsageParameterInfo.getValue())));
            counterTypeUsageParametersPanel.setCell(row, 3, resetLink);

            resetLink.addClickListener(new ClickListener() {
              public void onClick(Widget sender) {
                onResetParameter(usageParameterInfo.getName(), true);
              }
            });
          }

          if (GWT.getTypeName(usageParameterInfo).equals("org.mobicents.slee.container.management.console.client.usage.SampleTypeUsageParameterInfo")) {
            SampleTypeUsageParameterInfo sampleTypeSBBUsageParameterInfo = (SampleTypeUsageParameterInfo) usageParameterInfo;
            int row = sampleTypeUsageParametersPanel.getRowCount();
            sampleTypeUsageParametersPanel.setCell(row, 0, new Image("images/usage.parameter.gif"));
            sampleTypeUsageParametersPanel.setCellText(row, 1, sampleTypeSBBUsageParameterInfo.getName());
            sampleTypeUsageParametersPanel.setCellText(row, 2, Long.toString((sampleTypeSBBUsageParameterInfo.getMinimum())));
            sampleTypeUsageParametersPanel.setCellText(row, 3, Double.toString((sampleTypeSBBUsageParameterInfo.getMean())));
            sampleTypeUsageParametersPanel.setCellText(row, 4, Long.toString((sampleTypeSBBUsageParameterInfo.getMaximum())));
            sampleTypeUsageParametersPanel.setCellText(row, 5, Long.toString((sampleTypeSBBUsageParameterInfo.getSampleCount())));
            sampleTypeUsageParametersPanel.setCell(row, 6, resetLink);

            resetLink.addClickListener(new ClickListener() {
              public void onClick(Widget sender) {
                onResetParameter(usageParameterInfo.getName(), false);
              }
            });
          }
        }
      }
    };

    usageService.getSBBUsageParameters(serviceInfo.getID(), sbbInfo.getID(), parameterSetName, callback);
  }

  private void onResetAllUsageParameters() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("All usage parameters of parameter set " + parameterSetName + " have been reset");
        refreshUsageParameters();
      }
    };
    usageService.resetAllUsageParameters(serviceInfo.getID(), sbbInfo.getID(), parameterSetName, callback);
  }

  private void onResetParameter(final String parameterName, boolean isCounterType) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Usage parameter " + parameterName + " has been reset");
        refreshUsageParameters();
      }
    };
    usageService.resetUsageParameter(serviceInfo.getID(), sbbInfo.getID(), parameterSetName, parameterName, isCounterType, callback);
  }
}
