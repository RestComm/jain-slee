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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

/**
 * 
 * MQueryExpressionType.java
 *
 * <br>Project:  mobicents
 * <br>3:27:19 AM Feb 12, 2009 
 * <br>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public enum MQueryExpressionType {

  Compare(1), LongestPrefixMatch(2), HasPrefix(3), RangeMatch(4), And(5), Or(6), Not(7);

  private int code = -1;

  MQueryExpressionType(int val)
  {
    this.code=val;
  }

  public int getCode()
  {
    return code;
  }

}
