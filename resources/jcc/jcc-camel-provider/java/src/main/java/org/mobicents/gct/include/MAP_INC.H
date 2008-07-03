/*
    Copyright (C) 1997-2004 Intel Corporation

 Name:          map_inc.h

 Description:   Include file for use by modules that interface
		with the MAP module.

		For details of the use of each message and the
		meaning of the parameters please refer to the
		DataKinetics MAP Programmer's Manual.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A    01-Jul-97   JER   - Initial code.
   B	24-Feb-98   JER   - Added trace mask definitions.
   1    01-Mar-99   RBP   - Added MAPPN_timeout & MAPPN_linked_id parameters.
   2    25-Jan-00   HJM   - Added support for USSD
                          - Removed local definition of MAP_TASK_ID
   3	14-Aug-00   JER   - Added support for Anytime interrogation, Provide
   			    subscriber info, and Send imsi.
   4	21-May-01   JER   - Added support for the Send Routing Information for
                            GPRS service.
   5	14-Sep-01   JER   - Added new ellipsis parameters.
   8    26-Oct-01   YTA   - Added support for ProvideRoamingNumber operation
   9    30-Jan-02   YTA   - Addition of new parameter names for the new
                            operations
   10   11-Feb-02   YTA   - Addition of new parameter to support
                            NoRoamingAllowed error
   11   31-May-02   YTA   - Added support for the NoteMMEvent operation, and
                            upgraded the following operations to V3:
                            SendRoutingInfoForSM, MOForwardSM, MTForwardSM and
                            SendRoutingInfo
   12   02-Oct-02   YTA   - Comment added to note that MAPPN_network_node_num
                            is no longer used.
   13   15-Jan-03   JER   - Added selective tracing.
   14   08-May-03   JER   - Added support for Send Authentication and
                            Interrogate SS.
   15   24-Feb-04   YTA   - MAPPN_subscribe_ellipsis was an unecessary parameter
                            and has been removed.
                    JER   - Added reporting of unexpected user messages.
                          - Added MAPF_V2_ERRORS option.
   16   05-Aug-04    XS   - Added support for ActivateSS, DeActivateSS, and 
                            CheckIMEI services.
                          - Added unauth_lcs_cli_diag,
                            posit_method_fail_diag and add_loc_est parameters
                            in ProvideSubscriberLocation response.
 */


/*
 * Message type definitions (values for m->hdr.type)
 */

/*
 * Protocol primitive messages:
 */
#define  MAP_MSG_SRV_REQ    (0xc7e0)
#define  MAP_MSG_SRV_IND    (0x87e1)
#define  MAP_MSG_DLG_REQ    (0xc7e2)
#define  MAP_MSG_DLG_IND    (0x87e3)

/*
 * Non-primitive messages:
 */
#define MAP_MSG_CONFIG          (0x77e4)
#define MAP_MSG_CNF_TIM         (0x77e5)

#define MAP_MSG_ERROR_IND       (0x07e9)
#define MAP_MSG_MAINT_IND       (0x07ea)
#define MAP_MSG_EVENT_IND       (0x07eb)

#define MAP_MSG_TRACE_MASK      (0x57e6)
#define MAP_MSG_S_SELTRACE_MASK (0x57ec)
#define MAP_MSG_S_ERROR_MASK    (0x57e7)
#define MAP_MSG_S_MAINT_MASK    (0x57e8)

/*******************************************************************
 *                                                                 *
 *            Status values used in confirmation messages          *
 *                                                                 *
 *******************************************************************/
#define MAPE_MSG_OK       (0) /* Message received Correctly - Not set explictly just allowed to default*/
#define MAPE_BAD_ID       (1)  /* Inappropriate/invalid id */
#define MAPE_BAD_STATE    (2)  /* Message received in wrong state */
#define MAPE_BAD_MSG      (5)  /* Inappropriate/unrecognised message */
#define MAPE_BAD_PARAM    (6)  /* Invalid parameters in message */
#define MAPE_NO_RESOURCES (7)  /* Insufficient internal resources */



/*******************************************************************
 *                                                                 *
 *               Message Parameter Definitions                     *
 *                                                                 *
 *******************************************************************/

/*
 * Definitions of length, offset and size of
 * MAP_MSG_CONFIG message parameter field:
 */
#define MAPML_CONFIG			(24)
#define MAPMO_CONFIG_cnf_ver            (0)
#define MAPMS_CONFIG_cnf_ver              (1)
#define MAPMO_CONFIG_user_id            (1)
#define MAPMS_CONFIG_user_id              (1)
#define MAPMO_CONFIG_TCAP_id            (2)
#define MAPMS_CONFIG_TCAP_id              (1)
#define MAPMO_CONFIG_mngt_id            (3)
#define MAPMS_CONFIG_mngt_id              (1)
#define MAPMO_CONFIG_maint_id           (4)
#define MAPMS_CONFIG_maint_id             (1)
#define MAPMO_CONFIG_trace_id           (5)
#define MAPMS_CONFIG_trace_id             (1)
#define MAPMO_CONFIG_base_usr_ogdlg_id  (6)
#define MAPMS_CONFIG_base_usr_ogdlg_id    (2)
#define MAPMO_CONFIG_base_usr_icdlg_id  (8)
#define MAPMS_CONFIG_base_usr_icdlg_id    (2)
#define MAPMO_CONFIG_base_tc_ogdlg_id   (10)
#define MAPMS_CONFIG_base_tc_ogdlg_id     (2)
#define MAPMO_CONFIG_base_tc_icdlg_id   (12)
#define MAPMS_CONFIG_base_tc_icdlg_id     (2)
#define MAPMO_CONFIG_nog_dialogues      (14)
#define MAPMS_CONFIG_nog_dialogues        (2)
#define MAPMO_CONFIG_nic_dialogues      (16)
#define MAPMS_CONFIG_nic_dialogues        (2)
#define MAPMO_CONFIG_num_invokes        (18)
#define MAPMS_CONFIG_num_invokes          (2)
#define MAPMO_CONFIG_options            (20)
#define MAPMS_CONFIG_options              (4)

/*
 * Definitions for options fields in
 * MAP_MSG_CONFIG message:
 */
#define MAPF_V2_ERRORS		(0x0001)  /* V3 dialogues use V2 error format */
/*
 * Definitions of length, offset and size of
 * MAP_MSG_CNF_TIM message parameter field:
 */
#define MAPML_CNF_TIM			(3)
#define MAPMO_CNF_TIM_cnf_ver           (0)
#define MAPMS_CNF_TIM_cnf_ver             (1)
#define MAPMO_CNF_TIM_t0                (1)
#define MAPMS_CNF_TIM_t0                  (2)
/*
 * Definitions of length, offset and size of
 * MAP_MSG_TRACE_MASK message parameter field:
 */
#define MAPML_TRACE_MASK		(12)
#define MAPMO_TRACE_MASK_op_evt_mask    (0)
#define MAPMS_TRACE_MASK_op_evt_mask      (4)
#define MAPMO_TRACE_MASK_ip_evt_mask    (4)
#define MAPMS_TRACE_MASK_ip_evt_mask      (4)
#define MAPMO_TRACE_MASK_non_prim_mask  (8)
#define MAPMS_TRACE_MASK_non_prim_mask    (4)
#define MAPMO_TRACE_MASK_reserved       (12)
#define MAPMS_TRACE_MASK_reserved         (28)

/*
 * Output event trace mask definitions
 */
#define MAPOEM_MAPDLG_IND	(0x00000001)	/* MAP_MSG_DLG_IND */
#define MAPOEM_MAPSRV_IND	(0x00000002)	/* MAP_MSG_SRV_IND */
#define MAPOEM_TCDLG_REQ	(0x00000004)	/* TCP_MSG_DLG_REQ */
#define MAPOEM_TCCMP_REQ	(0x00000008)	/* TCP_MSG_CPT_REQ */

