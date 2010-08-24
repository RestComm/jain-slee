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
package org.mobicents.slee.resource.diameter.base;

import javax.slee.resource.ActivityHandle;

/**
 * Activity handle wrapper for DiameterSession activity with given unique session ID
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author Erick Svenson
 */
public class DiameterActivityHandle implements ActivityHandle
{
  private String handle;
  
  public DiameterActivityHandle(String id) {
    this.handle = id;
  }
  
  public boolean equals(Object o)
  {
    if (o != null && o.getClass() == this.getClass()) {
      return ((DiameterActivityHandle)o).handle.equals(this.handle);
    }
    else {
      return false;
    }
  }
  
  public String toString() {
      return "Diameter Session ID[" + handle + "]";
  }
  
  public int hashCode() {
    return handle.hashCode();
  }

  /**
   * @return the id
   */
  public String getId() {
    return handle;
  }

}
