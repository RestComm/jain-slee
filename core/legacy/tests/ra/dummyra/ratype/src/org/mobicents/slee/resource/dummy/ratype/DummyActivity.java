/*
 * DummyActivity.java
 *
 * Created on 14 Декабрь 2006 г., 9:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.dummy.ratype;

import java.io.Serializable;

/**
 *
 * @author Oleg Kulikov
 */
public class DummyActivity implements Serializable {
    
    private String id;
    
    /** Creates a new instance of DummyActivity */
    public DummyActivity(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
}
