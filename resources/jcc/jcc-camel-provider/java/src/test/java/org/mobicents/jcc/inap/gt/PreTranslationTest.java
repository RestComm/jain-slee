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
public class PreTranslationTest extends TestCase {
    
    private TranslationTable tt = new TranslationTable("/data/pre-gt.properties");
    
    public PreTranslationTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    /**
     * Test of translate method, of class org.itech.jcc.inap.gt.TranslationTable.
     */
    public void testTranslateInternational() throws Exception {
        System.out.println("translate international");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.INTERNATIONAL, 0,1,"79023629581");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("9023629581", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateNational() throws Exception {
        System.out.println("translate national");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.NATIONAL, 0,1,"89023629581");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("9023629581", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateSpare() throws Exception {
        System.out.println("translate spare");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.SPARE, 0,1,"7777");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("7777", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateUnknown() throws Exception {
        System.out.println("translate unknown");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.UNKNOWN, 0,1,"7777");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("7777", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateDefault() throws Exception {
        System.out.println("translate unknown");
        
        CalledPartyNumber cpn  = new CalledPartyNumber(CalledPartyNumber.SUBSCRIBER, 0,1,"7777");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("7777", number.getAddress());
        assertEquals(CalledPartyNumber.SUBSCRIBER, number.getNai());
    }

}