/*
 * Input event trace mask definitions
 */
#define MAPIEM_MAPDLG_REQ	(0x00000001)	/* MAP_MSG_DLG_REQ */
#define MAPIEM_MAPSRV_REQ	(0x00000002)	/* MAP_MSG_SRV_REQ */
#define MAPIEM_TCDLG_IND	(0x00000004)	/* TCP_MSG_DLG_IND */
#define MAPIEM_TCCMP_IND	(0x00000008)	/* TCP_MSG_CPT_IND */

/*
 * Non-primitive trace mask definitions
 */
#define MAPNPM_CNF_TIM		(0x00000001)	/* MAP_MSG_CNF_TIM */
#define MAPNPM_SW_EVT		(0x00000004)	/* MAP_MSG_ERROR_INDWE
 */
#define MAPNPM_SMT_MSK		(0x00000008)	/* MAP_MSG_S_MAINT_MASK */
#define MAPNPM_SSW_MSK		(0x00000010)	/* MAP_MSG_S_ERROR_MASK */
#define MAPNPM_STR_MSK		(0x00000020)	/* MAP_MSG_TRACE_MASK */
#define MAPNPM_SSL_MSK		(0x00000040)	/* MAP_MAG_S_SELTRACE_MASK */

/*
 * Definitions of length, offset and size of
 * MAP_MSG_S_MAINT_MASK message parameter field.
 * Note - this is no longer used.
 */
#define MAPML_MAINT_MASK  (12)
#define MAPMO_S_MAINT_MASK_mask         (0)
#define MAPMS_S_MAINT_MASK_mask           (9)

/*
 * Definitions of length, offset and size of
 * MAP_MSG_S_ERROR_MASK message parameter field:
 */
#define MAPML_ERROR_MASK         (12)
#define MAPMO_S_ERROR_MASK_mask         (0)
#define MAPMS_S_ERROR_MASK_mask           (9)

/*
 * DEfinition of length of MAP_MSG_S_SELTRACE_MASK message parameter field:
 */
#define MAPML_S_SELTRACE_MASK	(9)
/*
 * Definitions used in the status field
 * of MAP_MSG_ERROR_IND messages:
 */
#define MAPSWE_NO_MSSM           (1)    /* MAP_SSM structures exhausted */
#define MAPSWE_NO_DLG            (2)    /* MAP_DLG structures exhausted */
#define MAPSWE_NO_MAPM           (3)    /* MAP_MSG structures exhausted */
#define MAPSWE_MAPM_LOW          (4)    /* Running short of MAP_MSG structures */
#define MAPSWE_BAD_MSG           (5)    /* Unsupported inter-task message received */
#define MAPSWE_TX_FMT_ERR        (6)    /* Failed to format message for tx */
#define MAPSWE_USER_BAD_FMT      (7)    /* Badly formatted MAP message received from User*/
#define MAPSWE_TCAP_BAD_FMT      (8)    /* Badly formatted MAP message received from TCAP*/
#define MAPSWE_UNREC_TYPE        (9)    /* Unrecognised MAP message received from TCAP */
#define MAPSWE_INVALID_DLG_ID    (10)   /* Message containing invalid dialogue id received from TCAP */
#define MAPSWE_USER_MAND_MISSING (11)	/* Missing mandatory parameter in message from user */
#define MAPSWE_TCAP_MAND_MISSING (12)	/* Missing mandatory parameter in message from TCAP */
#define MAPSWE_BAD_USER_PRIM     (13)   /* Unknown primitive recvd from user */
#define MAPSWE_BAD_TCAP_PRIM     (14)   /* Unknown primitive recvd from TCAP */
#define MAPSWE_USER_PAR_FMT_ERR  (15)	/* Bad parameter format recvd from user */
#define MAPSWE_TCAP_PAR_FMT_ERR  (16)	/* Bad parameter format recvd from TCAP */
#define MAPSWE_USER_UNREC_PARAM  (17)   /* Unrecognised parameter recvd from user */
#define MAPSWE_TCAP_UNREC_PARAM  (18)	/* Unrecognised parameter recvd from TCAP */
#define MAPSWE_USER_MSG_UNEXP    (19)   /* Unexpected message recvd from user */
#define MAPSWE_TCAP_MSG_UNEXP    (20)   /* Unexpected message recvd from TCAP */

/*
 * Definitions for use with
 * MAP_MSG_SEL_TRACE messages:
 */
#define MAPt_tx_fmt_err         (0)  /* Failed to format message for tx */
#define MAPt_user_bad_fmt	(1)  /* Badly formatted MAP message received from User*/
#define MAPt_tcap_bad_fmt	(2)  /* Badly formatted MAP message received from TCAP*/
#define MAPt_invalid_dlg_id	(3)  /* Message containing invalid dialogue id received from TCAP */
#define MAPt_user_mand_missing	(4)  /* Missing mandatory parameter in message from user */
#define MAPt_tcap_mand_missing	(5)  /* Missing mandatory parameter in message from TCAP */
#define MAPt_bad_user_prim	(6)  /* Unknown primitive recvd from user */
#define MAPt_bad_tcap_prim	(7)  /* Unknown primitive recvd from TCAP */
#define MAPt_user_par_fmt_err	(8)  /* Bad parameter format recvd from user */
#define MAPt_tcap_par_fmt_err	(9) /* Bad parameter format recvd from TCAP */
#define MAPt_user_unrec_param	(10) /* Unrecognised parameter recvd from user */
#define MAPt_tcap_unrec_param	(11) /* Unrecognised parameter recvd from TCAP */
#define MAPt_user_msg_unexp     (12) /* Unexpected message recvd from user */
#define MAPt_tcap_msg_unexp     (13) /* Unexpected message recvd from TCAP */

/*******************************************************************
 *                                                                 *
 *           Definitions for MAP Primitives and Parameters         *
 *                                                                 *
 *******************************************************************
 */

/*
 * MAP dialogue primitive types
 */
#define  MAPDT_OPEN_REQ         (1)     /* MAP-OPEN-REQ */
#define  MAPDT_OPEN_IND         (2)     /* MAP-OPEN-IND */
#define  MAPDT_CLOSE_REQ        (3)     /* MAP-CLOSE-REQ */
#define  MAPDT_CLOSE_IND        (4)     /* MAP-CLOSE-IND */
#define  MAPDT_DELIMITER_REQ    (5)     /* MAP-DELIMITER-REQ */
#define  MAPDT_DELIMITER_IND    (6)     /* MAP-DELIMITER-IND */
#define  MAPDT_U_ABORT_REQ      (7)     /* MAP-U-ABORT-REQ */
#define  MAPDT_U_ABORT_IND      (8)     /* MAP-U-ABORT-IND */
#define  MAPDT_P_ABORT_IND      (9)     /* MAP-P-ABORT-IND */
#define  MAPDT_NOTICE_IND       (10)    /* MAP-NOTICE-IND */
#define  MAPDT_MAX_DLG_REQ_PRIM (10)    /* Max user dlg primitive value */

#define  MAPDT_OPEN_RSP         (129)   /* MAP-OPEN-RSP */
#define  MAPDT_OPEN_CNF         (130)   /* MAP-OPEN-CNF */
#define  MAPDT_MAX_DLG_ACK_PRIM (130)   /* Max user dlg primitive value */

/*
 * MAP service primitive types
 */
