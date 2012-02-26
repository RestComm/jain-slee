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
package org.mobicents.slee.resource.lab.stack;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.slee.facilities.Tracer;
import javax.slee.resource.ResourceAdaptorContext;

import org.apache.log4j.Logger;

/**
 * RAFStack represents the core of the RAFrame resource adaptor. It is a simple
 * TCP/IP server socket, listening in a separate Thread on incoming connections.
 * Listening is terminated every 1000ms to react on incoming "shutdown"
 * commands. <br>
 * The socket connection starts on incoming connections a separate Thread of
 * type com.maretzke.raframe.stack.RAStackThread which receives byte[]
 * information (max. 2kb). The received information of any kind is then
 * published to the registered listeners. This information is distributed as
 * String.<br>
 * The intention of the RAFStack is not to be very efficient or effective - it
 * is implemented to fulfill the requirements of a simple to understand resource
 * adaptor stack implementation which almost everybody is familiar with and it
 * does not cost too much time to understand the message flows.
 * 
 * @author amit bhayani
 * @author Michael Maretzke
 */
public class RAFStack extends Thread {
	private Tracer tracer;

	private Logger logger = Logger.getLogger(RAFStack.class);

	// the socket to listen on
	private ServerSocket server;

	// the registered listeners
	private ArrayList<RAFStackListener> listener;

	// flag to indicate shutdown
	private boolean shutdown = false;

	private ResourceAdaptorContext resourceAdaptorContext;

	private String remoteHost;
	private int remotePort = 0;

	public RAFStack(String localHost, int port, String remotehost,
			int remoteport) throws IOException {
		this(localHost, port, remotehost, remoteport, null);
	}

	/**
	 * Create an instance of RAFStack.
	 * 
	 * @param port
	 *            the port number to listen on
	 * @param remotehost
	 *            the remotehost name the stack sends information to
	 * @param remoteport
	 *            the remotehost's port the stack sends information to
	 */
	public RAFStack(String localHost, int port, String remotehost,
			int remoteport, ResourceAdaptorContext resourceAdaptorContext)
			throws IOException {

		if (resourceAdaptorContext != null) {
			this.resourceAdaptorContext = resourceAdaptorContext;
			this.tracer = this.resourceAdaptorContext.getTracer(RAFStack.class
					.getSimpleName());
		}
		this.listener = new ArrayList<RAFStackListener>();

		this.remoteHost = remotehost;
		this.remotePort = remoteport;

		InetSocketAddress local = new InetSocketAddress(localHost, port);
		server = new ServerSocket();
		server.bind(local);

		// set socket timeout to 1000ms
		server.setSoTimeout(1000);

		if (this.tracer != null) {
			if (this.tracer.isFineEnabled()) {
				this.tracer.fine("Started ServerSocket and bound to " + local);
			}
		} else {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Started ServerSocket and bound to " + local);
			}
		}
	}

	public void addListener(RAFStackListener listener) {
		this.listener.add(listener);
	}

	public void run() {
		Socket socket = null;

		while (!shutdown) {
			try {
				socket = server.accept();
				// create a new working thread to work with the incoming
				// information
				new RAFStackThread(socket, this, listener,
						resourceAdaptorContext).start();
			} catch (SocketTimeoutException ste) {
//				if (this.tracer != null) {
//					this.tracer
//							.severe(
//									"SocketTimeoutException while accepting new Client Socket",
//									ste);
//				} else {
//					logger
//							.error(
//									"SocketTimeoutException while accepting new Client Socket",
//									ste);
//				}
			} catch (SocketException ste) {
				if (this.tracer != null) {
					this.tracer
							.severe(
									"SocketException while accepting new Client Socket",
									ste);
				} else {
					logger
							.error(
									"SocketException while accepting new Client Socket",
									ste);
				}
			} catch (IOException ioe) {
				if (this.tracer != null) {
					this.tracer.severe(
							"IOException while accepting new Client Socket",
							ioe);
				} else {
					logger.error(
							"IOException while accepting new Client Socket",
							ioe);
				}
			}
		}
	}

	/**
	 * This method sets the flag to shut down the stack. The stack will shut
	 * down in less than 1000ms.
	 */
	public void shutdown() {
		shutdown = true;
		try {
			server.close();
			Thread.sleep(1000);
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Send a string message to a TCP/IP socket. The information is transported
	 * as a byte[] array.
	 * 
	 * @param message
	 *            the message to transport to the listening port
	 */
	public void send(String message) {
		Socket socket;
		OutputStream out;

		if (this.tracer != null) {
			if (this.tracer.isFineEnabled()) {
				this.tracer.fine("RAFStack sends the following information: "
						+ message);
			}
		} else {
			this.logger.debug("RAFStack sends the following information: "
					+ message);
		}

		try {
			// create new client socket
			socket = new Socket(remoteHost, remotePort);
			if (this.tracer != null) {
				if (this.tracer.isFineEnabled()) {
					this.tracer.fine("Socket bound to "
							+ socket.getLocalAddress() + " / "
							+ socket.getLocalPort());
				}
			} else {
				this.logger.debug("Socket bound to " + socket.getLocalAddress()
						+ " / " + socket.getLocalPort());
			}

			// get the output stream of the socket
			out = socket.getOutputStream();
			// write information into the socket
			out.write(message.getBytes());
			// close connection
			socket.close();
		} catch (IOException ioe) {
			if (this.tracer != null) {
				this.tracer.severe("IOException caught: " + ioe);
			} else {
				this.logger.error("IOException caught: " + ioe);
			}
		}
	}
}