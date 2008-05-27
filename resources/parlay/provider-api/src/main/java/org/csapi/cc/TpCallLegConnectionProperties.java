package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallLegConnectionProperties"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegConnectionProperties
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallLegConnectionProperties(){}
	public org.csapi.cc.TpCallLegAttachMechanism AttachMechanism;
	public TpCallLegConnectionProperties(org.csapi.cc.TpCallLegAttachMechanism AttachMechanism)
	{
		this.AttachMechanism = AttachMechanism;
	}
}
