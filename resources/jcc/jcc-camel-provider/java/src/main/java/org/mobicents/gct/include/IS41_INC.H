/*
    Copyright (C) 1998 - 2004 Intel Corporation

    Name:          is41_inc.h

    Description:   Include file for use by modules that interface
                   with the IS41 module.

                   For details of the use of each message and the
                   meaning of the parameters please refer to the
                   IS41 Programmer's Manual.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------
   A    26-May-98   RBP   - Initial code.
   B    07-Jun-99   JET   - Updated values for message types
   1    04-May-04   XS    - Added values for new primitives, new operations
                            and new parameters (ORREQ, ANALYZD, CONNRES, 
                            DISCONNRES, CONNFAILRPT, CCDIR, BULKDISC, TBUSY, 
                            TNOANS, OANSWER, ODISCONNECT, TANSWER, TDISCONNECT)
                          - Added define for QoS                            
                    PBu   - Transparency-dedicated configuration message
                          - Implement support for operation transparency
                          - SSS/1179: Clean up source code comments.
                          - SSS/1259: Implement selective tracing, and
                            define new SoftWare Event error codes.
                          - SSS/1260: Extra information in IS41SWE_xxx events.
 */



/*
 * Message type definitions (values for m->hdr.type)
 */

/*
 * Protocol primitive messages:
 */
#define  IS41_MSG_SRV_REQ        (0xc7b0)
#define  IS41_MSG_SRV_IND        (0x87b1)
#define  IS41_MSG_DLG_REQ        (0xc7b2)
#define  IS41_MSG_DLG_IND        (0x87b3)

/*
 * Non-primitive messages:
 */
#define IS41_MSG_CONFIG          (0x77b4)
#define IS41_MSG_CNF_TIM         (0x77b5)
#define IS41_MSG_CNF_TRANSP_OP   (0x77bc)

#define IS41_MSG_ERROR_IND       (0x07b9)
#define IS41_MSG_MAINT_IND       (0x07ba)
#define IS41_MSG_EVENT_IND       (0x07bb)

#define IS41_MSG_TRACE_MASK      (0x57b6)
#define IS41_MSG_S_SELTRACE_MASK (0x57bd)
#define IS41_MSG_S_ERROR_MASK    (0x57b7)
#define IS41_MSG_S_MAINT_MASK    (0x57b8)

/*
 *******************************************************************
 *                                                                 *
 *            Status values used in confirmation messages          *
 *                                                                 *
 *******************************************************************
 */
#define IS41E_MSG_OK       (0)  /* Message received Correctly - Default */
#define IS41E_BAD_ID       (1)  /* Inappropriate/invalid id */
#define IS41E_BAD_STATE    (2)  /* Message received in wrong state */
#define IS41E_BAD_MSG      (5)  /* Inappropriate/unrecognised message */
#define IS41E_BAD_PARAM    (6)  /* Invalid parameters in message */
#define IS41E_NO_RESOURCES (7)  /* Insufficient internal resources */



/*
 *******************************************************************
 *                                                                 *
 *               Message Parameter Definitions                     *
 *                                                                 *
 *******************************************************************
 */

/*
 * Definitions of length, offset and size of
 * IS41_MSG_CONFIG message parameter field:
 */
#define IS41ML_CONFIG                      (40)
#define IS41MO_CONFIG_user_id            (0)
#define IS41MS_CONFIG_user_id              (1)
#define IS41MO_CONFIG_TCAP_id            (1)
#define IS41MS_CONFIG_TCAP_id              (1)
#define IS41MO_CONFIG_mngt_id            (2)
#define IS41MS_CONFIG_mngt_id              (1)
#define IS41MO_CONFIG_maint_id           (3)
#define IS41MS_CONFIG_maint_id             (1)
#define IS41MO_CONFIG_trace_id           (4)
#define IS41MS_CONFIG_trace_id             (1)
#define IS41MO_CONFIG_pad                (5)
#define IS41MS_CONFIG_pad                  (1)
#define IS41MO_CONFIG_base_usr_ogdlg_id  (6)
#define IS41MS_CONFIG_base_usr_ogdlg_id    (2)
#define IS41MO_CONFIG_base_usr_icdlg_id  (8)
#define IS41MS_CONFIG_base_usr_icdlg_id    (2)
#define IS41MO_CONFIG_base_tc_ogdlg_id   (10)
#define IS41MS_CONFIG_base_tc_ogdlg_id     (2)
#define IS41MO_CONFIG_base_tc_icdlg_id   (12)
#define IS41MS_CONFIG_base_tc_icdlg_id     (2)
#define IS41MO_CONFIG_nog_dialogues      (14)
#define IS41MS_CONFIG_nog_dialogues        (2)
#define IS41MO_CONFIG_nic_dialogues      (16)
#define IS41MS_CONFIG_nic_dialogues        (2)
#define IS41MO_CONFIG_num_invokes        (18)
#define IS41MS_CONFIG_num_invokes          (2)
#define IS41MO_CONFIG_options            (20)
#define IS41MS_CONFIG_options              (4)
#define IS41MO_CONFIG_reserved           (24)
#define IS41MS_CONFIG_reserved             (16)

/*
 * Definitions of length, offset and size of
 * IS41_MSG_CNF_TIM message parameter field:
 */
