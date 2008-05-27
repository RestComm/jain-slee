/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * SingleProfileException.java
 * 
 * Created on 10 déc. 2004
 *
 */
package org.mobicents.slee.container.profile;

/** 
 * 
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 */
public class SingleProfileException extends Exception {

    /**
     * 
     */
    public SingleProfileException() {
        super();        
    }

    /**
     * @param arg0
     */
    public SingleProfileException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public SingleProfileException(Throwable arg0) {
        super(arg0);        
    }

    /**
     * @param arg0
     * @param arg1
     */
    public SingleProfileException(String arg0, Throwable arg1) {
        super(arg0, arg1);        
    }

}
