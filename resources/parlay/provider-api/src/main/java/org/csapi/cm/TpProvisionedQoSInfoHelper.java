package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpProvisionedQoSInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpProvisionedQoSInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpProvisionedQoSInfoHelper.id(),"TpProvisionedQoSInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("delayDescriptor", org.csapi.cm.TpDelayDescriptorHelper.type(), null),new org.omg.CORBA.StructMember("lossDescriptor", org.csapi.cm.TpLossDescriptorHelper.type(), null),new org.omg.CORBA.StructMember("jitterDescriptor", org.csapi.cm.TpJitterDescriptorHelper.type(), null),new org.omg.CORBA.StructMember("excessLoadAction", org.csapi.cm.TpNameDescrpTagExcessLoadActionHelper.type(), null),new org.omg.CORBA.StructMember("description", org.csapi.cm.TpNameDescrpTagStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpProvisionedQoSInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpProvisionedQoSInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpProvisionedQoSInfo:1.0";
	}
	public static org.csapi.cm.TpProvisionedQoSInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpProvisionedQoSInfo result = new org.csapi.cm.TpProvisionedQoSInfo();
		result.delayDescriptor=org.csapi.cm.TpDelayDescriptorHelper.read(in);
		result.lossDescriptor=org.csapi.cm.TpLossDescriptorHelper.read(in);
		result.jitterDescriptor=org.csapi.cm.TpJitterDescriptorHelper.read(in);
		result.excessLoadAction=org.csapi.cm.TpNameDescrpTagExcessLoadActionHelper.read(in);
		result.description=org.csapi.cm.TpNameDescrpTagStringHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpProvisionedQoSInfo s)
	{
		org.csapi.cm.TpDelayDescriptorHelper.write(out,s.delayDescriptor);
		org.csapi.cm.TpLossDescriptorHelper.write(out,s.lossDescriptor);
		org.csapi.cm.TpJitterDescriptorHelper.write(out,s.jitterDescriptor);
		org.csapi.cm.TpNameDescrpTagExcessLoadActionHelper.write(out,s.excessLoadAction);
		org.csapi.cm.TpNameDescrpTagStringHelper.write(out,s.description);
	}
}
