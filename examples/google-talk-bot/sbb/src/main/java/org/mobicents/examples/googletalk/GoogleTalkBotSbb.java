package org.mobicents.examples.googletalk;

import java.util.Arrays;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.mobicents.slee.resource.xmpp.XmppResourceAdaptorSbbInterface;

/**
 * @author Neutel
 * @author Ivelin Ivanov
 * @author Eduardo Martins
 * @version 2.0
 * 
 * A prototype SBB for the XMPP Resource Adaptor in client mode. It demonstrates
 * connection with the GoogleTalk service. Simulates a regular GoogleTalk user.
 * 
 * The bot simply receives IM chat messages from Google Talk users and echoes
 * them back. It also responds to a 'time' message with the current time at the
 * server where the SBB is hosted.
 * 
 */

public abstract class GoogleTalkBotSbb implements javax.slee.Sbb {

	private Class<?>[] packetsToListen = { Message.class, Presence.class };

	private String connectionID = "org.mobicents.examples.googletalk.GoogleTalkBotSbb";

	private String username = "CHANGE_UID";

	private String password = "CHANGE_PASSWD";

	private String resource = "MobicentsGoogleTalkBot";

	private String serviceHost = "talk.google.com";

	private int servicePort = 5222;

	private String serviceName = "gmail.com";

	private XmppResourceAdaptorSbbInterface xmppSbbInterface;

	private SbbContext sbbContext; // This SBB's SbbContext

	static private Tracer logger;

	/*
	 * Init the xmpp connection to GOOGLE TALK when the service is activated by
	 * SLEE
	 */
	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		try {
			// connect to google talk xmpp server
			xmppSbbInterface.connectClient(connectionID, serviceHost,
					servicePort, serviceName, username, password, resource,
					Arrays.asList(packetsToListen));
		} catch (XMPPException e) {
			e.printStackTrace();
			logger.info("Connection to server failed! Error:" + e.getMessage());
		}
	}

	/**
	 * Here we receive the Presence messages.
	 */
	public void onPresence(org.jivesoftware.smack.packet.Presence packet,
			ActivityContextInterface aci) {
		// check if this event is "to" me
		if (StringUtils.parseBareAddress(packet.getTo()).equals(
				username + "@" + serviceName)) {
			logger.info("XMPP Presence event type! Status: '" + packet.getType() + "'. "
					+ "Sent by '" + packet.getFrom() + "'.");
			// reply hello msg if receives notification of available presence state
			if (packet.getType() == Presence.Type.AVAILABLE) {
				Message msg = new Message(packet.getFrom(), Message.Type.CHAT);
				msg.setBody("Hi. I'am online too.");
				xmppSbbInterface.sendPacket(connectionID, msg);
			}
		}
	}

	/**
	 * This is the point where we already have a chat session with the user, so,
	 * when they send us messages, we count the chars and reply or tell time :)
	 */
	public void onMessage(org.jivesoftware.smack.packet.Message message,
			ActivityContextInterface aci) {
		// check if this event is "to" me and it's not an error
		if (StringUtils.parseBareAddress(message.getTo()).equals(
				username + "@" + serviceName)
				&& !message.getType().equals(Message.Type.ERROR)) {
		  
			logger.info("XMPP Message event type! Message Body: '" + message.getBody() + "'. "
			    + "Sent by '" + message.getFrom() + "'.");
			
			String body = null;
			if (message.getBody() != null) {
				if (message.getBody().toLowerCase().startsWith("time")) {
					Date d = new Date();
					body = "My system time is " + d.toString();
				} else {
					body = message.getBody().length() + " chars in message <"
							+ message.getBody() + ">.";
				}
			}
      // Filter messages from other running bots! 
      if(message.getFrom().startsWith( "mobicents.org@gmail.com" )) {
        logger.info( "XMPP Message is from Bot '" + message.getFrom() + "'. Not replying!" );
        return;
      }
			
			Message msg = new Message(message.getFrom(), message.getType());
			msg.setBody(body);
			xmppSbbInterface.sendPacket(connectionID, msg);
		}
	}

	/*
	 * Handler to disconnect from Google when the service is being deactivated
	 */

	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {
		if (aci.getActivity() instanceof ServiceActivity) {
			try {
				xmppSbbInterface.disconnect(connectionID);
			} catch (Exception e) {
				logger.severe(e.getMessage(),e);
			}
		}
	}

	/**
	 * Initialize SBB component.
	 * 
	 * Gets a reference to the XMPP RA so that messages can be sent out.
	 * 
	 */
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		if (logger == null) {
			logger = sbbContext.getTracer(getClass().getSimpleName());
		}
		try {
			logger.info("Called setSbbContext!");
			
			Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
			
			xmppSbbInterface = (XmppResourceAdaptorSbbInterface) myEnv
					.lookup("slee/resources/xmpp/2.0/xmppinterface");

			// env-entries
			username = (String) myEnv.lookup("username");
			password = (String) myEnv.lookup("password");

			logger.info("setSbbContext() Retrieved uid[" + username + "]," +
					" passwd[" + password + "]");
		} catch (NamingException ne) {
			logger.severe("Could not set SBB context:" + ne.getMessage(),ne);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	// TODO: Implement the lifecycle methods if required
	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	/**
	 * Convenience method to retrieve the SbbContext object stored in
	 * setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove
	 * this method, the sbbContext variable and the variable assignment in
	 * setSbbContext().
	 * 
	 * @return this SBB's SbbContext object
	 */

	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	protected String getTraceMessageType() {
		return "GoogleTalkBotService";
	}

}
