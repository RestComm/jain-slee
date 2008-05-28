package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpLocationTrigger"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTrigger
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpLocationTrigger(){}
	public float Longitude;
	public float Latitude;
	public float AreaSemiMajor;
	public float AreaSemiMinor;
	public int AngleOfSemiMajor;
	public org.csapi.mm.TpLocationTriggerCriteria Criterion;
	public int ReportingInterval;
	public TpLocationTrigger(float Longitude, float Latitude, float AreaSemiMajor, float AreaSemiMinor, int AngleOfSemiMajor, org.csapi.mm.TpLocationTriggerCriteria Criterion, int ReportingInterval)
	{
		this.Longitude = Longitude;
		this.Latitude = Latitude;
		this.AreaSemiMajor = AreaSemiMajor;
		this.AreaSemiMinor = AreaSemiMinor;
		this.AngleOfSemiMajor = AngleOfSemiMajor;
		this.Criterion = Criterion;
		this.ReportingInterval = ReportingInterval;
	}
}
