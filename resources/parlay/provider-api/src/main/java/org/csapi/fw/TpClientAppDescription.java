package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpClientAppDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpClientAppDescription
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpClientAppDescription(){}
	public java.lang.String ClientAppID;
	public org.csapi.fw.TpProperty[] ClientAppProperties;
	public boolean HasAccessSession;
	public boolean HasServiceInstances;
	public TpClientAppDescription(java.lang.String ClientAppID, org.csapi.fw.TpProperty[] ClientAppProperties, boolean HasAccessSession, boolean HasServiceInstances)
	{
		this.ClientAppID = ClientAppID;
		this.ClientAppProperties = ClientAppProperties;
		this.HasAccessSession = HasAccessSession;
		this.HasServiceInstances = HasServiceInstances;
	}
}
