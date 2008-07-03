/*
                Copyright (C) 1995-2004 Intel Corporation

 Name:          tup_inc.h

 Description:   Include file for use by modules that interface
                with the TUP module.

                For details of the use of each message and the
                meaning of the parameters please refer to the
                TUP Programmer's Manual.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------
   A    04-Jul-95   SFP   - Initial code.
   1    04-Aug-95   SFP   - Raised to issue 1.
   2    02-Feb-96   SFP   - Definition of CAL_MSG_OVLD_REQ changed from
                            0x0709 to 0x77c8.
   3    29-Mar-96   SFP   - TUPF_ZCAD defined.
   4    07-Aug-96   SFP   - TUPGOP_NIAI_CLI defined.
   5    25-Mar-97   SFP   - Additional fields (user_id, user_inst, mngt_id,
                            mngt_inst, maint_id, maint_inst) added to the
                            reserved area of the TUP_MSG_CNF_GRP message
                            (from offset 24).
   6    09-May-97   SFP   - ucic_id added to TUP_MSG_CONFIG reserved area.
                            TUPF_DUAL defined. TUP_MSG_REM_GRP (previously
                            unused) renamed TUP_MSG_END_GRP.
   7    17-Sep-97   SFP   - TUPGOP_OGC_UGRQ, TUPF_GSPS, TUP_GCSCC_AVAILABLE
                            and TUP_GCSCC_UNAVAILABLE defined.
   8    09-Dec-97   RBP   - TUPE_BAD_STATE, TUPF_ACR & TUPF_NAI added.
   9    19-Dec-97   RBP   - TUPF_PARAM & support for French SSUTR2
   10   06-Jan-98   RBP   - Corrected backward compatibility of TUP specific
                            parameters TUPF_PARAM. All primitives and parameter
                            ids now referenced to the Common Call
                            interface. (ccp_inc.h)
   11   30-Jan-98    JB   - SSUTR2 full implementation
                            ***   TUP version 1.00   ***
   12   20-Feb-98   RBP   - TUP_MSG_R_STATUS and TUP_MSG_TRACE_MASK messages
                            added. CALPPN_TUP_INFOREQI parameter added for use
                            with backward information request primitives
   13   18-Jun-98    JB   - T100 reduced from 4 to 2 bytes for code size
                            optimisation.
                    RBP   - Added offset and length definitions for trace id in
                            the module configuration message.
   14   16-Nov-98    JB   - TUPF_PC_SIZE, TUPGOP_24PC, TUPGOP_CHN, TUPGOP_ZCAD;
   			    mtp_id, mtp_inst, sio and tim_tab_id parameters
                            added.
                          - CALPPN_SUSRESI parameter token added.
   15   12-Feb-99   RBP   - CALPN_CCT_SZD_IND primitive added.
   16   17-Mar-99   RBP   - Per circuit group options TUPGOP_UBA_IAM and
                            TUPGOP_TST_IRR added.
   17   26-May-99    JB   - CALPN_CCT_SZD_IND changed into CALPN_CCT_SZE_IND.
   			  - CALPN_CCT_SZE_REQ and group option TUPGOP_IEO_CTRL
                            added.
   18   12-Jul-99    JB   - TUPMI_SAM_AFTER_ST and TUPGOP_CCC_CHG added.
   			  - Maintenance event TUPMI_LBLK_rm_by_rsc added.
   19   17-Sep-99    JB   - TUPGOP_CCL group option and CALPN_CCL_IND primitive
                            added .
   18   30-Sep-99    JB   - TUPF_STD_MTYPE module option and updated
                            CAL_MSG_STATUS, CAL_MSG_OVLD_REQ, TUP_MSG_STATUS,
                            and TUP_MSG_OVLD_REQ.
   19	24-Nov-00    JB	  - Added support for _SET_IC_ACTIVE and _SET_OC_ACTIVE.
   20	16-Jun-01   JER   - Added selective tracing.
   21	22-Oct-02   JER   - Added CALPN_MPM_IND.
   22   18-Aug-03   JER   - Commentary changes only.
   23   28-Oct-03    ML   - Support for dynamic circuit group and host
                            configuration.
   24   11-May-04    ML  -  Added TUPF_16CID for not setting MSB in outgoing
                            calls.
*/


/*
 * Message type definitions, (values for m->hdr.type)
 *
 * Types common to other telephony user parts :
 */
#define CAL_MSG_TX_REQ          (0xc700)  /* Transmit request from user */
#define CAL_MSG_RX_IND          (0x8701)  /* Receive indication to user */
#define CAL_MSG_STATUS          (0x070e)  /* Signalling point status ind */
#define CAL_MSG_CGSC_REQ        (0x7703)  /* Cct group supervision request */
#define CAL_MSG_CGSC_IND        (0x0708)  /* Cct group supervision indication */
#define CAL_MSG_CGSC_CONF       (0x0709)  /* Cct group supervision confirmation */

#define CAL_MSG_OVLD_REQ        (0x770d)  /* Local overload request */

/*
 * TUP module specific message types :
 *
 * Protocol primitive messages:
 */
#define TUP_MSG_TX_REQ          (CAL_MSG_TX_REQ)
#define TUP_MSG_RX_IND          (CAL_MSG_RX_IND)

/*
 * Non-primitive messages:
 */
#define TUP_MSG_CONFIG          (0x77c0)  /* Module configuration */
#define TUP_MSG_CNF_GRP         (0x77c1)  /* Circuit group configuration */
#define TUP_MSG_END_GRP         (0x77c2)  /* Remove circuit group */
#define TUP_MSG_CNF_TIM         (0x77c3)  /* Configure all timers */
#define TUP_MSG_SET_TIMER       (0x77cd)  /* Configure single timer */
#define TUP_MSG_CNG_GRP         (0x7d12)  /* Dynamic circuit group configuration */

