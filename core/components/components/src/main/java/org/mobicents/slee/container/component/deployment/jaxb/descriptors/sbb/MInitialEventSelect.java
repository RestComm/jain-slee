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

	private InitialEventSelectVariable variable;
	
	public MInitialEventSelect(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.InitialEventSelect llInitialEventSelect) {
		this.variable=InitialEventSelectVariable.valueOf(llInitialEventSelect.getVariable());
	}

	public MInitialEventSelect(InitialEventSelect initialEventSelect) {
		this.variable=InitialEventSelectVariable.valueOf(initialEventSelect.getVariable());
	}

	public InitialEventSelectVariable getVariable() {
		return variable;
	}

	@Override
	public int hashCode() {
		return variable.hashCode();
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
