/*
                Copyright (C) 1994-2004 Intel Corporation.

 Name:          tcp_inc.h

 Description:   Include file for use by modules that interface
                with the TCAP module.

		For details of the use of each message and the
		meaning of the parameters please refer to the
		DataKinetics TCAP Programmer's Manual.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A    01-Jun-94   SFP   - Initial code.
   B    23-Aug-94   SRG   - *** TCAP Alpha Release ****
   B+   14-Sep-94   SFP   - TCP_MSG_S_TCU_ID added.
   C    18-Oct-94   SRG   - *** TCAP Beta Release ***
   D    22-Nov-94   SRG   - TCPSWE_NO_ICDLG replaced by TCPSWE_NO_DLG.
			    TCP_MSG_S_TCI_ID added.
   D+   26-May-95   SFP   - TCP_MSG_DIS_DLG_IND , TCP_MSG_DIS_CPT_IND,
			    TCPF_TR_DISC and TCPPD_xxx values added.
   1    05-Jun-95   SRG   - Raised to issue 1.
   2    30-Aug-95   SFP   - TCPPT_TC_NULL defined.
   3    09-May-97   SFP   - Definitions for ANSI operation added.
   4    08-Apr-98   SFP   - TCPSWE_DBUF_LOW, TCPSWE_NO_DBUF and
                            TCPSWE_DBUF_ABMT defined.
   5    08-May-98   SFP   - TCPF_NO_RXUID added.
   6    11-Dec-98   SFP   - Defined TCPF_DLGGRP, TCPMO_CONFIG_dlg_hunt and
                            TCPMS_CONFIG_dlg_hunt.
                            New messages TCP_MSG_CNF_DGRP and
                            TCP_MSG_TRACE_MASK added.
   7    05-Feb-99   SFP   - addr_format added to TCP_MSG_CONFIG.
                            TCPAF_xxx values added.
   8    28-Jul-00   JET   - New status codes added for TCP_MSG_DIS_DLG_IND and
                            TCP_MSG_DIS_CPT_IND messages.
   9    14-Nov-01   JET   - Increased dialogure resources and resource
                            monitoring
   10   04-Dec-02   JTD   - Change to use of Intel Corp Copyright.
   11   08-Dec-03   YTA   - Add parameter required for format of
                            protocol version.
   12   10-Feb-04   ML    - TCP_MSG_RESET added.
 */

/*******************************************************************
 *                                                                 *
 *                  Message Type Definitions                       *
 *                  (values for m->hdr.type)                       *
 *                                                                 *
 *******************************************************************/

/*
 * Protocol primitive messages:
 */
#define	TCP_MSG_CPT_REQ		(0xc781)  /* Component request from user */
#define	TCP_MSG_CPT_IND		(0x8782)  /* Component indication to user */
#define TCP_MSG_DLG_REQ         (0xc783)  /* Dialogue request from user */
#define	TCP_MSG_DLG_IND  	(0x8784)  /* Dialogue indication to user */

/*
 * Non-primitive messages:
 */
#define TCP_MSG_CONFIG          (0x7780)  /* Configuratiuon */
#define TCP_MSG_DEF_PARAM	(0x7781)  /* Set default parameters */
#define	TCP_MSG_CNF_DGRP  	(0x7785)  /* Dialogue id group configuration */
#define	TCP_MSG_RESET  	        (0x7786)  /* Reset */

#define TCP_MSG_R_RAM           (0x6790)  /* Read RAM address (debug only) */
#define TCP_MSG_R_DLG           (0x6791)  /* Read dialogue address (debug only) */
#define TCP_MSG_R_STATS         (0x6792)  /* Read global statistics */
#define TCP_MSG_R_MOD_STATUS    (0x6796)  /* Read global module status */
#define TCP_MSG_R_DLG_STATUS    (0x6797)  /* Read dialogue status */
#define TCP_MSG_S_TCU_ID        (0x5793)  /* Set local TC-User module id */
#define TCP_MSG_S_TCI_ID        (0x5794)  /* Set TC instance module id */
#define TCP_MSG_TRACE_MASK      (0x5795)  /* Set TCAP trace mask */

