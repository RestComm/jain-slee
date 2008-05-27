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
package org.mobicents.slee.container.management.console.client.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.CommonControl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * This class shows TreeStructure of loggers, allows browsing them in through BrowseCOntainer class.
 * @author baranowb
 *
 */
public class LogStructureTreePanel extends Composite implements CommonControl{

	
	
	//We are displayed in this component, also log links will make this component display us.
	private BrowseContainer browseContainer;
	private final  Tree logTree=new Tree();
	
	
	
	public LogStructureTreePanel(BrowseContainer browseContainer) {
		super();
		this.browseContainer = browseContainer;
		browseContainer.add("Logger Configuration", this.logTree);
		ScrollPanel scroll=new ScrollPanel();
		scroll.setHeight("500px");
		scroll.setWidth("630px");
		scroll.add(browseContainer);
		initWidget(scroll);
		
	}


	public void onHide() {
		// TODO Auto-generated method stub
		
	}

	
	public void onInit() {
		refreshData();
		
	}

	
	public void onShow() {
	
		
	}

	private void refreshData()
	{
		//final Tree t1 = logTree;
		//ArrayList names = new ArrayList();
		//names.add("org");
		//names.add("org.mobicents.slee");
		//names.add("org.apache");
		//names.add("org.mobicents");
		//names.add("org.mobicents.slee.container");
		//names.add("org.mobicents.slee.container.management.jmx.MBEAN");
		//names.add("org.mobicents.slee.container.management.jmx.ABEAN");
		//names.add("org.mobicents.slee.container.management.xmx.MBEAN");
		//names.add("org.mobicents.slee.container.management.xmx.ABEAN");
		//names.add("org.mobicents.slee.container.fun.jmx.MBEAN");
		//names.add("org.mobicents.slee.container.fun.jmx.ABEAN");
		//names.add("org.mobicents.slee.container.fun.xmx.MBEAN");
		//names.add("org.mobicents.slee.container.fun.xmx.ABEAN");
		//names.add("org.mobicents.mbean.container");
		//names.add("org.mobicents.mbean.container.management");
		//names.add("org.mobicents.mbean.container.management.jmx.MBEAN");
		//names.add("org.mobicents.mbean.container.management.jmx.ABEAN");
		//names.add("com");
		//names.add("com.mobicents.slee");
		//names.add("com.apache");

		
		AsyncCallback refreshCallback = new AsyncCallback()
		{

			public void onFailure(Throwable caught) {
				Logger.error("Failed to create tree of loggers!!!["+caught+"]");
				
			}

			public void onSuccess(Object result) {

				List names=(List)result;
				
				names.remove("");
				Iterator it=(names).iterator();
				FQDNNode root=new FQDNNode(false,"root","root");
				while(it.hasNext())
				{
					
					String s=(String) it.next();

					root.addNode(s.split("\\."), s, 0);
				}
		
				logTree.clear();
	
			
				logTree.addItem(doTree(root));
	
			}
			
			
		};
	
		ServerConnection.logServiceAsync.getLoggerNames(refreshCallback);
	
	}
	
//TMP DEV METHODS
	
	private  TreeItem doTree(FQDNNode localRoot)
	{
		
		TreeItem localLeaf=new TreeItem();

		LogTreeNode logTreeNode=new LogTreeNode(browseContainer,localRoot.getShortName(),localRoot.getFqdName(),localRoot.isWasLeaf());
	
		localLeaf.setWidget(logTreeNode);

		if(localRoot.getChildren().size()>0)
		{
	
			Tree t=new Tree();
			
			ArrayList names=new ArrayList(localRoot.getChildrenNames());
			Collections.sort(names);
			Iterator it=names.iterator();
			while(it.hasNext())
			{
				t.addItem(doTree(localRoot.getChild((String) it.next())));
			}
			localLeaf.addItem(t);
		}
		
		return localLeaf;
		
	}
	
}
