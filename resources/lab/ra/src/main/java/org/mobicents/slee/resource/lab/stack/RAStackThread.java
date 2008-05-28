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
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;


/**
 * RAStackThread implements a single working thread to work on incoming data
 * after RAFStack.serversocket.accept() returns with a socket.<br>
 * The thread simply receives the byte[] stream information and publish it to
 * the registered listeners.<br>
 * The thread terminates immediately after information was received and 
 * the connection is closed.
 *
 * @author Michael Maretzke
 */
public class RAStackThread extends Thread {
    private static Logger logger = Logger.getLogger(RAStackThread.class);
    // the socket returned by RAFStack.serversocket.accept()
    private Socket socket;
    private ArrayList listener;
    // the thread owning instance of RAFStack
    private RAFStack parent;
    
    /**
     * Create an instance of RAStackThread
     *
     * @param socket the socket returned by RAFStack.serversocket.accept()
     * @param parent the thread owning parent instance
     * @param listener the listeners to be informed when information received.
     */    
    public RAStackThread(Socket socket, RAFStack parent, ArrayList listener) {
        this.parent = parent;
        this.socket = socket;
        this.listener = listener; 
    }
    
    /**
     * Iterates through the listeners and invokes the onEvent() method with
     * the information received through the socket.
     */
    private void informListeners(String incomingData) {
        Iterator it = listener.iterator();

        while(it.hasNext()) {
            RAFStackListener rsl = (RAFStackListener) it.next();
            rsl.onEvent(incomingData);
        }
    }
    
    public void run() {
        try {
            logger.debug("Serverthread " + getName() + " started. ");
            InputStream in;
            byte [] b = new byte[2000];

            // get the socket's input stream
            in = socket.getInputStream();
            // read information of type byte[] - read contains the amount of information read
            int read = in.read(b);
            // extract the "real" information 
            String request = new String(b, 0, read);
            logger.debug("bytes received (" + read + ") = " + request); 
            informListeners(request);            
        } 
        catch (IOException io) {
            logger.error("IOException caught. " + io);
            io.printStackTrace();
        }
        logger.debug("Serverthread " + getName() + " finished. ");
    }
}

