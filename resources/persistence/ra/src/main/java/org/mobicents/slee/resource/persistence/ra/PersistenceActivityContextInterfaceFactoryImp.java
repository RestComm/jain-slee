package org.mobicents.slee.resource.persistence.ra;

import javax.persistence.EntityManager;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.resource.persistence.ratype.PersistenceActivityContextInterfaceFactory;
import org.mobicents.slee.resource.persistence.ratype.SbbEntityManager;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

public class PersistenceActivityContextInterfaceFactoryImp implements
		PersistenceActivityContextInterfaceFactory,ResourceAdaptorActivityContextInterfaceFactory {

	
	private final String jndiName;
    private String raEntityName;
    private SleeContainer serviceContainer;
    
    private ActivityContextFactory activityContextFactory;
	
	
    public PersistenceActivityContextInterfaceFactoryImp(SleeContainer svcContainer, String entityName)
    {
    	this.serviceContainer = svcContainer;
        this.activityContextFactory = svcContainer.getActivityContextFactory();
        this.raEntityName = entityName;
        this.jndiName = "java:slee/resources/" + entityName + "/persistenceacif";
        
    	
    }
    
    
    
    
    
	public ActivityContextInterface getActivityContextInterface(
			SbbEntityManager em) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
		
		if(em==null)
		{
			throw new NullPointerException("Activity Cant be null!!!!!");
			
		}
		PersistenceActivityHandle PAH=new PersistenceActivityHandle(em);
		SleeActivityHandle SLAH=new SleeActivityHandle(raEntityName, PAH, serviceContainer);
		ActivityContext AC=this.activityContextFactory.getActivityContext(SLAH);
		return new ActivityContextInterfaceImpl(this.serviceContainer,AC.getActivityContextId());
	}

	public String getJndiName() {
		
		return this.jndiName;
	}




}
