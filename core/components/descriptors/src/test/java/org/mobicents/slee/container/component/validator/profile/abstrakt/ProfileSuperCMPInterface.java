package org.mobicents.slee.container.component.validator.profile.abstrakt;

import java.io.Serializable;

public interface ProfileSuperCMPInterface {
	public int[] getMasterTestArray();

	public void setMasterTestSerializable(Serializable[] v);

	public Serializable[] getMasterTestSerializable();
}
