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

package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.VCard;
import org.w3c.dom.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * vCard provider. 
 *
 * @author Gaston Dombiak
 */
public class VCardProvider implements IQProvider {

  public IQ parseIQ(XmlPullParser parser) throws Exception {
      StringBuffer sb = new StringBuffer();
      try {
          int event = parser.getEventType();
          // get the content
          while (true) {
              switch (event) {
                  case XmlPullParser.TEXT:
                      sb.append(parser.getText());
                      break;
                  case XmlPullParser.START_TAG:
                      sb.append('<').append(parser.getName()).append('>');
                      break;
                  case XmlPullParser.END_TAG:
                      sb.append("</").append(parser.getName()).append('>');
                      break;
                  default:
              }

              if (event == XmlPullParser.END_TAG && "vCard".equals(parser.getName())) break;

              event = parser.next();
          }
      } catch (XmlPullParserException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }

      String xmlText = sb.toString();
      return _createVCardFromXml(xmlText);
  }

    public static VCard _createVCardFromXml(String xmlText) {
      VCard vCard = new VCard();
      try {
          DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
          Document document = documentBuilder.parse(new ByteArrayInputStream(xmlText.getBytes()));

          new VCardReader(vCard, document).initializeFields();

      } catch (Exception e) {
          e.printStackTrace(System.err);
      }
      return vCard;
  }

    private static class VCardReader {
        private final VCard vCard;
        private final Document document;

        VCardReader(VCard vCard, Document document) {
            this.vCard = vCard;
            this.document = document;
        }

        public void initializeFields() {
            vCard.setFirstName(getTagContents("GIVEN"));
            vCard.setLastName(getTagContents("FAMILY"));
            vCard.setMiddleName(getTagContents("MIDDLE"));
            vCard.setEncodedImage(getTagContents("BINVAL"));

            setupEmails();

            vCard.setOrganization(getTagContents("ORGNAME"));
            vCard.setOrganizationUnit(getTagContents("ORGUNIT"));

            setupSimpleFields();

            setupPhones();
            setupAddresses();
        }

        private void setupEmails() {
            NodeList nodes = document.getElementsByTagName("USERID");
            if (nodes == null) return;
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                if ("HOME".equals(element.getParentNode().getFirstChild().getNodeName())) {
                    vCard.setEmailHome(getTextContent(element));
                } else {
                    vCard.setEmailWork(getTextContent(element));
                }
            }
        }

        private void setupPhones() {
            NodeList allPhones = document.getElementsByTagName("TEL");
            if (allPhones == null) return;
            for (int i = 0; i < allPhones.getLength(); i++) {
                NodeList nodes = allPhones.item(i).getChildNodes();
                String type = null;
                String code = null;
                String value = null;
                for (int j = 0; j < nodes.getLength(); j++) {
                    Node node = nodes.item(j);
                    if (node.getNodeType() != Node.ELEMENT_NODE) continue;
                    String nodeName = node.getNodeName();
                    if ("NUMBER".equals(nodeName)) {
                        value = getTextContent(node);
                    }
                    else if (isWorkHome(nodeName)) {
                        type = nodeName;
                    }
                    else {
                        code = nodeName;
                    }
                }
                if (code == null || value == null) continue;
                if ("HOME".equals(type)) {
                        vCard.setPhoneHome(code, value);
                    }
                else { // By default, setup work phone
                    vCard.setPhoneWork(code, value);
                }
            }
        }

        private boolean isWorkHome(String nodeName) {
            return "HOME".equals(nodeName) || "WORK".equals(nodeName);
        }

        private void setupAddresses() {
            NodeList allAddresses = document.getElementsByTagName("ADR");
            if (allAddresses == null) return;
            for (int i = 0; i < allAddresses.getLength(); i++) {
                Element addressNode = (Element) allAddresses.item(i);

                String type = null;
                List code = new ArrayList();
                List value = new ArrayList();
                NodeList childNodes = addressNode.getChildNodes();
                for(int j = 0; j < childNodes.getLength(); j++) {
                    Node node = childNodes.item(j);
                    if (node.getNodeType() != Node.ELEMENT_NODE) continue;
                    String nodeName = node.getNodeName();
                    if (isWorkHome(nodeName)) {
                        type = nodeName;
                            }
                            else {
                        code.add(nodeName);
                        value.add(getTextContent(node));
                            }
                        }
                for (int j = 0; j < value.size(); j++) {
                    if ("HOME".equals(type)) {
                        vCard.setAddressFieldHome((String) code.get(j), (String) value.get(j));
                    }
                    else { // By default, setup work address
                        vCard.setAddressFieldWork((String) code.get(j), (String) value.get(j));
                    }
                }
            }
        }

        private String getTagContents(String tag) {
            NodeList nodes = document.getElementsByTagName(tag);
            if (nodes != null && nodes.getLength() == 1) {
                return getTextContent(nodes.item(0));
            }
            return null;
        }

        private void setupSimpleFields() {
            NodeList childNodes = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node instanceof Element) {
                    Element element = (Element) node;

                    String field = element.getNodeName();
                    if (element.getChildNodes().getLength() == 0) {
                        vCard.setField(field, "");
                    } else if (element.getChildNodes().getLength() == 1 &&
                            element.getChildNodes().item(0) instanceof Text) {
                        vCard.setField(field, getTextContent(element));
                    }
                }
            }
        }

        private String getTextContent(Node node) {
            StringBuffer result = new StringBuffer();
            appendText(result, node);
            return result.toString();
        }

        private void appendText(StringBuffer result, Node node) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node nd = childNodes.item(i);
                String nodeValue = nd.getNodeValue();
                if (nodeValue != null) {
                    result.append(nodeValue);
                }
                appendText(result, nd);
            }
        }
    }
}
