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
 * IncorrectRequestFormatException is thrown by 
 * com.maretzke.raframe.message.MessageParser if the information to parse does
 * not follow the defined rules of a message.<br>
 *
 * @author Michael Maretzke
 */
public class IncorrectRequestFormatException extends Exception {
    
    public IncorrectRequestFormatException() {
    }
    
    public IncorrectRequestFormatException(String reason) {
        super(reason);
    }     
}
