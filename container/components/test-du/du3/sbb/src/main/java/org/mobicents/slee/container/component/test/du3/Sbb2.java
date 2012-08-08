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

package org.mobicents.slee.container.component.test.du3;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceStartedEvent;

public abstract class Sbb2 implements Sbb {

	public abstract void setSbb1(Sbb1LocalObject sbbLocalObject);
	public abstract Sbb1LocalObject getSbb1();
	
	public void sbbActivate() {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbCreate() throws CreateException {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbLoad() {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbPassivate() {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbPostCreate() throws CreateException {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbRemove() {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbRolledBack(RolledBackContext arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void sbbStore() {
		// TODO Auto-generated method stub
		
	}
	
	public void setSbbContext(SbbContext arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void unsetSbbContext() {
		// TODO Auto-generated method stub
		
	}
	
	public void onServiceStartedEvent(ServiceStartedEvent event, ActivityContextInterface aci) {
		
	}
}
