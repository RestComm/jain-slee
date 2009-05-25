package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.util.ArrayList;

/**
 * Start time:16:33:45 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MCMPField {

  private String description;
  private String cmpFieldName;

  private boolean unique = false;
  private String uniqueCollatorRef;

  private ArrayList<MIndexHint> indexHints = null;

  public MCMPField(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.CmpField cmpField11)
  {

    this.description = cmpField11.getDescription() == null ? null : cmpField11.getDescription().getvalue();

    this.cmpFieldName = cmpField11.getCmpFieldName().getvalue();

    this.unique=Boolean.parseBoolean(cmpField11.getUnique());

    // A unique-collator-ref attribute.
    // This optional attribute applies only when the unique attribute is
    // “True”, and the Java type of the Profile CMP field is
    // java.lang.String. It references a collator by its collatoralias
    // that is specified within the same profile-spec element. It
    // is used to determine equality of the CMP field between the various Profiles
    // within the Profile Table. If this attribute is not specified, and the
    // Java type of the CMP field is java.lang.String then the
    // String.equals() method is used for determining equality.
    this.uniqueCollatorRef = cmpField11.getUniqueCollatorRef();

    this.indexHints = new ArrayList<MIndexHint>();
    if(cmpField11.getIndexHint() != null && cmpField11.getIndexHint().size()>0)
    {
      for(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.IndexHint ih : cmpField11.getIndexHint())
      {
        this.indexHints.add(new MIndexHint(ih));
      }
    }
  }

  public String getDescription() {
    return description;
  }

  public String getCmpFieldName() {
    return cmpFieldName;
  }

  public boolean getUnique() {
    return unique;
  }

  public String getUniqueCollatorRef() {
    return uniqueCollatorRef;
  }

  public ArrayList<MIndexHint> getIndexHints() {
    return indexHints;
  }

}
