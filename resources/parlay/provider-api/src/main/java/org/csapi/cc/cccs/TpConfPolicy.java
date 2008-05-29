package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of union "TpConfPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpConfPolicy
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.cccs.TpConfPolicyType discriminator;
	private org.csapi.cc.cccs.TpMonoMediaConfPolicy MonoMedia;
	private org.csapi.cc.cccs.TpMultiMediaConfPolicy MultiMedia;
	private short Dummy;

	public TpConfPolicy ()
	{
	}

	public org.csapi.cc.cccs.TpConfPolicyType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.cc.cccs.TpMonoMediaConfPolicy MonoMedia ()
	{
		if (discriminator != org.csapi.cc.cccs.TpConfPolicyType.P_CONFERENCE_POLICY_MONOMEDIA)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MonoMedia;
	}

	public void MonoMedia (org.csapi.cc.cccs.TpMonoMediaConfPolicy _x)
	{
		discriminator = org.csapi.cc.cccs.TpConfPolicyType.P_CONFERENCE_POLICY_MONOMEDIA;
		MonoMedia = _x;
	}

	public org.csapi.cc.cccs.TpMultiMediaConfPolicy MultiMedia ()
	{
		if (discriminator != org.csapi.cc.cccs.TpConfPolicyType.P_CONFERENCE_POLICY_MULTIMEDIA)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MultiMedia;
	}

	public void MultiMedia (org.csapi.cc.cccs.TpMultiMediaConfPolicy _x)
	{
		discriminator = org.csapi.cc.cccs.TpConfPolicyType.P_CONFERENCE_POLICY_MULTIMEDIA;
		MultiMedia = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.cccs.TpConfPolicyType.P_CONFERENCE_POLICY_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.cccs.TpConfPolicyType.P_CONFERENCE_POLICY_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.cccs.TpConfPolicyType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.cccs.TpConfPolicyType.P_CONFERENCE_POLICY_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
