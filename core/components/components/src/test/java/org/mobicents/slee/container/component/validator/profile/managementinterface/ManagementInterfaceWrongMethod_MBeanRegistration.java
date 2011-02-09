package org.mobicents.slee.container.component.validator.profile.managementinterface;

import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.slee.InvalidStateException;
import javax.slee.management.ManagementException;
import javax.slee.profile.ProfileVerificationException;

public interface ManagementInterfaceWrongMethod_MBeanRegistration {

	public void doSomeTricktMGMTMagic(String xxxx);
	public void dontLookAtMeImUglyDefinedMethodWithLongName(java.io.Serializable cheese);
	
	void postDeregister();
}
