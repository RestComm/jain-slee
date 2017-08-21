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
