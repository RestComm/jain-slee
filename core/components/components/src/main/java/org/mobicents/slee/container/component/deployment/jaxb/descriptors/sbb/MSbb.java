package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MEnvEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MEjbRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MSbbRef;
import org.mobicents.slee.container.component.sbb.EjbRefDescriptor;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorTypeBindingDescriptor;
import org.mobicents.slee.container.component.sbb.SbbRefDescriptor;

/**
 * 
 * MSbb.java
 *
 * <br>Project:  mobicents
 * <br>3:02:57 PM Feb 16, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a>
 */
public class MSbb {

  private String description;

  private String sbbName;
  private String sbbVendor;
  private String sbbVersion;

  private String sbbAlias;

  private List<MLibraryRef> libraryRef = new ArrayList<MLibraryRef>();
  private List<SbbRefDescriptor> sbbRef = new ArrayList<SbbRefDescriptor>();
  private List<ProfileSpecRefDescriptor> profileSpecRef = new ArrayList<ProfileSpecRefDescriptor>();
  private MSbbClasses sbbClasses;

  private String addressProfileSpecAliasRef;

  private List<MEventEntry> event = new ArrayList<MEventEntry>();

  private List<MActivityContextAttributeAlias> activityContextAttributeAlias = new ArrayList<MActivityContextAttributeAlias>();
  private List<EnvEntryDescriptor> envEntry = new ArrayList<EnvEntryDescriptor>();

  private List<ResourceAdaptorTypeBindingDescriptor> resourceAdaptorTypeBinding = new ArrayList<ResourceAdaptorTypeBindingDescriptor>();

  private List<EjbRefDescriptor> ejbRef = new ArrayList<EjbRefDescriptor>();

  public MSbb(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.Sbb sbb10)
  {
    this.description = sbb10.getDescription() == null ? null : sbb10.getDescription().getvalue();

    this.sbbName = sbb10.getSbbName().getvalue();
    this.sbbVendor = sbb10.getSbbVendor().getvalue();
    this.sbbVersion = sbb10.getSbbVersion().getvalue();

    this.sbbAlias = sbb10.getSbbAlias() == null ? null : sbb10.getSbbAlias().getvalue();

    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbRef sbbRef10 : sbb10.getSbbRef())
    {
      this.sbbRef.add( new MSbbRef(sbbRef10) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ProfileSpecRef profileSpecRef10 : sbb10.getProfileSpecRef())
    {
      this.profileSpecRef.add( new MProfileSpecRef(profileSpecRef10) );
    }

    this.sbbClasses = new MSbbClasses(sbb10.getSbbClasses());

    this.addressProfileSpecAliasRef = sbb10.getAddressProfileSpecAliasRef() == null ? null : sbb10.getAddressProfileSpecAliasRef().getvalue();

    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.Event event10 : sbb10.getEvent())
    {
      this.event.add( new MEventEntry(event10) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ActivityContextAttributeAlias activityContextAttributeAlias10 : sbb10.getActivityContextAttributeAlias())
    {
      this.activityContextAttributeAlias.add( new MActivityContextAttributeAlias(activityContextAttributeAlias10) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EnvEntry envEntry10 : sbb10.getEnvEntry())
    {
      this.envEntry.add( new MEnvEntry(envEntry10) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ResourceAdaptorTypeBinding resourceAdaptorTypeBinding10 : sbb10.getResourceAdaptorTypeBinding())
    {
      this.resourceAdaptorTypeBinding.add( new MResourceAdaptorTypeBinding(resourceAdaptorTypeBinding10) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EjbRef ejbRef10 : sbb10.getEjbRef())
    {
      this.ejbRef.add( new MEjbRef(ejbRef10) );
    }
  }

  public MSbb(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.Sbb sbb11)
  {
    this.description = sbb11.getDescription() == null ? null : sbb11.getDescription().getvalue();

    this.sbbName = sbb11.getSbbName().getvalue();
    this.sbbVendor = sbb11.getSbbVendor().getvalue();
    this.sbbVersion = sbb11.getSbbVersion().getvalue();

    this.sbbAlias = sbb11.getSbbAlias() == null ? null : sbb11.getSbbAlias().getvalue();

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.LibraryRef libraryRef11 : sbb11.getLibraryRef())
    {
      this.libraryRef.add( new MLibraryRef(libraryRef11) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbRef sbbRef11 : sbb11.getSbbRef())
    {
      this.sbbRef.add( new MSbbRef(sbbRef11) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ProfileSpecRef profileSpecRef11 : sbb11.getProfileSpecRef())
    {
      this.profileSpecRef.add( new MProfileSpecRef(profileSpecRef11) );
    }

    this.sbbClasses = new MSbbClasses(sbb11.getSbbClasses());

    this.addressProfileSpecAliasRef = sbb11.getAddressProfileSpecAliasRef() == null ? null : sbb11.getAddressProfileSpecAliasRef().getvalue();

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.Event event11 : sbb11.getEvent())
    {
      this.event.add( new MEventEntry(event11) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ActivityContextAttributeAlias activityContextAttributeAlias11 : sbb11.getActivityContextAttributeAlias())
    {
      this.activityContextAttributeAlias.add( new MActivityContextAttributeAlias(activityContextAttributeAlias11) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EnvEntry envEntry11 : sbb11.getEnvEntry())
    {
      this.envEntry.add( new MEnvEntry(envEntry11) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorTypeBinding resourceAdaptorTypeBinding11 : sbb11.getResourceAdaptorTypeBinding())
    {
      this.resourceAdaptorTypeBinding.add( new MResourceAdaptorTypeBinding(resourceAdaptorTypeBinding11) );
    }

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EjbRef ejbRef11 : sbb11.getEjbRef())
    {
      this.ejbRef.add( new MEjbRef(ejbRef11) );
    }
  }

  public String getDescription()
  {
    return description;
  }

  public String getSbbName()
  {
    return sbbName;
  }

  public String getSbbVendor()
  {
    return sbbVendor;
  }

  public String getSbbVersion()
  {
    return sbbVersion;
  }

  public String getSbbAlias()
  {
    return sbbAlias;
  }
  
  public List<MLibraryRef> getLibraryRef()
  {
    return libraryRef;
  }

  public List<SbbRefDescriptor> getSbbRef()
  {
    return sbbRef;
  }

  public List<ProfileSpecRefDescriptor> getProfileSpecRef()
  {
    return profileSpecRef;
  }

  public MSbbClasses getSbbClasses()
  {
    return sbbClasses;
  }

  public String getAddressProfileSpecAliasRef()
  {
    return addressProfileSpecAliasRef;
  }

  public List<MEventEntry> getEvent()
  {
    return event;
  }

  public List<MActivityContextAttributeAlias> getActivityContextAttributeAlias()
  {
    return activityContextAttributeAlias;
  }

  public List<EnvEntryDescriptor> getEnvEntry()
  {
    return envEntry;
  }

  public List<ResourceAdaptorTypeBindingDescriptor> getResourceAdaptorTypeBinding()
  {
    return resourceAdaptorTypeBinding;
  }

  public List<EjbRefDescriptor> getEjbRef()
  {
    return ejbRef;
  }

}