#define  MAPST_SND_RTISM_REQ            (1)   /* MAP-SEND-ROUTING-INFO-FOR-SM-REQ */
#define  MAPST_SND_RTISM_IND            (2)   /* MAP-SEND-ROUTING-INFO-FOR-SM-IND */
#define  MAPST_FWD_SM_REQ               (3)   /* MAP-FORWARD-SHORT-MESSAGE-REQ */
#define  MAPST_FWD_SM_IND               (4)   /* MAP-FORWARD-SHORT-MESSAGE-IND */
#define  MAPST_MO_FWD_SM_REQ            (3)   /* MAP-MO-FORWARD-SHORT-MESSAGE-REQ */
#define  MAPST_MO_FWD_SM_IND            (4)   /* MAP-MO-FORWARD-SHORT-MESSAGE-IND */
#define  MAPST_SM_DELIV_STATUS_REQ      (5)   /* MAP-SHORT-MESSAGE-DELIVERY-STATUS-REQ */
#define  MAPST_SM_DELIV_STATUS_IND      (6)   /* MAP-SHORT-MESSAGE-DELIVERY-STATUS-IND */
#define  MAPST_READY_FOR_SM_REQ         (7)   /* MAP-READY-FOR-SHORT-MESSAGE-REQ */
#define  MAPST_READY_FOR_SM_IND         (8)   /* MAP-READY-FOR-SHORT-MESSAGE-IND */
#define  MAPST_ALERT_SRV_CENTRE_REQ     (9)   /* MAP-ALERT-SERVICE-CENTRE-REQ */
#define  MAPST_ALERT_SRV_CENTRE_IND     (10)  /* MAP-ALERT-SERVICE-CENTRE-IND */
#define  MAPST_INFORM_SRV_CENTRE_REQ    (11)  /* MAP-INFORM-SERVICE-CENTRE-REQ */
#define  MAPST_INFORM_SRV_CENTRE_IND    (12)  /* MAP-INFORM-SERVICE-CENTRE-IND */
#define  MAPST_SEND_INFO_FOR_MT_SMS_REQ (13)  /* MAP-SEND-INFO-FOR-MT-SMS-REQ */
#define  MAPST_SEND_INFO_FOR_MT_SMS_IND (14)  /* MAP-SEND-INFO-FOR-MT-SMS-IND */
#define  MAPST_SEND_INFO_FOR_MO_SMS_REQ (15)  /* MAP-SEND-INFO-FOR-MO-SMS-REQ */
#define  MAPST_SEND_INFO_FOR_MO_SMS_IND (16)  /* MAP-SEND-INFO-FOR-MO-SMS-IND */
#define  MAPST_ALERT_SC_WO_RESULT_REQ   (17)  /* MAP-ALERT-SERVICE-CENTRE-WITHOUT_RESULT-REQ */
#define  MAPST_ALERT_SC_WO_RESULT_IND   (18)  /* MAP-ALERT-SERVICE-CENTRE-WITHOUT_RESULT-IND */
#define  MAPST_NOTE_SUB_PRESENT_REQ     (19)  /* MAP-NOTE-SUBSCRIBER-PRESENT-REQ */
#define  MAPST_NOTE_SUB_PRESENT_IND     (20)  /* MAP-NOTE-SUBSCRIBER-PRESENT-IND */
#define  MAPST_SND_RI_REQ               (21)  /* MAPST_SEND_ROUTING_INFO_REQ */
#define  MAPST_SND_RI_IND               (22)  /* MAPST_SEND_ROUTING_INFO_IND */
#define  MAPST_UNSTR_SS_REQ_REQ         (23)  /* MAP_UNSTRUCTURED_SS_REQUEST REQ */
#define  MAPST_UNSTR_SS_REQ_IND         (24)  /* MAP_UNSTRUCTURED_SS_REQUEST IND */
#define  MAPST_UNSTR_SS_NOTIFY_REQ      (25)  /* MAP_UNSTRUCTURED_SS_NOTIFY REQ */
#define  MAPST_UNSTR_SS_NOTIFY_IND      (26)  /* MAP_UNSTRUCTURED_SS_NOTIFY IND */
#define  MAPST_PRO_UNSTR_SS_REQ_REQ     (27)  /* MAP_PROCESS_UNSTRUCTURED_SS_REQUEST REQ */
#define  MAPST_PRO_UNSTR_SS_REQ_IND     (28)  /* MAP_PROCESS_UNSTRUCTURED_SS_REQUEST IND */
#define  MAPST_ANYTIME_INT_REQ          (29)  /* MAP_ANYTIME_INTERROGATION REQ */
#define  MAPST_ANYTIME_INT_IND          (30)  /* MAP_ANYTIME_INTERROGATION IND */
#define  MAPST_PROV_SUB_INFO_REQ        (31)  /* MAP_PROVIDE_SUBSCRIBER_INFO REQ */
#define  MAPST_PROV_SUB_INFO_IND        (32)  /* MAP_PROVIDE_SUBSCRIBER_INFO IND */
#define  MAPST_SEND_IMSI_REQ            (33)  /* MAP_SEND_IMSI REQ */
#define  MAPST_SEND_IMSI_IND            (34)  /* MAP_SEND_IMSI IND */
#define  MAPST_SND_RTIGPRS_REQ          (35)  /* MAP-SEND-ROUTING-INFO-FOR-GPRS-REQ */
#define  MAPST_SND_RTIGPRS_IND          (36)  /* MAP-SEND-ROUTING-INFO-FOR-GPRS-IND */
#define  MAPST_PROV_ROAM_NUM_REQ        (37)  /* MAP-PROVIDE-ROAMING-NUMBER-REQ */
#define  MAPST_PROV_ROAM_NUM_IND        (38)  /* MAP-PROVIDE-ROAMING-NUMBER-IND */
#define  MAPST_UPDATE_LOCATION_REQ      (39)  /* MAP-UPDATE-LOCATION-REQ */
#define  MAPST_UPDATE_LOCATION_IND      (40)  /* MAP-UPDATE-LOCATION-IND */
#define  MAPST_CANCEL_LOCATION_REQ      (41)  /* MAP-CANCEL-LOCATION-REQ */
#define  MAPST_CANCEL_LOCATION_IND      (42)  /* MAP-CANCEL-LOCATION-IND */
#define  MAPST_INS_SUBS_DATA_REQ        (43)  /* MAP-INSERT-SUBSCRIBER-DATA-REQ */
#define  MAPST_INS_SUBS_DATA_IND        (44)  /* MAP-INSERT-SUBSCRIBER-DATA-IND */
#define  MAPST_ACTIV_TRACE_MODE_REQ     (45)  /* MAP-ACTIVATE-TRACE-MODE-REQ */
#define  MAPST_ACTIV_TRACE_MODE_IND     (46)  /* MAP-ACTIVATE-TRACE-MODE-IND */
#define  MAPST_SEND_IDENT_REQ           (47)  /* MAP-SEND-IDENTIFICATION-REQ */
#define  MAPST_SEND_IDENT_IND           (48)  /* MAP-SEND-IDENTIFICATION-IND */
#define  MAPST_DEL_SUBS_DATA_REQ        (49)  /* MAP-DELETE-SUBSCRIBER-DATA-REQ */
#define  MAPST_DEL_SUBS_DATA_IND        (50)  /* MAP-DELETE-SUBSCRIBER-DATA-IND */
#define  MAPST_NOTE_MS_PRES_GPRS_REQ    (51)  /* MAP-NOTE-MS-PRESENT-GPRS-REQ */
#define  MAPST_NOTE_MS_PRES_GPRS_IND    (52)  /* MAP-NOTE-MS-PRESENT-GPRS-IND */
#define  MAPST_FAIL_REPORT_REQ          (53)  /* MAP-FAILURE-REPORT-REQ */
#define  MAPST_FAIL_REPORT_IND          (54)  /* MAP-FAILURE-REPORT-IND */
#define  MAPST_UPDATE_GPRS_LOC_REQ      (55)  /* MAP-UPDATE-GPRS-LOCATION-REQ */
#define  MAPST_UPDATE_GPRS_LOC_IND      (56)  /* MAP-UPDATE-GPRS-LOCATION-IND */
#define  MAPST_PURGE_MS_REQ             (57)  /* MAP-PURGE-MSUBSCRIBER-DATA-REQ */
#define  MAPST_PURGE_MS_IND             (58)  /* MAP-PURGE-MSUBSCRIBER-DATA-IND */
#define  MAPST_ROUT_INFO_LCS_REQ        (59)  /* MAP-ROUTING-INFO-LCS-REQ */
#define  MAPST_ROUT_INFO_LCS_IND        (60)  /* MAP-ROUTING-INFO-LCS-IND */
#define  MAPST_PROVIDE_SUBS_LOC_REQ     (61)  /* MAP-PROVIDE-SUBSCRIBER-LOC-REQ */
#define  MAPST_PROVIDE_SUBS_LOC_IND     (62)  /* MAP-PROVIDE-SUBSCRIBER-LOC-IND */
#define  MAPST_SUBS_LOC_REPORT_REQ      (63)  /* MAP-SUBSCRIBER-LOC-REPORT-REQ */
#define  MAPST_SUBS_LOC_REPORT_IND      (64)  /* MAP-SUBSCRIBER-LOC-REPORT-IND */
#define  MAPST_NOTE_MM_EVENT_REQ        (65)  /* MAP-NOTE-MM-EVENT-REQ */
#define  MAPST_NOTE_MM_EVENT_IND        (66)  /* MAP-NOTE-MM-EVENT-IND */
#define  MAPST_FWD_CHK_SS_INDICAT_REQ   (67)  /* MAP-FORWARD-CHECK-SS-INDICATION-REQ */
#define  MAPST_FWD_CHK_SS_INDICAT_IND   (68)  /* MAP-FORWARD-CHECK-SS-INDICATION-IND */
#define  MAPST_MT_FWD_SM_REQ            (69)  /* MAP-MT-FORWARD-SHORT-MESSAGE-REQ */
#define  MAPST_MT_FWD_SM_IND            (70)  /* MAP-MT-FORWARD-SHORT-MESSAGE-IND */
#define  MAPST_SEND_AUTH_INFO_REQ       (71)  /* MAP-SEND-AUTHENTICATION-INFO-REQ */
#define  MAPST_SEND_AUTH_INFO_IND       (72)  /* MAP-SEND-AUTHENTICATION-INFO-IND */
#define  MAPST_INTERROGATE_SS_REQ       (73)  /* MAP-INTERROGATE-SS-REQ */
#define  MAPST_INTERROGATE_SS_IND       (74)  /* MAP-INTERROGATE-SS-IND */
#define  MAPST_ACTIVATE_SS_REQ          (75)  /* MAP-ACTIVATE-SS-REQ */
#define  MAPST_ACTIVATE_SS_IND          (76)  /* MAP-ACTIVATE-SS-IND */
#define  MAPST_DEACTIVATE_SS_REQ        (77)  /* MAP-DEACTIVATE-SS-REQ */
#define  MAPST_DEACTIVATE_SS_IND        (78)  /* MAP-DEACTIVATE-SS-IND */
#define  MAPST_CHECK_IMEI_REQ           (79)  /* MAP-CHECK-IMEI-REQ */
#define  MAPST_CHECK_IMEI_IND           (80)  /* MAP-CHECK-IMEI-IND */

