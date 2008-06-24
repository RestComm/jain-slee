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
package org.mobicents.mgcp.stack;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static Logger logger = Logger.getLogger(MessageHandler.class);

    /** Creates a new instance of MessageHandler */
    public MessageHandler(JainMgcpStackImpl stack, byte[] data,
            InetAddress address, int port) {
        this.stack = stack;
        this.data = data;
        this.address = address;
        this.port = port;
    }

    /**
     * RFC 3435, $3.5.5: split piggybacked messages again
     * <P>
     * Messages within the packet are split on their separator "EOL DOT EOL".
     *
     * @param packet	the packet to split
     * @return array of all separate messages
     */
    public static String[] piggyDismount(String packet) {
        final String pb = "\r?\n\\.\r?\n";
        final Pattern p = Pattern.compile(pb);
        int idx = 0;
        ArrayList<String> mList = new ArrayList<String>();

        Matcher m = p.matcher(packet);
        while (m.find()) {
            mList.add(packet.substring(idx, m.start()) + "\n");
            idx = m.end();
        }
        mList.add(packet.substring(idx));
        String[] result = new String[mList.size()];
        return (String[]) mList.toArray(result);
    }

    public boolean isRequest(String header) {
        return header.matches("[\\w]{4}(\\s|\\S)*");
    }

    public void run() {
        for (String msg : piggyDismount(new String(data))) {

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
                TransactionHandler handle;
                if (verb.equalsIgnoreCase("crcx")) {
                    handle = new CreateConnectionHandler(stack, address, port);
                } else if (verb.equalsIgnoreCase("mdcx")) {
                    handle = new ModifyConnectionHandler(stack, address, port);
                } else if (verb.equalsIgnoreCase("dlcx")) {
                    handle = new DeleteConnectionHandler(stack, address, port);
                } else if (verb.equalsIgnoreCase("epcf")) {
                    handle = new EndpointConfigurationHandler(stack, address, port);
                } else if (verb.equalsIgnoreCase("rqnt")) {
                    handle = new NotificationRequestHandler(stack, address, port);
                } else if (verb.equalsIgnoreCase("ntfy")) {
                    handle = new NotifyHandler(stack, address, port);
                } else if (verb.equalsIgnoreCase("rsip")) {
                    handle = new RestartInProgressHandler(stack, address, port);
                } else {
                    logger.warn("Unsupported message verbose " + verb);
                    return;
                }
                handle.receiveCommand(msg);
            } else {
                // RESPONSE HANDLING
                if (logger.isDebugEnabled()) {
                    logger.debug("Processing response message");
                }
                String domainName = address.getHostName();
                String tid = tokens[1];

                TransactionHandler handler = (TransactionHandler) stack.transactions.get(Integer.valueOf(tid));
                if (handler == null) {
                    logger.warn("Unknown transaction: " + tid);
                    return;
                }
                handler.receiveResponse(msg);
            }
        }
    }
}