#define TUP_MSG_TRACE_MASK      (0x57ce)  /* Configure trace masks for input, output and management trace events */
#define TUP_MSG_S_SELTRACE_MASK	(0x5d10)  /* Configure selective trace masks */

#define TUP_MSG_CGSC_REQ        (CAL_MSG_CGSC_REQ)

#define TUP_MSG_R_RAM           (0x67c4)  /* Read module ram base address */
#define TUP_MSG_R_GRP           (0x67c5)  /* Read group structure base address */
#define TUP_MSG_R_CCT           (0x67c6)  /* Read cct structure base address */
#define TUP_MSG_R_CALL          (0x67c7)  /* Read call structure base address */
#define TUP_MSG_R_STATUS	(0x67cf)  /* Read circuit status for all circuits in group */
#define TUP_MSG_R_GRP_STATS     (0x67cc)  /* Read group statistics */
#define TUP_MSG_R_GRPID         (0x6d11)  /* Read circuit group identity */
#define TUP_MSG_R_DATA          (0x6d13)  /* Read cicruit group data */

#define TUP_MSG_CGSC_IND        (CAL_MSG_CGSC_IND)
#define TUP_MSG_CGSC_CONF       (CAL_MSG_CGSC_CONF)

#define TUP_MSG_MAINT_IND       (0x07ca)  /* Maintenance indication */
#define TUP_MSG_ERROR_IND       (0x07cb)  /* Software error indication */

/*
 * These definitions are retained for backward compatibility and should not
 * be used by new applications.
 * New applications should use CAL_MSG_STATUS and CAL_MSG_OVLD_REQ instead.
 */
#define TUP_MSG_OVLD_REQ        (0x77c8)
#define TUP_MSG_STATUS          (0x07c9)

#define TUP_MSG_SDL_SIG         (0x87cc)  /* Internal use only */

/*
 * Definitions used in status field
 * of confirmation messages :
 */
#define TUPE_BAD_ID     (1)  /* Unrecognised message h->id field value */
#define TUPE_BAD_STATE  (2)  /* Message received in incorrect state */
#define TUPE_BAD_MSG    (5)  /* Inappropriate or unrecognised message */
#define TUPE_BAD_PARAM  (6)  /* Invalid parameters contained in message */

/*
 * Definitions of length, offset and size of
 * TUP_MSG_CONFIG message parameter field:
 */
#define TUPML_CONFIG            (40)
#define TUPMO_CONFIG_cnf_ver    (0)
#define TUPMS_CONFIG_cnf_ver            (1)
#define TUPMO_CONFIG_sio        (1)
#define TUPMS_CONFIG_sio                (1)
#define TUPMO_CONFIG_mtp_id     (2)
#define TUPMS_CONFIG_mtp_id             (1)
#define TUPMO_CONFIG_mngt_id    (3)
#define TUPMS_CONFIG_mngt_id            (1)
#define TUPMO_CONFIG_maint_id   (4)
#define TUPMS_CONFIG_maint_id           (1)
#define TUPMO_CONFIG_cgsm_id    (5)
#define TUPMS_CONFIG_cgsm_id            (1)
#define TUPMO_CONFIG_user_id    (6)
#define TUPMS_CONFIG_user_id            (1)
#define TUPMO_CONFIG_pad1       (7)
#define TUPMS_CONFIG_pad1               (1)
#define TUPMO_CONFIG_options    (8)
#define TUPMS_CONFIG_options            (2)
#define TUPMO_CONFIG_max_sif    (10)
#define TUPMS_CONFIG_max_sif            (2)
#define TUPMO_CONFIG_timer_res  (12)
#define TUPMS_CONFIG_timer_res          (2)
#define TUPMO_CONFIG_num_tscan  (14)
#define TUPMS_CONFIG_num_tscan          (2)
#define TUPMO_CONFIG_num_grps   (16)
#define TUPMS_CONFIG_num_grps           (2)
#define TUPMO_CONFIG_num_ccts   (18)
#define TUPMS_CONFIG_num_ccts           (2)
#define TUPMO_CONFIG_instance   (20)
#define TUPMS_CONFIG_instance           (2)
#define TUPMO_CONFIG_ucic_id    (22)
#define TUPMS_CONFIG_ucic_id            (1)
#define TUPMO_CONFIG_trace_id	(23)
#define TUPMS_CONFIG_trace_id		(1)
#define TUPMO_CONFIG_gap_msgs	(24)
#define TUPMS_CONFIG_gap_msgs		(2)
#define TUPMO_CONFIG_gap_tim	(26)
#define TUPMS_CONFIG_gap_tim		(2)
#define TUPMO_CONFIG_reserved   (28)
#define TUPMS_CONFIG_reserved           (12)

/*
 * Definition of options field in
 * TUP_MSG_CONFIG message parameter field:
 */
#define TUPF_TFRM        (0x0001)  /* Use T_FRAME's */
#define TUPF_24PC        (0x0002)  /* Use 24 bit point codes */
#define TUPF_CHN         (0x0004)  /* Enable China extensions */
#define TUPF_ZCAD        (0x0008)  /* Enable zero called address digits */
#define TUPF_DUAL        (0x0010)  /* Enable dual TUP operation */
#define TUPF_GSPS        (0x0020)  /* Send point code status to each group */
#define TUPF_ACR         (0x0040)  /* Application Controlled Release */
#define TUPF_PARAM       (0x0100)  /* Enable Common Call Control Parameter values  */
#define TUPF_PC_SIZE  	 (0x0200)  /* Run time determination of PC size of received msg */
#define TUPF_STD_MTYPE   (0x0400)  /* Enable Common Call Control Message type values  */
#define TUPF_16CID       (0x1000)  /* Use 16 bits CID - do not set MSB for outgoing*/
#define TUPF_NAI         (0x8000)  /* Issue release conf to user */

#define TUPF_RLC         (0x8000)  /* Old mnemonic for TUPF_NAI */

/*
 * Definitions of length, offset and size of
 * TUP_MSG_CNF_GRP message parameter field:
 */
