package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpJitterDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpJitterDescriptor
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpJitterDescriptor(){}
	public org.csapi.cm.TpNameDescrpTagInt meanJitter;
	public org.csapi.cm.TpNameDescrpTagInt measurementPeriod;
	public org.csapi.cm.TpNameDescrpTagInt maxJitter;
	public org.csapi.cm.TpNameDescrpTagInt minJitter;
	public org.csapi.cm.TpNameDescrpTagInt jitterPriority;
	public org.csapi.cm.TpNameDescrpTagString description;
	public TpJitterDescriptor(org.csapi.cm.TpNameDescrpTagInt meanJitter, org.csapi.cm.TpNameDescrpTagInt measurementPeriod, org.csapi.cm.TpNameDescrpTagInt maxJitter, org.csapi.cm.TpNameDescrpTagInt minJitter, org.csapi.cm.TpNameDescrpTagInt jitterPriority, org.csapi.cm.TpNameDescrpTagString description)
	{
		this.meanJitter = meanJitter;
		this.measurementPeriod = measurementPeriod;
		this.maxJitter = maxJitter;
		this.minJitter = minJitter;
		this.jitterPriority = jitterPriority;
		this.description = description;
	}
}
