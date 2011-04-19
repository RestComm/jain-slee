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

package org.mobicents.slee.container.management.jmx;

import java.net.URL;
import java.util.Hashtable;

import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.ProfileProvisioningMBean;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.SleeManagementMBean;
import javax.slee.resource.ConfigProperties;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.logging.Logger;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;

public class SleeCommandInterface {

	private static Logger logger = Logger
			.getLogger(org.mobicents.slee.container.management.jmx.SleeCommandInterface.class
					.getName());

	protected RMIAdaptor rmiserver = null;
	protected String user,password;
	public String commandBean = null;

	public String commandString = null;

	public SleeCommandInterface() {

	}
	/**
	 * Constructor
	 */
	public SleeCommandInterface(RMIAdaptor rmiserver,String user, String password) {
		this.rmiserver = rmiserver;
		this.user = user;
		this.password = password;
		setCallPrincipials();
		
	}
	
	/**
	 * Constructor that takes a JNDI url
	 * 
	 * @param jndiurl
	 *            JNDI Url (jnp://localhost:1099)
	 */
	public SleeCommandInterface(String jndiurl) throws Exception {
		init(jndiurl, null, null);

	}

	public SleeCommandInterface(String jndiurl, String user, String password)
			throws Exception {
		init(jndiurl, user, password);

	}

	private void init(String jndiurl, String user, String password)
			throws Exception {
		this.user = user;
		this.password = password;
		// Set Some JNDI Properties
		Hashtable env = new Hashtable();
		env.put(Context.PROVIDER_URL, jndiurl);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
		if (user != null) {
			
			env.put(Context.SECURITY_CREDENTIALS   , password);
			env.put(Context.SECURITY_PRINCIPAL     , user);
		}
		setCallPrincipials();

		InitialContext ctx = new InitialContext(env);
		rmiserver = (RMIAdaptor) ctx.lookup("jmx/rmi/RMIAdaptor");

		if (rmiserver == null)
			logger.info("RMIAdaptor is null");
	}
	
	private void setCallPrincipials() {

		if (user != null) {
			// Set a security context using the SecurityAssociation
			SecurityAssociation.setPrincipal(new SimplePrincipal(user));
			// Set password
			SecurityAssociation.setCredential(password);
		} else {

		}

	}
	

	/**
	 * Get the Metadata for the MBean
	 * 
	 * @param oname
	 *            ObjectName of the MBean
	 * @return MBeanInfo about the MBean
	 */
	public MBeanInfo getMBeanInfo(ObjectName oname) throws Exception {
		MBeanInfo info = null;
		setCallPrincipials();
		info = rmiserver.getMBeanInfo(oname);

		return info;
	}

	/**
	 * Invoke an Operation on the MBean
	 * 
	 * @param oname
	 *            ObjectName of the MBean
	 * @param methodname
	 *            Name of the operation on the MBean
	 * @param pParams
	 *            Arguments to the operation
	 * @param pSignature
	 *            Signature for the operation.
	 * @return result from the MBean operation
	 * @throws Exception
	 */
	public Object invokeCommand(ObjectName oname, String methodname,
			Object[] pParams, String[] pSignature) throws Exception {
		setCallPrincipials();
		return rmiserver.invoke(oname, methodname, pParams, pSignature);
	}

	/**
	 * Invoke an Operation on the MBean
	 * 
	 * @param oname
	 *            ObjectName of the MBean
	 * @param Attribute
	 *            Name of the Attribute on the MBean
	 * 
	 * @return result from the MBean operation
	 * @throws Exception
	 */
	public String getAttribute(ObjectName oname, String AttributeName)
			throws Exception {
		setCallPrincipials();
		return rmiserver.getAttribute(oname, AttributeName).toString();
	}

	public static final String START_SLEE_OPERATION = "-startSlee";
	public static final String STOP_SLEE_OPERATION = "-stopSlee";
	public static final String GET_SLEE_STATE_OPERATION = "-getSleeState";
	public static final String INSTALL_DU_OPERATION = "-install";
	public static final String UNINSTALL_DU_OPERATION = "-uninstall";
	public static final String ACTIVATE_SERVICE_OPERATION = "-activateService";
	public static final String DEACTIVATE_SERVICE_OPERATION = "-deactivateService";
	public static final String GET_SERVICE_STATE_OPERATION = "-getServiceState";
	
	public static final String CREATE_RA_ENTITY_OPERATION = "-createRaEntity";
	public static final String ACTIVATE_RA_ENTITY_OPERATION = "-activateRaEntity";
	public static final String DEACTIVATE_RA_ENTITY_OPERATION = "-deactivateRaEntity";
	public static final String REMOVE_RA_ENTITY_OPERATION = "-removeRaEntity";
	public static final String CREATE_RA_LINK_OPERATION = "-createRaLink";
	public static final String REMOVE_RA_LINK_OPERATION = "-removeRaLink";
	
	public static final String CREATE_PROFILE_TABLE_OPERATION = "-createProfileTable";
	public static final String REMOVE_PROFILE_TABLE_OPERATION = "-removeProfileTable";
	public static final String CREATE_PROFILE_OPERATION = "-createProfile";
	public static final String REMOVE_PROFILE_OPERATION = "-removeProfile";
	