#define  MAPST_MAX_SRV_REQ_PRIM         (80)  /* Max value of the service primitives */

  /* Values above 127 are used for acknowledgements */
#define  MAPST_SND_RTISM_RSP            (129) /* MAP-SEND-ROUTING-INFO-FOR-SM-RSP */
#define  MAPST_SND_RTISM_CNF            (130) /* MAP-SEND-ROUTING-INFO-FOR-SM-CNF */
#define  MAPST_FWD_SM_RSP               (131) /* MAP-FORWARD-SHORT-MESSAGE-RSP */
#define  MAPST_FWD_SM_CNF               (132) /* MAP-FORWARD-SHORT-MESSAGE-CNF */
#define  MAPST_MO_FWD_SM_RSP            (131) /* MAP-MO-FORWARD-SHORT-MESSAGE-RSP */
#define  MAPST_MO_FWD_SM_CNF            (132) /* MAP-MO-FORWARD-SHORT-MESSAGE-CNF */
#define  MAPST_SM_DELIV_STATUS_RSP      (133) /* MAP-SHORT-MESSAGE-DELIVERY-STATUS-RESP */
#define  MAPST_SM_DELIV_STATUS_CNF      (134) /* MAP-SHORT-MESSAGE-DELIVERY-STATUS-CONF */
#define  MAPST_READY_FOR_SM_RSP         (135) /* MAP-READY-FOR-SHORT-MESSAGE-RESP */
#define  MAPST_READY_FOR_SM_CNF         (136) /* MAP-READY-FOR-SHORT-MESSAGE-CONF */
#define  MAPST_ALERT_SRV_CENTRE_RSP     (137) /* MAP-ALERT-SERVICE-CENTRE-RESP */
#define  MAPST_ALERT_SRV_CENTRE_CNF     (138) /* MAP-ALERT-SERVICE-CENTRE-CONF */
#define  MAPST_SEND_INFO_FOR_MT_SMS_RSP (139) /* MAP-SEND-INFO-FOR-MT-SMS-RESP */
#define  MAPST_SEND_INFO_FOR_MT_SMS_CNF (140) /* MAP-SEND-INFO-FOR-MT-SMS-CONF */
#define  MAPST_SEND_INFO_FOR_MO_SMS_RSP (141) /* MAP-SEND-INFO-FOR-MO-SMS-RESP */
#define  MAPST_SEND_INFO_FOR_MO_SMS_CNF (142) /* MAP-SEND-INFO-FOR-MO-SMS-CONF */
#define  MAPST_SND_RI_RSP               (143) /* MAPST-SEND-ROUTING-INFO-RSP */
#define  MAPST_SND_RI_CNF               (144) /* MAPST-SEND-ROUTING-INFO-CNF */
#define  MAPST_UNSTR_SS_REQ_RSP         (145) /* MAP-UNSTRUCTURED-SS-REQUEST RSP */
#define  MAPST_UNSTR_SS_REQ_CNF         (146) /* MAP-UNSTRUCTURED-SS-REQUEST CNF */
#define  MAPST_UNSTR_SS_NOTIFY_RSP      (147) /* MAP-UNSTRUCTURED-SS-NOTIFY RSP */
#define  MAPST_UNSTR_SS_NOTIFY_CNF      (148) /* MAP-UNSTRUCTURED-SS-NOTIFY CNF */
#define  MAPST_PRO_UNSTR_SS_REQ_RSP     (149) /* MAP-PROCESS-UNSTRUCTURED-SS-REQUEST RSP */
#define  MAPST_PRO_UNSTR_SS_REQ_CNF     (150) /* MAP-PROCESS-UNSTRUCTURED-SS-REQUEST CNF */
#define  MAPST_ANYTIME_INT_RSP          (151) /* MAP-ANYTIME-INTERROGATION RSP */
#define  MAPST_ANYTIME_INT_CNF          (152) /* MAP-ANYTIME-INTERROGATION CNF */
#define  MAPST_PROV_SUB_INFO_RSP        (153) /* MAP-PROVIDE-SUBSCRIBER-INFO RSP */
#define  MAPST_PROV_SUB_INFO_CNF        (154) /* MAP-PROVIDE-SUBSCRIBER-INFO CNF */
#define  MAPST_SEND_IMSI_RSP            (155) /* MAP-SEND-IMSI RSP */
#define  MAPST_SEND_IMSI_CNF            (156) /* MAP-SEND-IMSI CNF */
#define  MAPST_SND_RTIGPRS_RSP          (157) /* MAP-SEND-ROUTING-INFO-FOR-GPRS-RSP */
#define  MAPST_SND_RTIGPRS_CNF          (158) /* MAP-SEND-ROUTING-INFO-FOR-GPRS-CNF */
#define  MAPST_PROV_ROAM_NUM_RSP        (159) /* MAP-PROVIDE-ROAMING-NUMBER-RSP */
#define  MAPST_PROV_ROAM_NUM_CNF        (160) /* MAP-PROVIDE-ROAMING-NUMBER-CNF */
#define  MAPST_UPDATE_LOCATION_RSP      (161) /* MAP-UPDATE-LOCATION-RSP */
#define  MAPST_UPDATE_LOCATION_CNF      (162) /* MAP-UPDATE-LOCATION-CNF */
#define  MAPST_CANCEL_LOCATION_RSP      (163) /* MAP-CANCEL-LOCATION-RSP */
#define  MAPST_CANCEL_LOCATION_CNF      (164) /* MAP-CANCEL-LOCATION-CNF */
#define  MAPST_INS_SUBS_DATA_RSP        (165) /* MAP-INSERT-SUBSCRIBER-DATA-RSP */
#define  MAPST_INS_SUBS_DATA_CNF        (166) /* MAP-INSERT-SUBSCRIBER-DATA-CNF */
#define  MAPST_ACTIV_TRACE_MODE_RSP     (167) /* MAP-ACTIVATE-TRACE-MODE-RSP */
#define  MAPST_ACTIV_TRACE_MODE_CNF     (168) /* MAP-ACTIVATE-TRACE-MODE-CNF */
#define  MAPST_SEND_IDENT_RSP           (169) /* MAP-SEND-IDENTIFICATION-RSP */
#define  MAPST_SEND_IDENT_CNF           (170) /* MAP-SEND-IDENTIFICATION-CNF */
#define  MAPST_DEL_SUBS_DATA_RSP        (171) /* MAP-DELETE-SUBSCRIBER-DATA-RSP */
#define  MAPST_DEL_SUBS_DATA_CNF        (172) /* MAP-DELETE-SUBSCRIBER-DATA-CNF */
#define  MAPST_NOTE_MS_PRES_GPRS_RSP    (173) /* MAP-NOTE-MS-PRESENT-GPRS-RSP */
#define  MAPST_NOTE_MS_PRES_GPRS_CNF    (174) /* MAP-NOTE-MS-PRESENT-GPRS-CNF */
#define  MAPST_FAIL_REPORT_RSP          (175) /* MAP-FAILURE-REPORT-RSP */
#define  MAPST_FAIL_REPORT_CNF          (176) /* MAP-FAILURE-REPORT-CNF */
#define  MAPST_UPDATE_GPRS_LOC_RSP      (177) /* MAP-UPDATE-GPRS-LOCATION-RSP */
#define  MAPST_UPDATE_GPRS_LOC_CNF      (178) /* MAP-UPDATE-GPRS-LOCATION-CNF */
#define  MAPST_PURGE_MS_RSP             (179) /* MAP-PURGE-MSUBSCRIBER-DATA-RSP */
#define  MAPST_PURGE_MS_CNF             (180) /* MAP-PURGE-MSUBSCRIBER-DATA-CNF */
#define  MAPST_ROUT_INFO_LCS_RSP        (181) /* MAP-ROUTING-INFO-LCS-RSP */
#define  MAPST_ROUT_INFO_LCS_CNF        (182) /* MAP-ROUTING-INFO-LCS-CNF */
#define  MAPST_PROVIDE_SUBS_LOC_RSP     (183) /* MAP-PROVIDE-SUBSCRIBER-LOC-RSP */
#define  MAPST_PROVIDE_SUBS_LOC_CNF     (184) /* MAP-PROVIDE-SUBSCRIBER-LOC-CNF */
#define  MAPST_SUBS_LOC_REPORT_RSP      (185) /* MAP-SUBSCRIBER-LOC-REPORT-RSP */
#define  MAPST_SUBS_LOC_REPORT_CNF      (186) /* MAP-SUBSCRIBER-LOC-REPORT-CNF */
#define  MAPST_NOTE_MM_EVENT_RSP        (187) /* MAP-NOTE-MM-EVENT-RSP */
#define  MAPST_NOTE_MM_EVENT_CNF        (188) /* MAP-NOTE-MM-EVENT-CNF */
#define  MAPST_FWD_CHK_SS_INDICAT_RSP   (189) /* MAP-FORWARD-CHECK-SS-INDICATION-RSP */
#define  MAPST_FWD_CHK_SS_INDICAT_CNF   (190) /* MAP-FORWARD-CHECK-SS-INDICATION-CNF */
#define  MAPST_MT_FWD_SM_RSP            (191) /* MAP-MT-FORWARD-SHORT-MESSAGE-RSP */
#define  MAPST_MT_FWD_SM_CNF            (192) /* MAP-MT-FORWARD-SHORT-MESSAGE-CNF */
#define  MAPST_SEND_AUTH_INFO_RSP       (193) /* MAP-SEND-AUTHENTICATION-INFO-RSP */
#define  MAPST_SEND_AUTH_INFO_CNF       (194) /* MAP-SEND-AUTHENTICATION-INFO-CNF */
#define  MAPST_INTERROGATE_SS_RSP       (195) /* MAP-INTERROGATE-SS-RSP */
#define  MAPST_INTERROGATE_SS_CNF       (196) /* MAP-INTERROGATE-SS-CNF */
#define  MAPST_ACTIVATE_SS_RSP          (197) /* MAP-ACTIVATE-SS-RSP */
#define  MAPST_ACTIVATE_SS_CNF          (198) /* MAP-ACTIVATE-SS-CNF */
#define  MAPST_DEACTIVATE_SS_RSP        (199) /* MAP-DEACTIVATE-SS-RSP */
#define  MAPST_DEACTIVATE_SS_CNF        (200) /* MAP-DEACTIVATE-SS-CNF */
#define  MAPST_CHECK_IMEI_RSP           (201) /* MAP-CHECK-IMEI-RSP */
#define  MAPST_CHECK_IMEI_CNF           (202) /* MAP-CHECK-IMEI-CNF */

