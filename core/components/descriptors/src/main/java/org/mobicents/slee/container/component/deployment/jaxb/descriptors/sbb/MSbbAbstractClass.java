package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import java.util.ArrayList;
import java.util.List;

/**
 * Start time:11:19:14 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a>
 */
public class MSbbAbstractClass {

  private String description;
  private String sbbAbstractClassName;

  private boolean reentrant = false;

  //Map at this level will mask duplicate declarations
  //private Map<String,MSbbCMPField> cmpFields;
  private List<MSbbCMPField> cmpFields;

  private List<MGetProfileCMPMethod> getProfileCMPMethods;

  private List<MGetChildRelationMethod> getChildRelationMethods;

  public MSbbAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbAbstractClass sbbAbstractClass10)
  {
    this.description = sbbAbstractClass10.getDescription() == null ? null : sbbAbstractClass10.getDescription().getvalue();
    this.sbbAbstractClassName = sbbAbstractClass10.getSbbAbstractClassName().getvalue();

    this.reentrant = Boolean.parseBoolean( sbbAbstractClass10.getReentrant() );

    this.cmpFields = new ArrayList< MSbbCMPField>();
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.CmpField cmpField10 : sbbAbstractClass10.getCmpField())
    {
      this.cmpFields.add(new MSbbCMPField(cmpField10));
    }

    this.getProfileCMPMethods = new ArrayList<MGetProfileCMPMethod>();
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetProfileCmpMethod getProfileCmpMethod10 : sbbAbstractClass10.getGetProfileCmpMethod())
    {
      this.getProfileCMPMethods.add(new MGetProfileCMPMethod(getProfileCmpMethod10));
    }

    this.getChildRelationMethods = new ArrayList<MGetChildRelationMethod>();
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetChildRelationMethod getChildRelationMethod10 : sbbAbstractClass10.getGetChildRelationMethod())
    {
      MGetChildRelationMethod mg=new MGetChildRelationMethod(getChildRelationMethod10);
      this.getChildRelationMethods.add(mg);
    }
  }

  public MSbbAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbAbstractClass sbbAbstractClass11)
  {
    this.description = sbbAbstractClass11.getDescription() == null ? null : sbbAbstractClass11.getDescription().getvalue();
    this.sbbAbstractClassName = sbbAbstractClass11.getSbbAbstractClassName().getvalue();

    this.reentrant = Boolean.parseBoolean( sbbAbstractClass11.getReentrant() );

    this.cmpFields = new ArrayList< MSbbCMPField>();
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.CmpField cmpField11 : sbbAbstractClass11.getCmpField())
    {
      this.cmpFields.add(new MSbbCMPField(cmpField11));
    }

    this.getProfileCMPMethods=new ArrayList<MGetProfileCMPMethod>();
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetProfileCmpMethod getProfileCmpMethod11 : sbbAbstractClass11.getGetProfileCmpMethod())
    {
      this.getProfileCMPMethods.add(new MGetProfileCMPMethod(getProfileCmpMethod11));
    }

    this.getChildRelationMethods=new ArrayList<MGetChildRelationMethod>();
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetChildRelationMethod getChildRelationMethod11 : sbbAbstractClass11.getGetChildRelationMethod())
    {
      this.getChildRelationMethods.add(new MGetChildRelationMethod(getChildRelationMethod11));
    }
  }

  public String getDescription()
  {
    return description;
  }

  public boolean isReentrant() 
  {
    return reentrant;
  }

  public String getSbbAbstractClassName()
  {
    return sbbAbstractClassName;
  }

  public List<MSbbCMPField> getCmpFields()
  {
    return cmpFields;
  }

  public List<MGetProfileCMPMethod> getProfileCMPMethods()
  {
    return getProfileCMPMethods;
  }

  public List<MGetChildRelationMethod> getChildRelationMethods()
  {
    return getChildRelationMethods;
  }

}
