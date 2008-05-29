package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpLocationResponseTime"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationResponseTime
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpLocationResponseTime(){}
	public org.csapi.mm.TpLocationResponseIndicator ResponseTime;
	public int TimerValue;
	public TpLocationResponseTime(org.csapi.mm.TpLocationResponseIndicator ResponseTime, int TimerValue)
	{
		this.ResponseTime = ResponseTime;
		this.TimerValue = TimerValue;
	}
}
