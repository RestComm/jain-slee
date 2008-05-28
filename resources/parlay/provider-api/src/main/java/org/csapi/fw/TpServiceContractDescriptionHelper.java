package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpServiceContractDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceContractDescriptionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpServiceContractDescriptionHelper.id(),"TpServiceContractDescription",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ServiceRequestor", org.csapi.fw.TpServiceRequestorHelper.type(), null),new org.omg.CORBA.StructMember("BillingContact", org.csapi.fw.TpBillingContactHelper.type(), null),new org.omg.CORBA.StructMember("ServiceStartDate", org.csapi.fw.TpServiceStartDateHelper.type(), null),new org.omg.CORBA.StructMember("ServiceEndDate", org.csapi.fw.TpServiceEndDateHelper.type(), null),new org.omg.CORBA.StructMember("ServiceTypeName", org.csapi.fw.TpServiceTypeNameHelper.type(), null),new org.omg.CORBA.StructMember("ServiceID", org.csapi.fw.TpServiceIDHelper.type(), null),new org.omg.CORBA.StructMember("ServiceSubscriptionProperties", org.csapi.fw.TpServiceSubscriptionPropertiesHelper.type(), null),new org.omg.CORBA.StructMember("InUse", org.csapi.TpBooleanHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpServiceContractDescription s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpServiceContractDescription extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServiceContractDescription:1.0";
	}
	public static org.csapi.fw.TpServiceContractDescription read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpServiceContractDescription result = new org.csapi.fw.TpServiceContractDescription();
		result.ServiceRequestor=org.csapi.fw.TpPersonHelper.read(in);
		result.BillingContact=org.csapi.fw.TpPersonHelper.read(in);
		result.ServiceStartDate=in.read_string();
		result.ServiceEndDate=in.read_string();
		result.ServiceTypeName=in.read_string();
		result.ServiceID=in.read_string();
		result.ServiceSubscriptionProperties = org.csapi.fw.TpServicePropertyListHelper.read(in);
		result.InUse=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpServiceContractDescription s)
	{
		org.csapi.fw.TpPersonHelper.write(out,s.ServiceRequestor);
		org.csapi.fw.TpPersonHelper.write(out,s.BillingContact);
		out.write_string(s.ServiceStartDate);
		out.write_string(s.ServiceEndDate);
		out.write_string(s.ServiceTypeName);
		out.write_string(s.ServiceID);
		org.csapi.fw.TpServicePropertyListHelper.write(out,s.ServiceSubscriptionProperties);
		out.write_boolean(s.InUse);
	}
}