#define IS41ML_CNF_TIM                     (2)
#define IS41MO_CNF_TIM_t0                (0)
#define IS41MS_CNF_TIM_t0                  (2)

/*
 * Definitions of length, offset and size of
 * IS41_MSG_TRACE_MASK message parameter field:
 */
#define IS41ML_TRACE_MASK                  (12)
#define IS41MO_TRACE_MASK_op_evt_mask    (0)
#define IS41MS_TRACE_MASK_op_evt_mask      (4)
#define IS41MO_TRACE_MASK_ip_evt_mask    (4)
#define IS41MS_TRACE_MASK_ip_evt_mask      (4)
#define IS41MO_TRACE_MASK_non_prim_mask  (8)
#define IS41MS_TRACE_MASK_non_prim_mask    (4)

/*
 * Output event trace mask definitions
 */
#define IS41OEM_IS41DLG_IND (0x00000001) /* IS41_MSG_DLG_IND */
#define IS41OEM_IS41SRV_IND (0x00000002) /* IS41_MSG_SRV_IND */
#define IS41OEM_TCDLG_REQ   (0x00000004) /* TCP_MSG_DLG_REQ */
#define IS41OEM_TCCMP_REQ   (0x00000008) /* TCP_MSG_CPT_REQ */

/*
 * Input event trace mask definitions
 */
#define IS41IEM_IS41DLG_REQ (0x00000001) /* IS41_MSG_DLG_REQ */
#define IS41IEM_IS41SRV_REQ (0x00000002) /* IS41_MSG_SRV_REQ */
#define IS41IEM_TCDLG_IND   (0x00000004) /* TCP_MSG_DLG_IND */
#define IS41IEM_TCCMP_IND   (0x00000008) /* TCP_MSG_CPT_IND */

/*
 * Non-primitive trace mask definitions
 */
#define IS41NPM_CNF_TIM         (0x00000001) /* IS41_MSG_CNF_TIM */
#define IS41NPM_MT_EVT          (0x00000002) /* IS41_MSG_MAINT_IND */
#define IS41NPM_SW_EVT          (0x00000004) /* IS41_MSG_ERROR_IND */
#define IS41NPM_STR_MSK         (0x00000008) /* IS41_MSG_S_TRACE_MASK */
#define IS41NPM_SMT_MSK         (0x00000010) /* IS41_MSG_S_MAINT_MASK */
#define IS41NPM_SSW_MSK         (0x00000020) /* IS41_MSG_S_ERROR_MASK */
#define IS41NPM_CNF_TRANSP_OP   (0x00000040) /* IS41_MSG_CNF_TRANSP_OP */
#define IS41NPM_SSL_MSK         (0x00000080) /* IS41_MSG_S_SELTRACE_MASK */

/*
 * Definitions of length, offset and size of
 * IS41_MSG_S_MAINT_MASK message parameter field:
 */
#define IS41ML_MAINT_MASK                  (12)
#define IS41MO_S_MAINT_MASK_mask         (0)
#define IS41MS_S_MAINT_MASK_mask           (9)

/*
 * Definitions of length, offset and size of
 * IS41_MSG_S_ERROR_MASK message parameter field:
 */
#define IS41ML_ERROR_MASK                  (12)
#define IS41MO_S_ERROR_MASK_mask         (0)
#define IS41MS_S_ERROR_MASK_mask           (9)

/*
 * Definitions of length, offset and size of
 * IS41_MSG_S_SELTRACE_MASK message parameter field:
 */
#define IS41ML_S_SELTRACE_MASK             (9)
#define IS41MO_S_SELTRACE_MASK_mask      (0)
#define IS41MS_S_SELTRACE_MASK_mask        (9)

/*
 * Definitions of length, offset and size of
 * IS41_MSG_CNF_TRANSP_OP message parameter field:
 *
 * NOTES:
 * - The parameter area of the IS41_MSG_CNF_TRANSP_OP message consists of:
 *   - a fixed length part, which contains one field:
 *     - cnf_ver
 *   - a variable length part, which contains blocks of operation-related fields:
 *     - op_spec
 *     - priority
 *     - class
 *     - permission_req
 *     - permission_rsp
 *     - parent
 * - As a result, the total length of the parameter area is the sum of the fixed length and the variable
 *   length, which itself is the number of blocks multiplied by the block length.
 *   When N operations are specified, the total length of the parameter area can thus be computed as:
 *   IS41MO_CNF_TRANSP_OP_cnf_ver + IS41MS_CNF_TRANSP_OP_cnf_ver + N * IS41BLOCKLEN_CNF_TRANSP_OP_operation
 *   This computed value can be passed to the getm() function as the "len" parameter.
 * - The rpackbytes() function can be used to fill the parameter area.
 *   For the cnf_ver field in the fixed length part, IS41MO_CNF_TRANSP_OP_cnf_ver and
 *   IS41MS_CNF_TRANSP_OP_cnf_ver can be passed, as usually, as the "dest_byte_offset" and "bytecount"
 *   parameters, respectively.
 *   For the Jth operation in the variable length part, where 0 <= J < N, the fields op_spec, priority,
 *   class, permission_req, permission_rsp and parent can be set using the rpackbytes() function. The value
 *   of the "dest_byte_offset" parameter can be computed as:
 *   (J * IS41BLOCKLEN_CNF_TRANSP_OP_operation) + IS41MO_CNF_TRANSP_OP_<fieldname>
 *   while the value of the "bytecount" parameter is IS41MS_CNF_TRANSP_OP_<fieldname>
 * - The maximum number of operations N that can be specified in a single IS41_MSG_CNF_TRANSP_OP message
 *   is 53. Given that IS41BLOCKLEN_CNF_TRANSP_OP_operation is 6, the maximum length of the parameter area
 *   is 319. This maximum length is defined by IS41MAXLEN_CNF_TRANSP_OP.
 * - Refer to the IS41 Programmer's Manual for further details.
 */
