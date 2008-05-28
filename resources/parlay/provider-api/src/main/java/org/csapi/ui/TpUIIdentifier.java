package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpUIIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUIIdentifier(){}
	public org.csapi.ui.IpUI UIRef;
	public int UserInteractionSessionID;
	public TpUIIdentifier(org.csapi.ui.IpUI UIRef, int UserInteractionSessionID)
	{
		this.UIRef = UIRef;
		this.UserInteractionSessionID = UserInteractionSessionID;
	}
}
