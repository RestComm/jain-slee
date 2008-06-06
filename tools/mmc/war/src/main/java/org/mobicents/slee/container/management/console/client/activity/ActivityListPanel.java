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
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.Logger;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;

/**
 * @author Vladimir Ralev
 *
 */
public class ActivityListPanel extends Composite {

    private ActivityServiceAsync service = ServerConnection.activityServiceAsync;

    private BrowseContainer browseContainer;
    
    private ControlContainer rootPanel = new ControlContainer();
    
    private ListPanel listPanel;
    
    private ActivityContextInfo[] activityContextInfos;
        
    public ActivityListPanel(BrowseContainer browseContainer, ActivityContextInfo[] activityContextInfos) {
        super();
        
        this.browseContainer = browseContainer;
        this.activityContextInfos = activityContextInfos;
        
        initWidget(rootPanel);

        setData();
    }
    
    private void setData() {
        
        listPanel = new ListPanel();
        
        listPanel.setHeader(1, "Activity Context ID");
        listPanel.setHeader(2, "TTL (seconds)");
        listPanel.setHeader(3, "Class Name");
        listPanel.setColumnWidth(1, "90%");
        listPanel.setColumnWidth(2, "50");
        
        for (int i = 0; i < activityContextInfos.length; i++) {
            final ActivityContextInfo activityContextInfo = activityContextInfos[i];
            final int index = i;
            
            ActivityContextIdLabelListener listener = 
               new ActivityContextIdLabelListener()
            {
               public void onClick(String id)
               {
                  onActivityClick(activityContextInfo);
               }
            };
            Hyperlink endACLink = new Hyperlink("end", "end");
            
            ClickListener killListener = new ClickListener()
            {
               public void onClick(Widget widget)
               {
                  onEnd(activityContextInfo.getId(), index);
               }
            };
            
            endACLink.addClickListener(killListener);
            
            ActivityContextIdLabel id = 
               new ActivityContextIdLabel(activityContextInfo.getId(), listener);
            listPanel.setCell(i, 0, new Image("images/activity.context.gif"));
            
            listPanel.setCell(i, 1, id);
            listPanel.setCellText(i, 2, "" + activityContextInfo.getTTL());
            listPanel.setCellText(i, 3, "" + activityContextInfo.getActivityClass());
            listPanel.setCell(i, 4, endACLink);
        }
        
        Button livButton = new Button("Query Activity Context Liveness");
        ClickListener livListener = new ClickListener()
        {
           public void onClick(Widget widget)
           {
              onQueryActivityContextLiveness();
           }
        }; 
        livButton.addClickListener(livListener);
        rootPanel.setWidget(0, 0, listPanel);
        rootPanel.setWidget(1, 0, livButton);
    }
    
    private void onActivityClick(final ActivityContextInfo activityContext)
    {
       
       ServerCallback callback = new ServerCallback(this)
       {
          public void onSuccess(Object result)
          {
             ActivityContextInfo ac = (ActivityContextInfo) result;
             ActivityContextDetailsPanel detailsPanel = 
                new ActivityContextDetailsPanel(browseContainer, ac);
             browseContainer.add(getLabelName(activityContext.getId()), detailsPanel);
          }
       };
       service.retrieveActivityContextDetails(activityContext.getId(), callback);
    }
    
    public static String getLabelName(String id)
    {
       if(id.length()>11)
       {
          String name = id.substring(0, 3) +
             ".." + id.substring(id.length() - 8, id.length());
          name = "AC (" + name + ")";
          return name;
       }
       else
          return id;
    }
    
    private void onEnd(final String id, final int index)
    {
       ServerCallback callback = new ServerCallback(this)
       {
          public void onSuccess(Object result)
          {
             listPanel.setCellText(index, 4, "ended");
          }
          public void onFailure(Throwable t)
          {
             Logger.error("Could not kill activity Context ["+id+"]. Reason: "
                   + t.getMessage() + ", " + t.getCause());
          }
       };
       service.endActivity(id, callback);
    }
    
    private void onQueryActivityContextLiveness()
    {
       ServerCallback callback = new ServerCallback(this)
       {
          public void onSuccess(Object result)
          {
             setData();
          }
       };
       service.queryActivityContextLiveness(callback);
    }

}