#define TUPML_CNF_GRP                  (40)
#define TUPMO_CNF_GRP_opc         (0)
#define TUPMS_CNF_GRP_opc               (4)
#define TUPMO_CNF_GRP_dpc         (4)
#define TUPMS_CNF_GRP_dpc               (4)
#define TUPMO_CNF_GRP_base_cic    (8)
#define TUPMS_CNF_GRP_base_cic          (2)
#define TUPMO_CNF_GRP_base_cid    (10)
#define TUPMS_CNF_GRP_base_cid          (2)
#define TUPMO_CNF_GRP_cic_mask    (12)
#define TUPMS_CNF_GRP_cic_mask          (4)
#define TUPMO_CNF_GRP_options     (16)
#define TUPMS_CNF_GRP_options           (4)
#define TUPMO_CNF_GRP_instance_id (20)
#define TUPMS_CNF_GRP_instance_id       (2)
#define TUPMO_CNF_GRP_tup_id      (22)
#define TUPMS_CNF_GRP_tup_id            (2)
#define TUPMO_CNF_GRP_user_id     (24)
#define TUPMS_CNF_GRP_user_id           (1)
#define TUPMO_CNF_GRP_user_inst   (25)
#define TUPMS_CNF_GRP_user_inst         (1)
#define TUPMO_CNF_GRP_maint_id    (26)
#define TUPMS_CNF_GRP_maint_id          (1)
#define TUPMO_CNF_GRP_maint_inst  (27)
#define TUPMS_CNF_GRP_maint_inst        (1)
#define TUPMO_CNF_GRP_cgsm_id     (28)
#define TUPMS_CNF_GRP_cgsm_id           (1)
#define TUPMO_CNF_GRP_cgsm_inst   (29)
#define TUPMS_CNF_GRP_cgsm_inst         (1)
#define TUPMO_CNF_GRP_mtp_id  	  (30)
#define TUPMS_CNF_GRP_mtp_id     	(1)
#define TUPMO_CNF_GRP_mtp_inst	  (31)
#define TUPMS_CNF_GRP_mtp_inst		(1)
#define TUPMO_CNF_GRP_sio    	  (32)
#define TUPMS_CNF_GRP_sio          	(1)
#define TUPMO_CNF_GRP_tim_tab_id  (33)
#define TUPMS_CNF_GRP_tim_tab_id        (1)
#define TUPMO_CNF_GRP_reserved    (34)
#define TUPMS_CNF_GRP_reserved          (6)

/*
 * Definition of options field in
 * TUP_MSG_CNF_GRP message parameter field:
 */
#define TUPGOP_RST_REQ        (0x00000001L)   /* Reset required before use*/
#define TUPGOP_OGC_PRI        (0x00000002L)   /* Outgoing call priority */
#define TUPGOP_PRI_CIC        (0x00000004L)   /* Base priotity on OPC, DPC & CIC */
#define TUPGOP_OGC_COT        (0x00000008L)   /* Outgoing COT required */
#define TUPGOP_ACM_CTRL       (0x00000010L)   /* Run T2 while waiting ACM */
#define TUPGOP_ANS_CTRL       (0x00000020L)   /* Run T100 while waiting answer */
#define TUPGOP_ICC_CLI        (0x00000040L)   /* Request incoming CLI */
#define TUPGOP_ICC_ECHO       (0x00000080L)   /* Request incoming 1/2 echo device */
#define TUPGOP_ICC_OCN        (0x00000100L)   /* Request incoming original num */
#define TUPGOP_ICC_ST         (0x00000200L)   /* Generate ST digit after T103 */
#define TUPGOP_NIAI_CLI       (0x00000400L)   /* Do not request CLI if IAI rx'd */
#define TUPGOP_OGC_UGRQ       (0x00000800L)   /* Pass OG CLI requests to user */
#define TUPGOP_SSUTR2         (0x00001000L)   /* Use French SSUTR2 signalling for this group */
#define TUPGOP_SAM_TIM        (0x00002000L)   /* Call fail if no SAM after IAM */
#define TUPGOP_RESP_TIM       (0x00004000L)   /* Call fail if no resp to IAM */
#define TUPGOP_REMOTE         (0x00008000L)   /* Group is managed by another TUP */
#define TUPGOP_CLF_TIM        (0x00010000L)   /* Issue CFL to ntwk & maint rep if no CLF after CBK */
#define TUPGOP_NO_CGB         (0x00020000L)   /* CGB and CGU not used - BLO and UBL used instead */
#define TUPGOP_NO_GRS         (0x00040000L)   /* GRS not used - RSC used instead */
#define TUPGOP_GRS_GRP        (0x00080000L)   /* GRS applies to whole grp (range=0) */
#define TUPGOP_24PC 	      (0x00100000L)   /* 24PC applies to whole grp */
#define TUPGOP_CHN            (0x00200000L)   /* CHN applies to whole grp */
#define TUPGOP_ZCAD	      (0x00400000L)   /* ZCAD applies to whole grp */
#define TUPGOP_UBA_IAM	      (0x00800000L)   /* IAM rx in Wt UBA state triggers cct reset */
#define TUPGOP_TST_IRR	      (0x01000000L)   /* Irrational Msg during Test call setup ignored */
#define TUPGOP_IEO_CTRL	      (0x02000000L)   /* Handling Control of information element order */
#define TUPGOP_CCC_CHG	      (0x04000000L)   /* Handle BCI charge values as ccc format  */
#define TUPGOP_CCL	      (0x08000000L)   /* CCL issue to user if set instead of release  */

/*
 * Definitions of length, offset and size of
 * TUP_MSG_CNG_GRP message parameter field:
 */
