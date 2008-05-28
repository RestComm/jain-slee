package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpClientAppDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpClientAppDescriptionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpClientAppDescriptionHelper.id(),"TpClientAppDescription",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ClientAppID", org.csapi.fw.TpClientAppIDHelper.type(), null),new org.omg.CORBA.StructMember("ClientAppProperties", org.csapi.fw.TpClientAppPropertiesHelper.type(), null),new org.omg.CORBA.StructMember("HasAccessSession", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("HasServiceInstances", org.csapi.TpBooleanHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpClientAppDescription s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpClientAppDescription extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpClientAppDescription:1.0";
	}
	public static org.csapi.fw.TpClientAppDescription read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpClientAppDescription result = new org.csapi.fw.TpClientAppDescription();
		result.ClientAppID=in.read_string();
		result.ClientAppProperties = org.csapi.fw.TpPropertyListHelper.read(in);
		result.HasAccessSession=in.read_boolean();
		result.HasServiceInstances=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpClientAppDescription s)
	{
		out.write_string(s.ClientAppID);
		org.csapi.fw.TpPropertyListHelper.write(out,s.ClientAppProperties);
		out.write_boolean(s.HasAccessSession);
		out.write_boolean(s.HasServiceInstances);
	}
}
