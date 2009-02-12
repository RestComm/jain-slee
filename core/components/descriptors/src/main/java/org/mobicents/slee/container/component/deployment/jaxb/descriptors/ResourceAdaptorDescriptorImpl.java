package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.resource.ResourceAdaptorID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MConfigProperty;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptorClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptorClasses;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptorJar;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptorTypeRef;
import org.w3c.dom.Document;

/**
 * 
 * ResourceAdaptorDescriptorImpl.java
 * 
 * <br>
 * Project: mobicents <br>
 * 4:55:40 PM Jan 29, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ResourceAdaptorDescriptorImpl extends JAXBBaseUtilityClass {

	private MResourceAdaptorJar resourceAdaptorJar;
	private int index;

	private MResourceAdaptor resourceAdaptor;

	private ResourceAdaptorID resourceAdaptorID;
	private String description;

	private List<MLibraryRef> libraryRefs;
	private List<MProfileSpecRef> profileSpecRefs;
	private List<MResourceAdaptorTypeRef> resourceAdaptorTypeRefs;

	private List<MConfigProperty> configProperties;
	private Boolean ignoreRaTypeEventTypeCheck;

	private MUsageParametersInterface resourceAdaptorUsageParametersInterface;
	private String resourceAdaptorClassName;

	private Boolean supportsActiveReconfiguration;

	public ResourceAdaptorDescriptorImpl(Document doc) {
		super(doc);
	}

	public ResourceAdaptorDescriptorImpl(
			Document doc,
			org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptorJar resourceAdaptorJar10,
			int index) {
		super(doc);

		this.resourceAdaptorJar = new MResourceAdaptorJar(resourceAdaptorJar10);
		this.index = index;
	}

	public ResourceAdaptorDescriptorImpl(
			Document doc,
			org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorJar resourceAdaptorJar11,
			int index) {
		super(doc);

		this.resourceAdaptorJar = new MResourceAdaptorJar(resourceAdaptorJar11);
		this.index = index;
	}

	@Override
	public void buildDescriptionMap() {
		this.resourceAdaptor = this.resourceAdaptorJar.getResourceAdaptor()
				.get(index);
		this.description = this.resourceAdaptor.getDescription();
		this.resourceAdaptorID = new ResourceAdaptorID(resourceAdaptor
				.getResourceAdaptorName(), resourceAdaptor
				.getResourceAdaptorVendor(), resourceAdaptor
				.getResourceAdaptorVersion());

		this.libraryRefs = this.resourceAdaptor.getLibraryRef();
		this.resourceAdaptorTypeRefs = this.resourceAdaptor
				.getResourceAdaptorTypeRefs();

		this.profileSpecRefs = this.resourceAdaptor.getProfileSpecRef();

		this.configProperties = this.resourceAdaptor.getConfigProperty();
		this.ignoreRaTypeEventTypeCheck = this.resourceAdaptor
				.getIgnoreRaTypeEventTypeCheck();

		MResourceAdaptorClasses raClasses = this.resourceAdaptor
				.getResourceAdaptorClasses();
		MResourceAdaptorClass raClass = raClasses.getResourceAdaptorClass();

		this.resourceAdaptorUsageParametersInterface = raClasses
				.getResourceAdaptorUsageParametersInterface();
		this.resourceAdaptorClassName = raClass.getResourceAdaptorClassName();

		this.supportsActiveReconfiguration = raClass
				.getSupportsActiveReconfiguration();
	}

	@Override
	public Object getJAXBDescriptor() {
		return this.resourceAdaptorJar;
	}

	public MResourceAdaptor getResourceAdaptor() {
		return resourceAdaptor;
	}

	public String getDescription() {
		return description;
	}

	public List<MLibraryRef> getLibraryRefs() {
		return libraryRefs;
	}

	public List<MResourceAdaptorTypeRef> getResourceAdaptorTypeRefs() {
		return resourceAdaptorTypeRefs;
	}

	public List<MProfileSpecRef> getProfileSpecRefs() {
		return profileSpecRefs;
	}

	public List<MConfigProperty> getConfigProperties() {
		return configProperties;
	}

	public Boolean getIgnoreRaTypeEventTypeCheck() {
		return ignoreRaTypeEventTypeCheck;
	}

	public MUsageParametersInterface getResourceAdaptorUsageParametersInterface() {
		return resourceAdaptorUsageParametersInterface;
	}

	public String getResourceAdaptorClassName() {
		return resourceAdaptorClassName;
	}

	public Boolean getSupportsActiveReconfiguration() {
		return supportsActiveReconfiguration;
	}

	public ResourceAdaptorID getResourceAdaptorID() {
		return resourceAdaptorID;
	}

	public Set<ComponentID> getDependenciesSet() {
		// TODO Auto-generated method stub
		return null;
	}

}
