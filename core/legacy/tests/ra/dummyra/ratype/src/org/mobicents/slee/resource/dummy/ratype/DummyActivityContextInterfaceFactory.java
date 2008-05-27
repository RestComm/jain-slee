/*
 * ActivityContextInterfaceFactory.java
 *
 * Created on 14 Декабрь 2006 г., 9:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.dummy.ratype;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 *
 * @author Oleg Kulikov
 */
public interface DummyActivityContextInterfaceFactory {
       public ActivityContextInterface getActivityContextInterface(DummyActivity activity)
            throws NullPointerException, UnrecognizedActivityException, FactoryException;
}
