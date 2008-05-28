/*
 * File Name     : EndpointConfigurationHandle.java
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

package org.mobicents.mgcp;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.EndpointConfiguration;
import jain.protocol.ip.mgcp.message.EndpointConfigurationResponse;
import jain.protocol.ip.mgcp.message.parms.BearerInformation;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import java.net.InetAddress;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.mobicents.mgcp.parser.MgcpContentHandler;
import org.mobicents.mgcp.parser.MgcpMessageParser;
import org.mobicents.mgcp.parser.Utils;

/**
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class EndpointConfigurationHandler extends TransactionHandler {
	
	private EndpointConfiguration command;
    private EndpointConfigurationResponse response;
    
    private Logger logger = Logger.getLogger(EndpointConfigurationHandler.class);
    
    /** Creates a new instance of EndpointConfigurationHandle */
    public EndpointConfigurationHandler(JainMgcpStackImpl stack) {
        super(stack);
    }

    public EndpointConfigurationHandler(JainMgcpStackImpl stack, InetAddress address, int port) {
        super(stack, address, port);
    }
    
    protected JainMgcpCommandEvent decodeCommand(String message) throws ParseException {
        MgcpMessageParser parser = new MgcpMessageParser( new CommandContentHandle());
        try {
            parser.parse(message);
        } catch (Exception e) {
            //should never happen
        }
        
        return command;
    }

    protected JainMgcpResponseEvent decodeResponse(String message) throws ParseException {
        MgcpMessageParser parser = new MgcpMessageParser(new ResponseContentHandle());
        try {
            parser.parse(message);
        } catch (Exception e) {
            //should never happen
        }
        return response;
    }

    protected String encode(JainMgcpCommandEvent event) {
        //encode message header
        EndpointConfiguration evt = (EndpointConfiguration) event;
        String msg = "EPCF " + evt.getTransactionHandle() + " " + 
                evt.getEndpointIdentifier() + " MGCP 1.0\n";
        
        //encode mandatory parameters
        msg += "B:e:" + evt.getBearerInformation() + "\n";
        return msg;
    }

    protected String encode(JainMgcpResponseEvent event) {
        EndpointConfigurationResponse response = (EndpointConfigurationResponse) event;
        ReturnCode returnCode = response.getReturnCode();
        
        String msg = returnCode.getValue() + " " + response.getTransactionHandle()  + " " +
                returnCode.getComment() + "\n";
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
            
            command = new EndpointConfiguration(stack, endpoint, BearerInformation.EncMethod_A_Law);
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
            } 
        }
        
        /**
         * Receive notification of the session description.
         * Parser will call this method to report about session descriptor reading.
         *
         * @param sd the session description from message.
         */
        public void sessionDescription(String sd) throws ParseException {
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
            response = new EndpointConfigurationResponse(stack,
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
        }
        
        /**
         * Receive notification of the session description.
         * Parser will call this method to report about session descriptor reading.
         *
         * @param sd the session description from message.
         */
        public void sessionDescription(String sd) throws ParseException {
        }
    }
    
}
