/**
 * Start time:19:30:18 2009-01-15<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.File;
import java.net.URL;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit;

/**
 * This class it meant to test if JAXB context and marshalers work properly.
 * Start time:19:30:18 2009-01-15<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class JAXBBaseUtilityClassTest extends SuperTestCase {

	public JAXBBaseUtilityClassTest() {
		// TODO Auto-generated constructor stub
	}

	public void testUnmarshallerPesence() throws Exception {

		assertNotNull(this.testSubject.getUnmarshaller(false));
	}

	public void testGetDocment() throws Exception {
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				"xml/deployable-unit-template11.xml");
		assertNotNull("Didnt find file: xml/deployable-unit-template11.xml!!!",
				url);
		assertNotNull("Failed building document.", this.builder.parse(new File(
				url.toURI())));
	}

	public void test11DU() throws Exception {
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				"xml/deployable-unit-template11.xml");
		assertNotNull("Didnt find file: xml/deployable-unit-template11.xml!!!",
				url);

		Object o = this.testSubject.getUnmarshaller(false).unmarshal(
				this.builder.parse(new File(url.toURI())));

		assertNotNull("Failed on unmarshaling", o);

		assertTrue("Wrong unmarshall?", o instanceof DeployableUnit);
	}


}
