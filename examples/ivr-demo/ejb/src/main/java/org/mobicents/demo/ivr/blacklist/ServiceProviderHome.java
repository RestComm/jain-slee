/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.demo.ivr.blacklist;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 *
 * @author kulikov
 */
public interface ServiceProviderHome extends EJBLocalHome {
    public ServiceProvider create() throws CreateException;
}
