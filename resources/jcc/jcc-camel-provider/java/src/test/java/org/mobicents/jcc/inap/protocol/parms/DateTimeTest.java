/*
 * DateTimeTest.java
 * JUnit based test
 *
 * Created on 25 Август 2007 г., 13:28
 */

package org.mobicents.jcc.inap.protocol.parms;

import junit.framework.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Oleg Kulikov
 */
public class DateTimeTest extends TestCase {
    
    private byte[] hex = new byte[] {0x02, 0x50, (byte)0x90, 0x10, 0x12, 0x13, 0x04};
    private Date etime;
    
    public DateTimeTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        String etimestr = "01.09.05 21:31:40";
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        try {
            etime = df.parse(etimestr);
        } catch (ParseException e) {
        }
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DateTimeTest.class);
        return suite;
    }

    public void testDecode() {
        try {
            DateTime time = new DateTime(hex);
            assertEquals(etime, time.getValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
