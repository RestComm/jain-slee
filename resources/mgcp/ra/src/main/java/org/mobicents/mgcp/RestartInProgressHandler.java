
package org.mobicents.mgcp;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.RestartInProgress;
import jain.protocol.ip.mgcp.message.RestartInProgressResponse;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.RestartMethod;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;

import org.mobicents.mgcp.parser.MgcpContentHandler;
import org.mobicents.mgcp.parser.MgcpMessageParser;
import org.mobicents.mgcp.parser.Utils;

/**
 * Parse/encode RSIP commands.
 * 
 * @author Tom Uijldert
 *
 */
public class RestartInProgressHandler extends TransactionHandler {

    private RestartInProgress command;
    private RestartInProgressResponse response;

    public RestartInProgressHandler(JainMgcpStackImpl stack) {
        super(stack);
    }

    public RestartInProgressHandler(JainMgcpStackImpl stack, InetAddress address, int port) {
        super(stack, address, port);
    }

    @Override
    protected JainMgcpCommandEvent decodeCommand(String message)
            throws ParseException {
        MgcpMessageParser parser = new MgcpMessageParser(new CommandContentHandle());
        try {
            parser.parse(message);
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), -1);
        }
        return command;
    }

    @Override
    protected JainMgcpResponseEvent decodeResponse(String message)
            throws ParseException {
        MgcpMessageParser parser = new MgcpMessageParser(new ResponseContentHandle());
        try {
            parser.parse(message);
        } catch (IOException e) {
        //should never happen
        }
        return response;
    }

    @Override
    protected String encode(JainMgcpCommandEvent event) {
        RestartInProgress rsip = (RestartInProgress) event;
        StringBuffer message = new StringBuffer();
        message.append("RSIP " + event.getTransactionHandle() + " " +
                rsip.getEndpointIdentifier() + " MGCP 1.0\n");

        message.append("RM:" + rsip.getRestartMethod() + "\n");
        if (rsip.getRestartDelay() != 0) {
            message.append("RD:" + rsip.getRestartDelay() + "\n");
        }
        if (rsip.getReasonCode() != null) {
            message.append("E:" + rsip.getReasonCode() + "\n");
        }
        return message.toString();
    }

    @Override
    protected String encode(JainMgcpResponseEvent event) {
        return event.getReturnCode().getValue() + " " + event.getTransactionHandle() +
                " " + event.getReturnCode().getComment() + "\n";
    }

    private class CommandContentHandle implements MgcpContentHandler {

        public void header(String header) throws ParseException {
            String[] tokens = header.split("\\s");

            String verb = tokens[0].trim();
            String transactionID = tokens[1].trim();
            String version = tokens[3].trim() + " " + tokens[4].trim();

            int tid = Integer.parseInt(transactionID);
            EndpointIdentifier endpoint = Utils.decodeEndpointIdentifier(tokens[2].trim());

            command = new RestartInProgress(getObjectSource(tid), endpoint, RestartMethod.Restart);
            command.setTransactionHandle(tid);
        }

        public void param(String name, String value) throws ParseException {
            if (name.equalsIgnoreCase("RM")) {
                command.setRestartMethod(Utils.decodeRestartMethod(value));
            } else if (name.equalsIgnoreCase("RD")) {
                command.setRestartDelay(Integer.parseInt(value));
            } else if (name.equalsIgnoreCase("E")) {
                command.setReasonCode(Utils.decodeReasonCode(value));
            }
        }

        public void sessionDescription(String sd) throws ParseException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private class ResponseContentHandle implements MgcpContentHandler {

        public void header(String header) throws ParseException {
            String[] tokens = header.split("\\s");

            int tid = Integer.parseInt(tokens[1]);
            response = new RestartInProgressResponse(stack,
                    Utils.decodeReturnCode(Integer.parseInt(tokens[0])));
            response.setTransactionHandle(tid);
        }
        // TODO: Add support for [NotifiedEntity] and [PackageList]
        public void param(String name, String value) throws ParseException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void sessionDescription(String sd) throws ParseException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
