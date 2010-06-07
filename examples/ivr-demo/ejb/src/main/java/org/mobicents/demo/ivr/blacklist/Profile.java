/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.demo.ivr.blacklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author kulikov
 */
public class Profile implements Serializable {
    private String address;
    private ArrayList<String> restrictions = new ArrayList();
    
    public Profile(String address) {
        this.address = address;
    }
    
    public String getAddress() {
        return address;
    }
    
    public Collection<String> getRestrictions() {
        return restrictions;
    }
    
    public void add(String address) {
        restrictions.add(address);
    }
    
    public void remove(String address) {
        restrictions.remove(address);
    }
}
