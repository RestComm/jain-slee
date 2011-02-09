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
import org.mobicents.slee.container.component.sbb.ResourceAdaptorEntityBindingDescriptor;

/**
 * Start time:14:49:20 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MResourceAdaptorEntityBinding implements ResourceAdaptorEntityBindingDescriptor {

	private String description = null;
	private String resourceAdaptorObjectName = null;
	private String resourceAdaptorEntityLink = null;

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
		this.description = resourceAdaptorEntityBinding.getDescription() == null ? null
				: resourceAdaptorEntityBinding.getDescription().getvalue();
		this.resourceAdaptorObjectName = resourceAdaptorEntityBinding
				.getResourceAdaptorObjectName().getvalue();
		// Optional
		if (resourceAdaptorEntityBinding.getResourceAdaptorEntityLink() != null) {
			this.resourceAdaptorEntityLink = resourceAdaptorEntityBinding
					.getResourceAdaptorEntityLink().getvalue();
		}
	}

	public MResourceAdaptorEntityBinding(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorEntityBinding llResourceAdaptorEntityBinding) {
		super();
		this.description = llResourceAdaptorEntityBinding.getDescription() == null ? null
				: llResourceAdaptorEntityBinding.getDescription().getvalue();
		this.resourceAdaptorObjectName = llResourceAdaptorEntityBinding
				.getResourceAdaptorObjectName().getvalue();
		// Optional
		if (llResourceAdaptorEntityBinding.getResourceAdaptorEntityLink() != null) {
			this.resourceAdaptorEntityLink = llResourceAdaptorEntityBinding
					.getResourceAdaptorEntityLink().getvalue();
		}
	}

}
