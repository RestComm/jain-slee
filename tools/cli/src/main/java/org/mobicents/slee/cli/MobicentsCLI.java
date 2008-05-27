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
 * This class provides a command line interface to the JAIN
 * SLEE container.  The following commands are supported:
 *   java SleeCommandInterface -startSlee
 *   java SleeCommandInterface -stopSlee
 *   java SleeCommandInterface -getSleeState
 *   java SleeCommandInterface -deploy <url>
 *   java SleeCommandInterface -undeploy <Deployment ID>
 *   java SleeCommandInterface -getDescriptor <Deployment ID>
 *   java SleeCommandInterface -getDeploymentId <file url>
 *   java SleeCommandInterface -activateService <Service ID>
 *   java SleeCommandInterface -deactivateService <Service ID>
 *   java SleeCommandInterface -getServiceState <Service ID>
 *   java SleeCommandInterface -setTraceLevel <Service ID> <level>");
 *   java SleeCommandInterface -getTraceLevel <Service ID>");
 *   java SleeCommandInterface -createRaEntity <ResourceAdaptor ID> <entity name> <props>");
 *   java SleeCommandInterface -activateRaEntity <entity name>");
 *   java SleeCommandInterface -deactivateRaEntity <entity name>");
 *   java SleeCommandInterface -removeRaEntity <entity name>");
 *   java SleeCommandInterface -createEntityLink <link name> <entity name>");
 *   java SleeCommandInterface -removeEntityLink <link name> <entity name>");
 *
 * 
 * @author Buddy Bright
 *  
 */

package org.mobicents.slee.cli;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class MobicentsCLI {
	private static Logger logger = Logger
			.getLogger(org.mobicents.slee.cli.MobicentsCLI.class.getName());

	public static void usage() {
		System.out
				.println("Usage: java -jar mobicents-cli.jar [options] <command> <args>");
		System.out.println("options:");
		System.out.println("-user");
		System.out.println("-password");
		System.out.println("-host");
		System.out.println("-jnpPort");
		System.out.println("");
		System.out.println("Valid commands:");
		System.out.println("-startSlee");
		System.out.println("-stopSlee");
		System.out.println("-getSleeState");
		System.out.println("-install <url>");
		System.out.println("-uninstall <Deployment ID>");
		System.out.println("-uninstall <url>");
		System.out.println("-getDescriptor <Deployment ID>");
		System.out.println("-getDeploymentId <file url>");
		System.out.println("-activateService <Service ID>");
		System.out.println("-deactivateService <Service ID>");
		System.out.println("-getServiceState <Service ID>");
		System.out.println("-setTraceLevel <Component ID> <level>");
		System.out.println("-getTraceLevel <Component ID>");
		System.out
				.println("-createRaEntity <ResourceAdaptor ID> <entity name> <props>");
		System.out.println("-activateRaEntity <entity name>");
		System.out.println("-deactivateRaEntity <entity name>");
		System.out.println("-removeRaEntity <entity name>");
		System.out.println("-createEntityLink <link name> <entity name>");
		System.out.println("-removeEntityLink <link name>");
		System.out
				.println("-createProfileTable <ProfileSpecification ID> <profile table name>");
		System.out.println("-removeProfileTable <profile table name>");
		System.out
				.println("-createProfile <profile table name> <profile name>");
		System.out
				.println("-removeProfile <profile table name> <profile name>");
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		String host;
		String jnpPort;

		String user = null;
		String password = null;

		String data1 = null;
		String data2 = null;
		String data3 = null;
		String command;

		if (args.length < 1) {
			usage();
			System.exit(1);
		}

		int k = 0;

		if (args[k].equals("-user")) {
			user = args[k + 1];
			k += 2;
		}
		
		if (args[k].equals("-password")) {
			password = args[k + 1];
			k += 2;
		}		

		if (args[k].equals("-host")) {
			host = args[k + 1];
			k += 2;
		} else {
			host = "localhost";
		}

		if (args[k].equals("-jnpPort")) {
			jnpPort = args[k + 1];
			k += 2;
		} else {
			jnpPort = "1099";
		}
		command = args[k];

		if (args.length >= k + 2)
			data1 = args[k + 1];

		if (args.length >= k + 3)
			data2 = args[2 + k];

		if (args.length >= 4 + k)
			data3 = args[3 + k];

		try {
			//SleeCommandInterface sleeCommandInterface = new SleeCommandInterface(
			//		, user, password);
			SleeCommandInterface sleeCommandInterface = new SleeCommandInterface("jnp://" + host + ":" + jnpPort);
			Object result = sleeCommandInterface.invokeOperation(command,
					data1, data2, data3);

			if (result == null) {
				logger.info("No response");
			} else {
				logger.info(result.toString());
			}

		} catch (SecurityException seEx) {
			seEx.printStackTrace();
			logger.log(Level.WARNING, "Security Exception: "
					+ seEx.getMessage() + " Cause = "
					+ seEx.getCause().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			// Log the error
			logger
					.log(Level.WARNING, "Bad result: "
							+ ex.getCause().toString());

		}
	}
}