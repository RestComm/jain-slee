package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMAvailabilityProfile"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAvailabilityProfileHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMAvailabilityProfileHelper.id(),"TpPAMAvailabilityProfile",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("PrivacyCode", org.csapi.pam.TpPAMPrivacyCodeHelper.type(), null),new org.omg.CORBA.StructMember("AvailabilityData", org.csapi.pam.TpPAMPresenceDataHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMAvailabilityProfile s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMAvailabilityProfile extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMAvailabilityProfile:1.0";
	}
	public static org.csapi.pam.TpPAMAvailabilityProfile read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMAvailabilityProfile result = new org.csapi.pam.TpPAMAvailabilityProfile();
		result.PrivacyCode=in.read_string();
		result.AvailabilityData=org.csapi.pam.TpPAMPresenceDataHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMAvailabilityProfile s)
	{
		out.write_string(s.PrivacyCode);
		org.csapi.pam.TpPAMPresenceDataHelper.write(out,s.AvailabilityData);
	}
}