	/**
	 * Invoking operations
	 * 
	 * @param command
	 *            indicate the operation
	 * @param data#
	 *            The necessary data for the operation
	 * @throws Exception
	 */
	public Object invokeOperation(String command, String data1, String data2,
			String data3) throws Exception {
		ObjectName name = null;

		Object opArg1 = null;
		Object opArg2 = null;
		Object opArg3 = null;

		// Slee Management
		if (command.equals(START_SLEE_OPERATION)) {
			commandBean = SleeManagementMBean.OBJECT_NAME;
			commandString = "start";
		} else if (command.equals(STOP_SLEE_OPERATION)) {
			commandBean = SleeManagementMBean.OBJECT_NAME;
			commandString = "stop";
		} else if (command.equals(GET_SLEE_STATE_OPERATION)) {
			commandBean = SleeManagementMBean.OBJECT_NAME;
			commandString = "getState";
		}

		// Deployment Management
		else if (command.equals(INSTALL_DU_OPERATION)) {
			commandBean = DeploymentMBean.OBJECT_NAME;
			commandString = "install";
			opArg1 = (new URL(data1)).toString();
		} else if (command.equals(UNINSTALL_DU_OPERATION)) {
			commandBean = DeploymentMBean.OBJECT_NAME;
			commandString = "uninstall";
			opArg1 = new DeployableUnitID((new URL(data1)).toString());
		}

		// Service Management
		else if (command.equals(ACTIVATE_SERVICE_OPERATION)) {
			commandBean = ServiceManagementMBean.OBJECT_NAME;
			commandString = "activate";
			ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
			editor.setAsText(data1);
			opArg1 = editor.getValue();
		} else if (command.equals(DEACTIVATE_SERVICE_OPERATION)) {
			commandBean = ServiceManagementMBean.OBJECT_NAME;
			commandString = "deactivate";
			ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
			editor.setAsText(data1);
			opArg1 = editor.getValue();
		} else if (command.equals(GET_SERVICE_STATE_OPERATION)) {
			commandBean = ServiceManagementMBean.OBJECT_NAME;
			commandString = "getState";
			ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
			editor.setAsText(data1);
			opArg1 = editor.getValue();
		}

		// Resource Management
		else if (command.equals(CREATE_RA_ENTITY_OPERATION)) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "createResourceAdaptorEntity";
			ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
			editor.setAsText(data1);
			opArg1 = editor.getValue();
			opArg2 = data2;
			opArg3 = new ConfigProperties();
			if (data3 != null) {
				logger.warn("SLEE 1.1 config properties not supported yet");

			}
		} else if (command.equals(ACTIVATE_RA_ENTITY_OPERATION)) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "activateResourceAdaptorEntity";
			opArg1 = data1;
		} else if (command.equals(DEACTIVATE_RA_ENTITY_OPERATION)) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "deactivateResourceAdaptorEntity";
			opArg1 = data1;
		} else if (command.equals(REMOVE_RA_ENTITY_OPERATION)) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "removeResourceAdaptorEntity";
			opArg1 = data1;
		} else if (command.equals(CREATE_RA_LINK_OPERATION)) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "bindLinkName";
			opArg1 = data1;
			opArg2 = data2;
		} else if (command.equals(REMOVE_RA_LINK_OPERATION)) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "unbindLinkName";
			opArg1 = data1;
		}

		// Profile Provisioning
		else if (command.equals(CREATE_PROFILE_TABLE_OPERATION)) {
			commandBean = ProfileProvisioningMBean.OBJECT_NAME;
			commandString = "createProfileTable";
			ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
			editor.setAsText(data1);
			opArg1 = editor.getValue();
			opArg2 = data2;
		}
		// Profile Provisioning
		else if (command.equals(REMOVE_PROFILE_TABLE_OPERATION)) {
			commandBean = ProfileProvisioningMBean.OBJECT_NAME;
			commandString = "removeProfileTable";
			opArg1 = data1;
		}
		// Profile Provisioning
		else if (command.equals(CREATE_PROFILE_OPERATION)) {
			commandBean = ProfileProvisioningMBean.OBJECT_NAME;
			commandString = "createProfile";
			opArg1 = data1;
			opArg2 = data2;
		}
		// Profile Provisioning
		else if (command.equals(REMOVE_PROFILE_OPERATION)) {
			commandBean = ProfileProvisioningMBean.OBJECT_NAME;
			commandString = "removeProfile";
			opArg1 = data1;
			opArg2 = data2;
		}

		// Bad Command
		else {
			logger
					.warn("invokeOperation called with unknown command. Accepted commands are -startSlee, "
							+ "-stopSlee, -getSleeState, -install, -uninstall, "
							+ "-activateService, -deactivateService, -getServiceState, "
							+ "-createRaEntity, -activateRaEntity, -deactivateRaEntity, -removeRaEntity, -createRaLink, "
							+ "-removeRaLink, -createProfileTable, -removeProfileTable, -createProfile, -removeProfile");
			throw new Exception("invokeOperation called with unknown command.");
		}

		name = new ObjectName(commandBean);

		String[] sigs = null;
		Object[] args = null;

		// Passing in 3 args
		if (opArg3 != null) {
			sigs = new String[] { opArg1.getClass().getName(),
					opArg2.getClass().getName(), opArg3.getClass().getName() };
			args = new Object[] { opArg1, opArg2, opArg3 };

		}
		// Passing in 2 args
		else if (opArg2 != null) {
			sigs = new String[] { opArg1.getClass().getName(),
					opArg2.getClass().getName() };
			args = new Object[] { opArg1, opArg2 };
		}
		// Passing in one argument
		else if (opArg1 != null) {
			sigs = new String[] { opArg1.getClass().getName() };
			args = new Object[] { opArg1 };
		}

		return this.invokeCommand(name, commandString, args, sigs);

	}
}