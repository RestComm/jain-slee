/*
        Copyright (C) 1998-2003 Intel Corporation.

        Name:           in_inc.h

        Description:    INAP API interface.

        Procedure and value definitions for user of the INAP functional API.

        -----  ---------  -----  ---------------------------------------------
        Issue    Date      By                     Changes
        -----  ---------  -----  ---------------------------------------------

         A    03-Aug-98   RBP,JET - Initial code.
         1    14-Oct-98   JET     - First release
         2    02-Mar-99   JET     - Further changes to support additional ETSI
                                    operations
                                  - Replaced IN_FAILURE with IN_GENERAL_FAILURE
                                    for use only when no specific error return
                                    code exists.
         3    21-May-99   JET     - Add support for all remaining operations in
                                    ETSI CS-1. This includes new parameter names
                                    where required.
                                  - References to Ellipses corrected to Ellipsis
         4    01-Nov-99   JET     - Support for ITU-T Q.1218 codings of those
                                    operations already supported in ETSI CS-1.
                                  - Support for CAMEL CAP v1, v2.
         5    01-Aug-00  JET      - Addition of further CS-2 definitions.
         6    22-Feb-01  JET      - Correction of INOP_DFC_with_Argument op
                                    code.
         7    11-Jun-01  JET      - INPN_CallAttemptElapsedTimeValue
                                    INPN_CallingPartyBusinessGroupID corrected
                                    spelling
                                  - Value for INPN_TimeAndTimezone changed.
                                  - Addtion of parameters for remaining
                                    ETSI CS-2 operations.
         8    19-Feb-02  YTA      - ConferenceTreatmentIndicator has been
                                    removed and replaced with
                                    Fw_ConferenceTreatmentIndicator and
                                    Bw_ConferenceTreatmentIndicator
         9    29-Aug-02  YTA      - BearerCap_Ellipsis added to BearerCapability
                                    subtable
         10   07-May-03  YTA      - Change to allow User Information parameter to
                                    be formatted.
         11   25-Jun-03  YTA      - Addition of new errors, operation codes &
                                    application contexts to support CAP v3.
         12   09-Dec-03  YTA      - Added INPN_Extension_Type_ObjId to support
                                    Siemens format Extensions parameter.
*/

/*
 *******************************************************************
 *                                                                 *
 *            Dialogue handling definitions                        *
 *                                                                 *
 *******************************************************************
 */

/*
 * Dialogue type definitions.
 *
 * Not all of the dialogue types defined in INAP_INC.H should be used at the
 * API level. The set defined below are valid and will be returned by
 * IN_get_dialogue_type.
 * (Note: the values have been chosen to align with those in INAP_INC.H)
 */
#define INDT_OPEN                            (1)
#define INDT_CLOSE                           (2)
#define INDT_DELIMIT                         (3)
#define INDT_U_ABORT                         (4)
#define INDT_P_ABORT                         (5)
#define INDT_NOTICE                          (6)
#define INDT_OPEN_RSP                        (129)

#define INDT_UNDEFINED                       (0)   /* Functional API use only */


/*
 * Dialogue parameter definitions.
 *
 * These are currently defined to align with the definitions
 * in INAP_INC.H, however IN API users should use the values here.
 */
#define INDP_dest_address                    (1)
#define INDP_orig_address                    (3)
#define INDP_result                          (5)
#define INDP_refuse_rsn                      (6)
#define INDP_release_method                  (7)
#define INDP_user_rsn                        (8)
#define INDP_prov_rsn                        (9)
#define INDP_user_info                       (10)
#define INDP_applic_context                  (11)
#define INDP_source                          (12)
#define INDP_prob_diag                       (13)
#define INDP_dest_FE                         (14)
#define INDP_orig_FE                         (15)
#define INDP_applic_context_index            (27)
#define INDP_release_confirm                 (28)
#define INDP_qos                             (29)

/*
 ******************************************************************************
 *                                                                            *
 * Application Contexts and Indices                                           *
 *                                                                            *
 ******************************************************************************
 */

#define INAC_ETSI_300_374_1       (0x00)         /* ETSI CS-1 */
#define INAC_Q1218                (0x10)         /* ITU-T CS-1 partial support*/
#define INAC_CAP_GSM_09_78        (0x20)         /* CAMEL CAP v1,v2 */
#define INAC_ETSI_301_140_1       (0x30)         /* ETSI CS-2 */
#define INAC_CAP_3GPP_29_78       (0x50)         /* CAMEL CAP v3 */

/*
 * Application Context Indices for use with ESTI ETS 300-374-1
 * Indices 0x0 to 0x6
 */
#define INETS_AC_CS1_SSP_TO_SCP                     (INAC_ETSI_300_374_1 + 0)
#define INETS_AC_CS1_ASSIST_HANDOFF_TO_SSP_TO_SCP   (INAC_ETSI_300_374_1 + 1)
#define INETS_AC_CS1_IP_TO_SCP                      (INAC_ETSI_300_374_1 + 2)
#define INETS_AC_CS1_SCP_TO_SSP                     (INAC_ETSI_300_374_1 + 3)
#define INETS_AC_CS1_SCP_TO_SSP_TRAFFIC_MANAGEMENT  (INAC_ETSI_300_374_1 + 4)
#define INETS_AC_CS1_SCP_TO_SSP_SERVICE_MANAGEMENT  (INAC_ETSI_300_374_1 + 5)
#define INETS_AC_CS1_SSP_TO_SCP_SERVICE_MANAGEMENT  (INAC_ETSI_300_374_1 + 6)

/*
 * Application Context Indices for use with ESTI ETS 301-140-1
 * ETSI Core INAP CS-2
 *
 * Indices in range 0x30 to 0x4f
 *
 * Interfaces SDF - SDF, SCF - SDF not included
 */
#define INETS_AC_CS2_SSF_SCF_GENERIC                  (INAC_ETSI_301_140_1 + 4)
#define INETS_AC_CS2_SSF_SCF_ASSIST_HANDOFF           (INAC_ETSI_301_140_1 + 6)
#define INETS_AC_CS2_SSF_SCF_SERVICE_MANAGEMENT       (INAC_ETSI_301_140_1 + 7)
#define INETS_AC_CS2_SCF_SSF_GENERIC                  (INAC_ETSI_301_140_1 + 8)
#define INETS_AC_CS2_SCF_SSF_TRAFFIC_MANAGEMENT       (INAC_ETSI_301_140_1 + 10)
#define INETS_AC_CS2_SCF_SSF_SERVICE_MANAGEMENT       (INAC_ETSI_301_140_1 + 11)
#define INETS_AC_CS2_SCF_SSF_TRIGGER_MANAGEMENT       (INAC_ETSI_301_140_1 + 13)

#define INETS_AC_CS2_SRF_SCF                          (INAC_ETSI_301_140_1 + 14)

#define INETS_AC_CS2_SCF_SCF_OPERATIONS               (INAC_ETSI_301_140_1 + 21)
#define INETS_AC_CS2_DISTRIBUTED_SCF_SYSTEM           (INAC_ETSI_301_140_1 + 22)
#define INETS_AC_CS2_SCF_SCF_OPERATIONS_WITH_3SE      (INAC_ETSI_301_140_1 + 23)
#define INETS_AC_CS2_DISTRIBUTED_SCF_SYSTEM_WITH_3SE  (INAC_ETSI_301_140_1 + 24)

#define INETS_AC_CS2_SCF_CUSF                         (INAC_ETSI_301_140_1 + 25)
#define INETS_AC_CS2_CUSF_SCF                         (INAC_ETSI_301_140_1 + 26)


/*
 * Application Context Indices for use with ITU-T Q.1218
 * Indices 0x10 to 0x19
 */
#define INITU_CS1_SSF_TO_SCF_GENERIC_AC             (INAC_Q1218 + 0)
#define INITU_CS1_SSF_TO_SCF_DPSPECIFIC_AC          (INAC_Q1218 + 1)
#define INITU_CS1_ASSIST_HANDOFF_SSF_TO_SCF_AC      (INAC_Q1218 + 2)
#define INITU_CS1_SRF_TO_SCF_AC                     (INAC_Q1218 + 3)
#define INITU_CS1_SCF_TO_SSF_AC                     (INAC_Q1218 + 4)
#define INITU_CS1_DP_SPECIFIC_SCF_TO_SSF_AC         (INAC_Q1218 + 5)
#define INITU_CS1_SCF_TO_SSF_TRAFFIC_MANAGEMENT_AC  (INAC_Q1218 + 6)
#define INITU_CS1_SCF_TO_SSF_SERVICE_MANAGEMENT_AC  (INAC_Q1218 + 7)
#define INITU_CS1_SSF_TO_SCF_SERVICE_MANAGEMENT_AC  (INAC_Q1218 + 8)
#define INITU_CS1_SCF_TO_SSF_STATUS_REPORTING_AC    (INAC_Q1218 + 9)


/*
 * Application Context Indices for use with CAMEL CAP (GSM 09.78)
 * Indices 0x20 to 0x23
 */
#define INCAP_V1_GSMSSF_TO_GSMSCF                   (INAC_CAP_GSM_09_78 + 0)
#define INCAP_V2_GSMSSF_TO_GSMSCF                   (INAC_CAP_GSM_09_78 + 1)
#define INCAP_V2_ASSIST_HANDOFF_GSMSSF_TO_GSMSCF    (INAC_CAP_GSM_09_78 + 2)
#define INCAP_V2_GSMSRF_TO_GSMSCF                   (INAC_CAP_GSM_09_78 + 3)

/*
 * Application Context Indices for use with CAMEL CAP (GSM 29.78)
 * Indices 0x50 to 0x55
 */
