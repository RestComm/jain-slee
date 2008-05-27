/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/


/*
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */

package org.mobicents.slee.container.xml;

import java.io.*;

/**
 * The class is used to mask any XML specific exceptions thrown during parsing
 * various descriptors.
 *
 * @author Emil Ivov
 * @version 1.0
 */

public class XMLException extends IOException
{
    /**
     * Constructs a new XMLException with the specified detail message and cause.
     *
     * @param message - the detail message (which is saved for later retrieval
     * by the Throwable.getMessage() method).
     *
     * @param cause - the cause (which is saved
     * for later retrieval by the Throwable.getCause() method). (A null value is
     * permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public XMLException(String message, Throwable cause)
    {
        super (message);
        initCause(cause);
    }

    /**
       * Constructs a new XMLException with the specified detail message.
       *
       * @param cause - the cause (which is saved
       * for later retrieval by the Throwable.getCause() method). (A null value is
       * permitted, and indicates that the cause is nonexistent or unknown.)
       */
    public XMLException(String message)
    {
        super(message);
    }

}
