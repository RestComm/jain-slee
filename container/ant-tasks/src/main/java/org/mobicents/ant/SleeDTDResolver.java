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

package org.mobicents.ant;

import org.mobicents.util.DTDResolver;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
 
public class SleeDTDResolver extends DTDResolver {

    private static final String DTD_DIRECTORY = "dtd";

    public SleeDTDResolver() {
        super();
        try {        	
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.0//EN",getDTDURL("slee-deployable-unit_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN",getDTDURL("slee-sbb-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.0//EN",getDTDURL("slee-profile-spec-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.0//EN",getDTDURL("slee-event-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.0//EN",getDTDURL("slee-resource-adaptor-type-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.0//EN",getDTDURL("slee-resource-adaptor-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.0//EN",getDTDURL("slee-service-xml_1_0.dtd"));
        } catch(MalformedURLException e) {
            throw new RuntimeException("Caught MalformedURLException: "+e);
        }
    }

    private static URL getDTDURL(String dtdName) throws MalformedURLException {
        return new File(DTD_DIRECTORY+File.separator+dtdName).toURL();
    }
}
