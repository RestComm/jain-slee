package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventDefinition;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventJar;

/**
 * Factory to build {@link EventTypeDescriptorImpl} objects.
 * @author martins
 *
 */
public class EventTypeDescriptorFactory extends AbstractDescriptorFactory {
	
	/**
	 * Builds a list of {@link EventTypeDescriptorImpl} objects, from an {@link InputStream} containing the event jar xml.
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<EventTypeDescriptorImpl> parse(InputStream inputStream) throws DeploymentException {
		
		Object jaxbPojo = buildJAXBPojo(inputStream);
		
		List<EventTypeDescriptorImpl> result = new ArrayList<EventTypeDescriptorImpl>();
		
		boolean isSlee11 = false;
		MEventJar mEventJar = null;
		if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.event.EventJar) {
			mEventJar = new MEventJar((org.mobicents.slee.container.component.deployment.jaxb.slee.event.EventJar)jaxbPojo);
		}
		else if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.event.EventJar ) {
			mEventJar = new MEventJar((org.mobicents.slee.container.component.deployment.jaxb.slee11.event.EventJar)jaxbPojo);
			isSlee11 = true;
		} 
		else {
			throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
		}
		
		List<MLibraryRef> libraryRefs = mEventJar.getLibraryRef();		
		for (MEventDefinition mEventDefinition : mEventJar.getEventDefinition()) {
			result.add(new EventTypeDescriptorImpl(mEventDefinition,libraryRefs,isSlee11));
		}
		return result;
	}
}
