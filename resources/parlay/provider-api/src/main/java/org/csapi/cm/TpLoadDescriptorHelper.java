package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpLoadDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadDescriptorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpLoadDescriptorHelper.id(),"TpLoadDescriptor",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("meanBandwidth", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("measurementInterval", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("maxBandwidth", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("minBandwidth", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("bandwidthShare", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("bandwidthWeight", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("burstSize", org.csapi.cm.TpNameDescrpTagIntHelper.type(), null),new org.omg.CORBA.StructMember("description", org.csapi.cm.TpNameDescrpTagStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpLoadDescriptor s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpLoadDescriptor extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpLoadDescriptor:1.0";
	}
	public static org.csapi.cm.TpLoadDescriptor read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpLoadDescriptor result = new org.csapi.cm.TpLoadDescriptor();
		result.meanBandwidth=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.measurementInterval=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.maxBandwidth=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.minBandwidth=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.bandwidthShare=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.bandwidthWeight=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.burstSize=org.csapi.cm.TpNameDescrpTagIntHelper.read(in);
		result.description=org.csapi.cm.TpNameDescrpTagStringHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpLoadDescriptor s)
	{
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.meanBandwidth);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.measurementInterval);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.maxBandwidth);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.minBandwidth);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.bandwidthShare);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.bandwidthWeight);
		org.csapi.cm.TpNameDescrpTagIntHelper.write(out,s.burstSize);
		org.csapi.cm.TpNameDescrpTagStringHelper.write(out,s.description);
	}
}
