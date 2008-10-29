package org.mobicents.slee.resource.rules.ra;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.resource.rules.ratype.RulesSession;
import org.mobicents.slee.resource.rules.ratype.RulesActivityContextInterfaceFactory;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

public class RulesActivityContextInterfaceFactoryImpl implements
		RulesActivityContextInterfaceFactory,
		ResourceAdaptorActivityContextInterfaceFactory {

	private static Logger logger = Logger
			.getLogger(RulesActivityContextInterfaceFactoryImpl.class);
	
    // reference to the SLEE for further usage    
    private SleeContainer sleeContainer; 
    
    private ActivityContextFactory factory=null;
    
    private String raEntityName=null;    
    
    //jndi name of this acif
    String jndiName;
    
    public RulesActivityContextInterfaceFactoryImpl(SleeContainer sleeContainer, String name){
    	this.sleeContainer = sleeContainer;
    	this.jndiName = "java:slee/resources/" + name + "/rulesraacif";   
        factory=sleeContainer.getActivityContextFactory();
        this.raEntityName = name;    	
    	logger.debug("RulesActivityContextInterfaceFactoryImpl.jndiName = " + jndiName);
    	
    }

	public ActivityContextInterface getActivityContextInterface(
			RulesSession activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
        logger.debug("RulesActivityContextInterfaceFactoryImpl.getActivityContextInterface(" + activity.getId() + ") called.");	
        
        return  new ActivityContextInterfaceImpl( 
        		this.factory.getActivityContext(new SleeActivityHandle(raEntityName, new RulesActivityHandle(activity), sleeContainer)).getActivityContextId());        
		//return new ActivityContextInterfaceImpl(this.sleeContainer, activity.getId());
	}

	// implements ResourceAdaptorActivityContextInterfaceFactory
	public String getJndiName() {
		return jndiName;
	}

}
