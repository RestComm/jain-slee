/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mobicents.slee.container.component.sbb.CMPFieldDescriptor;
import org.mobicents.slee.container.component.sbb.GetChildRelationMethodDescriptor;
import org.mobicents.slee.container.component.sbb.GetProfileCMPMethodDescriptor;
import org.mobicents.slee.container.component.sbb.SbbAbstractClassDescriptor;

/**
 * Start time:11:19:14 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a>
 */
public class MSbbAbstractClass implements SbbAbstractClassDescriptor {

  private String description;
  private String sbbAbstractClassName;

  private boolean reentrant = false;

  //Map at this level will mask duplicate declarations
  //private Map<String,MSbbCMPField> cmpFields;
  private List<CMPFieldDescriptor> cmpFields;

  private Map<String,GetProfileCMPMethodDescriptor> getProfileCMPMethods;

  private Map<String,GetChildRelationMethodDescriptor> getChildRelationMethods;

  public MSbbAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbAbstractClass sbbAbstractClass10)
  {
    this.description = sbbAbstractClass10.getDescription() == null ? null : sbbAbstractClass10.getDescription().getvalue();
    this.sbbAbstractClassName = sbbAbstractClass10.getSbbAbstractClassName().getvalue();

    this.reentrant = Boolean.parseBoolean( sbbAbstractClass10.getReentrant() );

    this.cmpFields = new ArrayList<CMPFieldDescriptor>();
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.CmpField cmpField10 : sbbAbstractClass10.getCmpField())
    {
      this.cmpFields.add(new MSbbCMPField(cmpField10));
    }

    this.getProfileCMPMethods = new HashMap<String,GetProfileCMPMethodDescriptor>(sbbAbstractClass10.getGetProfileCmpMethod().size()*2+1);
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetProfileCmpMethod getProfileCmpMethod10 : sbbAbstractClass10.getGetProfileCmpMethod())
    {
    	MGetProfileCMPMethod mGetProfileCMPMethod = new MGetProfileCMPMethod(getProfileCmpMethod10);	
    	this.getProfileCMPMethods.put(mGetProfileCMPMethod.getProfileCmpMethodName(),mGetProfileCMPMethod);
    }

    this.getChildRelationMethods = new HashMap<String,GetChildRelationMethodDescriptor>(sbbAbstractClass10.getGetChildRelationMethod().size()*2+1);
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetChildRelationMethod getChildRelationMethod10 : sbbAbstractClass10.getGetChildRelationMethod())
    {
      MGetChildRelationMethod mg=new MGetChildRelationMethod(getChildRelationMethod10);
      this.getChildRelationMethods.put(mg.getChildRelationMethodName(),mg);
    }
  }

  public MSbbAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbAbstractClass sbbAbstractClass11)
  {
    this.description = sbbAbstractClass11.getDescription() == null ? null : sbbAbstractClass11.getDescription().getvalue();
    this.sbbAbstractClassName = sbbAbstractClass11.getSbbAbstractClassName().getvalue();

    this.reentrant = Boolean.parseBoolean( sbbAbstractClass11.getReentrant() );

    this.cmpFields = new ArrayList< CMPFieldDescriptor>();
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.CmpField cmpField11 : sbbAbstractClass11.getCmpField())
    {
      this.cmpFields.add(new MSbbCMPField(cmpField11));
    }

    this.getProfileCMPMethods = new HashMap<String,GetProfileCMPMethodDescriptor>(sbbAbstractClass11.getGetProfileCmpMethod().size()*2+1);
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetProfileCmpMethod getProfileCmpMethod11 : sbbAbstractClass11.getGetProfileCmpMethod())
    {
    	MGetProfileCMPMethod mGetProfileCMPMethod = new MGetProfileCMPMethod(getProfileCmpMethod11);	
    	this.getProfileCMPMethods.put(mGetProfileCMPMethod.getProfileCmpMethodName(),mGetProfileCMPMethod);
    }

    this.getChildRelationMethods = new HashMap<String,GetChildRelationMethodDescriptor>(sbbAbstractClass11.getGetChildRelationMethod().size()*2+1);
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetChildRelationMethod getChildRelationMethod11 : sbbAbstractClass11.getGetChildRelationMethod())
    {
      MGetChildRelationMethod mg=new MGetChildRelationMethod(getChildRelationMethod11);
      this.getChildRelationMethods.put(mg.getChildRelationMethodName(),mg);
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

  public List<CMPFieldDescriptor> getCmpFields()
  {
    return cmpFields;
  }

  /**
   * Retrieves the map between profile CMP method names and {@link MGetProfileCMPMethod}s
   * @return
   */
  public Map<String,GetProfileCMPMethodDescriptor> getProfileCMPMethods()
  {
    return getProfileCMPMethods;
  }

  /**
   * Retrieves the Map between child relation method names and {@link MGetChildRelationMethod}s
   * @return
   */
  public Map<String,GetChildRelationMethodDescriptor> getChildRelationMethods()
  {
    return getChildRelationMethods;
  }

}
