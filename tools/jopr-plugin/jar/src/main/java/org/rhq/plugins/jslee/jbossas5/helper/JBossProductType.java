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

package org.rhq.plugins.jslee.jbossas5.helper;

import java.util.jar.Attributes;

/**
 * The product type of a JBoss application server installation - AS, EAP, or SOA.
 *
 * @author Jessica Sant
 * @author Ian Springer
 */
public enum JBossProductType {
    AS("JBoss AS", "JBoss Application Server", "default"), // the public offering
    EAP("JBoss EAP", "JBoss Enterprise Application Platform", "default"), // the customer offering
    SOA("JBoss SOA-P", "JBoss Enterprise SOA Platform", "production"); // the customer SOA platform

    public final String NAME;
    public final String DESCRIPTION;
    public final String DEFAULT_CONFIG_NAME;

    private static final String EAP_IMPLEMENTATION_TITLE = "JBoss [EAP]";
    private static final String SOA_IMPLEMENTATION_TITLE = "JBoss [SOA]";

    JBossProductType(String name, String description, String defaultConfigName) {
        this.NAME = name;
        this.DESCRIPTION = description;
        this.DEFAULT_CONFIG_NAME = defaultConfigName;
    }

    /**
     * Determines the product type (AS, EAP or SOA) based on the Implementation-Title MANIFEST.MF attribute.
     *
     * @param attributes the attributes from a manifest file (typically run.jar or jboss-j2ee.jar)
     * @return AS, EAP or SOA
     */
    public static JBossProductType determineJBossProductType(Attributes attributes) {
        JBossProductType result = JBossProductType.AS;
        String implementationTitle = (attributes != null) ? attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE)
            : null;
        if (implementationTitle != null) {
            if (implementationTitle.equalsIgnoreCase(EAP_IMPLEMENTATION_TITLE)) {
                result = JBossProductType.EAP;
            } else if (implementationTitle.equalsIgnoreCase(SOA_IMPLEMENTATION_TITLE)) {
                result = JBossProductType.SOA;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.NAME;
    }
}