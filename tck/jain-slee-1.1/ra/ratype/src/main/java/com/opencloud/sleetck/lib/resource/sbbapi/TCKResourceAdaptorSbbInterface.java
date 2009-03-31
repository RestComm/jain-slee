/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.sbbapi;

/**
 * Defines the SBB's interface to the TCK resource adaptor.
 */
public interface TCKResourceAdaptorSbbInterface {

    /**
     * Returns a reference to the TCK resource.
     */
    public TCKResourceSbbInterface getResource();

    /**
     * Returns a TransactionIDAccess object
     */
    public TransactionIDAccess getTransactionIDAccess();

}