/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.management.console.client.components;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentSearchParams;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Vladimir Ralev
 * 
 */
public class SimpleComponentNameLabel extends Composite {
  private String id;

  private Label label;

  private Hyperlink link;

  BrowseContainer browseContainer;

  private ComponentsServiceAsync componentsService = ServerConnection.componentsService;

  public SimpleComponentNameLabel(final String id, final BrowseContainer browseContainer) {
    link = new Hyperlink("", "");
    this.browseContainer = browseContainer;

    if (id != null && id.length() > 0) {
      initWidget(link);
      ServerCallback callback = new ServerCallback(this) {
        public void onSuccess(Object result) {
          final ComponentInfo[] infos = (ComponentInfo[]) result;
          if (infos.length >= 1) {
            link.setText(infos[0].getID());
            ClickListener serviceClickListener = new ClickListener() {
              public void onClick(Widget source) {
                onServiceLabelClick(infos[0]);
              }
            };
            link.addClickListener(serviceClickListener);

          }
          else
            initWidget(new Label("Not Found"));
        }
      };
      try {
        ComponentSearchParams params = new ComponentSearchParams("", id, "", "");
        componentsService.searchComponents(params, callback);
      }
      catch (Exception e) {
        Logger.info(e.toString());
      }
    }
    else {
      initWidget(new Label("-"));
    }
  }

  public void onServiceLabelClick(ComponentInfo componentInfo) {
    ComponentPanel cp = new ComponentPanel(browseContainer, componentInfo);
    browseContainer.add(componentInfo.getID(), cp);
  }
}
