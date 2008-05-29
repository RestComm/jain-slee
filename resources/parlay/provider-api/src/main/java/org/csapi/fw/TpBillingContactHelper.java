package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpBillingContact"
 *	@author JacORB IDL compiler 
 */

public final class TpBillingContactHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.fw.TpPerson s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.fw.TpPerson extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.fw.TpBillingContactHelper.id(), "TpBillingContact",org.csapi.fw.TpPersonHelper.type());
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpBillingContact:1.0";
	}
	public static org.csapi.fw.TpPerson read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.fw.TpPerson _result;
		_result=org.csapi.fw.TpPersonHelper.read(_in);
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.fw.TpPerson _s)
	{
		org.csapi.fw.TpPersonHelper.write(_out,_s);
	}
}
