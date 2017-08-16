/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.management.jmx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.slee.resource.ActivityHandle;

//import org.jboss.serial.io.JBossObjectInputStream;
//import org.jboss.serial.io.JBossObjectOutputStream;
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
        return new JmxActivityContextHandle(ach.getActivityType(), ach.getActivityType() == ActivityType.RA ? ((ResourceAdaptorActivityContextHandle) ach).getResourceAdaptorEntity().getName() : "", handleToString(ach.getActivityHandle()), ach.getActivityHandle().toString());
    }

    public static ActivityContextHandle decode(JmxActivityContextHandle jmxach, SleeContainer sleeContainer) {
        final ActivityHandle activityHandle = StringToHandle(jmxach.getActivityHandleBase64());
        switch (jmxach.getActivityType()) {

            case RA:
                final String raEntityName = jmxach.getActivitySource();
                final ResourceAdaptorEntity raEntity = sleeContainer.getResourceManagement().getResourceAdaptorEntity(raEntityName);
                if (raEntity != null) {
                    return raEntity.getActivityContextHandle(activityHandle);
                } else {
                    throw new IllegalArgumentException("unable to recreate activity context handle, the ra entity not found");
                }

            case PTABLE:
                return ((ProfileTableActivityHandle) activityHandle).getActivityContextHandle();

            case SERVICE:
                return ((ServiceActivityHandle) activityHandle).getActivityContextHandle();

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
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);
            //final JBossObjectOutputStream jboos = new JBossObjectOutputStream(baos);
            final ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(ah);
            bytes = baos.toByteArray();

            base64 = Base64.encodeBytes(bytes);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return base64;
    }

    private static ActivityHandle StringToHandle(String base64) {
        ActivityHandle activityHandle = null;
        try {
            byte[] bytes = Base64.decode(base64);

            final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            //final JBossObjectInputStream jbois = new JBossObjectInputStream(bais);

            final ObjectInputStream ois = new ObjectInputStream(bais);

            activityHandle = (ActivityHandle) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return activityHandle;
    }

}
