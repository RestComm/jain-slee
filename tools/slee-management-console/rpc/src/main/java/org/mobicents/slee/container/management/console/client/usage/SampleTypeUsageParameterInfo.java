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

package org.mobicents.slee.container.management.console.client.usage;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Stefano Zappaterra
 * 
 */
public class SampleTypeUsageParameterInfo extends UsageParameterInfo implements IsSerializable {

  private long maximum;

  private long minimum;

  private double mean;

  private long sampleCount;

  public SampleTypeUsageParameterInfo(String name, long maximum, long minimum, double mean, long sampleCount) {
    super(name);
    this.maximum = maximum;
    this.minimum = minimum;
    this.mean = mean;
    this.sampleCount = sampleCount;
  }

  public SampleTypeUsageParameterInfo() {
  }

  public long getMaximum() {
    return maximum;
  }

  public double getMean() {
    return mean;
  }

  public long getMinimum() {
    return minimum;
  }

  public long getSampleCount() {
    return sampleCount;
  }
}
