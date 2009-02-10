package org.mobicents.slee.container.component.validator.profile;

import java.io.Serializable;

import javax.management.MBeanInfo;

public interface ProfileCMPInterfaceForbbidenMethods {

	public void setMasterTest(int v);

	public int getMasterTest();

	public void setMasterTestArray(int[] v);

	public int[] getMasterTestArray();

	public void setMasterTestSerializable(Serializable[] v);

	public Serializable[] getMasterTestSerializable();

	// MBeanInfo implement serializablem, so its ok, but this method is
	// forbbiden
	public MBeanInfo getMBeanInfo();

	public void setMBeanInfo(MBeanInfo i);
}
