/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.demo.ivr.blacklist;

import java.rmi.RemoteException;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 *
 * @author kulikov
 */
public class ServiceProviderBean implements SessionBean {

    private HashMap<String, Profile> profiles = new HashMap();
    
    public Profile getProfile(String address) {
        return profiles.get(address);
    }
    
    public void subscribe(String address) {
        Profile profile = new Profile(address);
        profiles.put(address, profile);
    }
    
    public void unsubscribe(String address) {
        profiles.remove(address);
    }
    
    public void block(String address, String addressToBlock) {
        Profile profile = profiles.get(address);
        profile.add(addressToBlock);
    }

    public void unblock(String address, String addressToBlock) {
        Profile profile = profiles.get(address);
        profile.remove(addressToBlock);
    }
    
    public void ejbCreate() throws CreateException {
    }

    public void ejbPostCreate() throws CreateException {
    }
    
    public void setSessionContext(SessionContext context) throws EJBException, RemoteException {
        Profile profile = new Profile("79023629581");
        profiles.put("79023629581", profile);
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

}
