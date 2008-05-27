package org.mobicents.slee.services.sip.registrar.mbean;

import java.io.Serializable;

public interface RegistrarConfiguratorMBean extends Cloneable{

	
	public static final String MBEAN_NAME_PREFIX="slee:sipregistrarconfigurator=";
	
	public long getSipRegistrationMinExpires();
    public void setSipRegistrationMinExpires(long minExpires);
    public long getSipRegistrationMaxExpires();
    public void setSipRegistrationMaxExpires(long maxExpires);

    public Object clone();
	
}
