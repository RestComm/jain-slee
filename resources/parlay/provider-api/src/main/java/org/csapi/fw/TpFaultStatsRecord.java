package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpFaultStatsRecord"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatsRecord
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpFaultStatsRecord(){}
	public org.csapi.TpTimeInterval Period;
	public org.csapi.fw.TpFaultStats[] FaultStatsSet;
	public TpFaultStatsRecord(org.csapi.TpTimeInterval Period, org.csapi.fw.TpFaultStats[] FaultStatsSet)
	{
		this.Period = Period;
		this.FaultStatsSet = FaultStatsSet;
	}
}