#define IS41MAXLEN_CNF_TRANSP_OP               (319)  /* Maximum length */
#define IS41MO_CNF_TRANSP_OP_cnf_ver         (0)      /* must be set to zero */
#define IS41MS_CNF_TRANSP_OP_cnf_ver           (1)
#define IS41BLOCKLEN_CNF_TRANSP_OP_operation   (6)    /* block length for each operation */
#define IS41MO_CNF_TRANSP_OP_op_spec         (1)      /* operation specifier [0..223] */
#define IS41MS_CNF_TRANSP_OP_op_spec           (1)
#define IS41MO_CNF_TRANSP_OP_priority        (2)      /* MTP priority */
#define IS41MS_CNF_TRANSP_OP_priority          (1)
#define IS41MO_CNF_TRANSP_OP_class           (3)      /* TCAP class */
#define IS41MS_CNF_TRANSP_OP_class             (1)
#define IS41MO_CNF_TRANSP_OP_permission_req  (4)      /* TCAP release permission for this operation in IS41ST_TRANSP_SRV_REQ */
#define IS41MS_CNF_TRANSP_OP_permission_req    (1)
#define IS41MO_CNF_TRANSP_OP_permission_rsp  (5)      /* TCAP release permission for this operation in IS41ST_TRANSP_SRV_RSP */
#define IS41MS_CNF_TRANSP_OP_permission_rsp    (1)
#define IS41MO_CNF_TRANSP_OP_parent          (6)      /* can be a parent of linked operations */
#define IS41MS_CNF_TRANSP_OP_parent            (1)


/*
 * Definitions used in the status field
 * of IS41_MSG_ERROR_IND messages:
 * Extra information is listed between parentheses:
 * - ID = Dialogue ID, provided in id field of IS41_MSG_ERROR_IND messages
 * - MT = Type of the message where the error was observed
 * - PT = Primitive type for which the error was observed
 * - PN = Parameter name for which the error was observed
 * MT, PT and PN are provided in the parameter area of IS41_MSG_ERROR_IND messages
 */
#define IS41SWE_NO_MSSM           (1)     /* IS41_SSM structures exhausted (ID,MT) */
#define IS41SWE_NO_DLG            (2)     /* IS41_DLG structures exhausted (ID,MT) */
#define IS41SWE_NO_ISM            (3)     /* IS41_MSG structures exhausted (ID,MT) */
#define IS41SWE_ISM_LOW           (4)     /* Running short of IS41_MSG structs (ID,MT) */
#define IS41SWE_BAD_MSG           (5)     /* Unsupported inter-task message rx (ID,MT) */
#define IS41SWE_TX_FMT_ERR        (6)     /* Failed to format message for tx (ID,MT,PT) */
#define IS41SWE_USER_BAD_FMT      (7)     /* Bad format message rx from User (ID,MT,PT) */
#define IS41SWE_TCAP_BAD_FMT      (8)     /* Bad format message rx from TCAP (ID,MT,PT) */
#define IS41SWE_UNREC_TYPE        (9)     /* Unrecognised message rx from TCAP */
#define IS41SWE_INVALID_DLG_ID    (10)    /* Received message contains invalid dialogue id (ID,MT) */
#define IS41SWE_USER_MAND_MISSING (11)    /* Missing mandatory parameter in message from user (ID,MT,PT,PN) */
#define IS41SWE_TCAP_MAND_MISSING (12)    /* Missing mandatory parameter in message from TCAP (ID,MT,PT,PN) */
#define IS41SWE_BAD_USER_PRIM     (13)    /* Unknown primitive recvd from user (ID,MT,PT) */
#define IS41SWE_BAD_TCAP_PRIM     (14)    /* Unknown primitive recvd from TCAP (ID,MT,PT) */
#define IS41SWE_USER_PAR_FMT_ERR  (15)    /* Bad parameter format recvd from user (ID,MT,PT,PN) */
#define IS41SWE_TCAP_PAR_FMT_ERR  (16)    /* Bad parameter format recvd from TCAP (ID,MT,PT,PN) */
#define IS41SWE_USER_UNREC_PARAM  (17)    /* Unrecognised parameter recvd from user (ID,MT,PT,PN) */

/*
 * Definitions used in the status field
 * of MGT_MSG_SEL_TRACE messages:
 */
