/*
 * File Name     : CreateConnectionHandle.java
 *
 * The JAIN MGCP API implementaion.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.mgcp.stack;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.DeleteConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.NotificationRequestParms;
import jain.protocol.ip.mgcp.message.parms.RequestIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.mobicents.mgcp.stack.parser.MgcpContentHandler;
import org.mobicents.mgcp.stack.parser.MgcpMessageParser;
import org.mobicents.mgcp.stack.parser.Utils;


/**
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class DeleteConnectionHandler extends TransactionHandler {

    private DeleteConnection command;
    private DeleteConnectionResponse response;
    
    private Logger logger = Logger.getLogger(DeleteConnectionHandler.class);
    
    /** Creates a new instance of CreateConnectionHandle */
    public DeleteConnectionHandler(JainMgcpStackImpl stack) {
        super(stack);
    }
    
    public DeleteConnectionHandler(JainMgcpStackImpl stack, InetAddress address, int port) {
        super(stack, address, port);
    }
    
    protected JainMgcpCommandEvent decodeCommand(String message) throws ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Decoding delete connection command");
        }
        
        MgcpMessageParser parser = new MgcpMessageParser(
                new CommandContentHandle());
        try {
            parser.parse(message);
        } catch (IOException e) {
            //should never happen
        }
        
        return command;
    }
    
    protected JainMgcpResponseEvent decodeResponse(String message) throws ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Decoding delete connection response command");
        }
        
        MgcpMessageParser parser = new MgcpMessageParser(
                new ResponseContentHandle());
        try {
            parser.parse(message);
        } catch (Exception e) {
            //should never happen
        }
        
        return response;
    }
    
    protected String encode(JainMgcpCommandEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Encoding DeleteConnection object into MGCP delete connection command");
        }
        
        //encode message header
        
        DeleteConnection evt = (DeleteConnection) event;
        String msg = "DLCX " + evt.getTransactionHandle() + " " + 
                evt.getEndpointIdentifier() + " MGCP 1.0\n";
        
        
        //encode optional parameters
        
        if (evt.getBearerInformation() != null) {
            msg += "B:e:" + evt.getBearerInformation() + "\n";
        }
        
        if (evt.getCallIdentifier() != null) {
            msg += "C:" + evt.getCallIdentifier() + "\n";
        }
        
        if (evt.getConnectionIdentifier() != null) {
            msg += "I:" + evt.getConnectionIdentifier() + "\n";
        }
        
        if (evt.getConnectionParms() != null) {
            msg += "P:" + Utils.encodeConnectionParms(evt.getConnectionParms()) + "\n";
        }
        
        if (evt.getNotificationRequestParms() != null) {
            msg += Utils.encodeNotificationRequestParms(evt.getNotificationRequestParms());
        }
        
        if (evt.getReasonCode() != null) {
            msg += "E:" + evt.getReasonCode();
        }
        
        return msg;
    }
    
    protected String encode(JainMgcpResponseEvent event) {
        DeleteConnectionResponse response = (DeleteConnectionResponse) event;
        ReturnCode returnCode = response.getReturnCode();
        
        String msg = returnCode.getValue() + " " +
                response.getTransactionHandle()  + " " +
                returnCode.getComment() + "\n";
        
        if (response.getConnectionParms() != null) {
            msg += response.getConnectionParms() + "\n";
        }
        
        return msg;
    }
    
    private class CommandContentHandle implements MgcpContentHandler {
        
        
        public CommandContentHandle() {
        }
        
        /**
         * Receive notification of the header of a message.
         * Parser will call this method to report about header reading.
         *
         * @param header the header from the message.
         */
        public void header(String header) throws ParseException {
            String[] tokens = header.split("\\s");
            
            String verb = tokens[0].trim();
            String transactionID = tokens[1].trim();
            String version = tokens[3].trim() + " " + tokens[4].trim();
            
            int tid = Integer.parseInt(transactionID);
            EndpointIdentifier endpoint = Utils.decodeEndpointIdentifier(tokens[2].trim());
            
            command = new DeleteConnection(getObjectSource(tid), endpoint);
            command.setTransactionHandle(tid);
        }
        
        /**
         * Receive notification of the parameter of a message.
         * Parser will call this method to report about parameter reading.
         *
         * @param name the name of the paremeter
         * @param value the value of the parameter.
         */
        public void param(String name, String value) throws ParseException {
            if (name.equalsIgnoreCase("B")) {
                command.setBearerInformation(Utils.createBearerInformation(value));
            } else if (name.equalsIgnoreCase("c")) {
                command.setCallIdentifier(new CallIdentifier(value));
            } else if (name.equalsIgnoreCase("I")) {
                command.setConnectionIdentifier(new ConnectionIdentifier(value));
            } else if (name.equalsIgnoreCase("X")) {
                command.setNotificationRequestParms(
                        new NotificationRequestParms(
                        new RequestIdentifier(value)));
            } else if (name.equalsIgnoreCase("R")) {
                command.getNotificationRequestParms().setRequestedEvents(
                        Utils.decodeRequestedEvents(value));
            } else if (name.equalsIgnoreCase("S")) {
                command.getNotificationRequestParms().setSignalRequests(
                        Utils.decodeEventNames(value));
            } else if (name.equalsIgnoreCase("T")) {
                command.getNotificationRequestParms().setDetectEvents(
                        Utils.decodeEventNames(value));
            } else if (name.equalsIgnoreCase("P")) {
                command.setConnectionParms(Utils.decodeConnectionParms(value));
            } else if (name.equalsIgnoreCase("E")) {
                command.setReasonCode(Utils.decodeReasonCode(value));
            } 
        }
        
        /**
         * Receive notification of the session description.
         * Parser will call this method to report about session descriptor reading.
         *
         * @param sd the session description from message.
         */
        public void sessionDescription(String sd) throws ParseException {
            //do nothing
        }
    }
    
    private class ResponseContentHandle implements MgcpContentHandler {
        
        
        public ResponseContentHandle() {
        }
        
        /**
         * Receive notification of the header of a message.
         * Parser will call this method to report about header reading.
         *
         * @param header the header from the message.
         */
        public void header(String header) throws ParseException {
            String[] tokens = header.split("\\s");
            
            int tid = Integer.parseInt(tokens[1]);
            response = new DeleteConnectionResponse(stack,
                Utils.decodeReturnCode(Integer.parseInt(tokens[0])));
            response.setTransactionHandle(tid);
        }
        
        /**
         * Receive notification of the parameter of a message.
         * Parser will call this method to report about parameter reading.
         *
         * @param name the name of the paremeter
         * @param value the value of the parameter.
         */
        public void param(String name, String value) throws ParseException {
            if (name.equalsIgnoreCase("I")) {
                //@todo connection params
            } 
        }
        
        /**
         * Receive notification of the session description.
         * Parser will call this method to report about session descriptor reading.
         *
         * @param sd the session description from message.
         */
        public void sessionDescription(String sd) throws ParseException {
            //not used
        }
    }
    
}

