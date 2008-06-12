/*
 * File Name     : MgcpContentHandler.java
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

package org.mobicents.mgcp.stack.parser;

import java.text.ParseException;

/**
 * Receive notification of the logical content of a message. 
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public interface MgcpContentHandler {
    /** 
     * Receive notification of the header of a message.
     * Parser will call this method to report about header reading.
     *
     * @param header the header from the message.
     */
    public void header(String header) throws ParseException;
    
    /**
     * Receive notification of the parameter of a message.
     * Parser will call this method to report about parameter reading.
     *
     * @param name the name of the paremeter
     * @param value the value of the parameter.
     */
    public void param(String name, String value) throws ParseException;
    
    /**
     * Receive notification of the session description.
     * Parser will call this method to report about session descriptor reading.
     *
     * @param sd the session description from message.
     */
    public void sessionDescription(String sd) throws ParseException;
    
}
