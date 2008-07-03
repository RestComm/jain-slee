/*
                  Copyright (C) 1998-2003 Intel Corporation.

        Name:          inap_inc.h

        Description:   INAP API interface experiment.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------
   A    17-Jun-98  RBP    - Initial code.
   1    06-Nov-98  RBP    - First Release.
   2    26-May-99  JET    - INAPML_CONFIG increased from 23 to 40
                            INAP_MSG_CNF_AC message changed to include
                            further reserved space for future expansion.
   3    01-Nov-99  JET    - INAPMO_CONFIG_filler corrected to 5.
   4    22-May-00  JET    - Addition of INAPPN_release_confirm parameter.
   5    29-Nov-01  JET    - INAP_MSG_EVENT no longer used.
   6    17-MAy-02  JER    - Addition of QOS parameter.
   7    04-Dec-02  JTD    - Change to use of Intel Corp Copyright.
   8    06-May-03  YTA    - Change to add User Info. parameter.
   9    15-Aug-03  YTA    - Addition of new problem diagnostic codes.
*/


/*
 * Protocol primitive messages:
 */
#define  INAP_MSG_SRV_REQ       (0xc7f0)
#define  INAP_MSG_SRV_IND       (0x87f1)
#define  INAP_MSG_DLG_REQ       (0xc7f2)
#define  INAP_MSG_DLG_IND       (0x87f3)

/*
 * Non-primitive messages:
 */
#define INAP_MSG_CONFIG         (0x77f4)
#define INAP_MSG_CNF_TIM        (0x77f5)
#define INAP_MSG_CNF_AC         (0x77f6)
#define INAP_MSG_CNF_FE         (0x77f7)

#define INAP_MSG_ERROR_IND      (0x07f8)
#define INAP_MSG_MAINT_IND      (0x07f9)
#define INAP_MSG_EVENT_IND      (0x07fa)  /* Obsolete: Not longer used */

#define INAP_MSG_TRACE_MASK     (0x57fb)
#define INAP_MSG_S_ERROR_MASK   (0x57fc)
#define INAP_MSG_S_MAINT_MASK   (0x57fd)

/*
*******************************************************************
*                                                                 *
*            Status values used in confirmation messages          *
*                                                                 *
*******************************************************************
*/
#define INAPE_MSG_OK            (0)  /* Message received Correctly */
#define INAPE_BAD_ID            (1)  /* Inappropriate/invalid id */
#define INAPE_BAD_STATE         (2)  /* Message received in wrong state */
#define INAPE_BAD_MSG           (5)  /* Inappropriate/unrecognised message */
#define INAPE_BAD_PARAM         (6)  /* Invalid parameters in message */
#define INAPE_NO_RESOURCES      (7)  /* Insufficient internal resources */


/*
 * Definition of options field in INAP_CONFIG_MSG message :
 */
#define INAPF_ANSI       (0x0001)  /* Use ANSI SS7 stack. */
#define INAPF_TRNS_AC    (0x0002)  /* Pass Application Context transparently */
#define INAPF_24PC       (0x0004)  /* Use 24 bit point codes. */

/*
 * Definitions of length, offset and size of
 * INAP_MSG_CONFIG message parameter field:
 */
