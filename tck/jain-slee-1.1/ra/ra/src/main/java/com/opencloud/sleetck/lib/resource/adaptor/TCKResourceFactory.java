/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceImpl;
import java.rmi.RemoteException;

/**
 * Resource creators use this class to create an instance
 * of the JAIN SLEE TCK 1.0 resource.
 */
public class TCKResourceFactory {

    /**
     * Creates and returns an instance of the JAIN SLEE TCK 1.0 resource.
     */
    public static TCKResourceSetupInterface createResource() throws TCKTestErrorException {
        try {
            return new TCKResourceImpl();
        } catch(RemoteException re) {
            throw new TCKTestErrorException("RemoteException caught while creating resource",re);
        }
    }

}
