/**
 * Start time:14:49:20 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ResourceAdaptorEntityBinding;

/**
 * Start time:14:49:20 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MResourceAdaptorEntityBinding {

	
	private ResourceAdaptorEntityBinding resourceAdaptorEntityBinding=null; 
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorEntityBinding llResourceAdaptorEntityBinding=null; 
	
	private String description=null;
	private String resourceAdaptorObjectName=null;
	private String resourceAdaptorEntityLink=null;
	public String getDescription() {
		return description;
	}
	public String getResourceAdaptorObjectName() {
		return resourceAdaptorObjectName;
	}
	public String getResourceAdaptorEntityLink() {
		return resourceAdaptorEntityLink;
	}
	public MResourceAdaptorEntityBinding(
			ResourceAdaptorEntityBinding resourceAdaptorEntityBinding) {
		super();
		this.resourceAdaptorEntityBinding = resourceAdaptorEntityBinding;
		this.description=this.resourceAdaptorEntityBinding.getDescription()==null?null:this.resourceAdaptorEntityBinding.getDescription().getvalue();
		this.resourceAdaptorObjectName=this.resourceAdaptorEntityBinding.getResourceAdaptorObjectName().getvalue();
		//Optional
		if(this.resourceAdaptorEntityBinding.getResourceAdaptorEntityLink()!=null)
		{
			this.resourceAdaptorEntityLink=this.resourceAdaptorEntityBinding.getResourceAdaptorEntityLink().getvalue();
		}
		
	}
	public MResourceAdaptorEntityBinding(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorEntityBinding llResourceAdaptorEntityBinding) {
		super();
		this.llResourceAdaptorEntityBinding = llResourceAdaptorEntityBinding;
		
		this.description=this.llResourceAdaptorEntityBinding.getDescription()==null?null:this.llResourceAdaptorEntityBinding.getDescription().getvalue();
		this.resourceAdaptorObjectName=this.llResourceAdaptorEntityBinding.getResourceAdaptorObjectName().getvalue();
		//Optional
		if(this.llResourceAdaptorEntityBinding.getResourceAdaptorEntityLink()!=null)
		{
			this.resourceAdaptorEntityLink=this.llResourceAdaptorEntityBinding.getResourceAdaptorEntityLink().getvalue();
		}
		
	}
	
	
	
}
