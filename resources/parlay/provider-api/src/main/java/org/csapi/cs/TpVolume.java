package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpVolume
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpVolume(){}
	public org.csapi.cs.TpAmount Amount;
	public int Unit;
	public TpVolume(org.csapi.cs.TpAmount Amount, int Unit)
	{
		this.Amount = Amount;
		this.Unit = Unit;
	}
}