#define TCP_MSG_MAINT_IND       (0x07a1)  /* Protocol event indication */
#define TCP_MSG_ERROR_IND       (0x07a2)  /* Software event indication */
#define TCP_MSG_DIS_DLG_IND     (0x07a3)  /* Discarded dialogue prim trace */
#define TCP_MSG_DIS_CPT_IND     (0x07a4)  /* Discarded component prim trace */


/*******************************************************************
 *                                                                 *
 *            Status values used in confirmation messages          *
 *                                                                 *
 *******************************************************************/

#define TCPE_BAD_ID             (1)  /* Inappropriate/invalid id */
#define TCPE_BAD_MSG            (5)  /* Inappropriate/unrecognised message */
#define TCPE_BAD_PARAM          (6)  /* Invalid parameters in message */
#define TCPE_NO_RESOURCES       (7)  /* Insufficient internal resources */


/*******************************************************************
 *                                                                 *
 *               Message Parameter Definitions                     *
 *                                                                 *
 *******************************************************************/

/*
 * Definitions of length, offset and size of
 * TCP_MSG_CONFIG message parameter field:
 */
#define TCPML_CONFIG		    (40)
#define TCPMO_CONFIG_cnf_ver	    (0)	    /* must be set to zero */
#define TCPMS_CONFIG_cnf_ver            (1)
#define TCPMO_CONFIG_module_id      (1)     /* TCAP module id */
#define TCPMS_CONFIG_module_id          (1)
#define TCPMO_CONFIG_user_id        (2)     /* Default TC-user module id */
#define TCPMS_CONFIG_user_id            (1)
#define TCPMO_CONFIG_nsap_id        (3)     /* Network layer module id */
#define TCPMS_CONFIG_nsap_id            (1)
#define TCPMO_CONFIG_mngt_id        (4)     /* Management module id */
#define TCPMS_CONFIG_mngt_id            (1)
#define TCPMO_CONFIG_maint_id       (5)     /* Maintenance module id */
#define TCPMS_CONFIG_maint_id           (1)
#define TCPMO_CONFIG_flags          (6)     /* Run-time configuration flags */
#define TCPMS_CONFIG_flags              (2)
#define TCPMO_CONFIG_nog_dialogues  (8)     /* Max outgoing dialogues */
#define TCPMS_CONFIG_nog_dialogues      (2)
#define TCPMO_CONFIG_nic_dialogues  (10)    /* Max incoming dialogues */
#define TCPMS_CONFIG_nic_dialogues      (2)
#define TCPMO_CONFIG_num_invokes    (12)    /* Max invokes */
#define TCPMS_CONFIG_num_invokes        (2)
#define TCPMO_CONFIG_num_components (14)    /* Max buffered components */
#define TCPMS_CONFIG_num_components     (2)
#define TCPMO_CONFIG_base_ogdlg_id  (16)    /* First outgoing dialogue id */
#define TCPMS_CONFIG_base_ogdlg_id      (2)
#define TCPMO_CONFIG_base_icdlg_id  (18)    /* First incoming dialogue id */
#define TCPMS_CONFIG_base_icdlg_id      (2)
#define TCPMO_CONFIG_tid_ninst      (20)    /* No instance bits in TID */
#define TCPMS_CONFIG_tid_ninst          (1)
#define TCPMO_CONFIG_tid_ndref      (21)    /* No reference bits in TID */
#define TCPMS_CONFIG_tid_ndref          (1)
#define TCPMO_CONFIG_tid_nseq       (22)    /* No sequence bits in TID */
#define TCPMS_CONFIG_tid_nseq           (1)
#define TCPMO_CONFIG_tcap_instance  (23)    /* TCAP instance number */
#define TCPMS_CONFIG_tcap_instance      (1)
#define TCPMO_CONFIG_max_data       (24)    /* Max length for user data */
#define TCPMS_CONFIG_max_data           (2)
#define TCPMO_CONFIG_dlg_hunt       (26)    /* Dialogue hunt algorithm */
#define TCPMS_CONFIG_dlg_hunt           (2)
#define TCPMO_CONFIG_addr_format    (28)    /* Address format */
#define TCPMS_CONFIG_addr_format        (1)
#define TCPMO_CONFIG_reserved       (29)    /* Reserved (must be set to zero) */
#define TCPMS_CONFIG_reserved           (11)

