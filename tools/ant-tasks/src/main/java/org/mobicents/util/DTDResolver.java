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
