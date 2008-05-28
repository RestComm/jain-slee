package org.csapi.cs;

/**
 *	Generated from IDL definition of union "TpAppInformation"
 *	@author JacORB IDL compiler 
 */

public final class TpAppInformationHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpAppInformation s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpAppInformation extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpAppInformation:1.0";
	}
	public static TpAppInformation read (org.omg.CORBA.portable.InputStream in)
	{
		TpAppInformation result = new TpAppInformation ();
		org.csapi.cs.TpAppInformationType disc = org.csapi.cs.TpAppInformationType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cs.TpAppInformationType._P_APP_INF_TIMESTAMP:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.Timestamp (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpAppInformation s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cs.TpAppInformationType._P_APP_INF_TIMESTAMP:
			{
				out.write_string(s.Timestamp ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[1];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cs.TpAppInformationTypeHelper.insert(label_any, org.csapi.cs.TpAppInformationType.P_APP_INF_TIMESTAMP);
			members[0] = new org.omg.CORBA.UnionMember ("Timestamp", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpAppInformation",org.csapi.cs.TpAppInformationTypeHelper.type(), members);
		}
		return _type;
	}
}
