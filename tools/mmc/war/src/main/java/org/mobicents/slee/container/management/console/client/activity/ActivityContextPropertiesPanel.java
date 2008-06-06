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

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.components.ComponentSpecificPropertiesPanel;
import org.mobicents.slee.container.management.console.client.components.ComponentsServiceAsync;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentSearchParams;

import org.mobicents.slee.container.management.console.client.resources.*;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntityInfo;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntityPropertiesPanel;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntityLabel;

import com.google.gwt.user.client.ui.*;
/**
 * @author Stefano Zappaterra
 *
 */
public class ActivityContextPropertiesPanel extends PropertiesPanel {
   
    private ActivityServiceAsync activityService = ServerConnection.activityServiceAsync;
    private ResourceServiceAsync resourceService = ServerConnection.resourceServiceAsync;
    
    private BrowseContainer browseContainer;
    
    public ActivityContextPropertiesPanel(BrowseContainer browseContainer, ActivityContextInfo activityInfo) {
        super();
        this.browseContainer = browseContainer;
        add("Id", activityInfo.getId());  
        add("Activity Class", activityInfo.getActivityClass()); 
        add("Last Access Timestamp", activityInfo.getLastAccessTime()); 
        add("Timers", arrayToString(activityInfo.getAttachedTimers())); 
        add("Data Attributes", arrayToString(activityInfo.getDataAttributes())); 
        add("Name Bindings", arrayToString(activityInfo.getNamesBoundTo())); 
        final String raEntityId = activityInfo.getRaEntityId();
        if(raEntityId != null)
        {
           Hyperlink raLink = new Hyperlink(raEntityId, raEntityId);
           ClickListener listener = new ClickListener()
           {
              public void onClick(Widget widget)
              {
                 onRaClick(raEntityId);
              }
           };
           raLink.addClickListener(listener);
           add("RA Entity", raLink);
        }
        else add("RA Entity", "-");
        add("SBB Attachments", arrayToSbbLinksPanel(activityInfo.getSbbAttachments())); 
    }
    
    private static String arrayToString(String[] strings)
    {
       if(strings == null || strings.length == 0) return "-";
       String ret = "";
       for(int q=0; q<strings.length; q++)
          ret += strings[q] + ", ";
       return ret.substring(0, ret.length()-2);
    }
    
    private Widget[] arrayToSbbLinksPanel(String[] sbbs)
    {
       if(sbbs == null || sbbs.length == 0) return new Widget[]{new Label("-")};
       Widget[] widgets = new Widget[sbbs.length];
       for(int q=0; q<sbbs.length; q++)
       {
          HorizontalPanel sbbPanel = new HorizontalPanel();
          String sbbId = sbbs[q];
          Image sbbIcon = new Image("images/components.sbb.gif");
          sbbPanel.add(sbbIcon);
          sbbPanel.add(new SbbEntityLabel(sbbId, null, browseContainer));
          widgets[q] = sbbPanel;
       }
       return widgets;
    }
    
    private void onRaClick(String id)
    {
       final String raEntityIdf = id;

       ServerCallback propCallback = new ServerCallback(this)
       {
          public void onSuccess(Object result) {
             ResourceAdaptorEntityInfo info = (ResourceAdaptorEntityInfo) result;
             ResourceAdaptorEntityPanel raEntityPanel =
                new ResourceAdaptorEntityPanel(browseContainer, info.getName());
             browseContainer.add(info.getName(), raEntityPanel);

          }
       };  
       resourceService.getResourceAdaptorEntityInfo(raEntityIdf, propCallback);
    }
}
