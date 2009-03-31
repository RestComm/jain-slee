/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.sbbapi;

import com.opencloud.sleetck.lib.TCKTestErrorException;

/**
 * The TransactionIDAccess interface is the Sbb's means of access to an ID
 * representing the current transaction.
 *
 * A TransactionIDAccess object may be accessed by an Sbb at any point in its lifecycle, and
 * may be used across multiple transactions.
 */
public interface TransactionIDAccess {

    /**
     * Returns an Object to represent the current transaction for the calling
     * thread, or null if (and only if) the current thread is not executing
     * within a transaction.
     * If the current thread is executing within a transaction, the returned
     * Object must satisfy the following requirements:<ol><li>
     * It must not be null (null indicates that there is no current transaction for the calling thread)</li><li>
     * id.equals(Object anotherID) returns true if and only if anotherID represents the same
     * transaction as this ID</li><li>
     * It must satisfy the requirements of the Object.hashcode() method </li><li>
     * It must be serializable: it and all its attributes must implement java.io.Serializable
     * </li></ol>
     */
    public Object getCurrentTransactionID() throws TCKTestErrorException;

}