#define IS41t_tx_fmt_err          (0)     /* Failed to format message for tx */
#define IS41t_user_bad_fmt        (1)     /* Bad format message rx from User */
#define IS41t_tcap_bad_fmt        (2)     /* Bad format message rx from TCAP */
#define IS41t_unrec_type          (3)     /* Unrecognised message rx from TCAP */
#define IS41t_invalid_dlg_id      (4)     /* Received message contains invalid dialogue id */
#define IS41t_user_mand_missing   (5)     /* Missing mandatory parameter in message from user */
#define IS41t_tcap_mand_missing   (6)     /* Missing mandatory parameter in message from TCAP */
#define IS41t_bad_user_prim       (7)     /* Unknown primitive recvd from user */
#define IS41t_bad_tcap_prim       (8)     /* Unknown primitive recvd from TCAP */
#define IS41t_user_par_fmt_err    (9)     /* Bad parameter format recvd from user */
#define IS41t_tcap_par_fmt_err    (10)    /* Bad parameter format recvd from TCAP */
#define IS41t_user_unrec_param    (11)    /* Unrecognised parameter recvd from user */


/*
 *******************************************************************
 *                                                                 *
 *           Definitions for IS41 Primitives and Parameters        *
 *                                                                 *
 *******************************************************************
 */

/*
 * IS41 dialogue primitive types
 * The abort component is not used in IS41 - the associated
 * primitives are not available.
 */
#define  IS41DT_OPEN_REQ                (1)     /* IS41-OPEN-REQ */
#define  IS41DT_OPEN_IND                (1)     /* IS41-OPEN-IND */
#define  IS41DT_CLOSE_REQ               (2)     /* IS41-CLOSE-REQ */
#define  IS41DT_CLOSE_IND               (2)     /* IS41-CLOSE-IND */
#define  IS41DT_DELIMITER_REQ           (3)     /* IS41-DELIMITER-REQ */
#define  IS41DT_DELIMITER_IND           (3)     /* IS41-DELIMITER-IND */
#define  IS41DT_U_ABORT_REQ             (4)     /* IS41-U-ABORT-REQ */
#define  IS41DT_U_ABORT_IND             (4)     /* IS41-U-ABORT-IND */
#define  IS41DT_P_ABORT_IND             (5)     /* IS41-P-ABORT-IND */
#define  IS41DT_NOTICE_IND              (6)     /* IS41-NOTICE-IND */
#define  IS41DT_UNI_REQ                 (7)     /* IS41-UNI-REQ */
#define  IS41DT_UNI_IND                 (7)     /* IS41-UNI-IND */
#define  IS41DT_MAX_DLG_REQ_PRIM        (7)     /* Max user dlg primitive value */

#define  IS41DT_OPEN_RSP                (129)   /* IS41-OPEN-RSP */
#define  IS41DT_OPEN_CNF                (129)   /* IS41-OPEN-CNF */
#define  IS41DT_MAX_DLG_ACK_PRIM        (129)   /* Max user dlg primitive value */




/*
 * IS41 service primitive types
 */
#define  IS41ST_NULL_PRIMITIVE          (0)
#define  IS41ST_SMS_DB_REQ              (1)   /* IS41-SMS-DELIVERY-BACKWARD-REQ */
#define  IS41ST_SMS_DB_IND              (1)   /* IS41-SMS-DELIVERY-BACKWARD-IND */
#define  IS41ST_SMS_DF_REQ              (2)   /* IS41-SMS-DELIVERY-FORWARD-REQ */
#define  IS41ST_SMS_DF_IND              (2)   /* IS41-SMS-DELIVERY-FORWARD-IND */
#define  IS41ST_SMS_DPTP_REQ            (3)   /* IS41-SMS-DELIVERY-POINT-TO-POINT-REQ */
#define  IS41ST_SMS_DPTP_IND            (3)   /* IS41-SMS-DELIVERY-POINT-TO-POINT-IND */
#define  IS41ST_SMS_N_REQ               (4)   /* IS41-SMS-NOTIFICATION-REQ */
#define  IS41ST_SMS_N_IND               (4)   /* IS41-SMS-NOTIFICATION-IND */
#define  IS41ST_SMS_R_REQ               (5)   /* IS41-SMS-REQUEST-REQ */
#define  IS41ST_SMS_R_IND               (5)   /* IS41-SMS-REQUEST-IND */
#define  IS41ST_ORREQ_REQ               (6)   /* IS826-ORIGINATIONREQUEST-REQ */
#define  IS41ST_ORREQ_IND               (6)   /* IS826-ORIGINATIONREQUEST-IND */
#define  IS41ST_ANALYZD_REQ             (7)   /* IS826-ANALYZED-INFORMATION-REQ */
#define  IS41ST_ANALYZD_IND             (7)   /* IS826-ANALYZED-INFORMATION-IND */
#define  IS41ST_CONNRES_REQ             (8)   /* IS771-CONNRES-REQ */
#define  IS41ST_CONNRES_IND             (8)   /* IS771-CONNRES-IND */
#define  IS41ST_CONNFAILRPT_REQ         (9)   /* IS771-CONNFAILRPT-REQ */
#define  IS41ST_CONNFAILRPT_IND         (9)   /* IS771-CONNFAILRPT-IND */
#define  IS41ST_DISCONNRES_REQ          (10)  /* IS771-DISCONNRES-REQ */
#define  IS41ST_DISCONNRES_IND          (10)  /* IS771-DISCONNRES-IND */
#define  IS41ST_CCDIR_REQ               (11)  /* IS826-CCDIR-REQ */
#define  IS41ST_CCDIR_IND               (11)  /* IS826-CCDIR-IND */
#define  IS41ST_TBUSY_REQ               (12)  /* IS826-TBUSY-REQ */
#define  IS41ST_TBUSY_IND               (12)  /* IS826-TBUSY-IND */
#define  IS41ST_TNOANS_REQ              (13)  /* IS826-TNOANS-REQ */
#define  IS41ST_TNOANS_IND              (13)  /* IS826-TNOANS-IND */
#define  IS41ST_BULKDISCONN_REQ         (14)  /* IS826-BULKDISCONN-REQ */
#define  IS41ST_BULKDISCONN_IND         (14)  /* IS826-BULKDISCONN-IND */
#define  IS41ST_OANSWER_REQ             (15)  /* IS826-OANSWER-REQ */
#define  IS41ST_OANSWER_IND             (15)  /* IS826-OANSWER-IND */
#define  IS41ST_ODISCONNECT_REQ         (16)  /* IS826-ODISCONNECT-REQ */
#define  IS41ST_ODISCONNECT_IND         (16)  /* IS826-ODISCONNECT-IND */
#define  IS41ST_TANSWER_REQ             (17)  /* IS826-TANSWER-REQ */
#define  IS41ST_TANSWER_IND             (17)  /* IS826-TANSWER-IND */
#define  IS41ST_TDISCONNECT_REQ         (18)  /* IS826-TDISCONNECT-REQ */
#define  IS41ST_TDISCONNECT_IND         (18)  /* IS826-TDISCONNECT-IND */
#define  IS41ST_TRANSP_SRV_REQ          (19)  /* Transparency-REQ */
#define  IS41ST_TRANSP_SRV_IND          (19)  /* Transparency-IND */

