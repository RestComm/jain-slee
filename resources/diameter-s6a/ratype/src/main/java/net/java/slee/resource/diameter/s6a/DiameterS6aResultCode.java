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

package net.java.slee.resource.diameter.s6a;

/**
 * Diameter S6a Result Codes
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DiameterS6aResultCode {

  /**
   * This result code shall be sent by the HSS to indicate that the user identified by the IMSI is
   * unknown.
   */
  public static final int DIAMETER_ERROR_USER_UNKNOWN = 5001;

  /**
   * This result code shall be sent by the HSS to indicate that no EPS subscription is associated
   * with the IMSI.
   */
  public static final int DIAMETER_ERROR_UNKNOWN_EPS_SUBSCRIPTION = 5420; 

  /**
   * This result code shall be sent by the HSS to indicate the RAT type the UE is using is not
   * allowed for the IMSI.
   */
  public static final int DIAMETER_ERROR_RAT_NOT_ALLOWED = 5421; 

  /**
   * This result code shall be sent by the HSS to indicate that the subscriber is not allowed to
   * roam within the MME or SGSN area.
   */
  public static final int DIAMETER_ERROR_ROAMING_NOT_ALLOWED = 5004; 

  /**
   * This result code shall be sent by the EIR to indicate that the mobile equipment is not known
   * in the EIR.
   */
  public static final int DIAMETER_ERROR_EQUIPMENT_UNKNOWN = 5422; 

  /**
   * This result code shall be sent by the HSS to indicate that a Notify command has been received
   * from a serving node which is not registered in HSS as the node currently serving the user.
   */
  public static final int DIAMETER_ERROR_UNKOWN_SERVING_NODE = 5423; 

  /**
   * This result code shall be sent by the HSS to indicate that an unexpectedly transient failure
   * occurs. The requesting node can try the request again in the future.
   */
  public static final int DIAMETER_AUTHENTICATION_DATA_UNAVAILABLE = 4181; 

}
