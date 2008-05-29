package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpDelayDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpDelayDescriptorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpDelayDescriptorHelper.id(),"TpDelayDescriptor",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("meanDelay", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("measurementPeriod", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("maxDelay", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("minDelay", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("delayPriority", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("description", org.csapi.cm.TpNameDescrpTagStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpDelayDescriptor s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpDelayDescriptor extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpDelayDescriptor:1.0";
	}
	public static org.csapi.cm.TpDelayDescriptor read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpDelayDescriptor result = new org.csapi.cm.TpDelayDescriptor();
		result.meanDelay=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.measurementPeriod=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.maxDelay=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.minDelay=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.delayPriority=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.description=org.csapi.cm.TpNameDescrpTagStringHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpDelayDescriptor s)
	{
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.meanDelay);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.measurementPeriod);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.maxDelay);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.minDelay);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.delayPriority);
		org.csapi.cm.TpNameDescrpTagStringHelper.write(out,s.description);
	}
}
