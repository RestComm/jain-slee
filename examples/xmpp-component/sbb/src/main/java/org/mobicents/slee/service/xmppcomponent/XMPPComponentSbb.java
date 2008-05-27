 /*
  * Mobicents: The Open Source SLEE Platform      
  *
  * Copyright 2003-2005, CocoonHive, LLC., 
  * and individual contributors as indicated
  * by the @authors tag. See the copyright.txt 
  * in the distribution for a full listing of   
  * individual contributors.
  *
  * This is free software; you can redistribute it
  * and/or modify it under the terms of the 
  * GNU Lesser General Public License as
  * published by the Free Software Foundation; 
  * either version 2.1 of
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
  * GNU Lesser General Public
  * License along with this software; 
  * if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, 
  * Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site:
  * http://www.fsf.org.
  */

package org.mobicents.slee.service.xmppcomponent;

import java.util.Arrays;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.mobicents.slee.resource.xmpp.XmppResourceAdaptorSbbInterface;



public abstract class XMPPComponentSbb implements javax.slee.Sbb {

	private SbbContext sbbContext; // This SBB's SbbContext
	private XmppResourceAdaptorSbbInterface xmppSbbInterface;  
		
	private final String componentPropertiesFile = "component.properties";
	private final String servicediscoveryPropertiesFile = "servicediscovery.properties";
	private Properties properties;
	
