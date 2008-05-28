package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallReleaseCause"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReleaseCause
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallReleaseCause(){}
	public int Value;
	public int Location;
	public TpCallReleaseCause(int Value, int Location)
	{
		this.Value = Value;
		this.Location = Location;
	}
}
