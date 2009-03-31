/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor;

import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.logging.Logable;

/**
 * The JAIN SLEE TCK 1.0 resource creator uses this interface to setup the resource.
 */
public interface TCKResourceSetupInterface {

    /**
     * The resource creator can optionally pass a logger to the resource
     * instance via this method. If no log is explicity set, the resource
     * logs to the standard error stream.
     */
    public void setLog(Logable log);

    /**
     * The resource creator must call this method and bind the
     * returned object to TCKResourceTestInterface.RMI_NAME in the
     * RMI registry during resource initialisation.
     * The TCKResourceTestInterface interface is used by the test to connect to the resource.
     */
    public TCKResourceTestInterface getTestInterface() throws TCKTestErrorException;

    /**
     * The resource creator is responsible for calling this method and ensuring that
     * the returned reference is made available to all JAIN SLEE TCK 1.0 resource adaptor entities.
     *
     * For example the resource creator for a single node JAIN SLEE 1.0 implementation might be the
     * resource adaptor entity itself, in which case the returned reference can simply
     * be stored locally in the resource adaptor entity.
     * For a distributed JAIN SLEE implementation, the resource creator may be a standalone application which
     * binds the returned reference in an RMI registry, to be looked up be each
     * JAIN SLEE TCK 1.0 resource adaptor entity object. If the interface is to be bound in an rmiregistry, 
     * it should be bound to TCKResourceAdaptorInterface.RMI_NAME.
     */
    public TCKResourceAdaptorInterface getResourceAdaptorInterface() throws TCKTestErrorException;

}
