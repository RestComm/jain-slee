package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallPartyToChargeAdditionalInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallPartyToChargeAdditionalInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.TpCallPartyToChargeType discriminator;
	private org.csapi.TpAddress CallPartySpecial;
	private short Dummy;

	public TpCallPartyToChargeAdditionalInfo ()
	{
	}

	public org.csapi.cc.TpCallPartyToChargeType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.TpAddress CallPartySpecial ()
	{
		if (discriminator != org.csapi.cc.TpCallPartyToChargeType.P_CALL_PARTY_SPECIAL)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallPartySpecial;
	}

	public void CallPartySpecial (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.cc.TpCallPartyToChargeType.P_CALL_PARTY_SPECIAL;
		CallPartySpecial = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.TpCallPartyToChargeType.P_CALL_PARTY_ORIGINATING && discriminator != org.csapi.cc.TpCallPartyToChargeType.P_CALL_PARTY_DESTINATION)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.TpCallPartyToChargeType.P_CALL_PARTY_ORIGINATING;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.TpCallPartyToChargeType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.TpCallPartyToChargeType.P_CALL_PARTY_ORIGINATING && discriminator != org.csapi.cc.TpCallPartyToChargeType.P_CALL_PARTY_DESTINATION)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
