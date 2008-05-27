package org.mobicents.slee.container.component.deployment;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Emil Ivov
 * @version 1.0
 */

/*
* The Open SLEE project
*
* The source code contained in this file is in in the public domain.
* It can be used in any project or product without prior permission,
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/

import junit.framework.*;
import java.io.*;

import org.apache.log4j.BasicConfigurator;
import org.jboss.logging.Log4jLoggerPlugin;
import org.jboss.logging.Logger;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.mobicents.slee.container.component.MobicentsServiceDescriptorInternalImpl;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.container.management.xml.*;
import javax.xml.parsers.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Emil Ivov
 * @version 1.0
 */

public class ServiceDeploymentDescriptorParserTest extends TestCase {

    private ServiceDeploymentDescriptorParser  serviceDeploymentDescriptorParser = null;
    private Document                           serviceJarDocument    = null;
    private Element                            rootNode          = null;
    private Element                            serviceNode           = null;
    private ServiceDescriptorImpl                  serviceDescriptorImpl = null;

    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
        Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
        serviceDeploymentDescriptorParser = new ServiceDeploymentDescriptorParser();
        InputSource sbbJarSource = new InputSource(new StringReader(XMLDescriptorStringsFixture.SERVICE_JAR_XML));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        serviceJarDocument =  builder.parse(sbbJarSource);
        List serviceNodes = XMLUtils.getAllChildElements(serviceJarDocument.getDocumentElement(), "service");
        serviceNode = (Element)serviceNodes.get(0);

        serviceDescriptorImpl = new MobicentsServiceDescriptorInternalImpl();
    }

    protected void tearDown() throws Exception {
        serviceDeploymentDescriptorParser = null;
        super.tearDown();
    }

    public void testParseServiceComponent() throws IOException, Exception {

        serviceDescriptorImpl = serviceDeploymentDescriptorParser.parseServiceComponent(serviceNode, serviceDescriptorImpl);

        //---------- vendor name version
        assertEquals( "Error - service name",     serviceDescriptorImpl.getName(), "FooService");
        assertEquals( "Error - service vendor",   serviceDescriptorImpl.getVendor(),"com.foobar");
        assertEquals( "Error - service version ", serviceDescriptorImpl.getVersion(),"1.0");


        //-----------TODO fill in code for Root SBB once we are done setting it
        //assertEquals( "Error - sbb name",     rootSbb.getName(), "FooSbb");
        //assertEquals( "Error - sbb vendor",   rootSbb.getVendor(),"com.foobar");
        //assertEquals( "Error - sbb version ", rootSbb.getVersion(),"1.0");

        assertTrue("Error - default priority", serviceDescriptorImpl.getDefaultPriority() == 55);
        assertEquals("Error - AddressProfileTable", serviceDescriptorImpl.getAddressProfileTable(),  "MyAddressProfileTable");
        assertEquals("Error - ResourceInfoProfileTable", serviceDescriptorImpl.getResourceInfoProfileTable(), "MyResourceInfoProfileTable");


    }
}
