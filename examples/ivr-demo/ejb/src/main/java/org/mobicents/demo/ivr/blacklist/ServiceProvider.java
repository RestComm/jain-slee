/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.demo.ivr.blacklist;

import javax.ejb.EJBLocalObject;

/**
 *
 * @author kulikov
 */
public interface ServiceProvider extends EJBLocalObject {
    public Profile getProfile(String address);
    public void subscribe(String address);
    public void unsubscribe(String address);
    
    public void block(String address, String addressToBlock);
    public void unblock(String address, String addressToBlock);    
}
