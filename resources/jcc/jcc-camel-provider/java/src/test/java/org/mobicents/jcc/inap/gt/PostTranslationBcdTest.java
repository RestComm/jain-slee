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
public class PostTranslationBcdTest extends TestCase {
    
    private TranslationTable tt = new TranslationTable("/data/post-gt.properties");
    
    public PostTranslationBcdTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }


    public void testTranslateToNational() throws Exception {
        System.out.println("translate to national");
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0,CalledPartyNumber.NATIONAL, 0,"9023629581");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("1999023629581", number.getAddress());
        assertEquals(CalledPartyNumber.NATIONAL, number.getNai());
    }

    public void testTranslateToInternational() throws Exception {
        System.out.println("translate to international");
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0,CalledPartyNumber.NATIONAL,1,"89023629581");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("79023629581", number.getAddress());
        assertEquals(CalledPartyNumber.INTERNATIONAL, number.getNai());
    }

    public void testTranslateToUnknown() throws Exception {
        System.out.println("translate to international");
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0,CalledPartyNumber.NATIONAL, 1,"7777");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("7777", number.getAddress());
        assertEquals(CalledPartyNumber.UNKNOWN, number.getNai());
    }

    public void testTranslateToSubscriber() throws Exception {
        System.out.println("translate to subscriber");
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0,CalledPartyNumber.NATIONAL, 1,"77778");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("77778", number.getAddress());
        assertEquals(CalledPartyNumber.SUBSCRIBER, number.getNai());
    }

    public void testTranslateToSpare() throws Exception {
        System.out.println("translate to spare");
        
        CalledPartyBcdNumber cpn  = new CalledPartyBcdNumber(0,CalledPartyNumber.NATIONAL, 0,"777789");
        CalledPartyNumber number = tt.translate(cpn);
        assertEquals("777789", number.getAddress());
        assertEquals(CalledPartyNumber.SPARE, number.getNai());
    }
    
}
