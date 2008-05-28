package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpLoadDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadDescriptor
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpLoadDescriptor(){}
	public org.csapi.cm.TpNameDescrpTagInt meanBandwidth;
	public org.csapi.cm.TpNameDescrpTagInt measurementInterval;
	public org.csapi.cm.TpNameDescrpTagInt maxBandwidth;
	public org.csapi.cm.TpNameDescrpTagInt minBandwidth;
	public org.csapi.cm.TpNameDescrpTagInt bandwidthShare;
	public org.csapi.cm.TpNameDescrpTagInt bandwidthWeight;
	public org.csapi.cm.TpNameDescrpTagInt burstSize;
	public org.csapi.cm.TpNameDescrpTagString description;
	public TpLoadDescriptor(org.csapi.cm.TpNameDescrpTagInt meanBandwidth, org.csapi.cm.TpNameDescrpTagInt measurementInterval, org.csapi.cm.TpNameDescrpTagInt maxBandwidth, org.csapi.cm.TpNameDescrpTagInt minBandwidth, org.csapi.cm.TpNameDescrpTagInt bandwidthShare, org.csapi.cm.TpNameDescrpTagInt bandwidthWeight, org.csapi.cm.TpNameDescrpTagInt burstSize, org.csapi.cm.TpNameDescrpTagString description)
	{
		this.meanBandwidth = meanBandwidth;
		this.measurementInterval = measurementInterval;
		this.maxBandwidth = maxBandwidth;
		this.minBandwidth = minBandwidth;
		this.bandwidthShare = bandwidthShare;
		this.bandwidthWeight = bandwidthWeight;
		this.burstSize = burstSize;
		this.description = description;
	}
}
