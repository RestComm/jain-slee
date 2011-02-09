/**
 * Start time:14:43:52 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import java.util.ArrayList;
import java.util.List;

import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ResourceAdaptorEntityBinding;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ResourceAdaptorTypeBinding;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorEntityBindingDescriptor;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorTypeBindingDescriptor;

/**
 * Start time:14:43:52 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MResourceAdaptorTypeBinding implements ResourceAdaptorTypeBindingDescriptor {

	private String description = null;
	private ResourceAdaptorTypeID resourceAdaptorTypeRef = null;
	private String activityContextInterfaceFactoryName = null;
	private List<ResourceAdaptorEntityBindingDescriptor> resourceAdaptorEntityBinding = null;

	public String getDescription() {
		return description;
	}

	public ResourceAdaptorTypeID getResourceAdaptorTypeRef() {
		return resourceAdaptorTypeRef;
	}

	public String getActivityContextInterfaceFactoryName() {
		return activityContextInterfaceFactoryName;
	}

	public List<ResourceAdaptorEntityBindingDescriptor> getResourceAdaptorEntityBinding() {
		return resourceAdaptorEntityBinding;
	}

	public MResourceAdaptorTypeBinding(
			ResourceAdaptorTypeBinding resourceAdaptorTypeBinding) {
		super();
		this.description = resourceAdaptorTypeBinding.getDescription() == null ? null
				: resourceAdaptorTypeBinding.getDescription().getvalue();
		this.resourceAdaptorTypeRef = new ResourceAdaptorTypeID(
				resourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeName().getvalue(),
				resourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVendor().getvalue(),
				resourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVersion().getvalue());
		
		//Optional
		if(resourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName()!=null)
		{
			this.activityContextInterfaceFactoryName=resourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName().getvalue();
		}
		
		this.resourceAdaptorEntityBinding=new ArrayList<ResourceAdaptorEntityBindingDescriptor>();
		//Zero+
		if(resourceAdaptorTypeBinding.getResourceAdaptorEntityBinding()!=null)
		{
			for(ResourceAdaptorEntityBinding raeb:resourceAdaptorTypeBinding.getResourceAdaptorEntityBinding())
			{
				this.resourceAdaptorEntityBinding.add(new MResourceAdaptorEntityBinding(raeb));
			}
		}
		
	}

	public MResourceAdaptorTypeBinding(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorTypeBinding llResourceAdaptorTypeBinding) {
		super();		
		this.description = llResourceAdaptorTypeBinding.getDescription() == null ? null
				: llResourceAdaptorTypeBinding.getDescription().getvalue();
		this.resourceAdaptorTypeRef = new ResourceAdaptorTypeID(
				llResourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeName().getvalue(),
				llResourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVendor().getvalue(),
				llResourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVersion().getvalue());
		
		//Optional
		if(llResourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName()!=null)
		{
			this.activityContextInterfaceFactoryName=llResourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName().getvalue();
		}
		
		this.resourceAdaptorEntityBinding=new ArrayList<ResourceAdaptorEntityBindingDescriptor>();
		//Zero+
		if(llResourceAdaptorTypeBinding.getResourceAdaptorEntityBinding()!=null)
		{
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorEntityBinding raeb:llResourceAdaptorTypeBinding.getResourceAdaptorEntityBinding())
			{
				this.resourceAdaptorEntityBinding.add(new MResourceAdaptorEntityBinding(raeb));
			}
		}
	}

}
