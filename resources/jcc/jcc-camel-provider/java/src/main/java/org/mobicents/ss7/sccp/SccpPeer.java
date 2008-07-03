/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.ss7.sccp;

import java.util.Properties;

/**
 *
 * @author Oleg Kulikov
 */
public class SccpPeer {
    
    public final static String INTEL_DRIVER = "INTEL_HDC";
    public final static String M3UA = "M3UA";
    
    private String driver;
    
    /** Creates a new instance of SccpPeer */
    public SccpPeer(String driver) {
        this.driver = driver;
    }
    
    public SccpProvider getProvider(String fileName) throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream(fileName));
        
        if (driver.equals(INTEL_DRIVER)) {
            return new SccpIntelHDCProviderImpl(properties);
        } else if (driver.equals(M3UA)) {
            return new SccpM3UAProviderImpl(properties);
        } else return new DummySccpProvider();
    }
}
