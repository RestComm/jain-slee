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

/**
 * Start time:12:17:40 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbLocalInterface {

	private SbbLocalInterface sbbLocalInterface=null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbLocalInterface llSbbLocalInterface=null;
	
	private boolean isolateSecurityPermissions=false;
	private String description=null;
	private String sbbLocalInterfaceName=null;
	public boolean isIsolateSecurityPermissions() {
		return isolateSecurityPermissions;
	}
	public String getDescription() {
		return description;
	}
	public String getSbbLocalInterfaceName() {
		return sbbLocalInterfaceName;
	}
	public MSbbLocalInterface(SbbLocalInterface sbbLocalInterface) {
		super();
		this.sbbLocalInterface = sbbLocalInterface;
		this.description=this.sbbLocalInterface.getDescription()==null?null:this.sbbLocalInterface.getDescription().getvalue();
		this.sbbLocalInterfaceName=this.sbbLocalInterface.getSbbLocalInterfaceName().getvalue();
		
	}
	public MSbbLocalInterface(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbLocalInterface llSbbLocalInterface) {
		super();
		this.llSbbLocalInterface = llSbbLocalInterface;
		this.description=this.llSbbLocalInterface.getDescription()==null?null:this.llSbbLocalInterface.getDescription().getvalue();
		this.sbbLocalInterfaceName=this.llSbbLocalInterface.getSbbLocalInterfaceName().getvalue();
		
		String v=this.llSbbLocalInterface.getIsolateSecurityPermissions();
		if(v!=null && Boolean.parseBoolean(v))
		{
			this.isolateSecurityPermissions=true;
		}
	}
	
	
	
	
}
