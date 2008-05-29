package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpValidityInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpValidityInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpValidityInfo(){}
	public org.csapi.cm.TpNameDescrpTagDateTime validFrom;
	public org.csapi.cm.TpNameDescrpTagTimePeriod validPeriod;
	public org.csapi.cm.TpNameDescrpTagTimeOfDay validDailyFrom;
	public org.csapi.cm.TpNameDescrpTagTimePeriod validDailyPeriod;
	public org.csapi.cm.TpNameDescrpTagDayOfWeek validDayOfWeek;
	public org.csapi.cm.TpNameDescrpTagMonth validMonth;
	public org.csapi.cm.TpNameDescrpTagString description;
	public TpValidityInfo(org.csapi.cm.TpNameDescrpTagDateTime validFrom, org.csapi.cm.TpNameDescrpTagTimePeriod validPeriod, org.csapi.cm.TpNameDescrpTagTimeOfDay validDailyFrom, org.csapi.cm.TpNameDescrpTagTimePeriod validDailyPeriod, org.csapi.cm.TpNameDescrpTagDayOfWeek validDayOfWeek, org.csapi.cm.TpNameDescrpTagMonth validMonth, org.csapi.cm.TpNameDescrpTagString description)
	{
		this.validFrom = validFrom;
		this.validPeriod = validPeriod;
		this.validDailyFrom = validDailyFrom;
		this.validDailyPeriod = validDailyPeriod;
		this.validDayOfWeek = validDayOfWeek;
		this.validMonth = validMonth;
		this.description = description;
	}
}
