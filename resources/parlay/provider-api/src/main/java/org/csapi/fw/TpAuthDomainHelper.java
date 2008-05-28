package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpAuthDomain"
 *	@author JacORB IDL compiler 
 */

public final class TpAuthDomainHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpAuthDomainHelper.id(),"TpAuthDomain",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DomainID", org.csapi.fw.TpDomainIDHelper.type(), null),new org.omg.CORBA.StructMember("AuthInterface", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/IpInterface:1.0", "IpInterface"), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpAuthDomain s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpAuthDomain extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpAuthDomain:1.0";
	}
	public static org.csapi.fw.TpAuthDomain read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpAuthDomain result = new org.csapi.fw.TpAuthDomain();
		result.DomainID=org.csapi.fw.TpDomainIDHelper.read(in);
		result.AuthInterface=org.csapi.IpInterfaceHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpAuthDomain s)
	{
		org.csapi.fw.TpDomainIDHelper.write(out,s.DomainID);
		org.csapi.IpInterfaceHelper.write(out,s.AuthInterface);
	}
}
