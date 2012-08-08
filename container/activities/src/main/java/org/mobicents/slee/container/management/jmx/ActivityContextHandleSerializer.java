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

package org.mobicents.slee.container.management.jmx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.slee.resource.ActivityHandle;

import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.jboss.util.Base64;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.profile.ProfileTableActivityHandle;
import org.mobicents.slee.container.resource.ResourceAdaptorActivityContextHandle;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;
import org.mobicents.slee.container.service.ServiceActivityHandle;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityContextHandle;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandleImpl;

public class ActivityContextHandleSerializer {

  public static JmxActivityContextHandle encode(ActivityContextHandle ach) {
    return new JmxActivityContextHandle(ach.getActivityType(), ach.getActivityType() == ActivityType.RA ? ((ResourceAdaptorActivityContextHandle)ach).getResourceAdaptorEntity().getName() : "", handleToString(ach.getActivityHandle()), ach.getActivityHandle().toString());
  }

  public static ActivityContextHandle decode(JmxActivityContextHandle jmxach, SleeContainer sleeContainer) {
	 final ActivityHandle activityHandle = StringToHandle(jmxach.getActivityHandleBase64());
	  switch (jmxach.getActivityType()) {
	
	  case RA:
		final String raEntityName = jmxach.getActivitySource();
		final ResourceAdaptorEntity raEntity = sleeContainer.getResourceManagement().getResourceAdaptorEntity(raEntityName);
		if (raEntity != null) {
			return raEntity.getActivityContextHandle(activityHandle);
		}
		else {
			throw new IllegalArgumentException("unable to recreate activity context handle, the ra entity not found");
		}
		
	  case PTABLE:
		return ((ProfileTableActivityHandle)activityHandle).getActivityContextHandle();
		
	  case SERVICE:
		return ((ServiceActivityHandle)activityHandle).getActivityContextHandle();
			
	  case NULL:
		return new NullActivityContextHandle((NullActivityHandleImpl) activityHandle);
			
	  default:
		throw new IllegalArgumentException("unexpected activity type");		
	} 
  }

  private static String handleToString(ActivityHandle ah) {
    String base64 = null;
    try {
      byte[] bytes = null;
      // need to marshall the handle, but this happens at most once per AS instance
      int bufferSize = 1024;
      final ByteArrayOutputStream baos =  new ByteArrayOutputStream(bufferSize);
      final JBossObjectOutputStream jboos = new JBossObjectOutputStream(baos);

      jboos.writeObject(ah);

      bytes = baos.toByteArray();

      base64 = Base64.encodeBytes(bytes);
      jboos.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return base64;
  }

  private static ActivityHandle StringToHandle(String base64) {

    ActivityHandle activityHandle = null;

    try {
      byte[] bytes = Base64.decode(base64);

      final ByteArrayInputStream bais =  new ByteArrayInputStream(bytes);
      final JBossObjectInputStream jbois = new JBossObjectInputStream(bais);

      activityHandle = (ActivityHandle) jbois.readObject();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return activityHandle;
  }

}
