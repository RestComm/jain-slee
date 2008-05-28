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

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.CommonControl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class represents logger or possible logger in some fqdn - org.mobicents
 * (two nodes org and mobicents, both can be logger, or neither) This class
 * displays whather node is logger - allows logger init to default values that
 * loggers take and switching logger of. Also on link click it will display
 * information in BrowseContainer - about logger - level, filter, formatter,
 * handlers...
 * 
 * @author baranowb
 * 
 */
public class LogTreeNode extends Composite {

	
	private BrowseContainer parentDisplay=null;
	
	private CommonControl myLogerDetails=null;
	
	private HorizontalPanel myDisplay=new HorizontalPanel();
	
	private String shortName=null;
	
	private String fullName=null;

	private boolean isLogger=false;
	
	private Image stateImage=null;
	
	private Image stateChangeImage=null;
	
	private Hyperlink loggerDetailsLink=null;
	
	//statics 
	
	public static final String _LEVEL_ALL="ALL";
	public static final String _LEVEL_SEVERE="SEVERE";
	public static final String _LEVEL_WARNING="WARNING";
	public static final String _LEVEL_INFO="INFO";
	public static final String _LEVEL_FINE="FINE";
	public static final String _LEVEL_FINER="FINER";
	public static final String _LEVEL_FINEST="FINEST";
	public static final String _LEVEL_CONFIG="CONFIG";
	public static final String _LEVEL_OFF="OFF";

	
	public static final String[] _LEVELS={_LEVEL_ALL,_LEVEL_SEVERE,_LEVEL_WARNING,_LEVEL_INFO,_LEVEL_CONFIG,_LEVEL_FINE,_LEVEL_FINER,_LEVEL_FINEST,_LEVEL_OFF};
	public LogTreeNode(BrowseContainer parentDisplay, String shortName,
			String fullName, boolean isLogger) {
		super();
		try{
		initWidget(myDisplay);
		this.parentDisplay = parentDisplay;
		this.shortName = shortName;
		this.fullName = fullName;
		this.isLogger=isLogger;
		this.init();
		}catch(Exception e)
		{
			Logger.error(e.getMessage());
		}
		
	}
	
	private void init()
	{
		this.myDisplay.clear();
		this.loggerDetailsLink=new Hyperlink();
		
		this.loggerDetailsLink.setHTML(this.shortName);
		this.loggerDetailsLink.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				
				//Create details, add to BrowseContainer
				if(fullName.equals("root"))
				{
					DefaultOperationsPanel dop=new DefaultOperationsPanel();
					parentDisplay.add("Default configuration", dop);
					dop.onShow();
				}
				else
				{
					//Add conf screen for logger
					LoggerDetailsPanel ldtp=new LoggerDetailsPanel(parentDisplay,shortName,fullName);
					parentDisplay.add(shortName,ldtp );
					ldtp.onShow();
				}
			}});
		
		if(this.isLogger /* && Level>Level.OFF */)
		{
			this.stateImage=new Image("images/log.mgmt.logtree.state_active.jpg");
			this.stateChangeImage=new Image("images/log.mgmt.logtree.red_square.jpg");
			
		}else
		{
			this.stateImage=new Image("images/log.mgmt.logtree.state_inactive.jpg");
			this.stateChangeImage=new Image("images/log.mgmt.logtree.green_square.jpg");	
		}
		
		
		
		this.stateChangeImage.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {

			if(fullName.equals("root") || fullName.equals("global"))
				return;
				//USE SERVICE
					
					if(stateImage.getUrl().endsWith("log.mgmt.logtree.state_inactive.jpg") )
					{
		
						AsyncCallback resetLevelCallBack = new AsyncCallback(){

							public void onFailure(Throwable caught) {
								Logger.error("Failed to reset logger level to default due to:"+caught);
								
							}

							public void onSuccess(Object result) {

								if(result.equals(_LEVEL_OFF))
								{
									stateImage.setUrl("images/log.mgmt.logtree.state_inactive.jpg");
									stateChangeImage.setUrl("images/log.mgmt.logtree.green_square.jpg");
								}else
								{
									stateImage.setUrl("images/log.mgmt.logtree.state_active.jpg");
									stateChangeImage.setUrl("images/log.mgmt.logtree.red_square.jpg");
								}
								
								
							}};
							
							ServerConnection.logServiceAsync.resetLoggerLevel(fullName, resetLevelCallBack);

					
					
					}else
					{

						
						AsyncCallback setLevelCallback=new AsyncCallback(){

							public void onFailure(Throwable caught) {
								
								Logger.error("Failed to turn off logger due to:"+caught);
								
							}

							public void onSuccess(Object result) {

									stateImage.setUrl("images/log.mgmt.logtree.state_inactive.jpg");
									stateChangeImage.setUrl("images/log.mgmt.logtree.green_square.jpg");

								//FIXME = do more?
							}};
					
						ServerConnection.logServiceAsync.setLoggerLevel(fullName, _LEVEL_OFF, setLevelCallback);
						
						
						
						
					}
					
				
				
			}});
		
		this.myDisplay.setSpacing(3);
		this.myDisplay.add(this.loggerDetailsLink);
		this.myDisplay.add(this.stateImage);
		this.myDisplay.add(this.stateChangeImage);
		
	}
	
	
}
