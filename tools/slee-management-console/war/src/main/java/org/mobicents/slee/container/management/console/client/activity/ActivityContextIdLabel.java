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

package org.mobicents.slee.container.management.console.client.activity;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ActivityContextIdLabel extends Composite {

  private String id;

  private Label label;

  private Hyperlink link;

  @SuppressWarnings("deprecation") // GWT 1.x vs 2.x
  public ActivityContextIdLabel(final String id, final ActivityContextIdLabelListener listener) {

    if (id != null && id.length() > 0) {
      this.id = (String) id;
      String name = ActivityListPanel.getLabelName(this.id);
      link = new Hyperlink(name, name);
      initWidget(link);

      link.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          listener.onClick(id);
        }
      });
      link.setText(name);

    }
    else {
      initWidget(new Label("-"));
    }
  }

  public ActivityContextIdLabel(String id) {
    if (id != null && id.length() > 0) {
      label = new Label(id);
      initWidget(label);

      id = (String) id;
      label.setText(id);
    }
    else {
      initWidget(new Label("-"));
    }
  }

  public static ActivityContextIdLabel[] toArray(String[] ids, ActivityContextIdLabelListener listener) {
    if (ids == null || ids.length == 0)
      return null;

    ActivityContextIdLabel[] activityContextLabels = new ActivityContextIdLabel[ids.length];
    for (int i = 0; i < ids.length; i++) {
      activityContextLabels[i] = new ActivityContextIdLabel(ids[i], listener);
    }

    return activityContextLabels;
  }

  public static ActivityContextIdLabel[] toArray(String[] ids) {
    if (ids == null || ids.length == 0)
      return null;

    ActivityContextIdLabel[] activityContextLabels = new ActivityContextIdLabel[ids.length];
    for (int i = 0; i < ids.length; i++) {
      activityContextLabels[i] = new ActivityContextIdLabel(ids[i]);
    }

    return activityContextLabels;
  }

}