#define  MAPST_MAX_SRV_ACK_PRIM         (202) /* Max value of the acknowledge primitives */

/*
 ******************************************************************************
 *                                                                            *
 * Parameter name macro MAPPN_SEQ:                                            *
 *                                                                            *
 * This macro allows the parameter name defintion in the form below           *
 *                                                                            *
 * #define MAPPN_parameterName(i)                 (MAPPN_SEQ(i,27,3))         *
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
 * #define MAPPN_DestinationRoutingAddress(i)       (MAPPN_SEQ(i,27,3))       *
 *                                                                            *
 * In use, this means each element has a parameter name defined as follows    *
 *                                                                            *
 * MAPPN_DestinationRoutingAddress(0)   ( value of 27  +  0      ->  27 )     *
 * MAPPN_DestinationRoutingAddress(1)   ( value of 27  +  1      ->  28 )     *
 * MAPPN_DestinationRoutingAddress(2)   ( value of 27  +  2      ->  29 )     *
 *                                                                            *
 * If an index is specified which is too large for the sequence then a null   *
 * parameter name is returned. This will force it to be rejected by the API   *
 * functions.                                                                 *
 *                                                                            *
 * MAPPN_DestinationRoutingAddress(>2)  ( value of MAPPN_Null     ->   0 )    *
 *                                                                            *
 ******************************************************************************
 */
#define MAPPN_SEQ(i,base,size)     ((i < size) ? (base + i) : MAPPN_null)

/*
 * Primitive parameter name definitions
 */
