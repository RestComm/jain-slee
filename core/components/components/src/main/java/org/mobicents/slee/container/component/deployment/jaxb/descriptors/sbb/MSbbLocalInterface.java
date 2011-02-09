/**
 * Start time:12:17:40 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbLocalInterface;
import org.mobicents.slee.container.component.sbb.SbbLocalInterfaceDescriptor;

/**
 * Start time:12:17:40 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbLocalInterface implements SbbLocalInterfaceDescriptor {
	
	private boolean isolateSecurityPermissions=false;
	private String sbbLocalInterfaceName=null;
	
	public boolean isIsolateSecurityPermissions() {
		return isolateSecurityPermissions;
	}
	
	public String getSbbLocalInterfaceName() {
		return sbbLocalInterfaceName;
	}
	
	public MSbbLocalInterface(SbbLocalInterface sbbLocalInterface) {
		super();
		this.sbbLocalInterfaceName=sbbLocalInterface.getSbbLocalInterfaceName().getvalue();
		
	}
	public MSbbLocalInterface(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbLocalInterface llSbbLocalInterface) {
		super();
		this.sbbLocalInterfaceName=llSbbLocalInterface.getSbbLocalInterfaceName().getvalue();
		
		String v=llSbbLocalInterface.getIsolateSecurityPermissions();
		if(v!=null && Boolean.parseBoolean(v))
		{
			this.isolateSecurityPermissions=true;
		}
	}
	
	
	
	
}