#define INCAP_V3_GSMSSF_TO_GSMSCF_GENERIC           (INAC_CAP_3GPP_29_78 + 0)
#define INCAP_V3_ASSIST_HANDOFF_GSMSSF_TO_GSMSCF    (INAC_CAP_3GPP_29_78 + 1)
#define INCAP_V3_GSMSRF_TO_GSMSCF                   (INAC_CAP_3GPP_29_78 + 2)
#define INCAP_V3_GPRSSSF_TO_GSMSCF                  (INAC_CAP_3GPP_29_78 + 3)
#define INCAP_V3_GSMSCF_TO_GPRSSSF                  (INAC_CAP_3GPP_29_78 + 4)
#define INCAP_V3_SMS                                (INAC_CAP_3GPP_29_78 + 5)

/*
 *******************************************************************
 *                                                                 *
 *            Service handling definitions                         *
 *                                                                 *
 *******************************************************************
 */

/*
 * Service Type Definitions
 *
 * Values chosen to align with those of the service type primitives in
 * INAP_INC.H
 */
#define INCPT_INVOKE                  (1)
#define INCPT_RESULT_L                (2)
#define INCPT_U_ERROR                 (3)

#define INCPT_NULL                    (0) /* NULL component, no equivalent in */
                                          /* INAP_INC.H  */

/*
 ******************************************************************************
 * Operation Identifier definitions                                           *
 *                                                                            *
 * Values chosen to match the local value definitions in the ASN coding       *
 * definition of the protocols supported.                                     *
 *                                                                            *
 * Any addition, removal or change to the operation values listed below must  *
 * be also reflected in the structure IN_operation_data_table in in_op.c      *
 *                                                                            *
 * Operations are valid in both ETS 300_374_1 and ITU Q.1218 unless stated    *
 * otherwise.                                                                 *
 *                                                                            *
 ******************************************************************************
 */

/*
 * SCF activation ASE
 */
#define INOP_InitialDP                        (0)

/*
 * Basic BCP DP ASE (ITU Q.1218 only)
 */
#define INOP_OriginationAttemptAuthorized     (1)  /* (ITU Q.1218 only) */
#define INOP_CollectedInformation             (2)  /* (ITU Q.1218 only) */
#define INOP_AnalysedInformation              (3)  /* (ITU Q.1218 only) */
#define INOP_RouteSelectFailure               (4)  /* (ITU Q.1218 only) */
#define INOP_OCalledPartyBusy                 (5)  /* (ITU Q.1218 only) */
#define INOP_ONoAnswer                        (6)  /* (ITU Q.1218 only) */
#define INOP_OAnswer                          (7)  /* (ITU Q.1218 only) */
#define INOP_ODisconnect                      (8)  /* (ITU Q.1218 only) */
#define INOP_TermAttemptAuthorized            (9)  /* (ITU Q.1218 only) */
#define INOP_TBusy                            (10) /* (ITU Q.1218 only) */
#define INOP_TNoAnswer                        (11) /* (ITU Q.1218 only) */
#define INOP_TAnswer                          (12) /* (ITU Q.1218 only) */
#define INOP_TDisconnect                      (13) /* (ITU Q.1218 only) */

/*
 * Advanced BCP DP ASE (ITU Q.1218 only)
 */
#define INOP_OMidCall                         (14) /* (ITU Q.1218 only) */
#define INOP_TMidCall                         (15) /* (ITU Q.1218 only) */

/*
 * SCF/SRF activation of assist ASE
 */
#define INOP_AssistRequestInstructions        (16)

/*
 * Assist connection establishment ASE
 */
#define INOP_EstablishTemporaryConnection     (17)

/*
 * Generic disconnect resource ASE
 */
#define INOP_DisconnectForwardConnection      (18)
#define INOP_DFC_with_Argument                (86) /* CS-2 only */

/*
 * Non-assisted connection establishment ASE
 */
#define INOP_ConnectToResource                (19)

/*
 * Connect ASE (elementary SSF function)
 */
#define INOP_Connect                          (20)

/*
 * Call handling ASE (elementary SSF function)
 */
#define INOP_HoldCallInNetwork                (21) /* (ITU Q.1218 only) */
#define INOP_ReleaseCall                      (22)

/*
 * BCSM Event handling ASE
 */
#define INOP_RequestReportBCSMEvent           (23)
#define INOP_EventReportBCSM                  (24)

/*
 * Charging Event handling ASE
 */
#define INOP_RequestNotificationChargingEvent (25)
#define INOP_EventNotificationCharging        (26)

/*
 * SSF call processing ASE
 */
#define INOP_CollectInformation               (27)
#define INOP_AnalyseInformation               (28) /* (ITU Q.1218 only) */
#define INOP_SelectRoute                      (29) /* (ITU Q.1218 only) */
#define INOP_SelectFacility                   (30) /* (ITU Q.1218 only) */
#define INOP_Continue                         (31)
#define INOP_CAPv3_ContinueWithArgument       (56)

/*
 * SCF call initiation ASE
 */
#define INOP_InitiateCallAttempt              (32)

/*
 * Timer ASE
 */
#define INOP_ResetTimer                       (33)

/*
 * Billing ASE
 */
#define INOP_FurnishChargingInformation       (34)

/*
 * Charging ASE
 */
#define INOP_ApplyCharging                    (35)
#define INOP_ApplyChargingReport              (36)

/*
 * Status reporting ASE (ITU Q.1218 only)
 */
#define INOP_RequestCurrentStatusReport       (37)  /* (ITU Q.1218 only) */
#define INOP_RequestEveryStatusChangeReport   (38)  /* (ITU Q.1218 only) */
#define INOP_RequestFirstStatusMatchReport    (39)  /* (ITU Q.1218 only) */
#define INOP_StatusReport                     (40)  /* (ITU Q.1218 only) */

/*
 * Traffic management ASE
 */
#define INOP_CallGap                          (41)

/*
 * Service management ASE
 */
#define INOP_ActivateServiceFiltering         (42)
#define INOP_ServiceFilteringResponse         (43)

/*
 * Call report ASE
 */
#define INOP_CallInformationReport            (44)
#define INOP_CallInformationRequest           (45)

/*
 * Signalling control ASE
 */
#define INOP_SendChargingInformation          (46)

/*
 * Specialized resource control ASE
 */
#define INOP_PlayAnnouncement                 (47)
#define INOP_PromptAndCollectUserInformation  (48)
#define INOP_SpecializedResourceReport        (49)

/*
 * Cancel ASE
 */
#define INOP_Cancel                           (53)
#define INOP_CancelStatusReportRequest        (54)  /* (ITU Q.1218 only) */

/*
 * Activity Test ASE
 */
#define INOP_ActivityTest                     (55)


/*
 * SMS Activation
 */
#define INOP_InitialDPSMS                     (60)

/*
 * SMS Billing
 */
#define INOP_FurnishChargingInfoSMS           (61)

/*
 * SMS Connect
 */
#define INOP_ConnectSMS                       (62)

/*
 * SMS Event Handling
 */
#define INOP_RequestReportSMSRequest          (63)
#define INOP_EventReportSMS                   (64)

/*
 * SMS Processing
 */
#define INOP_ContinueSMS                      (65)

/*
 * SMS Release
 */
#define INOP_ReleaseSMS                       (66)

/*
 * SMS Timer
 */
#define INOP_ResetTimerSMS                    (67)

/*
 * GPRS Activity Test
 */
#define INOP_ActivityTestGPRS                 (70)

/*
 * GPRS Charging
 */
#define INOP_ApplyChargingGPRS                (71)
#define INOP_ApplyChargingReportGPRS          (72)

/*
 * GPRS Cancel
 */
#define INOP_CancelGPRS                       (73)

/*
 * GPRS Connect
 */
#define INOP_ConnectGPRS                      (74)

/*
 * GPRS Processing
 */
#define INOP_ContinueGPRS                     (75)

/*
 * GPRS Exception Information
 */
#define INOP_EntityReleasedGPRS               (76)

/*
 * GPRS Billing
 */
#define INOP_FurnishChargingInfoGPRS          (77)

/*
 * GPRS SCF Activation
 */
#define INOP_InitialDPGPRS                    (78)

/*
 * GPRS Release
 */
#define INOP_ReleaseGPRS                      (79)

/*
 * GPRS Event Handling
 */
#define INOP_EventReportGPRS                  (80)
#define INOP_RequestReportGPRSEvent           (81)

/*
 * GPRS Timer
 */
#define INOP_ResetTimerGPRS                   (82)

/*
 * GPRS Charge Advice
 */
#define INOP_SendChargingInfoGPRS             (83)

/*
 * CPH Response Package
 */
#define INOP_ContinueWithArgument             (88)
#define INOP_CreateCallSegmentAssociation     (89)
#define INOP_DisconnectLeg                    (90)
#define INOP_MergeCallSegments                (91)
#define INOP_MoveCallSegments                 (92)
#define INOP_MoveLeg                          (93)
#define INOP_SplitLeg                         (95)

/*
 * Exception Inform Package
 */
#define INOP_EntityReleased                   (96)

/*
 * Trigger Management Package
 */
#define INOP_ManageTriggerData                (97)

/*
 * USI Handling Package
 */
#define INOP_RequestReportUTSI                (98)
#define INOP_SendSTUI                         (100)
#define INOP_ReportUTSI                       (101)

/*
 * SRF/SCF interface
 */
#define INOP_PromptAndReceiveMessage          (107)
#define INOP_ScriptInformation                (108)
#define INOP_ScriptEvent                      (109)
#define INOP_ScriptRun                        (110)
#define INOP_ScriptClose                      (111)

/*
 * SCF/SCF interface
 */
#define INOP_EstablishChargingRecord          (112)
#define INOP_HandlingInformationRequest       (113)
#define INOP_HandlingInformationResult        (114)
#define INOP_NetworkCapability                (115)
#define INOP_NotificationProvided             (116)
#define INOP_ConfirmedNotificationProvided    (117)
#define INOP_ProvideUserInformation           (118)
#define INOP_ConfirmedReportChargingInformation (119)
#define INOP_ReportChargingInformation        (120)
#define INOP_RequestNotification              (121)

/*
 * CUSF/SCF interface
 */
