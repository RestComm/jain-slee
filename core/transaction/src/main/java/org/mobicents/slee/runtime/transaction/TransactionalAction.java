/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Jan 19, 2005
 *
 * TransactionAction.java
 */
package org.mobicents.slee.runtime.transaction;

/**
 * Represents something that needs to happen when a SLEE transaction ends (commit or rollback)
 * Implements the command pattern
 * 
 * @author Tim
 *
 */
public interface TransactionalAction {
    public void execute();
}