#define  IS41ST_MAX_SRV_REQ_PRIM        (19)   /* Max value of the service primitives */

/*
 * Values above 127 are used for acknowledgements
 */
#define  IS41ST_SMS_DB_RSP              (129)   /* IS41-SMS-DELIVERY-BACKWARD-RSP */
#define  IS41ST_SMS_DB_CNF              (129)   /* IS41-SMS-DELIVERY-BACKWARD-CNF */
#define  IS41ST_SMS_DF_RSP              (130)   /* IS41-SMS-DELIVERY-FORWARD-RSP */
#define  IS41ST_SMS_DF_CNF              (130)   /* IS41-SMS-DELIVERY-FORWARD-CNF */
#define  IS41ST_SMS_DPTP_RSP            (131)   /* IS41-SMS-DELIVERY-POINT-TO-POINT-RSP */
#define  IS41ST_SMS_DPTP_CNF            (131)   /* IS41-SMS-DELIVERY-POINT-TO-POINT-CNF */
#define  IS41ST_SMS_N_RSP               (132)   /* IS41-SMS-NOTIFICATION-RSP */
#define  IS41ST_SMS_N_CNF               (132)   /* IS41-SMS-NOTIFICATION-CNF */
#define  IS41ST_SMS_R_RSP               (133)   /* IS41-SMS-REQUEST-RSP */
#define  IS41ST_SMS_R_CNF               (133)   /* IS41-SMS-REQUEST-CNF */
#define  IS41ST_ORREQ_RSP               (134)   /* IS826-ORIGINATIONREQUEST-RSP */
#define  IS41ST_ORREQ_CNF               (134)   /* IS826-ORIGINATIONREQUEST-CNF */
#define  IS41ST_ANALYZD_RSP             (135)   /* IS826-ANALYZED-INFORMATION-RSP */
#define  IS41ST_ANALYZD_CNF             (135)   /* IS826-ANALYZED-INFORMATION-CNF */
#define  IS41ST_CONNRES_RSP             (136)   /* IS771-CONNRES-RSP */
#define  IS41ST_CONNRES_CNF             (136)   /* IS771-CONNRES-CNF */
#define  IS41ST_CONNFAILRPT_RSP         (137)   /* IS771-CONNFAILRPT-RSP */
#define  IS41ST_CONNFAILRPT_CNF         (137)   /* IS771-CONNFAILRPT-CNF */
#define  IS41ST_DISCONNRES_RSP          (138)   /* IS771-DISCONNRES-RSP */
#define  IS41ST_DISCONNRES_CNF          (138)   /* IS771-DISCONNRES-CNF */
#define  IS41ST_CCDIR_RSP               (139)   /* IS826-CCDIR-RSP */
#define  IS41ST_CCDIR_CNF               (139)   /* IS826-CCDIR-CNF */
#define  IS41ST_TBUSY_RSP               (140)   /* IS826-TBUSY-RSP */
#define  IS41ST_TBUSY_CNF               (140)   /* IS826-TBUSY-CNF */
#define  IS41ST_TNOANS_RSP              (141)   /* IS826-TNOANS-RSP */
#define  IS41ST_TNOANS_CNF              (141)   /* IS826-TNOANS-CNF */
#define  IS41ST_BULKDISCONN_RSP         (142)   /* IS826-BULKDISCONN-RSP */
#define  IS41ST_BULKDISCONN_CNF         (142)   /* IS826-BULKDISCONN-CNF */
#define  IS41ST_ODISCONNECT_RSP         (144)   /* IS826-ODISCONNECT-RSP */
#define  IS41ST_ODISCONNECT_CNF         (144)   /* IS826-ODISCONNECT-CNF */
#define  IS41ST_TDISCONNECT_RSP         (146)   /* IS826-TDISCONNECT-RSP */
#define  IS41ST_TDISCONNECT_CNF         (146)   /* IS826-TDISCONNECT-CNF */
#define  IS41ST_TRANSP_SRV_RSP          (147)   /* Transparency-RSP */
#define  IS41ST_TRANSP_SRV_CNF          (147)   /* Transparency-CNF */