#define TUPML_CNG_GRP		 (10)
#define TUPMO_CNG_GRP_cgsm_id	 (0)
#define TUPMS_CNG_GRP_cgsm_id      	(1)
#define TUPMO_CNG_GRP_cgsm_inst  (1)
#define TUPMS_CNG_GRP_cgsm_inst         (1)
#define TUPMO_CNG_GRP_user_id    (2)
#define TUPMS_CNG_GRP_user_id           (1)
#define TUPMO_CNG_GRP_user_inst  (3)
#define TUPMS_CNG_GRP_user_inst         (1)
#define TUPMO_CNG_GRP_maint_id   (4)
#define TUPMS_CNG_GRP_maint_id          (1)
#define TUPMO_CNG_GRP_maint_inst (5)
#define TUPMS_CNG_GRP_maint_inst        (1)
#define TUPMO_CNG_GRP_reserved   (6)
#define TUPMS_CNG_GRP_reserved          (4)

/*
 * Definitions of length, offset and size of
 * TUP_MSG_CNF_TIM message parameter field:
 */
#define TUPML_CNF_TIM                  (120)
#define TUPMO_CNF_TIM_tim_ver   (0)
#define TUPMS_CNF_TIM_tim_ver          (1)
#define TUPMO_CNF_TIM_pad1      (1)
#define TUPMS_CNF_TIM_pad1             (1)
#define TUPMO_CNF_TIM_T1        (2)
#define TUPMS_CNF_TIM_T1               (2)
#define TUPMO_CNF_TIM_T2        (4)
#define TUPMS_CNF_TIM_T2               (2)
#define TUPMO_CNF_TIM_T3        (6)
#define TUPMS_CNF_TIM_T3               (2)
#define TUPMO_CNF_TIM_T4        (8)
#define TUPMS_CNF_TIM_T4               (2)
#define TUPMO_CNF_TIM_T5        (10)
#define TUPMS_CNF_TIM_T5               (2)
#define TUPMO_CNF_TIM_T6        (12)
#define TUPMS_CNF_TIM_T6               (2)
#define TUPMO_CNF_TIM_T7        (14)
#define TUPMS_CNF_TIM_T7               (2)
#define TUPMO_CNF_TIM_T8        (16)
#define TUPMS_CNF_TIM_T8               (2)
#define TUPMO_CNF_TIM_T9        (18)
#define TUPMS_CNF_TIM_T9               (2)
#define TUPMO_CNF_TIM_T10       (20)
#define TUPMS_CNF_TIM_T10              (2)
#define TUPMO_CNF_TIM_T11       (22)
#define TUPMS_CNF_TIM_T11              (2)
#define TUPMO_CNF_TIM_T12       (24)
#define TUPMS_CNF_TIM_T12              (2)
#define TUPMO_CNF_TIM_T13       (26)
#define TUPMS_CNF_TIM_T13              (2)
#define TUPMO_CNF_TIM_T14       (28)
#define TUPMS_CNF_TIM_T14              (2)
#define TUPMO_CNF_TIM_T15       (30)
#define TUPMS_CNF_TIM_T15              (2)
#define TUPMO_CNF_TIM_T16       (32)
#define TUPMS_CNF_TIM_T16              (2)
#define TUPMO_CNF_TIM_T17       (34)
#define TUPMS_CNF_TIM_T17              (2)
#define TUPMO_CNF_TIM_T18       (36)
#define TUPMS_CNF_TIM_T18              (2)
#define TUPMO_CNF_TIM_T19       (38)
#define TUPMS_CNF_TIM_T19              (2)
#define TUPMO_CNF_TIM_T20       (40)
#define TUPMS_CNF_TIM_T20              (2)
#define TUPMO_CNF_TIM_T21       (42)
#define TUPMS_CNF_TIM_T21              (2)
#define TUPMO_CNF_TIM_T22       (44)
#define TUPMS_CNF_TIM_T22              (2)
#define TUPMO_CNF_TIM_T23       (46)
#define TUPMS_CNF_TIM_T23              (2)
#define TUPMO_CNF_TIM_T24       (48)
#define TUPMS_CNF_TIM_T24              (2)
#define TUPMO_CNF_TIM_T25       (50)
#define TUPMS_CNF_TIM_T25              (2)
#define TUPMO_CNF_TIM_T26       (52)
#define TUPMS_CNF_TIM_T26              (2)
#define TUPMO_CNF_TIM_T27       (54)
#define TUPMS_CNF_TIM_T27              (2)
#define TUPMO_CNF_TIM_T28       (56)
#define TUPMS_CNF_TIM_T28              (2)
#define TUPMO_CNF_TIM_T29       (58)
#define TUPMS_CNF_TIM_T29              (2)
#define TUPMO_CNF_TIM_T30       (60)
#define TUPMS_CNF_TIM_T30              (2)
#define TUPMO_CNF_TIM_T31       (62)
#define TUPMS_CNF_TIM_T31              (2)
#define TUPMO_CNF_TIM_T32       (64)
#define TUPMS_CNF_TIM_T32              (2)
#define TUPMO_CNF_TIM_T33       (66)
#define TUPMS_CNF_TIM_T33              (2)
#define TUPMO_CNF_TIM_T34       (68)
#define TUPMS_CNF_TIM_T34              (2)
#define TUPMO_CNF_TIM_T35       (70)
#define TUPMS_CNF_TIM_T35              (2)
#define TUPMO_CNF_TIM_T36       (72)
#define TUPMS_CNF_TIM_T36              (2)
#define TUPMO_CNF_TIM_T37       (74)
#define TUPMS_CNF_TIM_T37              (2)
#define TUPMO_CNF_TIM_T38       (76)
#define TUPMS_CNF_TIM_T38              (2)
#define TUPMO_CNF_TIM_T39       (78)
#define TUPMS_CNF_TIM_T39              (2)
#define TUPMO_CNF_TIM_T40       (80)
#define TUPMS_CNF_TIM_T40              (2)
#define TUPMO_CNF_TIM_T41       (82)
#define TUPMS_CNF_TIM_T41              (2)
#define TUPMO_CNF_TIM_T100      (84)
#define TUPMS_CNF_TIM_T100             (2)
#define TUPMO_CNF_TIM_pad3      (86)
#define TUPMS_CNF_TIM_pad3             (2)
#define TUPMO_CNF_TIM_T101      (88)
#define TUPMS_CNF_TIM_T101             (2)
#define TUPMO_CNF_TIM_T102      (90)
#define TUPMS_CNF_TIM_T102             (2)
#define TUPMO_CNF_TIM_T103      (92)
#define TUPMS_CNF_TIM_T103             (2)
#define TUPMO_CNF_TIM_T104      (94)
#define TUPMS_CNF_TIM_T104             (2)
#define TUPMO_CNF_TIM_T105      (96)
#define TUPMS_CNF_TIM_T105             (2)
#define TUPMO_CNF_TIM_T106      (98)
#define TUPMS_CNF_TIM_T106             (2)
#define TUPMO_CNF_TIM_pad2      (100)
#define TUPMS_CNF_TIM_pad2             (6)
#define TUPMO_CNF_TIM_T110      (106)
#define TUPMS_CNF_TIM_T110             (2)
#define TUPMO_CNF_TIM_T111      (108)
#define TUPMS_CNF_TIM_T111             (2)
#define TUPMO_CNF_TIM_T112      (110)
#define TUPMS_CNF_TIM_T112             (2)
#define TUPMO_CNF_TIM_T113      (112)
#define TUPMS_CNF_TIM_T113             (2)
#define TUPMO_CNF_TIM_T114      (114)
#define TUPMS_CNF_TIM_T114             (2)
#define TUPMO_CNF_TIM_T115      (116)
#define TUPMS_CNF_TIM_T115             (2)
#define TUPMO_CNF_TIM_T116      (118)
#define TUPMS_CNF_TIM_T116             (2)