	//which packets should this component "import" to SLEE
	private final Class[] packetsToListen = {Message.class, Presence.class, IQ.class, DiscoverInfo.class, DiscoverItems.class};
	
	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context;
		try {            
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");            
            xmppSbbInterface = (XmppResourceAdaptorSbbInterface) myEnv.lookup("slee/resources/xmpp/2.0/xmppinterface");                        
            properties = new Properties(System.getProperties());
	    } catch (Exception e) {
            e.printStackTrace();
        }    
	}
	
    public void unsetSbbContext() { this.sbbContext = null; }
    
    // TODO: Implement the lifecycle methods if required
    public void sbbCreate() throws javax.slee.CreateException {}
    public void sbbPostCreate() throws javax.slee.CreateException {}
    public void sbbActivate() {}
    public void sbbPassivate() {}
    public void sbbRemove() {}
    public void sbbLoad() {    	
    }
    
    public void sbbStore() {}
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
    public void sbbRolledBack(RolledBackContext context) {}
		
	/**
	 * Convenience method to retrieve the SbbContext object stored in setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove this 
	 * method, the sbbContext variable and the variable assignment in setSbbContext().
	 *
	 * @return this SBB's SbbContext object
	 */
	
	protected SbbContext getSbbContext() {
		return sbbContext;
	}	
	
	/*
	 * Init the xmpp component connection when the service is activated by SLEE
	 */
	public void onStartServiceEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {		           
		try {            
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");                     
            //check if it's my service that is starting
            ServiceActivity sa = ((ServiceActivityFactory) myEnv.lookup("slee/serviceactivity/factory")).getActivity();                       
	    	if (sa.equals(aci.getActivity())) {
	    		//load component properties file to system properties
	    		properties.load(getClass().getResourceAsStream(componentPropertiesFile));
				System.setProperties(properties);
				//connect to xmpp server
        		xmppSbbInterface.connectComponent(
				properties.getProperty("org.mobicents.slee.service.xmppcomponent.CONNECTIONID"),
				properties.getProperty("org.mobicents.slee.service.xmppcomponent.SERVERHOST"),
				Integer.parseInt(properties.getProperty("org.mobicents.slee.service.xmppcomponent.SERVERPORT")),
				properties.getProperty("org.mobicents.slee.service.xmppcomponent.SERVICENAME"),
				properties.getProperty("org.mobicents.slee.service.xmppcomponent.COMPONENTNAME"),
				properties.getProperty("org.mobicents.slee.service.xmppcomponent.COMPONENTSECRET"),
				Arrays.asList(packetsToListen));
        	}
		}
        catch (Exception e) {
        	e.printStackTrace();
        }					
	}
	
	/*
	 * Presence XMPP Packet handler, replies only if the TO is the component
	 * in this case this component accepts all requested subscriptions, no need to register
	 */
	public void onPresence(Presence packet, ActivityContextInterface aci) {
		if(packet.getTo().equalsIgnoreCase(properties.getProperty("org.mobicents.slee.service.xmppcomponent.COMPONENTNAME"))) {
			Presence reply = null;
			if(packet.getType() == Presence.Type.SUBSCRIBE) {
				//return subscribed
				reply = new Presence(Presence.Type.SUBSCRIBED);				
			}
			else 
			if(packet.getType() == Presence.Type.UNSUBSCRIBE) {				
				//return unsubscribed
				reply = new Presence(Presence.Type.UNSUBSCRIBED);				
			}
			if(packet.getType() == Presence.Type.AVAILABLE) {				
				//return available
				reply = new Presence(Presence.Type.AVAILABLE);				
			}
			if(reply != null) {
				reply.setFrom(packet.getTo());
				reply.setTo(packet.getFrom());
				//send to RA
				System.out.println("XMPP Component - sending PRESENCE reply with packet "+reply.toXML());
				xmppSbbInterface.sendPacket(properties.getProperty("org.mobicents.slee.service.xmppcomponent.CONNECTIONID"),reply);
			}
		}
	}
	
	/*
	 * XMPP Service Discovery request handler, replies with info readed from a properties file
	 */
	public void onDiscoverInfo(DiscoverInfo packet, ActivityContextInterface aci) {				
		//always read service discovery info from properties file
		Properties p = new Properties(); 
		try {
			p.load(getClass().getResourceAsStream(servicediscoveryPropertiesFile));
			if(packet.getTo().equalsIgnoreCase(properties.getProperty("org.mobicents.slee.service.xmppcomponent.COMPONENTNAME")) && packet.getType().equals(IQ.Type.GET)) {			
				DiscoverInfo reply = new DiscoverInfo();				
				//add identities from System Properties
				String identityName;
		        String identityCategory;
		        String identityType;        
		        for(int i=1;
		        	(identityName = p.getProperty("org.mobicents.slee.service.xmppcomponent.IDENTITY."+i+".NAME"))!= null &&
					(identityCategory = p.getProperty("org.mobicents.slee.service.xmppcomponent.IDENTITY."+i+".CATEGORY"))!= null &&
					(identityType = p.getProperty("org.mobicents.slee.service.xmppcomponent.IDENTITY."+i+".TYPE"))!= null
				;i++) {
		        	DiscoverInfo.Identity identity = new DiscoverInfo.Identity(identityCategory,identityName);
		            identity.setType(identityType);
		            reply.addIdentity(identity);	            
		        }				
		        //add features from System Properties
		        String feature;	        
		        for(int i=1; (feature = p.getProperty("org.mobicents.slee.service.xmppcomponent.FEATURE."+i))!= null; i++)        	
		        	reply.addFeature(feature);				              
				//set IQ packet
				reply.setTo(packet.getFrom());
				reply.setFrom(packet.getTo());      
				reply.setType(IQ.Type.RESULT);
				reply.setPacketID(packet.getPacketID());				
				//send to RA
				System.out.println("XMPP Component - sending disco info with packet "+reply.toXML());
				xmppSbbInterface.sendPacket(properties.getProperty("org.mobicents.slee.service.xmppcomponent.CONNECTIONID"),reply);
			}					
		} catch (Exception e) {
			e.printStackTrace();            
		}		
		
	}			
	
	/*
	 * Handler to clean system properties when the service is being deactivated
	 */
	
	public void onActivityEndEvent(ActivityEndEvent event, ActivityContextInterface aci) {
		try {
			Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
			//check if it's my service aci that is ending, it may be a client or translator
			ServiceActivity sa = ((ServiceActivityFactory) myEnv.lookup("slee/serviceactivity/factory")).getActivity();                       
			if (sa.equals(aci.getActivity())) {			
				xmppSbbInterface.disconnect(properties.getProperty("org.mobicents.slee.service.xmppcomponent.CONNECTIONID"));		
				Properties p = new Properties(System.getProperties());
				p.remove("org.mobicents.slee.service.xmppcomponent.CONNECTIONID");
				p.remove("org.mobicents.slee.service.xmppcomponent.SERVERHOST");
				p.remove("org.mobicents.slee.service.xmppcomponent.SERVERPORT");
				p.remove("org.mobicents.slee.service.xmppcomponent.SERVICENAME");
				p.remove("org.mobicents.slee.service.xmppcomponent.COMPONENTNAME");
				p.remove("org.mobicents.slee.service.xmppcomponent.COMPONENTSECRET");
				System.setProperties(p);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}	
			
}