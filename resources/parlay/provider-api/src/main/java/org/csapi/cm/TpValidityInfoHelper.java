package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpValidityInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpValidityInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpValidityInfoHelper.id(),"TpValidityInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("validFrom", org.csapi.cm.TpNameDescrpTagDateTimeHelper.type(), null),new org.omg.CORBA.StructMember("validPeriod", org.csapi.cm.TpNameDescrpTagTimePeriodHelper.type(), null),new org.omg.CORBA.StructMember("validDailyFrom", org.csapi.cm.TpNameDescrpTagTimeOfDayHelper.type(), null),new org.omg.CORBA.StructMember("validDailyPeriod", org.csapi.cm.TpNameDescrpTagTimePeriodHelper.type(), null),new org.omg.CORBA.StructMember("validDayOfWeek", org.csapi.cm.TpNameDescrpTagDayOfWeekHelper.type(), null),new org.omg.CORBA.StructMember("validMonth", org.csapi.cm.TpNameDescrpTagMonthHelper.type(), null),new org.omg.CORBA.StructMember("description", org.csapi.cm.TpNameDescrpTagStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpValidityInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpValidityInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpValidityInfo:1.0";
	}
	public static org.csapi.cm.TpValidityInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpValidityInfo result = new org.csapi.cm.TpValidityInfo();
		result.validFrom=org.csapi.cm.TpNameDescrpTagDateTimeHelper.read(in);
		result.validPeriod=org.csapi.cm.TpNameDescrpTagTimePeriodHelper.read(in);
		result.validDailyFrom=org.csapi.cm.TpNameDescrpTagTimeOfDayHelper.read(in);
		result.validDailyPeriod=org.csapi.cm.TpNameDescrpTagTimePeriodHelper.read(in);
		result.validDayOfWeek=org.csapi.cm.TpNameDescrpTagDayOfWeekHelper.read(in);
		result.validMonth=org.csapi.cm.TpNameDescrpTagMonthHelper.read(in);
		result.description=org.csapi.cm.TpNameDescrpTagStringHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpValidityInfo s)
	{
		org.csapi.cm.TpNameDescrpTagDateTimeHelper.write(out,s.validFrom);
		org.csapi.cm.TpNameDescrpTagTimePeriodHelper.write(out,s.validPeriod);
		org.csapi.cm.TpNameDescrpTagTimeOfDayHelper.write(out,s.validDailyFrom);
		org.csapi.cm.TpNameDescrpTagTimePeriodHelper.write(out,s.validDailyPeriod);
		org.csapi.cm.TpNameDescrpTagDayOfWeekHelper.write(out,s.validDayOfWeek);
		org.csapi.cm.TpNameDescrpTagMonthHelper.write(out,s.validMonth);
		org.csapi.cm.TpNameDescrpTagStringHelper.write(out,s.description);
	}
}