/*
 * Definitions of length, offset and size of
 * TUP_MSG_SET_TIMER message parameter field:
 */
#define TUPML_SET_TIMER                (4)
#define TUPMO_SET_TIMER_value     (0)
#define TUPMS_SET_TIMER_value          (2)
#define TUPMO_SET_TIMER_reserved  (2)
#define TUPMS_SET_TIMER_reserved       (2)

/*
 * Definitions of length, offset and size of
 * TUP_MSG_CGSC_REQ, TUP_MSG_CGSC_IND and
 * TUP_MSG_CGSC_CONF message parameter field:
 */
#define TUPML_CGSC                (6)
#define TUPMO_CGSC_ptype          (0)
#define TUPMS_CGSC_ptype               (1)
#define TUPMO_CGSC_reserved       (1)
#define TUPMS_CGSC_reserved            (1)
#define TUPMO_CGSC_cic_mask       (2)
#define TUPMS_CGSC_cic_mask            (4)

/*
 * Definitions of length, offset and size of
 * TUP_MSG_CGSC_REQ, TUP_MSG_CGSC_IND and
 * TUP_MSG_CGSC_CONF message parameter field:
 */
#define TUPML_STATUS              (6)
#define TUPMO_STATUS_spc          (0)
#define TUPMS_STATUS_spc               (4)
#define TUPMO_STATUS_cong         (4)
#define TUPMS_STATUS_cong              (2)
/*
 * Definition of the status field for TUP_MSG_STATUS
 */
#define TUP_STATUS_RUU            (1)        /* Remote user unavailable */
#define TUP_STATUS_CONG           (2)        /* Remote user congested */

/*
 * Definitions of length, offset and size of
 * TUP_MSG_R_STATUS message parameter fields:
 */
#define TUPMINL_R_STATUS          (4)
#define TUPMAXL_R_STATUS          (68)
#define TUPMO_R_STATUS_cic_mask	  (0)
#define TUPMS_R_STATUS_cic_mask        (4)
#define TUPMO_R_STATUS_status     (4)
#define TUPMS_R_STATUS_status          (64)

/*
 * Definitions of length, offset and size of
 * ISP_MSG_R_GRPID message parameter fields:
 */
#define TUPML_R_GRPID		(320)
#define TUPMO_R_GRPID_gid_mask		(0)
#define TUPMS_R_GRPID_gid_mask	 		(320)

/*
 * Definitions of length, offset and size of
 * TUP_MSG_GRP_DATA message parameter field:
 */
#define TUPML_R_DATA                  (40)
#define TUPMO_R_DATA_opc         (0)
#define TUPMS_R_DATA_opc               (4)
#define TUPMO_R_DATA_dpc         (4)
#define TUPMS_R_DATA_dpc               (4)
#define TUPMO_R_DATA_base_cic    (8)
#define TUPMS_R_DATA_base_cic          (2)
#define TUPMO_R_DATA_base_cid    (10)
#define TUPMS_R_DATA_base_cid          (2)
#define TUPMO_R_DATA_cic_mask    (12)
#define TUPMS_R_DATA_cic_mask          (4)
#define TUPMO_R_DATA_options     (16)
#define TUPMS_R_DATA_options           (4)
#define TUPMO_R_DATA_instance_id (20)
#define TUPMS_R_DATA_instance_id       (2)
#define TUPMO_R_DATA_tup_id      (22)
#define TUPMS_R_DATA_tup_id            (2)
#define TUPMO_R_DATA_user_id     (24)
#define TUPMS_R_DATA_user_id           (1)
#define TUPMO_R_DATA_user_inst   (25)
#define TUPMS_R_DATA_user_inst         (1)
#define TUPMO_R_DATA_maint_id    (26)
#define TUPMS_R_DATA_maint_id          (1)
#define TUPMO_R_DATA_maint_inst  (27)
#define TUPMS_R_DATA_maint_inst        (1)
#define TUPMO_R_DATA_cgsm_id     (28)
#define TUPMS_R_DATA_cgsm_id           (1)
#define TUPMO_R_DATA_cgsm_inst   (29)
#define TUPMS_R_DATA_cgsm_inst         (1)
#define TUPMO_R_DATA_mtp_id  	  (30)
#define TUPMS_R_DATA_mtp_id     	(1)
#define TUPMO_R_DATA_mtp_inst	  (31)
#define TUPMS_R_DATA_mtp_inst		(1)
#define TUPMO_R_DATA_sio    	  (32)
#define TUPMS_R_DATA_sio          	(1)
#define TUPMO_R_DATA_tim_tab_id  (33)
#define TUPMS_R_DATA_tim_tab_id        (1)
#define TUPMO_R_DATA_reserved    (34)
#define TUPMS_R_DATA_reserved          (6)

