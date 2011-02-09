package org.mobicents.slee.container.component.validator.sbb.abstracts.aci;

import javax.slee.ActivityContextInterface;

public interface ACIConstraintsWrongParameterType extends
		ActivityContextInterface {

	
	public void setSimple(SimpleSeralizable ss);
	public void setSimpleWrong(Object ss);
	
}