/*
 * Definition for bit values in the flags
 * parameter of TCP_MSG_CONFIG messages:
 */
#define TCPF_TR_DISC	(0x0001)  /* Trace discarded primitives to mngt */
#define TCPF_ANSI       (0x0002)  /* Select ANSI PDU formats if set */
#define TCPF_DIS_TINV	(0x0004)  /* Disable Invoke timer */
#define TCPF_INVRES	(0x0008)  /* Allow an Invoke to convey a result */
#define TCPF_DIS_RXFILT (0x0010)  /* Disable filtering of Rx component type */
#define TCPF_DIS_TXFILT	(0x0020)  /* Disable filtering of Tx component type */
#define TCPF_NO_RXUID 	(0x0040)  /* Disable rx of destination user id by ssn */
#define TCPF_DLGGRP 	(0x0080)  /* Enable dialogue groups */
/*
 * Bits 8 to 11 are used to define the address format.
 */
#define TCPF_NO_ABORT	(0x8000)  /* Disable abort on user dlg error */

/*
 * Definition of address format (addr_format)
 * values in the TCP_MSG_CONFIG message
 */
#define TCPAF_DEFAULT	(0)	/* Default; depends on ITU-T or ANSI config */
#define TCPAF_ITU14	(1)	/* ITU-T format, 14 bit point codes */
#define TCPAF_ITU24	(2)	/* ITU-T format, 24 bit point codes */
#define TCPAF_ANSI14	(3)	/* ANSI format, 14 bit point codes */
#define TCPAF_ANSI24	(4)	/* ANSI format, 24 bit point codes */

/*
 * Definitions of length, offset and size of
 * TCP_MSG_R_STATS message parameter field:
 *
 *
 * NOTE: The parameter area for this message
 *       currently not defined.
 *
 */

/*
 * Definitions of length, offset and size of
 * TCP_MSG_MOD_STATUS message parameter field:
 */
#define TCPML_MOD_STATUS                     (40)
#define TCPMO_MOD_STATUS_version          (0)    /* Status version, set to 0 */
#define TCPMS_MOD_STATUS_version             (1)
#define TCPMO_MOD_STATUS_num_ic_dlg       (1)    /* Number of active IC dlgs */
#define TCPMS_MOD_STATUS_num_ic_dlg          (4)
#define TCPMO_MOD_STATUS_num_og_dlg       (5)    /* Number of active OG dlgs */
#define TCPMS_MOD_STATUS_num_og_dlg          (4)
#define TCPMO_MOD_STATUS_num_act_invokes  (9)    /* Number of active invokes */
#define TCPMS_MOD_STATUS_num_act_invokes     (4)
#define TCPMO_MOD_STATUS_num_alloc_cpt    (13)   /* Allocated components */
#define TCPMS_MOD_STATUS_num_alloc_cpt       (4)
#define TCPMO_MOD_STATUS_num_alloc_dbuf   (17)   /* Allocated dialogue buffers*/
#define TCPMS_MOD_STATUS_num_alloc_dbuf      (4)
#define TCPMO_MOD_STATUS_reserved         (21)  /* reserved */
#define TCPMS_MOD_STATUS_reserved            (19)

/*
 * Definitions of length, offset and size of
 * TCP_MSG_DLG_STATUS message parameter field:
 */
#define TCPML_DLG_STATUS                     (40)
#define TCPMO_DLG_STATUS_version          (0)    /* Status version, set to 0 */
#define TCPMS_DLG_STATUS_version             (1)
#define TCPMO_DLG_STATUS_DHA_state        (1)    /* TCAP DHA state of dialogue*/
#define TCPMS_DLG_STATUS_DHA_state           (1)
#define TCPMO_DLG_STATUS_TSM_state        (2)    /* TCAP TSM state of dialogue*/
#define TCPMS_DLG_STATUS_TSM_state           (1)
#define TCPMO_DLG_STATUS_DLG_state        (3)    /* TCAP state of dialogue*/
#define TCPMS_DLG_STATUS_DLG_state           (1)
#define TCPMO_DLG_STATUS_num_invokes      (4)    /* Allocated invokes */
#define TCPMS_DLG_STATUS_num_invokes         (4)
#define TCPMO_DLG_STATUS_ltid             (8)    /* Local transaction id */
#define TCPMS_DLG_STATUS_ltid                (4)
#define TCPMO_DLG_STATUS_rtid             (12)   /* Remote transaction id */
#define TCPMS_DLG_STATUS_rtid                (4)
#define TCPMO_DLG_STATUS_reserved         (16)   /* reserved */
#define TCPMS_DLG_STATUS_reserved            (24)



