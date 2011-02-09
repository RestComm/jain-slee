package org.mobicents.slee.container.component.validator.sbb.abstracts.localinterface;

import javax.slee.SbbLocalObject;

public interface SbbLocalInterfaceSlaveOne extends SbbLocalInterfaceMaster, SbbLocalInterfaceSlaveTwo {

	
	public void makeSomeStupidThing(String paramOne, int makeCounterParamTwo) throws NoSuchMethodException;
	
	
}
