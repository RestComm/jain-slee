/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * SleeCommandInterface.java
 *
 * This new SleeCommandInterface class is prepared to do
 * management operations using the CLI (mobicents-cli.jar),
 * Ant Tasks from a .xml file or an auto-deploy script (.bsh file)
 *
 */
package org.mobicents.slee.container.management.jmx;

import java.net.URI;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.facilities.Level;
import javax.slee.management.DeployableUnitID;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.logging.Logger;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;

public class SleeCommandInterface {
	private static Logger logger = Logger
			.getLogger(org.mobicents.slee.container.management.jmx.SleeCommandInterface.class
					.getName());

	private Object result = null;

	protected RMIAdaptor rmiserver = null;

	public String commandBean = null;

	public String commandString = null;

	/**
	 * Constructor
	 */
	public SleeCommandInterface() {
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
		if (user != null) {
			// Set a security context using the SecurityAssociation
			SecurityAssociation.setPrincipal(new SimplePrincipal(user));

			// Set password
			SecurityAssociation.setCredential(password);
		}
		// Set Some JNDI Properties
		Hashtable env = new Hashtable();
		env.put(Context.PROVIDER_URL, jndiurl);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");

		InitialContext ctx = new InitialContext(env);
		rmiserver = (RMIAdaptor) ctx.lookup("jmx/rmi/RMIAdaptor");

		if (rmiserver == null)
			logger.info("RMIAdaptor is null");
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

		result = rmiserver.invoke(oname, methodname, pParams, pSignature);

		return result;
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
		Object result = null;

		result = rmiserver.getAttribute(oname, AttributeName);
		return result.toString();

	}

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

		String sig1 = null;
		String sig2 = null;
		String sig3 = null;

		Object opArg1 = null;
		Object opArg2 = null;
		Object opArg3 = null;