#define MAPPN_null                              (0)                  /* NULL */
#define MAPPN_dest_address                      (1)                  /* Destination address */
#define MAPPN_dest_ref                          (2)                  /* Destination reference */
#define MAPPN_orig_address                      (3)                  /* Origination address */
#define MAPPN_orig_ref                          (4)                  /* Origination reference */
#define MAPPN_result                            (5)                  /* Result */
#define MAPPN_refuse_rsn                        (6)                  /* Refuse reason */
#define MAPPN_release_method                    (7)                  /* Release method */
#define MAPPN_user_rsn                          (8)                  /* User reason */
#define MAPPN_prov_rsn                          (9)                  /* Provider reason */
#define MAPPN_diag_inf                          (10)                 /* Diagnostic information */
#define MAPPN_applic_context                    (11)                 /* Application context */
#define MAPPN_source                            (12)                 /* Source */
#define MAPPN_prob_diag                         (13)                 /* Problem diagnostic */
#define MAPPN_invoke_id                         (14)                 /* invoke id */
#define MAPPN_msisdn                            (15)                 /* Mobile Subscriber ISDN number */
#define MAPPN_sm_rp_pri                         (16)                 /* Short Message delivery priority */
#define MAPPN_sc_addr                           (17)                 /* Short Message Service centre address */
#define MAPPN_imsi                              (18)                 /* International Mobile Subscriber Identity */
#define MAPPN_msc_num                           (19)                 /* ISDN number of MSC */
#define MAPPN_lmsi                              (20)                 /* LMSI */
#define MAPPN_user_err                          (21)                 /* User Error */
#define MAPPN_prov_err                          (22)                 /* Provider Error */
#define MAPPN_sm_rp_da                          (23)                 /* Short message destination address (IMSI, LMSI, MS-ISDN roaming number or srv centre address) */
#define MAPPN_sm_rp_oa                          (24)                 /* Short message originating address (MS-ISDN or srv centre address) */
#define MAPPN_sm_rp_ui                          (25)                 /* Short message user data feild */
#define MAPPN_more_msgs                         (26)                 /* Srv centre has more short messages to send */
#define MAPPN_sm_deliv_outcome                  (27)                 /* SM delivery Outcome*/
#define MAPPN_alert_reason                      (28)                 /* Alert Reason*/
#define MAPPN_mwd_status                        (29)                 /* Message waiting data file status */
#define MAPPN_ntwk_res                          (30)                 /* Network resource class, (PLMN,HLR,VLR,MSC,EIR,radio subsystem). */
#define MAPPN_deliv_fail_cse                    (31)                 /* sm_DeliveryFailureCause (Error parameter). */
#define MAPPN_call_bar_cse                      (32)                 /* Call Barring cause (Error Parameter) */
#define MAPPN_teleservice                       (33)                 /* Teleservice */
#define MAPPN_mwd_set                           (34)                 /* Message Waiting Data flag set (V1 only)*/
#define MAPPN_roaming_num                       (35)                 /* Roaming Number */
#define MAPPN_CUG_interlock                     (36)                 /* Closed User Group Interlock code (V1 only) */
#define MAPPN_CUG_outgo_access                  (37)                 /* CUG outgoing access */
#define MAPPN_CUG_reject_cse                    (38)                 /* CUG reject cause */
#define MAPPN_num_of_fwding                     (39)                 /* Number of forwarding */
#define MAPPN_net_sig_info                      (40)                 /* Network signal info */
#define MAPPN_fwd_to_num                        (41)                 /* forwarded to number */
#define MAPPN_fwd_to_subaddr                    (42)                 /* forwarded to subaddress */
#define MAPPN_fwding_opt                        (43)                 /* forwarding options */
#define MAPPN_linked_id                         (44)                 /* linked id */
#define MAPPN_timeout                           (45)                 /* Operation TCAP timeout */
#define MAPPN_USSD_coding                       (46)                 /* USSD Coding scheme */
#define MAPPN_USSD_string                       (47)                 /* USSD String */
#define MAPPN_age_loc_info                      (48)                 /* Age of location information */
#define MAPPN_cell_id                           (49)                 /* Cell ID */
#define MAPPN_geog_info                         (50)                 /* Geographical information */
#define MAPPN_gsmscf_addr                       (51)                 /* GSM SCF address */
#define MAPPN_loc_num                           (52)                 /* Location number */
#define MAPPN_req_info                          (53)                 /* Requested information */
#define MAPPN_sub_state                         (54)                 /* Subscriber state */
#define MAPPN_vlr_number                        (55)                 /* VLR number */
#define MAPPN_not_reach_rsn                     (56)                 /* Not reachable reason */
#define MAPPN_ellipsis                          (57)                 /* Ellipsis */
#define MAPPN_ggsn_address                      (58)                 /* GGSN address */
#define MAPPN_sgsn_address                      (59)                 /* SGSN address */
#define MAPPN_ggsn_number                       (60)                 /* GGSN number */
#define MAPPN_unk_sub_diag                      (61)                 /* Unknown subscriber diagnostic */
#define MAPPN_abs_sub_rsn                       (62)                 /* Absent subscriber reason */
#define MAPPN_abs_sub_diag                      (63)                 /* Absent subscriber diagnostic SM */
#define MAPPN_reqinfo_ellipsis                  (64)                 /* Requested info ellipsis */
#define MAPPN_subinfo_ellipsis                  (65)                 /* Subscriber info ellipsis */
#define MAPPN_locinfo_ellipsis                  (66)                 /* Location info ellipsis */
#define MAPPN_supp_announce                     (67)                 /* Suppression of Announcement */
#define MAPPN_gmsc_address                      (68)                 /* GMSC Address */
#define MAPPN_or_interrogation                  (69)                 /* OR Interrogation */
#define MAPPN_alert_pattern                     (70)                 /* Alerting Pattern */
#define MAPPN_ccbs_call                         (71)                 /* CCBS Call */
#define MAPPN_suppcamelphase                    (72)                 /* Supported Camel Phase */
#define MAPPN_ornotsupp_gmsc                    (73)                 /* Or not Supported in GMSC */
#define MAPPN_callref_num                       (74)                 /* Call Reference Number */
#define MAPPN_solsa_supp_ind                    (75)                 /* Solsa Support Indicator */
#define MAPPN_previous_roam_num                 (76)                 /* Previous roaming number */
#define MAPPN_gsm_bearercap                     (77)                 /* GSM Bearer Capacity */
#define MAPPN_network_siginfo                   (78)                 /* Network signal info */
#define MAPPN_addition_siginfo                  (79)                 /* Additional signal info */
#define MAPPN_canceltype                        (80)                 /* Cancellation Type */
#define MAPPN_hlr_number                        (81)                 /* Home Location Register */
#define MAPPN_sgsn_number                       (82)                 /* SGSN number */
#define MAPPN_tmsi                              (83)                 /* TMSI */
#define MAPPN_rand(i)                           (MAPPN_SEQ(i,84,5))  /* RAND */
#define MAPPN_sres(i)                           (MAPPN_SEQ(i,89,5))  /* SRES */
#define MAPPN_kc(i)                             (MAPPN_SEQ(i,94,5))  /* KC */
#define MAPPN_subscriber_data_comp              (99)                 /* Subscriber Data Component */
#define MAPPN_sgsn_cap_ellipsis                 (100)                /* SGSN Capability ellipsis */
#define MAPPN_imsi_lmsi_ellipsis                (101)                /* IMSI with LMSI ellipsis */
#define MAPPN_vlr_cap_ellipsis                  (102)                /* VLR Capability ellipsis */
#define MAPPN_roam_not_allowed_cse              (103)                /* Roaming Not Allowed Cause */
#define MAPPN_authentic_set_ellipsis(i)         (MAPPN_SEQ(i,104,5)) /* Authentic Set ellipsis */
#define MAPPN_service_key                       (109)                /* Service key */
#define MAPPN_interrogation_type                (110)                /* Interrogation Type */
#define MAPPN_or_capability                     (111)                /* OR Capability */
#define MAPPN_forwarding_reason                 (112)                /* Forwarding Reason */
#define MAPPN_supp_ccbs_phase                   (113)                /* Suppress CCBS Phase */
#define MAPPN_bearerservice                     (114)                /* BearerService */
/*
 * Retained for backwards compatibility. Use MAPPN_bearerservice instead
 */
