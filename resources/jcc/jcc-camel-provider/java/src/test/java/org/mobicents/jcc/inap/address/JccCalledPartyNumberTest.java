/*
 * JccCalledPartyNumberTest.java
 * JUnit based test
 *
 * Created on 8 Сентябрь 2007 г., 10:47
 */

package org.mobicents.jcc.inap.address;

import javax.csapi.cc.jcc.JccPeer;
import javax.csapi.cc.jcc.JccPeerFactory;
import junit.framework.*;
import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccProvider;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.JccInapProviderImpl;

/**
 *
 * @author Oleg Kulikov
 */
public class JccCalledPartyNumberTest extends TestCase {
    
    private JccPeer peer;
    private JccProvider provider;

    private String conf = "<jcc-inap>; " +
            "sccp.provider=dummy;" +
            "sccp.conf=/data/sccp.properties;" +
            "terminating.initial.translation=/data/pre-gt.properties;" +
            "terminating.final.translation=/data/post-gt.properties;" +
            "originating.initial.translation=/data/pre-gt.properties;" +
            "originating.final.translation=/data/post-gt.properties";
    
    public JccCalledPartyNumberTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        try {
            peer = JccPeerFactory.getJccPeer("org.mobicents.jcc.inap.JccPeerImpl");
            provider = peer.getProvider(conf);
        } catch (ClassNotFoundException ex) {
            fail(ex.getMessage());
        }
    }

    @Override
    protected void tearDown() throws Exception {
    }

    /**
     * Test of getName method, of class org.itech.jcc.inap.address.JccCalledPartyNumber.
     */
    public void testGetName() {
        JccCalledPartyNumber cpn = new JccCalledPartyNumber(
                (JccInapProviderImpl) provider, "79023629581");
        assertEquals("79023629581", cpn.getName());

        cpn = new JccCalledPartyNumber(
                (JccInapProviderImpl) provider, 
                new CalledPartyNumber(CalledPartyNumber.INTERNATIONAL, 0, 0,"79023629581"));
        assertEquals("9023629581", cpn.getName());
    }

    /**
     * Test of getProvider method, of class org.itech.jcc.inap.address.JccCalledPartyNumber.
     */
    public void testGetProvider() {
        JccCalledPartyNumber cpn = new JccCalledPartyNumber(
                (JccInapProviderImpl) provider, "79023629581");
        assertEquals(provider, cpn.getProvider());
    }

    /**
     * Test of getType method, of class org.itech.jcc.inap.address.JccCalledPartyNumber.
     */
    public void testGetType() {
        JccCalledPartyNumber cpn = new JccCalledPartyNumber(
                (JccInapProviderImpl) provider, "79023629581");
        assertEquals(JccAddress.E164_MOBILE, cpn.getType());
    }

    /**
     * Test of getRouteAddress method, of class org.itech.jcc.inap.address.JccCalledPartyNumber.
     */
    public void testGetRouteAddress() {
        JccCalledPartyNumber cpn = new JccCalledPartyNumber(
                (JccInapProviderImpl) provider, "9023629581");
        assertEquals("1999023629581", cpn.getRouteAddress().getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, cpn.getRouteAddress().getNai());
    }

    /**
     * Test of toString method, of class org.itech.jcc.inap.address.JccCalledPartyNumber.
     */
    public void testToString() {
        JccCalledPartyNumber cpn = new JccCalledPartyNumber(
                (JccInapProviderImpl) provider, "79023629581");
        assertEquals("E164_MOBILE:79023629581", cpn.toString());
    }
    
}