#define INOP_InitiateAssociation              (123)
#define INOP_ReleaseAssociation               (126)
#define INOP_RequestReportBCUSMEvent          (127)
#define INOP_InitialAssociationDP             (131)
#define INOP_ConnectAssociation               (132)
#define INOP_ContinueAssociation              (133)
#define INOP_EventReportBCUSM                 (134)

/*
 *
 */
#define INOP_NO_OPERATION                     (0xff)

/*
 ******************************************************************************
 *                                                                            *
 * Operation - Error Codes                                                    *
 *                                                                            *
 * Values chosen to match the local value definitions in the ASN coding       *
 * definition of the protocols supported. Not all values valid for all        *
 * protocols or all operations in a particular protocol.                      *
 *                                                                            *
 * A value of INER_provider_error when stored in the component structure      *
 * indicates a problem detected in the TCAP/INAP layers. The problem code     *
 * will be stored in the INPN_provider_error parameter.                       *
 ******************************************************************************
 */
#define INER_Cancelled                        (0)
#define INER_CancelFailed                     (1)
#define INER_ETCFailed                        (3)
#define INER_ImproperCallerResponse           (4)
#define INER_MissingCustomerRecord            (6)
#define INER_MissingParameter                 (7)
#define INER_ParameterOutOfRange              (8)
#define INER_RequestedInfoError               (10)
#define INER_SystemFailure                    (11)
#define INER_TaskRefused                      (12)
#define INER_UnavailableResource              (13)
#define INER_UnexpectedComponentSequence      (14)
#define INER_UnexpectedDataValue              (15)
#define INER_UnexpectedParameter              (16)
#define INER_UnknownLegID                     (17)
#define INER_UnknownResource                  (18)
/*
 *  CS-2 only errors
 */
#define INER_SCF_Referral                     (21)
#define INER_SCF_TaskRefused                  (22)
#define INER_ChainingRefused                  (23)
/*
 * CAP v3 only error
 */
#define INER_UnknownPDPID                     (50)


/*
 * The error codes below are only ever returned from the API and will be
 * rejected by IN_set_Error.
 */
#define INER_provider_error                   (240) /* = 0xf0 */
#define INER_NO_ERROR                         (255) /* = 0xff */


/*
 ******************************************************************************
 *                                                                            *
 * Operation - timeout definitions                                            *
 *                                                                            *
 ******************************************************************************
 */

/*
 * Timeout ranges for operations in seconds
 *
 * ETS 300_374_1 / Q.1218 Ranges are:
 *
 *     short  (1-10 seconds),
 *     medium (1-60 seconds),
 *     long   (1 second - 30 minutes) and
 *     ffs    (for further study).
 *
 * For operations marked in Q.1218 as ffs (for further study) the range is
 * assumed to be between shortest value in the SHORT range and the longest
 * value of a LONG range
 */
#define IN_MIN_TIM_SHORT                      (1)       /* 1 second   */
#define IN_MAX_TIM_SHORT                      (10)      /* 10 seconds */

#define IN_MIN_TIM_MED                        (1)       /* 1 second   */
#define IN_MAX_TIM_MED                        (60)      /* 1 minute   */

#define IN_MIN_TIM_LONG                       (1)       /* 1 second   */
#define IN_MAX_TIM_LONG                       (60 * 30) /* 30 minutes */

/*
 * The value IN_USE_MAX_TIMEOUT below may be used in the in_api.c function
 * IN_set_operation to set the maximum valid timeout value for the operation.
 */
#define IN_USE_MAX_TIMEOUT                    (0)

/*
 ******************************************************************************
 *                                                                            *
 * Parameter name macro INPN_SEQ:                                             *
 *                                                                            *
 * This macro allows the parameter name defintion in the form below           *
 *                                                                            *
 * #define INPN_ParameterName(i)                   (INPN_SEQ(i,27,3))         *
 *                                                           |  | |           *
 *                                                   index___|  | |           *
 *                                                   base_______| |           *
 *                                                   size_________|           *
 *                                                                            *
 * index - used to specify the elememt in a sequence of parameters            *
 * base  - the value of the first parameter name in the sequence              *
 * size  - the number of parameter names defined in the sequence              *
 *                                                                            *
 * E.g.                                                                       *
 * The DestinationRouting address parameter is a SEQ of 1 to 3 called party   *
 * numbers. This is defined as below                                          *
 *                                                                            *
 * #define INPN_DestinationRoutingAddress(i)       (INPN_SEQ(i,27,3))         *
 *                                                                            *
 * In use, this means each element has a parameter name defined as follows    *
 *                                                                            *
 * INPN_DestinationRoutingAddress(0)   ( value of 27  +  0      ->  27 )      *
 * INPN_DestinationRoutingAddress(1)   ( value of 27  +  1      ->  28 )      *
 * INPN_DestinationRoutingAddress(2)   ( value of 27  +  2      ->  29 )      *
 *                                                                            *
 * If an index is specified which is too large for the sequence then a null   *
 * parameter name is returned. This will force it to be rejected by the API   *
 * functions.                                                                 *
 *                                                                            *
 * INPN_DestinationRoutingAddress(>2)  ( value of INPN_Null     ->   0 )      *
 *                                                                            *
 ******************************************************************************
 */
#define INPN_SEQ(i,base,size)              ((i < size) ? (base + i) : INPN_Null)

/*
 ******************************************************************************
 *                                                                            *
 * Parameter Identifier definitions:                                          *
 *                                                                            *
 * Note : the Invoke ID, Linked ID and Linked Op Code are treated as          *
 * service parameters.                                                        *
 *                                                                            *
 ******************************************************************************
 */
#define INPN_Null                               (0)

#define INPN_InvokeID                           (1)
#define INPN_LinkedID                           (2)
#define INPN_Linked_Op_Code                     (57)

/*
 * Params for InitialDPArg
 */
#define INPN_ServiceKey                         (3)
#define INPN_DialledDigits                      (4)
#define INPN_CalledPartyNumber                  (5)
#define INPN_CallingPartyNumber                 (6)
#define INPN_CallingPartyBusinessGroupID        (7)
/*
 * Unsupported, use INPN_CallingPartyBusinessGroupID above.
 */
#define INPN_CallingPartyBuisnessGroupID        (7)
#define INPN_CallingPartysCategory              (8)
#define INPN_CallingPartySubaddress             (9)
#define INPN_cGEncountered                      (10)
#define INPN_IPSSPCapabilities                  (11)
#define INPN_IPAvailable                        (12)
#define INPN_LocationNumber                     (13)
#define INPN_DPAssignment                       (14)
#define INPN_OriginalCalledPartyID              (15)
#define INPN_ServiceProfileIdentifier           (16)
#define INPN_TerminalType                       (17)
#define INPN_TriggerType                        (18)
#define INPN_HighLayerCompatibility             (19)
#define INPN_ServiceInteractionIndicators       (20)
#define INPN_AdditionalCallingPartyNumber       (21)
#define INPN_ForwardCallIndicators              (22)
#define INPN_BearerCapability                   (23)
#define INPN_BCSMEventCorrelationID             (24)
#define INPN_RedirectingPartyID                 (25)
#define INPN_RedirectionInformation             (26)
/*
 * Additional params for ConnectArg
 */
/*
 * For ETSI 300-374-1 only INPN_DestinationRoutingAddress(0) is valid
 * For Q.1218              INPN_DestinationRoutingAddress(0)..(2) are valid
 */
#define INPN_DestinationRoutingAddress(i)(INPN_SEQ(i,27,3)) /* 27-29 reserved */
#define INPN_AlertingPattern                    (30)
#define INPN_CorrelationID                      (31)
#define INPN_CutAndPaste                        (32)
#define INPN_ForwardingCondition                (33)
#define INPN_ISDNAccessRelatedInformation       (34)
/*
 * (35-37) Reserved for RouteList(0..2)
 */
#define INPN_RouteList(i)                (INPN_SEQ(i,35,3)) /* 35-37 reserved */
#define INPN_ScfID                              (38)
#define INPN_TravellingClassMark                (39)
#define INPN_Carrier                            (40)
/*
 * Additional params for ReleaseCallArg
 */
#define INPN_Cause                              (41)
/*
 * Additional params for ReturnError
 */
#define INPN_CancelFailed_Problem               (42) /* CancelFailed Error  */
#define INPN_CancelFailed_Operation             (43) /* CancelFailed Error  */
#define INPN_RequestedInfoError                 (44) /* RequestedInfo Error */
#define INPN_SystemFailure                      (45) /* SystemFailure Error */
#define INPN_TaskRefused                        (46) /* TaskRefused Error   */
/*
 * Additional params for ReturnResult
 */
#define INPN_DigitsResponse                     (47)
#define INPN_IA5Response                        (48)
/*
 * Application Context Handling
 * If application context indicies are not configured then the application
 * context handling will have to be done by the user. This parameter name allows
 * the user to recover the application context from a cpt structure when
 * recovering a message.
 */
#define INPN_ApplicationContextString           (49) /* (optional use) */
/*
 * Provider error Handling
 */
#define INPN_provider_error                     (50)
/*
 * Reject handling
 * Used by the API to store the problem code before sending to the INAP module
 */
#define INPN_problem_code                       (51)

#define INPN_AChBillingChargingCharacteristics  (52)
#define INPN_SendCalculationToSCPIndication     (53)
#define INPN_FCIBillingChargingCharacteristics  (54)
#define INPN_CallResult                         (55)
#define INPN_MessageType                        (56)

/*
 * (57 Reserved for Linked Op Code above)
 */

/*
 * For EventNotificationCharging
 */
#define INPN_EventSpecificInformationCharging   (58)
/*
 * For PromptAndCollectUserInformation
 */
#define INPN_DisconnectFromIPForbidden          (59)

/*
 * For PlayAnnouncement
 */
#define INPN_RequestAnnouncementComplete        (60)

/*
 * For Reset Timer
 */
#define INPN_TimerID                            (61)
#define INPN_TimerValue                         (62)


#define INPN_StartTime                          (63)
/*
 * For Tone (see also INPN_Duration for Tone)
 */
