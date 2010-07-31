/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.resource.lab.test.server;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.slee.resource.lab.stack.RAFStack;
import org.mobicents.slee.resource.lab.stack.RAFStackListener;

/**
 * RAFServer represents a stand-alone version of the RAFrame resource adaptor.
 * It is used as a test-instance for the resource adaptor wrapper.
 * 
 * @author Michael Maretzke
 */
public class RAFServer implements RAFStackListener {

	// the address of this server's socket
	private final static String LOCALHOST = "localhost";
	private final static int LOCALPORT = 40000;
	// the address of the receiving socket
	private final static String REMOTEHOST = "localhost";
	private final static int REMOTEPORT = 40001;
	private static Logger logger = Logger.getLogger(RAFServer.class);
	// the server stack instance
	private RAFStack stack;

	public RAFServer() {
		logger.info("RAFServer instantiated.");
		try {
			stack = new RAFStack(LOCALHOST, LOCALPORT, REMOTEHOST, REMOTEPORT);
		} catch (IOException ioe) {
			logger.error("Could not create the server stack! Terminating. "
					+ ioe);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		// log4j initialization
		PropertyConfigurator.configure(args[0]);
		// instantiate the server and go
		RAFServer server = new RAFServer();
		server.go();
	}

	public void go() {
		// add this class as a listener to the stack
		stack.addListener(this);
		// start the listening
		stack.start();
	}

	/**
	 * This method implements the RAFStackListener interface
	 */
	public void onEvent(String event) {
		logger.info("Event received: " + event);
		stack.send("And back: " + event);
	}
}
