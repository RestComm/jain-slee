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
package org.mobicents.slee.resource.lab.message;


/**
 * The RAFMessageParser accepts strings and split them according to predefined rules
 * into separate elements and creates value objects of type Message.<br>
 * The RAFStack stack supports a simple message format / protocol. It contains
 * an id and a command. Normally, the message format looks like:<br>
 * id command<br>
 * e.g.<br>
 * 100 INIT<br>
 * <br>
 * The message format is embedded in a "protocol".<br>
 * The very first message in a "dialog" is the "100 INIT" command. It initiates
 * the "dialog". Here, the "dialog" for id 100 is initiated.<br>
 * The next message in a valid "dialog" is any number of "100 ANY" commands. They
 * continue the "dialog". Here, the "dialog" for id 100 is continued.<br>
 * The terminating message is the "100 END" command. It terminates the "dialog". 
 * Here the "dialog" for id 100 ends.<br>
 * The RAFMessageParser implements the simple message protocol above. 
 * 
 * @author Michael Maretzke
 */
public class MessageParserImpl implements MessageParser {
    
    public MessageParserImpl() {
    }
    
    public Message parse(String message) throws IncorrectRequestFormatException {
        // expecting something like this: 1234 command
        int space = message.indexOf(' ');
        if ((space == -1) || (space == message.length()))
            throw new IncorrectRequestFormatException("RequestParser.parse(): The request " + message + " does not follow the request rules.");
        String callID = message.substring(0, space);
        String command = message.substring(space+1);
        return new MessageFactoryImpl().createMessage(callID, command);
    }    
}
