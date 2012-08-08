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

package org.mobicents.slee.container.component.deployment.xml;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.logging.Logger;


import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * The class is used as a Default Entity Resolver when parsing slee XML files
 * such as deployment descriptors for example.
 * @author Emil Ivov
 * @author Tim Fox - refactored to use ClassLoader of slee
 */
public class DefaultSleeEntityResolver implements EntityResolver{

    private Hashtable resources = null;
    
    private static Logger log = Logger.getLogger(DefaultSleeEntityResolver.class.getName());
    
    private final ClassLoader sleeClassLoader;
    
    public DefaultSleeEntityResolver(ClassLoader sleeClassLoader) {
        this.sleeClassLoader = sleeClassLoader;
        
        resources = new Hashtable();
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.0//EN", "dtd/slee-deployable-unit_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN", "dtd/slee-sbb-jar_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.0//EN", "dtd/slee-service-xml_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.0//EN", "dtd/slee-resource-adaptor-type-jar_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.0//EN", "dtd/slee-resource-adaptor-jar_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.0//EN", "dtd/slee-profile-spec-jar_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.0//EN", "dtd/slee-event-jar_1_0.dtd");
        
        
        //We add this, cause JAXB supports XMLNode as source, we need to parse to determine which Context we want to use, 1.0 or 1.1
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.1//EN", "dtd/slee-deployable-unit_1_1.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.1//EN", "dtd/slee-sbb-jar_1_1.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.1//EN", "dtd/slee-service-xml_1_1.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.1//EN", "dtd/slee-resource-adaptor-type-jar_1_1.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.1//EN", "dtd/slee-resource-adaptor-jar_1_1.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.1//EN", "dtd/slee-profile-spec-jar_1_1.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.1//EN", "dtd/slee-event-jar_1_1.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Library 1.1//EN","dtd/slee-library-jar_1_1.dtd");
        
        // For SLEE 1.1 Extensions
        // TODO can't we have a single slee entity resolver, starting at deployer?
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Ext Library 1.1//EN", "dtd/slee-library-jar_1_1-ext.dtd");
        
    }

    /**
     * Adds a URL to the specified resource (as returned by the system classloader).
     * to the resource table of the resolver.
     * @param publicID the public id of the resource
     * @param resourceName the path (starting from a location in the class path)
     * and name of the dtd that should be used by the resolver for documents
     * with the specified public id.
     */
    private void registerResource(String publicID, String resourceName)
    {
        URL url = this.sleeClassLoader.getResource(resourceName);
        if (url != null) {
            resources.put(publicID, url);
        }
        else {
            //All the slee dtds should be packaged locally in sar of slee itself
            throw new IllegalStateException("Cannot find resource:" + resourceName);
        }
    }

    /**
     * Creates an InputSource with a SystemID corresponding to a local dtd file.
     * @param publicId The public identifier of the external entity
     *        being referenced, or null if none was supplied.
     * @param systemId The system identifier of the external entity
     *        being referenced (This is a dummy parameter and is overridden by
     *        the resource names earlier specified by the
     *        <code>registrerResource</code>) method to correspond to the publicID.
     * @return An InputSource object describing the new input source,
     *         or null to request that the parser open a regular
     *         URI connection to the system identifier.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @exception java.io.IOException A Java-specific IO exception,
     *            possibly the result of creating a new InputStream
     *            or Reader for the InputSource.
     */
    public InputSource resolveEntity(String publicId, String systemId)
        throws IOException
    {
    	if (publicId != null) {
			URL resourceURL = (URL) resources.get(publicId);
			if (resourceURL != null) {
				InputStream resourceStream = null;
				resourceStream = resourceURL.openStream();
				InputSource is = new InputSource(resourceStream);
				is.setPublicId(publicId);
				is.setSystemId(resourceURL.toExternalForm());
				return is;
			}
		}
		return null;
	}

}
