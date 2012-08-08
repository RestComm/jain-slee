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

package org.mobicents.util;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import java.util.HashMap;
import java.io.InputStream;
import java.net.URL;

// Generic resource-lookup-based DTD resolver
public abstract class DTDResolver implements EntityResolver {
    private final HashMap resources = new HashMap();

    /**
     * Register a local resource as a substitute for an external entity reference..
     * @param publicID the public identitify of the external entity.
     * @param resourceURL the URL of the resource
     */
    protected void registerDTDURL(String publicID, URL resourceURL) {
        resources.put(publicID, resourceURL);
    }

    protected void registerDTDResource(String publicId, String resourcePath) {
        registerDTDURL(publicId, getClass().getClassLoader().getResource(resourcePath));
    }

    /**
     * Attempt to resolve an external identity.
     * @param publicId the public identifier of the external entity being referenced.
     * @param systemId the system identifier of the external entity being referenced.
     * @return an InputSource object describing the new input source, or null to request
     *        that the parser open a regular URI connection to the system identifier.
     */
    public InputSource resolveEntity(String publicId, String systemId) {
        URL resourceURL = (URL)resources.get(publicId);
        if (resourceURL != null) {
            InputStream resourceStream = null;
            try {
                resourceStream = resourceURL.openStream();
                InputSource is = new InputSource(resourceStream);
                is.setPublicId(publicId);
                is.setSystemId(resourceURL.toExternalForm());
                return is;
            }
            catch (Exception e) {
                try { if(resourceStream != null) resourceStream.close(); } catch (Exception e2) {}
            }
        }
        return null;
    }
}
