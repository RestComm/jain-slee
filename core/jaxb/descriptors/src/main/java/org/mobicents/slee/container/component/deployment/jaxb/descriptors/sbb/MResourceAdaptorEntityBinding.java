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
	private String objectName=null;
	private String entityLink=null;
	public String getDescription() {
		return description;
	}
	public String getObjectName() {
		return objectName;
	}
	public String getEntityLink() {
		return entityLink;
	}
	public MResourceAdaptorEntityBinding(
			ResourceAdaptorEntityBinding resourceAdaptorEntityBinding) {
		super();
		this.resourceAdaptorEntityBinding = resourceAdaptorEntityBinding;
		this.description=this.resourceAdaptorEntityBinding.getDescription()==null?null:this.resourceAdaptorEntityBinding.getDescription().getvalue();
		this.objectName=this.resourceAdaptorEntityBinding.getResourceAdaptorObjectName().getvalue();
		//Optional
		if(this.resourceAdaptorEntityBinding.getResourceAdaptorEntityLink()!=null)
		{
			this.entityLink=this.resourceAdaptorEntityBinding.getResourceAdaptorEntityLink().getvalue();
		}
		
	}
	public MResourceAdaptorEntityBinding(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorEntityBinding llResourceAdaptorEntityBinding) {
		super();
		this.llResourceAdaptorEntityBinding = llResourceAdaptorEntityBinding;
		
		this.description=this.llResourceAdaptorEntityBinding.getDescription()==null?null:this.llResourceAdaptorEntityBinding.getDescription().getvalue();
		this.objectName=this.llResourceAdaptorEntityBinding.getResourceAdaptorObjectName().getvalue();
		//Optional
		if(this.llResourceAdaptorEntityBinding.getResourceAdaptorEntityLink()!=null)
		{
			this.entityLink=this.llResourceAdaptorEntityBinding.getResourceAdaptorEntityLink().getvalue();
		}
		
	}
	
	
	
}
