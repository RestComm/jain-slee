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

package org.mobicents.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.rf.events.avp.ClassIdentifier;
import net.java.slee.resource.diameter.rf.events.avp.MessageClass;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * MessageClassImpl.java
 *
 * <br>Project:  mobicents
 * <br>1:10:15 PM Apr 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MessageClassImpl extends GroupedAvpImpl implements MessageClass {

  public MessageClassImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public MessageClassImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MessageClass#getClassIdentifier()
   */
  public ClassIdentifier getClassIdentifier() {
    return (ClassIdentifier) getAvpAsEnumerated(DiameterRfAvpCodes.CLASS_IDENTIFIER, DiameterRfAvpCodes.TGPP_VENDOR_ID, ClassIdentifier.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MessageClass#getTokenText()
   */
  public String getTokenText() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.TOKEN_TEXT, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MessageClass#hasClassIdentifier()
   */
  public boolean hasClassIdentifier() {
    return hasAvp( DiameterRfAvpCodes.CLASS_IDENTIFIER, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MessageClass#hasTokenText()
   */
  public boolean hasTokenText() {
    return hasAvp( DiameterRfAvpCodes.TOKEN_TEXT, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MessageClass#setClassIdentifier(net.java.slee.resource.diameter.rf.events.avp.ClassIdentifier)
   */
  public void setClassIdentifier( ClassIdentifier classIdentifier ) {
    addAvp(DiameterRfAvpCodes.CLASS_IDENTIFIER, DiameterRfAvpCodes.TGPP_VENDOR_ID, classIdentifier.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MessageClass#setTokenText(String)
   */
  public void setTokenText( String tokenText ) {
    addAvp(DiameterRfAvpCodes.TOKEN_TEXT, DiameterRfAvpCodes.TGPP_VENDOR_ID, tokenText);
  }

}
