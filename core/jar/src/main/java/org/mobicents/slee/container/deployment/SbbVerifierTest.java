/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * SbbVerifierTest.java
 *
 * Created on Aug 14, 2004
 *
 */
package org.mobicents.slee.container.deployment;

import org.mobicents.slee.container.component.MobicentsSbbDescriptorInternalImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.deployment.SbbDeploymentDescriptorParser;
import org.mobicents.slee.container.management.xml.XMLUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.BasicConfigurator;
import org.jboss.logging.Log4jLoggerPlugin;
import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public class SbbVerifierTest extends TestCase {
    private SbbDeploymentDescriptorParser sbbDeploymentDescriptorParser = null;
    private Document                           sbbJarDocument    = null;
    private Element                            rootNode          = null;
    private Element                            sbbNode           = null;
    private MobicentsSbbDescriptor                  mobicentsSbbDescriptor = null;

    Logger logger=null;
    public static String   SBB_JAR_XML =
        "<?xml version=\"1.0\"?>" + "\n"
       //+"<!DOCTYPE sbb-jar PUBLIC \"-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN\" \"http://java.sun.com/dtd/slee-sbb-jar_1_0.dtd\">" + "\n"
       +"<sbb-jar>" + "\n"
       +"    <sbb>" + "\n"
       +"        <description>Foo Sbba</description>" + "\n"
       +"        <sbb-name>Foo Sbb</sbb-name>" + "\n"
       +"        <sbb-vendor>NIST</sbb-vendor>" + "\n"
       +"        <sbb-version>1.0</sbb-version>" + "\n"
       +"\n"
       +"        <profile-spec-ref>" + "\n"
       +"            <profile-spec-name>CallForwardingProfile</profile-spec-name>" + "\n"
       +"            <profile-spec-vendor>The Open Source Community</profile-spec-vendor>" + "\n"
       +"            <profile-spec-version>1.0</profile-spec-version>" + "\n"
       +"            <profile-spec-alias>CFP</profile-spec-alias>" + "\n"
       +"        </profile-spec-ref>" + "\n"
       +"\n"
       +"        <sbb-ref>" + "\n"
       +"           <description></description>" + "\n"
       +"        	<sbb-name>Foo SBB</sbb-name>" + "\n"
       +"        	<sbb-vendor>NIST</sbb-vendor>" + "\n"
       +"        	<sbb-version> 1.0 </sbb-version>" + "\n"
       +"        	<sbb-alias>FooSBB</sbb-alias>" + "\n"
       +"        </sbb-ref>" + "\n"
       +"        <sbb-classes>" + "\n"
       +"            <sbb-abstract-class>" + "\n"
       +"                <sbb-abstract-class-name>org.mobicents.slee.container.deployment.FooSbb</sbb-abstract-class-name>" + "\n"
       +"                <get-profile-cmp-method>" + "\n"
       +"                    <profile-spec-alias-ref>CFP</profile-spec-alias-ref>" + "\n"
       +"                    <get-profile-cmp-method-name>getProfile</get-profile-cmp-method-name>" + "\n"
       +"                </get-profile-cmp-method>" + "\n"
       +"             		<cmp-field>" + "\n"
       +"             			<description>" + "\n"
       +"             				An integer counter." + "\n"
       +"             			</description>" + "\n"
       +"             			<cmp-field-name>counter</cmp-field-name>" + "\n"
       +"             		</cmp-field>" + "\n"
       +"             		<cmp-field>" + "\n"
       +"             			<description>" + "\n"
       +"             			The most recent time that the counter was updated." + "\n"
       +"             			</description>" + "\n"
       +"             			<cmp-field-name>counterLastUpdated</cmp-field-name>" + "\n"
       +"             		</cmp-field>" + "\n"
       +"             		<cmp-field>" + "\n"
       +"             			<description>" + "\n"
       +"             			Holds a reference to my peer FooSBB." + "\n"
       +"             			</description>" + "\n"
       +"             			<cmp-field-name>peerFooSbb</cmp-field-name>" + "\n"
       +"             		</cmp-field>" + "\n"
       +"             		<get-child-relation-method>" + "\n"
       +"             		       <description />" + "\n"
       +"             		       <sbb-alias-ref>FooSBB</sbb-alias-ref>" + "\n"
       +"             		<get-child-relation-method-name>getFooSbb</get-child-relation-method-name>" + "\n"
       +"             		<default-priority>1</default-priority>" + "\n"
       +"             		</get-child-relation-method>" + "\n"
       +"            </sbb-abstract-class>" + "\n"
       +"                   <sbb-activity-context-interface>" + "\n"
       +"                   <description />" + "\n"
       +"                   <sbb-activity-context-interface-name>org.mobicents.slee.container.deployment.FooSbbActivityContextInterface</sbb-activity-context-interface-name>" + "\n"
       +"                   </sbb-activity-context-interface>" + "\n"
       +"        </sbb-classes>" + "\n"
       +"    	<event event-direction=\"Receive\">" + "\n"
       +"    		<description>" + "\n"
       +"    			This SBB fires events of the org.mobicents.slee.container.deployment.FooEvent.StartEvent" + "\n"
       +"    			event type and assigns StartEvent as the event name of" + "\n"
       +"    			this event type." + "\n"
       +"    		</description>" + "\n"
       +"    		<event-name>StartEvent</event-name>" + "\n"
       +"    		<event-type-ref>" + "\n"
       +"    		<event-type-name> org.mobicents.slee.container.deployment.FooEvent.StartEvent </event-type-name>" + "\n"
       +"    		<event-type-vendor> org.mobicents.slee.container.deployment.foobar </event-type-vendor>" + "\n"
       +"    		<event-type-version> 1.3a </event-type-version>" + "\n"
       +"    		</event-type-ref>" + "\n"
       +"    	</event>" + "\n"
       +"    </sbb>" + "\n"
       +"</sbb-jar>" + "\n";

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        try{
            BasicConfigurator.configure();
            Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
            logger = Logger.getLogger(SbbVerifierTest.class);
            sbbDeploymentDescriptorParser = new SbbDeploymentDescriptorParser();
            InputSource sbbJarSource = new InputSource(new StringReader(SBB_JAR_XML));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            sbbJarDocument =  builder.parse(sbbJarSource);
            List sbbNodes = XMLUtils.getAllChildElements(sbbJarDocument.getDocumentElement(), "sbb");
            sbbNode = (Element)sbbNodes.get(0);

            mobicentsSbbDescriptor = new MobicentsSbbDescriptorInternalImpl();
            mobicentsSbbDescriptor = sbbDeploymentDescriptorParser.parseSbbComponent(sbbNode, (MobicentsSbbDescriptorInternalImpl) mobicentsSbbDescriptor);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        mobicentsSbbDescriptor = null;
        super.tearDown();
    }

    /**
     * Constructor for SbbVerifierTest.
     * @param name
     */
    public SbbVerifierTest(String name) {
        super(name);
    }

    public void testVerifySbbAbstractClass() {
        SbbVerifier sbbVerifier=new SbbVerifier(mobicentsSbbDescriptor);
        logger.info(mobicentsSbbDescriptor.getSbbAbstractClassName());
        boolean result=sbbVerifier.verifySbbAbstractClass(mobicentsSbbDescriptor.getSbbAbstractClassName(),null);
        assertEquals("Verification failed",true,result);
    }

    public void testVerifySbbConcreteClass() {
    }

}