#define  IS41ST_MAX_SRV_ACK_PRIM        (147)   /* Max value of the acknowledge primitives */

/*
 * Primitive parameter name definitions
 * Aligned with MAP parameter names.
 */
#define  IS41PN_null                    (0)
#define  IS41PN_dest_address            (1)       /* Destination address */
#define  IS41PN_orig_address            (3)       /* Origination address */
#define  IS41PN_result                  (5)       /* Result */
#define  IS41PN_release_method          (7)       /* Release method */
#define  IS41PN_user_rsn                (8)       /* User reason */
#define  IS41PN_prov_rsn                (9)       /* Provider reason */
#define  IS41PN_source                  (12)      /* Source */
#define  IS41PN_prob_diag               (13)      /* Problem diagnostic */
#define  IS41PN_invoke_id               (14)      /* invoke id */
#define  IS41PN_linked_id               (15)      /* linked id */
#define  IS41PN_timeout                 (16)      /* Operation TCAP timeout */
#define  IS41PN_interMSCcctId           (17)      /* Inter MSC circuit id */
#define  IS41PN_mobileIdNum             (18)      /* 6.5.2.81 - Mobile ID num */
#define  IS41PN_SMSbearerdata           (19)      /* SMS bearer data */
#define  IS41PN_SMStelesrvId            (20)      /* SMS teleservice id */
#define  IS41PN_esn                     (21)      /* 6.5.2.63 - Electronic serial number */
#define  IS41PN_SMSchargeInd            (22)      /* SMS charge Indicator */
#define  IS41PN_user_err                (23)      /* User Error */
#define  IS41PN_prov_err                (24)      /* Provider Error */
#define  IS41PN_SMSdestAddr             (25)      /* SMS destination address */
#define  IS41PN_SMSorigDestAddr         (26)      /* SMS original destination address */
#define  IS41PN_SMSorigDestSubAddr      (27)      /* SMS original destination sub-address */
#define  IS41PN_SMSorigAddr             (28)      /* SMS origination address */
#define  IS41PN_SMSorigOrigAddr         (29)      /* SMS original origination address */
#define  IS41PN_SMSorigOrigSubAddr      (30)      /* SMS original origination sub-address */
#define  IS41PN_SMScauseCode            (31)      /* SMS cause code */
#define  IS41PN_SMSmsgCount             (32)      /* SMS message count */
#define  IS41PN_SMSnoteInd              (33)      /* SMS notification Indication */
#define  IS41PN_SMSaccessDeniedRsn      (34)      /* SMS Access denied reason */
#define  IS41PN_SMSaddress              (35)      /* SMS address */
#define  IS41PN_faultyParam             (36)      /* Faulty Parameter */
#define  IS41PN_destinationDigits           (37)       /* 6.5.2.56 - DestinationDigits */
#define  IS41PN_carrierDigits               (38)       /* 6.5.2.28 - CarrierDigits */
#define  IS41PN_routingDigits               (39)       /* 6.5.2.114 - RoutingDigits */
#define  IS41PN_billingID                   (40)       /* 6.5.2.16 - billingID */
#define  IS41PN_MSCID                       (41)       /* 6.5.2.82 - MSCID */
#define  IS41PN_transactionCapability       (42)       /* 6.5.2.160 - TransactionCapability */
#define  IS41PN_triggerType                 (43)       /* 6.5.2.dh - TriggerType */
#define  IS41PN_callingPartyNumberDigits1   (44)       /* 6.5.2.21 - CallingPartyNumberDigits1 */
#define  IS41PN_callingPartyNumberDigits2   (45)       /* 6.5.2.22 - CallingPartyNumberDigits2 */
#define  IS41PN_callingPartySubAddress      (46)       /* 6.5.2.25 - CallingPartySubAddress */
#define  IS41PN_groupInformation            (47)       /* 6.5.2.69 - GroupInformation */
#define  IS41PN_legInformation              (48)       /* 6.5.2.75 - LegInformation */
#define  IS41PN_locationAreaID              (49)       /* 6.5.2.77 - LocationAreaID */
#define  IS41PN_mobileDirectoryNumber       (50)       /* 6.5.2.80 - MobileDirectoryNumber */
#define  IS41PN_MSCIdentificationNumber     (51)       /* 6.5.2.83 - MSCIdentificationNumber */
#define  IS41PN_oneTimeFeatureIndicator     (52)       /* 6.5.2.88 - OneTimeFeatureIndicator */
#define  IS41PN_pilotBillingID              (53)       /* 6.5.2.94 - PilotBillingID */
#define  IS41PN_pilotNumber                 (54)       /* 6.5.2.95 - PilotNumber */
#define  IS41PN_preferredLanguageIndicator  (55)       /* 6.5.2.96 - PreferredLanguageIndicator */
#define  IS41PN_redirectingNumberDigits     (56)       /* 6.5.2.107 - RedirectingNumberDigits */
#define  IS41PN_redirectingSubAddress       (57)       /* 6.5.2.109 - RedirectingSubAddress */
#define  IS41PN_redirectionReason           (58)       /* 6.5.2.110 - RedirectingReason */
#define  IS41PN_servingCellID               (59)       /* 6.5.2.117 - ServingCellID */
#define  IS41PN_systemMyTypeCode            (60)       /* 6.5.2.147 - SystemMyTypeCode */
#define  IS41PN_terminationAccessType       (61)       /* 6.5.2.155 - TerminationAccessType */
#define  IS41PN_digits                      (62)       /* 6.5.2.58 - Digits */
#define  IS41PN_originationTriggers         (63)       /* 6.5.2.90 - OriginationTriggers */
#define  IS41PN_callingPartyName            (64)       /* 6.5.2.bw - CallingPartyName */
#define  IS41PN_featureIndicator            (65)       /* 6.5.2.ej - FeatureIndicator */
#define  IS41PN_PC_SSN                      (66)       /* 6.5.2.93 - PC_SSN */
#define  IS41PN_senderIdentificationNumber  (67)       /* 6.5.2.116 - SenderIdentificationNumber */
#define  IS41PN_conferenceCallingIndicator  (68)       /* 6.5.2.49 - ConferenceCallingIndicator */
#define  IS41PN_DMHRedirectionIndicator     (69)       /* 6.5.2.62 - DMHRedirectionIndicator */
#define  IS41PN_redirectingPartyName        (70)       /* 6.5.2.by - RedirectingPartyName */
#define  IS41PN_timeDateOffset              (71)       /* 6.5.2.dd - TimeDateOffset */
#define  IS41PN_timeOfDay                   (72)       /* 6.5.2.em - TimeOfDay */
#define  IS41PN_failureType                 (73)       /* 6.5.2.cn - FailureTime */
#define  IS41PN_failureCause                (74)       /* 6.5.2.cm - FailureCause */
#define  IS41PN_actionCode                  (75)       /* 6.5.2.2 - ActionCode */
#define  IS41PN_displayText                 (76)       /* 6.5.2.bx - DisplayText */
#define  IS41PN_DMHAccountCodeDigits        (77)       /* 6.5.2.59 - DMHAccountCodeDigits */
#define  IS41PN_DMHAlternateBillingDigits   (78)       /* 6.5.2.60 - DMHAlternateBillingDigits */
#define  IS41PN_releaseCause                (79)       /* 6.5.2.el - ReleaseCause */
#define  IS41PN_DMHBillingDigits            (80)       /* 6.5.2.61 - DMHBillingDigits */
#define  IS41PN_IMSI                        (81)       /* 6.5.2.bv - IMSI, used by MSID defined in 6.5.2.bv  */
#define  IS41PN_WINCapability               (82)       /* 6.5.2.di - WINCapability */
#define  IS41PN_announcementList            (83)       /* 6.5.2.6 - AnnouncementList */
#define  IS41PN_terminationList             (84)       /* 6.5.2.156 - TerminationList */
#define  IS41PN_accessDeniedReason          (85)       /* 6.5.2.1 - AccessDeniedReason */
#define  IS41PN_callingPartyNumberString1   (86)       /* 6.5.2.23 - CallingPartyNumberString1 */
#define  IS41PN_callingPartyNumberString2   (87)       /* 6.5.2.24 - CallingPartyNumberString2 */
#define  IS41PN_noAnswerTime                (88)       /* 6.5.2.87 - NoAnswerTime */
#define  IS41PN_redirectingNumberString     (89)       /* 6.5.2.108 - RedirectingNumberString */
#define  IS41PN_terminationTriggers         (90)       /* 6.5.2.159 - TerminationTriggers */
#define  IS41PN_resumePIC                   (91)       /* 6.5.2.cu - ResumePIC */
#define  IS41PN_callStatus                  (92)       /* 6.5.2.en - CallStatus */
#define  IS41PN_DMHServiceID                (93)       /* 6.5.2.ei - DMH_ServiceID */
#define  IS41PN_triggerAddressList          (94)       /* 6.5.2.de - TriggerAddressList */
#define  IS41PN_transp_component            (95)       /* Transparency: raw contents of the TCPPN_COMPONENT TCAP parameter */
#define  IS41PN_qos                         (96)       /* QOS */

