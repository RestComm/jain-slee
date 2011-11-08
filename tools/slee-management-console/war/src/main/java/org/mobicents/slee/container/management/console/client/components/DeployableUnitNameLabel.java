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

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitsServiceAsync;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitNameLabel extends Composite {

  private String name;

  private Label label;

  // private Hyperlink link;

  private DeployableUnitsServiceAsync service = ServerConnection.deployableUnitsService;

  /*
   * public DeployableUnitNameLabel(final String id, final ComponentNameClickListener listener) {
   * 
   * if (id != null && id.length() > 0) { link = new Hyperlink(id, id); initWidget(link);
   * 
   * link.addClickListener(new ClickListener() { public void onClick(Widget sender) { listener.onClick(id, name); } });
   * 
   * ServerCallback callback = new ServerCallback(this) { public void onSuccess(final Object result) { name = (String) result;
   * link.setText(name); } }; service.getComponentName(id, callback); } else { initWidget(new Label("-")); } }
   */
  public DeployableUnitNameLabel(String id) {
    if (id != null && id.length() > 0) {
      label = new Label(id);
      initWidget(label);

      ServerCallback callback = new ServerCallback(this) {
        public void onSuccess(final Object result) {
          name = (String) result;
          label.setText(name);
        }
      };
      service.getDeployableUnitName(id, callback);
    }
    else {
      initWidget(new Label("-"));
    }
  }
}
