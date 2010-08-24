/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.diameter;

import java.util.concurrent.ConcurrentHashMap;

import net.java.slee.resource.diameter.base.DiameterActivity;

import org.mobicents.slee.resource.diameter.base.DiameterActivityHandle;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LocalDiameterActivityManagement implements DiameterActivityManagement{

  private ConcurrentHashMap<DiameterActivityHandle,DiameterActivity> activities = new ConcurrentHashMap<DiameterActivityHandle, DiameterActivity>();

  public DiameterActivity get(DiameterActivityHandle handle) {
    return activities.get(handle);
  }

  public void put(DiameterActivityHandle handle, DiameterActivity activity) {
    // JIC check
    if(this.activities.containsKey(handle)) {
      throw new IllegalArgumentException("There is already activity for: "+handle+" -- "+this.activities.get(handle));
    }
    this.activities.put(handle, activity);
  }

  public DiameterActivity remove(DiameterActivityHandle handle) {
    return this.activities.remove(handle);
  }

  public boolean containsKey(DiameterActivityHandle activityHandle) {
    return this.activities.containsKey(activityHandle);
  }

  public void update(DiameterActivityHandle handle, DiameterActivity activity) {
    //here we don't do a thing
  }

}
