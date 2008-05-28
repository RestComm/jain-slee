package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallAdditionalTreatmentInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalTreatmentInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.TpCallTreatmentType discriminator;
	private org.csapi.ui.TpUIInfo InformationToSend;
	private short Dummy;

	public TpCallAdditionalTreatmentInfo ()
	{
	}

	public org.csapi.cc.TpCallTreatmentType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.ui.TpUIInfo InformationToSend ()
	{
		if (discriminator != org.csapi.cc.TpCallTreatmentType.P_CALL_TREATMENT_SIAR)
			throw new org.omg.CORBA.BAD_OPERATION();
		return InformationToSend;
	}

	public void InformationToSend (org.csapi.ui.TpUIInfo _x)
	{
		discriminator = org.csapi.cc.TpCallTreatmentType.P_CALL_TREATMENT_SIAR;
		InformationToSend = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.TpCallTreatmentType.P_CALL_TREATMENT_DEFAULT && discriminator != org.csapi.cc.TpCallTreatmentType.P_CALL_TREATMENT_RELEASE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.TpCallTreatmentType.P_CALL_TREATMENT_DEFAULT;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.TpCallTreatmentType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.TpCallTreatmentType.P_CALL_TREATMENT_DEFAULT && discriminator != org.csapi.cc.TpCallTreatmentType.P_CALL_TREATMENT_RELEASE)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