#define INPN_ToneID                             (64)
/*
 * For Text
 */
#define INPN_MessageContent                     (65)
#define INPN_Attributes                         (66)

/*
 * For Generic Numbers
 */
#define INPN_GenericNumber(i)          (INPN_SEQ(i,67,5))   /* 67-71 reserved */

/*
 * For CallGap
 */
#define INPN_Gap_Duration                       (72)
#define INPN_GapInterval                        (73)

/*
 * For PromptAndCollectUserInformation
 */
#define INPN_MinimumNumberOfDigits              (74)
#define INPN_MaximumNumberOfDigits              (75)
#define INPN_EndOfReplyDigit                    (76)
#define INPN_CancelDigit                        (77)
#define INPN_StartDigit                         (78)
#define INPN_FirstDigitTimeOut                  (79)
#define INPN_InterDigitTimeOut                  (80)
#define INPN_Errortreatment                     (81)
#define INPN_InterruptableAnnInd                (82)
#define INPN_VoiceInformation                   (83)
#define INPN_VoiceBack                          (84)

/*
 * For ConnectToResource
 */
#define INPN_IPRoutingAddress                   (85)

/*
 * For Cancel
 */
#define INPN_Cancel_InvokeID                    (86)

/*
 * For SendChargingInformation
 */
#define INPN_SCIBillingChargingCharacteristics  (87)

/*
 * For InbandInfo
 * Use INPN_Duration for InbandInfo and Tone.
 */
#define INPN_NumberOfRepetitions                (88)
#define INPN_Duration                           (89)
#define INPN_Interval                           (90)
/*
 * For InformationToSend
 */
#define INPN_DisplayInformation                 (91)
/*
 * For FilteredCallTreatment
 */
#define INPN_SFBillingChargingCharacteristics   (92)
#define INPN_MaximumNumberOfCounters            (93)

#define INPN_FailureCause                       (94)
#define INPN_BusyCause                          (95)
#define INPN_ReleaseCause                       (96)

#define INPN_NumberOfCalls                      (97)
/*
 * INPN_Filtering_Duration for the Duration paramter in FilteringTimeout
 */
#define INPN_Filtering_Duration                 (98)

#define INPN_CallingAddressValue                (99)
#define INPN_StopTime                           (100)
#define INPN_ControlType                        (101)
#define INPN_AssistingSSPIPRoutingAddress       (102)
#define INPN_ConnectTime                        (103)
#define INPN_Tmr                                (104)
#define INPN_DialledNumber                      (105)
#define INPN_CallingLineID                      (106)
#define INPN_NumberingPlan                      (107)
#define INPN_ResponseCondition                  (108)
#define INPN_SuppressionOfAnnouncement          (109)
#define INPN_OCSIApplicable                     (110)
#define INPN_BothwayThroughConnectionIndicator  (111)

/*
 * For handling the ellipsis at the end of Component Arg structures
 * Anything recovered from the ellipses at the end of an operation will
 * be recovered in native ASN coding into this parameter.
 */
#define INPN_Ellipsis                           (112)
/*
 * For the Ellipsis element in the event specific information
 * Anything recovered from the ellipses at the end of the event info will
 * be recovered in native ASN coding into this parameter.
 */
#define INPN_EventSpecInfo_Ellipsis             (113)
#define INPN_LocationInfo_Ellipsis              (114)
#define INPN_NAInfo_Ellipsis                    (115)
#define INPN_NACarrierInfo_Ellipsis             (116)
#define INPN_InitialDPArgExt_Ellipsis           (117)
#define INPN_RelIfDurExc_Ellipsis               (118)
#define INPN_BearerCap_Ellipsis                 (119)

#define INPN_Ext_BearerServiceCode              (120)
#define INPN_Ext_TeleserviceCode                (121)
#define INPN_CellIdFixedLength                  (122)
#define INPN_LAIFixedLength                     (123)
#define INPN_AgeOfLocationInformation           (124)
#define INPN_GeographicalInformation            (125)
#define INPN_Vlr_Number                         (126)
#define INPN_LI_LocationNumber                  (127)
#define INPN_ExtensionContainer                 (128)
#define INPN_CalledPartyBCDNumber               (129)
#define INPN_IMSI                               (130)
#define INPN_CallReferenceNumber                (131)
#define INPN_MSCAddress                         (132)
#define INPN_TimeAndTimezone                    (703)
#define INPN_GSM_ForwardingPending              (133)
#define INPN_AssumedIdle                        (134)
#define INPN_CamelBusy                          (135)
#define INPN_NetDetNotReachable                 (136)
#define INPN_NotProvidedFromVLR                 (137)
/*
 * The parameter INPN_Include_NA_Info is used to determine if the constructed
 * parameter NA_Info is set in the message. It is not formatted in the message.
 */
#define INPN_Include_NA_Info                    (138)
#define INPN_NACICSelectionType                 (139)
#define INPN_NACarrierID                        (140)
#define INPN_NAOliInfo                          (141)
#define INPN_NAChargeNumber                     (142)

/*
 * INPN_CAMEL_AOC_BeforeAnswer will not be formatted or decoded, it is used to
 * determine if the AOC is for before or after answer.
 * If present AOCBeforeAnswer will be sent, otherwise AOCAfterAnswer will be
 * sent.
 */
#define INPN_CAMEL_AOC_BeforeAnswer             (143)
#define INPN_Initial_CAI_E1                     (144)
#define INPN_Initial_CAI_E2                     (145)
#define INPN_Initial_CAI_E3                     (146)
#define INPN_Initial_CAI_E4                     (147)
#define INPN_Initial_CAI_E5                     (148)
#define INPN_Initial_CAI_E6                     (149)
#define INPN_Initial_CAI_E7                     (150)
#define INPN_Subsequent_CAI_E1                  (151)
#define INPN_Subsequent_CAI_E2                  (152)
#define INPN_Subsequent_CAI_E3                  (153)
#define INPN_Subsequent_CAI_E4                  (154)
#define INPN_Subsequent_CAI_E5                  (155)
#define INPN_Subsequent_CAI_E6                  (156)
#define INPN_Subsequent_CAI_E7                  (157)
#define INPN_TariffSwitchInterval               (158)
#define INPN_MaxCallPeriodDuration              (159)

/*
 * Extension field values moved to higher numbers
 */

/*
 * For DPSpecificCriteria
 */
#define INPN_NumberOfDigits(i)       (INPN_SEQ(i,160,16)) /* 160-175 reserved */
#define INPN_ApplicationTimer(i)     (INPN_SEQ(i,176,16)) /* 176-191 reserved */
/*
 * For BCSMEvent
 */
#define INPN_EventTypeBCSM(i)        (INPN_SEQ(i,192,16)) /* 192-207 reserved */
#define INPN_MonitorMode(i)          (INPN_SEQ(i,208,16)) /* 208-223 reserved */

/*
 * For LegID
 */
#define INPN_SendingSideID(i)        (INPN_SEQ(i,224,16)) /* 224-239 reserved */
#define INPN_ReceivingSideID(i)      (INPN_SEQ(i,240,16)) /* 240-255 reserved */

/*
 * For MessageID
 */
#define INPN_ElementaryMessageIDs(i) (INPN_SEQ(i,256,16)) /* 256-271 reserved */

/*
 * For RequestNotificationChargingEvent
 */
#define INPN_EventTypeCharging(i)    (INPN_SEQ(i,272,16)) /* 272-287 reserved */

/*
 * For CallInformationReport
 *     CallInformationRequest
 */
#define INPN_RequestedInformationType(i)      (INPN_SEQ(i,288,5))/* 288-292 */
#define INPN_CallAttemptElapsedTimeValue(i)   (INPN_SEQ(i,293,5))/* 293-297 */
/*
 * Unsupported, use INPN_CallAttemptElapsedTimeValue(i) above.
 */
#define INPN_CallAttemptEllapsedTimeValue(i)  (INPN_SEQ(i,293,5))
#define INPN_CallStopTimeValue(i)             (INPN_SEQ(i,298,5))/* 298-302 */
#define INPN_CallConnectedElapsedTimeValue(i) (INPN_SEQ(i,303,5))/* 303-307 */
#define INPN_CalledAddressValue(i)            (INPN_SEQ(i,308,5))/* 308-312 */
#define INPN_ReleaseCauseValue(i)             (INPN_SEQ(i,313,5))/* 313-317 */

#define INPN_RelIfDurExc_Tone                   (318)
#define INPN_FreeFormData                       (319)
#define INPN_CallSegmentID                      (320)
#define INPN_SubscriberID                       (321)
#define INPN_MailBoxID                          (322)
#define INPN_Media                              (323)
#define INPN_ScriptId_Local                     (324)
#define INPN_ScriptId_Global                    (325)
#define INPN_ScriptSpecificInfo                 (326)
#define INPN_ScriptResult                       (327)

/*
 * For ServiceFilteringResponse
 */
#define INPN_CounterID(i)            (INPN_SEQ(i,328,100))/* 328-427 reserved */
#define INPN_CounterValue(i)         (INPN_SEQ(i,428,100))/* 428-527 reserved */

/*
 * For VariablePart of ActivateServiceFiltering
 */
#define INPN_Integer(i)              (INPN_SEQ(i,528,5))/* 528-532 reserved */
#define INPN_Number(i)               (INPN_SEQ(i,533,5))/* 533-537 reserved */
#define INPN_Time(i)                 (INPN_SEQ(i,538,5))/* 538-541 reserved */
#define INPN_Date(i)                 (INPN_SEQ(i,543,5))/* 543-547 reserved */
#define INPN_Price(i)                (INPN_SEQ(i,548,5))/* 548-552 reserved */

/*
 * Parameters for InfoToRecord. INPN_CancelDigit defined above (77)
 */
#define INPN_EndOfRecordingDigit                (553)
#define INPN_ReplayDigit                        (554)
#define INPN_RestartRecordingDigit              (555)
#define INPN_RestartAllowed                     (556)
#define INPN_ReplayAllowed                      (557)
#define INPN_ControlDigits_Ellipsis             (558)
#define INPN_InfoToRecord_Ellipsis              (559)

