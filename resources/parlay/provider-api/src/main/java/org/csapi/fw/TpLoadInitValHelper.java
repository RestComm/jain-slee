package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpLoadInitVal"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadInitValHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpLoadInitValHelper.id(),"TpLoadInitVal",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("LoadLevel", org.csapi.fw.TpLoadLevelHelper.type(), null),new org.omg.CORBA.StructMember("LoadThreshold", org.csapi.fw.TpLoadThresholdHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadInitVal s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadInitVal extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadInitVal:1.0";
	}
	public static org.csapi.fw.TpLoadInitVal read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpLoadInitVal result = new org.csapi.fw.TpLoadInitVal();
		result.LoadLevel=org.csapi.fw.TpLoadLevelHelper.read(in);
		result.LoadThreshold=org.csapi.fw.TpLoadThresholdHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpLoadInitVal s)
	{
		org.csapi.fw.TpLoadLevelHelper.write(out,s.LoadLevel);
		org.csapi.fw.TpLoadThresholdHelper.write(out,s.LoadThreshold);
	}
}
