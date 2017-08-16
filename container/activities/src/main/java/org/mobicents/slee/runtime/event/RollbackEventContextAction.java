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

package org.mobicents.slee.runtime.event;

import java.io.Serializable;

import org.mobicents.slee.container.activity.ActivityEventQueueManager;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

/**
 * Tx action to rollback slee event.
 * 
 * @author eduardomartins
 *
 */
public class RollbackEventContextAction implements TransactionalAction, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8427380358783989321L;
	
	private final EventContext e;
	private final ActivityEventQueueManager aeqm; 
	
	public RollbackEventContextAction(EventContext e, ActivityEventQueueManager aeqm) {
		this.e = e;
		this.aeqm = aeqm;
	}
	
	public void execute() {				
		aeqm.rollback(e);						
	}
	
}