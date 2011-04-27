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

package net.java.slee.resource.diameter.sh.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * AVP representation of Data-Reference AVP. Defined in 3GPP TS 29.329 section
 * 6.3.4.<br>
 * The Data-Reference AVP is of type Enumerated, and indicates the type of the
 * requested user data in the operation UDR and SNR. Its exact values and
 * meaning is defined in 3GPP TS 29.328 [1]. The following values are defined
 * (more details are given in 3GPP TS 29.328 [1]):
 * <ul>
 * <li>RepositoryData (0)</li>
 * <li>IMSPublicIdentity (10)</li>
 * <li>IMSUserState (11)</li>
 * <li>S-CSCFName (12)</li>
 * <li>InitialFilterCriteria (13) - This value is used to request initial filter
 * criteria relevant to the requesting AS</li>
 * 
 * <li>LocationInformation (14)</li>
 * <li>UserState (15)</li>
 * <li>ChargingInformation (16)</li>
 * <li>MSISDN (17)</li>

 * </ul>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */

public class DataReferenceType implements Serializable, Enumerated {

  private static final long serialVersionUID = -8270968643651090042L;

  /**
   * Int value equal to one passed in UDR/SNR - it indicates that data is repository data - see TS29.328 for description
   */
  public static final int _REPOSITORY_DATA = 0;

  /**
   * Int value equal to one passed in UDR/SNR - it indicates that data is IMS user identity - see TS29.328 for description
   */
  public static final int _IMS_PUBLIC_IDENTITY = 10;

  /**
   * Int value equal to one passed in UDR/SNR - it indicates that data is state of IMS user - see TS29.328 for description
   */
  public static final int _IMS_USER_STATE = 11;

  /**
   * Int value equal to one passed in UDR/SNR - it indicates that data is service cscf name - see TS29.328 for description
   */
  public static final int _S_CSCFNAME = 12;

  /**
   * Int value equal to one passed in UDR/SNR - it indicates that data is filter criteria - see TS29.328 for description
   */
  public static final int _INITIAL_FILTER_CRITERIA = 13;

  /**
   * Int value equal to one passed in UDR/SNR - it indicates that data is location info - see TS29.328 for description
   */
  public static final int _LOCATION_INFORMATION = 14;

  /**
   * Int value equal to one passed in UDR/SNR - it indicates that data is user state - see TS29.328 for description
   */
  public static final int _USER_STATE = 15;

  /**
   * Int value equal to one passed in UDR/SNR - it indicates that data is charging information - see TS29.328 for description
   */
  public static final int _CHARGING_INFORMATION = 16;

  /**
   * Int value equal to one passed in UDR/SNR - it indicates that data is msisdn info - see TS29.328 for description
   */
  public static final int _MSISDN = 17;

  /**
   * 
   */
  public static final int _PSI_ACTIVATION = 18;

  /**
   * 
   */
  public static final int _DSAI = 19;

  /**
   * 
   */
  public static final int _SERVICE_LEVEL_TRACE_INFO = 21;

  /**
   * 
   */
  public static final int _IP_ADDRESS_SECURE_BINDING_INFORMATION = 22;

  /**
   * Singleton representation of {@link _REPOSITORY_DATA}
   */
  public static final DataReferenceType REPOSITORY_DATA = new DataReferenceType(_REPOSITORY_DATA);

  /**
   * Singleton representation of {@link _IMS_PUBLIC_IDENTITY}
   */
  public static final DataReferenceType IMS_PUBLIC_IDENTITY = new DataReferenceType(_IMS_PUBLIC_IDENTITY);

  /**
   * Singleton representation of {@link _IMS_USER_STATE}
   */
  public static final DataReferenceType IMS_USER_STATE = new DataReferenceType(_IMS_USER_STATE);

  /**
   * Singleton representation of {@link _S_CSCFNAME}
   */
  public static final DataReferenceType S_CSCFNAME = new DataReferenceType(_S_CSCFNAME);

  /**
   * Singleton representation of {@link _INITIAL_FILTER_CRITERIA}
   */
  public static final DataReferenceType INITIAL_FILTER_CRITERIA = new DataReferenceType(_INITIAL_FILTER_CRITERIA);

