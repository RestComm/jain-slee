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

package org.mobicents.slee.resource.diameter.base.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.java.slee.resource.diameter.base.events.DiameterCommand;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpType;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.DiameterURI;
import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.base.events.avp.IPFilterRule;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.RedirectHostUsageType;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;

import org.apache.log4j.Logger;
import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.Message;
import org.mobicents.diameter.dictionary.AvpDictionary;
import org.mobicents.diameter.dictionary.AvpRepresentation;
import org.mobicents.slee.resource.diameter.base.events.avp.DiameterAvpImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.FailedAvpImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ProxyInfoAvpImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvpImpl;

/**
 * Super class for all diameter messages <br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class DiameterMessageImpl implements DiameterMessage {

  private Logger log = Logger.getLogger(DiameterMessageImpl.class);

  protected Message message = null;

  /**
   * Constructor taking a jDiameter {@link Message} as argument.
   * 
   * @param message the jDiameter Message object to create the DiameterMessage from
   */
  public DiameterMessageImpl(Message message) {
    this.message = message;
  }

  // Begin of DiameterMessage Implementation

  /**
   * This method returns long name of this message type - Like
   * Device-Watchdog-Request
   * 
   * @return
   */
  public abstract String getLongName();

  /**
   * This method return short name of this message type - for instance DWR,DWA
   * for DeviceWatchdog message
   * 
   * @return
   */
  public abstract String getShortName();

  public DiameterHeader getHeader() {
    return new DiameterHeaderImpl(this.message);
  }

  public DiameterCommand getCommand() {
    return new DiameterCommandImpl(this.message.getCommandCode(), this.message.getApplicationId(), this.getShortName(), this.getLongName(), this.message.isRequest(), this.message.isProxiable());
  }

  public DiameterAvp[] getAvps() {
    DiameterAvp[] avps = new DiameterAvp[0];

    try {
      avps = getAvpsInternal(message.getAvps());
    }
    catch (Exception e) {
      log.error("Failed to obtain/decode AVP/data.", e);
    }

    return avps;
  }

  public DiameterAvp[] getExtensionAvps() {
    return getAvps();
  }

  public void setExtensionAvps(DiameterAvp... avps) throws AvpNotAllowedException {
    for (DiameterAvp a : avps) {
      this.addAvp(a);
    }
  }

  public Object clone() {
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException e) {
      return null;
    }
  }

  // AVP Getters and Setters

  public boolean hasSessionId() {
    return hasAvp(Avp.SESSION_ID);
  }

  public String getSessionId() {
    return getAvpAsUTF8String(Avp.SESSION_ID);
  }

  public void setSessionId(String sessionId) {
    addAvp(Avp.SESSION_ID, sessionId);
  }

  public boolean hasOriginHost() {
    return hasAvp(Avp.ORIGIN_HOST);
  }

  public DiameterIdentity getOriginHost() {
    return getAvpAsDiameterIdentity(Avp.ORIGIN_HOST);
  }

  public void setOriginHost(DiameterIdentity originHost) {
    addAvp(Avp.ORIGIN_HOST, originHost);
  }

  public boolean hasOriginRealm() {
    return hasAvp(Avp.ORIGIN_REALM);
  }

  public DiameterIdentity getOriginRealm() {
    return getAvpAsDiameterIdentity(Avp.ORIGIN_REALM);
  }

  public void setOriginRealm(DiameterIdentity originRealm) {
    addAvp(Avp.ORIGIN_REALM, originRealm);
  }

  public boolean hasDestinationHost() {
    return hasAvp(Avp.DESTINATION_HOST);
  }

  public DiameterIdentity getDestinationHost() {
    return getAvpAsDiameterIdentity(Avp.DESTINATION_HOST);
  }

  public void setDestinationHost(DiameterIdentity destinationHost) {
    addAvp(Avp.DESTINATION_HOST, destinationHost);
  }

  public boolean hasDestinationRealm() {
    return hasAvp(Avp.DESTINATION_REALM);
  }

  public DiameterIdentity getDestinationRealm() {
    return getAvpAsDiameterIdentity(Avp.DESTINATION_REALM);
  }

  public void setDestinationRealm(DiameterIdentity destinationRealm) {
    addAvp(Avp.DESTINATION_REALM, destinationRealm);
  }

  public boolean hasAcctApplicationId() {
    return hasAvp(Avp.ACCT_APPLICATION_ID);
  }

  public long getAcctApplicationId() {
    return getAvpAsUnsigned32(Avp.ACCT_APPLICATION_ID);
  }

  public void setAcctApplicationId(long acctApplicationId) {
    addAvp(Avp.ACCT_APPLICATION_ID, acctApplicationId);
  }

  public boolean hasAuthApplicationId() {
    return hasAvp(Avp.AUTH_APPLICATION_ID);
  }

  public long getAuthApplicationId() {
    return getAvpAsUnsigned32(Avp.AUTH_APPLICATION_ID);
  }

  public void setAuthApplicationId(long authApplicationId) {
    addAvp(Avp.AUTH_APPLICATION_ID, authApplicationId);
  }

  public boolean hasErrorMessage() {
    return hasAvp(Avp.ERROR_MESSAGE);
  }

  public String getErrorMessage() {
    return getAvpAsUTF8String(Avp.ERROR_MESSAGE);
  }

  public void setErrorMessage(String errorMessage) {
    addAvp(Avp.ERROR_MESSAGE, errorMessage);
  }

  public boolean hasErrorReportingHost() {
    return hasAvp(Avp.ERROR_REPORTING_HOST);
  }

  public DiameterIdentity getErrorReportingHost() {
    return getAvpAsDiameterIdentity(Avp.ERROR_REPORTING_HOST);
  }

  public void setErrorReportingHost(DiameterIdentity errorReportingHost) {
    addAvp(Avp.ERROR_REPORTING_HOST, errorReportingHost);
  }

  public boolean hasEventTimestamp() {
    return hasAvp(Avp.EVENT_TIMESTAMP);
  }

  public Date getEventTimestamp() {
    return getAvpAsTime(Avp.EVENT_TIMESTAMP);
  }

  public void setEventTimestamp(Date eventTimestamp) {
    addAvp(Avp.EVENT_TIMESTAMP, eventTimestamp);
  }

  public boolean hasOriginStateId() {
    return hasAvp(Avp.ORIGIN_STATE_ID);
  }

  public long getOriginStateId() {
    return getAvpAsUnsigned32(Avp.ORIGIN_STATE_ID);
  }

  public void setOriginStateId(long originStateId) {
    addAvp(Avp.ORIGIN_STATE_ID, originStateId);
  }

  public boolean hasResultCode() {
    return hasAvp(Avp.RESULT_CODE);
  }

  public long getResultCode() {
    return getAvpAsUnsigned32(Avp.RESULT_CODE);
  }

  public void setResultCode(long resultCode) {
    addAvp(Avp.RESULT_CODE, resultCode);
  }

  public boolean hasFailedAvp() {
    return hasAvp(Avp.FAILED_AVP);
  }

  public FailedAvp[] getFailedAvps() {
    return (FailedAvp[]) getAvpsAsCustom(Avp.FAILED_AVP, FailedAvpImpl.class);
  }

  public FailedAvp getFailedAvp() {
    return (FailedAvp) getAvpAsCustom(Avp.FAILED_AVP, FailedAvpImpl.class);
  }

  public void setFailedAvp(FailedAvp failedAvp) {
    addAvp(Avp.FAILED_AVP, failedAvp.byteArrayValue());
  }

  public void setFailedAvps(FailedAvp[] failedAvps) {
    DiameterAvp[] values = new DiameterAvp[failedAvps.length];

    for(int index = 0; index < failedAvps.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.FAILED_AVP, failedAvps[index].getExtensionAvps());
    }

    this.message.getAvps().removeAvp(Avp.FAILED_AVP);
    this.setExtensionAvps(values);
  }

  public boolean hasUserName() {
    return hasAvp(Avp.USER_NAME);
  }

  public String getUserName() {
    return getAvpAsUTF8String(Avp.USER_NAME);
  }

  public void setUserName(String userName) {
    addAvp(Avp.USER_NAME, userName);
  }

  public void setProxyInfo(ProxyInfoAvp proxyInfo) {
    addAvp(Avp.PROXY_INFO, proxyInfo.byteArrayValue());
  }

  public ProxyInfoAvp[] getProxyInfos() {
    return (ProxyInfoAvp[]) getAvpsAsCustom(Avp.PROXY_INFO, ProxyInfoAvpImpl.class);
  }

  public void setProxyInfos(ProxyInfoAvp[] proxyInfos) {
    DiameterAvp[] values = new DiameterAvp[proxyInfos.length];

    for(int index = 0; index < proxyInfos.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.PROXY_INFO, proxyInfos[index].getExtensionAvps());
    }

    this.message.getAvps().removeAvp(Avp.PROXY_INFO);
    this.setExtensionAvps(values);
  }

  public boolean hasRedirectHostUsage() {
    return hasAvp(Avp.REDIRECT_HOST_USAGE);
  }

  public void setRedirectHostUsage(RedirectHostUsageType redirectHostUsage) {
    addAvp(Avp.REDIRECT_HOST_USAGE, redirectHostUsage.getValue());
  }

  public RedirectHostUsageType getRedirectHostUsage() {
    return (RedirectHostUsageType) getAvpAsEnumerated(Avp.REDIRECT_HOST_USAGE, RedirectHostUsageType.class);
  }

  public boolean hasRedirectMaxCacheTime() {
    return hasAvp(Avp.REDIRECT_MAX_CACHE_TIME);
  }

  public void setRedirectMaxCacheTime(long redirectMaxCacheTime) {
    addAvp(Avp.REDIRECT_MAX_CACHE_TIME, redirectMaxCacheTime);
  }

  public long getRedirectMaxCacheTime() {
    return getAvpAsUnsigned32(Avp.REDIRECT_MAX_CACHE_TIME);
  }

  public boolean hasRedirectHosts() {
    return hasAvp(Avp.REDIRECT_HOST);
  }

  public DiameterURI[] getRedirectHosts() {
    return getAvpsAsDiameterURI(Avp.REDIRECT_HOST);
  }

  public void setRedirectHost(DiameterURI redirectHost) {
    addAvp(Avp.REDIRECT_HOST, redirectHost.toString());
  }

  public void setRedirectHosts(DiameterURI[] redirectHosts) {
    DiameterAvp[] values = new DiameterAvp[redirectHosts.length];

    for(int index = 0; index < redirectHosts.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.REDIRECT_HOST, redirectHosts[index].toString());
    }

    this.message.getAvps().removeAvp(Avp.REDIRECT_HOST);
    this.setExtensionAvps(values);
  }

  public DiameterIdentity[] getRouteRecords() {
    return getAvpsAsDiameterIdentity(Avp.ROUTE_RECORD);
  }

  public void setRouteRecord(DiameterIdentity routeRecord) {
    addAvp(Avp.ROUTE_RECORD, routeRecord);
  }

  public void setRouteRecords(DiameterIdentity[] routeRecords) {
    DiameterAvp[] values = new DiameterAvp[routeRecords.length];

    for(int index = 0; index < routeRecords.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.ROUTE_RECORD, routeRecords[index].toString());
    }

    this.message.getAvps().removeAvp(Avp.ROUTE_RECORD);
    this.setExtensionAvps(values);
  }

  public boolean hasVendorSpecificApplicationId() {
    return hasAvp(Avp.VENDOR_SPECIFIC_APPLICATION_ID);
  }

  public VendorSpecificApplicationIdAvp getVendorSpecificApplicationId() {
    return (VendorSpecificApplicationIdAvp) getAvpAsCustom( Avp.VENDOR_SPECIFIC_APPLICATION_ID, VendorSpecificApplicationIdAvpImpl.class );
  }

  public void setVendorSpecificApplicationId(VendorSpecificApplicationIdAvp vsaid) {
    addAvp(Avp.VENDOR_SPECIFIC_APPLICATION_ID, vsaid.byteArrayValue());
  }

  public Message getGenericData() {
    return message;
  }

  @Override
  public String toString() {
    DiameterHeader header = this.getHeader();

    StringBuilder sb = new StringBuilder();
    sb.append("\r\n");
    sb.append("+----------------------------------- HEADER ----------------------------------+\r\n");
    sb.append("| Version.................").append(header.getVersion()).append("\r\n");
    sb.append("| Message-Length..........").append(header.getMessageLength()).append("\r\n"); 
    sb.append("| Command-Flags...........").append("R[" + header.isRequest()).append("] P[").append(header.isProxiable()).append("] ").append("E[").append(header.isError()).append("] T[" + header.isPotentiallyRetransmitted()).append("]").append("\r\n");
    sb.append("| Command-Code............").append(this.getHeader().getCommandCode()).append("\r\n");
    sb.append("| Application-Id..........").append(this.getHeader().getApplicationId()).append("\r\n");
    sb.append("| Hop-By-Hop Identifier...").append(this.getHeader().getHopByHopId()).append("\r\n");
    sb.append("| End-To-End Identifier...").append(this.getHeader().getEndToEndId()).append("\r\n");
    sb.append("+------------------------------------ AVPs -----------------------------------+\r\n");

    for (Avp avp : this.getGenericData().getAvps()) {
      sb.append(printAvp(avp, ""));
    }
    sb.append("+-----------------------------------------------------------------------------+\r\n");

    return sb.toString();
  }

  // ===== AVP Management =====

  private DiameterAvp[] getAvpsInternal(AvpSet set) throws Exception {
    List<DiameterAvp> avps = new ArrayList<DiameterAvp>();

    for (Avp a : set) {
      AvpRepresentation avpRep = AvpDictionary.INSTANCE.getAvp(a.getCode(), a.getVendorId());

      if (avpRep == null) {
        //log.error("Avp with code: " + a.getCode() + " VendorId: " + a.getVendorId() + " is not listed in dictionary, skipping!");
        continue;
      }
      else {
        if (avpRep.getType().equals("Grouped")) {
          // TODO: There's no info about if AVP has mandatory or protected flags set...
          GroupedAvpImpl gAVP = new GroupedAvpImpl(a.getCode(), a.getVendorId(), avpRep.getRuleMandatoryAsInt(), avpRep.getRuleProtectedAsInt(), a.getRaw());

          gAVP.setExtensionAvps(getAvpsInternal(a.getGrouped()));

          // This is a grouped AVP... let's make it like that.
          avps.add(gAVP);
        }
        else {
          // TODO: There's no info about if AVP has mandatory or protected flags set...
          avps.add(new DiameterAvpImpl(a.getCode(), a.getVendorId(), avpRep.getRuleMandatoryAsInt(), avpRep.getRuleProtectedAsInt(), a.getRaw(), DiameterAvpType.fromString(avpRep.getType())));
        }
      }
    }

    return avps.toArray(new DiameterAvp[avps.size()]);
  }

  private String printAvp(Avp avp, String indent) {
    Object avpValue = null;
    StringBuilder avpStringSB = new StringBuilder();
    boolean isGrouped = false;

    try {
      String avpType = AvpDictionary.INSTANCE.getAvp(avp.getCode(), avp.getVendorId()).getType();

      if ("Integer32".equals(avpType) || "AppId".equals(avpType)) {
        avpValue = avp.getInteger32();
      }
      else if ("Unsigned32".equals(avpType) || "VendorId".equals(avpType)) {
        avpValue = avp.getUnsigned32();
      }
      else if ("Float64".equals(avpType)) {
        avpValue = avp.getFloat64();
      }
      else if ("Integer64".equals(avpType)) {
        avpValue = avp.getInteger64();
      }
      else if ("Time".equals(avpType)) {
        avpValue = avp.getTime();
      }
      else if ("Unsigned64".equals(avpType)) {
        avpValue = avp.getUnsigned64();
      }
      else if ("Grouped".equals(avpType)) {
        avpValue = "<Grouped>";
        isGrouped = true;
      }
      else {
        avpValue = avp.getUTF8String().replaceAll("\r", "").replaceAll("\n", "");
      }
    }
    catch (Exception ignore) {
      try {
        avpValue = avp.getUTF8String().replaceAll("\r", "").replaceAll("\n", "");
      }
      catch (AvpDataException e) {
        avpValue = avp.toString();
      }
    }

    avpStringSB.append("| ").append(indent).append("AVP: Code[").append(avp.getCode()).append("] VendorID[").append(avp.getVendorId()).append("] Value[").append(avpValue).
    append("] Flags[V=").append(avp.isVendorId()).append(";M=").append(avp.isMandatory()).append(";P=").append(avp.isEncrypted()).append("]\r\n");

    if (isGrouped) {
      try {
        for (Avp subAvp : avp.getGrouped()) {
          avpStringSB.append(printAvp(subAvp, indent + "  "));
        }
      }
      catch (AvpDataException e) {
        // Failed to ungroup... ignore then...
      }
    }

    return avpStringSB.toString();
  }

  protected void reportAvpFetchError(String msg, long code) {
    log.error("Failed to fetch avp, code: " + code + ". Message: " + msg);
  }

  // AVP Utilities Proxy Methods

  protected Date getAvpAsTime(int code) {
    return AvpUtilities.getAvpAsTime(code, message.getAvps());
  }

  protected Date getAvpAsTime(int code, long vendorId) {
    return AvpUtilities.getAvpAsTime(code, vendorId, message.getAvps());
  }

  protected Date[] getAvpsAsTime(int code) {
    return AvpUtilities.getAvpsAsTime(code, message.getAvps());
  }

  protected Date[] getAvpsAsTime(int code, long vendorId) {
    return AvpUtilities.getAvpsAsTime(code, vendorId, message.getAvps());
  }

  protected void setAvpAsTime(int code, long vendorId, Date value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsTime(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected float getAvpAsFloat32(int code) {
    return AvpUtilities.getAvpAsFloat32(code, message.getAvps());
  }

  protected float getAvpAsFloat32(int code, long vendorId) {
    return AvpUtilities.getAvpAsFloat32(code, vendorId, message.getAvps());
  }

  protected float[] getAvpsAsFloat32(int code) {
    return AvpUtilities.getAvpsAsFloat32(code, message.getAvps());
  }

  protected float[] getAvpsAsFloat32(int code, long vendorId) {
    return AvpUtilities.getAvpsAsFloat32(code, vendorId, message.getAvps());
  }

  protected void setAvpAsFloat32(int code, long vendorId, float value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsFloat32(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected double getAvpAsFloat64(int code) {
    return AvpUtilities.getAvpAsFloat64(code, message.getAvps());
  }

  protected double getAvpAsFloat64(int code, long vendorId) {
    return AvpUtilities.getAvpAsFloat64(code, vendorId, message.getAvps());
  }

  protected double[] getAvpsAsFloat64(int code) {
    return AvpUtilities.getAvpsAsFloat64(code, message.getAvps());
  }

  protected double[] getAvpsAsFloat64(int code, long vendorId) {
    return AvpUtilities.getAvpsAsFloat64(code, vendorId, message.getAvps());
  }

  protected void setAvpAsFloat64(int code, long vendorId, float value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsFloat64(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected byte[] getAvpAsGrouped(int code) {
    return AvpUtilities.getAvpAsGrouped(code, message.getAvps());
  }

  protected byte[] getAvpAsGrouped(int code, long vendorId) {
    return AvpUtilities.getAvpAsGrouped(code, vendorId, message.getAvps());
  }

  protected byte[][] getAvpsAsGrouped(int code) {
    return AvpUtilities.getAvpsAsGrouped(code, message.getAvps());
  }

  protected byte[][] getAvpsAsGrouped(int code, long vendorId) {
    return AvpUtilities.getAvpsAsGrouped(code, vendorId, message.getAvps());
  }

  protected AvpSet setAvpAsGrouped(int code, long vendorId, DiameterAvp[] childs, boolean isMandatory, boolean isProtected) {
    return AvpUtilities.setAvpAsGrouped(message, code, vendorId, message.getAvps(), isMandatory, isProtected, childs);
  }

  protected int getAvpAsInteger32(int code) {
    return AvpUtilities.getAvpAsInteger32(code, message.getAvps());
  }

  protected int getAvpAsInteger32(int code, long vendorId) {
    return AvpUtilities.getAvpAsInteger32(code, vendorId, message.getAvps());
  }

  protected int[] getAvpsAsInteger32(int code) {
    return AvpUtilities.getAvpsAsInteger32(code, message.getAvps());
  }

  protected int[] getAvpsAsInteger32(int code, long vendorId) {
    return AvpUtilities.getAvpsAsInteger32(code, vendorId, message.getAvps());
  }

  protected void setAvpAsInteger32(int code, long vendorId, int value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsInteger32(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected long getAvpAsInteger64(int code) {
    return AvpUtilities.getAvpAsInteger64(code, message.getAvps());
  }

  protected long getAvpAsInteger64(int code, long vendorId) {
    return AvpUtilities.getAvpAsInteger64(code, vendorId, message.getAvps());
  }

  protected long[] getAvpsAsInteger64(int code) {
    return AvpUtilities.getAvpsAsInteger64(code, message.getAvps());
  }

  protected long[] getAvpsAsInteger64(int code, long vendorId) {
    return AvpUtilities.getAvpsAsInteger64(code, vendorId, message.getAvps());
  }

  protected void setAvpAsInteger64(int code, long vendorId, long value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsInteger64(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected long getAvpAsUnsigned32(int code) {
    return AvpUtilities.getAvpAsUnsigned32(code, message.getAvps());
  }

  protected long getAvpAsUnsigned32(int code, long vendorId) {
    return AvpUtilities.getAvpAsUnsigned32(code, vendorId, message.getAvps());
  }

  protected long[] getAvpsAsUnsigned32(int code) {
    return AvpUtilities.getAvpsAsUnsigned32(code, message.getAvps());
  }

  protected long[] getAvpsAsUnsigned32(int code, long vendorId) {
    return AvpUtilities.getAvpsAsUnsigned32(code, vendorId, message.getAvps());
  }

  protected void setAvpAsUnsigned32(int code, long vendorId, long value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsUnsigned32(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected long getAvpAsUnsigned64(int code) {
    return AvpUtilities.getAvpAsUnsigned64(code, message.getAvps());
  }

  protected long getAvpAsUnsigned64(int code, long vendorId) {
    return AvpUtilities.getAvpAsUnsigned64(code, vendorId, message.getAvps());
  }

  protected long[] getAvpsAsUnsigned64(int code) {
    return AvpUtilities.getAvpsAsUnsigned64(code, message.getAvps());
  }

  protected long[] getAvpsAsUnsigned64(int code, long vendorId) {
    return AvpUtilities.getAvpsAsUnsigned64(code, vendorId, message.getAvps());
  }

  protected void setAvpAsUnsigned64(int code, long vendorId, long value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsUnsigned64(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected String getAvpAsUTF8String(int code) {
    return AvpUtilities.getAvpAsUTF8String(code, message.getAvps());
  }

  protected String getAvpAsUTF8String(int code, long vendorId) {
    return AvpUtilities.getAvpAsUTF8String(code, vendorId, message.getAvps());
  }

  protected String[] getAvpsAsUTF8String(int code) {
    return AvpUtilities.getAvpsAsUTF8String(code, message.getAvps());
  }

  protected String[] getAvpsAsUTF8String(int code, long vendorId) {
    return AvpUtilities.getAvpsAsUTF8String(code, vendorId, message.getAvps());
  }

  protected void setAvpAsUTF8String(int code, long vendorId, String value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsUTF8String(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected byte[] getAvpAsOctetString(int code) {
    return AvpUtilities.getAvpAsOctetString(code, message.getAvps());
  }

  protected byte[] getAvpAsOctetString(int code, long vendorId) {
    return AvpUtilities.getAvpAsOctetString(code, vendorId, message.getAvps());
  }

  protected byte[][] getAvpsAsOctetString(int code) {
    return AvpUtilities.getAvpsAsOctetString(code, message.getAvps());
  }

  protected byte[][] getAvpsAsOctetString(int code, long vendorId) {
    return AvpUtilities.getAvpsAsOctetString(code, vendorId, message.getAvps());
  }

  protected void setAvpAsOctetString(int code, long vendorId, String value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsOctetString(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected byte[] getAvpAsRaw(int code) {
    return AvpUtilities.getAvpAsRaw(code, message.getAvps());
  }

  protected byte[] getAvpAsRaw(int code, long vendorId) {
    return AvpUtilities.getAvpAsRaw(code, vendorId, message.getAvps());
  }

  protected byte[][] getAvpsAsRaw(int code) {
    return AvpUtilities.getAvpsAsRaw(code, message.getAvps());
  }

  protected byte[][] getAvpsAsRaw(int code, long vendorId) {
    return AvpUtilities.getAvpsAsRaw(code, vendorId, message.getAvps());
  }

  protected void setAvpAsRaw(int code, long vendorId, byte[] value, boolean isMandatory, boolean isProtected) {
    AvpUtilities.setAvpAsRaw(message, code, vendorId, message.getAvps(), isMandatory, isProtected, value);
  }

  protected Object getAvpAsCustom(int code, Class clazz) {
    return AvpUtilities.getAvpAsCustom(code, message.getAvps(), clazz);
  }

  protected Object getAvpAsCustom(int code, long vendorId, Class clazz) {
    return AvpUtilities.getAvpAsCustom(code, vendorId, message.getAvps(), clazz);
  }

  protected Object[] getAvpsAsCustom(int code, Class clazz) {
    return AvpUtilities.getAvpsAsCustom(code, message.getAvps(), clazz);
  }

  protected Object[] getAvpsAsCustom(int code, long vendorId, Class clazz) {
    return AvpUtilities.getAvpsAsCustom(code, vendorId, message.getAvps(), clazz);
  }

  protected DiameterIdentity getAvpAsDiameterIdentity(int code) {
    return AvpUtilities.getAvpAsDiameterIdentity(code, message.getAvps());
  }

  protected DiameterIdentity getAvpAsDiameterIdentity(int code, long vendorId) {
    return AvpUtilities.getAvpAsDiameterIdentity(code, vendorId, message.getAvps());
  }

  protected DiameterIdentity[] getAvpsAsDiameterIdentity(int code) {
    return AvpUtilities.getAvpsAsDiameterIdentity(code, message.getAvps());
  }

  protected DiameterIdentity[] getAvpsAsDiameterIdentity(int code, long vendorId) {
    return AvpUtilities.getAvpsAsDiameterIdentity(code, vendorId, message.getAvps());
  }

  protected DiameterURI getAvpAsDiameterURI(int code) {
    return AvpUtilities.getAvpAsDiameterURI(code, message.getAvps());
  }

  protected DiameterURI getAvpAsDiameterURI(int code, long vendorId) {
    return AvpUtilities.getAvpAsDiameterURI(code, vendorId, message.getAvps());
  }

  protected DiameterURI[] getAvpsAsDiameterURI(int code) {
    return AvpUtilities.getAvpsAsDiameterURI(code, message.getAvps());
  }

  protected DiameterURI[] getAvpsAsDiameterURI(int code, long vendorId) {
    return AvpUtilities.getAvpsAsDiameterURI(code, vendorId, message.getAvps());
  }

  protected Address getAvpAsAddress(int code) {
    return AvpUtilities.getAvpAsAddress(code, message.getAvps());
  }

  protected Address getAvpAsAddress(int code, long vendorId) {
    return AvpUtilities.getAvpAsAddress(code, vendorId, message.getAvps());
  }

  protected Address[] getAvpsAsAddress(int code) {
    return AvpUtilities.getAvpsAsAddress(code, message.getAvps());
  }

  protected Address[] getAvpsAsAddress(int code, long vendorId) {
    return AvpUtilities.getAvpsAsAddress(code, vendorId, message.getAvps());
  }

  protected Object getAvpAsEnumerated(int code, Class clazz) {
    return AvpUtilities.getAvpAsEnumerated(code, message.getAvps(), clazz);
  }

  protected Object getAvpAsEnumerated(int code, long vendorId, Class clazz) {
    return AvpUtilities.getAvpAsEnumerated(code, vendorId, message.getAvps(), clazz);
  }

  protected Object[] getAvpsAsEnumerated(int code, Class clazz) {
    return AvpUtilities.getAvpsAsEnumerated(code, message.getAvps(), clazz);
  }

  protected Object[] getAvpsAsEnumerated(int code, long vendorId, Class clazz) {
    return AvpUtilities.getAvpsAsEnumerated(code, vendorId, message.getAvps(), clazz);
  }

  protected IPFilterRule getAvpAsIPFilterRule(int code) {
    return AvpUtilities.getAvpAsIPFilterRule(code, message.getAvps());
  }

  protected IPFilterRule getAvpAsIPFilterRule(int code, long vendorId) {
    return AvpUtilities.getAvpAsIPFilterRule(code, vendorId, message.getAvps());
  }

  protected IPFilterRule[] getAvpsAsIPFilterRule(int code) {
    return AvpUtilities.getAvpsAsIPFilterRule(code, message.getAvps());
  }

  protected IPFilterRule[] getAvpsAsIPFilterRule(int code, long vendorId) {
    return AvpUtilities.getAvpsAsIPFilterRule(code, vendorId, message.getAvps());
  }

  protected void addAvp(String avpName, Object avp) {
    AvpUtilities.addAvp(message, avpName, message.getAvps(), avp);
  }

  protected void addAvp(int avpCode, Object avp) {
    AvpUtilities.addAvp(message, avpCode, 0L, message.getAvps(), avp);
  }

  protected void addAvp(int avpCode, long vendorId, Object avp) {
    AvpUtilities.addAvp(message, avpCode, vendorId, message.getAvps(), avp);
  }

  protected boolean hasAvp(int code) {
    return AvpUtilities.hasAvp(code, 0L, message.getAvps());
  }

  protected boolean hasAvp(int code, long vendorId) {
    return AvpUtilities.hasAvp(code, vendorId, message.getAvps());
  }

  protected Object getAvp(int avpCode) {
    return getAvp(avpCode, 0L);
  }

  protected Object getAvp(String avpName) {
    AvpRepresentation avpRep = AvpDictionary.INSTANCE.getAvp(avpName);

    if(avpRep != null) {
      return getAvp(avpRep.getCode(), avpRep.getVendorId());
    }

    return null;
  }

  protected Object getAvp(int avpCode, long vendorId) {
    AvpRepresentation avpRep = AvpDictionary.INSTANCE.getAvp(avpCode, vendorId);

    if(avpRep != null)
    {
      int avpType = AvpRepresentation.Type.valueOf(avpRep.getType()).ordinal();

      switch (avpType)
      {
        case DiameterAvpType._ADDRESS:
        case DiameterAvpType._DIAMETER_IDENTITY:
        case DiameterAvpType._DIAMETER_URI:
        case DiameterAvpType._IP_FILTER_RULE:
        case DiameterAvpType._OCTET_STRING:
        case DiameterAvpType._QOS_FILTER_RULE:
        {
          return getAvpAsOctetString(avpCode, vendorId);
        }
        case DiameterAvpType._ENUMERATED:
        case DiameterAvpType._INTEGER_32:
        {
          return getAvpAsInteger32(avpCode, vendorId);        
        }
        case DiameterAvpType._FLOAT_32:
        {
          return getAvpAsFloat32(avpCode, vendorId);        
        }
        case DiameterAvpType._FLOAT_64:
        {
          return getAvpAsFloat64(avpCode, vendorId);        
        }
        case DiameterAvpType._GROUPED:
        {
          return getAvpAsGrouped(avpCode, vendorId);
        }
        case DiameterAvpType._INTEGER_64:
        {
          return getAvpAsInteger64(avpCode, vendorId);
        }
        case DiameterAvpType._TIME:
        {
          return getAvpAsTime(avpCode, vendorId);
        }
        case DiameterAvpType._UNSIGNED_32:
        {
          return getAvpAsUnsigned32(avpCode, vendorId);
        }
        case DiameterAvpType._UNSIGNED_64:
        {
          return getAvpAsUnsigned64(avpCode, vendorId);
        }
        case DiameterAvpType._UTF8_STRING:
        {
          return getAvpAsUTF8String(avpCode, vendorId);
        }
        default:
        {
          return getAvpAsRaw(avpCode, vendorId);
        }
      }
    }

    return null;
  }

  public void addAvp(DiameterAvp avp) {
    AvpUtilities.addAvp(avp, message.getAvps());
  }

  //some hack
  private transient Object data = null;

  public void setData(Object d) {
    this.data = d;
  }

  public Object removeData() {
    Object o = this.data;
    this.data = null;
    return o;
  }

  public Object getData() {
    return this.data;
  }

}
