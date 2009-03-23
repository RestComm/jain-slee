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
		return rmiserver.getAttribute(oname, AttributeName).toString();
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

		Object opArg1 = null;
		Object opArg2 = null;
		Object opArg3 = null;

		// Slee Management
		if (command.equals("-startSlee")) {
			commandBean = SleeManagementMBean.OBJECT_NAME;
			commandString = "start";
		} else if (command.equals("-stopSlee")) {
			commandBean = SleeManagementMBean.OBJECT_NAME;
			commandString = "stop";
		} else if (command.equals("-getSleeState")) {
			commandBean = SleeManagementMBean.OBJECT_NAME;
			commandString = "getState";
		}

		// Deployment Management
		else if (command.equals("-install")) {
			commandBean = DeploymentMBean.OBJECT_NAME;
			commandString = "install";
			opArg1 = (new URL(data1)).toString();
		} else if (command.equals("-uninstall")) {
			commandBean = DeploymentMBean.OBJECT_NAME;
			commandString = "uninstall";
			opArg1 = new DeployableUnitID(data1);
		}

		// Service Management
		else if (command.equals("-activateService")) {
			commandBean = ServiceManagementMBeanImpl.OBJECT_NAME;
			commandString = "activate";
			ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
			editor.setAsText(data1);
			opArg1 = editor.getValue();
		} else if (command.equals("-deactivateService")) {
			commandBean = ServiceManagementMBeanImpl.OBJECT_NAME;
			commandString = "deactivate";
			ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
			editor.setAsText(data1);
			opArg1 = editor.getValue();
		} else if (command.equals("-getServiceState")) {
			commandBean = ServiceManagementMBeanImpl.OBJECT_NAME;
			commandString = "getState";
			ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
			editor.setAsText(data1);
			opArg1 = editor.getValue();
		}

		// Resource Management
		else if (command.equals("-createRaEntity")) {
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
		} else if (command.equals("-activateRaEntity")) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "activateResourceAdaptorEntity";
			opArg1 = data1;
		} else if (command.equals("-deactivateRaEntity")) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "deactivateResourceAdaptorEntity";
			opArg1 = data1;
		} else if (command.equals("-removeRaEntity")) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "removeResourceAdaptorEntity";
			opArg1 = data1;
		} else if (command.equals("-createRaLink")) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "bindLinkName";
			opArg1 = data1;
			opArg2 = data2;
		} else if (command.equals("-removeRaLink")) {
			commandBean = ResourceManagementMBean.OBJECT_NAME;
			commandString = "unbindLinkName";
			opArg1 = data1;
		}

		// Profile Provisioning
		else if (command.equals("-createProfileTable")) {
			commandBean = ProfileProvisioningMBean.OBJECT_NAME;
			commandString = "createProfileTable";
			ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
			editor.setAsText(data1);
			opArg1 = editor.getValue();
			opArg2 = data2;
		}
		// Profile Provisioning
		else if (command.equals("-removeProfileTable")) {
			commandBean = ProfileProvisioningMBean.OBJECT_NAME;
			commandString = "removeProfileTable";
			opArg1 = data1;
		}
		// Profile Provisioning
		else if (command.equals("-createProfile")) {
			commandBean = ProfileProvisioningMBean.OBJECT_NAME;
			commandString = "createProfile";
			opArg1 = data1;
			opArg2 = data2;
		}
		// Profile Provisioning
		else if (command.equals("-removeProfile")) {
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