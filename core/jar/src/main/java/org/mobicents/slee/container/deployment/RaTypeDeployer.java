package org.mobicents.slee.container.deployment;

import javax.slee.EventTypeID;
import javax.slee.management.DeploymentException;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.resource.ResourceAdaptorType;
import org.mobicents.slee.resource.ResourceAdaptorTypeDescriptorImpl;

public class RaTypeDeployer {

	private static Logger logger=Logger.getLogger(RaTypeDeployer.class);
	
	private ResourceAdaptorTypeDescriptorImpl raTypeDescriptor=null;
	private SleeContainer container=null;
	private RaTypeVerifier verifier=null;
	
	
	public RaTypeDeployer(ResourceAdaptorTypeDescriptorImpl raTypeDescriptor, SleeContainer container) {
		super();
		this.raTypeDescriptor = raTypeDescriptor;
		this.container = container;
	}



	public void deployRaType() throws DeploymentException{

		
		this.verifier=new RaTypeVerifier(raTypeDescriptor);
		
		boolean verified=false;
		try{
			verified=this.verifier.verifyRaType();
		}catch(Exception e)
		{
			logger.error("Exception while verifying ra type",e);
			throw new DeploymentException("Failed to verify RaType["+this.raTypeDescriptor.getID()+"] due to: "+e.getMessage());			
		}
		
		if(!verified)
		{
			throw new DeploymentException("Failed to verify RaType["+this.raTypeDescriptor.getID()+"]");
		}
		
		ComponentKey[] eventTypeRefEntries = raTypeDescriptor
				.getEventTypeRefEntries();
		EventTypeID[] eventTypeIDs = new EventTypeIDImpl[eventTypeRefEntries.length];
		for (int i = 0; i < eventTypeRefEntries.length; i++) {
			EventTypeID eventTypeId = (EventTypeID) container.getEventType(eventTypeRefEntries[i]);
			if (eventTypeId == null)
				throw new DeploymentException(
						"Could not resolve event type ref"
								+ eventTypeRefEntries[i]);
			else
				eventTypeIDs[i] = eventTypeId;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("The event type array has " + eventTypeIDs.length);
		}
		raTypeDescriptor.setEventTypes(eventTypeIDs);
		ResourceAdaptorType raType = new ResourceAdaptorType(raTypeDescriptor);
		ResourceAdaptorTypeID id = raType.getResourceAdaptorTypeID();
		
		container.addResourceAdaptorType(id, raType);
		
		logger.info("Added RA Type with id " + id);

	}

}