/*
 * Definitions of length, offset and size of
 * TCP_MSG_CNF_DGRP message parameter field:
 */
#define TCPML_CNFDLGRP   		     (16)
#define TCPMO_CNFDGRP_base_ogdlg_id       (0)    /* First OG dialogue id */
#define TCPMS_CNFDGRP_base_ogdlg_id          (2)
#define TCPMO_CNFDGRP_nog_dialogues       (2)    /* No of OG dialogue id */
#define TCPMS_CNFDGRP_nog_dialogues          (2)
#define TCPMO_CNFDGRP_base_icdlg_id       (4)    /* First IC dialogue id */
#define TCPMS_CNFDGRP_base_icdlg_id          (2)
#define TCPMO_CNFDGRP_nic_dialogues       (6)    /* No of IC dialogue id */
#define TCPMS_CNFDGRP_nic_dialogues          (2)
#define TCPMO_CNFDGRP_options             (8)    /* Options */
#define TCPMS_CNFDGRP_options                (2)
#define TCPMO_CNFDGRP_user_instance       (10)   /* User instance */
#define TCPMS_CNFDGRP_user_instance          (2)
#define TCPMO_CNFDGRP_reserved            (12)   /* User instance */
#define TCPMS_CNFDGRP_reserved               (4)

/*
 * Definitions of length, offset and size of
 * TCP_MSG_S_TCU_ID message parameter field:
 */
#define TCPML_S_TCU_ID		    (1)
#define TCPMO_S_TCU_ID_user_id      (0)
#define TCPMS_S_TCU_ID_user_id      (1)

/*
 * Definitions of length, offset and size of
 * TCP_MSG_S_TCI_ID message parameter field:
 */
#define TCPML_S_TCI_ID		    (1)
#define TCPMO_S_TCI_ID_mod_id       (0)
#define TCPMS_S_TCI_ID_mod_id       (1)

/*
 * Definitions of length, offset and size of
 * TCP_MSG_TRACE_MASK message parameter field:
 */
#define TCPML_TRACE_MASK  		(12)
#define TCPMO_TRACE_MASK_op_evt_mask    (0)
#define TCPMS_TRACE_MASK_op_evt_mask      	(4)
#define TCPMO_TRACE_MASK_ip_evt_mask    (4)
#define TCPMS_TRACE_MASK_ip_evt_mask      	(4)
#define TCPMO_TRACE_MASK_mng_evt_mask   (8)
#define TCPMS_TRACE_MASK_mng_evt_mask    	(4)

/*
 * Output event trace mask definitions
 */
#define TCPOEM_DLG_IND		(0x00000001)	/* Dialogue Ind to TC-User */
#define TCPOEM_CPT_IND	        (0x00000002)	/* Component Ind to TC-User  */
#define TCPOEM_UDT_REQ	        (0x00000004)	/* UDT Req to SCCP */

/*
 * Input event trace mask definitions
 */
#define TCPIEM_DLG_REQ		(0x00000001)	/* Dialogue req from TC-user */
#define TCPIEM_CPT_REQ	        (0x00000002)	/* Component Req from TC-User */
#define TCPIEM_UDT_IND		(0x00000004)	/* UDT Ind from SCCP */
#define TCPIEM_UDTS_IND		(0x00000008)	/* UDTS Ind from SCCP */


/*
 * Definitions used in the status field
 * of TCP_MSG_MAINT_IND messages:
 */
#define TCPEV_CPT_REQ_DISCARD (1)  /* Component req discarded */
#define TCPEV_DLG_REQ_DISCARD (2)  /* Dialogue primitive req discarded */
#define TCPEV_DATA_LEN_ERR    (3)  /* Data length exceeds max user data len */
#define TCPEV_UNREC_TYPE      (4)  /* Unrecognised TCAP message type received */
#define TCPEV_UNREC_TID       (5)  /* Message for unknown ltid received */
#define TCPEV_SYNTAX_ERR      (6)  /* Syntax error in TR portion of message */
#define TCPEV_BAD_REJ_RXD     (7)  /* Received a bad REJECT component */

