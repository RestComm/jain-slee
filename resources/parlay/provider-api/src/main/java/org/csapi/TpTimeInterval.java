package org.csapi;

/**
 *	Generated from IDL definition of struct "TpTimeInterval"
 *	@author JacORB IDL compiler 
 */

public final class TpTimeInterval
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpTimeInterval(){}
	public java.lang.String StartTime;
	public java.lang.String StopTime;
	public TpTimeInterval(java.lang.String StartTime, java.lang.String StopTime)
	{
		this.StartTime = StartTime;
		this.StopTime = StopTime;
	}
}
