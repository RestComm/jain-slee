package org.mobicents.slee.container;

/**
 * The timer shared in the slee container, forbiddens
 * the usage of the cancel() method.
 * 
 * @author martins
 * 
 */
public class SleeContainerTimer extends java.util.Timer {

	@Override
	public void cancel() {
		throw new UnsupportedOperationException();
	}
	
	void realCancel() {
		super.cancel();
	}
}