#define INAPML_CONFIG                    (40)
#define INAPMO_CONFIG_user_id            (0)
#define INAPMS_CONFIG_user_id              (1)
#define INAPMO_CONFIG_TCAP_id            (1)
#define INAPMS_CONFIG_TCAP_id              (1)
#define INAPMO_CONFIG_mngt_id            (2)
#define INAPMS_CONFIG_mngt_id              (1)
#define INAPMO_CONFIG_maint_id           (3)
#define INAPMS_CONFIG_maint_id             (1)
#define INAPMO_CONFIG_trace_id           (4)
#define INAPMS_CONFIG_trace_id             (1)
#define INAPMO_CONFIG_filler             (5)
#define INAPMS_CONFIG_filler               (1)
#define INAPMO_CONFIG_base_usr_ogdlg_id  (6)
#define INAPMS_CONFIG_base_usr_ogdlg_id    (2)
#define INAPMO_CONFIG_base_usr_icdlg_id  (8)
#define INAPMS_CONFIG_base_usr_icdlg_id    (2)
#define INAPMO_CONFIG_base_tc_ogdlg_id   (10)
#define INAPMS_CONFIG_base_tc_ogdlg_id     (2)
#define INAPMO_CONFIG_base_tc_icdlg_id   (12)
#define INAPMS_CONFIG_base_tc_icdlg_id     (2)
#define INAPMO_CONFIG_nog_dialogues      (14)
#define INAPMS_CONFIG_nog_dialogues        (2)
#define INAPMO_CONFIG_nic_dialogues      (16)
#define INAPMS_CONFIG_nic_dialogues        (2)
#define INAPMO_CONFIG_num_invokes        (18)
#define INAPMS_CONFIG_num_invokes          (2)
#define INAPMO_CONFIG_options            (20)
#define INAPMS_CONFIG_options              (4)

#define INAPMO_max_CONFIG_offset         (24)

/*
 * Definitions of length, offset and size of
 * INAP_MSG_CNF_TIM message parameter field:
 */
#define INAPML_CNF_TIM                  (2)
#define INAPMO_CNF_TIM_tguard           (0)
#define INAPMS_CNF_TIM_tguard             (2)

/*
 * Definitions of length, offset and size of
 * INAP_MSG_CNF_AC message parameter field:
 */
#define INAPMO_CNF_AC_filler            (0)
#define INAPMS_CNF_AC_filler              (8)
#define INAPMO_CNF_AC_len               (8)
#define INAPMS_CNF_AC_len                 (1)
#define INAPMO_CNF_AC_context           (9)


/*
 * Definition of flags field in INAP_CNF_FE_MSG message :
 */
#define INAPFE_LOCAL            (0x0001)      /*FE is at the local point code*/
/*
 * Definitions of length, offset and size of
 * INAP_MSG_CNF_FE message parameter field:
 */
#define INAPML_CNF_FE                   (40)
#define INAPMO_CNF_FE_options           (0)
#define INAPMS_CNF_FE_options             (2)
#define INAPMO_CNF_FE_local_module_id   (2)
#define INAPMS_CNF_FE_local_module_id     (1)
#define INAPMO_CNF_FE_addr_len          (3)
#define INAPMS_CNF_FE_addr_len            (1)
#define INAPMO_CNF_FE_addr              (4)
#define INAPMS_CNF_FE_addr                (34)



/*
 * Definitions of length, offset and size of
 * INAP_MSG_TRACE_MASK message parameter field:
 */
#define INAPML_TRACE_MASK                (12)
#define INAPMO_TRACE_MASK_op_evt_mask    (0)
#define INAPMS_TRACE_MASK_op_evt_mask      (4)
#define INAPMO_TRACE_MASK_ip_evt_mask    (4)
#define INAPMS_TRACE_MASK_ip_evt_mask      (4)
#define INAPMO_TRACE_MASK_non_prim_mask  (8)
#define INAPMS_TRACE_MASK_non_prim_mask    (4)

/*
 * Output event trace mask definitions
 */
#define INAPOEM_INAPDLG_IND     (0x00000001) /* INAP_MSG_DLG_IND */
#define INAPOEM_INAPSRV_IND     (0x00000002) /* INAP_MSG_SRV_IND */
#define INAPOEM_TCDLG_REQ       (0x00000004) /* TCP_MSG_DLG_REQ */
#define INAPOEM_TCCMP_REQ       (0x00000008) /* TCP_MSG_CPT_REQ */

/*
 * Input event trace mask definitions
 */
#define INAPIEM_INAPDLG_REQ     (0x00000001) /* INAP_MSG_DLG_REQ */
#define INAPIEM_INAPSRV_REQ     (0x00000002) /* INAP_MSG_SRV_REQ */
#define INAPIEM_TCDLG_IND       (0x00000004) /* TCP_MSG_DLG_IND */
#define INAPIEM_TCCMP_IND       (0x00000008) /* TCP_MSG_CPT_IND */