/*
 * ExtensionField values moved to higher numbers
 */
#define INPN_Extension_Type(i)            (INPN_SEQ(i,560,16))/* 560-575 */
#define INPN_Extension_Criticality(i)     (INPN_SEQ(i,576,16))/* 576-591 */
#define INPN_AccessPtName                       (592)
#define INPN_Extension_Field_PrimData(i)  (INPN_SEQ(i,593,16))/* 593-608 */
#define INPN_Extension_Field_ConstData(i) (INPN_SEQ(i,609,16))/* 609-624 */

#define INPN_MessageDeletionTimeOut             (625)
#define INPN_TimeToRecord                       (626)
#define INPN_LastEventIndicator                 (627)

#define INPN_ElementaryMessageID                (628)
#define INPN_Variable_MessageID                 (629)
#define INPN_Record_MessageID                   (630)

#define INPN_ReceivedStatus                     (631)
#define INPN_RecordedMessageID                  (632)
#define INPN_RecordedMessageUnits               (633)

#define INPN_InfoToSend_Ellipsis                (634)
#define INPN_Error_InfoToSend_Ellipsis          (635)
#define INPN_Active                             (636)
#define INPN_AppendFreeFormData                 (637)
#define INPN_CallActive                         (638)
#define INPN_CallForwarded                      (639)
#define INPN_CallReleasedAtTcpExp               (640)
#define INPN_CGlobal_SArea_LAI                  (641)
#define INPN_ChargeNumber                       (642)
#define INPN_ChargingID                         (643)
#define INPN_CUG_Index                          (644)
#define INPN_CUG_Interlock                      (645)
#define INPN_CUG_OutgoingAccess                 (646)
#define INPN_CWTreatmentInd                     (647)
#define INPN_DestinationAddr                    (648)
#define INPN_DestSubscriberNumber               (649)
#define INPN_None                               (650)
#define INPN_ForwardedCall                      (651)
#define INPN_GGSNAddress                        (652)
#define INPN_GMSCAddress                        (653)
#define INPN_GPRSCause                          (654)
#define INPN_GPRSChargingID                     (655)
#define INPN_HoldTreatmentInd                   (656)
#define INPN_InitiateIntegrity                  (657)
#define INPN_MaxElapsedTime                     (658)
#define INPN_MaxTransferredVol                  (659)
#define INPN_MSISDN                             (660)
#define INPN_MSNetworkCapability                (661)
#define INPN_MSRadioAccessCapability            (662)
#define INPN_NonCUGCall                         (663)
#define INPN_OrCall                             (664)
#define INPN_PDPID                              (665)
#define INPN_PDPAddress                         (666)
#define INPN_PDPInitiationType                  (667)
#define INPN_Agreements(i)                      (INPN_SEQ(i,668,16)) /* 668-683 */
#define INPN_NetworkSpecific(i)                 (INPN_SEQ(i,684,16)) /* 684-699 */
#define INPN_FCIBCCS                            (700)
#define INPN_INServiceControlCode               (701)
#define INPN_LocalValue                         (702)
/*
 * 703 Reserved by INPN_TimeAndTimezone above.
 */
#define INPN_USIInformation                     (704)
#define INPN_CompRecSpecInfo                    (705)
#define INPN_AssRelReqSpecInfo                  (706)
#define INPN_EventSpecificInformationTariff     (707)
#define INPN_FCIBCC                             (708)
#define INPN_ECTTreatmentInd                    (709)
#define INPN_Bw_ConferenceTreatmentInd          (710)
#define INPN_Fw_ConferenceTreatmentInd          (711)
#define INPN_CallDiversionTreatmentInd          (712)
#define INPN_CallOfferingTreatmentInd           (713)
#define INPN_CallingPartyRestrictionInd         (714)
#define INPN_FCIBCCS1                           (715)
#define INPN_CallCompletionTreatmentInd         (716)
#define INPN_SuspendTimer                       (717)
#define INPN_ConnectedNumberTreatment           (718)
#define INPN_SuppressCallDiverNot               (719)
#define INPN_SuppressCallTransferNot            (720)
#define INPN_AllowCdINNoPresentInd              (721)
#define INPN_UserDialogueDuratInd               (722)
#define INPN_BackwardGVNS                       (723)
#define INPN_ForwardGVNS                        (724)
#define INPN_CSAID                              (725)
#define INPN_ISDNAddressString                  (726)
#define INPN_EventSpecificInformationBCUSM      (727)
#define INPN_EventTypeBCUSM(i)                (INPN_SEQ(i,728,16)) /* 728-743 */
#define INPN_CallSegment                        (744)
#define INPN_InitialCallSegment                 (745)
#define INPN_NegShtQoSFormat                    (746)
#define INPN_NegLngQoSFormat                    (747)
#define INPN_PDPTypeNumber                      (748)
#define INPN_PDPTypeOrganization                (749)
#define INPN_ReleaseIfDurExceeded               (750)
#define INPN_ReqShtQoSFormat                    (751)
#define INPN_ReqLngQoSFormat                    (752)
#define INPN_RouteNotPermitted                  (753)
#define INPN_RoutingAreaID                      (754)
#define INPN_RoutingAreaUpdate                  (755)
#define INPN_RO_TmGPRSNoTariffSwch              (756)
#define INPN_RO_TmGPRSLastTariffSwch            (757)
#define INPN_RO_TmGPRSTariffSwchInt             (758)
#define INPN_RO_VolLastTariffSwch               (759)
#define INPN_RO_VolTariffSwchInt                (760)
#define INPN_RO_VolIfNoTariffSwch               (761)
#define INPN_RPCause                            (762)
#define INPN_SAIPresent                         (763)
#define INPN_SecondaryPDPContext                (764)
#define INPN_SelectLSAIdentity                  (765)
#define INPN_SGSNCapabilities                   (766)
#define INPN_SGSNNumber                         (767)
#define INPN_SMSCAddress                        (768)
#define INPN_SMSCause                           (769)
#define INPN_SMSReferenceNumber                 (770)
#define INPN_SubShtQoSFormat                    (771)
#define INPN_SubLngQoSFormat                    (772)
#define INPN_TmGPRSNoTariffSwch                 (773)
#define INPN_TmGPRSSinceLastTariffSwch          (774)
#define INPN_TmGPRSTariffSwchInt                (775)
#define INPN_TmIfNoTariffSwch                   (776)
#define INPN_TmSinceTariffSwch                  (777)
#define INPN_Tone                               (778)
#define INPN_TPDataCodingScheme                 (779)
#define INPN_TPProtocolID                       (780)
#define INPN_TPShtMsgSubInfo                    (781)
#define INPN_TPValidityPeriod                   (782)
#define INPN_VolLastTariffSwch                  (783)
#define INPN_VolTariffSwchInt                   (784)
#define INPN_VolIfNoTariffSwch                  (785)
/*
 * 786 - 793 spare
 */
#define INPN_EventTypeTariff(i)               (INPN_SEQ(i,794,16)) /* 794-809 */
#define INPN_Crgt                               (810)
#define INPN_Aocrg                              (811)
#define INPN_USIMonitorMode(i)                (INPN_SEQ(i,812,16)) /* 812-827 */
#define INPN_Global(i)                        (INPN_SEQ(i,828,16)) /* 828-843 */
#define INPN_Local(i)                         (INPN_SEQ(i,844,16)) /* 844-859 */
#define INPN_AbandonCause                       (860)
#define INPN_EventTypeSMS(i)                  (INPN_SEQ(i,861,16)) /* 861-876 */
#define INPN_GPRSEventType(i)                 (INPN_SEQ(i,877,16)) /* 877-892 */
#define INPN_ComServiceKey                      (893)
#define INPN_ComCalledAddressValue              (894)
#define INPN_GPRSGeographicalInformation        (895)
#define INPN_GPRSExtensionContainer             (896)
#define INPN_GPRSRoutingAreaID                  (897)

#define INPN_Extension_Type_ObjId(i)         (INPN_SEQ(i,898,16))/* 898-913 */
/*
 * 914 - 931 spare
 */
#define INPN_ChargingTariffInformation          (932)
#define INPN_SFTarrifMessage_Ellipsis           (933)
#define INPN_GenericName                        (934)
#define INPN_NewCallSegmentAssociation          (935)
#define INPN_TargetCallSegment                  (936)
#define INPN_TargetCallSegmentAssociation       (937)
/*
 * 938 - 941 spare
 */
#define INPN_InitiatingEntity                   (942)
#define INPN_EntityReleased_Reason              (943)
#define INPN_EntityReleased_Cause               (944)
#define INPN_CSFailure_Ellipsis                 (945)
#define INPN_BCSMFailure_Ellipsis               (946)
#define INPN_ActionIndicator                    (947)
#define INPN_ActionPerformed                    (948)
#define INPN_RegistratorIdentifier              (949)
#define INPN_TriggerDataId_Ellipsis             (950)
#define INPN_TrunkGroupID                       (951)
#define INPN_PrivateFacilityID                  (952)
#define INPN_HuntGroup                          (953)
#define INPN_RouteIndex                         (954)
#define INPN_IA5Information                     (955)
#define INPN_TariffMessage                      (956)
/*
 * 957-971 spare
 */
#define INPN_SourceLeg(i)                 (INPN_SEQ(i,972,16))/* 972-987 */
#define INPN_NewLeg(i)                    (INPN_SEQ(i,988,16))/* 988-1003 */
#define INPN_CallSegmentID_Array(i)       (INPN_SEQ(i,1004,16))/* 1004-1019 */
#define INPN_SourceCallSegment(i)         (INPN_SEQ(i,1020,16))/* 1020-1035 */
#define INPN_NewCallSegment(i)            (INPN_SEQ(i,1036,16))/* 1036-1051 */

/*
 * For ErrorInfo InformationToSend
 */
