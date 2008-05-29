package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMContext"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMContextHelper.id(),"TpPAMContext",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ContextData", org.csapi.pam.TpPAMContextDataHelper.type(), null),new org.omg.CORBA.StructMember("AskerData", org.csapi.TpAttributeListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMContext s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMContext extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMContext:1.0";
	}
	public static org.csapi.pam.TpPAMContext read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMContext result = new org.csapi.pam.TpPAMContext();
		result.ContextData=org.csapi.pam.TpPAMContextDataHelper.read(in);
		result.AskerData = org.csapi.TpAttributeListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMContext s)
	{
		org.csapi.pam.TpPAMContextDataHelper.write(out,s.ContextData);
		org.csapi.TpAttributeListHelper.write(out,s.AskerData);
	}
}
