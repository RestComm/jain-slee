/**
 * Start time:16:00:31 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

import javassist.CtClass;

import javax.slee.SbbID;
import javax.slee.management.DeployableUnitID;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;


import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;


/**
 * Start time:16:00:31 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbComponent {

	
	protected static final transient Logger logger=Logger.getLogger(SbbComponent.class.getName());
	
	protected SbbDescriptorImpl descriptor=null;
	protected SbbID sbbID=null;
	protected DeployableUnitID deployableUnitID=null;
	
	// The concrete SBB class
	private transient Class abstractSbbClass;
	
	// The concrete SBB class
	private transient Class concreteSbbClass;
	
	
	// Ptr to sbb local interface class.
	private transient Class sbbLocalInterfaceClass;

	private transient Class sbbLocalInterfaceConcreteClass;
	
	private transient Class activityContextInterface;

	// Ptr to activity context interface concrete class.
	private transient Class activityContextInterfaceConcreteClass;
	
	private transient Class usageParametersInterface;
	


	public SbbComponent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SbbComponent(String tets) {
		super();
		// TODO Auto-generated constructor stub
	}
	public SbbDescriptorImpl getDescriptor() {
		return descriptor;
	}


	public SbbID getSbbID() {
		return sbbID;
	}


	public DeployableUnitID getDeployableUnitID() {
		return deployableUnitID;
	}


	public Class getAbstractSbbClass() {
		return abstractSbbClass;
	}


	public Class getConcreteSbbClass() {
		return concreteSbbClass;
	}

	/**
	 * This must never return null, if no custom interface is defined, this has to return generic javax.slee.SbbLocalObject 
	 * @return
	 */
	public Class getSbbLocalInterfaceClass() {
		return sbbLocalInterfaceClass;
	}


	public Class getSbbLocalInterfaceConcreteClass() {
		return sbbLocalInterfaceConcreteClass;
	}


	public Class getActivityContextInterface() {
		return activityContextInterface;
	}


	public Class getActivityContextInterfaceConcreteClass() {
		return activityContextInterfaceConcreteClass;
	}


	public void setDescriptor(SbbDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}


	public void setDeployableUnitID(DeployableUnitID deployableUnitID) {
		this.deployableUnitID = deployableUnitID;
	}


	public void setAbstractSbbClass(Class abstractSbbClass) {
		this.abstractSbbClass = abstractSbbClass;
	}


	public void setConcreteSbbClass(Class concreteSbbClass) {
		this.concreteSbbClass = concreteSbbClass;
	}


	public void setSbbLocalInterfaceClass(Class sbbLocalInterfaceClass) {
		this.sbbLocalInterfaceClass = sbbLocalInterfaceClass;
	}


	public void setSbbLocalInterfaceConcreteClass(
			Class sbbLocalInterfaceConcreteClass) {
		this.sbbLocalInterfaceConcreteClass = sbbLocalInterfaceConcreteClass;
	}


	public void setActivityContextInterface(Class activityContextInterface) {
		this.activityContextInterface = activityContextInterface;
	}


	public void setActivityContextInterfaceConcreteClass(
			Class activityContextInterfaceConcreteClass) {
		this.activityContextInterfaceConcreteClass = activityContextInterfaceConcreteClass;
	}

	public boolean isSlee11() {
		return this.descriptor.isSlee11();
	}

	
	
	public Map<String, SbbID> getSbbReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getUsageParametersInterface() {
		return usageParametersInterface;
	}

	public void setUsageParametersInterface(Class usageParametersInterface) {
		this.usageParametersInterface = usageParametersInterface;
	}

	public Map<String, ProfileSpecificationID> getProfileReferences() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
