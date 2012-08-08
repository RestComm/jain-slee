/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:12:50:56 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.Event;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.InitialEventSelect;
import org.mobicents.slee.container.component.sbb.EventDirection;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;

/**
 * Start time:12:50:56 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEventEntry implements EventEntryDescriptor {

	private boolean initialEvent = false;
	private EventDirection eventDirection = null;
	private boolean maskOnAttach = false;
	private boolean lastInTransaction = true;
	private String description = null;
	private EventTypeID eventReference=null;
	
	private String eventName = null;
	private String initialEventSelectorMethod = null;
	private String resourceOption = null;
	private final InitialEventSelectorVariablesImpl initialEventSelectorVariables = new InitialEventSelectorVariablesImpl();
	
	public MEventEntry(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.Event llEvent) {
		super();
		
		this.eventDirection = EventDirection.fromString(llEvent
				.getEventDirection());
		this.initialEvent = Boolean.parseBoolean(llEvent.getInitialEvent());
		this.maskOnAttach = Boolean.parseBoolean(llEvent.getMaskOnAttach());

		// 1.1 last in transaction
		String v=llEvent.getLastInTransaction();
		if(v!=null && !Boolean.parseBoolean(v))
		{
			this.lastInTransaction=false;
		}
		
		this.description = llEvent.getDescription() == null ? null
				: llEvent.getDescription().getvalue();
				
		this.eventReference=new EventTypeID(llEvent.getEventTypeRef().getEventTypeName().getvalue(), llEvent.getEventTypeRef().getEventTypeVendor().getvalue(), llEvent.getEventTypeRef().getEventTypeVersion().getvalue());

		this.eventName=llEvent.getEventName().getvalue();
		
		if(llEvent.getInitialEventSelect()!=null) {
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.InitialEventSelect ies:llEvent.getInitialEventSelect()) {
				initialEventSelectorVariables.setVariable(InitialEventSelectVariable.valueOf(ies.getVariable()));
			}
		}
		
		if(llEvent.getInitialEventSelectorMethodName()!=null )
		{
			this.initialEventSelectorMethod=llEvent.getInitialEventSelectorMethodName().getvalue();
		}
		if(llEvent.getEventResourceOption()!=null )
		{
			this.resourceOption=llEvent.getEventResourceOption().getvalue();
		}
		
		
	}

	public MEventEntry(Event event) {
		super();

		this.eventDirection = EventDirection.fromString(event
				.getEventDirection());
		this.initialEvent = Boolean.parseBoolean(event.getInitialEvent());
		this.maskOnAttach = Boolean.parseBoolean(event.getMaskOnAttach());

		// 1.1 last in transaction
		this.description = event.getDescription() == null ? null
				: event.getDescription().getvalue();

		this.eventReference=new EventTypeID(event.getEventTypeRef().getEventTypeName().getvalue(), event.getEventTypeRef().getEventTypeVendor().getvalue(), event.getEventTypeRef().getEventTypeVersion().getvalue());

		eventName=event.getEventName().getvalue();
		
		if(event.getInitialEventSelect()!=null) {
			for(InitialEventSelect ies:event.getInitialEventSelect()) {
				initialEventSelectorVariables.setVariable(InitialEventSelectVariable.valueOf(ies.getVariable()));
			}
		}
		
		if(event.getInitialEventSelectorMethodName()!=null )
		{
			this.initialEventSelectorMethod=event.getInitialEventSelectorMethodName().getvalue();
		}
		if(event.getEventResourceOption()!=null )
		{
			this.resourceOption=event.getEventResourceOption().getvalue();
		}
		
		
		
	}

	public boolean isInitialEvent() {
		return initialEvent;
	}

	public EventDirection getEventDirection() {
		return eventDirection;
	}

	public boolean isMaskOnAttach() {
		return maskOnAttach;
	}

	public boolean isLastInTransaction() {
		return lastInTransaction;
	}

	public String getDescription() {
		return description;
	}

	public EventTypeID getEventReference() {
		return eventReference;
	}

	public String getEventName() {
		return eventName;
	}

	@Override
	public InitialEventSelectorVariablesImpl getInitialEventSelectVariables() {
		return initialEventSelectorVariables;
	}

	public String getInitialEventSelectorMethod() {
		return initialEventSelectorMethod;
	}

	public String getResourceOption() {
		return resourceOption;
	}

	/**
	 * 
	 * @return true if the event direction is Receive or FireAndReceive
	 */
	public boolean isReceived() {
		return eventDirection == EventDirection.Receive || eventDirection == EventDirection.FireAndReceive;
	}

	/**
	 * 
	 * @return true if the event direction is Fire or FireAndReceive
	 */
	public boolean isFired() {
		return eventDirection == EventDirection.Fire || eventDirection == EventDirection.FireAndReceive;
	}
}
