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
package org.mobicents.slee.container.management.console.client.activity;

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ActivityContextIdLabel extends Composite {
    
    private String id;

    private Label label;

    private Hyperlink link;

    public ActivityContextIdLabel(final String id,
            final ActivityContextIdLabelListener listener) {

        if (id != null && id.length() > 0) {
            String name = ActivityListPanel.getLabelName(id);
            link = new Hyperlink(name, name);
            initWidget(link);

            link.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    listener.onClick(id);
                }
            });
            this.id = (String) id;
            link.setText(name);

        } else {
            initWidget(new Label("-"));
        }
    }

    public ActivityContextIdLabel(String id) {
        if (id != null && id.length() > 0) {
            label = new Label(id);
            initWidget(label);

            id = (String) id;
            label.setText(id);
        } else {
            initWidget(new Label("-"));
        }
    }

    public static ActivityContextIdLabel[] toArray(String[] ids,
          ActivityContextIdLabelListener listener) {
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
