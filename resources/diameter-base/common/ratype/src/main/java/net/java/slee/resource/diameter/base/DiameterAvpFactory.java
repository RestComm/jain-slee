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

package net.java.slee.resource.diameter.base;

import java.util.Date;

import net.java.slee.resource.diameter.base.events.DiameterCommand;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpType;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;

/**
 * 
 * Factory to create instances of {@link DiameterAvp}.
 * <p/>
 * An implementation of this class should be returned by the
 * {@link DiameterProvider#getDiameterAvpFactory()} method.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface DiameterAvpFactory {

  /**
   * Create a value for a Grouped AVP.  Type will always be
   * {@link DiameterAvpType#GROUPED}
   *
   * @param avpCode the code for the AVP
   * @param avps an array of DiameterAvp objects
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, DiameterAvp[] avps) throws NoSuchAvpException, AvpNotAllowedException;

  /**
   * Create a value for a vendor-specific Grouped AVP.  Type will always be
   * {@link DiameterAvpType#GROUPED}
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param avps an array of DiameterAvp objects
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, DiameterAvp[] avps) throws NoSuchAvpException, AvpNotAllowedException;

  /**
   * Create an AVP containing a DiameterAvpValue from the byte[] value provided.  The
   * AVP type will be determined from the AVP code.
   *
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, byte[] value) throws NoSuchAvpException;

  /**
   * Create a vendor-specific AVP containing a DiameterAvpValue from the byte[] value provided.
   * The AVP type will be determined from the AVP code.
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, byte[] value) throws NoSuchAvpException;

  /**
   * Create an AVP containing a DiameterAvpValue from the int value provided.  The
   * AVP type will be determined from the AVP code.
   *
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, int value) throws NoSuchAvpException;

  /**
   * Create a vendor-specific AVP containing a DiameterAvpValue from the int value provided.
   * The AVP type will be determined from the AVP code.
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, int value) throws NoSuchAvpException;

  /**
   * Create an AVP containing a DiameterAvpValue from the long value provided.  The
   * AVP type will be determined from the AVP code.
   *
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, long value) throws NoSuchAvpException;

  /**
   * Create a vendor-specific AVP containing a DiameterAvpValue from the long value provided.
   * The AVP type will be determined from the AVP code.
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, long value) throws NoSuchAvpException;

  /**
   * Create an AVP containing a DiameterAvpValue from the float value provided.  The
   * AVP type will be determined from the AVP code.
   *
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, float value) throws NoSuchAvpException;

  /**
   * Create a vendor-specific AVP containing a DiameterAvpValue from the float value provided.
   * The AVP type will be determined from the AVP code.
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, float value) throws NoSuchAvpException;

  /**
   * Create an AVP containing a DiameterAvpValue from the double value provided.  The
   * AVP type will be determined from the AVP code.
   *
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, double value) throws NoSuchAvpException;

  /**
   * Create a vendor-specific AVP containing a DiameterAvpValue from the double value provided.
   * The AVP type will be determined from the AVP code.
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, double value) throws NoSuchAvpException;

  /**
   * Create an AVP containing a DiameterAvpValue from the java.net.InetAddress value provided.  The
   * AVP type will be determined from the AVP code.
   *
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, java.net.InetAddress value) throws NoSuchAvpException;

  /**
   * Create a vendor-specific AVP containing a DiameterAvpValue from the java.net.InetAddress value provided.
   * The AVP type will be determined from the AVP code.
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, java.net.InetAddress value) throws NoSuchAvpException;

  /**
   * Create an AVP containing a DiameterAvpValue from the Date value provided.  The
   * AVP type will be determined from the AVP code.
   *
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, Date value) throws NoSuchAvpException;

  /**
   * Create a vendor-specific AVP containing a DiameterAvpValue from the Date value provided.
   * The AVP type will be determined from the AVP code.
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, Date value) throws NoSuchAvpException;

  /**
   * Create an AVP containing a DiameterAvpValue from the java.lang.String value provided.  The
   * AVP type will be determined from the AVP code.
   *
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, java.lang.String value) throws NoSuchAvpException;

  /**
   * Create a vendor-specific AVP containing a DiameterAvpValue from the java.lang.String value provided.
   * The AVP type will be determined from the AVP code.
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, java.lang.String value) throws NoSuchAvpException;

  /**
   * Create an AVP containing a DiameterAvpValue from the net.java.slee.resource.diameter.base.types.Enumerated value provided.  The
   * AVP type will be determined from the AVP code.
   *
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int avpCode, net.java.slee.resource.diameter.base.events.avp.Enumerated value) throws NoSuchAvpException;

  /**
   * Create a vendor-specific AVP containing a DiameterAvpValue from the net.java.slee.resource.diameter.base.types.Enumerated value provided.
   * The AVP type will be determined from the AVP code.
   *
   * @param vendorID the IANA-assigned enterprise number of the vendor that specified the AVP (eg. 10415 for 3GPP).
   * @param avpCode the code for the AVP
   * @param value the value itself
   * @return an implementation of the DiameterAvp interface
   */
  DiameterAvp createAvp(int vendorID, int avpCode, net.java.slee.resource.diameter.base.events.avp.Enumerated value) throws NoSuchAvpException;

  /**
   * Create an instance of a DiameterCommand concrete implementation using
   * the given arguments.
   *
   * @param commandCode the command code of the command
   * @param applicationId the application ID of the command
   * @param shortName   the short name of the command, e.g., "CER"
   * @param longName    the long name of the command, e.g., "Capabilities-Exchange-Request"
   * @param isRequest   true if this command represents a request (not answer)
   * @param isProxiable true if this command may be proxied
   * @return a complete and correct DiameterCommand object to be passed to
   *         {@link #createMessage(DiameterCommand, DiameterAvp[] avps)}.
   */
  DiameterCommand createCommand(int commandCode, int applicationId, String shortName, String longName, boolean isRequest, boolean isProxiable);

  /**
   * Create a ProxyInfo (Grouped AVP) instance using required AVP values.
   * 
   * @param proxyHost
   * @param proxyState
   * @return
   */
  ProxyInfoAvp createProxyInfo(DiameterIdentity proxyHost, byte[] proxyState);

  /**
   * Create an empty ProxyInfo (Grouped AVP) instance.
   * 
   * @return
   */
  ProxyInfoAvp createProxyInfo();

  /**
   * Create a ProxyInfo (Grouped AVP) instance, populating one AVP.
   * 
   * @param avp
   * @return
   */
  ProxyInfoAvp createProxyInfo(DiameterAvp avp);

  /**
   * Create a ProxyInfo (Grouped AVP) instance using the given array to populate the AVPs.
   * 
   * @param avps
   * @return
   */
  ProxyInfoAvp createProxyInfo(DiameterAvp[] avps);

  /**
   * Create a VendorSpecificApplicationId (Grouped AVP) instance using required AVP values.
   * 
   * @param vendorId
   * @return
   */
  VendorSpecificApplicationIdAvp createVendorSpecificApplicationId(long vendorId);

  /**
   * Create an empty VendorSpecificApplicationId (Grouped AVP) instance.
   * 
   * @return
   */
  VendorSpecificApplicationIdAvp createVendorSpecificApplicationId();

  /**
   * Create a VendorSpecificApplicationId (Grouped AVP) instance, populating one AVP.
   * 
   * @param avp
   * @return
   * @throws AvpNotAllowedException
   */
  VendorSpecificApplicationIdAvp createVendorSpecificApplicationId(DiameterAvp avp) throws AvpNotAllowedException;

  /**
   * Create a VendorSpecificApplicationId (Grouped AVP) instance using the given array to populate the AVPs.
   * 
   * @param avps
   * @return
   * @throws AvpNotAllowedException
   */
  VendorSpecificApplicationIdAvp createVendorSpecificApplicationId(DiameterAvp[] avps) throws AvpNotAllowedException;

  /**
   * Create an empty FailedAvp (Grouped AVP) instance.
   * 
   * @return
   */
  FailedAvp createFailedAvp();

  /**
   * Create a FailedAvp (Grouped AVP) instance, populating one AVP.
   * 
   * @param avp
   * @return
   */
  FailedAvp createFailedAvp(DiameterAvp avp);

  /**
   * Create a FailedAvp (Grouped AVP) instance using the given array to populate the AVPs.
   * 
   * @param avps
   * @return
   */
  FailedAvp createFailedAvp(DiameterAvp[] avps);

  /**
   * Create a ExperimentalResult (Grouped AVP) instance using required AVP values.
   * @param vendorId
   * @param experimentalResultCode
   * @return
   */
  ExperimentalResultAvp createExperimentalResult(long vendorId, long experimentalResultCode);

  /**
   * Create an empty ExperimentalResult (Grouped AVP) instance.
   * 
   * @return
   */
  ExperimentalResultAvp createExperimentalResult();

  /**
   * Create a ExperimentalResult (Grouped AVP) instance, populating one AVP.
   * 
   * @param avp
   * @return
   * @throws AvpNotAllowedException
   */
  ExperimentalResultAvp createExperimentalResult(DiameterAvp avp) throws AvpNotAllowedException;

  /**
   * Create a ExperimentalResult (Grouped AVP) instance using the given array to populate the AVPs.
   * 
   * @param avps
   * @return
   * @throws AvpNotAllowedException
   */
  ExperimentalResultAvp createExperimentalResult(DiameterAvp[] avps) throws AvpNotAllowedException;

}