/*
 * Non-primitive trace mask definitions
 */
#define INAPNPM_CNF_TIM         (0x00000001) /* INAP_MSG_CNF_TIM */
#define INAPNPM_MT_EVT          (0x00000002) /* INAP_MSG_MAINT_IND */
#define INAPNPM_SW_EVT          (0x00000004) /* INAP_MSG_ERROR_IND */
#define INAPNPM_SMT_MSK         (0x00000008) /* INAP_MSG_S_MAINT_MASK */
#define INAPNPM_SSW_MSK         (0x00000010) /* INAP_MSG_S_ERROR_MASK */
#define INAPNPM_CNF_FE          (0x00000020) /* INAP_MSG_CNF_FE */
#define INAPNPM_CNF_AC          (0x00000040) /* INAP_MSG_CNF_AC */

/*
 * Definitions of length, offset and size of
 * INAP_MSG_S_MAINT_MASK message parameter field:
 */
#define INAPML_MAINT_MASK                (12)
#define INAPMO_S_MAINT_MASK_mask         (0)
#define INAPMS_S_MAINT_MASK_mask           (9)

/*
 * Definitions used in the status field
 * of INAP_MSG_MAINT_IND messages:
 */
#define INAPME_INVALID_AC                (1)  /* Dialogue with invalid AC rx */



/*
 * Definitions of length, offset and size of
 * INAP_MSG_S_ERROR_MASK message parameter field:
 */
#define INAPML_ERROR_MASK                (12)
#define INAPMO_S_ERROR_MASK_mask         (0)
#define INAPMS_S_ERROR_MASK_mask           (9)


/*
 * Definitions used in the status field
 * of INAP_MSG_ERROR_IND messages:
 */
#define INAPSWE_NO_MSSM          (1)     /* INAP_SSM structures exhausted */
#define INAPSWE_NO_DLG           (2)     /* INAP_DLG structures exhausted */
#define INAPSWE_NO_ISM           (3)     /* INAP_MSG structures exhausted */
#define INAPSWE_ISM_LOW          (4)     /* Running short of INAP_MSG structs */
#define INAPSWE_BAD_MSG          (5)     /* Unsupported inter-task message rx */
#define INAPSWE_TX_FMT_ERR       (6)     /* Failed to format message for tx */
#define INAPSWE_USER_BAD_FMT     (7)     /* Badly formatted User msg rx */
#define INAPSWE_TCAP_BAD_FMT     (8)     /* Badly formatted TCAP message rx */
#define INAPSWE_UNREC_TYPE       (9)     /* Unrecognised TCAP message rx  */
#define INAPSWE_INVALID_DLG_ID   (10)    /* TCAP Msg rx invalid dialogue id */

/*
*******************************************************************
*                                                                 *
*            User Parameter name definitions                      *
*                                                                 *
*******************************************************************
*/
/*
 * Dialogue parameter definitions.
 */
#define INAPPN_dest_address               (1)
#define INAPPN_orig_address               (3)
#define INAPPN_result                     (5)
#define INAPPN_refuse_rsn                 (6)
#define INAPPN_release_method             (7)
#define INAPPN_user_rsn                   (8)
#define INAPPN_prov_rsn                   (9)
#define INAPPN_user_info                  (10)
#define INAPPN_applic_context             (11)
#define INAPPN_source                     (12)
#define INAPPN_prob_diag                  (13)
#define INAPPN_dest_FE                    (14)
#define INAPPN_orig_FE                    (15)
#define INAPPN_ac_reference               (27)
#define INAPPN_release_confirm            (28)
#define INAPPN_qos                        (29)
#define INAPPN_report_cause               (30)

/*
 * Service Parameter Identifier definitions
 */
