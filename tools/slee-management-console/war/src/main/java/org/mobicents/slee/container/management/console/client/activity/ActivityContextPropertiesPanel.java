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

package org.mobicents.slee.container.management.console.client.activity;

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.resources.ResourceAdaptorEntityInfo;
import org.mobicents.slee.container.management.console.client.resources.ResourceAdaptorEntityPanel;
import org.mobicents.slee.container.management.console.client.resources.ResourceServiceAsync;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntityLabel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ActivityContextPropertiesPanel extends PropertiesPanel {

  // private ActivityServiceAsync activityService = ServerConnection.activityServiceAsync;
  private ResourceServiceAsync resourceService = ServerConnection.resourceServiceAsync;

  private BrowseContainer browseContainer;

  @SuppressWarnings("deprecation") // GWT 1.x vs 2.x
  public ActivityContextPropertiesPanel(BrowseContainer browseContainer, ActivityContextInfo activityInfo) {
    super();
    this.browseContainer = browseContainer;
    add("ID", activityInfo.getId());
    add("Activity Class", activityInfo.getActivityClass());
    add("Last Access Timestamp", activityInfo.getLastAccessTime());
    add("Is Ending", activityInfo.getIsEnding());
    add("Timers", arrayToString(activityInfo.getAttachedTimers()));
    add("Data Attributes", arrayToString(activityInfo.getDataAttributes()));
    add("Name Bindings", arrayToString(activityInfo.getNamesBoundTo()));
    final String raEntityId = activityInfo.getRaEntityId();
    if (raEntityId != null) {
      Hyperlink raLink = new Hyperlink(raEntityId, raEntityId);
      raLink.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          onRaClick(raEntityId);
        }
      });
      add("RA Entity", raLink);
    }
    else
      add("RA Entity", "-");
    add("SBB Attachments", arrayToSbbLinksPanel(activityInfo.getSbbAttachments()));
  }

  private static String arrayToString(String[] strings) {
    if (strings == null || strings.length == 0)
      return "-";
    String ret = "";
    for (int q = 0; q < strings.length; q++)
      ret += strings[q] + ", ";
    return ret.substring(0, ret.length() - 2);
  }

  private Widget[] arrayToSbbLinksPanel(String[] sbbs) {
    if (sbbs == null || sbbs.length == 0)
      return new Widget[] { new Label("-") };
    Widget[] widgets = new Widget[sbbs.length];
    for (int q = 0; q < sbbs.length; q++) {
      HorizontalPanel sbbPanel = new HorizontalPanel();
      String sbbId = sbbs[q];
      Image sbbIcon = new Image("images/components.sbb.gif");
      sbbPanel.add(sbbIcon);
      sbbPanel.add(new SbbEntityLabel(sbbId, null, browseContainer));
      widgets[q] = sbbPanel;
    }
    return widgets;
  }

  private void onRaClick(String id) {
    final String raEntityIdf = id;

    ServerCallback propCallback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        ResourceAdaptorEntityInfo info = (ResourceAdaptorEntityInfo) result;
        ResourceAdaptorEntityPanel raEntityPanel = new ResourceAdaptorEntityPanel(browseContainer, info.getName());
        browseContainer.add(info.getName(), raEntityPanel);

      }
    };
    resourceService.getResourceAdaptorEntityInfo(raEntityIdf, propCallback);
  }
}
