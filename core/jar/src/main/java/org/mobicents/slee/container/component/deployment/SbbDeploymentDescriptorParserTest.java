/*
* The Open SLEE project
*
* The source code contained in this file is in in the public domain.
* It can be used in any project or product without prior permission,
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/
package org.mobicents.slee.container.component.deployment;

import junit.framework.*;
import java.io.*;

import org.apache.log4j.BasicConfigurator;
import org.jboss.logging.Log4jLoggerPlugin;
import org.jboss.logging.Logger;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.mobicents.slee.container.component.MobicentsSbbDescriptorInternalImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbEventEntry;
import org.mobicents.slee.container.management.xml.*;
import javax.xml.parsers.*;
import java.util.*;
import org.mobicents.slee.container.*;

/**
 * @author Emil Ivov
 * @version 1.0
 */

public class SbbDeploymentDescriptorParserTest extends TestCase {

    private SbbDeploymentDescriptorParser sbbDeploymentDescriptorParser = null;
    private Document                           sbbJarDocument    = null;
    private Element                            rootNode          = null;
    private Element                            sbbNode           = null;
    private MobicentsSbbDescriptor                  mobicentsSbbDescriptor = null;

    private SbbDeploymentDescriptorParser sbbDeploymentDescriptorParser1 = null;
    private Document                           sbbJarDocument1    = null;
    private Element                            rootNode1          = null;
    private Element                            sbbNode1           = null;
    private MobicentsSbbDescriptor                  sbbDescriptorImpl1 = null;

    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
        Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
        sbbDeploymentDescriptorParser = new SbbDeploymentDescriptorParser();
        InputSource sbbJarSource = new InputSource(new StringReader(XMLDescriptorStringsFixture.SBB_JAR_XML));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        sbbJarDocument =  builder.parse(sbbJarSource);
        List sbbNodes = XMLUtils.getAllChildElements(sbbJarDocument.getDocumentElement(), "sbb");
        sbbNode = (Element)sbbNodes.get(0);

        mobicentsSbbDescriptor = new MobicentsSbbDescriptorInternalImpl(){
            public void setSbbAbstractClassName(String abstractClassName)
            {try {super.setSbbAbstractClassName(abstractClassName);}catch (Throwable ex) {}}
            public void setSbbLocalInterface(String localInterfaceName)
            {try {super.setSbbLocalInterfaceClassName(localInterfaceName);}catch (Throwable ex) {}}
        };

        sbbDeploymentDescriptorParser1 = new SbbDeploymentDescriptorParser();
        InputSource sbbJarSource1 = new InputSource(new StringReader(XMLDescriptorStringsFixture.SBB_JAR_XML1));

        sbbDescriptorImpl1 = new MobicentsSbbDescriptorInternalImpl();
        DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder1 = factory1.newDocumentBuilder();
        sbbJarDocument1 =  builder1.parse(sbbJarSource1);
        List sbbNodes1 = XMLUtils.getAllChildElements(sbbJarDocument1.getDocumentElement(), "sbb");
        sbbNode1 = (Element)sbbNodes1.get(0);


    }

    protected void tearDown() throws Exception {
        sbbDeploymentDescriptorParser = null;
        super.tearDown();
    }

    public void testParseSbbComponent() throws IOException, Exception {

        /**
        mobicentsSbbDescriptor = sbbDeploymentDescriptorParser.parseSbbComponent(sbbNode, mobicentsSbbDescriptor);

        //---------- vendor name version
        assertEquals( "Error - sbb name", mobicentsSbbDescriptor.getName(), "JCC Call Forwarding SBB");
        assertEquals( "Error - sbb vendor", mobicentsSbbDescriptor.getVendor(),"The Open Source Community");
        assertEquals( "Error - sbb version ", mobicentsSbbDescriptor.getVersion(),"1.0");
        **/

        //----------- abstract class name
        //assertTrue("Initial Event Types is not 1!",mobicentsSbbDescriptor.getInitialEventTypes().size()==1);
        /** @todo finish this */

        sbbDescriptorImpl1 = this.sbbDeploymentDescriptorParser1.parseSbbComponent(sbbNode1,(MobicentsSbbDescriptorInternalImpl) sbbDescriptorImpl1);
        SbbEventEntry sbbEventEntry = (SbbEventEntry) sbbDescriptorImpl1.getSbbEventEntries().iterator().next();

        assertTrue(sbbEventEntry.isInitial());
        assertTrue(sbbEventEntry.isFiredAndReceived());


    }
}
