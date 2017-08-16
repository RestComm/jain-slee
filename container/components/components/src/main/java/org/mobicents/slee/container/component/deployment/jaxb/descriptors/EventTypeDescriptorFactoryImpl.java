/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventDefinition;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventJar;
import org.mobicents.slee.container.component.event.EventTypeDescriptorFactory;

/**
 * Factory to build {@link EventTypeDescriptorImpl} objects.
 * @author martins
 *
 */
public class EventTypeDescriptorFactoryImpl extends AbstractDescriptorFactory implements EventTypeDescriptorFactory {
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.event.EventTypeDescriptorFactory#parse(java.io.InputStream)
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
		
		List<LibraryID> libraryRefs = mEventJar.getLibraryRef();		
		for (MEventDefinition mEventDefinition : mEventJar.getEventDefinition()) {
			result.add(new EventTypeDescriptorImpl(mEventDefinition,libraryRefs,isSlee11));
		}
		return result;
	}
}