#define INPN_Error_ElementaryMessageIDs(i) (INPN_SEQ(i,1052,16))/* 1052-1067 */
#define INPN_Error_Integer(i)              (INPN_SEQ(i,1068,5)) /* 1068-1072 */
#define INPN_Error_Number(i)               (INPN_SEQ(i,1073,5)) /* 1073-1077 */
#define INPN_Error_Time(i)                 (INPN_SEQ(i,1078,5)) /* 1078-1082 */
#define INPN_Error_Date(i)                 (INPN_SEQ(i,1083,5)) /* 1083-1087 */
#define INPN_Error_Price(i)                (INPN_SEQ(i,1088,5)) /* 1088-1092 */
#define INPN_Error_NumberOfRepetitions          (1093)
#define INPN_Error_Duration                     (1094)
#define INPN_Error_Interval                     (1095)
#define INPN_Error_DisplayInformation           (1096)
#define INPN_NumberOfAllowedRetries             (1097)
#define INPN_TypeOfRequestedInfo                (1098)
#define INPN_CallRecord_Ellipsis                (1099)
#define INPN_UserAbandon(i)                (INPN_SEQ(i,1100,16)) /* 1100-1115 */
#define INPN_CallFailure(i)                (INPN_SEQ(i,1116,16)) /* 1116-1131 */
#define INPN_NoReply(i)                    (INPN_SEQ(i,1132,16)) /* 1132-1147 */
#define INPN_CallRelease(i)                (INPN_SEQ(i,1148,16)) /* 1148-1163 */
#define INPN_SS_Invocation(i)              (INPN_SEQ(i,1164,16)) /* 1164-1179 */
#define INPN_CreditLimitReached(i)         (INPN_SEQ(i,1180,16)) /* 1180-1195 */
#define INPN_CallDuration(i)               (INPN_SEQ(i,1196,16)) /* 1196-1211 */
#define INPN_CalledNumber(i)               (INPN_SEQ(i,1212,16)) /* 1212-1227 */
#define INPN_AnsweredCall(i)               (INPN_SEQ(i,1228,16)) /* 1228-1243 */
#define INPN_CurrencyID                         (1244)
#define INPN_Amount                             (1245)
#define INPN_CurrencyValue_Ellipsis             (1246)
#define INPN_UnitsPerInterval                   (1247)
#define INPN_TimePerInterval                    (1248)
#define INPN_ScalingFactor                      (1249)
#define INPN_InitialUnitIncrement               (1250)
#define INPN_UnitsPerDataInterval               (1251)
#define INPN_SegmentsPerDataInterval            (1252)
#define INPN_InitialTimeInterval                (1253)
#define INPN_ChargingParameters_Ellipsis        (1254)
#define INPN_Error_MessageContent               (1255)
#define INPN_Error_Attributes                   (1256)
#define INPN_Error_Variable_MessageID           (1257)
#define INPN_Error_ElementaryMessageID          (1258)
#define INPN_Error_ToneID                       (1259)
#define INPN_RoutingProhibited                  (1260)
#define INPN_IA5String(i)                  (INPN_SEQ(i,1261,16)) /* 1261-1276*/
#define INPN_SupplimentaryServices              (1277)
#define INPN_ReportExpected                     (1278)
#define INPN_RequestedType                      (1279)
#define INPN_NumberOfCallAttempts               (1280)
#define INPN_InvokedSupplementaryService        (1281)
#define INPN_ActiveSupplementaryServices        (1282)
#define INPN_CauseOfLastCallFailure             (1283)
#define INPN_UserInteractionModes               (1284)
#define INPN_PreferredLanguage                  (1285)
#define INPN_Bits_BearerCapabilities            (1286)
#define INPN_Bits_HighLayerCompatiblities       (1287)
#define INPN_Notification                       (1288)
#define INPN_UniqueCallID                       (1289)
#define INPN_Actions                            (1290)
#define INPN_AccountNumber                      (1291)
#define INPN_TimeStamp                          (1292)
#define INPN_InvokableService                   (1293)
/*
 * Security Parameters from X.511
 */
#define INPN_CertificationPath                  (1294)
#define INPN_DistinguishedName                  (1295)
#define INPN_UTCTime                            (1296)
#define INPN_Random                             (1297)
#define INPN_Target                             (1298)
#define INPN_Response                           (1299)
#define INPN_OperationCode                      (1300)
#define INPN_AttributeCertificationPath         (1301)
#define INPN_ErrorProtection                    (1302)

#define INPN_ChargingEvent_Ellipsis(i)     (INPN_SEQ(i,1303,16)) /* 1303-1318*/
#define INPN_RequestedUTSI_Ellipsis(i)     (INPN_SEQ(i,1319,16)) /* 1319-1334*/
#define INPN_BCSMEvent_Ellipsis(i)         (INPN_SEQ(i,1335,16)) /* 1335-1350*/
#define INPN_BCUSMEvent_Ellipsis(i)        (INPN_SEQ(i,1351,16)) /* 1351-1366*/
#define INPN_FCIGPRSseq_Ellipsis                (1367)
#define INPN_CreditUnit                         (1368)
#define INPN_LocationInfoGPRS_Ellipsis          (1369)
#define INPN_QoS_Ellipsis                       (1370)
#define INPN_EventSpecInfoSMS_Ellipsis          (1371)
#define INPN_EventSpecInfoGPRS_Ellipsis         (1372)
#define INPN_TmDurCharRes_Ellipsis              (1373)
#define INPN_TmDurChar_Ellipsis                 (1374)
#define INPN_Tone_Ellipsis                      (1375)
#define INPN_ReqInfo_Ellipsis(i)           (INPN_SEQ(i,1376,5)) /* 1376-1380*/
#define INPN_MiscCall_Ellipsis                  (1381)
#define INPN_InbandInfo_Ellipsis                (1383)
/*
 * 1383 - 1397 spare
 */
#define INPN_CldAddrServ_Ellipsis               (1398)
#define INPN_FCIBCCSeq_Ellipsis                 (1399)
#define INPN_ClgAddrServ_Ellipsis               (1400)
#define INPN_GapOnServ_Ellipsis                 (1401)
#define INPN_ForwardServIntInd_Ellipsis         (1402)
#define INPN_BackwardServIntInd_Ellipsis        (1403)
#define INPN_ServIntActInd2_Ellipsis            (1404)
#define INPN_AssRelReqSpecInfo_Ellipsis         (1405)
#define INPN_MidCallInfo_Ellipsis               (1406)
#define INPN_MidCallSpecInfo_Ellipsis           (1407)
#define INPN_Abandon_Ellipsis                   (1408)
#define INPN_DiscSpecInfo_Ellipsis              (1409)
#define INPN_AnsSpecInfo_Ellipsis               (1410)
#define INPN_GapInd_Ellipsis                    (1411)
#define INPN_AssCallSeg_Ellipsis                (1412)
#define INPN_AllCallSeg_Ellipsis                (1413)
#define INPN_IPAddrLegID_Ellipsis               (1414)
#define INPN_IPAddrCallSeg_Ellipsis             (1415)

#define INPN_MidCallInfoType0_Ellipsis(i)       (INPN_SEQ(i,1416,16)) /* 1416-1431 */
#define INPN_MidCallCtrlSeq0_Ellipsis(i)        (INPN_SEQ(i,1432,16)) /* 1432-1447 */
#define INPN_INServiceControlCodeLow0(i)        (INPN_SEQ(i,1448,16)) /* 1448-1463 */
#define INPN_INServiceControlCodeHigh0(i)       (INPN_SEQ(i,1464,16)) /* 1464-1479 */
#define INPN_MidCallReportType0(i)              (INPN_SEQ(i,1480,16)) /* 1480-1495 */

#define INPN_MidCallInfoType1_Ellipsis(i)       (INPN_SEQ(i,1496,16)) /* 1496-1511 */
#define INPN_MidCallCtrlSeq1_Ellipsis(i)        (INPN_SEQ(i,1512,16)) /* 1512-1527 */
#define INPN_INServiceControlCodeLow1(i)        (INPN_SEQ(i,1528,16)) /* 1528-1543 */
#define INPN_INServiceControlCodeHigh1(i)       (INPN_SEQ(i,1544,16)) /* 1544-1559 */
#define INPN_MidCallReportType1(i)              (INPN_SEQ(i,1560,16)) /* 1560-1575 */

#define INPN_MidCallInfoType2_Ellipsis(i)       (INPN_SEQ(i,1576,16)) /* 1576-1591 */
#define INPN_MidCallCtrlSeq2_Ellipsis(i)        (INPN_SEQ(i,1592,16)) /* 1592-1607 */
#define INPN_INServiceControlCodeLow2(i)        (INPN_SEQ(i,1608,16)) /* 1608-1623 */
#define INPN_INServiceControlCodeHigh2(i)       (INPN_SEQ(i,1624,16)) /* 1624-1639 */
#define INPN_MidCallReportType2(i)              (INPN_SEQ(i,1640,16)) /* 1640-1655 */

#define INPN_MidCallInfoType3_Ellipsis(i)       (INPN_SEQ(i,1656,16)) /* 1656-1671 */
#define INPN_MidCallCtrlSeq3_Ellipsis(i)        (INPN_SEQ(i,1672,16)) /* 1672-1687 */
#define INPN_INServiceControlCodeLow3(i)        (INPN_SEQ(i,1688,16)) /* 1688-1703 */
#define INPN_INServiceControlCodeHigh3(i)       (INPN_SEQ(i,1704,16)) /* 1704-1719 */
#define INPN_MidCallReportType3(i)              (INPN_SEQ(i,1720,16)) /* 1720-1735 */
/*
 * 1736 - 2683 reserved if upto 12 more subtables added to IN_CS2_MidCallControlInfo_tab
 */
#define INPN_Extension_Type2(i)            (INPN_SEQ(i,2700,16))/* 2700-2715 */
#define INPN_Extension_Criticality2(i)     (INPN_SEQ(i,2716,16))/* 2716-2731 */
#define INPN_Extension_Field_PrimData2(i)  (INPN_SEQ(i,2732,16))/* 2732-2747 */
#define INPN_Extension_Field_ConstData2(i) (INPN_SEQ(i,2748,16))/* 2748-2763 */

