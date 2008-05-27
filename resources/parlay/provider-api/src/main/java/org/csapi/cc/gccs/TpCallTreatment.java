package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallTreatment"
 *	@author JacORB IDL compiler 
 */

public final class TpCallTreatment
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallTreatment(){}
	public org.csapi.cc.TpCallTreatmentType CallTreatmentType;
	public org.csapi.cc.gccs.TpCallReleaseCause ReleaseCause;
	public org.csapi.cc.TpCallAdditionalTreatmentInfo AdditionalTreatmentInfo;
	public TpCallTreatment(org.csapi.cc.TpCallTreatmentType CallTreatmentType, org.csapi.cc.gccs.TpCallReleaseCause ReleaseCause, org.csapi.cc.TpCallAdditionalTreatmentInfo AdditionalTreatmentInfo)
	{
		this.CallTreatmentType = CallTreatmentType;
		this.ReleaseCause = ReleaseCause;
		this.AdditionalTreatmentInfo = AdditionalTreatmentInfo;
	}
}
