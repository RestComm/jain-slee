
package org.mobicents.csapi.jr.slee;

import javax.slee.resource.ResourceException;

/**
 * This interface defines the behaviour common to all client connections.
 */
public interface ClientConnection {

    /**
     * Closes this Connection. Any further invocations will throw an Exception
     * It is imperative that close() is called before
     * this reference goes out of scope or the connection pooling in the
     * application server may not function correctly.
     * @exception ResourceException if close fails
     */
    void close() throws ResourceException;
}
