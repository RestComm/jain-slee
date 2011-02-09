/**
 * Start time:12:28:23 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbActivityContextInterface;

/**
 * Start time:12:28:23 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbActivityContextInterface {

	private String interfaceName = null;

	public String getInterfaceName() {
		return interfaceName;
	}

	public MSbbActivityContextInterface(
			SbbActivityContextInterface sbbActivityContextInterface) {
		super();
		this.interfaceName = sbbActivityContextInterface
				.getSbbActivityContextInterfaceName().getvalue();
	}

	public MSbbActivityContextInterface(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbActivityContextInterface llSbbActivityContextInterface) {
		super();
		this.interfaceName = llSbbActivityContextInterface
				.getSbbActivityContextInterfaceName().getvalue();
	}
	
	
	
	
}
