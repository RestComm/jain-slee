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
import org.mobicents.slee.resource.xmpp.XmppActivityContextInterfaceFactory;
import org.mobicents.slee.resource.xmpp.XmppConnection;
import org.mobicents.slee.resource.xmpp.XmppResourceAdaptorSbbInterface;

/**
 * @author Neutel
 * @author Ivelin Ivanov
 * @author Eduardo Martins
 * @version 2.0
 * 
 *          A prototype SBB for the XMPP Resource Adaptor in client mode. It
 *          demonstrates connection with the GoogleTalk service. Simulates a
 *          regular GoogleTalk user.
 * 
 *          The bot simply receives IM chat messages from Google Talk users and
 *          echoes them back. It also responds to a 'time' message with the
 *          current time at the server where the SBB is hosted.
 * 
 */

public abstract class GoogleTalkBotSbb implements javax.slee.Sbb {

	private final static Class<?>[] packetsToListen = { Message.class,
			Presence.class };

	private final static String connectionID = "org.mobicents.examples.googletalk.GoogleTalkBotSbb";

	private static final String resource = "MobicentsGoogleTalkBot";

	private static final String serviceHost = "talk.google.com";

	private static final int servicePort = 5222;

	private static final String serviceName = "gmail.com";

	private XmppResourceAdaptorSbbInterface xmppSbbInterface;
	private XmppActivityContextInterfaceFactory xmppActivityContextInterfaceFactory;

	private SbbContext sbbContext;
	private Tracer tracer;

	private String username;
	private String password;

	/**
	 * Init the xmpp connection to GOOGLE TALK when the service is activated by
	 * SLEE
	 * 
	 * @param event
	 * @param aci
	 */
	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		try {
			// connect to google talk xmpp server
			XmppConnection connection = xmppSbbInterface.connectClient(
					connectionID, serviceHost, servicePort, serviceName,
					username, password, resource, Arrays
							.asList(packetsToListen));
			xmppActivityContextInterfaceFactory.getActivityContextInterface(
					connection).attach(sbbContext.getSbbLocalObject());
		} catch (XMPPException e) {
			tracer.severe("Connection to server failed!",e);
		}
	}

	/**
	 * Here we handle the Presence messages.
	 * 
	 * @param packet
	 * @param aci
	 */
	public void onPresence(org.jivesoftware.smack.packet.Presence packet,
			ActivityContextInterface aci) {
		tracer.info("XMPP Presence event type! Status: '" + packet.getType()
				+ "'. " + "Sent by '" + packet.getFrom() + "'.");
		// reply hello msg if receives notification of available presence state
		if (packet.getType() == Presence.Type.AVAILABLE) {
			Message msg = new Message(packet.getFrom(), Message.Type.CHAT);
			msg.setBody("Hi. I'am online too.");
			xmppSbbInterface.sendPacket(connectionID, msg);
		}
	}

	/**
	 * This is the point where we already have a chat session with the user, so,
	 * when they send us messages, we count the chars and reply or tell time :)
	 * 
	 * @param message
	 * @param aci
	 */
	public void onMessage(org.jivesoftware.smack.packet.Message message,
			ActivityContextInterface aci) {
		// only process messages which are not an error and not sent by another
		// bot instance
		if (!message.getType().equals(Message.Type.ERROR)
				&& !StringUtils.parseBareAddress(message.getFrom()).equals(
						username + "@" + serviceName)) {
			tracer.info("XMPP Message event type! Message Body: '"
					+ message.getBody() + "'. " + "Sent by '"
					+ message.getFrom() + "'.");
			String body = null;
			if (message.getBody() != null) {
				if (message.getBody().equalsIgnoreCase("time")) {
					body = "My system time is " + new Date().toString();
				} else {
					body = message.getBody().length() + " chars in message <"
							+ message.getBody() + ">.";
				}
			}
			Message msg = new Message(message.getFrom(), message.getType());
			msg.setBody(body);
			xmppSbbInterface.sendPacket(connectionID, msg);
		}
	}

	/**
	 * Handler to disconnect from Google when the service is being deactivated.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {
		if (aci.getActivity() instanceof ServiceActivity) {
			try {
				xmppSbbInterface.disconnect(connectionID);
			} catch (Exception e) {
				tracer.severe(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
	 */
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		this.tracer = sbbContext.getTracer(getClass().getSimpleName());
		try {
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");

			xmppSbbInterface = (XmppResourceAdaptorSbbInterface) myEnv
					.lookup("slee/resources/xmpp/2.0/xmppinterface");
			xmppActivityContextInterfaceFactory = (XmppActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/xmpp/2.0/factoryprovider");
			// env-entries
			username = (String) myEnv.lookup("username");
			password = (String) myEnv.lookup("password");

			tracer.info("setSbbContext() Retrieved uid[" + username + "],"
					+ " passwd[" + password + "]");
		} catch (NamingException ne) {
			tracer.severe("Could not set SBB context:" + ne.getMessage(), ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */
	public void unsetSbbContext() {
		this.sbbContext = null;
		this.tracer = null;
		xmppActivityContextInterfaceFactory = null;
		xmppSbbInterface = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbCreate()
	 */
	public void sbbCreate() throws javax.slee.CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPostCreate()
	 */
	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	public void sbbActivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	public void sbbPassivate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRemove()
	 */
	public void sbbRemove() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	public void sbbLoad() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */
	public void sbbStore() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception,
	 * java.lang.Object, javax.slee.ActivityContextInterface)
	 */
	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
	 */
	public void sbbRolledBack(RolledBackContext context) {
	}

}
