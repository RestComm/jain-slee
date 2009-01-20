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

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ResourceAdaptorEntityBinding;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ResourceAdaptorTypeBinding;

/**
 * Start time:14:43:52 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MResourceAdaptorTypeBidning {

	private ResourceAdaptorTypeBinding resourceAdaptorTypeBinding = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorTypeBinding llResourceAdaptorTypeBinding = null;

	private String description = null;
	private ComponentKey raTypeReferenceKey = null;
	private String activityContextInterfaceFactoryName = null;
	private ArrayList<MResourceAdaptorEntityBinding> resourceAdaptorEntityBinding = null;

	public String getDescription() {
		return description;
	}

	public ComponentKey getRaTypeReferenceKey() {
		return raTypeReferenceKey;
	}

	public String getActivityContextInterfaceFactoryName() {
		return activityContextInterfaceFactoryName;
	}

	public ArrayList<MResourceAdaptorEntityBinding> getResourceAdaptorEntityBinding() {
		return resourceAdaptorEntityBinding;
	}

	public MResourceAdaptorTypeBidning(
			ResourceAdaptorTypeBinding resourceAdaptorTypeBinding) {
		super();
		this.resourceAdaptorTypeBinding = resourceAdaptorTypeBinding;
		this.description = this.resourceAdaptorTypeBinding.getDescription() == null ? null
				: this.resourceAdaptorTypeBinding.getDescription().getvalue();
		this.raTypeReferenceKey = new ComponentKey(
				this.resourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeName().getvalue(),
				this.resourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVendor().getvalue(),
				this.resourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVersion().getvalue());
		
		//Optional
		if(this.resourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName()!=null)
		{
			this.activityContextInterfaceFactoryName=this.resourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName().getvalue();
		}
		
		this.resourceAdaptorEntityBinding=new ArrayList<MResourceAdaptorEntityBinding>();
		//Zero+
		if(this.resourceAdaptorTypeBinding.getResourceAdaptorEntityBinding()!=null)
		{
			for(ResourceAdaptorEntityBinding raeb:this.resourceAdaptorTypeBinding.getResourceAdaptorEntityBinding())
			{
				this.resourceAdaptorEntityBinding.add(new MResourceAdaptorEntityBinding(raeb));
			}
		}
		
	}

	public MResourceAdaptorTypeBidning(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorTypeBinding llResourceAdaptorTypeBinding) {
		super();
		this.llResourceAdaptorTypeBinding = llResourceAdaptorTypeBinding;
		
		this.description = this.llResourceAdaptorTypeBinding.getDescription() == null ? null
				: this.llResourceAdaptorTypeBinding.getDescription().getvalue();
		this.raTypeReferenceKey = new ComponentKey(
				this.llResourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeName().getvalue(),
				this.llResourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVendor().getvalue(),
				this.llResourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVersion().getvalue());
		
		//Optional
		if(this.llResourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName()!=null)
		{
			this.activityContextInterfaceFactoryName=this.llResourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName().getvalue();
		}
		
		this.resourceAdaptorEntityBinding=new ArrayList<MResourceAdaptorEntityBinding>();
		//Zero+
		if(this.llResourceAdaptorTypeBinding.getResourceAdaptorEntityBinding()!=null)
		{
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorEntityBinding raeb:this.llResourceAdaptorTypeBinding.getResourceAdaptorEntityBinding())
			{
				this.resourceAdaptorEntityBinding.add(new MResourceAdaptorEntityBinding(raeb));
			}
		}
	}

}
