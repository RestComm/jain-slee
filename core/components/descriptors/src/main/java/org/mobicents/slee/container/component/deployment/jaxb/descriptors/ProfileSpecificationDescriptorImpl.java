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
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MCollator;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileClasses;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileIndex;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpec;
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
  private List<MLibraryRef> libraryRefs;
  private List<MProfileSpecRef> profileSpecRefs;
  private List<MCollator> collators;

  private List<MEnvEntry> envEntries;
  private List<MQuery> queryElements;
  private boolean profileHints = false;
  private String readOnly;
  private String eventsEnabled;

  private MSecurityPermissions securityPremissions;

  private boolean isSlee11;

  private Set<ComponentID> dependenciesSet;

  public ProfileSpecificationDescriptorImpl( MProfileSpec profileSpec, MSecurityPermissions securityPermissions, boolean isSlee11 )
  {
    this.description = profileSpec.getDescription();
    this.profileSpecificationID =  new ProfileSpecificationID(profileSpec.getProfileSpecName(), profileSpec.getProfileSpecVendor(), profileSpec.getProfileSpecVersion());

    this.profileClasses = profileSpec.getProfileClasses();

    // Just for 1.0
    for(MProfileIndex indexedAttribute : profileSpec.getProfileIndex())
    {
      this.indexedAttributes.add( indexedAttribute );
    }

    // Now it's only 1.1
    this.libraryRefs = profileSpec.getLibraryRef();
    this.profileSpecRefs = profileSpec.getProfileSpecRef();
    this.collators = profileSpec.getCollator();

    this.envEntries = profileSpec.getEnvEntry();

    this.queryElements = profileSpec.getQuery();

    this.isSlee11 = isSlee11;

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

  public List<MLibraryRef> getLibraryRefs() {
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

  public List<MProfileSpecRef> getProfileSpecRefs() {
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

  public boolean getProfileHints() {
    return profileHints;
  }

  public String getReadOnly() {
    return readOnly;
  }

  public String getEventsEnabled() {
    return eventsEnabled;
  }

  public MSecurityPermissions getSecurityPremissions() {
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

}
