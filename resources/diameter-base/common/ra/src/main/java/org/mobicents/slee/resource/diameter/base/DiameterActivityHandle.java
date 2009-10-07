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
  
}
