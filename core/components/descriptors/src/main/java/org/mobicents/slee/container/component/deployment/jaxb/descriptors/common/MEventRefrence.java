/**
 * Start time:11:58:56 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EventTypeRef;

/**
 * Start time:11:58:56 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEventRefrence {

	private String description = null;
	private EventTypeRef sbbEventTypeRef = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EventTypeRef llSbbEvenTypeRef = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.EventTypeRef raTypeEventTypeRef = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.EventTypeRef llRaTypeEventTypeRef = null;
	//private ComponentKey reference = null;
	private EventTypeID reference=null;

	public MEventRefrence(EventTypeRef eventTypeRef, String description) {
		this.description = description;
		this.sbbEventTypeRef = eventTypeRef;
		this.reference = new EventTypeID(eventTypeRef.getEventTypeName()
				.getvalue(), eventTypeRef.getEventTypeVendor().getvalue(),
				eventTypeRef.getEventTypeVersion().getvalue());
	}

	public MEventRefrence(

			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EventTypeRef llSbbEvenTypeRef,
			String description) {
		super();
		this.description = description;
		this.llSbbEvenTypeRef = llSbbEvenTypeRef;
		this.reference = new EventTypeID(llSbbEvenTypeRef.getEventTypeName()
				.getvalue(), llSbbEvenTypeRef.getEventTypeVendor().getvalue(),
				llSbbEvenTypeRef.getEventTypeVersion().getvalue());
	}

	public MEventRefrence(
			String description,
			org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.EventTypeRef raTypeEventTypeRef) {
		super();
		this.description = description;
		this.raTypeEventTypeRef = raTypeEventTypeRef;
		this.reference = new EventTypeID(raTypeEventTypeRef.getEventTypeName()
				.getvalue(),
				raTypeEventTypeRef.getEventTypeVendor().getvalue(),
				raTypeEventTypeRef.getEventTypeVersion().getvalue());
	}

	public MEventRefrence(
			String description,
			org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.EventTypeRef llRaTypeEventTypeRef) {
		super();
		this.description = description;
		this.llRaTypeEventTypeRef = llRaTypeEventTypeRef;
		this.reference = new EventTypeID(llRaTypeEventTypeRef
				.getEventTypeName().getvalue(), llRaTypeEventTypeRef
				.getEventTypeVendor().getvalue(), llRaTypeEventTypeRef
				.getEventTypeVersion().getvalue());
	}

	public String getDescription() {
		return description;
	}

	public EventTypeID getReference() {
		return reference;
	}

}
