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

package org.mobicents.slee.resource.diameter.gq;

import gov.nist.javax.sdp.fields.AttributeField;
import gov.nist.javax.sdp.fields.BandwidthField;

import java.util.Vector;

import javax.sdp.Media;
import javax.sdp.MediaDescription;
import javax.sdp.SdpException;
import javax.sdp.SdpFactory;
import javax.sdp.SdpParseException;
import javax.sdp.SessionDescription;

import sun.net.util.IPAddressUtil;

import net.java.slee.resource.diameter.base.events.avp.IPFilterRule;
import net.java.slee.resource.diameter.gq.GqAvpFactory;
import net.java.slee.resource.diameter.gq.GqProvider;
import net.java.slee.resource.diameter.gq.events.avp.FlowStatus;
import net.java.slee.resource.diameter.gq.events.avp.FlowUsage;
import net.java.slee.resource.diameter.gq.events.avp.MediaComponentDescription;
import net.java.slee.resource.diameter.gq.events.avp.MediaSubComponent;
import net.java.slee.resource.diameter.gq.events.avp.MediaType;


/**
 * Converts SDP to Gq' Media-Component-Description AVP.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V3.2.1) specification Annex B
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class SDPConverter {

  /**
   * Returns the value of the Media-Component-Description[] AVP, of type Grouped.
   */
  public static MediaComponentDescription[] convertSDP(GqProvider gqProvider, String sourceSDP, String destinationSDP) throws SdpException,
      SdpParseException {
    SdpFactory sdpFactory = SdpFactory.getInstance();
    SessionDescription sourceSessionDescription = sdpFactory.createSessionDescription(sourceSDP);
    SessionDescription destinationSessionDescription = sdpFactory.createSessionDescription(destinationSDP);
    Vector<MediaDescription> sourceMediaDescriptions = sourceSessionDescription.getMediaDescriptions(false);
    Vector<MediaDescription> destinationMediaDescriptions = destinationSessionDescription.getMediaDescriptions(false);
    Vector<AttributeField> attributes;
    Vector<BandwidthField> bandwidth;
    Vector<MediaComponentDescription> mediaComponentDescriptions = new Vector<MediaComponentDescription>();
    Vector<MediaSubComponent[]> mediaSubComponents = new Vector<MediaSubComponent[]>();

    GqAvpFactory avpFactory = gqProvider.getGqAvpFactory();
    MediaComponentDescription currDescription = null;
    Media currMedia;
    String mediaType;

    String codecLine;
    String mode;

    if (sourceMediaDescriptions == null)
      throw new SdpParseException(0, 0, "no source media found");

    if (destinationMediaDescriptions == null)
      throw new SdpParseException(0, 0, "no destination media found");

    String proto;
    int dataPort;
    int rtcpPort;
    int ports;
    int step;
    String dataAddress;
    String rtcpAddress;
    MediaSubComponent currSubComponent;
    IPFilterRule currFlowDescription;
    String[] rtcpValues;
    for (int i = 0; i < sourceMediaDescriptions.size(); i++) {
      attributes = sourceMediaDescriptions.get(i).getAttributes(false);
      bandwidth = sourceMediaDescriptions.get(i).getBandwidths(false);
      currMedia = sourceMediaDescriptions.get(i).getMedia();

      currDescription = avpFactory.createMediaComponentDescription();
      mediaType = currMedia.getMediaType().toLowerCase();

      if (mediaType.equals("audio"))
        currDescription.setMediaType(MediaType.AUDIO);
      else if (mediaType.equals("video"))
        currDescription.setMediaType(MediaType.VIDEO);
      else if (mediaType.equals("control"))
        currDescription.setMediaType(MediaType.CONTROL);
      else if (mediaType.equals("application"))
        currDescription.setMediaType(MediaType.APPLICATION);
      else if (mediaType.equals("message"))
        currDescription.setMediaType(MediaType.MESSAGE);
      else if (mediaType.equals("data"))
        currDescription.setMediaType(MediaType.DATA);
      else if (mediaType.equals("text"))
        currDescription.setMediaType(MediaType.TEXT);
      else
        currDescription.setMediaType(MediaType.OTHER);

      codecLine = "downlink" + System.getProperty("line.separator");
      codecLine = codecLine + "offer" + System.getProperty("line.separator");
      codecLine = codecLine + currMedia.toString() + System.getProperty("line.separator");

      mode = "";
      dataPort = currMedia.getMediaPort();
      rtcpPort = currMedia.getMediaPort() + 1;
      if (sourceMediaDescriptions.get(i).getConnection() != null)
        rtcpAddress = sourceMediaDescriptions.get(i).getConnection().getAddress();
      else
        rtcpAddress = sourceSessionDescription.getConnection().getAddress();

      dataAddress = rtcpAddress;
      proto = currMedia.getProtocol().toLowerCase();
      ports = 1;
      step = 2;
      if (currMedia.getPortCount() > 0)
        ports = currMedia.getPortCount();

      if (attributes != null)
        for (int j = 0; j < attributes.size(); j++)
          if (attributes.get(j).getName().equals("recvonly"))
            mode = "recvonly";
          else if (attributes.get(j).getName().equals("sendonly"))
            mode = "sendonly";
          else if (attributes.get(j).getName().equals("sendrecv"))
            mode = "sendrecv";
          else if (attributes.get(j).getName().equals("inactive"))
            mode = "inactive";
          else if (attributes.get(j).getName().equals("rtcp")) {
            rtcpValues = attributes.get(j).getValue().split(" ");
            rtcpPort = Integer.parseInt(rtcpValues[0]);
            if (rtcpPort != dataPort + 1)
              step = 1;

            if (rtcpValues.length == 3)
              rtcpAddress = rtcpValues[2];
          }
          else
            codecLine += attributes.get(j).toString() + System.getProperty("line.separator");

      if (proto.equals("rtp/avp")) {
        mediaSubComponents.add(new MediaSubComponent[ports * 2]);
        // 2 flows
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * ports);
            currFlowDescription = new IPFilterRule("permit in 17 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            mediaSubComponents.get(i)[0 + j * ports] = currSubComponent;
          }
        }

        for (int j = 0; j < ports; j++) {
          currSubComponent = avpFactory.createMediaSubComponent();
          currSubComponent.setFlowNumber(2 + j * ports);
          currSubComponent.setFlowUsage(FlowUsage.RTCP);
          currFlowDescription = new IPFilterRule("permit in 17 from " + rtcpAddress + " " + (rtcpPort + j * step) + " to any");
          currSubComponent.setFlowDescription(currFlowDescription);
          mediaSubComponents.get(i)[1 + j * ports] = currSubComponent;
        }
      }
      else if (proto.equals("udp")) {
        // 1 flow
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * ports);
            currFlowDescription = new IPFilterRule("permit in 17 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            mediaSubComponents.get(i)[0 + j * ports] = currSubComponent;
          }
        }
      }
      else if (proto.equals("rtp/avp-tcp") || proto.equals("tcp/rtp/avp")) {
        // 2 flows
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * ports);
            currFlowDescription = new IPFilterRule("permit in 6 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            mediaSubComponents.get(i)[0 + j * ports] = currSubComponent;
          }
        }

        for (int j = 0; j < ports; j++) {
          currSubComponent = avpFactory.createMediaSubComponent();
          currSubComponent.setFlowNumber(2 + j * ports);
          currSubComponent.setFlowUsage(FlowUsage.RTCP);
          currFlowDescription = new IPFilterRule("permit in 6 from " + rtcpAddress + " " + (rtcpPort + j * step) + " to any");
          currSubComponent.setFlowDescription(currFlowDescription);
          mediaSubComponents.get(i)[1 + j * ports] = currSubComponent;
        }
      }
      else if (proto.equals("tcp")) {
        // 1 flow
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * ports);
            currFlowDescription = new IPFilterRule("permit in 6 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            mediaSubComponents.get(i)[0 + j * ports] = currSubComponent;
          }
        }
      }
      else if (proto.equals("rtp/avp-dccp") || proto.equals("dccp/rtp/avp")) {
        // 2 flows
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * ports);
            currFlowDescription = new IPFilterRule("permit in 33 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            mediaSubComponents.get(i)[0 + j * ports] = currSubComponent;
          }
        }

        for (int j = 0; j < ports; j++) {
          currSubComponent = avpFactory.createMediaSubComponent();
          currSubComponent.setFlowNumber(2 + j * ports);
          currSubComponent.setFlowUsage(FlowUsage.RTCP);
          currFlowDescription = new IPFilterRule("permit in 33 from " + rtcpAddress + " " + (rtcpPort + j * step) + " to any");
          currSubComponent.setFlowDescription(currFlowDescription);
          mediaSubComponents.get(i)[1 + j * ports] = currSubComponent;
        }
      }
      else if (proto.equals("dccp")) {
        // 1 flow
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * ports);
            currFlowDescription = new IPFilterRule("permit in 33 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            mediaSubComponents.get(i)[0 + j * ports] = currSubComponent;
          }
        }
      }
      else
        throw new SdpParseException(0, 0, "protocol not supported " + proto);

      if (bandwidth != null)
        for (int j = 0; j < bandwidth.size(); j++)
          if (bandwidth.get(j).getType().equals("AS"))
            currDescription.setMaxRequestedBandwidthDL(bandwidth.get(j).getValue());
          else if (bandwidth.get(j).getType().equals("RR"))
            currDescription.setRRBandwidth(bandwidth.get(j).getValue());
          else if (bandwidth.get(j).getType().equals("RS"))
            currDescription.setRSBandwidth(bandwidth.get(j).getValue());
          else
            codecLine += bandwidth.get(j).toString() + System.getProperty("line.separator");

      currDescription.setCodecData(codecLine.getBytes());
      if (currMedia.getMediaPort() == 0)
        currDescription.setFlowStatus(FlowStatus.REMOVED);
      else if (mode.equals("recvonly"))
        currDescription.setFlowStatus(FlowStatus.ENABLED_DOWNLINK);
      else if (mode.equals("sendonly"))
        currDescription.setFlowStatus(FlowStatus.ENABLED_UPLINK);
      else if (mode.equals("inactive"))
        currDescription.setFlowStatus(FlowStatus.DISABLED);
      else
        currDescription.setFlowStatus(FlowStatus.ENABLED);

      currDescription.setMediaComponentNumber(i + 1);
      mediaComponentDescriptions.add(currDescription);
    }

    Boolean found = false;
    for (int i = 0; i < destinationMediaDescriptions.size(); i++) {
      found = false;
      currMedia = destinationMediaDescriptions.get(i).getMedia();
      mediaType = currMedia.getMediaType().toLowerCase();
      attributes = destinationMediaDescriptions.get(i).getAttributes(false);
      bandwidth = destinationMediaDescriptions.get(i).getBandwidths(false);

      codecLine = "uplink" + System.getProperty("line.separator");
      codecLine = codecLine + "answer" + System.getProperty("line.separator");
      codecLine = codecLine + currMedia.toString() + System.getProperty("line.separator");

      for (int j = 0; j < mediaComponentDescriptions.size(); j++)
        if (mediaComponentDescriptions.get(j).getMediaType().toString().toLowerCase().equals(mediaType)) {
          currDescription = mediaComponentDescriptions.get(j);
          found = true;
          break;
        }

      if (found) {
        dataPort = currMedia.getMediaPort();
        rtcpPort = currMedia.getMediaPort() + 1;
        if (destinationMediaDescriptions.get(i).getConnection() != null)
          rtcpAddress = destinationMediaDescriptions.get(i).getConnection().getAddress();
        else
          rtcpAddress = destinationSessionDescription.getConnection().getAddress();

        dataAddress = rtcpAddress;
        proto = currMedia.getProtocol().toLowerCase();
        ports = 1;
        step = 2;
        if (currMedia.getPortCount() > 0)
          ports = currMedia.getPortCount();

        mode = "";
        if (attributes != null)
          for (int j = 0; j < attributes.size(); j++)
            if (attributes.get(j).getName().equals("recvonly"))
              mode = "recvonly";
            else if (attributes.get(j).getName().equals("sendonly"))
              mode = "sendonly";
            else if (attributes.get(j).getName().equals("sendrecv"))
              mode = "sendrecv";
            else if (attributes.get(j).getName().equals("inactive"))
              mode = "inactive";
            else if (attributes.get(j).getName().equals("rtcp")) {
              rtcpValues = attributes.get(j).getValue().split(" ");
              rtcpPort = Integer.parseInt(rtcpValues[0]);
              if (rtcpPort != dataPort + 1)
                step = 1;

              if (rtcpValues.length == 3)
                rtcpAddress = rtcpValues[2];
            }
            else
              codecLine += attributes.get(j).toString() + System.getProperty("line.separator");

        if (proto.equals("rtp/avp")) {
          // 2 flows
          if (!mode.equals("sendonly")) {
            for (int j = 0; j < ports; j++) {
              currFlowDescription = new IPFilterRule("permit out 17 from any to " + dataAddress + " " + (dataPort + j * step));
              mediaSubComponents.get(i)[0 + j * ports].setFlowDescription(currFlowDescription);
            }
          }

          for (int j = 0; j < ports; j++) {
            currFlowDescription = new IPFilterRule("permit out 17 from any to " + rtcpAddress + " " + (rtcpPort + j * step));
            mediaSubComponents.get(i)[1 + j * ports].setFlowDescription(currFlowDescription);
          }
        }
        else if (proto.equals("udp")) {
          // 1 flow
          if (!mode.equals("sendonly")) {
            for (int j = 0; j < ports; j++) {
              currFlowDescription = new IPFilterRule("permit out 17 from any to " + dataAddress + " " + (dataPort + j * step));
              mediaSubComponents.get(i)[0 + j * ports].setFlowDescription(currFlowDescription);
            }
          }
        }
        else if (proto.equals("rtp/avp-tcp") || proto.equals("tcp/rtp/avp")) {
          // 2 flows
          if (!mode.equals("sendonly")) {
            for (int j = 0; j < ports; j++) {
              currFlowDescription = new IPFilterRule("permit out 6 from any to " + dataAddress + " " + (dataPort + j * step));
              mediaSubComponents.get(i)[0 + j * ports].setFlowDescription(currFlowDescription);
            }
          }

          for (int j = 0; j < ports; j++) {
            currFlowDescription = new IPFilterRule("permit out 6 from any to " + rtcpAddress + " " + (rtcpPort + j * step));
            mediaSubComponents.get(i)[1 + j * ports].setFlowDescription(currFlowDescription);
          }
        }
        else if (proto.equals("tcp")) {
          // 1 flow
          if (!mode.equals("sendonly")) {
            for (int j = 0; j < ports; j++) {
              currFlowDescription = new IPFilterRule("permit out 6 from any to " + dataAddress + " " + (dataPort + j * step));
              mediaSubComponents.get(i)[0 + j * ports].setFlowDescription(currFlowDescription);
            }
          }
        }
        else if (proto.equals("rtp/avp-dccp") || proto.equals("dccp/rtp/avp")) {
          // 2 flows
          if (!mode.equals("sendonly")) {
            for (int j = 0; j < ports; j++) {
              currFlowDescription = new IPFilterRule("permit out 33 from any to " + dataAddress + " " + (dataPort + j * step));
              mediaSubComponents.get(i)[0 + j * ports].setFlowDescription(currFlowDescription);
            }
          }

          for (int j = 0; j < ports; j++) {
            currFlowDescription = new IPFilterRule("permit out 33 from any to " + rtcpAddress + " " + (rtcpPort + j * step));
            mediaSubComponents.get(i)[1 + j * ports].setFlowDescription(currFlowDescription);
          }
        }
        else if (proto.equals("dccp")) {
          // 1 flow
          if (!mode.equals("sendonly")) {
            for (int j = 0; j < ports; j++) {
              currFlowDescription = new IPFilterRule("permit out 33 from any to " + dataAddress + " " + (dataPort + j * step));
              mediaSubComponents.get(i)[0 + j * ports].setFlowDescription(currFlowDescription);
            }
          }
        }
        else
          throw new SdpParseException(0, 0, "protocol not supported " + proto);

        if (bandwidth != null)
          for (int j = 0; j < bandwidth.size(); j++)
            if (bandwidth.get(j).getType().equals("AS"))
              currDescription.setMaxRequestedBandwidthUL(bandwidth.get(j).getValue());
            else if (bandwidth.get(j).getType().equals("RR")) {
              // do nothing
            }
            else if (bandwidth.get(j).getType().equals("RS")) {
              // do nothing
            }
            else
              codecLine += bandwidth.get(j).toString() + System.getProperty("line.separator");

        currDescription.setMediaSubComponents(mediaSubComponents.get(i));
        currDescription.setCodecData(codecLine.getBytes());
      }
    }

    return mediaComponentDescriptions.toArray(new MediaComponentDescription[mediaComponentDescriptions.size()]);
  }

  /**
   * Returns the value of the Media-Component-Description[] AVP, of type Grouped.
   */
  public static MediaComponentDescription[] convertSDP(GqProvider gqProvider, String sourceSDP) throws SdpException, SdpParseException {
    SdpFactory sdpFactory = SdpFactory.getInstance();
    SessionDescription sourceSessionDescription = sdpFactory.createSessionDescription(sourceSDP);
    Vector<MediaDescription> sourceMediaDescriptions = sourceSessionDescription.getMediaDescriptions(false);
    Vector<AttributeField> attributes;
    Vector<BandwidthField> bandwidth;
    Vector<MediaComponentDescription> mediaComponentDescriptions = new Vector<MediaComponentDescription>();

    GqAvpFactory avpFactory = gqProvider.getGqAvpFactory();
    MediaComponentDescription currDescription = null;
    Media currMedia;
    String mediaType;

    String codecLine;
    String mode;

    if (sourceMediaDescriptions == null)
      throw new SdpParseException(0, 0, "no source media found");

    String proto;
    int dataPort;
    int rtcpPort;
    int ports;
    int step;
    String dataAddress;
    String rtcpAddress;
    MediaSubComponent currSubComponent;
    IPFilterRule currFlowDescription;
    String[] rtcpValues;
    for (int i = 0; i < sourceMediaDescriptions.size(); i++) {
      attributes = sourceMediaDescriptions.get(i).getAttributes(false);
      bandwidth = sourceMediaDescriptions.get(i).getBandwidths(false);
      currMedia = sourceMediaDescriptions.get(i).getMedia();

      currDescription = avpFactory.createMediaComponentDescription();
      mediaType = currMedia.getMediaType().toLowerCase();

      if (mediaType.equals("audio"))
        currDescription.setMediaType(MediaType.AUDIO);
      else if (mediaType.equals("video"))
        currDescription.setMediaType(MediaType.VIDEO);
      else if (mediaType.equals("control"))
        currDescription.setMediaType(MediaType.CONTROL);
      else if (mediaType.equals("application"))
        currDescription.setMediaType(MediaType.APPLICATION);
      else if (mediaType.equals("message"))
        currDescription.setMediaType(MediaType.MESSAGE);
      else if (mediaType.equals("data"))
        currDescription.setMediaType(MediaType.DATA);
      else if (mediaType.equals("text"))
        currDescription.setMediaType(MediaType.TEXT);
      else
        currDescription.setMediaType(MediaType.OTHER);

      codecLine = "downlink" + System.getProperty("line.separator");
      codecLine = codecLine + "offer" + System.getProperty("line.separator");
      codecLine = codecLine + currMedia.toString() + System.getProperty("line.separator");

      mode = "";
      dataPort = currMedia.getMediaPort();
      rtcpPort = currMedia.getMediaPort() + 1;
      if (sourceMediaDescriptions.get(i).getConnection() != null)
        rtcpAddress = sourceMediaDescriptions.get(i).getConnection().getAddress();
      else
        rtcpAddress = sourceSessionDescription.getConnection().getAddress();

      dataAddress = rtcpAddress;
      proto = currMedia.getProtocol().toLowerCase();
      ports = 1;
      step = 2;
      if (currMedia.getPortCount() > 0)
        ports = currMedia.getPortCount();

      if (attributes != null)
        for (int j = 0; j < attributes.size(); j++)
          if (attributes.get(j).getName().equals("recvonly"))
            mode = "recvonly";
          else if (attributes.get(j).getName().equals("sendonly"))
            mode = "sendonly";
          else if (attributes.get(j).getName().equals("sendrecv"))
            mode = "sendrecv";
          else if (attributes.get(j).getName().equals("inactive"))
            mode = "inactive";
          else if (attributes.get(j).getName().equals("rtcp")) {
            rtcpValues = attributes.get(j).getValue().split(" ");
            rtcpPort = Integer.parseInt(rtcpValues[0]);
            if (rtcpPort != dataPort + 1)
              step = 1;

            if (rtcpValues.length == 3)
              rtcpAddress = rtcpValues[2];
          }
          else
            codecLine += attributes.get(j).toString() + System.getProperty("line.separator");

      if (proto.equals("rtp/avp")) {
        // 2 flows
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * step);
            currFlowDescription = new IPFilterRule("permit in 17 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            currDescription.setMediaSubComponent(currSubComponent);
          }
        }

        for (int j = 0; j < ports; j++) {
          currSubComponent = avpFactory.createMediaSubComponent();
          currSubComponent.setFlowNumber(2 + j * step);
          currSubComponent.setFlowUsage(FlowUsage.RTCP);
          currFlowDescription = new IPFilterRule("permit in 17 from " + rtcpAddress + " " + (rtcpPort + j * step) + " to any");
          currSubComponent.setFlowDescription(currFlowDescription);
          currDescription.setMediaSubComponent(currSubComponent);
        }
      }
      else if (proto.equals("udp")) {
        // 1 flow
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * step);
            currFlowDescription = new IPFilterRule("permit in 17 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            currDescription.setMediaSubComponent(currSubComponent);
          }
        }
      }
      else if (proto.equals("rtp/avp-tcp") || proto.equals("tcp/rtp/avp")) {
        // 2 flows
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * step);
            currFlowDescription = new IPFilterRule("permit in 6 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            currDescription.setMediaSubComponent(currSubComponent);
          }
        }

        for (int j = 0; j < ports; j++) {
          currSubComponent = avpFactory.createMediaSubComponent();
          currSubComponent.setFlowNumber(2 + j * step);
          currSubComponent.setFlowUsage(FlowUsage.RTCP);
          currFlowDescription = new IPFilterRule("permit in 6 from " + rtcpAddress + " " + (rtcpPort + j * step) + " to any");
          currSubComponent.setFlowDescription(currFlowDescription);
          currDescription.setMediaSubComponent(currSubComponent);
        }
      }
      else if (proto.equals("tcp")) {
        // 1 flow
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * step);
            currFlowDescription = new IPFilterRule("permit in 6 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            currDescription.setMediaSubComponent(currSubComponent);
          }
        }
      }
      else if (proto.equals("rtp/avp-dccp") || proto.equals("dccp/rtp/avp")) {
        // 2 flows
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * step);
            currFlowDescription = new IPFilterRule("permit in 33 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            currDescription.setMediaSubComponent(currSubComponent);
          }
        }

        for (int j = 0; j < ports; j++) {
          currSubComponent = avpFactory.createMediaSubComponent();
          currSubComponent.setFlowNumber(2 + j * step);
          currSubComponent.setFlowUsage(FlowUsage.RTCP);
          currFlowDescription = new IPFilterRule("permit in 33 from " + rtcpAddress + " " + (rtcpPort + j * step) + " to any");
          currSubComponent.setFlowDescription(currFlowDescription);
          currDescription.setMediaSubComponent(currSubComponent);
        }
      }
      else if (proto.equals("dccp")) {
        // 1 flow
        if (!mode.equals("recvonly")) {
          for (int j = 0; j < ports; j++) {
            currSubComponent = avpFactory.createMediaSubComponent();
            currSubComponent.setFlowNumber(1 + j * step);
            currFlowDescription = new IPFilterRule("permit in 33 from " + dataAddress + " " + (dataPort + j * step) + " to any");
            currSubComponent.setFlowDescription(currFlowDescription);
            currDescription.setMediaSubComponent(currSubComponent);
          }
        }
      }
      else
        throw new SdpParseException(0, 0, "protocol not supported " + proto);

      if (bandwidth != null)
        for (int j = 0; j < bandwidth.size(); j++)
          if (bandwidth.get(j).getType().equals("AS"))
            currDescription.setMaxRequestedBandwidthDL(bandwidth.get(j).getValue());
          else if (bandwidth.get(j).getType().equals("RR"))
            currDescription.setRRBandwidth(bandwidth.get(j).getValue());
          else if (bandwidth.get(j).getType().equals("RS"))
            currDescription.setRSBandwidth(bandwidth.get(j).getValue());
          else
            codecLine += bandwidth.get(j).toString() + System.getProperty("line.separator");

      currDescription.setCodecData(codecLine.getBytes());
      if (currMedia.getMediaPort() == 0)
        currDescription.setFlowStatus(FlowStatus.REMOVED);
      else if (mode.equals("recvonly"))
        currDescription.setFlowStatus(FlowStatus.ENABLED_DOWNLINK);
      else if (mode.equals("sendonly"))
        currDescription.setFlowStatus(FlowStatus.ENABLED_UPLINK);
      else if (mode.equals("inactive"))
        currDescription.setFlowStatus(FlowStatus.DISABLED);
      else
        currDescription.setFlowStatus(FlowStatus.ENABLED);

      currDescription.setMediaComponentNumber(i + 1);
      mediaComponentDescriptions.add(currDescription);
    }

    return mediaComponentDescriptions.toArray(new MediaComponentDescription[mediaComponentDescriptions.size()]);
  }

  public static SessionDescription convertMediaComponents(MediaComponentDescription[] data, Boolean source) {
    String result = new String();
    MediaSubComponent[] currSubComponents;
    IPFilterRule[] currRules;
    Boolean rtcpFound, dataFound;
    String originAddress = new String();

    for (int i = 0; i < data.length; i++) {
      if (source && data[i].getCodecData().length > 0)
        result += data[i].getCodecData()[0] + System.getProperty("line.separator");
      else if (!source && data[i].getCodecData().length > 1)
        result += data[i].getCodecData()[1] + System.getProperty("line.separator");

      if (data[i].hasFlowStatus()) {
        switch (data[i].getFlowStatus().getValue()) {
          case FlowStatus._ENABLED_UPLINK:
            if (source)
              result += "a=sendonly" + System.getProperty("line.separator");
            else
              result += "a=recvonly" + System.getProperty("line.separator");
            break;
          case FlowStatus._ENABLED_DOWNLINK:
            if (source)
              result += "a=recvonly" + System.getProperty("line.separator");
            else
              result += "a=sendonly" + System.getProperty("line.separator");
            break;
          case FlowStatus._ENABLED:
            result += "a=sendrecv" + System.getProperty("line.separator");
            break;
          case FlowStatus._DISABLED:
            result += "a=inactive" + System.getProperty("line.separator");
            break;
        }
      }

      if (data[i].hasMaxRequestedBandwidthDL() && source)
        result += "b=AS:" + data[i].getMaxRequestedBandwidthDL() + System.getProperty("line.separator");

      if (data[i].hasMaxRequestedBandwidthUL() && !source)
        result += "b=AS:" + data[i].getMaxRequestedBandwidthUL() + System.getProperty("line.separator");

      if (data[i].hasRRBandwidth())
        result += "b=RR:" + data[i].getRRBandwidth() + System.getProperty("line.separator");

      if (data[i].hasRSBandwidth())
        result += "b=RS:" + data[i].getRSBandwidth() + System.getProperty("line.separator");

      currSubComponents = data[i].getMediaSubComponents();

      rtcpFound = false;
      dataFound = false;
      for (int j = 0; j < currSubComponents.length; j++) {
        if (currSubComponents[j].hasFlowUsage() && currSubComponents[j].getFlowUsage().getValue() == FlowUsage._RTCP && !rtcpFound) {
          currRules = currSubComponents[j].getFlowDescriptions();
          for (int k = 0; k < currRules.length; k++)
            if (currRules[k].getDirection() == IPFilterRule.DIR_IN && source) {
              if (currRules[k].getSourcePorts().length > 0 && IPAddressUtil.isIPv4LiteralAddress(currRules[k].getSourceIp())) {
                result += "a=rtcp:" + currRules[k].getSourcePorts()[0][0] + " IN IP4 " + currRules[k].getSourceIp()
                    + System.getProperty("line.separator");
                rtcpFound = true;
              }
              else if (currRules[k].getSourcePorts().length > 0 && IPAddressUtil.isIPv6LiteralAddress(currRules[k].getSourceIp())) {
                result += "a=rtcp:" + currRules[k].getSourcePorts()[0][0] + " IN IP6 " + currRules[k].getSourceIp()
                    + System.getProperty("line.separator");
                rtcpFound = true;
              }
            }
            else if (currRules[k].getDirection() == IPFilterRule.DIR_OUT && !source) {
              if (currRules[k].getDestPorts().length > 0 && IPAddressUtil.isIPv4LiteralAddress(currRules[k].getDestIp())) {
                result += "a=rtcp:" + currRules[k].getDestPorts()[0][0] + " IN IP4 " + currRules[k].getDestIp()
                    + System.getProperty("line.separator");
                rtcpFound = true;
              }
              else if (currRules[k].getDestPorts().length > 0 && IPAddressUtil.isIPv6LiteralAddress(currRules[k].getDestIp())) {
                result += "a=rtcp:" + currRules[k].getDestPorts()[0][0] + " IN IP6 " + currRules[k].getDestIp()
                    + System.getProperty("line.separator");
                rtcpFound = true;
              }
            }
        }
        else if (!dataFound) {
          currRules = currSubComponents[j].getFlowDescriptions();
          for (int k = 0; k < currRules.length; k++)
            if (currRules[k].getDirection() == IPFilterRule.DIR_IN && source) {
              if (IPAddressUtil.isIPv4LiteralAddress(currRules[k].getSourceIp())) {
                originAddress = "IN IP4 " + currRules[k].getSourceIp();
                result += "c=IN IP4 " + currRules[k].getSourceIp() + System.getProperty("line.separator");
                dataFound = true;
              }
              else if (IPAddressUtil.isIPv6LiteralAddress(currRules[k].getSourceIp())) {
                originAddress = "IN IP6 " + currRules[k].getSourceIp();
                result += "c=IN IP6 " + currRules[k].getSourceIp() + System.getProperty("line.separator");
                dataFound = true;
              }
            }
            else if (currRules[k].getDirection() == IPFilterRule.DIR_OUT && !source) {
              if (IPAddressUtil.isIPv4LiteralAddress(currRules[k].getDestIp())) {
                originAddress = "IN IP4 " + currRules[k].getDestIp();
                result += "c=IN IP4 " + currRules[k].getDestIp() + System.getProperty("line.separator");
                dataFound = true;
              }
              else if (IPAddressUtil.isIPv6LiteralAddress(currRules[k].getSourceIp())) {
                originAddress = "IN IP6 " + currRules[k].getDestIp();
                result += "c=IN IP6 " + currRules[k].getDestIp() + System.getProperty("line.separator");
                dataFound = true;
              }
            }
        }
      }

    }

    SdpFactory sdpFactory = SdpFactory.getInstance();

    try {
      // those values are not transferred in gq and are required
      result = "o=- 1 1 " + originAddress + System.getProperty("line.separator") + result;
      result = "t=0 0" + System.getProperty("line.separator") + result;
      result = "s=Diameter GQ Parser" + System.getProperty("line.separator") + result;
      result = "v=0" + System.getProperty("line.separator") + result;
      SessionDescription resultDescription = sdpFactory.createSessionDescription(result);
      return resultDescription;
    }
    catch (Exception ex) {
      return null;
    }
  }
}
