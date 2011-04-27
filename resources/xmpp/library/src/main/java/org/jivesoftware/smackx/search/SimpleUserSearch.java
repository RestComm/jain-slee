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

package org.jivesoftware.smackx.search;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ReportedData;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SimpleUserSearch is used to support the non-dataform type of JEP 55. This provides
 * the mechanism for allowing always type ReportedData to be returned by any search result,
 * regardless of the form of the data returned from the server.
 *
 * @author Derek DeMoro
 */
class SimpleUserSearch extends IQ {

    private Form form;
    private ReportedData data;

    public void setForm(Form form) {
        this.form = form;
    }

    public ReportedData getReportedData() {
        return data;
    }


    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"jabber:iq:search\">");
        buf.append(getItemsToSearch());
        buf.append("</query>");
        return buf.toString();
    }

    private String getItemsToSearch() {
        StringBuffer buf = new StringBuffer();

        if (form == null) {
            form = Form.getFormFrom(this);
        }

        if (form == null) {
            return "";
        }

        Iterator fields = form.getFields();
        while (fields.hasNext()) {
            FormField field = (FormField) fields.next();
            String name = field.getVariable();
            String value = getSingleValue(field);
            if (value.trim().length() > 0) {
                buf.append("<").append(name).append(">").append(value).append("</").append(name).append(">");
            }
        }

        return buf.toString();
    }

    private static String getSingleValue(FormField formField) {
        Iterator values = formField.getValues();
        while (values.hasNext()) {
            return (String) values.next();
        }
        return "";
    }

    protected void parseItems(XmlPullParser parser) throws Exception {
        ReportedData data = new ReportedData();
        data.addColumn(new ReportedData.Column("JID", "jid", "text-single"));

        boolean done = false;

        List fields = new ArrayList();
        while (!done) {
            if (parser.getAttributeCount() > 0) {
                String jid = parser.getAttributeValue("", "jid");
                List valueList = new ArrayList();
                valueList.add(jid);
                ReportedData.Field field = new ReportedData.Field("jid", valueList);
                fields.add(field);
            }

            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                fields = new ArrayList();
            }
            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("item")) {
                ReportedData.Row row = new ReportedData.Row(fields);
                data.addRow(row);
            }
            else if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                String value = parser.nextText();

                List valueList = new ArrayList();
                valueList.add(value);
                ReportedData.Field field = new ReportedData.Field(name, valueList);
                fields.add(field);

                boolean exists = false;
                Iterator cols = data.getColumns();
                while (cols.hasNext()) {
                    ReportedData.Column column = (ReportedData.Column) cols.next();
                    if (column.getVariable().equals(name)) {
                        exists = true;
                    }
                }

                // Column name should be the same
                if (!exists) {
                    ReportedData.Column column = new ReportedData.Column(name, name, "text-single");
                    data.addColumn(column);
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }

        }

        this.data = data;
    }


}
