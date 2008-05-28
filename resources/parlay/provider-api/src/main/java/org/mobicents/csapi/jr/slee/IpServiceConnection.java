package org.mobicents.csapi.jr.slee;

import javax.slee.resource.ResourceException;


/**
 * This interface defines the behaviour common to all parlay interface connections .
 *
 */
public interface IpServiceConnection {

        // TODO remove this from here and ra src & test

       /** 
        * @deprecated No need to invoke this method. It will be removed from the API.
        * Close this Connection . Any further invocations with throw an Exception
        * It is imperative that close() is called before
        * this is reference goes out of scope for efficient connection pooling 
        * @exception ResourceException if close fails
        */
        void closeConnection() throws ResourceException;




     }
