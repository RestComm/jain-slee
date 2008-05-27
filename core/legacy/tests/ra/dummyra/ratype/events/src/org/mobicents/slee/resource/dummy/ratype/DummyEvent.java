package org.mobicents.slee.resource.dummy.ratype;


import java.io.Serializable;

/**
 *
 * @author Oleg Kulikov
 */
public class DummyEvent implements Serializable {
    
    private int id;
    
    /** Creates a new instance of Event */
    public DummyEvent() {
    }
    
    public int getId() {
        return id;
    }
}
