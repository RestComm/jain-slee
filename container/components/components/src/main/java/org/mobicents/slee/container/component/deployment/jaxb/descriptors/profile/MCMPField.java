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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.profile.cmp.ProfileCMPFieldDescriptor;

/**
 * Start time:16:33:45 2009-01-18<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MCMPField implements ProfileCMPFieldDescriptor {

  private String description;
  private String cmpFieldName;

  private boolean unique = false;
  private String uniqueCollatorRef;

  private List<MIndexHint> indexHints = null;

  public MCMPField(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.CmpField cmpField11)
  {

    this.description = cmpField11.getDescription() == null ? null : cmpField11.getDescription().getvalue();

    this.cmpFieldName = cmpField11.getCmpFieldName().getvalue();

    this.unique=Boolean.parseBoolean(cmpField11.getUnique());

    // A unique-collator-ref attribute.
    // This optional attribute applies only when the unique attribute is
    // �True�, and the Java type of the Profile CMP field is
    // java.lang.String. It references a collator by its collatoralias
    // that is specified within the same profile-spec element. It
    // is used to determine equality of the CMP field between the various Profiles
    // within the Profile Table. If this attribute is not specified, and the
    // Java type of the CMP field is java.lang.String then the
    // String.equals() method is used for determining equality.
    this.uniqueCollatorRef = cmpField11.getUniqueCollatorRef();

    this.indexHints = new ArrayList<MIndexHint>();
    if(cmpField11.getIndexHint() != null && !cmpField11.getIndexHint().isEmpty())
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

  public boolean isUnique() {
    return unique;
  }

  public String getUniqueCollatorRef() {
    return uniqueCollatorRef;
  }

  public List<MIndexHint> getIndexHints() {
    return indexHints;
  }

}
