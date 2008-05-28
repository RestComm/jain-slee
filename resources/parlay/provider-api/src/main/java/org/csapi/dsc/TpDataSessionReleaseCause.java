package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionReleaseCause"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReleaseCause
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionReleaseCause(){}
	public int Value;
	public int Location;
	public TpDataSessionReleaseCause(int Value, int Location)
	{
		this.Value = Value;
		this.Location = Location;
	}
}
