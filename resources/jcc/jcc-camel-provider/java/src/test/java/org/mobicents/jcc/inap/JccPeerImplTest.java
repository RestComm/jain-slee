/*
 * JccPeerImplTest.java
 * JUnit based test
 *
 * Created on 28 Август 2007 г., 16:04
 */

package org.mobicents.jcc.inap;

import junit.framework.*;
import javax.csapi.cc.jcc.*;

/**
 *
 * @author Oleg Kulikov
 */
public class JccPeerImplTest extends TestCase {
    
    private JccPeer peer;
    private String conf = "<jcc-inap>; " +
            "sccp.provider=dummy;" +
            "sccp.conf=/data/sccp.properties;" +
            "incoming.initial.translation=/data/pre-gt.properties;" +
            "incoming.final.translation=/data/post-gt.properties;" +
            "outgoing.initial.translation=/data/pre-gt.properties;" +
            "outgoing.final.translation=/data/post-gt.properties";
    
    public JccPeerImplTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        try {
            peer = JccPeerFactory.getJccPeer("org.mobicents.jcc.inap.JccPeerImpl");
        } catch (ClassNotFoundException ex) {
            fail(ex.getMessage());
        }
    }

    @Override
    protected void tearDown() throws Exception {
    }

    /**
     * Test of getName method, of class org.itech.jcc.inap.JccPeerImpl.
     */
    public void testGetName() {
        assertEquals("Java Call Control API for INAP", peer.getName());
    }

    /**
     * Test of getProvider method, of class org.itech.jcc.inap.JccPeerImpl.
     */
    public void testGetProvider() {
        JccProvider provider = peer.getProvider(conf);
        if (provider == null) fail("Provider should be not null");
    }

    
}