#define INAPPN_invoke_id                  (16)
#define INAPPN_linked_id                  (17)
#define INAPPN_component                  (18)
#define INAPPN_class                      (19)
#define INAPPN_timeout                    (20)
#define INAPPN_op_code                    (21)
#define INAPPN_linked_op_code             (22)
#define INAPPN_problem_code               (23)
#define INAPPN_parent_ind                 (24)
#define INAPPN_provider_error             (25)
#define INAPPN_priority                   (26)

#define INAPPN_end                        (29)





/*
*******************************************************************
*                                                                 *
*            Dialogue handling definnitions                       *
*                                                                 *
*******************************************************************
*/
/*
 * INAP dialogue primitive types
 * The abort component is not used in INAP - the associated
 * primitives are not available.
 */
#define INAPDT_OPEN_REQ                (1)     /* INAP-OPEN-REQ */
#define INAPDT_OPEN_IND                (1)     /* INAP-OPEN-IND */
#define INAPDT_CLOSE_REQ               (2)     /* INAP-CLOSE-REQ */
#define INAPDT_CLOSE_IND               (2)     /* INAP-CLOSE-IND */
#define INAPDT_DELIMITER_REQ           (3)     /* INAP-DELIMITER-REQ */
#define INAPDT_DELIMITER_IND           (3)     /* INAP-DELIMITER-IND */
#define INAPDT_U_ABORT_REQ             (4)     /* INAP-U-ABORT-REQ */
#define INAPDT_U_ABORT_IND             (4)     /* INAP-U-ABORT-IND */
#define INAPDT_P_ABORT_IND             (5)     /* INAP-P-ABORT-IND */
#define INAPDT_NOTICE_IND              (6)     /* INAP-NOTICE-IND */
#define INAPDT_MAX_DLG_REQ_PRIM        (6)     /* Max user dlg primitive value*/

#define INAPDT_OPEN_RSP                (129)   /* INAP-OPEN-RSP */
#define INAPDT_OPEN_CNF                (129)   /* INAP-OPEN-CNF */
#define INAPDT_MAX_DLG_ACK_PRIM        (129)   /* Max user dlg primitive value*/



/*
*******************************************************************
*                                                                 *
*            Service handling definitions                         *
*                                                                 *
*******************************************************************
*/

/*
 * INAP service primitive types
 */
#define INAPST_SRV_INVOKE_REQ          (1)   /* INAP Srv Invoke Req Primitive*/
#define INAPST_SRV_INVOKE_IND          (1)   /* INAP Srv Invoke Ind Primitive */
#define INAPST_SRV_RESULT_REQ          (2)   /* INAP Srv Result Req Primitive*/
#define INAPST_SRV_RESULT_IND          (2)   /* INAP Srv Result Ind Primitive */
#define INAPST_SRV_ERROR_REQ           (3)   /* INAP Srv Error Req Primitive*/
#define INAPST_SRV_ERROR_IND           (3)   /* INAP Srv Error Ind Primitive */
#define INAPST_SRV_REJECT_REQ          (4)   /* INAP Srv Reject Req Primitive*/
#define INAPST_SRV_REJECT_IND          (4)   /* INAP Srv Reject Ind Primitive */

#define INAPST_MAX_SRV_ACK_PRIM        (4)


/*
 * Values of operation Class.
 */
#define INAP_OP_CLASS_UNDEFINED        (0)
#define INAP_OP_CLASS_1                (1)
#define INAP_OP_CLASS_2                (2)
#define INAP_OP_CLASS_3                (3)
#define INAP_OP_CLASS_4                (4)




/*
 * Release Method values
 */
#define INAPRM_normal_release          (0)     /* Use normal release handshake*/
#define INAPRM_prearranged_end         (1)     /* Predetermined release point */

/*
 * Release Confirm values
 * Used to distinguish between a close indication from a remote INAP user and
 * a locally generated confirmation of a pre-arranged end.
 */
#define INAPRC_rel_normal              (0)   /* Normal end from remote system */
#define INAPRC_rel_confirmed           (1)   /* Confirm of pre-arranged end */


/*
 * Provider abort reason values.
 *
 */
