package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MEnvEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MCollator;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileAbstractClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileCMPInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileClasses;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileIndex;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileLocalInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileManagementInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpec;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileTableInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQuery;


/**
 * Start time:13:41:11 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecificationDescriptorImpl {

  private ProfileSpecificationID profileSpecificationID;
  private String description;

  private MProfileClasses profileClasses;

  private Set<MProfileIndex> indexedAttributes;
  // FIXME: add hints here?

  // 1.1 Stuff
  private Set<MLibraryRef> libraryRefs;
  //FIXME: what the hell - again its a set, now we CANT validate if it doubles or declares the same alias ;....
  private Set<MProfileSpecRef> profileSpecRefs;
  private List<MCollator> collators;

  private List<MEnvEntry> envEntries;
  private List<MQuery> queryElements;
  private boolean singleProfile = false;
  private boolean readOnly = true;
  private boolean eventsEnabled = true;

  private MSecurityPermissions securityPremissions;

  private boolean isSlee11;

  private Set<ComponentID> dependenciesSet;

  public ProfileSpecificationDescriptorImpl( MProfileSpec profileSpec, MSecurityPermissions securityPermissions, boolean isSlee11 )
  {
    this.description = profileSpec.getDescription();
    this.profileSpecificationID =  new ProfileSpecificationID(profileSpec.getProfileSpecName(), profileSpec.getProfileSpecVendor(), profileSpec.getProfileSpecVersion());
    this.securityPremissions=securityPermissions;
    this.profileClasses = profileSpec.getProfileClasses();

    indexedAttributes=new HashSet<MProfileIndex>();
    // Just for 1.0
    for(MProfileIndex indexedAttribute : profileSpec.getProfileIndex())
    {
      this.indexedAttributes.add( indexedAttribute );
    }

    // Now it's only 1.1
    this.libraryRefs = new HashSet<MLibraryRef>(profileSpec.getLibraryRef());
    this.profileSpecRefs = new HashSet<MProfileSpecRef>(profileSpec.getProfileSpecRef());
    this.collators = profileSpec.getCollator();

    this.envEntries = profileSpec.getEnvEntry();

    this.queryElements = profileSpec.getQuery();

    this.isSlee11 = isSlee11;

    this.readOnly = profileSpec.getProfileReadOnly().booleanValue();
    this.eventsEnabled = profileSpec.getProfileEventsEnabled().booleanValue();
    buildDependenciesSet();
  }

  private void buildDependenciesSet()
  {
    this.dependenciesSet = new HashSet<ComponentID>();

    for(MLibraryRef libraryRef : libraryRefs)
    {
      this.dependenciesSet.add( libraryRef.getComponentID() );
    }
    
    for(MProfileSpecRef profileSpecRef : profileSpecRefs)
    {
      this.dependenciesSet.add( profileSpecRef.getComponentID() );
    }
  }

  public Set<MLibraryRef> getLibraryRefs() {
    return libraryRefs;
  }

  public String getDescription() {
    return description;
  }

  public ProfileSpecificationID getProfileSpecificationID() {
    return profileSpecificationID;
  }

  public MProfileClasses getProfileClasses()
  {
    return profileClasses;
  }

  public Set<MProfileIndex> getIndexedAttributes() {
    return indexedAttributes;
  }

  public Set<MProfileSpecRef> getProfileSpecRefs() {
    return profileSpecRefs;
  }

  public List<MCollator> getCollators() {
    return collators;
  }

  public List<MEnvEntry> getEnvEntries() {
    return envEntries;
  }

  public List<MQuery> getQueryElements() {
    return queryElements;
  }

  public boolean isSingleProfile() {
    return this.singleProfile;
  }

  public boolean getReadOnly() {
    return readOnly;
  }

  public boolean getEventsEnabled() {
    return eventsEnabled;
  }

  public MSecurityPermissions getSecurityPermissions() {
    return securityPremissions;
  }

  public Set<ComponentID> getDependenciesSet() {
    return this.dependenciesSet;
  }
  
  public boolean isSlee11()
  {
    return isSlee11;
  }
  
  // FIXME: Do we need this at this point?
  public Map<String, MQuery> getQueriesMap()
  {
    List<MQuery> qs = this.getQueryElements();
    Map<String,MQuery> result = new HashMap<String, MQuery>();
    for(MQuery q:qs)
    {
      result.put(q.getName(), q);
    }

    return result;
  }

  // Convenience methods
  public MProfileTableInterface getProfileTableInterface()
  {
    return this.profileClasses.getProfileTableInterface();
  }

  public MUsageParametersInterface getProfileUsageParameterInterface()
  {
    return this.profileClasses.getProfileUsageParameterInterface();
  }

  public MProfileAbstractClass getProfileAbstractClass()
  {
    return this.profileClasses.getProfileAbstractClass();
  }

  public MProfileManagementInterface getProfileManagementInterface()
  {
    return this.profileClasses.getProfileManagementInterface();
  }

  public MProfileCMPInterface getProfileCMPInterface()
  {
    return this.profileClasses.getProfileCMPInterface();
  }

  public MProfileLocalInterface getProfileLocalInterface()
  {
    return this.profileClasses.getProfileLocalInterface();
  }

}
