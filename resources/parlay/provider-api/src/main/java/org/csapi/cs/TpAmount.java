package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpAmount"
 *	@author JacORB IDL compiler 
 */

public final class TpAmount
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAmount(){}
	public int Number;
	public int Exponent;
	public TpAmount(int Number, int Exponent)
	{
		this.Number = Number;
		this.Exponent = Exponent;
	}
}
