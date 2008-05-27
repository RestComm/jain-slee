package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMPresenceData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPresenceDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMPresenceDataHelper.id(),"TpPAMPresenceData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Name", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("subscriberStatus", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("networkStatus", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("communicationMeans", org.csapi.pam.TpPAMCapabilityHelper.type(), null),new org.omg.CORBA.StructMember("contactAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("subscriberProvidedLocation", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("networkProvidedLocation", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("Priority", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("otherInfo", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMPresenceData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMPresenceData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMPresenceData:1.0";
	}
	public static org.csapi.pam.TpPAMPresenceData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMPresenceData result = new org.csapi.pam.TpPAMPresenceData();
		result.Name=in.read_string();
		result.subscriberStatus=in.read_string();
		result.networkStatus=in.read_string();
		result.communicationMeans=in.read_string();
		result.contactAddress=org.csapi.TpAddressHelper.read(in);
		result.subscriberProvidedLocation=in.read_string();
		result.networkProvidedLocation=in.read_string();
		result.Priority=in.read_long();
		result.otherInfo=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMPresenceData s)
	{
		out.write_string(s.Name);
		out.write_string(s.subscriberStatus);
		out.write_string(s.networkStatus);
		out.write_string(s.communicationMeans);
		org.csapi.TpAddressHelper.write(out,s.contactAddress);
		out.write_string(s.subscriberProvidedLocation);
		out.write_string(s.networkProvidedLocation);
		out.write_long(s.Priority);
		out.write_string(s.otherInfo);
	}
}
