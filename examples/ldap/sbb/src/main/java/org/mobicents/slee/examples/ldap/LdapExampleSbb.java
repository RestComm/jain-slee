package org.mobicents.slee.examples.ldap;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;

import org.apache.log4j.Logger;

public abstract class LdapExampleSbb implements javax.slee.Sbb {

	private SbbContext sbbContext = null; // This SBB's context			

	private Context myEnv = null; // This SBB's environment

	// connection properties
	private String PROVIDER_URL = null;

	private String SECURITY_PRINCIPAL = null;

	private String SECURITY_CREDENTIALS = null;

	private String SECURITY_AUTHENTICATION = null;

	/**
	 * Called when an sbb object is instantied and enters the pooled state.
	 */
	public void setSbbContext(SbbContext context) {
		log4j.info("setSbbContext(context=" + context.toString() + ")");
		this.sbbContext = context;
		try {
			myEnv = (Context) new InitialContext().lookup("java:comp/env");
			PROVIDER_URL = (String) myEnv.lookup("PROVIDER_URL");
			SECURITY_PRINCIPAL = (String) myEnv.lookup("SECURITY_PRINCIPAL");
			SECURITY_CREDENTIALS = (String) myEnv
					.lookup("SECURITY_CREDENTIALS");
			SECURITY_AUTHENTICATION = (String) myEnv
					.lookup("SECURITY_AUTHENTICATION");

			if (log4j.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder(
						"Connection properties: { PROVIDER_URL=").append(
						PROVIDER_URL).append(", SECURITY_PRINCIPAL=").append(
						SECURITY_PRINCIPAL).append(", SECURITY_CREDENTIALS=")
						.append(SECURITY_CREDENTIALS).append(
								", SECURITY_AUTHENTICATION=").append(
								SECURITY_AUTHENTICATION).append(" }");
				log4j.info(sb.toString());
			}
		} catch (NamingException e) {
			log4j.error("Can't set sbb context.", e);
		}
	}

	public void unsetSbbContext() {
		log4j.info("unsetSbbContext()");
		this.sbbContext = null;
	}

	public void sbbCreate() throws javax.slee.CreateException {
		log4j.info("sbbCreate()");
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
		log4j.info("sbbPostCreate()");
	}

	public void sbbActivate() {
		log4j.info("sbbActivate()");
	}

	public void sbbPassivate() {
		log4j.info("sbbPassivate()");
	}

	public void sbbRemove() {
		log4j.info("sbbRemove()");
	}

	public void sbbLoad() {
		log4j.info("sbbLoad()");
	}

	public void sbbStore() {
		log4j.info("sbbStore()");
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
		log4j.info("sbbExceptionThrown(exception=" + exception.toString()
				+ ",event=" + event.toString() + ",activity="
				+ activity.toString() + ")");
	}

	public void sbbRolledBack(RolledBackContext sbbRolledBack) {
		log4j.info("sbbRolledBack(sbbRolledBack=" + sbbRolledBack.toString()
				+ ")");
	}

	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	/*
	 * Init the connection and retreive data when the service is activated by SLEE
	 */
	public void onServiceStartedEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		log4j.info("onServiceStartedEvent(event=" + event.toString() + ",aci="
				+ aci.toString() + ")");
		try {
			//check if it's my service that is starting
			ServiceActivity sa = ((ServiceActivityFactory) myEnv
					.lookup("slee/serviceactivity/factory")).getActivity();
			if (sa.equals(aci.getActivity())) {
				log4j.info("Service activated...");
				log4j.info("Opening connection to server...");
				InitialDirContext ctx = null;
				NamingEnumeration enm = null;
				try {
					// setup connection environment
					Hashtable<String, String> env = new Hashtable<String, String>();
					env.put(Context.INITIAL_CONTEXT_FACTORY,
							"com.sun.jndi.ldap.LdapCtxFactory");
					env.put(Context.PROVIDER_URL, PROVIDER_URL);
					env.put(Context.SECURITY_PRINCIPAL, SECURITY_PRINCIPAL);
					env.put(Context.SECURITY_CREDENTIALS, SECURITY_CREDENTIALS);
					env.put(Context.SECURITY_AUTHENTICATION,
							SECURITY_AUTHENTICATION);
					// create directory context
					ctx = new InitialDirContext(env);
					// get and log all top attributes
					Attributes attrs = ctx.getAttributes("");
					enm = attrs.getAll();
					ArrayList<Attribute> list = new ArrayList<Attribute>();
					while (enm.hasMore()) {
						list.add((Attribute) enm.next());
					}
					log4j.info("Attributes retreived:\n " + list);
				} catch (NamingException e) {
					log4j
							.error(
									"Unable to connect to server and retreive top attributes.",
									e);
				} finally {
					// close resources
					if (enm != null) {
						try {
							enm.close();
						} catch (NamingException e) {
							log4j.error("Unable to close naming enumeration.",
									e);
						}
					}
					if (ctx != null) {
						try {
							ctx.close();
						} catch (NamingException e) {
							log4j.error("Unable to close dir context.", e);
						}
					}
				}
			} else {
				log4j.info("Another service activated...");
				// we don't want to receive further events on this activity
				aci.detach(getSbbContext().getSbbLocalObject());
			}
		} catch (NamingException e) {
			log4j.error("Can't handle service started event.", e);
		}
	}

	private static Logger log4j = Logger.getLogger(LdapExampleSbb.class);

}