package org.csapi.pam;

/**
 *	Generated from IDL definition of union "TpPAMPreferenceData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPreferenceDataHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMPreferenceData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMPreferenceData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMPreferenceData:1.0";
	}
	public static TpPAMPreferenceData read (org.omg.CORBA.portable.InputStream in)
	{
		TpPAMPreferenceData result = new TpPAMPreferenceData ();
		org.csapi.pam.TpPAMPreferenceType disc = org.csapi.pam.TpPAMPreferenceType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.pam.TpPAMPreferenceType._PAM_EXTERNAL_CONTROL:
			{
				org.csapi.pam.access.IpAppPAMPreferenceCheck _var;
				_var=org.csapi.pam.access.IpAppPAMPreferenceCheckHelper.read(in);
				result.ExternalControlInterface (_var);
				break;
			}
			default:
			{
				short _var;
				_var=in.read_short();
				result.Dummy (_var);
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpPAMPreferenceData s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.pam.TpPAMPreferenceType._PAM_EXTERNAL_CONTROL:
			{
				org.csapi.pam.access.IpAppPAMPreferenceCheckHelper.write(out,s.ExternalControlInterface ());
				break;
			}
			default:
			{
				out.write_short(s.Dummy ());
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[2];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMPreferenceTypeHelper.insert(label_any, org.csapi.pam.TpPAMPreferenceType.PAM_EXTERNAL_CONTROL);
			members[1] = new org.omg.CORBA.UnionMember ("ExternalControlInterface", label_any, org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/pam/access/IpAppPAMPreferenceCheck:1.0", "IpAppPAMPreferenceCheck"),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpPAMPreferenceData",org.csapi.pam.TpPAMPreferenceTypeHelper.type(), members);
		}
		return _type;
	}
}
