package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMCredential"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMCredentialHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, byte[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static byte[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.pam.TpPAMCredentialHelper.id(), "TpPAMCredential",org.csapi.TpOctetSetHelper.type());
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMCredential:1.0";
	}
	public static byte[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		byte[] _result;
		_result = org.csapi.TpOctetSetHelper.read(_in);
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, byte[] _s)
	{
		org.csapi.TpOctetSetHelper.write(_out,_s);
	}
}
