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
