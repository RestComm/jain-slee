package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpJitterDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpJitterDescriptorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpJitterDescriptorHelper.id(),"TpJitterDescriptor",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("meanJitter", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("measurementPeriod", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("maxJitter", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("minJitter", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("jitterPriority", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("description", org.csapi.cm.TpNameDescrpTagStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpJitterDescriptor s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpJitterDescriptor extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpJitterDescriptor:1.0";
	}
	public static org.csapi.cm.TpJitterDescriptor read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpJitterDescriptor result = new org.csapi.cm.TpJitterDescriptor();
		result.meanJitter=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.measurementPeriod=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.maxJitter=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.minJitter=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.jitterPriority=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.description=org.csapi.cm.TpNameDescrpTagStringHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpJitterDescriptor s)
	{
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.meanJitter);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.measurementPeriod);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.maxJitter);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.minJitter);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.jitterPriority);
		org.csapi.cm.TpNameDescrpTagStringHelper.write(out,s.description);
	}
}
