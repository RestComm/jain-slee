package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallAppInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAppInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallAppInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallAppInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallAppInfo:1.0";
	}
	public static TpCallAppInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpCallAppInfo result = new TpCallAppInfo ();
		org.csapi.cc.TpCallAppInfoType disc = org.csapi.cc.TpCallAppInfoType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_ALERTING_MECHANISM:
			{
				int _var;
				_var=in.read_long();
				result.CallAppAlertingMechanism (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_NETWORK_ACCESS_TYPE:
			{
				org.csapi.cc.TpCallNetworkAccessType _var;
				_var=org.csapi.cc.TpCallNetworkAccessTypeHelper.read(in);
				result.CallAppNetworkAccessType (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_TELE_SERVICE:
			{
				org.csapi.cc.TpCallTeleService _var;
				_var=org.csapi.cc.TpCallTeleServiceHelper.read(in);
				result.CallAppTeleService (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_BEARER_SERVICE:
			{
				org.csapi.cc.TpCallBearerService _var;
				_var=org.csapi.cc.TpCallBearerServiceHelper.read(in);
				result.CallAppBearerService (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_PARTY_CATEGORY:
			{
				org.csapi.cc.TpCallPartyCategory _var;
				_var=org.csapi.cc.TpCallPartyCategoryHelper.read(in);
				result.CallAppPartyCategory (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_PRESENTATION_ADDRESS:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.CallAppPresentationAddress (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_GENERIC_INFO:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.CallAppGenericInfo (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_ADDITIONAL_ADDRESS:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.CallAppAdditionalAddress (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_ORIGINAL_DESTINATION_ADDRESS:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.CallAppOriginalDestinationAddress (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_REDIRECTING_ADDRESS:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.CallAppRedirectingAddress (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_HIGH_PROBABILITY_COMPLETION:
			{
				int _var;
				_var=in.read_long();
				result.CallHighProbabilityCompletion (_var);
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_CARRIER:
			{
				org.csapi.cc.TpCarrier[] _var;
				_var = org.csapi.cc.TpCarrierSetHelper.read(in);
				result.CallAppCarrier (_var);
				break;
			}
			default:
			{
				short _var;
				_var=in.read_short();
				result.Dummy (_var);
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpCallAppInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_ALERTING_MECHANISM:
			{
				out.write_long(s.CallAppAlertingMechanism ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_NETWORK_ACCESS_TYPE:
			{
				org.csapi.cc.TpCallNetworkAccessTypeHelper.write(out,s.CallAppNetworkAccessType ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_TELE_SERVICE:
			{
				org.csapi.cc.TpCallTeleServiceHelper.write(out,s.CallAppTeleService ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_BEARER_SERVICE:
			{
				org.csapi.cc.TpCallBearerServiceHelper.write(out,s.CallAppBearerService ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_PARTY_CATEGORY:
			{
				org.csapi.cc.TpCallPartyCategoryHelper.write(out,s.CallAppPartyCategory ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_PRESENTATION_ADDRESS:
			{
				org.csapi.TpAddressHelper.write(out,s.CallAppPresentationAddress ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_GENERIC_INFO:
			{
				out.write_string(s.CallAppGenericInfo ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_ADDITIONAL_ADDRESS:
			{
				org.csapi.TpAddressHelper.write(out,s.CallAppAdditionalAddress ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_ORIGINAL_DESTINATION_ADDRESS:
			{
				org.csapi.TpAddressHelper.write(out,s.CallAppOriginalDestinationAddress ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_REDIRECTING_ADDRESS:
			{
				org.csapi.TpAddressHelper.write(out,s.CallAppRedirectingAddress ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_HIGH_PROBABILITY_COMPLETION:
			{
				out.write_long(s.CallHighProbabilityCompletion ());
				break;
			}
			case org.csapi.cc.TpCallAppInfoType._P_CALL_APP_CARRIER:
			{
				org.csapi.cc.TpCarrierSetHelper.write(out,s.CallAppCarrier ());
				break;
			}
			default:
			{
				out.write_short(s.Dummy ());
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[13];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_ALERTING_MECHANISM);
			members[12] = new org.omg.CORBA.UnionMember ("CallAppAlertingMechanism", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_NETWORK_ACCESS_TYPE);
			members[11] = new org.omg.CORBA.UnionMember ("CallAppNetworkAccessType", label_any, org.csapi.cc.TpCallNetworkAccessTypeHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_TELE_SERVICE);
			members[10] = new org.omg.CORBA.UnionMember ("CallAppTeleService", label_any, org.csapi.cc.TpCallTeleServiceHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_BEARER_SERVICE);
			members[9] = new org.omg.CORBA.UnionMember ("CallAppBearerService", label_any, org.csapi.cc.TpCallBearerServiceHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_PARTY_CATEGORY);
			members[8] = new org.omg.CORBA.UnionMember ("CallAppPartyCategory", label_any, org.csapi.cc.TpCallPartyCategoryHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_PRESENTATION_ADDRESS);
			members[7] = new org.omg.CORBA.UnionMember ("CallAppPresentationAddress", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_GENERIC_INFO);
			members[6] = new org.omg.CORBA.UnionMember ("CallAppGenericInfo", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_ADDITIONAL_ADDRESS);
			members[5] = new org.omg.CORBA.UnionMember ("CallAppAdditionalAddress", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_ORIGINAL_DESTINATION_ADDRESS);
			members[4] = new org.omg.CORBA.UnionMember ("CallAppOriginalDestinationAddress", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_REDIRECTING_ADDRESS);
			members[3] = new org.omg.CORBA.UnionMember ("CallAppRedirectingAddress", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_HIGH_PROBABILITY_COMPLETION);
			members[2] = new org.omg.CORBA.UnionMember ("CallHighProbabilityCompletion", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallAppInfoTypeHelper.insert(label_any, org.csapi.cc.TpCallAppInfoType.P_CALL_APP_CARRIER);
			members[1] = new org.omg.CORBA.UnionMember ("CallAppCarrier", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.TpCarrierHelper.type()),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpCallAppInfo",org.csapi.cc.TpCallAppInfoTypeHelper.type(), members);
		}
		return _type;
	}
}
