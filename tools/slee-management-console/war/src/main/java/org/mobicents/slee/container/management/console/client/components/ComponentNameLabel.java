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
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ComponentNameLabel extends Composite {

  private String name;

  private Label label;

  private Hyperlink link;

  private BrowseContainer browseContainer;

  private ComponentsServiceAsync service = ServerConnection.componentsService;

  public ComponentNameLabel(final String id, final ComponentNameClickListener listener) {

    if (id != null && id.length() > 0) {
      link = new Hyperlink(id, id);
      initWidget(link);

      link.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          listener.onClick(id, name);
        }
      });

      ServerCallback callback = new ServerCallback(this) {
        public void onSuccess(final Object result) {
          name = (String) result;
          link.setText(name);
        }
      };
      service.getComponentName(id, callback);
    }
    else {
      initWidget(new Label("-"));
    }
  }

  public ComponentNameLabel(final String id, final BrowseContainer browseContainer) {
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
                onComponentNameLabelClick(infos[0]);
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
        service.searchComponents(params, callback);
      }
      catch (Exception e) {
        Logger.info(e.toString());
      }
    }
    else {
      initWidget(new Label("-"));
    }
  }

  public ComponentNameLabel(String id) {
    if (id != null && id.length() > 0) {
      label = new Label(id);
      initWidget(label);

      ServerCallback callback = new ServerCallback(this) {
        public void onSuccess(final Object result) {
          name = (String) result;
          label.setText(name);
        }
      };
      service.getComponentName(id, callback);
    }
    else {
      initWidget(new Label("-"));
    }
  }

  public static ComponentNameLabel[] toArray(String[] ids, ComponentNameClickListener listener) {
    if (ids == null || ids.length == 0)
      return null;

    ComponentNameLabel[] componentNameLabels = new ComponentNameLabel[ids.length];
    for (int i = 0; i < ids.length; i++) {
      componentNameLabels[i] = new ComponentNameLabel(ids[i], listener);
    }

    return componentNameLabels;
  }

  public static ComponentNameLabel[] toArray(String[] ids, BrowseContainer browseContainer) {
    if (ids == null || ids.length == 0)
      return null;

    ComponentNameLabel[] componentNameLabels = new ComponentNameLabel[ids.length];
    for (int i = 0; i < ids.length; i++) {
      componentNameLabels[i] = new ComponentNameLabel(ids[i], browseContainer);
    }

    return componentNameLabels;
  }

  public static ComponentNameLabel[] toArray(String[] ids) {
    if (ids == null || ids.length == 0)
      return null;

    ComponentNameLabel[] componentNameLabels = new ComponentNameLabel[ids.length];
    for (int i = 0; i < ids.length; i++) {
      componentNameLabels[i] = new ComponentNameLabel(ids[i]);
    }

    return componentNameLabels;
  }

  public void onComponentNameLabelClick(ComponentInfo componentInfo) {
    ComponentPanel cp = new ComponentPanel(browseContainer, componentInfo);
    browseContainer.add(componentInfo.getID(), cp);
  }
}
