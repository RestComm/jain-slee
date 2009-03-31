/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor11;

import javax.slee.transaction.SleeTransactionManager;
import javax.transaction.SystemException;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.sbbapi.TransactionIDAccess;

public class TCKTransactionIDAccessImpl implements TransactionIDAccess {
    public TCKTransactionIDAccessImpl(SleeTransactionManager sleeTransactionManager) {
        this.sleeTransactionManager = sleeTransactionManager;
    }

    public Object getCurrentTransactionID() throws TCKTestErrorException {
        try {
            return sleeTransactionManager.getSleeTransaction().toString();
        } catch (SystemException e) {
            throw new TCKTestErrorException("current transaction ID could not be obtained, ");
        }
    }

    private SleeTransactionManager sleeTransactionManager;

}
