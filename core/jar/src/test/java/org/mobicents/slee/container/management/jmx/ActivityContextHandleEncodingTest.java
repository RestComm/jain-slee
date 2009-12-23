package org.mobicents.slee.container.management.jmx;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityType;

public class ActivityContextHandleEncodingTest extends TestCase {

  public void testEncodeAndDecode() {
    ActivityContextHandle ach = new ActivityContextHandle(ActivityType.RA, "jUnitSource", new ActivityHandleTest("jUnitHandle"));
    JmxActivityContextHandle achJmx = ActivityContextHandleSerializer.encode(ach);
    ActivityContextHandle achCopy = ActivityContextHandleSerializer.decode(achJmx);

    Assert.assertEquals(ach, achCopy);
  }

  public void testDecodeAndEncode() {
    JmxActivityContextHandle achJmxDummy = ActivityContextHandleSerializer.encode(new ActivityContextHandle(ActivityType.RA, "jUnitSource", new ActivityHandleTest("jUnitHandle")));

    JmxActivityContextHandle achJmx = new JmxActivityContextHandle(ActivityType.RA, "jUnitSource", achJmxDummy.getActivityHandleBase64(), achJmxDummy.getActivityHandleToString());
    ActivityContextHandle ach = ActivityContextHandleSerializer.decode(achJmx);
    JmxActivityContextHandle achJmxCopy = ActivityContextHandleSerializer.encode(ach);

    Assert.assertEquals(achJmx, achJmxCopy);
  }

  private class ActivityHandleTest implements ActivityHandle, Serializable {

    private static final long serialVersionUID = 1L;

    private String handle;

    public ActivityHandleTest(String handle) {
      this.handle = handle;
    }

    public boolean equals(Object o) {
      if (o != null && o.getClass() == this.getClass()) {
        return ((ActivityHandleTest)o).handle.equals(this.handle);
      }
      else {
        return false;
      }
    }

    public String toString() {
      return "Activity Handle Test ID[" + handle + "]";
    }

    public int hashCode() {
      return handle.hashCode();
    }
  }

}
