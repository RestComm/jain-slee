package org.mobicents.slee.services.sip.proxy;

import javax.slee.ActivityContextInterface;

public interface ProxySbbActivityContextInterface extends
		ActivityContextInterface {

	
	
    
    /**
	 * This method is ment for alliasing purposes of service chaining. Should
	 * return true if some other service has handled sip call.
	 * 
	 * @return
	 * <li><b>true</b> - if this call has been handled by service lower in
	 * chain.
	 * <li><b>false</b> - otheriwse
	 */
	public boolean getHandledByAncestor();

	/**
	 * This method is ment for alliasing purposes of service chaining. If this
	 * call has been handled by service lower in chain (
	 * {@link #getHandledByAncestor()} returned <b>true</b> or this call is
	 * beeing handled by proxy, paramter should be <b>true</b>. Otherwise it
	 * should be <b>false</b>.
	 * 
	 * @param handled
	 */
	public void setHandledByMe(boolean handled);
	// if someone needs it.
	public boolean getHandledByMe();
	
	
}
