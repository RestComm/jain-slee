package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUICollectCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpUICollectCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUICollectCriteria(){}
	public int MinLength;
	public int MaxLength;
	public java.lang.String EndSequence;
	public int StartTimeout;
	public int InterCharTimeout;
	public TpUICollectCriteria(int MinLength, int MaxLength, java.lang.String EndSequence, int StartTimeout, int InterCharTimeout)
	{
		this.MinLength = MinLength;
		this.MaxLength = MaxLength;
		this.EndSequence = EndSequence;
		this.StartTimeout = StartTimeout;
		this.InterCharTimeout = InterCharTimeout;
	}
}
