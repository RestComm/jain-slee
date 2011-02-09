package org.mobicents.slee.container.component.validator.profile.localinterface;

import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.slee.InvalidStateException;
import javax.slee.management.ManagementException;
import javax.slee.profile.ProfileVerificationException;

public interface LocalInterfaceWrongMethod_MBeanRegistration extends javax.slee.profile.ProfileLocalObject{

	public void doSomeTricktMGMTMagic(String xxxx);
	public void dontLookAtMeImUglyDefinedMethodWithLongName(java.io.Serializable cheese);
	
	//JDK 1.5 and 1.6 ?
	 public void postRegister(Boolean registrationDone);
	 public void postRegister();
}
