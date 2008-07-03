/*
 * JccCalledPartyBCDNumberTest.java
 * JUnit based test
 *
 * Created on 8 Сентябрь 2007 г., 11:50
 */

package org.mobicents.jcc.inap.address;

import javax.csapi.cc.jcc.JccPeer;
import javax.csapi.cc.jcc.JccPeerFactory;
import junit.framework.*;
import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccProvider;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyBcdNumber;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.JccInapProviderImpl;

/**
 *
 * @author Oleg Kulikov
 */
public class JccCalledPartyBCDNumberTest extends TestCase {
    
    private JccPeer peer;
    private JccProvider provider;

    private String conf = "<jcc-inap>; " +
            "sccp.provider=dummy;" +
            "sccp.conf=/data/sccp.properties;" +
            "terminating.initial.translation=/data/pre-gt.properties;" +
            "terminating.final.translation=/data/post-gt.properties;" +
            "originating.initial.translation=/data/pre-gt.properties;" +
            "originating.final.translation=/data/post-gt.properties";
    
    public JccCalledPartyBCDNumberTest(String testName) {
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
     * Test of getName method, of class org.itech.jcc.inap.address.JccCalledPartyBCDNumber.
     */
    public void testGetName() {
        CalledPartyBcdNumber n = new CalledPartyBcdNumber(0, CalledPartyNumber.NATIONAL, 0, "9023629581");
        JccCalledPartyBCDNumber cpn = new JccCalledPartyBCDNumber((JccInapProviderImpl) provider, n);
        assertEquals("9023629581", cpn.getName());
        
        cpn = new JccCalledPartyBCDNumber((JccInapProviderImpl)provider,
                new CalledPartyBcdNumber(0, CalledPartyNumber.INTERNATIONAL,0, "79023629581"));
        assertEquals("9023629581", cpn.getName());
    }

    /**
     * Test of getProvider method, of class org.itech.jcc.inap.address.JccCalledPartyBCDNumber.
     */
    public void testGetProvider() {
        CalledPartyBcdNumber n = new CalledPartyBcdNumber(0, CalledPartyNumber.NATIONAL, 0, "9023629581");
        JccCalledPartyBCDNumber cpn = new JccCalledPartyBCDNumber((JccInapProviderImpl) provider, n);
        assertEquals(provider, cpn.getProvider());
    }

    /**
     * Test of getType method, of class org.itech.jcc.inap.address.JccCalledPartyBCDNumber.
     */
    public void testGetType() {
        CalledPartyBcdNumber n = new CalledPartyBcdNumber(0, CalledPartyNumber.NATIONAL, 0, "9023629581");
        JccCalledPartyBCDNumber cpn = new JccCalledPartyBCDNumber((JccInapProviderImpl) provider, n);
        assertEquals(JccAddress.E164_MOBILE, cpn.getType());
    }

    /**
     * Test of getRouteNumber method, of class org.itech.jcc.inap.address.JccCalledPartyBCDNumber.
     */
    public void testGetRouteNumber() {
        CalledPartyBcdNumber n = new CalledPartyBcdNumber(0, CalledPartyNumber.NATIONAL, 0, "9023629581");
        JccCalledPartyBCDNumber cpn = new JccCalledPartyBCDNumber((JccInapProviderImpl) provider, n);
        assertEquals("1999023629581", cpn.getRouteNumber().getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, cpn.getRouteNumber().getNai());
    }

    /**
     * Test of toString method, of class org.itech.jcc.inap.address.JccCalledPartyBCDNumber.
     */
    public void testToString() {
        CalledPartyBcdNumber n = new CalledPartyBcdNumber(0, CalledPartyNumber.NATIONAL, 0, "9023629581");
        JccCalledPartyBCDNumber cpn = new JccCalledPartyBCDNumber((JccInapProviderImpl) provider, n);
        assertEquals("E164_MOBILE:9023629581", cpn.toString());
    }
    
}
