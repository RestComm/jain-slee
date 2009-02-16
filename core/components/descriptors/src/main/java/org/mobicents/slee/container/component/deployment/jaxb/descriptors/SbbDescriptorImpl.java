package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MEnvEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MEjbRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MSbbRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MActivityContextAttributeAlias;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MResourceAdaptorTypeBinding;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbb;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbAbstractClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbActivityContextInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbClasses;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbLocalInterface;

/**
 * Start time:16:54:43 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a> 
 */
public class SbbDescriptorImpl {

  private String description;

  private SbbID sbbID;
  private String sbbAlias;

  private List<MSbbRef> sbbRefs;
  private List<MProfileSpecRef> profileSpecRefs;

  private MSbbClasses sbbClasses;

  // might be bad, we ommit sbb-classes/description, phew
  private String addressProfileSpecAliasRef;
  private List<MEventEntry> events;
  private List<MActivityContextAttributeAlias> activityContextAttributeAliases;
  private List<MEnvEntry> envEntries;
  private List<MResourceAdaptorTypeBinding> resourceAdaptorTypeBindings;

  // 1.1 stuff, profile specs refs have alias element, so we need another.
  private List<MLibraryRef> libraryRefs;
  private List<MEjbRef> ejbRefs;

  private MSecurityPermissions securityPermisions;

  private boolean isSlee11;
  
  private Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();

  public SbbDescriptorImpl(MSbb sbb, MSecurityPermissions sbbJarSecurityPermissions, boolean isSlee11) throws DeploymentException
  {
    try
    {
      this.description = sbb.getDescription();
      this.sbbID = new SbbID(sbb.getSbbName(), sbb.getSbbVendor(), sbb.getSbbVersion());

      this.sbbAlias = sbb.getSbbAlias();

      this.libraryRefs = sbb.getLibraryRef();
      this.ejbRefs = sbb.getEjbRef();
      this.profileSpecRefs = sbb.getProfileSpecRef();
      this.sbbRefs = sbb.getSbbRef();

      this.sbbClasses = sbb.getSbbClasses();

      this.addressProfileSpecAliasRef = sbb.getAddressProfileSpecAliasRef();

      this.events = sbb.getEvent();

      this.activityContextAttributeAliases = sbb.getActivityContextAttributeAlias();
      this.envEntries = sbb.getEnvEntry();

      this.resourceAdaptorTypeBindings = sbb.getResourceAdaptorTypeBinding();

      this.securityPermisions = sbbJarSecurityPermissions;

      this.isSlee11 = isSlee11;
      
      buildDependenciesSet();
    }
    catch (Exception e) {
      throw new DeploymentException("Failed to build sbb descriptor", e);
    }
  }

  private void buildDependenciesSet()
  {
    for(MSbbRef sbbRef : sbbRefs)
    {
      this.dependenciesSet.add( sbbRef.getComponentID() );
    }

    for(MProfileSpecRef profileSpecRef : profileSpecRefs)
    {
      this.dependenciesSet.add( profileSpecRef.getComponentID() );
    }

    for(MLibraryRef libraryRef : libraryRefs)
    {
      this.dependenciesSet.add( libraryRef.getComponentID() );
    }

    // FIXME: EJB's do not have component ID... what gives?
    // for(MEjbRef ejbRef : ejbRefs)
    // {
    //   this.dependenciesSet.add( ejbRef.getComponentID() );
    // }
  }


  public String getDescription() {
    return description;
  }

  public SbbID getSbbID() {
    return sbbID;
  }

  public String getSbbAlias() {
    return sbbAlias;
  }

  public List<MSbbRef> getSbbRefs() {
    return sbbRefs;
  }

  public List<MProfileSpecRef> getProfileSpecReference() {
    return profileSpecRefs;
  }

  public List<MProfileSpecRef> getProfileSpecRefs()
  {
    return profileSpecRefs;
  }

  public MSbbClasses getSbbClasses()
  {
    return sbbClasses;
  }

  public String getAddressProfileSpecAliasRef() {
    return addressProfileSpecAliasRef;
  }

  public List<MEventEntry> getEvents() {
    return events;
  }

  public List<MActivityContextAttributeAlias> getActivityContextAttributeAliases() {
    return activityContextAttributeAliases;
  }

  public List<MEnvEntry> getEnvEntries() {
    return envEntries;
  }

  public List<MResourceAdaptorTypeBinding> getResourceAdaptorTypeBindings() {
    return resourceAdaptorTypeBindings;
  }

  public List<MLibraryRef> getLibraryRefs() {
    return libraryRefs;
  }

  public List<MEjbRef> getEjbRefs() {
    return ejbRefs;
  }

  public MSecurityPermissions getSecurityPermisions() {
    return securityPermisions;
  }

  public Set<ComponentID> getDependenciesSet() {
    return this.dependenciesSet;
  }

  public boolean isSlee11()
  {
    return isSlee11;
  }

  // Convenience methods
  public MSbbAbstractClass getSbbAbstractClass()
  {
    return this.sbbClasses.getSbbAbstractClass();
  }

  public MSbbLocalInterface getSbbLocalInterface()
  {
    return this.sbbClasses.getSbbLocalInterface();
  }

  public MSbbActivityContextInterface getSbbActivityContextInterface()
  {
    return this.sbbClasses.getSbbActivityContextInterface();
  }
}