/*
 * Definitions of length, offset and size of
 * TUP_MSG_TRACE_MASK message parameter fields:
 */
#define TUPML_TRACE_MSG           (12)
#define TUPMO_TRACE_MSG_op_mask	  (0)
#define TUPMS_TRACE_MSG_op_mask        (4)
#define TUPMO_TRACE_MSG_ip_mask   (4)
#define TUPMS_TRACE_MSG_ip_mask        (4)
#define TUPMO_TRACE_MSG_mng_mask  (8)
#define TUPMS_TRACE_MSG_mng_mask       (4)


/*
 * Definitions for ptype field in
 * TUP_MSG_CGSC_xxx message parameter field:
 */
#define TUP_GCSCC_RESET         (0)
#define TUP_GCSCC_STOP_RESET    (1)
#define TUP_GCSCC_M_BLOCK       (2)
#define TUP_GCSCC_M_UNBLOCK     (3)
#define TUP_GCSCC_M_STOP        (4)
#define TUP_GCSCC_H_BLOCK       (5)
#define TUP_GCSCC_H_UNBLOCK     (6)
#define TUP_GCSCC_H_STOP        (7)
#define TUP_GCSCC_S_BLOCK       (9)
#define TUP_GCSCC_S_UNBLOCK     (10)
#define TUP_GCSCC_S_STOP        (11)
#define TUP_GCSCC_AVAILABLE     (12)
#define TUP_GCSCC_UNAVAILABLE   (13)
#define TUP_GCSCC_SET_IC_ACTIVE (14)     /* Set cct state to incoming active */
#define TUP_GCSCC_SET_OG_ACTIVE (15)     /* Set cct state to outgoing active */

/*
 * Definitions of length of TUP_MSG_S_ERROR_MASK, TUP_MSG_S_MAINT_MASK,
 * and TUP_MSG_S_SELTRACE_MASK message lengths:
 */
#define TUPML_S_ERROR_MASK		(9)
#define TUPML_S_MAINT_MASK		(9)
#define TUPML_S_SELTRACE_MASK		(9)

/*
 * Definitions for use with
 * TUP_MSG_MAINT_IND messages:
 */
#define TUPMI_prim_discarded   (0)    /* User call control prim discarded */
#define TUPMI_IC_congested     (1)    /* ICC failed, no resources */
#define TUPMI_T7_expiry        (2)    /* Failed to receive RLG */
#define TUPMI_T5_expiry        (3)    /* Failed to receive CLF */
#define TUPMI_bad_cic          (4)    /* Rxd a msg for an unknown cic */
#define TUPMI_bad_rxm          (5)    /* Bad received msg type or format */
#define TUPMI_bad_cgm          (6)    /* rxd a bad CG ack or repetition */
#define TUPMI_cgm_discard      (7)    /* rxd GRM discarded, no resources */
#define TUPMI_T114_expiry      (8)    /* No CLF in response to CBK */

#define TUPMI_CRR_reset        (10)    /* Circuit reset by CRS rx'd */
#define TUPMI_CRO_first_fail   (11)    /* CRO first failure */
#define TUPMI_CRO_stopped      (12)    /* CRO stopped not on first */
#define TUPMI_CRI_first_fail   (13)    /* CRI first failure */
#define TUPMI_CRI_passed       (14)    /* CRI passed not on first */

#define TUPMI_BLS_T13_expiry   (20)    /* Failed to receive BLA */
#define TUPMI_BLS_T16_expiry   (21)    /* Failed to receive UBA */
#define TUPMI_BLS_T11_expiry   (22)    /* Local M-blk exceeds time limit */
#define TUPMI_BLR_T11_expiry   (23)    /* Remote M-blk exceeds time limit */

#define TUPMI_CRS_T19_expiry   (30)    /* Failed to receive CRS response */
#define TUPMI_CGRS_T22_expiry  (31)    /* Failed to receive GRS response */

#define TUPMI_MBUS_T27_expiry  (40)    /* Failed to receive MBA */
#define TUPMI_MBUS_T29_expiry  (41)    /* Failed to receive MUA */
#define TUPMI_MBUR_T25_expiry  (42)    /* Rem M-blk exceeds time limit */
#define TUPMI_MBUS_T25_expiry  (43)    /* Local M-blk exceeds time limit */

#define TUPMI_HBUS_blocking    (50)    /* Circuits locally H-blocked */
#define TUPMI_HBUS_T33_expiry  (51)    /* Failed to receive HBA */
#define TUPMI_HBUS_T35_expiry  (52)    /* Failed to receive HUA */
#define TUPMI_HBUR_blocking    (53)    /* Circuits remotely H-blocked */

#define TUPMI_SBUS_blocking    (60)    /* Circuits locally S-blocked */
#define TUPMI_SBUS_T39_expiry  (61)    /* Failed to receive SBA */
#define TUPMI_SBUS_T41_expiry  (62)    /* Failed to receive SUA */
#define TUPMI_SBUR_blocking    (63)    /* Circuits remotely S-blocked */

#define TUPMI_NO_CGBA          (70)    /* Not all requested cct acknowledged */
#define TUPMI_BAD_CGBA         (71)    /* Unexpected blocking ack */
#define TUPMI_NO_CGUA          (72)    /* Not all requested cct acknowledged */
#define TUPMI_BAD_CGUA         (73)    /* Unexpected unblocking ack */
#define TUPMI_LBLK_rm_by_rsc   (74)    /* Local circuit M-blkg removed by circuit reset */

