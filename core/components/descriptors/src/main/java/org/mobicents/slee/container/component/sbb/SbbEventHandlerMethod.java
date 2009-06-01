package org.mobicents.slee.container.component.sbb;

import java.lang.reflect.Method;

import javax.slee.EventTypeID;

/**
 * Sbb event handler method info to deliver an event to the sbb component.
 *  
 * @author martins
 *
 */
public class SbbEventHandlerMethod {

	private final Method eventHandlerMethod;
	private final Class aciParamClass;
	private final boolean hasEventContextParam;
	
	public SbbEventHandlerMethod(Method eventHandlerMethod,
			Class aciParamClass, boolean hasEventContextParam) {
		this.eventHandlerMethod = eventHandlerMethod;
		this.aciParamClass = aciParamClass;
		this.hasEventContextParam = hasEventContextParam;
	}

	public Method getEventHandlerMethod() {
		return eventHandlerMethod;
	}

	public Class getAciParamClass() {
		return aciParamClass;
	}

	public boolean isHasEventContextParam() {
		return hasEventContextParam;
	}
	
}
