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