/*
 ******************************************************************************
 *                                                                            *
 * Parameter Length definitions                                               *
 *                                                                            *
 * Protocol specific lengths for ETS 300 374 1 have the prefix 'INPE_'        *
 * Protocol specific lengths for ITU Q 1218    have the prefix 'INPI_'        *
 * Protocol specific lengths for CAP GSM 09.78 have the prefix 'INPC_'        *
 *                                                                            *
 ******************************************************************************
 */
#define INPE_highLayerCompatibilityLength (2) /*As ETS 300_374_1, Q.1218*/
#define INPE_minAChBillingChargingLength  (1) /*arbitary min, network specific*/
#define INPE_maxAChBillingChargingLength  (15)/*arbitary max, network specific*/
#define INPE_minAttributesLength          (1) /*arbitary min, network specific*/
#define INPE_maxAttributesLength          (15)/*arbitary max, network specific*/
#define INPE_maxBearerCapabilityLength    (15)/*arbitary max, network specific*/
#define INPE_minCalledPartyNumberLength   (4) /*from ISUP, network specific*/
#define INPE_maxCalledPartyNumberLength   (11)/*from ISUP, network specific*/
#define INPE_minCallingPartyNumberLength  (4) /*from ISUP, network specific*/
#define INPE_maxCallingPartyNumberLength  (12)/*from ISUP, network specific*/
#define INPE_minCallResultLength          (1) /*arbitary min, network specific*/
#define INPE_maxCallResultLength          (15)/*arbitary max, network specific*/
#define INPE_minCauseLength               (2) /*As ETS 300_374_1, Q.1218*/
#define INPE_maxCauseLength               (15)/*arbitary max, network specific*/
#define INPE_minDigitsLength              (1) /*arbitary min, network specific*/
#define INPE_maxDigitsLength              (15)/*arbitary max, network specific*/
#define INPE_minDisplayInformationLength  (1) /*arbitary min, network specific*/
#define INPE_maxDisplayInformationLength  (15)/*arbitary max, network specific*/
#define INPE_minEventSpecificInformationChargingLength (1)
                                            /*arbitary min, network specific*/
#define INPE_maxEventSpecificInformationChargingLength (15)
                                            /*arbitary max, network specific*/
#define INPE_minEventTypeChargingLength   (1) /*arbitary min, network specific*/
#define INPE_maxEventTypeChargingLength   (15)/*arbitary max, network specific*/
#define INPE_minFCIBillingChargingLength  (1) /*arbitary min, network specific*/
#define INPE_maxFCIBillingChargingLength  (15)/*arbitary max, network specific*/
#define INPE_minIPAvailableLength         (0) /*arbitary min, network specific*/
#define INPE_maxIPAvailableLength         (15)/*arbitary max, network specific*/
#define INPE_minIPSSPCapabilitiesLength   (1) /*arbitary min, network specific*/
#define INPE_maxIPSSPCapabilitiesLength   (15)/*arbitary max, network specific*/
#define INPE_minLocationNumberLength      (1) /*arbitary min, network specific*/
#define INPE_maxLocationNumberLength      (15)/*arbitary max, network specific*/
#define INPE_minMessageContentLength      (1) /*arbitary min, network specific*/
#define INPE_maxMessageContentLength      (15)/*arbitary max, network specific*/
#define INPE_minOriginalCalledPartyIDLength (4)  /*from ISUP, network specific*/
#define INPE_maxOriginalCalledPartyIDLength (12) /*from ISUP, network specific*/
#define INPE_minRedirectingPartyIDLength   (4)/*from ISUP, network specific*/
#define INPE_maxRedirectingPartyIDLength  (12)/*from ISUP, network specific*/
#define INPE_minRouteListLength           (1) /*arbitary min, network specific*/
#define INPE_maxRouteListLength           (15)/*arbitary max, network specific*/
#define INPE_minScfIDLength               (1) /*arbitary min, network specific*/
#define INPE_maxScfIDLength               (15)/*arbitary max, network specific*/
#define INPE_minSCIBillingChargingLength  (1) /*arbitary min, network specific*/
#define INPE_maxSCIBillingChargingLength  (15)/*arbitary max, network specific*/
#define INPE_minServiceInteractionIndicatorsLength (1)
                                            /*arbitary min, network specific*/
#define INPE_maxServiceInteractionIndicatorsLength (15)
                                            /*arbitary max, network specific*/
#define INPE_minSFBillingChargingLength   (1) /*arbitary min, network specific*/
#define INPE_maxSFBillingChargingLength   (15)/*arbitary max, network specific*/
#define INPE_numOfBCSMEvents              (16)/*Num supported by coding table*/
#define INPE_numOfChargingEvents          (16) /*arbitary num, network specific*/
#define INPE_numOfCounters                (100)/*As ETS 300_374_1, Q.1218*/
#define INPE_numOfExtensions              (1) /*arbitary num, network specific*/
#define INPE_numOfInfoItems               (5) /*As ETS 300_374_1, Q.1218*/
#define INPE_numOfMessageIDs              (1) /*arbitary num, network specific*/



#define INPI_highLayerCompatibilityLength (2) /*As ETS 300_374_1, Q.1218*/
#define INPI_minAChBillingChargingLength  (1) /*arbitary min, network specific*/
#define INPI_maxAChBillingChargingLength  (15)/*arbitary max, network specific*/
#define INPI_minAttributesLength          (1) /*arbitary min, network specific*/
#define INPI_maxAttributesLength          (15)/*arbitary max, network specific*/
#define INPI_maxBearerCapabilityLength    (15)/*arbitary max, network specific*/
#define INPI_minCalledPartyNumberLength   (4) /*from ISUP, network specific*/
#define INPI_maxCalledPartyNumberLength   (11)/*from ISUP, network specific*/
#define INPI_minCallingPartyNumberLength  (4) /*from ISUP, network specific*/
#define INPI_maxCallingPartyNumberLength  (12)/*from ISUP, network specific*/
#define INPI_minCallResultLength          (1) /*arbitary min, network specific*/
#define INPI_maxCallResultLength          (15)/*arbitary max, network specific*/
#define INPI_minCauseLength               (2) /*As ETS 300_374_1, Q.1218*/
#define INPI_maxCauseLength               (15)/*arbitary max, network specific*/
#define INPI_minDigitsLength              (1) /*arbitary min, network specific*/
#define INPI_maxDigitsLength              (15)/*arbitary max, network specific*/
#define INPI_minDisplayInformationLength  (1) /*arbitary min, network specific*/
#define INPI_maxDisplayInformationLength  (15)/*arbitary max, network specific*/
#define INPI_minEventSpecificInformationChargingLength (1)
                                            /*arbitary min, network specific*/
#define INPI_maxEventSpecificInformationChargingLength (15)
                                            /*arbitary max, network specific*/
#define INPI_minEventTypeChargingLength   (1) /*arbitary min, network specific*/
#define INPI_maxEventTypeChargingLength   (15)/*arbitary max, network specific*/
#define INPI_minFCIBillingChargingLength  (1) /*arbitary min, network specific*/
#define INPI_maxFCIBillingChargingLength  (15)/*arbitary max, network specific*/
#define INPI_minIPAvailableLength         (0) /*arbitary min, network specific*/
#define INPI_maxIPAvailableLength         (15)/*arbitary max, network specific*/
#define INPI_minIPSSPCapabilitiesLength   (1) /*arbitary min, network specific*/
#define INPI_maxIPSSPCapabilitiesLength   (15)/*arbitary max, network specific*/
#define INPI_minLocationNumberLength      (1) /*arbitary min, network specific*/
#define INPI_maxLocationNumberLength      (15)/*arbitary max, network specific*/
#define INPI_minMessageContentLength      (1) /*arbitary min, network specific*/
#define INPI_maxMessageContentLength      (15)/*arbitary max, network specific*/
#define INPI_minOriginalCalledPartyIDLength (4)  /*from ISUP, network specific*/
#define INPI_maxOriginalCalledPartyIDLength (12) /*from ISUP, network specific*/
#define INPI_minRedirectingPartyIDLength  (4)/*from ISUP, network specific*/
#define INPI_maxRedirectingPartyIDLength  (12)/*from ISUP, network specific*/
#define INPI_minRouteListLength           (1) /*arbitary min, network specific*/
#define INPI_maxRouteListLength           (15)/*arbitary max, network specific*/
#define INPI_minScfIDLength               (1) /*arbitary min, network specific*/
#define INPI_maxScfIDLength               (15)/*arbitary max, network specific*/
#define INPI_minSCIBillingChargingLength  (1) /*arbitary min, network specific*/
#define INPI_maxSCIBillingChargingLength  (15)/*arbitary max, network specific*/
#define INPI_minServiceInteractionIndicatorsLength (1)
                                            /*arbitary min, network specific*/
#define INPI_maxServiceInteractionIndicatorsLength (15)
                                            /*arbitary max, network specific*/
#define INPI_minSFBillingChargingLength   (1) /*arbitary min, network specific*/
#define INPI_maxSFBillingChargingLength   (15)/*arbitary max, network specific*/
#define INPI_numOfBCSMEvents              (1) /*arbitary num, network specific*/
#define INPI_numOfChargingEvents          (1) /*arbitary num, network specific*/
#define INPI_numOfCounters                (100)/*As ETS 300_374_1, Q.1218*/
#define INPI_numOfExtensions              (1) /*arbitary num, network specific*/
#define INPI_numOfInfoItems               (5) /*As ETS 300_374_1, Q.1218*/
#define INPI_numOfMessageIDs              (1) /*arbitary num, network specific*/

/*
 * All values from CAMEL CAP (GSM 09.78)
 */
