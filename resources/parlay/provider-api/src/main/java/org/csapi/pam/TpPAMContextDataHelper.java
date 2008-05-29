package org.csapi.pam;

/**
 *	Generated from IDL definition of union "TpPAMContextData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextDataHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMContextData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMContextData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMContextData:1.0";
	}
	public static TpPAMContextData read (org.omg.CORBA.portable.InputStream in)
	{
		TpPAMContextData result = new TpPAMContextData ();
		org.csapi.pam.TpPAMContextName disc = org.csapi.pam.TpPAMContextName.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.pam.TpPAMContextName._PAM_CONTEXT_COMMUNICATION:
			{
				org.csapi.pam.TpPAMCommunicationContext _var;
				_var=org.csapi.pam.TpPAMCommunicationContextHelper.read(in);
				result.CommunicationContext (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpPAMContextData s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.pam.TpPAMContextName._PAM_CONTEXT_COMMUNICATION:
			{
				org.csapi.pam.TpPAMCommunicationContextHelper.write(out,s.CommunicationContext ());
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
			org.csapi.pam.TpPAMContextNameHelper.insert(label_any, org.csapi.pam.TpPAMContextName.PAM_CONTEXT_COMMUNICATION);
			members[1] = new org.omg.CORBA.UnionMember ("CommunicationContext", label_any, org.csapi.pam.TpPAMCommunicationContextHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpPAMContextData",org.csapi.pam.TpPAMContextNameHelper.type(), members);
		}
		return _type;
	}
}
