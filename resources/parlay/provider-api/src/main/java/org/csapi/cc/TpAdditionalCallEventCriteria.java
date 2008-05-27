package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpAdditionalCallEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpAdditionalCallEventCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.TpCallEventType discriminator;
	private int MinAddressLength;
	private org.csapi.cc.TpCallServiceCode[] OriginatingServiceCode;
	private org.csapi.cc.TpReleaseCause[] OriginatingReleaseCauseSet;
	private org.csapi.cc.TpReleaseCause[] TerminatingReleaseCauseSet;
	private org.csapi.cc.TpCallServiceCode[] TerminatingServiceCode;
	private short Dummy;

	public TpAdditionalCallEventCriteria ()
	{
	}

	public org.csapi.cc.TpCallEventType discriminator ()
	{
		return discriminator;
	}

	public int MinAddressLength ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_COLLECTED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MinAddressLength;
	}

	public void MinAddressLength (int _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_COLLECTED;
		MinAddressLength = _x;
	}

	public org.csapi.cc.TpCallServiceCode[] OriginatingServiceCode ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_SERVICE_CODE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return OriginatingServiceCode;
	}

	public void OriginatingServiceCode (org.csapi.cc.TpCallServiceCode[] _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_SERVICE_CODE;
		OriginatingServiceCode = _x;
	}

	public org.csapi.cc.TpReleaseCause[] OriginatingReleaseCauseSet ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_RELEASE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return OriginatingReleaseCauseSet;
	}

	public void OriginatingReleaseCauseSet (org.csapi.cc.TpReleaseCause[] _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_RELEASE;
		OriginatingReleaseCauseSet = _x;
	}

	public org.csapi.cc.TpReleaseCause[] TerminatingReleaseCauseSet ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_RELEASE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return TerminatingReleaseCauseSet;
	}

	public void TerminatingReleaseCauseSet (org.csapi.cc.TpReleaseCause[] _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_RELEASE;
		TerminatingReleaseCauseSet = _x;
	}

	public org.csapi.cc.TpCallServiceCode[] TerminatingServiceCode ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_SERVICE_CODE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return TerminatingServiceCode;
	}

	public void TerminatingServiceCode (org.csapi.cc.TpCallServiceCode[] _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_SERVICE_CODE;
		TerminatingServiceCode = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_UNDEFINED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_ANALYSED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_CALL_ATTEMPT && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ALERTING && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ANSWER && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_REDIRECTED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_QUEUED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.TpCallEventType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_UNDEFINED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_ANALYSED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_CALL_ATTEMPT && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ALERTING && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ANSWER && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_REDIRECTED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_QUEUED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
