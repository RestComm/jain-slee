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
 * The MessageParser interface needs to be implemented by concrete Message parser
 * implementations. 
 *
 * @author Michael Maretzke
 */
public interface MessageParser {
    public Message parse(String message) throws IncorrectRequestFormatException;
}
