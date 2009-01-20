/**
 * Start time:15:15:53 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EjbRef;

/**
 * Start time:15:15:53 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEjbRef {

	private EjbRef ejbRef=null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EjbRef llEjbRef=null;
	
	
	private String description=null;
	private String ejbRefName=null;
	private String ejbRefType=null;
	private String home=null;
	private String remote=null;
	private String ejbLink=null;
	public String getDescription() {
		return description;
	}
	public String getEjbRefName() {
		return ejbRefName;
	}
	public String getEjbRefType() {
		return ejbRefType;
	}
	public String getHome() {
		return home;
	}
	public String getRemote() {
		return remote;
	}
	public String getEjbLink() {
		return ejbLink;
	}
	public MEjbRef(EjbRef ejbRef) {
		super();
		this.ejbRef = ejbRef;
		
		this.description = this.ejbRef.getDescription() == null ? null
				: this.ejbRef.getDescription().getvalue();
		this.ejbRefName=this.ejbRef.getEjbRefName().getvalue();
		this.ejbRefType=this.ejbRef.getEjbRefType().getvalue();
		this.home=this.ejbRef.getHome().getvalue();
		this.remote=this.ejbRef.getRemote().getvalue();
		//Optional, removed in 1.1
		if(this.ejbRef.getEjbLink()!=null)
		{
			this.ejbLink=this.ejbRef.getEjbLink().getvalue();
		}
	}
	public MEjbRef(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EjbRef llEjbRef) {
		super();
		this.llEjbRef = llEjbRef;
		
		this.description = this.llEjbRef.getDescription() == null ? null
				: this.llEjbRef.getDescription().getvalue();
		this.ejbRefName=this.llEjbRef.getEjbRefName().getvalue();
		this.ejbRefType=this.llEjbRef.getEjbRefType().getvalue();
		this.home=this.llEjbRef.getHome().getvalue();
		this.remote=this.llEjbRef.getRemote().getvalue();
		
	}
	
	
	
}
