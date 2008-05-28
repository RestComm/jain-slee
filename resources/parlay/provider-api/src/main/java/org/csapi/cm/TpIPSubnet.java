package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpIPSubnet"
 *	@author JacORB IDL compiler 
 */

public final class TpIPSubnet
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpIPSubnet(){}
	public java.lang.String subnetNumber;
	public java.lang.String subnetMask;
	public org.csapi.cm.TpIPv4AddType addressType;
	public org.csapi.cm.TpIPVersion IPVersionSupport;
	public TpIPSubnet(java.lang.String subnetNumber, java.lang.String subnetMask, org.csapi.cm.TpIPv4AddType addressType, org.csapi.cm.TpIPVersion IPVersionSupport)
	{
		this.subnetNumber = subnetNumber;
		this.subnetMask = subnetMask;
		this.addressType = addressType;
		this.IPVersionSupport = IPVersionSupport;
	}
}
