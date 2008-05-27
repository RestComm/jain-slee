/*
* Created on Jul 7, 2004
*
* The source code contained in this file is in in the public domain.          
* It can be used in any project or product without prior permission, 	      
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/
package org.mobicents.slee.container.component;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;

import junit.framework.*;

/** Test case for component keys
 * 
 * @author F.Moggia
 * 
 */
public class ComponentKeyTest extends TestCase {
    private ComponentKey id2;

    private ComponentKey id3;

    private ComponentKey id1;

    private SbbIDImpl sbbid1;

    private SbbIDImpl sbbid2;

    private SbbIDImpl sbbid3;

    private ServiceIDImpl svcid1;

    private ServiceIDImpl svcid2;

    private ServiceIDImpl svcid3;

    public ComponentKeyTest(String arg) {
        super(arg);
    }

    protected void setUp() {
        id1 = new ComponentKey("Registrar1", "me", "1.0");
        id2 = new ComponentKey("Registrar2", "me", "1.0");
        id3 = new ComponentKey("Registrar1", "me", "1.0");

        sbbid1 = new SbbIDImpl(id1);
        sbbid2 = new SbbIDImpl(id2);
        sbbid3 = new SbbIDImpl(id3);

        svcid1 = new ServiceIDImpl(id1);
        svcid2 = new ServiceIDImpl(id2);
        svcid3 = new ServiceIDImpl(id3);

    }

    /*
     * Class under test for boolean equals(Object)
     */
    public void testEqualsComponentKey() {
        String expected = "Registrar1";
        String result = id1.getName();

        assertTrue(expected.equals(result));
        expected = "Registrar1/me/1.0";
        result = id1.toString();
        assertTrue(expected.equals(result));

        assertTrue(id1.equals(id3));
        assertTrue(id1.equals(id1));
        assertFalse(id1.equals(id2));
    }

    public void testSbbID() {
        String result = sbbid1.toString();
        String expec = "SbbID[Registrar1/me/1.0]";
        assertEquals(result, expec);
        assertTrue(sbbid1.equals(sbbid1));
        assertFalse(sbbid2.equals(sbbid1));
        assertTrue(sbbid1.equals(sbbid3));
    }

    public void testServiceID() {
        String result = svcid1.toString();
        String expec = "ServiceID[Registrar1/me/1.0]";
        assertEquals(result, expec);
        assertTrue(svcid1.equals(svcid1));
        assertFalse(svcid2.equals(svcid1));
        assertTrue(svcid3.equals(svcid1));
        

    }

}
