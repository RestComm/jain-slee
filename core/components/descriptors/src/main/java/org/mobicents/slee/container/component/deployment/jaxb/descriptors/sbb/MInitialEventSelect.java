/**
 * Start time:13:10:35 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.InitialEventSelect;

/**
 * Start time:13:10:35 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MInitialEventSelect {

	
	private InitialEventSelect initialEventSelect=null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.InitialEventSelect llInitialEventSelect=null;
	
	//Possibly this should be just a string....
	private String variable=null;

	
	
	
	public MInitialEventSelect(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.InitialEventSelect llInitialEventSelect) {
		super();
		this.llInitialEventSelect = llInitialEventSelect;
		this.variable=this.llInitialEventSelect.getVariable();
	}

	public MInitialEventSelect(InitialEventSelect initialEventSelect) {
		super();
		this.initialEventSelect = initialEventSelect;
		this.variable=this.initialEventSelect.getVariable();
	}

	public String getVariable() {
		return variable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((variable == null) ? 0 : variable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MInitialEventSelect other = (MInitialEventSelect) obj;
		if (variable == null) {
			if (other.variable != null)
				return false;
		} else if (!variable.equals(other.variable))
			return false;
		return true;
	}
	
	
	
}
