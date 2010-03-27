/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.slee.resource.diameter.base.events.avp;

import java.util.Arrays;

import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpType;

/**
 *
 * Implementation of {@link DiameterAvp}.
 * 
 * @author <a href = "mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href = "mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author Erick Svenson
 */
public class DiameterAvpImpl implements DiameterAvp {

  protected long vendorId;
  protected int code, mnd, prt;
  protected String name = "undefined";
  protected DiameterAvpType type = null;

  protected byte[] value;

  public DiameterAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value, DiameterAvpType type) {
    this.code = code;
    this.vendorId = vendorId;
    this.mnd = mnd;
    this.prt = prt;
    this.value = value;
    this.type=type;
  }

  public int getCode() {
    return code;
  }

  public long getVendorId() {
    return vendorId;
  }

  public String getName() {
    return name;
  }

  public DiameterAvpType getType() {
    return type;
  }

  public int getMandatoryRule() {
    return mnd;
  }

  public int getProtectedRule() {
    return prt;
  }

  public double doubleValue() {
    try {
      return AvpUtilities.getParser().bytesToDouble(value);
    }
    catch (Exception e) {
      return Double.MIN_VALUE;
    }
  }

  public float floatValue() {
    try {
      return AvpUtilities.getParser().bytesToFloat(value);
    }
    catch (Exception e) {
      return Float.MIN_VALUE;
    }
  }

  public int intValue() {
    try {
      return AvpUtilities.getParser().bytesToInt(value);
    }
    catch (Exception e) {
      return Integer.MIN_VALUE;
    }
  }

  public long longValue() {
    try {
      return  AvpUtilities.getParser().bytesToLong(value);
    }
    catch (Exception e) {
      return Long.MIN_VALUE;
    }
  }

  public String stringValue() {
    try {
      return AvpUtilities.getParser().bytesToUtf8String(value);
    }
    catch (Exception e) {
      return null;
    }
  }

  public byte[] byteArrayValue() {
    return value;
  }

  public Object clone() {
    // TODO: Confirm it works as supposed! Findbugs complained...
    // return new DiameterAvpImpl(code, vendorId, mnd, prt, value, type);
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException e) {
      return null;
    }
  }

  @Override
  public String toString() {
    StringBuilder toStringSB = new StringBuilder();

    toStringSB.append("DiameterAVP[Vendor[").append(this.vendorId).append("], Code[").append(this.code).append("], ").append("Name[").
    append(this.name).append("], Type[").append(this.type).append("], Mandatory[").append(this.mnd).append("], ").append("Protected[").
    append(this.prt).append("], Value[").append(String.valueOf(this.value)).append("]]");

    return toStringSB.toString();
  }

  public String octetStringValue() {
    try {
      return AvpUtilities.getParser().bytesToOctetString(value);
    }
    catch (Exception e) {
      return null;
    }
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object that) {
    if(!(that instanceof DiameterAvp)) {
      return false;
    }
    else {
      DiameterAvp other = (DiameterAvp) that;
      return this == other || (this.code == other.getCode() && this.vendorId == other.getVendorId() && Arrays.equals( this.byteArrayValue(), other.byteArrayValue() ));
    }
  }
}