#define  IS41PN_end                         (96)      /* Max value of parameter IDs*/

/*
 * Refuse reason values
 */
#define  IS41RR_no_reason               (0)       /* No reason given */
#define  IS41RR_inv_dest_ref            (1)       /* Invalid destination reference */
#define  IS41RR_inv_orig_ref            (2)       /* Invalid origination reference */
#define  IS41RR_appl_context            (3)       /* Application context not supported */
#define  IS41RR_ver_incomp              (4)       /* Potential version incompatibility */

/*
 * Provider reason values.
 */
#define  IS41PR_prov_malfct             (0)       /* Provider malfunction */
#define  IS41PR_dlg_rlsd                (1)       /* Supporting dialogue/transaction released */
#define  IS41PR_rsrc_limit              (2)       /* Resource limitation */
#define  IS41PR_mnt_act                 (3)       /* Maintenance activity */
#define  IS41PR_ver_incomp              (4)       /* version incompatibility */
#define  IS41PR_ab_dlg                  (5)       /* Abnormal IS41 dialogue */
#define  IS41PR_invalid_PDU             (6)       /* Invalid PDU */


/*
 * User reason values.
 */
#define  IS41UR_user_specific           (0)       /* user specific reason */
#define  IS41UR_user_resource_limitaion (1)       /* user resource limitation */
#define  IS41UR_resource_unavail        (2)       /* resource unavailable */
#define  IS41UR_app_proc_cancelled      (3)       /* application procedure cancelled */
#define  IS41UR_procedure_error         (4)       /* Procedure Error */
#define  IS41UR_unspecified_reason      (5)       /* Unspecified Reason */
#define  IS41UR_version_not_supported   (6)       /* Version Not Supported */


