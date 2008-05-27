/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ConcreteActivityContextInterfaceGeneratorTest.java
 *
 * Created on Aug 20, 2004
 *
 */
package org.mobicents.slee.container.deployment;

import javax.slee.ActivityContextInterface;
import javax.slee.management.DeploymentException;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.jboss.logging.Log4jLoggerPlugin;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.CMPField;
import org.mobicents.slee.container.component.MobicentsSbbDescriptorInternalImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public class ConcreteActivityContextInterfaceGeneratorTest extends TestCase {
    MobicentsSbbDescriptor sbbDeploymentDescriptor=null;
    ConcreteActivityContextInterfaceGenerator concreteActivityContextInterfaceGenerator=null;
    
    Logger logger = Logger.getLogger(ConcreteActivityContextInterfaceGeneratorTest.class.getName());

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
        Logger.setPluginClassName(Log4jLoggerPlugin.class.getName());
        new SbbDeployer("temp"); // TODO: is "temp" good enough?
        sbbDeploymentDescriptor=new MobicentsSbbDescriptorInternalImpl();
        CMPField[] cmpFields=new CMPField[3];
        cmpFields[0]=new CMPField("Counter",null);
        cmpFields[1]=new CMPField("CounterLastUpdated",null);
        cmpFields[2]=new CMPField("PeerFooSbb",null);
        sbbDeploymentDescriptor.setCMPFields(cmpFields);
        concreteActivityContextInterfaceGenerator=
            new ConcreteActivityContextInterfaceGenerator(sbbDeploymentDescriptor);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        sbbDeploymentDescriptor=null;
        concreteActivityContextInterfaceGenerator=null;
        super.tearDown();
    }

    /**
     * Constructor for ConcreteActivityContextInterfaceGeneratorTest.
     * @param arg0
     */
    public ConcreteActivityContextInterfaceGeneratorTest(String arg0) {
        super(arg0);
    }
    /**
     *
     *
     */
    public void testGenerateActivityContextConcreteClass() throws DeploymentException  {
        Class clazz=concreteActivityContextInterfaceGenerator.
            generateActivityContextInterfaceConcreteClass("org.mobicents.slee.container.deployment.FooSbbActivityContextInterface");
        ActivityContextInterface activityContextInterface=null;
        try {
            activityContextInterface=(ActivityContextInterface)clazz.newInstance();
        } catch (InstantiationException e2) {
            // Auto-generated catch block
            e2.printStackTrace();
        } catch (IllegalAccessException e2) {
            // Auto-generated catch block
            e2.printStackTrace();
        }
        ((FooSbbActivityContextInterface)activityContextInterface).setUserName("jean deruelle");
        String name=((FooSbbActivityContextInterface)activityContextInterface).getUserName();
        logger.info(name);
        assertNotNull("Failed creating ActivityContextInterace", activityContextInterface);
    }

}
