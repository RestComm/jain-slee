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
