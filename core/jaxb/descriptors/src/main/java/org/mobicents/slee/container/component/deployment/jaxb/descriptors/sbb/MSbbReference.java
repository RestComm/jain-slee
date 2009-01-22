/**
 * Start time:10:25:19 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbRef;


/**
 * Start time:10:25:19 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbReference {

	
	private org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbRef sbbRef=null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbRef llSbbRef=null;
	
	private String description;
	private String sbbAlias=null;
	private ComponentKey referenceKey=null;
	public String getDescription() {
		return description;
	}
	public String getSbbAlias() {
		return sbbAlias;
	}
	public ComponentKey getReferenceKey() {
		return referenceKey;
	}
	public MSbbReference(SbbRef llSbbRef) {
		super();
		this.llSbbRef = llSbbRef;
		this.description=this.llSbbRef.getDescription()==null?null:this.llSbbRef.getDescription().getvalue();
		this.referenceKey=new ComponentKey(this.llSbbRef.getSbbName().getvalue(),this.llSbbRef.getSbbVendor().getvalue(),this.llSbbRef.getSbbVersion().getvalue());
		this.sbbAlias=this.llSbbRef.getSbbAlias().getvalue();
		
	}
	public MSbbReference(
			org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbRef sbbRef) {
		super();
		this.sbbRef = sbbRef;
		this.description=this.sbbRef.getDescription()==null?null:this.sbbRef.getDescription().getvalue();
		this.referenceKey=new ComponentKey(this.sbbRef.getSbbName().getvalue(),this.sbbRef.getSbbVendor().getvalue(),this.sbbRef.getSbbVersion().getvalue());
		this.sbbAlias=this.sbbRef.getSbbAlias().getvalue();
	}
	
	
	
	

}
