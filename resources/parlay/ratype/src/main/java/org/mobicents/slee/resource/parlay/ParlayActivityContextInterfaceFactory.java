package org.mobicents.slee.resource.parlay;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier;
import org.mobicents.csapi.jr.slee.ui.TpUIIdentifier;

/**
 * ActivityConextInterfaceFactory for Parlay Resource Adaptor.
 * 
 * Returns the ActivityContextInterface by specified activity identifier.
 *  
 */
public interface ParlayActivityContextInterfaceFactory {

    /**
     * 
     * @param serviceIdentifier e.g. a call control manager service identifier
     * @return @throws
     *         NullPointerException
     * @throws UnrecognizedActivityException
     * @throws FactoryException
     */
    public ActivityContextInterface getActivityContextInterface(
            TpServiceIdentifier serviceIdentifier) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;

    /**
     * @param call
     * @return @throws
     *         NullPointerException
     * @throws UnrecognizedActivityException
     * @throws FactoryException
     */
    public ActivityContextInterface getActivityContextInterface(
            TpMultiPartyCallIdentifier call) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;

    /**
     * @param callLeg
     * @return @throws
     *         NullPointerException
     * @throws UnrecognizedActivityException
     * @throws FactoryException
     */
    public ActivityContextInterface getActivityContextInterface(
            TpCallLegIdentifier callLeg) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;
    
    /**
     * @param call
     * @return
     * @throws NullPointerException
     * @throws UnrecognizedActivityException
     * @throws FactoryException
     */
    public ActivityContextInterface getActivityContextInterface(TpCallIdentifier call) throws NullPointerException,
    	UnrecognizedActivityException, FactoryException;
    
    /**
     * @param ui
     * @return
     * @throws NullPointerException
     * @throws UnrecognizedActivityException
     * @throws FactoryException
     */
    public ActivityContextInterface getActivityContextInterface(TpUIIdentifier ui) throws NullPointerException,
        UnrecognizedActivityException, FactoryException;
    /**
     * @param uiCall
     * @return
     * @throws NullPointerException
     * @throws UnrecognizedActivityException
     * @throws FactoryException
     */
    public ActivityContextInterface getActivityContextInterface(TpUICallIdentifier uiCall) throws NullPointerException,
        UnrecognizedActivityException, FactoryException;

}