#define TUPMI_SAM_AFTER_ST     (80)    /* SAM ignored due to previously sent ST */

/*
 * Definitions for use with
 * TUP_MSG_ERROR_IND messages:
 */
#define TUPSWE_SDLSIG_LOW     (1)  /* Running short of SDL signals */
#define TUPSWE_NO_SDLSIG      (2)  /* SDL signals exhausted */
#define TUPSWE_TUPM_LOW       (3)  /* Running short of TUPM structures */
#define TUPSWE_NO_TUPM        (4)  /* TUPM structures exhausted */
#define TUPSWE_BAD_MSG        (5)  /* Unsupported message received */
#define TUPSWE_TIM_START_ERR  (6)  /* Attempt made to re-use active timer resource */
#define TUPSWE_NO_TCGC        (7)  /* TCGC structures exhausted */
#define TUPSWE_NO_CSCM        (8)  /* TCSC structures exhausted */

/*
 * Definitions for use with
 * TUP_MSG_SEL_TRACE messages:
 */
#define TUPt_prim_discarded	(0)  /* User call control prim discarded */
#define TUPt_bad_cic            (1)  /* Message received for a bad CIC */
#define TUPt_bad_rxm            (2)  /* Bad received msg type or format */
#define TUPt_bad_cgm            (3)  /* Rxd a bad CG ack or repetition */

/*
 * Primitive type tokens for requests:
 */
#define CALPN_SETUP_REQ         (CCPT_IAM)    /* Setup request */
#define CALPN_FINFO_REQ         (CCPT_SAM)    /* Forwards information request */
#define CALPN_BINFO_REQ         (CCPT_INR)    /* Backwards information request */
#define CALPN_BINFO_RESP        (CCPT_INF)    /* Backwards information response */
#define CALPN_COT_REQ           (CCPT_COT)    /* Continuity request */
#define CALPN_ALERT_REQ         (CCPT_ACM)    /* Alert request */
#define CALPN_FOT_REQ           (CCPT_FOT)    /* Forwards transfer request */
#define CALPN_SETUP_RESP        (CCPT_ANM)    /* Setup response */
#define CALPN_RELEASE_REQ       (CCPT_REL)    /* Release request */
#define CALPN_SUS_REQ           (CCPT_SUS)    /* Suspend request */
#define CALPN_RESUME_REQ        (CCPT_RES)    /* Resume request */
#define CALPN_RELEASE_RESP      (CCPT_RLC)    /* Release response */
#define CALPN_CCT_SZE_REQ       (CCPT_SZE)    /* Circuit seized request */
#define CALPN_MCE_REQ           (CCPT_MCE)    /* End-to-end information (SSUTR2)*/
#define CALPN_MUU_REQ           (CCPT_USR)    /* User-to-user information (SSUTR2)*/
#define CALPN_TAX_REQ           (CCPT_CRG)    /* Charging (SSUTR2)*/
#define CALPN_MPM_REQ           (CCPT_MPM)    /* Collection charging */
#define CALPN_ITX_REQ           (CCPT_MPM)    /* Collection charging */
#define CALPN_TXA_REQ           (CCPT_TXA)    /* Charging acknowledgement (SSUTR2)*/
#define CALPN_CHT_REQ           (CCPT_CHT)    /* Tariff Change (SSUTR2)*/

/*
 * Primitive type tokens for indications:
 */
#define CALPN_SETUP_IND         (CCPT_IAM)    /* Setup indication */
#define CALPN_FINFO_IND         (CCPT_SAM)    /* Forwards info indication */
#define CALPN_BINFO_IND         (CCPT_INR)    /* Backwards info indication */
#define CALPN_FINFO_CONF        (CCPT_INF)    /* Info confirmation */
#define CALPN_COT_IND           (CCPT_COT)    /* Continuity reprot indication */
#define CALPN_ALERT_IND         (CCPT_ACM)    /* Alert indication */
#define CALPN_FOT_IND           (CCPT_FOT)    /* Forward transfer indication */
#define CALPN_SETUP_CONF        (CCPT_ANM)    /* Setup confirmation */
#define CALPN_RELEASE_IND       (CCPT_REL)    /* Release indication */
#define CALPN_SUS_IND           (CCPT_SUS)    /* Suspend indication */
#define CALPN_RESUME_IND        (CCPT_RES)    /* Release indication */
#define CALPN_RELEASE_CONF      (CCPT_RLC)    /* Release confirmation */
#define CALPN_CCT_SZE_IND       (CCPT_SZE)    /* Circuit seized indication */
#define CALPN_MCE_IND           (CCPT_MCE)    /* End-to-end information (SSUTR2)*/
#define CALPN_MUU_IND           (CCPT_USR)    /* User-to-user information (SSUTR2)*/
#define CALPN_TAX_IND           (CCPT_CRG)    /* Charging (SSUTR2)*/
#define CALPN_ITX_IND           (CCPT_MPM)    /* Collection charging (SSUTR2 & China MPM)*/
#define CALPN_MPM_IND           (CCPT_MPM)    /* Collection charging (SSUTR2 & China MPM)*/
#define CALPN_TXA_IND           (CCPT_TXA)    /* Charging acknowledgement (SSUTR2)*/
#define CALPN_CHT_IND           (CCPT_CHT)    /* Tariff Change (SSUTR2)*/
#define CALPN_CCL_IND           (CCPT_CCL)    /* Calling Party Clearing (China)*/

#define TUPPN_MAX               (CCPT_CHT)    /* Largest prim token supported */



/*
 * Parameter name tokens
 */
