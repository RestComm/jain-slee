/**
 * Start time:16:38:05 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.JAXBBaseUtilityClassTest;

import javassist.ClassPool;
import javassist.LoaderClassPath;
import junit.framework.TestCase;

/**
 * Start time:16:38:05 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ValidatorSuperTestCase extends JAXBBaseUtilityClassTest {

	protected ClassPool classPool=null;

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		this.classPool=new ClassPool();
		classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		this.classPool.clearImportedPackages();
		this.classPool=null;
	}
	
}
