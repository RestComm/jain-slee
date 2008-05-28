package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallAppInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAppInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.TpCallAppInfoType discriminator;
	private int CallAppAlertingMechanism;
	private org.csapi.cc.TpCallNetworkAccessType CallAppNetworkAccessType;
	private org.csapi.cc.TpCallTeleService CallAppTeleService;
	private org.csapi.cc.TpCallBearerService CallAppBearerService;
	private org.csapi.cc.TpCallPartyCategory CallAppPartyCategory;
	private org.csapi.TpAddress CallAppPresentationAddress;
	private java.lang.String CallAppGenericInfo;
	private org.csapi.TpAddress CallAppAdditionalAddress;
	private org.csapi.TpAddress CallAppOriginalDestinationAddress;
	private org.csapi.TpAddress CallAppRedirectingAddress;
	private int CallHighProbabilityCompletion;
	private org.csapi.cc.TpCarrier[] CallAppCarrier;
	private short Dummy;

	public TpCallAppInfo ()
	{
	}

	public org.csapi.cc.TpCallAppInfoType discriminator ()
	{
		return discriminator;
	}

	public int CallAppAlertingMechanism ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_ALERTING_MECHANISM)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppAlertingMechanism;
	}

	public void CallAppAlertingMechanism (int _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_ALERTING_MECHANISM;
		CallAppAlertingMechanism = _x;
	}

	public org.csapi.cc.TpCallNetworkAccessType CallAppNetworkAccessType ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_NETWORK_ACCESS_TYPE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppNetworkAccessType;
	}

	public void CallAppNetworkAccessType (org.csapi.cc.TpCallNetworkAccessType _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_NETWORK_ACCESS_TYPE;
		CallAppNetworkAccessType = _x;
	}

	public org.csapi.cc.TpCallTeleService CallAppTeleService ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_TELE_SERVICE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppTeleService;
	}

	public void CallAppTeleService (org.csapi.cc.TpCallTeleService _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_TELE_SERVICE;
		CallAppTeleService = _x;
	}

	public org.csapi.cc.TpCallBearerService CallAppBearerService ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_BEARER_SERVICE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppBearerService;
	}

	public void CallAppBearerService (org.csapi.cc.TpCallBearerService _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_BEARER_SERVICE;
		CallAppBearerService = _x;
	}

	public org.csapi.cc.TpCallPartyCategory CallAppPartyCategory ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_PARTY_CATEGORY)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppPartyCategory;
	}

	public void CallAppPartyCategory (org.csapi.cc.TpCallPartyCategory _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_PARTY_CATEGORY;
		CallAppPartyCategory = _x;
	}

	public org.csapi.TpAddress CallAppPresentationAddress ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_PRESENTATION_ADDRESS)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppPresentationAddress;
	}

	public void CallAppPresentationAddress (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_PRESENTATION_ADDRESS;
		CallAppPresentationAddress = _x;
	}

	public java.lang.String CallAppGenericInfo ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_GENERIC_INFO)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppGenericInfo;
	}

	public void CallAppGenericInfo (java.lang.String _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_GENERIC_INFO;
		CallAppGenericInfo = _x;
	}

	public org.csapi.TpAddress CallAppAdditionalAddress ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_ADDITIONAL_ADDRESS)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppAdditionalAddress;
	}

	public void CallAppAdditionalAddress (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_ADDITIONAL_ADDRESS;
		CallAppAdditionalAddress = _x;
	}

	public org.csapi.TpAddress CallAppOriginalDestinationAddress ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_ORIGINAL_DESTINATION_ADDRESS)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppOriginalDestinationAddress;
	}

	public void CallAppOriginalDestinationAddress (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_ORIGINAL_DESTINATION_ADDRESS;
		CallAppOriginalDestinationAddress = _x;
	}

	public org.csapi.TpAddress CallAppRedirectingAddress ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_REDIRECTING_ADDRESS)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppRedirectingAddress;
	}

	public void CallAppRedirectingAddress (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_REDIRECTING_ADDRESS;
		CallAppRedirectingAddress = _x;
	}

	public int CallHighProbabilityCompletion ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_HIGH_PROBABILITY_COMPLETION)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallHighProbabilityCompletion;
	}

	public void CallHighProbabilityCompletion (int _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_HIGH_PROBABILITY_COMPLETION;
		CallHighProbabilityCompletion = _x;
	}

	public org.csapi.cc.TpCarrier[] CallAppCarrier ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_CARRIER)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallAppCarrier;
	}

	public void CallAppCarrier (org.csapi.cc.TpCarrier[] _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_CARRIER;
		CallAppCarrier = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.TpCallAppInfoType.P_CALL_APP_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.TpCallAppInfoType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.TpCallAppInfoType.P_CALL_APP_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
