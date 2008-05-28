package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpLoadStatistic"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatistic
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpLoadStatistic(){}
	public org.csapi.fw.TpLoadStatisticEntityID LoadStatisticEntityID;
	public java.lang.String TimeStamp;
	public org.csapi.fw.TpLoadStatisticInfo LoadStatisticInfo;
	public TpLoadStatistic(org.csapi.fw.TpLoadStatisticEntityID LoadStatisticEntityID, java.lang.String TimeStamp, org.csapi.fw.TpLoadStatisticInfo LoadStatisticInfo)
	{
		this.LoadStatisticEntityID = LoadStatisticEntityID;
		this.TimeStamp = TimeStamp;
		this.LoadStatisticInfo = LoadStatisticInfo;
	}
}
