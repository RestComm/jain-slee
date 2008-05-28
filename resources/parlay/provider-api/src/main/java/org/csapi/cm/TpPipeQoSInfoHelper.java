package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpPipeQoSInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPipeQoSInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpPipeQoSInfoHelper.id(),"TpPipeQoSInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("directionality", org.csapi.cm.TpNameDescrpTagDirHelper.type(), null),new org.omg.CORBA.StructMember("serviceOrigin", org.csapi.cm.TpEndpointHelper.type(), null),new org.omg.CORBA.StructMember("serviceDestination", org.csapi.cm.TpEndpointHelper.type(), null),new org.omg.CORBA.StructMember("forwardLoad", org.csapi.cm.TpLoadDescriptorHelper.type(), null),new org.omg.CORBA.StructMember("reverseLoad", org.csapi.cm.TpLoadDescriptorHelper.type(), null),new org.omg.CORBA.StructMember("description", org.csapi.cm.TpNameDescrpTagStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpPipeQoSInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpPipeQoSInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpPipeQoSInfo:1.0";
	}
	public static org.csapi.cm.TpPipeQoSInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpPipeQoSInfo result = new org.csapi.cm.TpPipeQoSInfo();
		result.directionality=org.csapi.cm.TpNameDescrpTagDirHelper.read(in);
		result.serviceOrigin=org.csapi.cm.TpEndpointHelper.read(in);
		result.serviceDestination=org.csapi.cm.TpEndpointHelper.read(in);
		result.forwardLoad=org.csapi.cm.TpLoadDescriptorHelper.read(in);
		result.reverseLoad=org.csapi.cm.TpLoadDescriptorHelper.read(in);
		result.description=org.csapi.cm.TpNameDescrpTagStringHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpPipeQoSInfo s)
	{
		org.csapi.cm.TpNameDescrpTagDirHelper.write(out,s.directionality);
		org.csapi.cm.TpEndpointHelper.write(out,s.serviceOrigin);
		org.csapi.cm.TpEndpointHelper.write(out,s.serviceDestination);
		org.csapi.cm.TpLoadDescriptorHelper.write(out,s.forwardLoad);
		org.csapi.cm.TpLoadDescriptorHelper.write(out,s.reverseLoad);
		org.csapi.cm.TpNameDescrpTagStringHelper.write(out,s.description);
	}
}
