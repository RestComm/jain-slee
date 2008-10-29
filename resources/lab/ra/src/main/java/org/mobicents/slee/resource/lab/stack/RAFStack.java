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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

/**
 * RAFStack represents the core of the RAFrame resource adaptor. 
 * It is a simple TCP/IP server socket, listening in a separate 
 * Thread on incoming connections. Listening is terminated every
 * 1000ms to react on incoming "shutdown" commands. <br>
 * The socket connection starts on incoming connections a separate
 * Thread of type com.maretzke.raframe.stack.RAStackThread which
 * receives byte[] information (max. 2kb). The received information
 * of any kind is then published to the registered listeners. This
 * information is distributed as String.<br>
 * The intention of the RAFStack is not to be very efficient or 
 * effective - it is implemented to fulfill the requirements of a 
 * simple to understand resource adaptor stack implementation which
 * almost everybody is familiar with and it does not cost too much
 * time to understand the message flows.
 *
 * @author Michael Maretzke
 */
public class RAFStack extends Thread {
    private static Logger logger = Logger.getLogger(RAFStack.class);
    // the socket to listen on
    private ServerSocket server; 
    // the port to listen on 
    private int port; 
    // the port to which the stack sends information to
    private int remoteport;
    // the host to which the stack sends information to
    private String remotehost; 
    // the registered listeners
    private ArrayList listener;
    // flag to indicate shutdown
    private AtomicBoolean shutdown = new AtomicBoolean(false);
    
    /**
     * Create an instance of RAFStack.
     * @param port the port number to listen on
     * @param remotehost the remotehost name the stack sends information to 
     * @param remoteport the remotehost's port the stack sends information to
     */
    public RAFStack(int port, String remotehost, int remoteport) throws IOException {
        logger.debug("RAFStack instantiated.");
        this.port = port;
        this.remotehost = remotehost;
        this.remoteport = remoteport;
        listener = new ArrayList();        
        server = new ServerSocket(port);
        // set socket timeout to 1000ms
        server.setSoTimeout(1000);
    }
    
    public void addListener(RAFStackListener listener) {
        this.listener.add(listener);
    }
    
    public void run() {
        Socket socket = null;
        
        while (!shutdown.get()) {
            try {
                socket = server.accept();
                // create a new working thread to work with the incoming information
                new RAFStackThread(socket, this, listener).start();
            }
            catch (SocketTimeoutException ste) {}           
            catch (IOException ioe) {
                logger.error("IOException caught: " + ioe);
                ioe.printStackTrace();
            }            
        }
    }
    
    /**
     * This method sets the flag to shut down the stack. The stack will shut down in 
     * less than 1000ms. 
     */
    public void shutdown() {
        shutdown.set(true); 
        try {
			server.close();
		} catch (IOException e) {
			
		}
    }    
    
    /**
     * Send a string message to a TCP/IP socket. The information is transported as a
     * byte[] array.
     * 
     * @param message the message to transport to the listening port
     */
    public void send(String message) {
        Socket socket;
        OutputStream out;
        
        logger.debug("RAFStack sends the following information: " + message);    

        try {
            // create new client socket
            socket = new Socket(remotehost, remoteport);
            logger.debug("Socket bound to " + socket.getLocalAddress() + " / " + socket.getLocalPort());
            
            // get the output stream of the socket
            out = socket.getOutputStream();
            // write information into the socket
            out.write(message.getBytes());
            // close connection
            socket.close();
        }
        catch (IOException ioe) {
            logger.error("IOException caught: " + ioe);
        }
    }
}