/*
 * Definitions used in the status field
 * of TCP_MSG_ERROR_IND messages:
 */
#define TCPSWE_NO_TCPT        (1)  /* TCP_CPT structures exhausted */
#define TCPSWE_NO_TISM        (2)  /* TCP_ISM structures exhausted */
#define TCPSWE_NO_DLG         (3)  /* TCP_DLG structures exhausted */
#define TCPSWE_NO_TCPM        (4)  /* TCP_MSG structures exhausted */
#define TCPSWE_TCPM_LOW	      (5)  /* Running short of TCP_MSG structures */
#define TCPSWE_BAD_MSG        (6)  /* Unsupported inter-task message received */
#define TCPSWE_TX_FMT_ERR     (7)  /* Failed to format message for tx */
#define TCPSWE_ISM_ERR        (8)  /* Internal ISM software error */
#define TCPSWE_BAD_NSAP_FMT   (9)  /* Badly formatted NSAP message received */
#define TCPSWE_DBUF_LOW       (10) /* Running short of TCP_CPTBUF structures */
#define TCPSWE_NO_DBUF        (11) /* TCP_CPTBUF structures exhausted */
#define TCPSWE_DBUF_ABMT      (12) /* Number of TCP_CPTBUF structures recovered */

/*
 * Definitions for use in the status field of
 * TCP_MSG_DIS_DLG_IND and TCP_MSG_DIS_CPT_IND
 * messages:
 */
#define TCPPD_UNREC_ID          (1)  /* ID is not in configured range */
#define TCPPD_INACTIVE_DLG	(2)  /* Specified dialogue is inactive */
#define TCPPD_BAD_FORMAT	(3)  /* Prim formatted incorrectly */
#define TCPPD_UNEX_PRIM		(4)  /* Prim not allowed in current state */
#define TCPPD_INVALID_PRO       (5)  /* Prim not allowed by protocol rules */
#define TCPPD_INTERNAL_FAIL	(6)  /* Internal failure */
#define TCPPD_INVALID_REQ_TYPE  (7)  /* Cpt or Dlg primitive type is invalid */
#define TCPPD_CPT_SYNTAX_ERR    (8)  /* Syntax error (e.g. missing element) */
#define TCPPD_CPT_ENCODE_ERR    (9)  /* Encoding error (e.g. bad length) */
#define TCPPD_CPT_UNREC_TYPE    (10) /* Unrecognised type in this context */
#define TCPPD_CPT_MISSING_PARAM (11) /* Missing manditory parameter */



/*******************************************************************
 *                                                                 *
 *           Definitions for TCAP Primitives and Parameters        *
 *                                                                 *
 *******************************************************************/

/*
 * Primitives for Dialogue Handling for ITU-T
 * operation (Table 1/Q.771) :
 */
#define TCPPT_TC_UNI    	(1)	/* TC-Uni */
#define TCPPT_TC_BEGIN       	(2)     /* TC-Begin */
#define TCPPT_TC_CONTINUE    	(3)     /* TC-Continue */
#define TCPPT_TC_END         	(4)     /* TC-End */
#define TCPPT_TC_U_ABORT     	(5)     /* TC-U-Abort */
#define TCPPT_TC_P_ABORT     	(6)     /* TC-P-Abort */
#define TCPPT_TC_NOTICE		(7)     /* TC-Notice */

/*
 * Primitives for Dialogue Handling for ANSI T1.113
 * operation :
 */
#define TCPPTA_TC_UNI    	(TCPPT_TC_UNI)	   /* Uni(directional) */
#define TCPPTA_TC_QUERY       	(TCPPT_TC_BEGIN)   /* Query */
#define TCPPTA_TC_CONVERSATION 	(TCPPT_TC_CONTINUE)/* Conversation */
#define TCPPTA_TC_RESPONSE      (TCPPT_TC_END)     /* Response */
#define TCPPTA_TC_U_ABORT     	(TCPPT_TC_U_ABORT) /* Abort (user) */
#define TCPPTA_TC_P_ABORT     	(TCPPT_TC_P_ABORT) /* Abort (protocol) */
#define TCPPTA_TC_NOTICE    	(TCPPT_TC_NOTICE)  /* Notice */