#define INPC_minAChBillingChargingLength     (5)
#define INPC_maxAChBillingChargingLength     (177)
#define INPC_minAttributesLength             (2)
#define INPC_maxAttributesLength             (10)
#define INPC_maxBearerCapabilityLength       (11)
#define INPC_minCallResultLength             (12)
#define INPC_maxCallResultLength             (24)
#define INPC_minCalledPartyBCDNumberLength   (1)
#define INPC_maxCalledPartyBCDNumberLength   (41)
#define INPC_minCalledPartyNumberLength      (3)
#define INPC_maxCalledPartyNumberLength      (12)
#define INPC_minCallingPartyNumberLength     (2)
#define INPC_maxCallingPartyNumberLength     (10)
#define INPC_minCauseLength                  (2)
#define INPC_maxCauseLength                  (2)
#define INPC_minDigitsLength                 (2)
#define INPC_maxDigitsLength                 (11)
#define INPC_minFCIBillingChargingDataLength (1)
#define INPC_maxFCIBillingChargingDataLength (40)
#define INPC_minFCIBillingChargingLength     (5)
#define INPC_maxFCIBillingChargingLength     (49)
#define INPC_minGenericNumberLength          (3)
#define INPC_maxGenericNumberLength          (11)
#define INPC_minIPSSPCapabilitiesLength      (1)
#define INPC_maxIPSSPCapabilitiesLength      (4)
#define INPC_minLocationNumberLength         (2)
#define INPC_maxLocationNumberLength         (10)
#define INPC_minMessageContentLength         (1)
#define INPC_maxMessageContentLength         (127
#define INPC_minOriginalCalledPartyIDLength  (2)
#define INPC_maxOriginalCalledPartyIDLength  (10)
#define INPC_minRedirectingPartyIDLength     (2)
#define INPC_maxRedirectingPartyIDLength     (10)
#define INPC_minScfIDLength                  (2)
#define INPC_maxScfIDLength                  (10)
#define INPC_minSCIBillingChargingLength     (4)
#define INPC_maxSCIBillingChargingLength     (69)
#define INPC_minTimeAndTimezoneLength        (8)
#define INPC_maxTimeAndTimezoneLength        (8)
#define INPC_numOfBCSMEvents                 (10)
#define INPC_numOfExtensions                 (10)
#define INPC_numOfGenericNumbers             (5)
#define INPC_numOfInfoItems                  (4)
#define INPC_numOfMessageIDs                 (5)
/*
 *******************************************************************
 *                                                                 *
 *           Return codes for INAP API functions.                  *
 *                                                                 *
 *******************************************************************
 */

#define IN_SUCCESS                              (0)

#define IN_GENERAL_FAILURE                      (-1)
#define IN_INVALID_MESSAGE_HEADER               (-2)
#define IN_MESSAGE_BUFFER_OVERFLOW              (-3)

#define IN_INVALID_PARAM_NAME                   (-4)
#define IN_INVALID_PARAM_LEN                    (-5)
#define IN_INVALID_OPERATION                    (-6)
#define IN_INVALID_ERROR                        (-7)
#define IN_INVALID_TIMEOUT                      (-8)

#define IN_INVALID_PROTOCOL_SPEC                (-9)
#define IN_INVALID_COMPONENT_POINTER            (-10)
#define IN_ERROR_IN_COMPONENT                   (-11)
#define IN_INVALID_COMPONENT_TYPE               (-12)
#define IN_COMPONENT_BUFFER_OVERFLOW            (-13)
#define IN_COMPONENT_CODING_ERROR               (-14)

#define IN_PARAM_NOT_FOUND                      (-15)
#define IN_PARAM_BUFFER_OVERFLOW                (-16)

#define IN_APPLIC_CONTEXT_NOT_FOUND             (-17)
#define IN_APPLIC_CONTEXT_ERROR                 (-18)
#define IN_COMPONENT_NOT_FOUND                  (-19)
#define IN_INVOKE_ID_NOT_FOUND                  (-20)
#define IN_OP_CODE_NOT_FOUND                    (-21)
#define IN_INVALID_LINKED_OPERATION             (-22)
#define IN_INVALID_OPERATION_CLASS              (-23)

#define IN_REJECT_REQUIRED                      (-24)

/*
 * Type definitions for the component.
 *
 * Structure holds the operation & error code explicitly. The user is advised
 * to make use of the supplied functions to access data in the component
 * structure rather than access it directly.
 *
 * The operation parameters are placed in the databuffer in name length data
 * format.
 *
 * The timeout used with the operation may be supplied in this structure.
 * If the timeout supplied is set to IN_USE_MAX_TIMEOUT (defined = 0)
 * a default of the maximum permitted timeout for that operation will be used.
 * A value larger than the permitted range will be rejected.
 *
 * The timeout is given in seconds.
 *
 *
 */
#define IN_MAX_DATABUF_SIZE                     (200)

typedef struct in_cpt
{
  void *prot_spec;      /* Pointer to the protocol spec used by the component */

  s16  first_error;                   /* API Error code of the first failure  */
  u16  first_error_reason;            /* Identifier reason of the first error */

  u16  operation;                     /* Operation Code */
  u16  timeout;                       /* Operation timeout value */
  u16  err;                           /* Operation Error Code */
  u8   type;                          /* Is it an invoke, result or error */
  u8   databuf_offset;                /* Size of completed databuf */
  u8   databuf[IN_MAX_DATABUF_SIZE];  /* Compoment parameters built into here */

}IN_CPT;

/*
 *******************************************************************
 *                                                                 *
 *           Prototypes for IN API functions.                      *
 *                                                                 *
 *******************************************************************
 */

#ifdef LINT_ARGS
/*
 *  User API Initialisation functions.
 */
int IN_init_component(void *prot_spec, IN_CPT *cpt);
int IN_init_applic_contexts(void *prot_spec, u8 sending_module, u8 inap_module_id);
int IN_version(u16 *maj_rev, u16 *min_rev, char **text);

/*
 *  Message buffer handing functions.
 */
HDR *IN_alloc_message(void);
int IN_free_message(HDR *h);
int IN_send_message(u8 sending_user_id, u8 inap_module_id, HDR *h);

/*
 *   Building a Dialogue Request message.
 */
int IN_set_dialogue_param(u16 param, u8  len, u8 *dptr, HDR *h);
int IN_dialogue_open(u16 dlg_id, HDR *h);
int IN_dialogue_close(u16 dlg_id, HDR *h);
int IN_dialogue_delimit(u16 dlg_id, HDR *h);
int IN_dialogue_u_abort(u16 dlg_id, HDR *h);
int IN_dialogue_open_rsp(u16 dlg_id, HDR *h);
/*
 *   Reading a received Dialogue Indication message.
 */
int IN_get_dialogue_type(HDR *h, u8 *dialogue_type);
int IN_get_dialogue_param(u16 param, u8 *dlen, u8 *dptr, u8 max_len, HDR *h);

/*
 *  Placing operation details in a component struct.
 */
int IN_set_operation(u16 op_name, u16 timeout, IN_CPT *cpt);
int IN_set_result(u16 result_op, IN_CPT *cpt);
int IN_set_error(u16 error_op, u16 err_code, IN_CPT *cpt);
/*
 * Builds a parameter into the component structure
 */
int IN_set_component_param (u16 param_name, u8 len, u8 *data_ptr, IN_CPT *cpt);
/*
 * Used to change default length ranges
 */
int IN_set_param_length_range(void *prot_spec, u16 param_name, u8 length_min, u8 length_max);
/*
 *  Coding a Service Request message from a component struct.
 */
int IN_code_operation_invoke(u16 dlg_id, IN_CPT *cpt, HDR *h);
int IN_code_result(u16 dlg_id, IN_CPT *cpt, HDR *h);
int IN_code_error(u16 dlg_id, IN_CPT *cpt, HDR *h);
int IN_code_reject(u16 dlg_id, IN_CPT *cpt, HDR *reject_h);

/*
 *  De-Coding a received Service Indication message into a component struct.
 */
int IN_decode_operation(IN_CPT *cpt, HDR *h);
int IN_decode_result(IN_CPT *cpt, HDR *h);
int IN_decode_error(IN_CPT *cpt, HDR *h);

/*
 *  Reading operation details from a component struct.
 */
int IN_get_component_type(HDR *h, u8 *cpt_type);
int IN_get_component_first_error (IN_CPT *cpt, u16 *error_reason);
int IN_get_operation(IN_CPT *cpt, u16 *op_code);
int IN_get_error(IN_CPT *cpt,u16 *error_code);
int IN_get_component_param(u16 param_name, u8 *data_len, u8 *data_ptr, u8 max_len, IN_CPT *cpt);

#else
/*
 *  User API Initialisation functions.
 */
int IN_init_component();
int IN_init_applic_contexts();
int IN_version();

/*
 *  Message buffer handing functions.
 */
HDR *IN_alloc_message();
int IN_free_message();
int IN_send_message();

/*
 *   Building a Dialogue Request message.
 */
int IN_set_dialogue_param();
int IN_dialogue_open();
int IN_dialogue_close();
int IN_dialogue_delimit();
int IN_dialogue_u_abort();
int IN_dialogue_open_rsp();
/*
 *   Reading a received Dialogue Indication message.
 */
int IN_get_dialogue_type();
int IN_get_dialogue_param();

/*
 *  Placing operation details in a component struct.
 */
int IN_set_operation();
int IN_set_result();
int IN_set_error();
/*
 * Builds a parameter into the component structure
 */
int IN_set_component_param();
/*
 * Used to change default length ranges
 */
int IN_set_param_length_range();

/*
 *  Coding a Service Request message from a component struct.
 */
int IN_code_operation_invoke();
int IN_code_result();
int IN_code_error();
int IN_code_reject();

/*
 *  De-Coding a received Service Indication message into a component struct.
 */
int IN_decode_operation();
int IN_decode_result();
int IN_decode_error();

/*
 *  Reading operation details from a component struct.
 */
int IN_get_component_type();
int IN_get_component_first_error();
int IN_get_operation();
int IN_get_error();
int IN_get_component_param();

#endif


