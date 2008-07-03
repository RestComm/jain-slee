/*
 * TranslationTableTest.java
 * JUnit based test
 *
 * Created on 6 Сентябрь 2007 г., 10:12
 */

package org.mobicents.jcc.inap.gt;

import junit.framework.*;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyBcdNumber;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;

/**
 *
 * @author Oleg Kulikov
 */
public class PreTranslationBcdTest extends TestCase {
    
    private TranslationTable tt = new TranslationTable("/data/pre-gt.properties");
    
    public PreTranslationBcdTest(String testName) {
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
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0, CalledPartyNumber.INTERNATIONAL, 0,"79023629581");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("9023629581", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateNational() throws Exception {
        System.out.println("translate national");
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0,CalledPartyNumber.NATIONAL, 0,"89023629581");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("9023629581", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateSpare() throws Exception {
        System.out.println("translate spare");
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0,CalledPartyNumber.SPARE, 0,"7777");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("7777", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateUnknown() throws Exception {
        System.out.println("translate unknown");
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0,CalledPartyNumber.UNKNOWN, 0,"7777");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("7777", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateDefault() throws Exception {
        System.out.println("translate unknown");
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0,CalledPartyNumber.SUBSCRIBER, 1,"7777");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("7777", number.getAddress());
        assertEquals(CalledPartyNumber.SUBSCRIBER, number.getNai());
    }

}
