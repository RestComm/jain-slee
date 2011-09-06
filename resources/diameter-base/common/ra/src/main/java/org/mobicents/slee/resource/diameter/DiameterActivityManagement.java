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

package org.mobicents.slee.resource.diameter;

import net.java.slee.resource.diameter.base.DiameterActivity;

import org.mobicents.slee.resource.diameter.base.DiameterActivityHandle;

/**
 * 
 * Interface for activity management in diameter RAs. Implementation may store
 * activities locally or remotely.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface DiameterActivityManagement {

  /**
   * Adds activity.
   * 
   * @param handle
   * @param activity
   */
  public void put(DiameterActivityHandle handle, DiameterActivity activity);

  /**
   * Retrieves activity from storage. If its present it performs any required operation 
   * 
   * @param handle
   * @return
   */
  public DiameterActivity get(DiameterActivityHandle handle);

  public DiameterActivity remove(DiameterActivityHandle handle);

  //public Set<DiameterActivityHandle> keySet();

  public boolean containsKey(DiameterActivityHandle activityHandle);

  public void update(DiameterActivityHandle handle, DiameterActivity activity);

  public void startActivityRemoveTimer(DiameterActivityHandle handle);

  public void stopActivityRemoveTimer(DiameterActivityHandle handle);
}
