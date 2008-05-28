package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUICallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpUICallIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUICallIdentifier(){}
	public org.csapi.ui.IpUICall UICallRef;
	public int UserInteractionSessionID;
	public TpUICallIdentifier(org.csapi.ui.IpUICall UICallRef, int UserInteractionSessionID)
	{
		this.UICallRef = UICallRef;
		this.UserInteractionSessionID = UserInteractionSessionID;
	}
}
