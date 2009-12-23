package org.mobicents.slee.container.management.jmx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.slee.resource.ActivityHandle;

import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.jboss.util.Base64;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;

public class ActivityContextHandleSerializer {

  public static JmxActivityContextHandle encode(ActivityContextHandle ach) {
    return new JmxActivityContextHandle(ach.getActivityType(), ach.getActivitySource(), handleToString(ach.getActivityHandle()), ach.getActivityHandle().toString());
  }

  public static ActivityContextHandle decode(JmxActivityContextHandle jmxach) {
    return new ActivityContextHandle(jmxach.getActivityType(), jmxach.getActivitySource(), StringToHandle(jmxach.getActivityHandleBase64()));
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
