/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * 
 */
package org.mobicents.slee.container.sbb;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.Sbb;
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
