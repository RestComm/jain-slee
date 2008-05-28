package net.java.slee.resource.diameter;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import net.java.slee.resource.diameter.activities.BaseProtocolActivity;
import net.java.slee.resource.diameter.activities.ShInterfaceActivity;

import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;




public interface DiameterRAActivityContextInterfaceFactory extends ResourceAdaptorActivityContextInterfaceFactory {

	
    
    
    //public ActivityContextInterface getActivityContextInterface(BaseProtocolActivity acivity) throws NullPointerException, UnrecognizedActivityException, FactoryException ;
    public ActivityContextInterface getActivityContextInterface(ShInterfaceActivity acivity) throws NullPointerException, UnrecognizedActivityException, FactoryException ;
}
