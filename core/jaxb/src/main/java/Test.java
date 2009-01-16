package org.mobicents.slee.container.component.deployment.jaxb.slee11.du;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.management.xml.DefaultSleeEntityResolver;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;



public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			JAXBContext jc = JAXBContext.newInstance( "org.mobicents.slee.container.component.deployment.jaxb.slee11.du" );
			Unmarshaller u=jc.createUnmarshaller();
			
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         factory.setValidating(true);
	         DocumentBuilder builder = factory.newDocumentBuilder();
	           
	                builder.setEntityResolver(new EntityResolver(){
	                	
	                	
	                private Hashtable resources = null;

	                
	                private ClassLoader sleeClassLoader;
	                
	              
	                
	             
	                
	        

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
	                  
	                        InputStream resourceStream = null;
	                        resourceStream = new FileInputStream(new File("D:/java/jprojects/mobicents_trunk/servers/jain-slee/core/jaxb/du11/src/main/resources/slee-deployable-unit_1_1.dtd"));
	                        InputSource is = new InputSource(resourceStream);
	                        is.setPublicId(publicId);
	                        is.setSystemId(new URL("file://D:/java/jprojects/mobicents_trunk/servers/jain-slee/core/jaxb/du11/src/main/resources/slee-deployable-unit_1_1.dtd").toExternalForm());
	                        return is;
	                  
	                }});
	        

	            /** @todo define and set an error handler */
	            // builder.setErrorHandler(new ParseErrorHandler());
	            Document doc= builder.parse(new FileInputStream(new File("D:/java/jprojects/mobicents_trunk/servers/jain-slee/core/jaxb/jar/src/main/java/org/mobicents/slee/container/component/deployment/jaxb/slee11/du/deployable-unit.xml")));
			
			DeployableUnit du=(DeployableUnit) u.unmarshal(doc);
			System.out.println(Arrays.toString(du.getJarOrServiceXml().toArray()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
