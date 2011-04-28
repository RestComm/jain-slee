/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.examples.callcontrol.common;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.RequestEvent;
import javax.sip.address.AddressFactory;
import javax.sip.header.CallIdHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.Tracer;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import net.java.slee.resource.sip.SleeSipProvider;

import org.apache.log4j.Logger;
import org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP;



/**
 * Base SBB offering profile lookup funcationality
 * @author torosvi
 * @author iivanov
 *
 */
public abstract class SubscriptionProfileSbb implements Sbb {
	
	//protected transient Logger log = Logger.getLogger(this.getClass());
	private Tracer log;
	
    public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		this.log = this.sbbContext.getTracer("SubscriptionProfileSbb");
		try {
			//"If NamingException is thrown check jmx-console -> JNDIView -> 
			// list or sbb-jar (check entity-bindning) for proper JNDI path!!!!"
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
            // Getting JAIN SIP Resource Adaptor interfaces
			fp = (SleeSipProvider) myEnv.lookup("slee/resources/jainsip/1.2/provider");
            // To create Address objects from a particular implementation of JAIN SIP
            addressFactory = fp.getAddressFactory();
	        // To create Request and Response messages from a particular implementation of JAIN SIP    	                       
            messageFactory = fp.getMessageFactory();
            // To allow SBB entities to interrogate the profile database to find
            // profiles that match a selection criteria         
            profileFacility = (ProfileFacility) myEnv.lookup("slee/facilities/profile");
            
        } catch (NamingException ne){
        	log.severe("COULD NOT LOCATE RESOURCE IN JNDI: Check JNDI TREE or entity-binding for proper path!!!", ne);
        }		      
	}
    
    /**
     * Looking up the called user data -> First we need a Profile CMP object to
     * access them.
     * @param address: Called user address.
     * @return CallControlProfileCMP: The SBB uses the returned Profile CMP object
     * to access the attributes of the Profile identified by the profileID.
     */
    protected CallControlProfileCMP lookup(javax.slee.Address address) {
		String profileTableName = new String();
    	//ProfileID profileID = null;
    	CallControlProfileCMP profile = null;
    	 	
    	try {
    		profileTableName = "CallControl";
    		
    		ProfileTable table = getProfileFacility().getProfileTable(profileTableName);
    		ProfileLocalObject plo=table.findProfileByAttribute("userAddress", address);
    		profile = (CallControlProfileCMP) plo;
    		
    		
        }catch (NullPointerException  e) {
        	log.severe("Exception using the getProfileByIndexedAttribute method", e);
        }catch (UnrecognizedProfileTableNameException e) {
        	log.severe("Exception in getting the Profile Specification in getControllerProfileCMP(profileID):" +
        			"The ProfileID object does not identify a Profile Table created from the Profile Specification", e);
        }catch (TransactionRolledbackLocalException e) {
        	log.severe(e.getMessage(), e);
		}catch (FacilityException e) {
			log.severe(e.getMessage(), e);
		}
        
        return profile;
	}
	
    public void unsetSbbContext() { this.sbbContext = null; }

    public void sbbCreate() throws CreateException {  }

    public void sbbPostCreate() throws CreateException { }

    public void sbbRemove() { }

    public void sbbPassivate() { }

    public void sbbActivate() { }

    public void sbbLoad() { }

    public void sbbStore() { }

    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface aci) { }

    public void sbbRolledBack(RolledBackContext context) { }

    protected final SbbContext getSbbContext() { return sbbContext; }

    protected final ProfileFacility getProfileFacility() { return profileFacility; }

    protected final SbbLocalObject getSbbLocalObject() { return sbbContext.getSbbLocalObject(); }
    
    protected final SleeSipProvider getSipFactoryProvider() { return fp; }
    
    protected final AddressFactory getAddressFactory() { return addressFactory; }
    
    protected final MessageFactory getMessageFactory() { return messageFactory; }
    	
	private SbbContext sbbContext;
	private SleeSipProvider fp;
	private AddressFactory addressFactory;
	private MessageFactory messageFactory;
	private ProfileFacility profileFacility;
	
	public abstract CallControlProfileCMP getCallControlProfileCMP(javax.slee.profile.ProfileID profileID) 
			throws UnrecognizedProfileNameException, UnrecognizedProfileTableNameException;

	public InitialEventSelector callIDSelect(InitialEventSelector ies) {
		Object event = ies.getEvent();
		String callID = null;
		
		if (event instanceof RequestEvent) {
			// If request event, the convergence name to callId
			Request request = ((RequestEvent) event).getRequest();
			callID = ((CallIdHeader) request.getHeader(CallIdHeader.NAME)).getCallId();
		}

		ies.setCustomName(callID);
		
        return ies;
    }
	
}
