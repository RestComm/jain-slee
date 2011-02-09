package org.mobicents.slee.container.deployment.jboss;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * Entity Resolver for JAIN SLEE DTDs used by Deployer 
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SLEEDeployerEntityResolver implements EntityResolver {

  private Hashtable<String, URL> resources = null;

  public static SLEEDeployerEntityResolver INSTANCE = new SLEEDeployerEntityResolver();

  private SLEEDeployerEntityResolver()
  {
    resources = new Hashtable<String, URL>();

    // For SLEE 1.0
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.0//EN", "dtd/slee-deployable-unit_1_0.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN", "dtd/slee-sbb-jar_1_0.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.0//EN", "dtd/slee-service-xml_1_0.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.0//EN", "dtd/slee-resource-adaptor-type-jar_1_0.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.0//EN", "dtd/slee-resource-adaptor-jar_1_0.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.0//EN", "dtd/slee-profile-spec-jar_1_0.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.0//EN", "dtd/slee-event-jar_1_0.dtd");

    // For SLEE 1.1
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.1//EN", "dtd/slee-deployable-unit_1_1.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.1//EN", "dtd/slee-sbb-jar_1_1.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.1//EN", "dtd/slee-service-xml_1_1.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.1//EN", "dtd/slee-resource-adaptor-type-jar_1_1.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.1//EN", "dtd/slee-resource-adaptor-jar_1_1.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.1//EN", "dtd/slee-profile-spec-jar_1_1.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.1//EN", "dtd/slee-event-jar_1_1.dtd");
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Library 1.1//EN", "dtd/slee-library-jar_1_1.dtd");
    
    // For SLEE 1.1 Extensions
    registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Ext Library 1.1//EN", "dtd/slee-library-jar_1_1-ext.dtd");
    
  }

  /**
   * Adds a URL to the specified resource (as returned by the system classloader) to the resource table of the resolver.
   * 
   * @param publicID the public id of the resource
   * @param resourceName the path (starting from a location in the class path) and name of the dtd that should be used by the resolver for documents
   *                      with the specified public id.
   */
  private void registerResource(String publicID, String resourceName)
  {
    URL url = this.getClass().getClassLoader().getResource(resourceName);

    if (url != null) {
      resources.put(publicID, url);
    }
    else {
      throw new IllegalStateException("Cannot find resource:" + resourceName);
    }
  }

  /**
   * Creates an InputSource with a SystemID corresponding to a local dtd file.
   * 
   * @param publicId The public identifier of the external entity being referenced, or null if none was supplied.
   * @param systemId The system identifier of the external entity being referenced (This is a dummy parameter and is overridden by the resource names
   *                  earlier specified by the <code>registrerResource</code>) method to correspond to the publicID.
   * @return An InputSource object describing the new input source, or null to request that the parser open a regular URI connection to the system identifier.
   * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping another exception.
   * @exception java.io.IOException A Java-specific IO exception, possibly the result of creating a new InputStream or Reader for the InputSource.
   */  
  public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
  {
    URL resourceURL = resources.get(publicId);

    if(resourceURL != null) {
      InputStream resourceStream = resourceURL.openStream();
      InputSource is = new InputSource(resourceStream);
      is.setPublicId(publicId);
      is.setSystemId(resourceURL.toExternalForm());
      return is;
    }

    return null;
  }
}
