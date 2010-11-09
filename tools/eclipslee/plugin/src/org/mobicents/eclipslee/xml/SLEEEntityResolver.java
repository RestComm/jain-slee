/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.mobicents.eclipslee.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


public class SLEEEntityResolver implements EntityResolver { 

	public static final int PUBLIC_ID = 0;
	public static final int SYSTEM_ID = 1;
	public static final int FILE = 2;
	public static final int DUMMY_FILE = 3;
	
	public static final String DIR = "/dtd/";
	
    public static final String catalogs [][] = {
        // JSLEE 1.0 definitions
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.0//EN", "http://java.sun.com/dtd/slee-event-jar_1_0.dtd", "slee-event-jar_1_0.dtd", "dummy-event-jar.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.0//EN", "http://java.sun.com/dtd/slee-profile-spec-jar_1_0.dtd", "slee-profile-spec-jar_1_0.dtd", "dummy-profile-spec-jar.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN", "http://java.sun.com/dtd/slee-sbb-jar_1_0.dtd", "slee-sbb-jar_1_0.dtd", "dummy-sbb-jar.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.0//EN", "http://java.sun.com/dtd/slee-deployable-unit_1_0.dtd", "slee-deployable-unit_1_0.dtd", "dummy-deployable-unit.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.0//EN", "http://java.sun.com/dtd/slee-resource-adaptor-type-jar_1_0.dtd", "slee-resource-adaptor-type-jar_1_0.dtd", "dummy-resource-adaptor-type-jar.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.0//EN", "http://java.sun.com/dtd/slee-resource-adaptor-jar_1_0.dtd", "slee-resource-adaptor-jar_1_0.dtd","dummy-resource-adaptor-jar.xml"},
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.0//EN", "http://java.sun.com/dtd/slee-service_1_0.dtd", "slee-service-xml_1_0.dtd", "dummy-service-xml.xml" },
        
        // JSLEE 1.1 definitions
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.1//EN", "http://java.sun.com/dtd/slee-event-jar_1_1.dtd", "slee-event-jar_1_1.dtd", "dummy-event-jar_1_1.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.1//EN", "http://java.sun.com/dtd/slee-profile-spec-jar_1_1.dtd", "slee-profile-spec-jar_1_1.dtd", "dummy-profile-spec-jar_1_1.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.1//EN", "http://java.sun.com/dtd/slee-sbb-jar_1_1.dtd", "slee-sbb-jar_1_1.dtd", "dummy-sbb-jar_1_1.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.1//EN", "http://java.sun.com/dtd/slee-deployable-unit_1_1.dtd", "slee-deployable-unit_1_1.dtd", "dummy-deployable-unit_1_1.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.1//EN", "http://java.sun.com/dtd/slee-resource-adaptor-type-jar_1_1.dtd", "slee-resource-adaptor-type-jar_1_1.dtd", "dummy-resource-adaptor-type-jar_1_1.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.1//EN", "http://java.sun.com/dtd/slee-resource-adaptor-jar_1_1.dtd", "slee-resource-adaptor-jar_1_1.dtd","dummy-resource-adaptor-jar_1_1.xml"},
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.1//EN", "http://java.sun.com/dtd/slee-service_1_1.dtd", "slee-service-xml_1_1.dtd", "dummy-service-xml_1_1.xml" },
        { "-//Sun Microsystems, Inc.//DTD JAIN SLEE Library 1.1//EN", "http://java.sun.com/dtd/slee-library-jar_1_1.dtd", "slee-library-jar_1_1.dtd", "dummy-library-jar_1_1.xml" },

        // Alcatel enhancements
        { "-//Alcatel OSP//DTD JAIN SLEE Resource Adaptor Instance 1.0//EN", "http://www.etb.bel.alcatel.be/dtd/resource-adaptor-instances-jar_1_0.dtd", "resource-adaptor-instances-jar_1_0.dtd", "dummy-resource-adaptor-instances.xml"}
        };

    public InputSource resolveEntity(String publicID, String systemID) {
        // get the jslee reference zip file

        for (int i = 0; i < catalogs.length; i++) {
            String catalog[] = catalogs[i];
            if ((publicID == null && catalog[SYSTEM_ID].equals(systemID))
                    || (systemID == null & catalog[PUBLIC_ID].equals(publicID))
                    || (catalog[SYSTEM_ID].equals(systemID) && catalog[PUBLIC_ID]
                            .equals(publicID))) {
                try {
                    InputStream stream = null;
                    IPath path = new Path(DIR + catalog[FILE]);
                    // get the jslee reference zip file
                    ZipFile zipFile_1_1 = ServiceCreationPlugin.getSleeAPI_1_1ZipFile();
                    
                    ZipEntry ze = zipFile_1_1.getEntry(path.toString().substring(1));
                    if (ze != null) {
                        stream = zipFile_1_1.getInputStream(ze);
                    }
                    else {
                        stream = FileLocator.openStream(ServiceCreationPlugin.getDefault().getBundle(), path, false);
                    }
                    return new InputSource(stream);
                } catch (IOException e) {
                    return null;
                }
            }
        }
        return null;
    }

    public static InputSource getEmptyXML(String publicID, String systemID) {
        for (int i = 0; i < catalogs.length; i++) {
            String catalog[] = catalogs[i];                     
            if ((publicID == null && catalog[SYSTEM_ID].equals(systemID))
                || (systemID == null & catalog[PUBLIC_ID].equals(publicID))
                || (catalog[SYSTEM_ID].equals(systemID) && catalog[PUBLIC_ID].equals(publicID)))
            	try {
            		return new InputSource(FileLocator.openStream(ServiceCreationPlugin.getDefault().getBundle(), new Path(DIR + catalog[DUMMY_FILE]), false));
            	} catch (IOException e) {
            		return null;
            	}
            }                
        return null;    	
    }
    
    public static IPath[] getDTDPaths() {
    	IPath paths[] = new IPath[catalogs.length];
    	for (int i = 0; i < paths.length; i++)
    		paths[i] = new Path(DIR + catalogs[i][FILE]);
    	return paths;
    }
} 
