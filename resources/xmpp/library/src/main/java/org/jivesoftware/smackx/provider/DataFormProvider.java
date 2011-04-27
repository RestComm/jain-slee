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

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.packet.DataForm;
import org.xmlpull.v1.XmlPullParser;

/**
 * The DataFormProvider parses DataForm packets.
 * 
 * @author Gaston Dombiak
 */
public class DataFormProvider implements PacketExtensionProvider {

    /**
     * Creates a new DataFormProvider.
     * ProviderManager requires that every PacketExtensionProvider has a public, no-argument constructor
     */
    public DataFormProvider() {
    }

    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        boolean done = false;
        StringBuffer buffer = null;
        DataForm dataForm = new DataForm(parser.getAttributeValue("", "type"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("instructions")) { 
                    dataForm.addInstruction(parser.nextText());
                }
                else if (parser.getName().equals("title")) {                    
                    dataForm.setTitle(parser.nextText());
                }
                else if (parser.getName().equals("field")) {                    
                    dataForm.addField(parseField(parser));
                }
                else if (parser.getName().equals("item")) {                    
                    dataForm.addItem(parseItem(parser));
                }
                else if (parser.getName().equals("reported")) {                    
                    dataForm.setReportedData(parseReported(parser));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(dataForm.getElementName())) {
                    done = true;
                }
            }
        }
        return dataForm;
    }

    private FormField parseField(XmlPullParser parser) throws Exception {
        boolean done = false;
        FormField formField = new FormField(parser.getAttributeValue("", "var"));
        formField.setLabel(parser.getAttributeValue("", "label"));
        formField.setType(parser.getAttributeValue("", "type"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("desc")) { 
                    formField.setDescription(parser.nextText());
                }
                else if (parser.getName().equals("value")) {                    
                    formField.addValue(parser.nextText());
                }
                else if (parser.getName().equals("required")) {                    
                    formField.setRequired(true);
                }
                else if (parser.getName().equals("option")) {                    
                    formField.addOption(parseOption(parser));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("field")) {
                    done = true;
                }
            }
        }
        return formField;
    }

    private DataForm.Item parseItem(XmlPullParser parser) throws Exception {
        boolean done = false;
        List fields = new ArrayList();
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("field")) { 
                    fields.add(parseField(parser));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("item")) {
                    done = true;
                }
            }
        }
        return new DataForm.Item(fields);
    }

    private DataForm.ReportedData parseReported(XmlPullParser parser) throws Exception {
        boolean done = false;
        List fields = new ArrayList();
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("field")) { 
                    fields.add(parseField(parser));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("reported")) {
                    done = true;
                }
            }
        }
        return new DataForm.ReportedData(fields);
    }

    private FormField.Option parseOption(XmlPullParser parser) throws Exception {
        boolean done = false;
        FormField.Option option = null;
        String label = parser.getAttributeValue("", "label");
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("value")) {
                    option = new FormField.Option(label, parser.nextText());                     
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("option")) {
                    done = true;
                }
            }
        }
        return option;
    }
}
