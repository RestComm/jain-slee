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

package org.mobicents.slee.container.component.validator.test;

public abstract class SbbSuperClass  {

	
	// ------ LIFE CYCLE
	// Method descriptor #6 ()V
	public void sbbPassivate() {
	}

	// Method descriptor #6 ()V
	public void sbbLoad() {
	}

	// Method descriptor #6 ()V
	public void sbbStore() {
	}

	// Method descriptor #6 ()V
	public void sbbRemove() {
	}

	// Method descriptor #17
	// (Ljava/lang/Exception;Ljava/lang/Object;Ljavax/slee/ActivityContextInterface;)V
	public void sbbExceptionThrown(java.lang.Exception arg0,
			java.lang.Object arg1, javax.slee.ActivityContextInterface arg2) {

	}

	// Method descriptor #19 (Ljavax/slee/RolledBackContext;)V
	public void sbbRolledBack(javax.slee.RolledBackContext arg0) {

	}

	
	// ------ SBB LO
	
	
	
	public void doSomeMagicInSuperInterface(int i, String s)
	{
		
	}
	
	
	
	// ------ CMP
	
	public abstract void setSuperCMP(int i);
	public abstract int getSuperCMP();
	public abstract void setSharedCMP(String t);
	
}
