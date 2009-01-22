/**
 * Start time:12:50:56 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import java.util.ArrayList;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.Event;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.InitialEventSelect;

/**
 * Start time:12:50:56 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEventEntry {

	private Event event = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.Event llEvent = null;

	private boolean initialEvent = false;
	private MEventDirection eventDirection = null;
	private boolean maskOnAttach = false;
	private boolean lastInTransaction = true;
	private String description = null;
	private ComponentKey eventReference = null;
	private String eventName = null;
	private ArrayList<MInitialEventSelect> initialEventSelects = null;
	private String initialEventSelectorMethod = null;
	private String resourceOption = null;

	public MEventEntry(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.Event llEvent) {
		super();
		this.llEvent = llEvent;
		
		this.eventDirection = MEventDirection.fromString(this.llEvent
				.getEventDirection());
		this.initialEvent = Boolean.parseBoolean(this.llEvent.getInitialEvent());
		this.maskOnAttach = Boolean.parseBoolean(this.llEvent.getMaskOnAttach());

		// 1.1 last in transaction
		String v=this.llEvent.getLastInTransaction();
		if(v!=null && !Boolean.parseBoolean(v))
		{
			this.lastInTransaction=false;
		}
		
		this.description = this.llEvent.getDescription() == null ? null
				: this.llEvent.getDescription().getvalue();
		this.eventReference = new ComponentKey(this.llEvent.getEventTypeRef()
				.getEventTypeName().getvalue(), this.llEvent.getEventTypeRef()
				.getEventTypeVendor().getvalue(), this.llEvent.getEventTypeRef()
				.getEventTypeVersion().getvalue());

		this.eventName=this.llEvent.getEventName().getvalue();
		this.initialEventSelects=new ArrayList<MInitialEventSelect>();
		
		if(this.llEvent.getInitialEventSelect()!=null)
		{
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.InitialEventSelect ies:this.llEvent.getInitialEventSelect())
			{
				MInitialEventSelect mies=new MInitialEventSelect(ies);
				this.initialEventSelects.add(mies);
			}
		}
		
		if(this.llEvent.getInitialEventSelectorMethodName()!=null )
		{
			this.initialEventSelectorMethod=this.llEvent.getInitialEventSelectorMethodName().getvalue();
		}
		if(this.llEvent.getEventResourceOption()!=null )
		{
			this.resourceOption=this.llEvent.getEventResourceOption().getvalue();
		}
		
		
	}

	public MEventEntry(Event event) {
		super();
		this.event = event;

		this.eventDirection = MEventDirection.fromString(this.event
				.getEventDirection());
		this.initialEvent = Boolean.parseBoolean(this.event.getInitialEvent());
		this.maskOnAttach = Boolean.parseBoolean(this.event.getMaskOnAttach());

		// 1.1 last in transaction
		this.description = this.event.getDescription() == null ? null
				: this.event.getDescription().getvalue();
		this.eventReference = new ComponentKey(this.event.getEventTypeRef()
				.getEventTypeName().getvalue(), this.event.getEventTypeRef()
				.getEventTypeVendor().getvalue(), this.event.getEventTypeRef()
				.getEventTypeVersion().getvalue());

		this.eventName=this.event.getEventName().getvalue();
		this.initialEventSelects=new ArrayList<MInitialEventSelect>();
		
		if(this.event.getInitialEventSelect()!=null)
		{
			for(InitialEventSelect ies:this.event.getInitialEventSelect())
			{
				MInitialEventSelect mies=new MInitialEventSelect(ies);
				this.initialEventSelects.add(mies);
			}
		}
		
		if(this.event.getInitialEventSelectorMethodName()!=null )
		{
			this.initialEventSelectorMethod=this.event.getInitialEventSelectorMethodName().getvalue();
		}
		if(this.event.getEventResourceOption()!=null )
		{
			this.resourceOption=this.event.getEventResourceOption().getvalue();
		}
		
		
		
	}

	public boolean isInitialEvent() {
		return initialEvent;
	}

	public MEventDirection getEventDirection() {
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

	public ComponentKey getEventReference() {
		return eventReference;
	}

	public String getEventName() {
		return eventName;
	}

	public ArrayList<MInitialEventSelect> getInitialEventSelects() {
		return initialEventSelects;
	}

	public String getInitialEventSelectorMethod() {
		return initialEventSelectorMethod;
	}

	public String getResourceOption() {
		return resourceOption;
	}

}
