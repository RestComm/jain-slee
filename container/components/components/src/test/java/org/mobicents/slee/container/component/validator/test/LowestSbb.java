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

package org.mobicents.slee.container.component.validator.test;

import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.SbbContext;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.mobicents.slee.container.component.validator.sbb.abstracts.aci.ACIConstraintsOk;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageOkInterface;

public abstract class LowestSbb extends SbbSuperClass implements javax.slee.Sbb
{

	

	

	public void sbbActivate() {
		// TODO Auto-generated method stub
		
	}

	public void sbbCreate() throws CreateException {
		// TODO Auto-generated method stub
		
	}

	public void sbbPostCreate() throws CreateException {
		// TODO Auto-generated method stub
		
	}

	public void setSbbContext(SbbContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void unsetSbbContext() {
		// TODO Auto-generated method stub
		
	}
	// SBBLO ---------
	
	public void doSomeMagicInSBBLO(int i, String s)
	{}
	
	
	public void doSomeMoreMagicInSBBLO(int i, String s)
	{}

	public void doSomeMoreMagicInSuperInterface(int i, String s)
	{}
	
	// CMPS ----------
	public abstract String getSharedCMP();
	
	

	// ACI ------
	public abstract ACIConstraintsOk asSbbActivityContextInterface(ActivityContextInterface aci);
	
	
	// CHILD relations --------
	
	public abstract ChildRelation childRelationOne();
	public abstract ChildRelation childRelationTwo();
	
	// USAGE ------
	public abstract UsageOkInterface getDefaultSbbUsageParameterSet();

	public abstract UsageOkInterface getSbbUsageParameterSet(String x) throws UnrecognizedUsageParameterSetNameException;
	
}
