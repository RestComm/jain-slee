package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpLossDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpLossDescriptor
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpLossDescriptor(){}
	public org.csapi.cm.TpNameDescrpTagInt meanLoss;
	public org.csapi.cm.TpNameDescrpTagInt measurementPeriod;
	public org.csapi.cm.TpNameDescrpTagInt maxLoss;
	public org.csapi.cm.TpNameDescrpTagInt minLoss;
	public org.csapi.cm.TpNameDescrpTagInt lossPriority;
	public org.csapi.cm.TpNameDescrpTagString description;
	public TpLossDescriptor(org.csapi.cm.TpNameDescrpTagInt meanLoss, org.csapi.cm.TpNameDescrpTagInt measurementPeriod, org.csapi.cm.TpNameDescrpTagInt maxLoss, org.csapi.cm.TpNameDescrpTagInt minLoss, org.csapi.cm.TpNameDescrpTagInt lossPriority, org.csapi.cm.TpNameDescrpTagString description)
	{
		this.meanLoss = meanLoss;
		this.measurementPeriod = measurementPeriod;
		this.maxLoss = maxLoss;
		this.minLoss = minLoss;
		this.lossPriority = lossPriority;
		this.description = description;
	}
}
