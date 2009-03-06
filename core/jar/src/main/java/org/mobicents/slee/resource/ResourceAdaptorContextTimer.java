package org.mobicents.slee.resource;

import javax.slee.resource.ResourceAdaptorContext;

/**
 * The timer returned by {@link ResourceAdaptorContext#getTimer()}, forbiddens
 * the usage of the cancel() method.
 * 
 * @author martins
 * 
 */
public class ResourceAdaptorContextTimer extends java.util.Timer {

	@Override
	public void cancel() {
		throw new UnsupportedOperationException();
	}
	
	void realCancel() {
		super.cancel();
	}
}
