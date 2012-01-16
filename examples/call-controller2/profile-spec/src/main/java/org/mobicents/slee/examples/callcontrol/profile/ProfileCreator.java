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

package org.mobicents.slee.examples.callcontrol.profile;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.management.ProfileProvisioningMBean;

import org.apache.log4j.Logger;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class ProfileCreator {
	private static final String CONF="credential.properties";
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	private static Logger log = Logger.getLogger(ProfileCreator.class);
	public static final String CC2_TABLE = "CallControl";
	public static final String CC2_ProfileSpecID = "ProfileSpecificationID[name=CallControlProfileCMP,vendor=org.mobicents,version=0.1]";
	
	public static void createProfiles() {

		try {
			executor.submit(new Callable<Object>() {

				public Object call() throws Exception {
					_createProfiles();
					return null;
				}
			}).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void removeProfiles() {

		log.info("Removing profiles for CallControl2 example");

		try {
			executor.submit(new Callable<Object>() {

				public Object call() throws Exception {
					_removeProfiles();
					return null;
				}
			}).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void setCallPrincipials(String user, String password) {

		if (user != null) {
			// Set a security context using the SecurityAssociation
			SecurityAssociation.setPrincipal(new SimplePrincipal(user));
			// Set password
			SecurityAssociation.setCredential(password);
		} else {

		}

	}
	
	private static void _removeProfiles() {
		// profile table removal can't be done by sbb
		try {
			ObjectName name = new ObjectName(ProfileProvisioningMBean.OBJECT_NAME); 
			MBeanServer mBeanServer = SleeContainer.lookupFromJndi().getMBeanServer();
			Object[] params = { CC2_TABLE };
			String[] signature = { "java.lang.String" };
			mBeanServer.invoke(name, "removeProfileTable", params, signature);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void _createProfiles() {

		// TODO change this code to be an exmaple of how to create Slee Profiles
		// from an external java app
		log.info("Creating profiles for CallControl2 example");
		
		try {

			//read user and password;
			String user=null;
			String password = null;
			Properties props = new Properties();
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONF));
			Iterator<Object> credentialsKeys = props.keySet().iterator();
			if(credentialsKeys.hasNext())
			{
				user = (String) credentialsKeys.next();
				password = props.getProperty(user);
				
			}
			
			String jbossBindAddress = System.getProperty("jboss.bind.address");
			
			Hashtable env = new Hashtable();
			env.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
			env.put("java.naming.provider.url", "jnp://"+jbossBindAddress);
			env.put("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
			env.put(Context.SECURITY_CREDENTIALS   , password);
			env.put(Context.SECURITY_PRINCIPAL     , user);
			RMIAdaptor adaptor = (RMIAdaptor) new InitialContext(env).lookup("jmx/rmi/RMIAdaptor");
			//init SCI, pass credentials
			//SleeCommandInterface sci = new SleeCommandInterface("jnp://"+ System.getProperty("jboss.bind.address") + ":1099", user, password);
			SleeCommandInterface sci = new SleeCommandInterface(adaptor,user,password);
			
			String controllerProfileSpecID = "ProfileSpecificationID[name=CallControlProfileCMP,vendor=org.mobicents,version=0.1]";
			String profileTableName = "CallControl";
			String domain = System.getProperty("jboss.bind.address","127.0.0.1");
						
			try{
				setCallPrincipials( user,  password);
				sci.invokeOperation("-removeProfileTable", profileTableName, null, null);
			}catch(Exception e)
			{
				if(log.isDebugEnabled())
					e.printStackTrace();
			}
			
			// create profile table
			sci.invokeOperation("-createProfileTable", controllerProfileSpecID,profileTableName, null);

			
			log.info("*** AddressProfileTable " + profileTableName + " created.");

			Address[] blockedAddresses = {new Address(AddressPlan.SIP, "sip:mobicents@"+domain),new Address(AddressPlan.SIP, "sip:hugo@"+domain)};
			newProfile(adaptor,sci,profileTableName, "torosvi", "sip:torosvi@"+domain, blockedAddresses, null, true, user,password);
			log.info("********** CREATED PROFILE: torosvi **********");
			
			newProfile(adaptor,sci,profileTableName, "mobicents", "sip:mobicents@"+domain, null, null, false,user,password);
			log.info("********** CREATED PROFILE: mobicents **********");
			
			Address backupAddress = new Address(AddressPlan.SIP, "sip:torosvi@"+domain);
			newProfile(adaptor,sci,profileTableName, "victor", "sip:victor@"+domain, null, backupAddress, false,user,password);
			log.info("********** CREATED PROFILE: victor **********");
			
			newProfile(adaptor,sci,profileTableName, "vhros2", "sip:vhros2@"+domain, null, null, true,user,password);
			log.info("********** CREATED PROFILE: vhros2 **********");
			
			newProfile(adaptor,sci,profileTableName, "vmail", "sip:vmail@"+domain, null, null, true,user,password);
			log.info("********** CREATED PROFILE: vmail **********");
			
			log.info("Finished creation of call-controller2 Profiles!");
	
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Failed to create call-controller2 Profiles!",e);
			
		}
	
	}
	private static void newProfile(RMIAdaptor adaptor, SleeCommandInterface sci, String profileTableName, String profileName,
			String callee, Address[] block, Address backup, boolean state, String user, String password)
			throws Exception {
		
		ObjectName profileObjectName = (ObjectName) sci.invokeOperation("-createProfile",
				profileTableName, profileName, null);
		log.info("*** AddressProfile " + profileName + " created: " + profileObjectName);
		setCallPrincipials( user,  password);
		if (!(Boolean) adaptor.getAttribute(profileObjectName, "ProfileWriteable")) {
			Object[] o = new Object[] {};
			setCallPrincipials( user,  password);
			adaptor.invoke(profileObjectName, "editProfile", o, new String[] {});
			log.info("*** Setting profile editable.");
		} else {
			log.info("********* Profile is editable.");
		}
		// Setting and Committing
		Address userAddress = new Address(AddressPlan.SIP, callee);
		Attribute userAttr = new Attribute("UserAddress", userAddress);
		Attribute blockedAttr = new Attribute("BlockedAddresses", block);
		Attribute backupAttr = new Attribute("BackupAddress", backup);
		Attribute voicemailAttr = new Attribute("VoicemailState", state);
		setCallPrincipials( user,  password);
		adaptor.setAttribute(profileObjectName, userAttr);
		setCallPrincipials( user,  password);
		adaptor.setAttribute(profileObjectName, blockedAttr);
		setCallPrincipials( user,  password);
		adaptor.setAttribute(profileObjectName, backupAttr);
		setCallPrincipials( user,  password);
		adaptor.setAttribute(profileObjectName, voicemailAttr);
		log.info("*** Profile modifications are not committed yet.");
		setCallPrincipials( user,  password);
		adaptor.invoke(profileObjectName, "commitProfile", new Object[] {},new String[] {});
		log.info("*** Profile modifications are committed.");
	}

}
