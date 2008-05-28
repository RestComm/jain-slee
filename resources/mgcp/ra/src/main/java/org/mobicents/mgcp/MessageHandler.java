/*
 * File Name     : MessageHandler.java
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

import java.net.InetAddress;

import org.apache.log4j.Logger;

/**
 *
 * @author Oleg Kulikov
 */
public class MessageHandler implements Runnable {

    private JainMgcpStackImpl stack;
    private byte[] data;
    private InetAddress address;
    private int port;
    private Logger logger = Logger.getLogger(MessageHandler.class);

    /** Creates a new instance of MessageHandler */
    public MessageHandler(JainMgcpStackImpl stack, byte[] data,
            InetAddress address, int port) {
        this.stack = stack;
        this.data = data;
        this.address = address;
        this.port = port;
    }

    public boolean isRequest(String header) {
        return header.matches("[\\w]{4}(\\s|\\S)*");
    }

    public void run() {
        String msg = new String(data);
        int pos = msg.indexOf("\n");

        // extract message header to determine transaction handle parameter
        // and type of the received message
        String header = msg.substring(0, pos).trim();
        if (logger.isDebugEnabled()) {
            logger.debug("Message header: " + header);
        }

        // check message type
        // if this message is command then create new transaction handler
        // for specified type of this message.
        // if received message is a response then try to find corresponded
        // transaction to handle this message
        String tokens[] = header.split("\\s");
        if (isRequest(header)) {

            if (logger.isDebugEnabled()) {
                logger.debug("Processing command message");
            }

            String verb = tokens[0];
            if (verb.equalsIgnoreCase("crcx")) {
                TransactionHandler handle = new CreateConnectionHandler(
                        stack, address, port);
                handle.receiveCommand(msg);
            } else if (verb.equalsIgnoreCase("mdcx")) {
                TransactionHandler handle = new ModifyConnectionHandler(
                        stack, address, port);
                handle.receiveCommand(msg);
            } else if (verb.equalsIgnoreCase("dlcx")) {
                TransactionHandler handle = new DeleteConnectionHandler(
                        stack, address, port);
                handle.receiveCommand(msg);
            } else if (verb.equalsIgnoreCase("epcf")) {
                TransactionHandler handle = new EndpointConfigurationHandler(
                        stack, address, port);
                handle.receiveCommand(msg);
            } else if (verb.equalsIgnoreCase("rqnt")) {
                TransactionHandler handle = new NotificationRequestHandler(
                        stack, address, port);
                handle.receiveCommand(msg);
            } else if (verb.equalsIgnoreCase("ntfy")) {
                TransactionHandler handle = new NotifyHandler(
                        stack, address, port);
                handle.receiveCommand(msg);
            } else if (verb.equalsIgnoreCase("rsip")) {
                TransactionHandler handle = new RestartInProgressHandler(
                        stack, address, port);
                handle.receiveCommand(msg);
            } else {
                logger.warn("Unsupported message verbose " + verb);
                return;
            }

        } else {

            // RESPONSE HANDLING

            if (logger.isDebugEnabled()) {
                logger.debug("Processing response message");
            }

            Integer tid = Integer.decode(tokens[1]);

            TransactionHandler handler = (TransactionHandler) stack.transactions.remove(tid);

            if (handler == null) {
                logger.warn("Unknown transaction :" + tid);
                return;
            }

            handler.receiveResponse(msg);
        }
    }
}
