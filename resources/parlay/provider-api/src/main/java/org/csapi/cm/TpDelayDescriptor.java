package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpDelayDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpDelayDescriptor
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDelayDescriptor(){}
	public org.csapi.cm.TpNameDescrpTagInt meanDelay;
	public org.csapi.cm.TpNameDescrpTagInt measurementPeriod;
	public org.csapi.cm.TpNameDescrpTagInt maxDelay;
	public org.csapi.cm.TpNameDescrpTagInt minDelay;
	public org.csapi.cm.TpNameDescrpTagInt delayPriority;
	public org.csapi.cm.TpNameDescrpTagString description;
	public TpDelayDescriptor(org.csapi.cm.TpNameDescrpTagInt meanDelay, org.csapi.cm.TpNameDescrpTagInt measurementPeriod, org.csapi.cm.TpNameDescrpTagInt maxDelay, org.csapi.cm.TpNameDescrpTagInt minDelay, org.csapi.cm.TpNameDescrpTagInt delayPriority, org.csapi.cm.TpNameDescrpTagString description)
	{
		this.meanDelay = meanDelay;
		this.measurementPeriod = measurementPeriod;
		this.maxDelay = maxDelay;
		this.minDelay = minDelay;
		this.delayPriority = delayPriority;
		this.description = description;
	}
}
