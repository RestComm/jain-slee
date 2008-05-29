package org.csapi.fw;

/**
 *	Generated from IDL definition of union "TpDomainID"
 *	@author JacORB IDL compiler 
 */

public final class TpDomainIDHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpDomainID s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpDomainID extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpDomainID:1.0";
	}
	public static TpDomainID read (org.omg.CORBA.portable.InputStream in)
	{
		TpDomainID result = new TpDomainID ();
		org.csapi.fw.TpDomainIDType disc = org.csapi.fw.TpDomainIDType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.fw.TpDomainIDType._P_FW:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.FwID (_var);
				break;
			}
			case org.csapi.fw.TpDomainIDType._P_CLIENT_APPLICATION:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.ClientAppID (_var);
				break;
			}
			case org.csapi.fw.TpDomainIDType._P_ENT_OP:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.EntOpID (_var);
				break;
			}
			case org.csapi.fw.TpDomainIDType._P_SERVICE_INSTANCE:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.ServiceID (_var);
				break;
			}
			case org.csapi.fw.TpDomainIDType._P_SERVICE_SUPPLIER:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.ServiceSupplierID (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpDomainID s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.fw.TpDomainIDType._P_FW:
			{
				out.write_string(s.FwID ());
				break;
			}
			case org.csapi.fw.TpDomainIDType._P_CLIENT_APPLICATION:
			{
				out.write_string(s.ClientAppID ());
				break;
			}
			case org.csapi.fw.TpDomainIDType._P_ENT_OP:
			{
				out.write_string(s.EntOpID ());
				break;
			}
			case org.csapi.fw.TpDomainIDType._P_SERVICE_INSTANCE:
			{
				out.write_string(s.ServiceID ());
				break;
			}
			case org.csapi.fw.TpDomainIDType._P_SERVICE_SUPPLIER:
			{
				out.write_string(s.ServiceSupplierID ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[5];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpDomainIDTypeHelper.insert(label_any, org.csapi.fw.TpDomainIDType.P_FW);
			members[4] = new org.omg.CORBA.UnionMember ("FwID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpDomainIDTypeHelper.insert(label_any, org.csapi.fw.TpDomainIDType.P_CLIENT_APPLICATION);
			members[3] = new org.omg.CORBA.UnionMember ("ClientAppID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpDomainIDTypeHelper.insert(label_any, org.csapi.fw.TpDomainIDType.P_ENT_OP);
			members[2] = new org.omg.CORBA.UnionMember ("EntOpID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpDomainIDTypeHelper.insert(label_any, org.csapi.fw.TpDomainIDType.P_SERVICE_INSTANCE);
			members[1] = new org.omg.CORBA.UnionMember ("ServiceID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpDomainIDTypeHelper.insert(label_any, org.csapi.fw.TpDomainIDType.P_SERVICE_SUPPLIER);
			members[0] = new org.omg.CORBA.UnionMember ("ServiceSupplierID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpDomainID",org.csapi.fw.TpDomainIDTypeHelper.type(), members);
		}
		return _type;
	}
}
