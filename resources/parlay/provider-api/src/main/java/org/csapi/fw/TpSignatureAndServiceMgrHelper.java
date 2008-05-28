package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpSignatureAndServiceMgr"
 *	@author JacORB IDL compiler 
 */

public final class TpSignatureAndServiceMgrHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpSignatureAndServiceMgrHelper.id(),"TpSignatureAndServiceMgr",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DigitalSignature", org.csapi.TpOctetSetHelper.type(), null),new org.omg.CORBA.StructMember("ServiceMgrInterface", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/IpService:1.0", "IpService"), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpSignatureAndServiceMgr s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpSignatureAndServiceMgr extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpSignatureAndServiceMgr:1.0";
	}
	public static org.csapi.fw.TpSignatureAndServiceMgr read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpSignatureAndServiceMgr result = new org.csapi.fw.TpSignatureAndServiceMgr();
		result.DigitalSignature = org.csapi.TpOctetSetHelper.read(in);
		result.ServiceMgrInterface=org.csapi.IpServiceHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpSignatureAndServiceMgr s)
	{
		org.csapi.TpOctetSetHelper.write(out,s.DigitalSignature);
		org.csapi.IpServiceHelper.write(out,s.ServiceMgrInterface);
	}
}