#define CALPPN_TERMINATOR       (CCPN_eoop)             /* End of parameters token */
#define CALPPN_TMR              (CCPN_tmr)              /* Transmission medium requirement */
#define CALPPN_CALLED_NUM       (CCPN_called_num)       /* Called party number */
#define CALPPN_SUBSQ_NUM        (CCPN_subsq_num)        /* Subsequent number */
#define CALPPN_NOCI             (CCPN_noci)             /* Nature of connection indicators */
#define CALPPN_FCI              (CCPN_fci)              /* Forward call indicators */
#define CALPPN_OFCI             (CCPN_ofci)             /* Optional forward call indicators */
#define CALPPN_CPC              (CCPN_cpc)              /* Calling party's category */
#define CALPPN_CALLING_NUM      (CCPN_calling_num)      /* Calling party number */
#define CALPPN_INFOREQI         (CCPN_inf_reqi)         /* Information request indicators */
#define CALPPN_INFOI            (CCPN_infi)             /* Information indicators */
#define CALPPN_CONTI            (CCPN_conti)            /* Continuity indicators */
#define CALPPN_BCI              (CCPN_bci)              /* Backward call indicators */
#define CALPPN_CAUSEI           (CCPN_causei)           /* Cause indicators */
#define CALPPN_REDIR_INFO       (CCPN_redirection_inf)  /* Redirection info */
#define CALPPN_CUGI             (CCPN_cugic)            /* CUG indicators */
#define CALPPN_SPC              (CCPN_spc)              /* Signalling point code */
#define CALPPN_UUINF            (CCPN_uuinf)            /* User to user info */
#define CALPPN_EVENT_INF        (CCPN_event_inf)        /* Event information */
#define CALPPN_ORIG_NUM         (CCPN_orig_called_num)  /* Original called number */
#define CALPPN_GENERIC_NUM      (CCPN_gen_number)       /* Generic number - SSUTR2 Additional Identity*/
#define CALPPN_TRF_IND          (CCPN_trf_ind)          /* Tariff indicator */
#define CALPPN_TRF_FAC          (CCPN_trf_fac)          /* Tariff factor */
#define CALPPN_TIM_IND          (CCPN_tim_ind)          /* Time indicator */
#define CALPPN_PKT_CHG          (CCPN_pkt_chg)          /* Packet charging */
#define CALPPN_ADD_CSE          (CCPN_add_cse)          /* Additional cause information */
#define CALPPN_MNF              (CCPN_mnf)              /* Message number */
#define CALPPN_COL_CHG          (CCPN_num_pulses)       /* Collection charging */
#define CALPPN_SUSRESI          (CCPN_susresi)          /* Suspend/Resume Indicators */
#define CALPPN_USINF            (CCPN_usinf)          	/* User Service Information */
#define CALPPN_AT               (CCPN_at)          	/* Access Transport */


/*
 * The following parameters are specific to the TUP module:
 * These values are part of the Common Call interface and should be
 * used in all new applications.
 * They are used if the TUPF_PARAM option in the module configuration message
 * is set.
 */
#define CALPPN_CCPN_ACINF        (CCPN_acinf)            /* Additional calling party info */
#define CALPPN_CCPN_ARTINF       (CCPN_artinf)           /* Additional routing info */
#define CALPPN_CCPN_CHRGINF      (CCPN_charginf)         /* Charging information */
#define CALPPN_CCPN_IAI_NAT      (CCPN_iai_nat)          /* IAI national use octet */
#define CALPPN_CCPN_ITTI         (CCPN_itti)             /* IC trunk and transit identity */
#define CALPPN_CCPN_TUP_INFOI    (CCPN_tup_infoi)        /* TUP information indicators */
#define CALPPN_TUP_INFOREQI      (CCPN_tup_inforeqi)     /* TUP information request indicators */

#define CALPPN_SSUTR2_RDINFO     (CCPN_ssutr2_rdinfo)    /* SSUTR2 Further Redirection info */
#define CALPPN_SSUTR2_ACDINF     (CCPN_ssutr2_acdinf)    /* SSUTR2 Additional Called Party info */

#define TUPPPN_MAX               (CCPN_num_pulses)       /* Maximum parameter token value */


/*
 * The following constants are used in the first octet of the CALPPN_TRF_IND,
 * CALPPN_TRF_FAC and CALPPN_PKT_CHG prameters to indicate whether the second
 * octet refers to the current tariff or the next tariff.
 */
#define CALPPN_CURRENT_CHARGE     (0)
#define CALPPN_NEXT_CHARGE        (1)


/*
 * The following parameters are specific to the TUP module:
 * These values are used for backward compatibility with exisiting applications.
 * They are used if the TUPF_PARAM option in the module configuration message
 * is clear.
 */
#define CALPPN_ACINF            (192)    /* Additional calling party info */
#define CALPPN_ARTINF           (193)    /* Additional routing info */
#define CALPPN_CHRGINF          (194)    /* Charging information */
#define CALPPN_IAI_NAT          (195)    /* IAI national use octet */
#define CALPPN_ITTI             (196)    /* IC trunk and transit identity */
#define CALPPN_TUP_INFOI        (197)    /* TUP information indicators */


/*
 * Circuit group supervision control management primitive types :
 */
#define CAL_CGSCPT_RESET          (0)
#define CAL_CGSCPT_STOP_RESET     (1)
#define CAL_CGSCPT_M_BLOCK        (2)
#define CAL_CGSCPT_M_UNBLOCK      (3)
#define CAL_CGSCPT_M_STOP         (4)
#define CAL_CGSCPT_H_BLOCK        (5)
#define CAL_CGSCPT_H_UNBLOCK      (6)
#define CAL_CGSCPT_H_STOP         (7)
#define CAL_CGSCPT_QUERY          (8)
#define CAL_CGSCPT_S_BLOCK        (9)
#define CAL_CGSCPT_S_UNBLOCK      (10)
#define CAL_CGSCPT_S_STOP         (11)
/*
 * Values 12-13 are used for destination point code availability.
 */
#define CAL_CGSCPT_SET_IC_ACTIVE (14)     /* Set cct state to incoming active */
#define CAL_CGSCPT_SET_OG_ACTIVE (15)     /* Set cct state to outgoing active */