  /**
   * Singleton representation of {@link _LOCATION_INFORMATION}
   */
  public static final DataReferenceType LOCATION_INFORMATION = new DataReferenceType(_LOCATION_INFORMATION);

  /**
   * Singleton representation of {@link _USER_STATE}
   */
  public static final DataReferenceType USER_STATE = new DataReferenceType(_USER_STATE);

  /**
   * Singleton representation of {@link _CHARGING_INFORMATION}
   */
  public static final DataReferenceType CHARGING_INFORMATION = new DataReferenceType(_CHARGING_INFORMATION);

  /**
   * Singleton representation of {@link _MSISDN}
   */
  public static final DataReferenceType MSISDN = new DataReferenceType(_MSISDN);

  /**
   * Singleton representation of {@link _PSI_ACTIVATION}
   */
  public static final DataReferenceType PSI_ACTIVATION = new DataReferenceType(_PSI_ACTIVATION);

  /**
   * Singleton representation of {@link _DSAI}
   */
  public static final DataReferenceType DSAI = new DataReferenceType(_DSAI);

  /**
   * Singleton representation of {@link _SERVICE_LEVEL_TRACE_INFO}
   */
  public static final DataReferenceType SERVICE_LEVEL_TRACE_INFO = new DataReferenceType(_SERVICE_LEVEL_TRACE_INFO);

  /**
   * Singleton representation of {@link _IP_ADDRESS_SECURE_BINDING_INFORMATION}
   */
  public static final DataReferenceType IP_ADDRESS_SECURE_BINDING_INFORMATION = new DataReferenceType(_IP_ADDRESS_SECURE_BINDING_INFORMATION);

  private DataReferenceType(int value) {
    this.value = value;
  }

  public static DataReferenceType fromInt(int type) {
    switch (type) {
      case _REPOSITORY_DATA:
        return REPOSITORY_DATA;
      case _IMS_PUBLIC_IDENTITY:
        return IMS_PUBLIC_IDENTITY;
      case _IMS_USER_STATE:
        return IMS_USER_STATE;
      case _S_CSCFNAME:
        return S_CSCFNAME;
      case _INITIAL_FILTER_CRITERIA:
        return INITIAL_FILTER_CRITERIA;
      case _LOCATION_INFORMATION:
        return LOCATION_INFORMATION;
      case _USER_STATE:
        return USER_STATE;
      case _CHARGING_INFORMATION:
        return CHARGING_INFORMATION;
      case _MSISDN:
        return MSISDN;
      case _PSI_ACTIVATION:
        return PSI_ACTIVATION;
      case _DSAI:
        return DSAI;
      case _SERVICE_LEVEL_TRACE_INFO:
        return SERVICE_LEVEL_TRACE_INFO;
      case _IP_ADDRESS_SECURE_BINDING_INFORMATION:
        return IP_ADDRESS_SECURE_BINDING_INFORMATION;
      default:
        throw new IllegalArgumentException("Invalid DataReference value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch (value) {
      case _REPOSITORY_DATA:
        return "REPOSITORY_DATA";
      case _IMS_PUBLIC_IDENTITY:
        return "IMS_PUBLIC_IDENTITY";
      case _IMS_USER_STATE:
        return "IMS_USER_STATE";
      case _S_CSCFNAME:
        return "S_CSCFNAME";
      case _INITIAL_FILTER_CRITERIA:
        return "INITIAL_FILTER_CRITERIA";
      case _LOCATION_INFORMATION:
        return "LOCATION_INFORMATION";
      case _USER_STATE:
        return "USER_STATE";
      case _CHARGING_INFORMATION:
        return "CHARGING_INFORMATION";
      case _MSISDN:
        return "MSISDN";
      case _PSI_ACTIVATION:
        return "PSI_ACTIVATION";
      case _DSAI:
        return "DSAI";
      case _SERVICE_LEVEL_TRACE_INFO:
        return "SERVICE_LEVEL_TRACE_INFO";
      case _IP_ADDRESS_SECURE_BINDING_INFORMATION:
        return "IP_ADDRESS_SECURE_BINDING_INFORMATION";
      default:
        return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  private int value;
}
