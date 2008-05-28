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
package org.mobicents.slee.resource.lab.test.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.slee.resource.lab.stack.RAFStack;
import org.mobicents.slee.resource.lab.stack.RAFStackListener;

/**
 * RAFClient is a very simple command line interface to send commands to the 
 * RAFrame resource adaptor executed inside the Mobicents JAIN SLEE 
 * implementation.<br>
 * Every input received from the command line interface will be send to the 
 * resource adaptor. Communication is done via TCP/IP.<br>
 * The command line interface will quit when entering "exit" as command.
 *
 * @author Michael Maretzke
 */
public class RAFClient implements RAFStackListener {
    // the address of the resource adaptor's socket
    private final static String REMOTEHOST = "localhost";
    private final static int REMOTEPORT = 40000;
    // the address of the sending socket
    private final static int LOCALPORT = 40001;
    private static Logger logger = Logger.getLogger(RAFClient.class);    
    private RAFStack stack;
    
    public RAFClient() {
        // create a new listening stack to receive answers from the RA
        try {
            stack = new RAFStack(LOCALPORT, REMOTEHOST, REMOTEPORT);
            stack.addListener(this);
            stack.start();        
        }
        catch (IOException ioe) {
            logger.error("Caught IOException. Could not create server port! Terminating. " + ioe);
            System.exit(1);
        }
    }
    
    public static void main(String [] args) {
        PropertyConfigurator.configure(args[0]);
        // create and start the CLI
        RAFClient client = new RAFClient();
        client.cli();
    }
    
    private void cli() {
        String command;
        
        try {
            while ((command = getCommand()).compareTo("exit") != 0) {
                logger.debug("command = " + command);
                // send the command to the RA
                send(command.getBytes());
            }
            // clean up and leave
            stack.shutdown();
        }
        catch (IOException ioe) {
            logger.error("Exception caught: " + ioe);
        }
    }
    
    private void send(byte[] command) throws IOException {
        Socket socket;
        OutputStream out;

        try {
            socket = new Socket(REMOTEHOST, REMOTEPORT);
            logger.debug("Socket bound to " + socket.getLocalAddress() + " / " + socket.getLocalPort());
            
            out = socket.getOutputStream();
            out.write(command);
            socket.close();
        }
        catch (IOException ioe) {
            logger.error("IOException caught: " + ioe);
            throw ioe; 
        }
    }

    private String getCommand() {
        String line = null;
        
        // command line handling
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(">> ");

        try {
            line = br.readLine();
        }
        catch (Exception e) {
            logger.error("Caught Exception: " + e);
        }
        return line.toLowerCase();      
    }

    /**
     * This method implements the RAFStackListener interface
     */
    public void onEvent(String incomingData) {
        System.out.println("<< " + incomingData);
        System.out.print(">> ");
    }    
}
