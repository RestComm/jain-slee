package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpLossDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpLossDescriptorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpLossDescriptorHelper.id(),"TpLossDescriptor",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("meanLoss", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("measurementPeriod", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("maxLoss", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("minLoss", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("lossPriority", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("description", org.csapi.cm.TpNameDescrpTagStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpLossDescriptor s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpLossDescriptor extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpLossDescriptor:1.0";
	}
	public static org.csapi.cm.TpLossDescriptor read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpLossDescriptor result = new org.csapi.cm.TpLossDescriptor();
		result.meanLoss=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.measurementPeriod=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.maxLoss=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.minLoss=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.lossPriority=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.description=org.csapi.cm.TpNameDescrpTagStringHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpLossDescriptor s)
	{
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.meanLoss);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.measurementPeriod);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.maxLoss);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.minLoss);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.lossPriority);
		org.csapi.cm.TpNameDescrpTagStringHelper.write(out,s.description);
	}
}
