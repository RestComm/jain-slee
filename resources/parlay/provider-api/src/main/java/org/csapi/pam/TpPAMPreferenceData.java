package org.csapi.pam;

/**
 *	Generated from IDL definition of union "TpPAMPreferenceData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPreferenceData
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.pam.TpPAMPreferenceType discriminator;
	private org.csapi.pam.access.IpAppPAMPreferenceCheck ExternalControlInterface;
	private short Dummy;

	public TpPAMPreferenceData ()
	{
	}

	public org.csapi.pam.TpPAMPreferenceType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.pam.access.IpAppPAMPreferenceCheck ExternalControlInterface ()
	{
		if (discriminator != org.csapi.pam.TpPAMPreferenceType.PAM_EXTERNAL_CONTROL)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ExternalControlInterface;
	}

	public void ExternalControlInterface (org.csapi.pam.access.IpAppPAMPreferenceCheck _x)
	{
		discriminator = org.csapi.pam.TpPAMPreferenceType.PAM_EXTERNAL_CONTROL;
		ExternalControlInterface = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.pam.TpPAMPreferenceType.PAM_ACCESS_LIST)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.pam.TpPAMPreferenceType.PAM_ACCESS_LIST;
		Dummy = _x;
	}

	public void Dummy (org.csapi.pam.TpPAMPreferenceType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.pam.TpPAMPreferenceType.PAM_ACCESS_LIST)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
