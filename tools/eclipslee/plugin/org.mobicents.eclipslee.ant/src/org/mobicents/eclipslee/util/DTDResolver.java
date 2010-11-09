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

package org.mobicents.eclipslee.util;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import java.util.HashMap;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

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
        String localPath = resourcePath.substring(5);   // remove the slee/
        File localPathFile = new File(localPath);
        URL localURL = null;
        if (localPathFile.exists()) {
            try {
                localURL = localPathFile.toURI().toURL();
            }
            catch (Exception ex) {
                return;
            }
            registerDTDURL(publicId, localURL);
        }
        else {
            registerDTDURL(publicId, getClass().getClassLoader().getResource(resourcePath));
        }
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
                    resourceStream.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }
}
