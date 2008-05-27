/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ConcreteSbbGeneratorTest.java
 *
 * Created on Jul 31, 2004
 *
 */
package org.mobicents.slee.container.deployment;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.mobicents.slee.container.component.MobicentsSbbDescriptorInternalImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.deployment.SbbDeploymentDescriptorParser;
import org.mobicents.slee.container.management.xml.XMLUtils;

import javax.slee.Sbb;
import javax.slee.management.DeploymentException;
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
public class ConcreteSbbGeneratorTest extends TestCase {
    private Logger logger=null;
    private SbbDeploymentDescriptorParser sbbDeploymentDescriptorParser = null;
    private Document                           sbbJarDocument    = null;
    private Element                            rootNode          = null;
    private Element                            sbbNode           = null;
    private MobicentsSbbDescriptor                  mobicentsSbbDescriptor = null;
    ConcreteSbbGenerator concreteSbbGenerator=null;
    public static String   SBB_JAR_XML =
        "<?xml version=\"1.0\"?>" + "\n"
       //+"<!DOCTYPE sbb-jar PUBLIC \"-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN\" \"http://java.sun.com/dtd/slee-sbb-jar_1_0.dtd\">" + "\n"
       +"<sbb-jar>" + "\n"
       +"    <sbb>" + "\n"
       +"        <description>Foo Sbb</description>" + "\n"
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
       +"                	<get-profile-cmp-method>" + "\n"
       +"                   	<profile-spec-alias-ref>CFP</profile-spec-alias-ref>" + "\n"
       +"                    	<get-profile-cmp-method-name>getFooProfileCMP</get-profile-cmp-method-name>" + "\n"
       +"                	</get-profile-cmp-method>" + "\n"
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
       +"			<sbb-local-interface>" + "\n"
       +"				<sbb-local-interface-name>" + "\n"
       +					"org.mobicents.slee.container.deployment.FooSbbLocalObject" + "\n"
       +" 				</sbb-local-interface-name>" + "\n"
       +"			</sbb-local-interface>" + "\n"
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

    /**
     *
     */
    public ConcreteSbbGeneratorTest() {

    }

    /**
     * @param name
     */
    public ConcreteSbbGeneratorTest(String name) {
        super(name);
    }

    /**
     * Initialize the fixture.
     * @throws Exception if setup fails
     */
    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
        Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
        logger = Logger.getLogger(ConcreteSbbGeneratorTest.class);
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

    /**
     *
     */
    protected void tearDown() throws Exception{
        mobicentsSbbDescriptor=null;
        concreteSbbGenerator=null;
        super.tearDown();
    }
    /**
     * Test the generation of a class
     * @throws IllegalArgumentException
     */
    public void testGenerateClass() throws IllegalArgumentException, DeploymentException
    {
        concreteSbbGenerator=new ConcreteSbbGenerator(mobicentsSbbDescriptor);
        Class clazz = null;
        mobicentsSbbDescriptor.setSbbAbstractClassName("org.mobicents.slee.container.deployment.FooSbb");
        
        clazz = concreteSbbGenerator.generateConcreteSbb();
        
        Sbb sbb=null;
        try {
            sbb=(Sbb)clazz.newInstance();
        } catch (InstantiationException e2) {
            // Auto-generated catch block
            e2.printStackTrace();
        } catch (IllegalAccessException e2) {
            // Auto-generated catch block
            e2.printStackTrace();
        }
        //((FooSbb)sbb).setCounter(3);
        //((FooSbb)sbb).fireStartEvent(new FooEvent(),null,null);
        //logger.debug(""+((FooSbb)sbb).getCounter());
        assertNotNull("Failed creating Sbb", sbb);
    }
}