#define MAPPN_ext_bearerservice                 (114)
#define MAPPN_suppress_t_csi                    (115)                /* Suppress T CSI */
#define MAPPN_CUG_subscript_flag                (116)                /* CUG Subscript Flag */
#define MAPPN_vmsc_address                      (117)                /* VMSC Address */
#define MAPPN_gprs_support_ind                  (118)                /* GPRS support ind */
#define MAPPN_sm_rp_mti                         (119)                /* SM RP MTI */
#define MAPPN_sm_rp_smea                        (120)                /* SM RP SMEA */
#define MAPPN_loc_lmsi_ellipsis                 (121)                /* Location LMSI ellipsis */
/*
 * The MAPPN_network_node_num parameter is no longer used
 */
#define MAPPN_network_node_num                  (122)                /* Network Node Number */
#define MAPPN_gprs_node_ind                     (123)                /* GPRS Node Indicator */
#define MAPPN_number_port_status                (124)                /* Number Port Status */
#define MAPPN_camel_cap_handle                  (125)                /* Camel Cap Handle */
#define MAPPN_naea_preferred_cic                (126)                /* NAEA Preferred CIC */
#define MAPPN_ccbs_possible                     (127)                /* CCBS Possible */
#define MAPPN_keep_ccbs_call_ind                (128)                /* Keep CCBS Call Indicator */
#define MAPPN_GMSC_camel_subs_info              (129)                /* GMSC Camel Subscriber Information */
#define MAPPN_ss_list                           (130)                /* SS List */
#define MAPPN_ccbs_ind_ellipsis                 (131)                /* CCBS Indicator ellipsis */
#define MAPPN_camel_route_ellipsis              (132)                /* Camel Route ellipsis */
#define MAPPN_naea_pref_ci_ellipsis             (133)                /* NAEA Preferred CI ellipsis */
#define MAPPN_fwd_data_ellipsis                 (134)                /* Forward Data ellipsis */
#define MAPPN_CUG_CI_ellipsis                   (135)                /* CUG CI ellipsis */
#define MAPPN_camel_info_ellipsis               (136)                /* Camel Info ellipsis */
#define MAPPN_mlc_number                        (137)                /* MLC Number */
#define MAPPN_trace_type                        (138)                /* Trace Type */
#define MAPPN_trace_ref                         (139)                /* Trace Reference */
#define MAPPN_omc_id                            (140)                /* OMC ID */
#define MAPPN_priv_override                     (141)                /* Privacy Override */
#define MAPPN_imei                              (142)                /* IMEI */
#define MAPPN_lcs_priority                      (143)                /* LCS Priority */
#define MAPPN_loc_estimate_type                 (144)                /* Location Estimate Type */
#define MAPPN_lcs_client_type                   (145)                /* LCS Client Type */
#define MAPPN_lcs_cli_dialled_by_ms             (146)                /* LCS Client Dialled by MS */
#define MAPPN_horizontal_accuracy               (147)                /* Horizontal Accuarcy */
#define MAPPN_vertical_coordinate_req           (148)                /* Vertical Coordinate Request */
#define MAPPN_vertical_accuracy                 (149)                /* Vertical Accuarcy */
#define MAPPN_response_time                     (150)                /* Response Time Category */
#define MAPPN_na_esrd                           (151)                /* NA-ESRD */
#define MAPPN_na_esrk                           (152)                /* NA-ESRK */
#define MAPPN_loc_estimate                      (153)                /* Location Estimate */
#define MAPPN_lcs_event                         (154)                /* LCS Event */
#define MAPPN_region_subscript_resp             (155)                /* Regional Subscription Response */
#define MAPPN_freeze_tmsi                       (156)                /* Freeze TMSI */
#define MAPPN_freeze_p_tmsi                     (157)                /* Freeze P TMSI */
#define MAPPN_age_loc_est                       (158)                /* Age of Location Estimation */
#define MAPPN_event_met                         (159)                /* Event Met */
#define MAPPN_lai                               (160)                /* LAI */
#define MAPPN_supported_GAD_shapes              (161)                /* Supported GAD Shapes */
#define MAPPN_selectedlsa_id                    (162)                /* Selected LSA ID */
#define MAPPN_geodetic_info                     (163)                /* Geodetic Information */
#define MAPPN_current_loc_retrieved             (164)                /* Current Location Retrieved */
#define MAPPN_sai_present                       (165)                /* SAI Present */
#define MAPPN_unauth_msg_orig                   (166)                /* Unauthorised Message Origination*/
#define MAPPN_ccbs_busy                         (167)                /* CCBS Busy */
#define MAPPN_add_abs_sub_diag                  (168)                /* Absent subscriber diagnostic SM */
#define MAPPN_gprs_connect_susp                 (169)                /* GPRS Connection Suspended */
#define MAPPN_group_id(i)                       (MAPPN_SEQ(i,170,10))/* Group ID */
#define MAPPN_external_address(i)               (MAPPN_SEQ(i,180,5)) /* External Address */
#define MAPPN_lcs_client_internal_id(i)         (MAPPN_SEQ(i,185,5)) /* LCS Client Internal ID */
#define MAPPN_odb_ellipsis                      (190)                /* SGSN Capability ellipsis */
#define MAPPN_override_category                 (191)                /* Override Category */
#define MAPPN_lcs_loc_info_ellipsis             (192)                /* LCS Location Information ellipsis */
#define MAPPN_response_time_ellipsis            (193)                /* Response Time ellipsis */
#define MAPPN_lcs_client_id_ellipsis            (194)                /* LCS Client ID ellipsis */
#define MAPPN_lcs_client_name_ellipsis          (195)                /* LCS Client Name ellipsis */
#define MAPPN_loc_type_ellipsis                 (196)                /* Location Type ellipsis */
#define MAPPN_lcs_qos_ellipsis                  (197)                /* LCS QoS ellipsis */
#define MAPPN_vce_grp_call_ellipsis(i)          (MAPPN_SEQ(i,198,10))/* Voice Group Call ellipsis */
#define MAPPN_lcs_cli_ext_ellipsis(i)           (MAPPN_SEQ(i,208,5)) /* LCS Client External ellipsis */
#define MAPPN_alert_reason_ind                  (213)                /* Alert Reason Indication */
#define MAPPN_deliv_outcome_ind                 (214)                /* Delivery Outcome Indication */
#define MAPPN_add_sm_deliv_outcome              (215)                /* SM delivery Outcome */
#define MAPPN_fwd_interrog_req                  (216)                /* Forward Interrogation Request */
#define MAPPN_equipment_status                  (217)                /* Equipment Status */
#define MAPPN_ss_code                           (218)                /* SS-code */
#define MAPPN_ss_status                         (219)                /* SS-status */
#define MAPPN_basic_svc_grp_list                (220)                /* Basic service group list */
#define MAPPN_fwd_feature_list                  (221)                /* Forwarding feature list */
#define MAPPN_cli_restrict_option               (222)                /* CLI restriction option */
#define MAPPN_max_priority                      (223)                /* Maximum entitled priority */
#define MAPPN_default_priority                  (224)                /* Default priority */
#define MAPPN_ccbs_feat_list                    (225)                /* CCBS feature list */
#define MAPPN_forwarding_ellipsis               (226)                /* Forwarding ellipsis */
#define MAPPN_callbar_ellipsis                  (227)                /* Call Barring ellipsis */
#define MAPPN_ss_data_ellipsis                  (228)                /* SS Data ellipsis */
#define MAPPN_callbar_feature_list              (229)                /* Call Barring Feature List */
#define MAPPN_CUG_facilities                    (230)                /* CUG facilities */
#define MAPPN_CUG_feature_list                  (231)                /* CUG Feature List */
#define MAPPN_CUG_feature                       (232)                /* CUG feature */
#define MAPPN_unauth_lcs_cli_diag               (233)                /* Unauthorised LCS Client Diagnostic */
#define MAPPN_posit_method_fail_diag            (234)                /* Position Method Failure Diagnostic */
#define MAPPN_add_loc_est                       (235)                /* Additional Location Estimate */
#define MAPPN_long_ftn_sup                      (236)                /* Long Forwarded-To Numbers */
#define MAPPN_nbr_user                          (237)                /* Nbr User */
/*
 * Values from 240 - 255 reserved for use with 16bit parameter extensions
 * and should not be used
 */
