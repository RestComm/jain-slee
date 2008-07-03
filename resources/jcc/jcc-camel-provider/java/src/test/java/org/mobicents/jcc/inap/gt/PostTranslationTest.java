/*
 * TranslationTableTest.java
 * JUnit based test
 *
 * Created on 6 Сентябрь 2007 г., 10:12
 */

package org.mobicents.jcc.inap.gt;

import junit.framework.*;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;

/**
 *
 * @author Oleg Kulikov
 */
public class PostTranslationTest extends TestCase {
    
    private TranslationTable tt = new TranslationTable("/data/post-gt.properties");
    
    public PostTranslationTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }


    public void testTranslateToNational() throws Exception {
        System.out.println("translate to national");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.NATIONAL, 0,1,"9023629581");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("1999023629581", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateToInternational() throws Exception {
        System.out.println("translate to international");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.NATIONAL, 0,1,"89023629581");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("79023629581", number.getAddress());
        assertEquals(CalledPartyNumber.INTERNATIONAL, number.getNai());
    }

    public void testTranslateToUnknown() throws Exception {
        System.out.println("translate to international");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.NATIONAL, 0,1,"7777");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("7777", number.getAddress());
        assertEquals(CalledPartyNumber.UNKNOWN, number.getNai());
    }

    public void testTranslateToSubscriber() throws Exception {
        System.out.println("translate to subscriber");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.NATIONAL, 0,1,"77778");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("77778", number.getAddress());
        assertEquals(CalledPartyNumber.SUBSCRIBER, number.getNai());
    }

    public void testTranslateToSpare() throws Exception {
        System.out.println("translate to spare");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.NATIONAL, 0,1,"777789");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("777789", number.getAddress());
        assertEquals(CalledPartyNumber.SPARE, number.getNai());
    }
    
}
