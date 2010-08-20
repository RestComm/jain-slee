package org.mobicents.slee.training.example9.profile;

import java.util.Hashtable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

/**
 * Profile Creator would be used to create profiles
 * 
 * @author MoBitE info@mobite.co.in
 * 
 */

public class ProfileCreator {

	private static boolean createdProfiles = false;

	private static Logger log = Logger.getLogger(ProfileCreator.class);

	private static ExecutorService executor = Executors
			.newSingleThreadExecutor();

	public static void createProfiles() {

		if (!createdProfiles) {
			log.info("Creating profiles ------------------------------------");

			try {
				executor.submit(new Callable<Object>() {

					public Object call() throws Exception {
						_createProfiles();
						return null;
					}
				}).get();
			} catch (InterruptedException e) {
				log.error("InterruptedException ", e);
			} catch (ExecutionException e) {
				log.error("ExecutionException ", e);
			}
		}
	}

	private static void _createProfiles() {
		// FIXME: this actually does not work, needs to be fixed or reworked,
		// statics in lib are not visible ;[
		if (!createdProfiles) {
			// TODO change this code to be an exmaple of how to create Slee
			// Profiles from an external java app
			log.info("_createProfiles");
			try {

				String jbossBindAddress = System
						.getProperty("jboss.bind.address");
				Hashtable env = new Hashtable();
				env.put("java.naming.factory.initial",
						"org.jnp.interfaces.NamingContextFactory");
				env
						.put("java.naming.provider.url", "jnp://"
								+ jbossBindAddress);
				env.put("java.naming.factory.url.pkgs",
						"org.jboss.naming:org.jnp.interfaces");
				RMIAdaptor adaptor = (RMIAdaptor) new InitialContext(env)
						.lookup("jmx/rmi/RMIAdaptor");
				SleeCommandInterface sci = new SleeCommandInterface("jnp://"
						+ System.getProperty("jboss.bind.address") + ":1099",
						null, null);

				String controllerProfileSpecID = "ProfileSpecificationID[name=EventControlProfileCMP,vendor=org.mobicents,version=2.0]";
				String profileTableName = "EventControl";

				try {
					sci.invokeOperation("-removeProfileTable",
							controllerProfileSpecID, profileTableName, null);
				} catch (Exception e) {
					log.error("Error Removing Profile Table ", e);
				}
				// create profile table
				sci.invokeOperation("-createProfileTable",
						controllerProfileSpecID, profileTableName, null);
				log.info("*** AddressProfileTable " + profileTableName
						+ " created.");

				newProfile(adaptor, sci, profileTableName, "100", "100", true,
						true);
				log.info("********** CREATED PROFILE: 100 **********");

				newProfile(adaptor, sci, profileTableName, "101", "101", false,
						true);
				log.info("********** CREATED PROFILE: 101 **********");

				newProfile(adaptor, sci, profileTableName, "102", "102", true,
						false);
				log.info("********** CREATED PROFILE: 102 **********");

				newProfile(adaptor, sci, profileTableName, "103", "103", true,
						false);
				log.info("********** CREATED PROFILE: 103 **********");

				newProfile(adaptor, sci, profileTableName, "104", "104", false,
						true);
				log.info("********** CREATED PROFILE: 104 **********");

				newProfile(adaptor, sci, profileTableName, "105", "105", true,
						true);
				log.info("********** CREATED PROFILE: 105 **********");

				log.info("Finished creation of Example9's Events Profiles!");

			} catch (Exception e) {
				log.error("Failed to create example9 Profiles!", e);
			}
			createdProfiles = true;
		}
	}

	private static void newProfile(RMIAdaptor adaptor,
			SleeCommandInterface sci, String profileTableName,
			String profileName, String activityId1, boolean init1, boolean any1)
			throws Exception {

		ObjectName profileObjectName = (ObjectName) sci.invokeOperation(
				"-createProfile", profileTableName, profileName, null);
		log.info("*** AddressProfile " + profileName + " created: "
				+ profileObjectName);
		if (!(Boolean) adaptor.getAttribute(profileObjectName,
				"ProfileWriteable")) {
			Object[] o = new Object[] {};
			adaptor
					.invoke(profileObjectName, "editProfile", o,
							new String[] {});
			log.info("*** Setting profile editable.");
		} else {
			log.info("********* Profile is editable.");
		}
		// Setting and Committing
		Attribute action = new Attribute("ActivityId", activityId1);
		Attribute init = new Attribute("Init", init1);
		Attribute any = new Attribute("Any", any1);
		adaptor.setAttribute(profileObjectName, action);
		adaptor.setAttribute(profileObjectName, init);
		adaptor.setAttribute(profileObjectName, any);
		log.info("*** Profile modifications are not committed yet.");
		adaptor.invoke(profileObjectName, "commitProfile", new Object[] {},
				new String[] {});
		log.info("*** Profile modifications are committed.");
	}

}
