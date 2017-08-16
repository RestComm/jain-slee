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

package org.mobicents.slee.container.management.console.client.sbb.entity;

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Vladimir Ralev
 * 
 */
public class SbbEntityLabel extends Composite {
  private String id;

  private Label label;

  private Hyperlink link;

  BrowseContainer browseContainer;

  private SbbEntitiesServiceAsync service = ServerConnection.sbbEntitiesServiceAsync;

  public SbbEntityLabel(final String id, String parentSbbeId, final BrowseContainer browseContainer) {
    this.browseContainer = browseContainer;
    if (id.equals(parentSbbeId))
      initWidget(new Label(id + " (same)"));
    else if (id != null && id.length() > 0) {
      String name = getLabelName(id);
      link = new Hyperlink(name, name);
      initWidget(link);

      link.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onEntityLabelClick(id, id);
        }
      });
    }
    else {
      initWidget(new Label("-"));
    }

  }

  public static String getLabelName(String id) {
    if (id.length() > 11) {
      String name = id.substring(0, 3) + ".." + id.substring(id.length() - 8, id.length());
      name = "SBB Entity (" + name + ")";
      return name;
    }
    else
      return id;
  }

  public void onEntityLabelClick(final String id, String name) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        SbbEntityInfo info = (SbbEntityInfo) result;
        SbbEntityDetailsPanel sbbProperties = new SbbEntityDetailsPanel(browseContainer, info);
        browseContainer.add(getLabelName(id), sbbProperties);
      }
    };
    service.retrieveSbbEntityInfo(id, callback);
  }

}
