package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpChargingError"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cs.TpChargingErrorHelper.id(),"TpChargingError",new String[]{"P_CHS_ERR_UNDEFINED","P_CHS_ERR_ACCOUNT","P_CHS_ERR_USER","P_CHS_ERR_PARAMETER","P_CHS_ERR_NO_DEBIT","P_CHS_ERR_NO_CREDIT","P_CHS_ERR_VOLUMES","P_CHS_ERR_CURRENCY","P_CHS_ERR_NO_EXTEND","P_CHS_ERR_RESERVATION_LIMIT","P_CHS_ERR_CONFIRMATION_REQUIRED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpChargingError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpChargingError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpChargingError:1.0";
	}
	public static TpChargingError read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpChargingError.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpChargingError s)
	{
		out.write_long(s.value());
	}
}
