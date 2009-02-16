package org.mobicents.slee.container.component.validator.profile.localinterface;

import javax.slee.InvalidStateException;
import javax.slee.management.ManagementException;
import javax.slee.profile.ProfileVerificationException;

public interface LocalInterfaceWrongMethod_ProfileMBean extends javax.slee.profile.ProfileLocalObject{

	public void doSomeTricktMGMTMagic(String xxxx);
	public void dontLookAtMeImUglyDefinedMethodWithLongName(java.io.Serializable cheese);
	
	public void commitProfile()
    throws InvalidStateException,
           ProfileVerificationException,
           ManagementException;
}
