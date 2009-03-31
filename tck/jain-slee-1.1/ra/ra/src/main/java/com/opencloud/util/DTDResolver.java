/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.util;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

// Generic resource-lookup-based DTD resolver
public abstract class DTDResolver implements EntityResolver {
    private final HashMap resources = new HashMap();

    /**
     * Register a local resource as a substitute for an external entity reference..
     *
     * @param publicId    the public identitify of the external entity.
     * @param resourceURL the URL of the resource
     */
    protected void registerDTDURL(String publicId, URL resourceURL) {
        resources.put(publicId, resourceURL);
    }

    protected void registerDTDResource(String publicId, String resourcePath) {
        registerDTDURL(publicId, getClass().getClassLoader().getResource(resourcePath));
    }

    /**
     * Attempt to resolve an external identity.
     *
     * @param publicId the public identifier of the external entity being referenced.
     * @param systemId the system identifier of the external entity being referenced.
     * @return an InputSource object describing the new input source, or null to request
     *         that the parser open a regular URI connection to the system identifier.
     */
    public InputSource resolveEntity(String publicId, String systemId) {
        URL resourceURL = (URL) resources.get(publicId);
        if (resourceURL != null) {
            InputStream resourceStream = null;
            try {
                resourceStream = resourceURL.openStream();
                InputSource is = new InputSource(resourceStream);
                is.setPublicId(publicId);
                is.setSystemId(resourceURL.toExternalForm());
                return is;
            } catch (Exception e) {
                try {
                    if (resourceStream != null) resourceStream.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }
}