		// Start SLEE
		if (command.equals("-startSlee")) {
			commandBean = "slee:service=SleeManagement";
			commandString = "start";
		}
		// Stop SLEE
		else if (command.equals("-stopSlee")) {
			commandBean = "slee:service=SleeManagement";
			commandString = "stop";
		}
		// Get SLEE State
		else if (command.equals("-getSleeState")) {
			commandBean = "slee:service=SleeManagement";

			name = new ObjectName(commandBean);

			logger.info("SLEE State = " + this.getAttribute(name, "State"));
			// System.exit(0); // Operation only called by the CLI
		}
		// Deployment Management
		else if (command.equals("-install")) {
			commandBean = "slee:name=DeploymentMBean";
			commandString = "install";
			sig1 = "java.lang.String";
			opArg1 = (new URL(data1)).toString();
		}
		// Deployment Management
		else if (command.equals("-uninstall")) {
			data1 = (new URL(data1)).toString();
			DeployableUnitID deployableUnitID = new DeployableUnitID(data1);
			opArg1 = deployableUnitID;			
			commandBean = "slee:name=DeploymentMBean";
			commandString = "uninstall";
			sig1 = "javax.slee.management.DeployableUnitID";
		}
		// Deployment Management
		else if (command.equals("-getDeploymentId")) {
			commandBean = "slee:name=DeploymentMBean";
			commandString = "getDeployableUnit";
			sig1 = "java.lang.String";
			opArg1 = data1;
		}
		// Deployment Management
		else if (command.equals("-getDescriptor")) {
			commandBean = "slee:name=DeploymentMBean";
			commandString = "getDescriptor";
			sig1 = "javax.slee.management.DeployableUnitID";
			data1 = (new URL(data1)).toString();
			DeployableUnitID deployableUnitID = new DeployableUnitID(data1);
			opArg1 = deployableUnitID;			
		}
		// Service Management
		else if (command.equals("-activateService")) {
			commandBean = "slee:name=ServiceManagementMBean";
			commandString = "activate";
			sig1 = "javax.slee.ServiceID";

			StringTokenizer stringTokenizer = new StringTokenizer(data1, "[",
					true);
			String componentType = stringTokenizer.nextToken();
			stringTokenizer.nextToken();
			String ckeyStr = stringTokenizer.nextToken("]");

			if (componentType.equalsIgnoreCase(ComponentIDImpl.SERVICE_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ServiceIDImpl(componentKey);
			} else {
				logger
						.warn("-activateService. Bad Result: ServiceID[service]\n");
				throw new Exception(
						"-activateService. Bad Result: ServiceID[service]\n");
			}
		}
		// Service Management
		else if (command.equals("-deactivateService")) {
			commandBean = "slee:name=ServiceManagementMBean";
			commandString = "deactivate";
			sig1 = "javax.slee.ServiceID";

			StringTokenizer stringTokenizer = new StringTokenizer(data1, "[",
					true);
			String componentType = stringTokenizer.nextToken();
			stringTokenizer.nextToken();
			String ckeyStr = stringTokenizer.nextToken("]");

			if (componentType.equalsIgnoreCase(ComponentIDImpl.SERVICE_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ServiceIDImpl(componentKey);
			} else {
				logger
						.warn("-deactivateService. Bad Result: ServiceID[service]\n");
				throw new Exception(
						"-deactivateService. Bad Result: ServiceID[service]\n");
			}

		}
		// Service Management
		else if (command.equals("-getServiceState")) {
			commandBean = "slee:name=ServiceManagementMBean";
			commandString = "getState";
			sig1 = "javax.slee.ServiceID";

			StringTokenizer stringTokenizer = new StringTokenizer(data1, "[",
					true);
			String componentType = stringTokenizer.nextToken();
			stringTokenizer.nextToken();
			String ckeyStr = stringTokenizer.nextToken("]");

			if (componentType.equalsIgnoreCase(ComponentIDImpl.SERVICE_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ServiceIDImpl(componentKey);
			} else {
				logger
						.warn("-getServiceState. Bad Result: ServiceID[service]\n");
				throw new Exception(
						"-getServiceState. Bad Result: ServiceID[service]\n");
			}

		}
		// Trace management
		else if (command.equals("-setTraceLevel")) {
			String level = data2;

			commandBean = "slee:name=TraceMBean";
			commandString = "setTraceLevel";
			sig1 = "javax.slee.ComponentID";
			sig2 = "javax.slee.facilities.Level";

			StringTokenizer stringTokenizer = new StringTokenizer(data1, "[",
					true);
			String componentType = stringTokenizer.nextToken();
			stringTokenizer.nextToken();
			String ckeyStr = stringTokenizer.nextToken("]");

			if (componentType.equalsIgnoreCase(ComponentIDImpl.SERVICE_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ServiceIDImpl(componentKey);

			} else if (componentType.equalsIgnoreCase(ComponentIDImpl.SBB_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new SbbIDImpl(componentKey);

			} else if (componentType
					.equalsIgnoreCase(ComponentIDImpl.PROFILE_SPECIFICATION_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ProfileSpecificationIDImpl(componentKey);

			} else if (componentType
					.equalsIgnoreCase(ComponentIDImpl.RESOURCE_ADAPTOR_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ResourceAdaptorIDImpl(componentKey);

			} else if (componentType
					.equalsIgnoreCase(ComponentIDImpl.RESOURCE_ADAPTOR_TYPE_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ResourceAdaptorTypeIDImpl(componentKey);

			} else if (componentType
					.equalsIgnoreCase((ComponentIDImpl.EVENT_TYPE_ID))) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new EventTypeIDImpl(componentKey);

			} else {
				logger
						.warn("-setTraceLevel. Bad Result: Unknown Component ID Type\n");
				logger
						.warn("-setTraceLevel. It has to be: ServiceID, SbbID, ProfileSpecificationID, "
								+ "ResourceAdaptorID, ResourceAdaptorTypeID or EventTypeID\n");

				throw new Exception(
						"-setTraceLevel. Bad Result: Unknown Component ID Type\n");

			}

			if (level.equalsIgnoreCase(Level.SEVERE.toString()))
				opArg2 = Level.SEVERE;
			else if (level.equalsIgnoreCase(Level.WARNING.toString()))
				opArg2 = Level.WARNING;
			else if (level.equalsIgnoreCase(Level.INFO.toString()))
				opArg2 = Level.INFO;
			else if (level.equalsIgnoreCase(Level.CONFIG.toString()))
				opArg2 = Level.CONFIG;
			else if (level.equalsIgnoreCase(Level.FINE.toString()))
				opArg2 = Level.FINE;
			else if (level.equalsIgnoreCase(Level.FINER.toString()))
				opArg2 = Level.FINER;
			else if (level.equalsIgnoreCase(Level.FINEST.toString()))
				opArg2 = Level.FINEST;
			else if (level.equalsIgnoreCase(Level.OFF.toString()))
				opArg2 = Level.OFF;
			else
				throw new IllegalArgumentException("-setTraceLevel. Bad level "
						+ level);

		}
		// Trace management
		else if (command.equals("-getTraceLevel")) {
			commandBean = "slee:name=TraceMBean";
			commandString = "getTraceLevel";
			sig1 = "javax.slee.ComponentID";

			StringTokenizer stringTokenizer = new StringTokenizer(data1, "[",
					true);
			String componentType = stringTokenizer.nextToken();
			stringTokenizer.nextToken();
			String ckeyStr = stringTokenizer.nextToken("]");

			if (componentType.equalsIgnoreCase(ComponentIDImpl.SERVICE_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ServiceIDImpl(componentKey);

			} else if (componentType.equalsIgnoreCase(ComponentIDImpl.SBB_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new SbbIDImpl(componentKey);

			} else if (componentType
					.equalsIgnoreCase(ComponentIDImpl.PROFILE_SPECIFICATION_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ProfileSpecificationIDImpl(componentKey);

			} else if (componentType
					.equalsIgnoreCase(ComponentIDImpl.RESOURCE_ADAPTOR_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ResourceAdaptorIDImpl(componentKey);

			} else if (componentType
					.equalsIgnoreCase(ComponentIDImpl.RESOURCE_ADAPTOR_TYPE_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ResourceAdaptorTypeIDImpl(componentKey);

			} else {
				logger
						.warn("-getTraceLevel. Bad Result: Unknown Component ID Type\n");
				logger
						.warn("-getTraceLevel. It has to be: ServiceID, SbbID, ProfileSpecificationID, "
								+ "ResourceAdaptorID or ResourceAdaptorTypeID\n");
				throw new Exception(
						"-getTraceLevel. Bad Result: Unknown Component ID Type\n");
			}

		}
		// Resource Management
		else if (command.equals("-createRaEntity")) {
			// FIXME Use ResourceManagementMBean
			commandBean = "slee:name=ResourceManagementMBean";
			commandString = "createResourceAdaptorEntity";
			sig1 = "javax.slee.resource.ResourceAdaptorID";
			sig2 = "java.lang.String"; // entity name
			opArg2 = data2;
			sig3 = "java.util.Properties"; // RA properties

			logger.info("Loading properties: " + data3);

			if (data3 != null) {
				Properties props = new Properties();
				URL url = new URI(data3).toURL();
				props.load(url.openStream());
				opArg3 = props;
			} else {
				opArg3 = null;
			}

			StringTokenizer stringTokenizer = new StringTokenizer(data1, "[",
					true);
			String componentType = stringTokenizer.nextToken();
			stringTokenizer.nextToken();
			String ckeyStr = stringTokenizer.nextToken("]");

			if (componentType
					.equalsIgnoreCase(ComponentIDImpl.RESOURCE_ADAPTOR_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ResourceAdaptorIDImpl(componentKey);
			} else {
				logger
						.warn("-createRaEntity. Bad Result: ResourceAdaptor[resourceID]\n");
				throw new Exception(
						"-createRaEntity. Bad Result: ResourceAdaptor[resourceID]\n");
			}

		}
		// Resource Management
		else if (command.equals("-activateRaEntity")) {
			// FIXME Use ResourceManagementMBean
			commandBean = "slee:name=ResourceManagementMBean";
			commandString = "activateResourceAdaptorEntity";
			sig1 = "java.lang.String"; // entity name
			opArg1 = data1;
		}
		// Resource Management
		else if (command.equals("-deactivateRaEntity")) {
			// FIXME Use ResourceManagementMBean
			commandBean = "slee:name=ResourceManagementMBean";
			commandString = "deactivateResourceAdaptorEntity";
			sig1 = "java.lang.String"; // entity name
			opArg1 = data1;
		}
		// Resource Management
		else if (command.equals("-removeRaEntity")) {
			// FIXME Use ResourceManagementMBean
			commandBean = "slee:name=ResourceManagementMBean";
			commandString = "removeResourceAdaptorEntity";
			sig1 = "java.lang.String"; // entity name
			opArg1 = data1;
		}
		// Resource Management
		else if (command.equals("-createRaLink")) {
			// FIXME Use ResourceManagementMBean
			commandBean = "slee:name=ResourceManagementMBean";
			commandString = "bindLinkName";
			sig1 = "java.lang.String"; // link name
			sig2 = "java.lang.String"; // entity name
			opArg1 = data1;
			opArg2 = data2;
		}
		// Resource Management
		else if (command.equals("-removeRaLink")) {
			// FIXME Use ResourceManagementMBean
			commandBean = "slee:name=ResourceManagementMBean";
			commandString = "unbindLinkName";
			sig1 = "java.lang.String"; // link name
			opArg1 = data1;
		}
		// Profile Provisioning
		else if (command.equals("-createProfileTable")) {
			commandBean = "slee:name=ProfileProvisoningMBean";
			commandString = "createProfileTable";
			sig1 = "javax.slee.profile.ProfileSpecificationID";
			sig2 = "java.lang.String"; // profileTableName

			opArg2 = data2;

			StringTokenizer stringTokenizer = new StringTokenizer(data1, "[",
					true);
			String componentType = stringTokenizer.nextToken();
			stringTokenizer.nextToken();
			String ckeyStr = stringTokenizer.nextToken("]");
			if (componentType
					.equalsIgnoreCase(ComponentIDImpl.PROFILE_SPECIFICATION_ID)) {
				ComponentKey componentKey = new ComponentKey(ckeyStr);
				opArg1 = new ProfileSpecificationIDImpl(componentKey);
			} else {
				logger
						.warn("-createProfileTable Bad Result: ProfileSpecificationID[profile_specification]\n");
				throw new Exception(
						"-createProfileTable Bad Result: ProfileSpecificationID[profile_specification]\n");
			}

		}
		// Profile Provisioning
		else if (command.equals("-removeProfileTable")) {
			commandBean = "slee:name=ProfileProvisoningMBean";
			commandString = "removeProfileTable";
			sig1 = "java.lang.String"; // profileTableName
			opArg1 = data1;
		}
		// Profile Provisioning
		else if (command.equals("-createProfile")) {
			commandBean = "slee:name=ProfileProvisoningMBean";
			commandString = "createProfile";
			sig1 = "java.lang.String"; // profileTableName
			sig2 = "java.lang.String"; // newProfileName
			opArg1 = data1;
			opArg2 = data2;
		}
		// Profile Provisioning
		else if (command.equals("-removeProfile")) {
			commandBean = "slee:name=ProfileProvisoningMBean";
			commandString = "removeProfile";
			sig1 = "java.lang.String"; // profileTableName
			sig2 = "java.lang.String"; // newProfileName
			opArg1 = data1;
			opArg2 = data2;
		}

		// Bad Command
		else {
			logger
					.warn("invokeOperation called with unknown command. Accepted commands are -startSlee, "
							+ "-stopSlee, -getSleeState, -install, -uninstall, -getDeploymentId, -getDescriptor, "
							+ "-activateService, -deactivateService, -getServiceState, -setTraceLevel, -getTraceLevel, "
							+ "-createRaEntity, -activateRaEntity, -deactivateRaEntity, -removeRaEntity, -createRaLink, "
							+ "-removeRaLink, -createProfileTable, -removeProfileTable, -createProfile, -removeProfile");
			throw new Exception("invokeOperation called with unknown command.");
		}

		name = new ObjectName(commandBean);

		// Passing in 3 args
		if (sig3 != null) {
			String sigs[] = { sig1, sig2, sig3 };
			Object[] opArgs = { opArg1, opArg2, opArg3 };
			this.invokeCommand(name, commandString, opArgs, sigs);
		}
		// Passing in 2 args
		else if (sig2 != null) {
			String sigs[] = { sig1, sig2 };
			Object[] opArgs = { opArg1, opArg2 };
			this.invokeCommand(name, commandString, opArgs, sigs);
		}
		// Passing in one argument
		else if (sig1 != null) {
			String sigs[] = { sig1 };
			Object[] opArgs = { opArg1 };
			this.invokeCommand(name, commandString, opArgs, sigs);
		}
		// No args
		else {
			this.invokeCommand(name, commandString, null, null);
		}

		return result;
	}
}