#define INAPPR_prov_malfct             (0)     /* Provider malfunction */
#define INAPPR_dlg_rlsd                (1)     /* Supporting dialogue/transaction released */
#define INAPPR_rsrc_limit              (2)     /* Resource limitation */
#define INAPPR_mnt_act                 (3)     /* Maintenance activity */
#define INAPPR_ver_incomp              (4)     /* version incompatibility */
#define INAPPR_ab_dlg                  (5)     /* Abnormal INAP dialogue */
#define INAPPR_invalid_PDU             (6)     /* Invalid PDU */

/*
 * Source values
 */
#define INAPSRC_inap                   (0)     /* INAP problem */
#define INAPSRC_tcap                   (1)     /* TCAP problem */
#define INAPSRC_network                (2)     /* network service problem */

/*
 * ITU  - User abort reason values.
 */
#define INAPUR_user_specific           (0)     /* user specific reason */
#define INAPUR_version_not_supported   (6)     /* Version Not Supported */
/*
 * ANSI  - User abort reason is user defined.
 */

/*
 * Result values
 */
#define INAPRS_DLG_ACC                 (0)     /* Dialogue accepted */
#define INAPRS_DLG_REF                 (1)     /* Dialogue refused */

/*
 * Refuse reason values
 */
#define INAPRR_no_reason               (0)     /* No reason given */
#define INAPRR_appl_context            (3)     /* Applic context not supported*/
#define INAPRR_ver_incomp              (4)     /* Potential version incompatibility */


/*
 * Problem code parameter values.
 *
 * Used by the API decoding functions to report problems with received
 * components.
 *
 */
#define INAPPC_Mistyped_Parameter      (1)     /* Cpt rejected: unable to decode cpt */
#define INAPPC_Unrecognised_Op         (2)     /* Cpt rejected: op not supported */
#define INAPPC_Unrecognised_Error      (3)     /* Cpt rejected: error not supported */


/*
 * Provider Error parameter values.
 *
 * Sent by the INAP module in the Error primitive to indicate problems that
 * have occured related to an operation invoked by the user.
 * Problems occuring with results or errors issued by the user are indicated
 * using a Notice Indication primitive
 */
#define INAPPE_Duplicate_Invoke_ID          (1)     /* Cpt rejected: invoke id already existed at remote system */
#define INAPPE_Unrecognised_Op              (2)     /* Cpt rejected: op not supported by remote system */
#define INAPPE_Mistyped_Parameter           (3)     /* Cpt rejected: remote system unable to decode cpt */
#define INAPPE_Unrecognised_Linked_ID       (4)     /* Cpt rejected: parent op does not exist at remote system */
#define INAPPE_Linked_Response_Unexpected   (5)     /* Cpt rejected: linked op unexpected by remote system */
#define INAPPE_Unexpected_Linked_Operation  (6)     /* Cpt rejected: linked op unexpected by remote system */
#define INAPPE_Initiating_Release           (7)     /* Cpt rejected: remote system releasing supporting dialogue */
#define INAPPE_Resource_Limitation          (8)     /* Cpt rejected: remote system has insufficient resource */
#define INAPPE_No_Response_From_Peer        (9)     /* Cpt rejected: no response was received from remote system*/
#define INAPPE_Invalid_Response_From_Peer   (10)    /* Cpt rejected: received response was inappropriate */
#define INAPPE_Service_Completion_Failure   (11)    /* Cpt rejected: expected result was not received */


/*
 * Problem Diagnostic Codes
 *
 * Sent by the INAP module to the user in the Notice primitive.
 * Indicates an unexpected event that is not related to any active
 * invocation on this dialogue.
 */
#define INAPPD_abnormal_event_detected_by_peer (0)
#define INAPPD_response_rejected_by_peer       (1)
#define INAPPD_abnormal_event_rx_from_peer     (2)
#define INAPPN_abnormal_ntwk_report_cause      (3)

/*
 * Parent Indication parameter values
 */
#define INAPPI_has_no_linked_ops                (0)
#define INAPPI_has_linked_ops                   (1)



