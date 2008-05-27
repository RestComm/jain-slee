/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Dec 12, 2004
 *
 * ConnectorTest.java
 */
package org.mobicents.slee.test.connector;

import org.mobicents.slee.test.connector.ejb.ConnectorTestHome;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import junit.framework.TestCase;

/**
 * @author Tim
 * 
 */
public class ConnectorTest extends TestCase {

	public void testConnector() throws Throwable {
		    System.out.println("Starting testConnector()");

			Properties props = new Properties();

			props.put("java.naming.factory.initial",
					"org.jnp.interfaces.NamingContextFactory");
			props.put("java.naming.provider.url", "jnp://localhost:1099");
			props.put("java.naming.factory.url.pkgs",
					"org.jboss.naming:org.jnp.interfaces");

			InitialContext ctx = new InitialContext(props);

			System.out.println("Got context");

			Object ref = ctx.lookup("/slee-test-ejbs/ConnectorTestHome");

			System.out.println("Got reference:" + ref);

			ConnectorTestHome home = (ConnectorTestHome) PortableRemoteObject
					.narrow(ref, ConnectorTestHome.class);

			System.out.println("Narrowed home");

			org.mobicents.slee.test.connector.ejb.ConnectorTest connectorTest = home
					.create();

			System.out.println("Created ejb");

			if (!connectorTest.test1())
				fail("test1 failed");
			Thread.sleep(5000); // Enough time for the events to be processed
			if (!connectorTest.test2())
				fail("test2 failed");
			Thread.sleep(5000); // Enough time for the events to be processed
			if (!connectorTest.test3())
				fail("test3 failed");
			Thread.sleep(5000); // Enough time for the events to be processed
			if (!connectorTest.test4())
				fail("test4 failed");
			Thread.sleep(5000); // Enough time for the events to be processed
			if (!connectorTest.test5())
				fail("test5 failed");
			Thread.sleep(5000); // Enough time for the events to be processed
			if (!connectorTest.test6())
				fail("test5 failed");
			Thread.sleep(5000); // Enough time for the events to be processed
			if (!connectorTest.test7())
				fail("test5 failed");
			Thread.sleep(5000); // Enough time for the events to be processed

			System.out.println("Test finished");
		

	}
}
