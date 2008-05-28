package org.csapi;

/**
 *	Generated from IDL definition of union "TpAttributeValue"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeValueHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAttributeValue s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAttributeValue extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAttributeValue:1.0";
	}
	public static TpAttributeValue read (org.omg.CORBA.portable.InputStream in)
	{
		TpAttributeValue result = new TpAttributeValue ();
		org.csapi.TpAttributeTagInfo disc = org.csapi.TpAttributeTagInfo.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.TpAttributeTagInfo._P_SIMPLE_TYPE:
			{
				org.csapi.TpSimpleAttributeValue _var;
				_var=org.csapi.TpSimpleAttributeValueHelper.read(in);
				result.SimpleValue (_var);
				break;
			}
			case org.csapi.TpAttributeTagInfo._P_STRUCTURED_TYPE:
			{
				org.csapi.TpStructuredAttributeValue _var;
				_var=org.csapi.TpStructuredAttributeValueHelper.read(in);
				result.StructuredValue (_var);
				break;
			}
			case org.csapi.TpAttributeTagInfo._P_XML_TYPE:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.XMLValue (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpAttributeValue s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.TpAttributeTagInfo._P_SIMPLE_TYPE:
			{
				org.csapi.TpSimpleAttributeValueHelper.write(out,s.SimpleValue ());
				break;
			}
			case org.csapi.TpAttributeTagInfo._P_STRUCTURED_TYPE:
			{
				org.csapi.TpStructuredAttributeValueHelper.write(out,s.StructuredValue ());
				break;
			}
			case org.csapi.TpAttributeTagInfo._P_XML_TYPE:
			{
				out.write_string(s.XMLValue ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[3];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpAttributeTagInfoHelper.insert(label_any, org.csapi.TpAttributeTagInfo.P_SIMPLE_TYPE);
			members[2] = new org.omg.CORBA.UnionMember ("SimpleValue", label_any, org.csapi.TpSimpleAttributeValueHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpAttributeTagInfoHelper.insert(label_any, org.csapi.TpAttributeTagInfo.P_STRUCTURED_TYPE);
			members[1] = new org.omg.CORBA.UnionMember ("StructuredValue", label_any, org.csapi.TpStructuredAttributeValueHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpAttributeTagInfoHelper.insert(label_any, org.csapi.TpAttributeTagInfo.P_XML_TYPE);
			members[0] = new org.omg.CORBA.UnionMember ("XMLValue", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpAttributeValue",org.csapi.TpAttributeTagInfoHelper.type(), members);
		}
		return _type;
	}
}