/*
 * Release Method values
 */
#define  IS41RM_normal_release          (0)       /* Use normal release handshake */
#define  IS41RM_prearranged_end         (1)       /* Release at predetermined point */


/*
 * Result values
 */
#define  IS41RS_DLG_ACC                 (0)       /* Dialogue accepted */
#define  IS41RS_DLG_REF                 (1)       /* Dialogue refused */

/*
 * Source values
 */
#define  IS41SRC_is41                   (0)       /* IS41 problem */
#define  IS41SRC_tcap                   (1)       /* TCAP problem */
#define  IS41SRC_network                (2)       /* network service problem */

/*
 * Problem Diagnostic
 */
#define  IS41PD_abnormal_event_detected_by_peer (0)
#define  IS41PD_response_rejected_by_peer       (1)
#define  IS41PD_abnormal_event_rx_from_peer     (2)

/*
 * IS41 User error codes
 */
#define  IS41UE_UnrecognisedMIN                 (0x81)
#define  IS41UE_UnrecognisedESN                 (0x82)
#define  IS41UE_MIN_HLRMismatch                 (0x83)
#define  IS41UE_OperationSequenceProblem        (0x84)
#define  IS41UE_ResourceShortage                (0x85)
#define  IS41UE_OperationNotSupported           (0x86)
#define  IS41UE_TrunkUnavailable                (0x87)
#define  IS41UE_ParameterError                  (0x88)
#define  IS41UE_SystemFailure                   (0x89)
#define  IS41UE_UnrecognisedParameterValue      (0x8a)
#define  IS41UE_FeatureInactive                 (0x8b)
#define  IS41UE_MissingParameter                (0x8c)


/*
 ***********************************************************
 *                                                         *
 * Service Specific Parameter definitions.                 *
 *                                                         *
 ***********************************************************
 */


/*
 * Short Message delivery priority
 */
#define  IS41SMPRI_priority_low                 (0)
#define  IS41SMPRI_priority_high                (1)


/*
 * Provider error
 */
#define  IS41PE_duplicated_invoke_id            (1)
#define  IS41PE_not_supported_service           (2)
#define  IS41PE_mistyped_parameter              (3)
#define  IS41PE_resource_limitation             (4)
#define  IS41PE_initiating_release              (5)
#define  IS41PE_unexpected_response_from_peer   (6)
#define  IS41PE_service_completion_failure      (7)
#define  IS41PE_no_response_from_peer           (8)
#define  IS41PE_invalid_response_received       (9)

/*
 * Srv centre has more short messages to send
 */
#define  IS41MM_more_messages                   (0)
#define  IS41MM_last_message                    (1)


/*
 * Short message delivery outcome
 */
#define  IS41DO_memory_capacity_exceeded        (0)
#define  IS41DO_absent_subscriber               (1)
#define  IS41DO_successful_transfer             (2)


/*
 * Reason for srv centre alert
 */
#define  IS41AR_MS_present                      (0)
#define  IS41AR_memory_available                (1)


/*
 * Network Resource Type.
 */
#define  IS41NR_PLMN                            (0)
#define  IS41NR_HLR                             (1)
#define  IS41NR_VLR                             (2)
#define  IS41NR_PVLR                            (3)
#define  IS41NR_CONT_MSC                        (4)
#define  IS41NR_VMSC                            (5)
#define  IS41NR_EIR                             (6)
#define  IS41NR_RSS                             (7)

/*
 * Call barring cause
 */
#define  IS41CBC_barring_service_active         (0)
#define  IS41CBC_operator_barring               (1)

/*
 * Delivery Failure cause
 */
#define  IS41DFC_memory_capacity_exceeded       (0)
#define  IS41DFC_equipment_protocol_error       (1)
#define  IS41DFC_equipment_not_SM_equipped      (2)
#define  IS41DFC_unknown_service_centre         (3)
#define  IS41DFC_SC_congestion                  (4)
#define  IS41DFC_invalid_SME_address            (5)
#define  IS41DFC_subscriber_not_SC_subscriber   (6)


