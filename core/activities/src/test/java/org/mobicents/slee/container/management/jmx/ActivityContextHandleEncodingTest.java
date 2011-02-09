package org.mobicents.slee.container.management.jmx;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityContextHandle;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandleImpl;

public class ActivityContextHandleEncodingTest extends TestCase {

  public void testEncodeAndDecode() {
    ActivityContextHandle ach = new NullActivityContextHandle(new NullActivityHandleImpl("jUnitHandle"));
    JmxActivityContextHandle achJmx = ActivityContextHandleSerializer.encode(ach);
    ActivityContextHandle achCopy = ActivityContextHandleSerializer.decode(achJmx,null);

    Assert.assertEquals(ach, achCopy);
  }

  public void testDecodeAndEncode() {
    JmxActivityContextHandle achJmxDummy = ActivityContextHandleSerializer.encode(new NullActivityContextHandle(new NullActivityHandleImpl("jUnitHandle")));

    JmxActivityContextHandle achJmx = new JmxActivityContextHandle(ActivityType.NULL, "jUnitSource", achJmxDummy.getActivityHandleBase64(), achJmxDummy.getActivityHandleToString());
    ActivityContextHandle ach = ActivityContextHandleSerializer.decode(achJmx,null);
    JmxActivityContextHandle achJmxCopy = ActivityContextHandleSerializer.encode(ach);

    Assert.assertEquals(achJmx, achJmxCopy);
  }

}
