package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.DeploymentException;
import javax.slee.resource.ResourceAdaptorID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MResourceAdaptorTypeRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MConfigProperty;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptorClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptorClasses;

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
public class ResourceAdaptorDescriptorImpl {

	private ResourceAdaptorID resourceAdaptorID;
	private String description;

	private List<MLibraryRef> libraryRefs;
	private List<MProfileSpecRef> profileSpecRefs;
	private List<MResourceAdaptorTypeRef> resourceAdaptorTypeRefs;

	private List<MConfigProperty> configProperties;
	private boolean ignoreRaTypeEventTypeCheck;

	private MUsageParametersInterface resourceAdaptorUsageParametersInterface;
	private String resourceAdaptorClassName;

	private boolean supportsActiveReconfiguration;
	
  private MSecurityPermissions securityPermissions;

  private boolean isSlee11;

  private Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();
  
	public ResourceAdaptorDescriptorImpl( MResourceAdaptor resourceAdaptor, MSecurityPermissions securityPermissions, boolean isSlee11 ) throws DeploymentException
  {
    this.description = resourceAdaptor.getDescription();
    this.resourceAdaptorID = new ResourceAdaptorID(resourceAdaptor.getResourceAdaptorName(), resourceAdaptor.getResourceAdaptorVendor(), resourceAdaptor.getResourceAdaptorVersion());

    this.libraryRefs = resourceAdaptor.getLibraryRef();
    this.resourceAdaptorTypeRefs = resourceAdaptor.getResourceAdaptorTypeRefs();

    this.profileSpecRefs = resourceAdaptor.getProfileSpecRef();

    this.configProperties = resourceAdaptor.getConfigProperty();
    this.ignoreRaTypeEventTypeCheck = resourceAdaptor.getIgnoreRaTypeEventTypeCheck();

    MResourceAdaptorClasses raClasses = resourceAdaptor.getResourceAdaptorClasses();
    MResourceAdaptorClass raClass = raClasses.getResourceAdaptorClass();

    this.resourceAdaptorUsageParametersInterface = raClasses.getResourceAdaptorUsageParametersInterface();
    this.resourceAdaptorClassName = raClass.getResourceAdaptorClassName();

    this.supportsActiveReconfiguration = raClass.getSupportsActiveReconfiguration();

    this.securityPermissions = securityPermissions;
    
    this.isSlee11 = isSlee11;
    
    buildDependenciesSet();
  }
	

  private void buildDependenciesSet()
  {
    for(MResourceAdaptorTypeRef resourceAdaptorTypeRef : resourceAdaptorTypeRefs)
    {
      this.dependenciesSet.add( resourceAdaptorTypeRef.getComponentID() );
    }

    for(MProfileSpecRef profileSpecRef : profileSpecRefs)
    {
      this.dependenciesSet.add( profileSpecRef.getComponentID() );
    }

    for(MLibraryRef libraryRef : libraryRefs)
    {
      this.dependenciesSet.add( libraryRef.getComponentID() );
    }
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

	public boolean getIgnoreRaTypeEventTypeCheck() {
		return ignoreRaTypeEventTypeCheck;
	}

	public MUsageParametersInterface getResourceAdaptorUsageParametersInterface() {
		return resourceAdaptorUsageParametersInterface;
	}

	public String getResourceAdaptorClassName() {
		return resourceAdaptorClassName;
	}

	public boolean getSupportsActiveReconfiguration() {
		return supportsActiveReconfiguration;
	}

	public ResourceAdaptorID getResourceAdaptorID() {
		return resourceAdaptorID;
	}

	public Set<ComponentID> getDependenciesSet() {
		return this.dependenciesSet;
	}

  public MSecurityPermissions getSecurityPermissions()
  {
    return securityPermissions;
  }
  
  public boolean isSlee11()
  {
    return isSlee11;
  }
}
