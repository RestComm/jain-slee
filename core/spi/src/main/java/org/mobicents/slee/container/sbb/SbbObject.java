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
 * 
 */
package org.mobicents.slee.container.sbb;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;

import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.eventrouter.SbbInvocationState;
import org.mobicents.slee.container.sbbentity.SbbEntity;

/**
 * @author martins
 *
 */
public interface SbbObject {

	public SbbInvocationState getInvocationState();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.runtime.SbbEntity#getSbbComponent()
	 */
	public SbbComponent getSbbComponent();

	/**
	 * Getter for property sbbConcrete. The sbb concrete object is an instance
	 * of the concrete class that is instantiated by the deployer.
	 * 
	 * @return Value of property sbbConcrete.
	 * 
	 */
	public Sbb getSbbConcrete();

	/**
	 * Getter for property sbbContextobj.
	 * 
	 * @return Value of property sbbContextobj.
	 * 
	 */
	public SbbContext getSbbContext();

	public SbbEntity getSbbEntity() throws TransactionRequiredLocalException;

	public ServiceID getServiceID();

	/**
	 * @return Returns the state.
	 */
	public SbbObjectState getState();
	
	/**
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	public void sbbActivate();

	/**
	 * An SBB object transitions from the Pooled state to the Ready state when
	 * the SLEE selects that SBB object to process an event or to service a
	 * logic object invocation. There are two possible transitions from the
	 * Pooled state to the Ready state: through the sbbCreate and sbbPostCreate
	 * methods, or through the sbbActivate method. The SLEE invokes the
	 * sbbCreate and sbbPostCreate methods when the SBB object is assigned to a
	 * new SBB entity that has just been created explicitly by an invocation of
	 * the create method on a ChildRelation object or implicitly by the SLEE to
	 * process an initial event.
	 * 
	 * @see javax.slee.Sbb#sbbCreate()
	 * 
	 */
	public void sbbCreate() throws CreateException;

	public void sbbExceptionThrown(Exception exception);

	/**
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	public void sbbLoad() throws TransactionRequiredLocalException;
	
	/**
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	public void sbbPassivate();
	
	/**
	 * This method may throw a javax.slee.CreateException when there is an
	 * application level problem (rather than SLEE or system level problem). The
	 * SLEE will propagate the CreateException unchanged to the caller that
	 * requested the creation of the SBB entity. The caller may be the SLEE or
	 * an SBB object. The throws clause is optional. The SLEE invokes this
	 * method with the transaction context (see Section 9.6) used to invoke the
	 * sbbCreate method. The SBB entity enters the Ready state when
	 * sbbPostCreate returns normally. If sbbPost- Create returns by throwing an
	 * exception, the SBB entity does not become Ready.
	 * 
	 * @see javax.slee.Sbb#sbbPostCreate()
	 * 
	 */
	public void sbbPostCreate() throws CreateException;

	public void sbbRemove();
	
	public void sbbRolledBack(Object event,
            ActivityContextInterface activityContextInterface,
            boolean removeRollback);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */
	public void sbbStore();
	
	public void setSbbEntity(SbbEntity sbbe);
	
	public void setSbbInvocationState(SbbInvocationState state);
	
	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(SbbObjectState state);
	
	/**
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */	
	public void unsetSbbContext();
}
