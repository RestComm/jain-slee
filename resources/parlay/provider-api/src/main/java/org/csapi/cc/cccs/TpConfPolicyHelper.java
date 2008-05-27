package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of union "TpConfPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpConfPolicyHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpConfPolicy s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpConfPolicy extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpConfPolicy:1.0";
	}
	public static TpConfPolicy read (org.omg.CORBA.portable.InputStream in)
	{
		TpConfPolicy result = new TpConfPolicy ();
		org.csapi.cc.cccs.TpConfPolicyType disc = org.csapi.cc.cccs.TpConfPolicyType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.cccs.TpConfPolicyType._P_CONFERENCE_POLICY_MONOMEDIA:
			{
				org.csapi.cc.cccs.TpMonoMediaConfPolicy _var;
				_var=org.csapi.cc.cccs.TpMonoMediaConfPolicyHelper.read(in);
				result.MonoMedia (_var);
				break;
			}
			case org.csapi.cc.cccs.TpConfPolicyType._P_CONFERENCE_POLICY_MULTIMEDIA:
			{
				org.csapi.cc.cccs.TpMultiMediaConfPolicy _var;
				_var=org.csapi.cc.cccs.TpMultiMediaConfPolicyHelper.read(in);
				result.MultiMedia (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpConfPolicy s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.cccs.TpConfPolicyType._P_CONFERENCE_POLICY_MONOMEDIA:
			{
				org.csapi.cc.cccs.TpMonoMediaConfPolicyHelper.write(out,s.MonoMedia ());
				break;
			}
			case org.csapi.cc.cccs.TpConfPolicyType._P_CONFERENCE_POLICY_MULTIMEDIA:
			{
				org.csapi.cc.cccs.TpMultiMediaConfPolicyHelper.write(out,s.MultiMedia ());
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
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[3];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.cccs.TpConfPolicyTypeHelper.insert(label_any, org.csapi.cc.cccs.TpConfPolicyType.P_CONFERENCE_POLICY_MONOMEDIA);
			members[2] = new org.omg.CORBA.UnionMember ("MonoMedia", label_any, org.csapi.cc.cccs.TpMonoMediaConfPolicyHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.cccs.TpConfPolicyTypeHelper.insert(label_any, org.csapi.cc.cccs.TpConfPolicyType.P_CONFERENCE_POLICY_MULTIMEDIA);
			members[1] = new org.omg.CORBA.UnionMember ("MultiMedia", label_any, org.csapi.cc.cccs.TpMultiMediaConfPolicyHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpConfPolicy",org.csapi.cc.cccs.TpConfPolicyTypeHelper.type(), members);
		}
		return _type;
	}
}
