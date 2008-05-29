package org.csapi.pam;

/**
 *	Generated from IDL definition of union "TpPAMContextData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextData
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.pam.TpPAMContextName discriminator;
	private org.csapi.pam.TpPAMCommunicationContext CommunicationContext;
	private short Dummy;

	public TpPAMContextData ()
	{
	}

	public org.csapi.pam.TpPAMContextName discriminator ()
	{
		return discriminator;
	}

	public org.csapi.pam.TpPAMCommunicationContext CommunicationContext ()
	{
		if (discriminator != org.csapi.pam.TpPAMContextName.PAM_CONTEXT_COMMUNICATION)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CommunicationContext;
	}

	public void CommunicationContext (org.csapi.pam.TpPAMCommunicationContext _x)
	{
		discriminator = org.csapi.pam.TpPAMContextName.PAM_CONTEXT_COMMUNICATION;
		CommunicationContext = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.pam.TpPAMContextName.PAM_CONTEXT_ANY)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.pam.TpPAMContextName.PAM_CONTEXT_ANY;
		Dummy = _x;
	}

	public void Dummy (org.csapi.pam.TpPAMContextName _discriminator, short _x)
	{
		if (_discriminator != org.csapi.pam.TpPAMContextName.PAM_CONTEXT_ANY)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