/*
 * Component Handling Primitives for ITU-T
 * operation (Table 2/Q.771) :
 */
#define TCPPT_TC_NULL		(0)	/* NULL component */
#define TCPPT_TC_INVOKE		(8)     /* TC-Invoke */
#define TCPPT_TC_RESULT_L	(9)     /* TC-Result-L */
#define TCPPT_TC_RESULT_NL	(10)    /* TC-Result-NL */
#define TCPPT_TC_U_ERROR	(11)    /* TC-U-Error */
#define TCPPT_TC_L_CANCEL	(12)    /* TC-L-Cancel */
#define TCPPT_TC_U_CANCEL	(13)    /* TC-U-Cancel */
#define TCPPT_TC_L_REJECT	(14)    /* TC-L-Reject */
#define TCPPT_TC_R_REJECT	(15)    /* TC-R-Reject */
#define TCPPT_TC_U_REJECT	(16)    /* TC-U-Reject */

/*
 * Component Handling Primitives for ANSI T1.114
 * operation.
 *
 * The following definitions are used for ANSI
 * operation in addition to those defined above
 * for ITU-T :
 */
#define TCPPT_TC_INVOKE_L	(TCPPT_TC_INVOKE)
#define TCPPT_TC_INVOKE_NL	(17)

#define TCPPT_PRIM_MAX   	(17)    /* For internal use */

/*
 * Primitive parameter name definitions
 */
#define TCPPN_COMPONENT         (1)  /* Component portion */
#define TCPPN_LAST_CPT          (2)  /* Last component */
#define TCPPN_CLASS             (3)  /* Class */
#define TCPPN_TIMEOUT           (4)  /* Timeout */
#define TCPPN_INVOKE_ID         (5)  /* Invoke ID */
#define TCPPN_QOS               (6)  /* Quality of service */
#define TCPPN_DEST_ADDR         (7)  /* Destination address */
#define TCPPN_ORIG_ADDR         (8)  /* Originating address */
#define TCPPN_TERMINATION       (9)  /* Termination type */
#define TCPPN_ABORT_REASON      (10) /* Abort reason */
#define TCPPN_REPORT_CAUSE      (11) /* TC-NOTICE report cause */
#define TCPPN_CPT_PRESENT       (12) /* Components present */
#define TCPPN_APPL_CONTEXT      (13) /* Application contect name */
#define TCPPN_USER_INFO         (14) /* User information */
#define TCPPN_P_ABORT           (15) /* P-Abort reason */
#define TCPPN_UABORT_INFO       (16) /* User abort information */
#define TCPPN_SECURITY          (17) /* Security */
#define TCPPN_CONFIDENTIALITY   (18) /* Confidentiality */
#define TCPPN_PERMISSION        (19) /* Permission to release */
#define TCPPN_VERSION           (20) /* Protocol version */
#define TCPPN_PARAM_MAX         (20) /* For internal use */

/*
 * TCPPN_TERMINATION values:
 */
#define TCPEND_BASIC		(0)     /* Basic end */
#define TCPEND_PREARRANGED	(1)     /* Pre-arranged end */

/*
 * TCPPN_ABORT_REASON values:
 */
#define TCPUABT_AC_NOT_SUP	(1)     /* Application context not supported */
#define TCPUABT_USER_DEFINED	(2)     /* User defined */

/*
 * TCPPN_P_ABORT values:
 * (In addition to those defined in Table 12/Q.773).
 */
#define TCPPABT_ABNORMAL_DLG    (126)
#define TCPPABT_NO_COMMON_DLG	(127)

/*
 * TCPPN_CPT_PRESENT values:
 */
#define TCP_NO_CPT		(0)	/* No component(s) present */
#define TCP_CPT_PRESENT		(1)     /* Component(s) present */

/*
 * TCPPN_LAST_CPT values:
 */
#define TCP_MORE_CPTS		(0)	/* More component(s) to follow */
#define TCP_LAST_CPT		(1)     /* This is the last component */

/*
 * TCPPN_PERMISSION values:
 */
#define TCP_NO_PERMISSION	(0)	/* No permission to release given */
#define TCP_PERMISSION		(1)	/* Permission to release given */