#define MAPPN_end                               (237)                 /* Max value of parameter IDs*/


/*
 * Refuse reason values
 */
typedef enum
{
  MAPRR_no_reason = 0,     /* No reason given */
  MAPRR_inv_dest_ref = 1,  /* Invalid destination reference */
  MAPRR_inv_orig_ref = 2,  /* Invalid origination reference */
  MAPRR_appl_context = 3,  /* Application context not supported */
  MAPRR_ver_incomp = 4,    /* Potential version incompatibility */
  MAPRR_node_notreach = 5  /* Remote node not reachable */
} MAP_REF_RSN;


/*
 * Release Method values
 */
typedef enum
{
  MAPRM_normal_release = 0,    /* Use normal release handshake */
  MAPRM_prearranged_end = 1    /* Release at predetermined point */
} MAP_REL_METHOD;

/*
 * Provider reason & MAP-providerAbortInfo values.
 *
 * Note the mapping between Provider reason and MAP-providerAbortInfo
 * is not 1 : 1. Not all values are legal in either
 */
typedef enum
{
  MAPPR_prov_malfct = 0,   /* Provider malfunction */
  MAPPR_dlg_rlsd = 1,      /* Supporting dialogue/transaction released */
  MAPPR_rsrc_limit = 2,    /* Resource limitation */
  MAPPR_mnt_act  = 3,      /* Maintenance activity */
  MAPPR_ver_incomp = 4,    /* version incompatibility */
  MAPPR_ab_dlg = 5,        /* Abnormal MAP dialogue */
  MAPPR_invalid_PDU = 6    /*Invalid PDU */
} MAP_PROV_RSN;

/*
 * User reason values.
 */
typedef enum
{
  MAPUR_user_specific = 0,               /* user specific reason */
  MAPUR_user_resource_limitaion = 1,     /* user resource limitation */
  MAPUR_resource_unavail = 2,            /* resource unavailable */
  MAPUR_app_proc_cancelled = 3,          /* application procedure cancelled */
  MAPUR_procedure_error = 4,             /* Procedure Error */
  MAPUR_unspecified_reason = 5,          /* Unspecified Reason */
  MAPUR_version_not_supported = 6              /* Version Not Supported */
} MAP_USER_RSN;

/*
 * Result values
 */
typedef enum
{
  MAPRS_DLG_ACC = 0,    /* Dialogue accepted */
  MAPRS_DLG_REF = 1     /* Dialogue refused */
} MAP_RESULT;

/*
 * Source values
 */
typedef enum
{
  MAPSRC_map = 0,        /* MAP problem */
  MAPSRC_tcap = 1,       /* TCAP problem */
  MAPSRC_network = 2     /* network service problem */
} MAP_SOURCE;


/*
 * Diagnostic Information values
 */
typedef enum
{
  MAPPDI_short_term_resource_limitation = 0,
  MAPPDI_long_term_resource_limitation = 1,
  MAPPDI_handover_cancellation = 2,
  MAPPDI_radio_channel_release = 3,
  MAPPDI_network_path_release = 4,
  MAPPDI_call_release = 5,
  MAPPDI_associated_procedure_release = 6,
  MAPPDI_tandem_dialogue_release = 7,
  MAPPDI_remote_operations_failure = 8
} MAP_DIAG_INF;

/*
 * Problem Diagnostic
 */
typedef enum
{
  MAPPD_abnormal_event_detected_by_peer = 0,
  MAPPD_response_rejected_by_peer = 1,
  MAPPD_abnormal_event_rx_from_peer = 2,
  MAPPD_message_not_delivered = 3
} MAP_PROB_DIAG;


/*
***********************************************************
*                                                         *
* Service Specific Parameter definitions.            *
*                                                         *
***********************************************************
*/


/*
 * Short Message delivery priority
 */
typedef enum
{
  MAPSM_PRI_priority_low = 0,
  MAPSMPRI_priority_high = 1
} MAP_SM_RP_PRI;

/*
 * User error
 */
typedef enum
{
  MAPUE_unknown_subscriber = 1,
  MAPUE_unidentified_subscriber = 5,
  MAPUE_illegal_subscriber = 9,
  MAPUE_teleservice_not_provisioned = 11,
  MAPUE_illegal_equipment = 12,
  MAPUE_call_barred = 13,
  MAPUE_facility_not_supported = 21,
  MAPUE_absent_subscriber = 27,
  MAPUE_subscriber_busy_for_MT_SMS = 31,
  MAPUE_SM_delivery_failure = 32,
  MAPUE_message_waiting_list_full = 33,
  MAPUE_system_failure = 34,
  MAPUE_data_missing = 35,
  MAPUE_unexpected_data_value = 36,
  MAPUE_resource_limitation = 37,
  MAPUE_initiating_release = 38,
  MAPUE_unknown_alphabet = 71,
  MAPUE_ussd_busy = 72
} MAP_USER_ERR;

/*
 * Provider error
 */
typedef enum
{
  MAPPE_duplicated_invoke_id = 1,
  MAPPE_not_supported_service = 2,
  MAPPE_mistyped_parameter = 3,
  MAPPE_resource_limitation = 4,
  MAPPE_initiating_release = 5,
  MAPPE_unexpected_response_from_peer = 6,
  MAPPE_service_completion_failure = 7,
  MAPPE_no_response_from_peer = 8,
  MAPPE_invalid_response_received = 9
} MAP_PROV_ERR;

/*
 * Srv centre has more short messages to send
 */
typedef enum
{
  MAPMM_more_messages = 0,
  MAPMM_last_message = 1
} MAP_MORE_MSGS;


/*
 * Short message delivery outcome
 */
typedef enum
{
  MAPDO_memory_capacity_exceeded = 0,
  MAPDO_absent_subscriber = 1,
  MAPDO_successful_transfer = 2
} MAP_SM_DELIV_OUTCOME;


/*
 * Reason for srv centre alert
 */
typedef enum
{
  MAPAR_MS_present = 0,
  MAPAR_memory_available = 1
} MAP_ALERT_RSN;


/*
 * Network Resource Type.
 */
typedef enum
{
  MAPNR_PLMN = 0,
  MAPNR_HLR = 1,
  MAPNR_VLR = 2,
  MAPNR_PVLR = 3,
  MAPNR_CONT_MSC = 4,
  MAPNR_VMSC = 5,
  MAPNR_EIR = 6,
  MAPNR_RSS = 7
} MAP_NTWK_RES;

/*
 * Call barring cause
 */
typedef enum
{
  MAPCBC_barring_service_active = 0,
  MAPCBC_operator_barring = 1
} MAP_CALL_BAR_CSE;

/*
 * Delivery Failure cause
 */
typedef enum
{
  MAPDFC_memory_capacity_exceeded = 0,
  MAPDFC_equipment_protocol_error = 1,
  MAPDFC_equipment_not_SM_equipped = 2,
  MAPDFC_unknown_service_centre = 3,
  MAPDFC_SC_congestion = 4,
  MAPDFC_invalid_SME_address = 5,
  MAPDFC_subscriber_not_SC_subscriber = 6
} MAP_DELIV_FAIL_CAUSE;


