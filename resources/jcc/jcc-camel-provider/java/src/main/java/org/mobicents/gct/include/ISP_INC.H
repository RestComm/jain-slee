/*
                Copyright (C) 1993-2004 Intel Corporation.

 Name:          isp_inc.h

 Description:   Include file for use by modules that interface
		with the ISUP module.

		For details of the use of each message and the
		meaning of the parameters please refer to the
		ISUP Programmer's Manual.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------
   A    31-Aug-93   SRG   - Initial code.
   B    23-Sep-93   SRG   - *** ISUP Alpha Release ****
   1    09-Feb-94   SRG   - *** ISUP Version 1.00 Release ****
   2    18-Aug-95   SFP   - CAL_MSG_CGSS_IND defined.
			    added CCm_CQR_T28_Expired.
			    ISPGOP_xxx values defined.
   3    12-Dec-95   SFP   - ISPGOP_COM defined.
   4    30-Jan-96   SFP   - module id and instance fields added to
			    ISP_MSG_CNF_GRP. ISPML_CNF_GRP increased
			    from 14 to 26. ISPF_xxx values moved from
			    isp_def.h.
   4+   09-Apr-96   SFP   - ISPF_ACR, ISPF_CCR, ISPF_NAI, ISPF_UCIC and
			    ISPGOP_T35 defined.
			    ISP_MSG_CNF_TIM definitions added.
			    CCm_SPRC_UCIC_rxd added.
   5    11-Apr-96   SRG   - *** ISUP Version 2.00 Release ***
   5+   23-May-96   SFP   - Added ISPF_24PC, ISPXGOP_ANSI, ISPXGOP_BLK_767,
			    ISPXGOP_UMSG_CFN, ISPXGOP_PRI_BA and
			    ISPXGOP_UBK_MH.
			    ISP_MSG_CNF_IGRP : point code field sizes
			    increased from 2 to 4 octets, ext_options added.
			    CCm_SPRC_CFN_rxd and CCm_SPRC_CFN_rxd added.
   6	06-Jul-96   SFP	  - ISPF_ANSI defined. ISPMx_CNF_TIM_Tnn definitions
                            changed to ISPMx_CNF_TIM_tnn to be consistent
			    with other parameters.
   7    10-Sep-96   JER   - Added ISPF_RPT_UERR, ISPXGOP_T34, ISPXGOP_TRAN_PAM,
			    ISPXGOP_COMPAT, ISPXGOP_ITU92, ISPe_PAR_FMT_ERR
			    ISPe_PAR_UNEXPECTED and ISPe_MSG_UNRECOG.
   8    18-Feb-97   JER   - Added new messages: ISP_MSG_R_STATUS,
			    ISP_MSG_END_GRP, ISP_MSG_S_MAINT_MASK,
			    and ISP_MSG_S_ERROR_MASK.
			    Added new run-time option ISPXGOP_CAUSE_767.
   9	23-Sep-97   JER   - Added ISPF_SYSTRACE and ISPF_ERRTRACE.
   9    23-Sep-97   JER   - *** ISUP Version 2.06 Release ***
   10   20-Nov-97   JER   - New option ISPXGOP_DEL_ST added.
   10   20-Nov-97   JER   - *** ISUP Version 2.07 Release ***
   11   25-Mar-98    JB   - Trace masks definition added in DKL standard.
   			    Removed ISPF_SYSTRACE and ISPF_ERRTRACE.
                            ISP_MSG_MAINT_IND definition updated.
   12   15-May-98    JB   - Added new fields to Configure Group message.
                          - Added the following new group options: ISPXGOP_24PC,
                            ISPX1GOP_ADD_ST, and ISPXGOP_UCIC.
   			  - Added the following new module option: ISPF_24PC.
   13	08-Jun-98    JB   - Added new maintenance indications: CCm_CGB_Bad_cct,
			    CCm_CGU_Bad_cct, and CCm_CQM_Bad_cct.
   14   16-Jun-98   SRG   - Comment closing reinstated!
   15   27-Aug-98   JB    - Added ISP_GCSCC_SET_OC_ACTIVE and
                            ISP_GCSCC_SET_IC_ACTIVE.
   16   21-Jan-99   JER   - Increased length of Configure timers message.
   17   29-Mar-99   JB    - Added ISPe_MSG_FMT_ERR and ISPe_WRONG_CID and
   			    ISPe_PRIM_DISCARDED.
                          - Added ISPF_GSPS, ISP_GCSCC_AVAILABLE, ISP_GCSCC_UNAVAILABLE.
                          - Added ISPGOP_GER to enable support of German ISUP
                            on per group basis.
   18   29-Jul-99   JB    - TIM_t104 and ISPGOP_TCCR_CTRL option added to
   			    implement Tccr (ANSI ISUP).
   19   26-Aug-99   JB    - ISPX1GOP_UK option added to support UK ISUP.
   20   23-Dec-99   JB    - Support for new circuit group "variant" field added.
   			  - Definitions for UK ISUP added .
                          - Removed ISPX1GOP_UK group option.
   21   21-Mar-00   JB    - ISPGVAR_TTC, ISPX1GOP_16PC added to support
   			    TTC Japanese ISUP. ISPX1GOP_T33EXP_NOREL added.
   22   13-Apr-00   JB    - ISPGVAR_ANSI_RLT and ISPX1GOP_USR_SVC_PRV added.
   23   15-May-00   ML    - Added group option ISPX1GOP_T39.
   24   07-Jun-00   JB    - Support for custom parameters added.
   25	28-Jun-00   JB    - Support for ITU_RLT added.
   26	07-Sep-00   JB    - Support for erro_ind msg extended.
                     	  - Support for ISPGVAR_ANSI95 and ISPGVAR_ITAL added.
                    ML    - Added ISPe_UMSG_UNEXPECTED.
   27   21-Jun-01   ML    - Added new group options ISPX1GOP_T38,
                            ISPX1GOP_SEND_UPT and ISPX1GOP_TX_CRG.
                          - Support for hop counter procedure and
                            selective tracing.
                          - Added CCm_CC_Zero_hops, CCm_HGBS_Unblocking
                            and CCm_MGBS_Unblocking.
   28   11-Sep-01   ML    - Support for SSURF added.
   29   16-Sep-02   ML    - Added macro definition for ANSI operation.
   30	09-Dec-02   JER   - Updated copyright notice.
   31   26-Mar-03   ML    - Support for China ISUP.
   32   28-Oct-03   ML    - Support for dynamic circuit group and host
                            configuration.
   33   05-Feb-04   ML    - Added ISPt_WRONG_CID for selective tracing.
   34   18-Feb-04   JER   - Added configuration of parameters for message
                            gapping.
   35   11-May-04   XS    - Added ISPF_16CID for not setting MSB in outgoing
                            calls.
   36   17-Aug-04   HJM  - Support for UISM functionality.
*/

/*
 * Message type definitions, (values for m->hdr.type)
 *
 * Types common to other telephony user parts :
 */
#ifndef CAL_MSG_TX_REQ
#define CAL_MSG_TX_REQ	      (0xc700)  /* Transmit request from user */
#define CAL_MSG_RX_IND        (0x8701)  /* Receive indication to user */
#define CAL_MSG_CGSC_REQ      (0x7703)  /* Cct group supervision request */
#define CAL_MSG_CGSC_IND      (0x0708)  /* Cct group supervision indication */
#define CAL_MSG_CGSC_CONF     (0x0709)  /* Cct group supervision confirmation */
#define CAL_MSG_CGSS_IND      (0x070c)  /* Cct group status indication */
#define CAL_MSG_OVLD_REQ      (0x770d)  /* Local overload request */
#define CAL_MSG_STATUS	      (0x070e)  /* Signalling point status ind */
#define CAL_MSG_HEARTBEAT     (0x7718)  /* User Instance Heartbeat */
#endif

/*
 * Protocol primitive messages:
 */
#define	ISP_MSG_TX_REQ 	      (CAL_MSG_TX_REQ)
#define	ISP_MSG_RX_IND 	      (CAL_MSG_RX_IND)  /* Receive indication to user */

/*
 * Non-primitive messages:
 */
#define ISP_MSG_CONFIG		(0x7700)
#define ISP_MSG_CNF_GRP         (0x7701)
#define ISP_MSG_CNF_TIM		(0x7702)
#define ISP_MSG_CNG_GRP         (0x7715)

#define ISP_MSG_END_GRP		(0x770f)

#define ISUP_MSG_SET_CUSTVAR	(0x5712) /* Custom variant initialisation msg */
#define ISUP_MSG_CUSTPARAM  	(0x5713) /* Custom parameter msg */

#define ISP_MSG_CGSC_REQ 	(CAL_MSG_CGSC_REQ)
#define ISP_MSG_CGSC_IND	(CAL_MSG_CGSC_IND)
#define ISP_MSG_CGSC_CONF	(CAL_MSG_CGSC_CONF)
#define ISP_MSG_CGSS_IND	(CAL_MSG_CGSS_IND)

#define ISP_MSG_R_RAM           (0x6704)
#define ISP_MSG_R_GRP           (0x6705)

#define ISP_MSG_R_CCT           (0x6706)
#define ISP_MSG_R_CALL          (0x6707)
#define ISP_MSG_R_STATUS	(0x6710)
#define ISP_MSG_R_GRPID         (0x6715)
#define ISP_MSG_R_DATA          (0x6717)

#define ISP_MSG_S_ERROR_MASK	(0x5710)
#define ISP_MSG_S_TRACE_MASK	(0x5711)
#define ISP_MSG_S_MAINT_MASK    (0x570f)
#define ISP_MSG_S_SELTRACE_MASK (0x5714)

#define ISP_MSG_MAINT_IND	(0x070a)
#define ISP_MSG_ERROR_IND	(0x070b)

#define ISP_MSG_OVLD_REQ        (CAL_MSG_OVLD_REQ)
#define ISP_MSG_STATUS          (CAL_MSG_STATUS)

#define CAL_MSG_HEARTBEAT_RSP	(CAL_MSG_HEARTBEAT & (~REQUEST))

/*******************************************************************
 *                                                                 *
 *               Message Parameter Definitions                     *
 *                                                                 *
 *******************************************************************/

/*
 * Definitions of length, offset and size of
 * ISP_MSG_CONFIG message parameter field:
 */
#define ISPML_CONFIG		(40)
#define ISPMO_CONFIG_options	(0)
#define ISPMS_CONFIG_options      	(2)
#define ISPMO_CONFIG_module_id  (2)
#define ISPMS_CONFIG_module_id          (1)
#define ISPMO_CONFIG_mngt_id    (3)
#define ISPMS_CONFIG_mngt_id            (1)
#define ISPMO_CONFIG_user_id    (4)
#define ISPMS_CONFIG_user_id            (1)
#define ISPMO_CONFIG_mtp_id     (5)
#define ISPMS_CONFIG_mtp_id             (1)
#define ISPMO_CONFIG_sccp_id    (6)
#define ISPMS_CONFIG_sccp_id            (1)
#define ISPMO_CONFIG_maint_id   (7)
#define ISPMS_CONFIG_maint_id           (1)
#define ISPMO_CONFIG_timer_res  (8)
#define ISPMS_CONFIG_timer_res          (2)
#define ISPMO_CONFIG_num_tscan  (10)
#define ISPMS_CONFIG_num_tscan          (2)
#define ISPMO_CONFIG_num_grps   (12)
#define ISPMS_CONFIG_num_grps           (2)
#define ISPMO_CONFIG_num_ccts   (14)
#define ISPMS_CONFIG_num_ccts           (2)
#define ISPMO_CONFIG_max_sif    (16)
#define ISPMS_CONFIG_max_sif            (2)
#define ISPMO_CONFIG_sio        (18)
#define ISPMS_CONFIG_sio                (1)
#define ISPMO_CONFIG_trace_id   (19)
#define ISPMS_CONFIG_trace_id           (1)
#define ISPMO_CONFIG_gap_msgs   (20)
#define ISPMS_CONFIG_gap_msgs           (2)
#define ISPMO_CONFIG_gap_tim    (22)
#define ISPMS_CONFIG_gap_tim            (2)
#define ISPMO_CONFIG_reserved   (24)
#define ISPMS_CONFIG_reserved           (16)

/*
 * Definitions for options fields in
 * ISP_MSG_CONFIG message:
 */
#define ISPF_TFRM     (0x0001)  /* Use T_FRAMEs in messages to MTP */
#define ISPF_DUAL     (0x0002)  /* Operate in dual ISUP mode */
#define ISPF_ACR      (0x0004)  /* Operate in appl controlled release mode */
#define ISPF_CCR      (0x0008)  /* Issue release on call collision */
#define ISPF_NAI      (0x0010)  /* Inform application when circuit is idle */
#define ISPF_UCIC     (0x0020)  /* Recognise and generate UCIC */
#define ISPF_GSPS     (0x0040)  /* Send point code status to each group */
#define ISPF_24PC     (0x0100)  /* Use 24 bit point codes */
#define ISPF_ANSI     (0x0200)  /* Select ANSI operation */
#define ISPF_RPT_UERR (0x0400)  /* Report errors in MSGs received from User*/
#define ISPF_PC_SIZE  (0x0800)  /* run time determination of PC size of received msg */
#define ISPF_16CID    (0x1000)  /* Use 16 bits CID - do not set MSB for outgoing*/

/*
 * Definitions of length, offset and size of
 * ISP_MSG_S_TRACE_MASK message parameter field:
 */
#define ISPML_TRACE_MASK  			(16)
#define ISPMO_TRACE_MASK_op_evt_mask    	(0)
#define ISPMS_TRACE_MASK_op_evt_mask      		(4)
#define ISPMO_TRACE_MASK_ip_evt_mask    	(4)
#define ISPMS_TRACE_MASK_ip_evt_mask      		(4)
#define ISPMO_TRACE_MASK_non_prim_mask  	(8)
#define ISPMS_TRACE_MASK_non_prim_mask    		(4)
#define ISPMO_TRACE_MASK_pad1 			(12)
#define ISPMS_TRACE_MASK_pad1		  		(4)

/*
 * Output event trace mask definitions
 */
#define ISPOEM_ISPRX_IND	(0x00000001)	/* ISP_MSG_RX_IND */
#define ISPOEM_MTPTRF_REQ	(0x00000002)	/* API_MSG_TX_REQ and SS7_MSG_TX_REQ  */
#define ISPOEM_CGSC_CONF	(0x00000004)	/* ISP_MSG_CGSC_CONF */
#define ISPOEM_CGSC_IND		(0x00000008)	/* ISP_MSG_CGSC_IND */
#define ISPOEM_CGSS_IND		(0x00000010)	/* ISP_MSG_CGSS_IND */
#define ISPOEM_STATUS		(0x00000020)	/* ISP_MSG_STATUS */

/*
 * Input event trace mask definitions
 */
#define ISPIEM_ISPTX_REQ	(0x00000001)	/* ISP_MSG_TX_REQ */
#define ISPIEM_MTPTRF_IND	(0x00000002)	/* API_MSG_RX_IND and SS7_MSG_RX_IND */
#define ISPIEM_MTP_PAUSE	(0x00000004)	/* ISP_MSG_MTP_PAUSE */
#define ISPIEM_MTP_RESUME	(0x00000008)	/* ISP_MSG_MTP_RESUME*/
#define ISPIEM_MTP_STATUS	(0x00000010)	/* MTP_MSG_STATUS */
#define ISPIEM_CGSC_REQ		(0x00000020)	/* ISP_MSG_CGSC_REQ */
#define ISPIEM_OVLD_REQ		(0x00000040)	/* ISP_MSG_OVLD_REQ */
#define ISPIEM_MTP_RESTART_IND	(0x00000080)	/* MTP_RESTART_IND; reserved */

/*
 * Non-primitive trace mask definitions
 */
#define ISPNPM_CNF_ISP		(0x00000001)	/* ISP_MSG_CONFIG */
#define ISPNPM_CNF_TIM		(0x00000002)	/* ISP_MSG_CNF_TIM */
#define ISPNPM_CNF_GRP		(0x00000004)	/* ISP_MSG_CNF_GRP */
#define ISPNPM_END_GRP		(0x00000008)	/* ISP_MSG_END_GRP */
#define ISPNPM_R_STATUS		(0x00000010)	/* ISP_MSG_R_STATUS */
#define ISPNPM_SMT_MSK		(0x00000020)    /* ISP_MSG_S_MAINT_MASK */
#define ISPNPM_SSW_MSK		(0x00000040)	/* ISP_MSG_S_ERROR_MASK */
#define ISPNPM_MT_EVT           (0x00000080)    /* ISP_MSG_MAINT_IND */
#define ISPNPM_SW_EVT           (0x00000100)    /* ISP_MSG_ERROR_IND */
#define ISPNPM_MGT_EV_IND       (0x00000200)    /* MGT_MSG_EVENT_IND */
#define ISPNPM_SET_CUSTVAR      (0x00000400)    /* ISUP_MSG_SET_CUSTVAR */
#define ISPNPM_MSG_CUSTPARAM    (0x00000800)    /* ISUP_MSG_CUSTPARAM */
#define ISPNPM_CNG_GRP          (0x00001000)    /* ISUP_MSG_CNG_GRP */
#define ISPNPM_R_DATA		(0x00002000)	/* ISP_MSG_R_DATA */
#define ISPNPM_R_GRPID		(0x00004000)	/* ISP_MSG_R_ID */  

/*
 * Definitions of length, offset and size of
 * ISP_MSG_SET_CUSTVAR message parameter field:
 */
#define ISPML_SET_CUSTVAR          (1)
#define ISPMO_SET_CUSTVAR_base_var (0)
#define ISPMS_SET_CUSTVAR_base_var  	(1)

#define ISP_MAX_CUST_VAR	(2) /* Maximum number of customisable variants */

/*
 * Definitions of length, offset and size of
 * ISP_MSG_CUSTPARAM message parameter field:
 */
#define ISPML_MSG_CUSTPARAM		        (32)
#define ISPMO_MSG_CUSTPARAM_version 	  	(0)
#define ISPMS_MSG_CUSTPARAM_version		  	(1)
#define ISPMO_MSG_CUSTPARAM_oper	     	(1)
#define ISPMS_MSG_CUSTPARAM_oper 			(1)
#define ISPMO_MSG_CUSTPARAM_mtype	     	(2)
#define ISPMS_MSG_CUSTPARAM_mtype 			(1)
#define ISPMO_MSG_CUSTPARAM_pad1	     	(3)
#define ISPMS_MSG_CUSTPARAM_pad1 			(5)
#define ISPMO_MSG_CUSTPARAM_ptype	     	(8)
#define ISPMS_MSG_CUSTPARAM_ptype 			(24)


/*
 * Definitions of length, offset and size of
 * ISP_MSG_CNF_GRP message parameter field:
 */
#define ISPML_CNF_GRP_EXT         	(64)
#define ISPML_CNF_GRP		  	(30)
#define ISPMO_CNF_GRP_opc	  (0)
#define ISPMS_CNF_GRP_opc 		(4)
#define ISPMO_CNF_GRP_dpc	  (4)
#define ISPMS_CNF_GRP_dpc 		(4)
#define ISPMO_CNF_GRP_base_cic	  (8)
#define ISPMS_CNF_GRP_base_cic 	 	(2)
#define ISPMO_CNF_GRP_base_cid	  (10)
#define ISPMS_CNF_GRP_base_cid 		(2)
#define ISPMO_CNF_GRP_cic_mask	  (12)
#define ISPMS_CNF_GRP_cic_mask 		(4)
#define ISPMO_CNF_GRP_options	  (16)
#define ISPMS_CNF_GRP_options 		(2)
#define ISPMO_CNF_GRP_ext_options (18)
#define ISPMS_CNF_GRP_ext_options	(2)
#define ISPMO_CNF_GRP_mngt_id	  (20)
#define ISPMS_CNF_GRP_mngt_id 		(1)
#define ISPMO_CNF_GRP_mngt_inst	  (21)
#define ISPMS_CNF_GRP_mngt_inst		(1)
#define ISPMO_CNF_GRP_user_id  	  (22)
#define ISPMS_CNF_GRP_user_id   	(1)
#define ISPMO_CNF_GRP_user_inst	  (23)
#define ISPMS_CNF_GRP_user_inst 	(1)
#define ISPMO_CNF_GRP_mtp_id  	  (24)
#define ISPMS_CNF_GRP_mtp_id     	(1)
#define ISPMO_CNF_GRP_mtp_inst	  (25)
#define ISPMS_CNF_GRP_mtp_inst		(1)
#define ISPMO_CNF_GRP_sccp_id  	  (26)
#define ISPMS_CNF_GRP_sccp_id   	(1)
#define ISPMO_CNF_GRP_sccp_inst	  (27)
#define ISPMS_CNF_GRP_sccp_inst 	(1)
#define ISPMO_CNF_GRP_maint_id 	  (28)
#define ISPMS_CNF_GRP_maint_id  	(1)
#define ISPMO_CNF_GRP_maint_inst  (29)
#define ISPMS_CNF_GRP_maint_inst 	(1)
#define ISPMO_CNF_GRP_sio	  (30)
#define ISPMS_CNF_GRP_sio		(1)

#define ISPMO_CNF_GRP_tim_tab_id  (31)
#define ISPMS_CNF_GRP_tim_tab_id  	(1)
#define ISPMO_CNF_GRP_ext_1_options (32)
#define ISPMS_CNF_GRP_ext_1_options     (4)
#define ISPMO_CNF_GRP_variant	  (36)
#define ISPMS_CNF_GRP_variant		(1)
#define ISPMO_CNF_GRP_ihop_count  (37)
#define ISPMS_CNF_GRP_ihop_count	(1)
#define ISPMO_CNF_GRP_pad1	  (38)
#define ISPMS_CNF_GRP_pad1		(26)

/*
 * Definitions for options fields in ISP_MSG_CNF_GRP:
 */
#define ISPGOP_OGC_PRI   	(0x0001) /* Outgoing call priority */
#define ISPGOP_PRI_CIC          (0x0002) /* Base priority on OPC, DPC & CIC */
#define ISPGOP_ACM_CTRL  	(0x0004) /* ACM Controlling exchange */
#define ISPGOP_ANM_CTRL  	(0x0008) /* ANM Controlling exchange */
#define ISPGOP_SUS_CTRL  	(0x0010) /* SUS Controlling exchange */
#define ISPGOP_RST_REQD  	(0x0020) /* Initial state is 'Unknown' */
#define ISPGOP_REQ_CLI   	(0x0040) /* Request cli and cpc if not supplied */
#define ISPGOP_EN_ST     	(0x0080) /* Generate ST to CC on T10 expiry */
#define ISPGOP_USR_SVC1  	(0x0100) /* Accept user to user info service 1 */
#define ISPGOP_USR_SVC2  	(0x0200) /* Accept user to user info service 2 */
#define ISPGOP_USR_SVC3  	(0x0400) /* Accept user to user info service 3 */
#define ISPGOP_COM       	(0x0800) /* Enable support for call intrusion */
#define ISPGOP_T35       	(0x1000) /* Run timer T35 */
#define ISPGOP_GER       	(0x2000) /* Enable support for German ISUP */
#define ISPGOP_TCCR_CTRL       	(0x4000) /* Enable support for ANSI Tccr (implemented with T104) */

/*
 * Extended options (ext_options) field:
 */
#define ISPXGOP_BLK_767  	(0x0001) /* Use Q.767 abnormal blocking procedures */
#define ISPXGOP_UBK_MH   	(0x0002) /* Remove both M+H blocking on CGU */
#define ISPXGOP_RPT_CGSM 	(0x0004) /* Repeat CGB and GRS messages */
#define ISPXGOP_UMSG_CFN 	(0x0008) /* Generate and recognise CFN */
#define ISPXGOP_ANSI     	(0x0010) /* Enable ANSI specific operation */
#define ISPXGOP_PRI_BA   	(0x0020) /* Encode priority in ssf bits BA */
#define ISPXGOP_T34	 	(0x0040) /* Run T34 */
#define ISPXGOP_TRAN_PAM 	(0x0080) /* Transit PAM messages */
#define ISPXGOP_COMPAT   	(0x0700) /* Compatibility type */
#define ISPXGOP_ITU92    	(0x0800) /* Enable 1992 ISUP message and parameter set */
#define ISPXGOP_CAUSE_767	(0x1000) /* Use Q.767 format cause */
#define ISPXGOP_DEL_ST	 	(0x2000) /* Delete ST digits from Called party number */
#define ISPXGOP_24PC 	 	(0x4000) /* Use 24 bit point codes for all groups */
#define ISPXGOP_UCIC     	(0x8000) /* Enable UCIC specific operation */

/*
 * More extended options (ext_1_options) field:
 */
#define ISPX1GOP_ADD_ST	 	(0x00000001) /* Add ST digits to Called party number */
#define ISPX1GOP_16PC	 	(0x00000002) /* Select 16 bit Point Code */
#define ISPX1GOP_T33EXP_NOREL 	(0x00000004) /* Do not send REL on T33 expiry (waiting for INF) */
#define ISPX1GOP_USR_SVC_PRV 	(0x00000008) /* Usr-to-usr srvc does not have to
					        be requested to use uuinf param */
#define ISPX1GOP_T39            (0x00000010) /* Run timer T39 */
#define ISPX1GOP_T38            (0x00000020) /* Run timer T38 */
#define ISPX1GOP_SEND_UPT       (0x00000040) /* Send 'user part test' message
                                              * NOTE: in ITU mode, UPT will be sent
                                              * otherwise in ANSI mode CVT will be sent
                                              */
#define ISPX1GOP_TX_CRG         (0x00000080) /* Pass on CRG message */
#define ISPX1GOP_CCL            (0x00000100) /* Send CCL indication instead of REL
                                              * Indication to user */
#define ISPX1GOP_AUTO_BLK  (0x00000200)  /* Autoblock CGRP */

/*
 * Definitions for variant field in ISP_MSG_CNF_GRP:
 */
#define ISPGVAR_BB  		(0) /* Blue Book ISUP variant */
#define ISPGVAR_ITU92  		(1) /* 1992 ISUP variant*/
#define ISPGVAR_ANSI   		(2) /* 1992 ANSI ISUP variant*/
#define ISPGVAR_GER   		(3) /* German ISUP variant*/
#define ISPGVAR_UK   		(4) /* UK ISUP variant */
#define ISPGVAR_TTC   		(5) /* Japanese TTC ISUP variant */
#define ISPGVAR_ANSI_RLT   	(6) /* ANSI RLT ISUP variant */
#define ISPGVAR_ITU_RLT   	(7) /* ITU RLT ISUP variant */
#define ISPGVAR_ANSI95   	(8) /* 1995 ANSI ISUP variant */
#define ISPGVAR_ITAL   		(9) /* Italian ISUP variant */
#define ISPGVAR_SSURF  		(0x0a) /* French SSURF ISUP variant */
#define ISPGVAR_CHN  		(0x0b) /* China ISUP variant */

#define ISPGVAR_CUST02		(0xfd) /* Custom variant 2 */
#define ISPGVAR_CUST01   	(0xfe) /* Custom variant 1 */

#define ISP_MAX_CUSTVAR		(2) /* Maximum number of customisable variants */

/*
 * Definitions of length, offset and size of
 * ISP_MSG_CNG_GRP message parameter field:
 */
#define ISPML_CNG_GRP		 (10)
#define ISPMO_CNG_GRP_mngt_id	 (0)
#define ISPMS_CNG_GRP_mngt_id      	(1)
#define ISPMO_CNG_GRP_mngt_inst  (1)
#define ISPMS_CNG_GRP_mngt_inst         (1)
#define ISPMO_CNG_GRP_user_id    (2)
#define ISPMS_CNG_GRP_user_id           (1)
#define ISPMO_CNG_GRP_user_inst  (3)
#define ISPMS_CNG_GRP_user_inst         (1)
#define ISPMO_CNG_GRP_maint_id   (4)
#define ISPMS_CNG_GRP_maint_id          (1)
#define ISPMO_CNG_GRP_maint_inst (5)
#define ISPMS_CNG_GRP_maint_inst        (1)
#define ISPMO_CNF_GRP_reserved   (6)
#define ISPMS_CNF_GRP_reserved          (4)

/*
 * Definitions of length, offset and size of
 * ISP_MSG_CNF_TIM message parameter fields.
 *
 * Note that ISPML_CNF_TIM_EXT is only used when
 * the version of the message is not set to 0.
 */
#define ISPML_CNF_TIM                  (80)
#define ISPML_CNF_TIM_EXT	       (128)
#define ISPMO_CNF_TIM_version	(0)
#define ISPMS_CNF_TIM_version          (1)
#define ISPMO_CNF_pad1          (1)
#define ISPMS_CNF_pad1                 (1)
#define ISPMO_CNF_TIM_t1        (2)
#define ISPMS_CNF_TIM_t1               (2)
#define ISPMO_CNF_TIM_t2        (4)
#define ISPMS_CNF_TIM_t2               (2)
#define ISPMO_CNF_TIM_t3        (6)
#define ISPMS_CNF_TIM_t3               (2)
#define ISPMO_CNF_TIM_t4        (8)
#define ISPMS_CNF_TIM_t4               (2)
#define ISPMO_CNF_TIM_t5        (10)
#define ISPMS_CNF_TIM_t5               (2)
#define ISPMO_CNF_TIM_t6        (12)
#define ISPMS_CNF_TIM_t6               (2)
#define ISPMO_CNF_TIM_t7        (14)
#define ISPMS_CNF_TIM_t7               (2)
#define ISPMO_CNF_TIM_t8        (16)
#define ISPMS_CNF_TIM_t8               (2)
#define ISPMO_CNF_TIM_t9        (18)
#define ISPMS_CNF_TIM_t9               (2)
#define ISPMO_CNF_TIM_t10       (20)
#define ISPMS_CNF_TIM_t10              (2)
#define ISPMO_CNF_TIM_t11       (22)
#define ISPMS_CNF_TIM_t11              (2)
#define ISPMO_CNF_TIM_t12       (24)
#define ISPMS_CNF_TIM_t12              (2)
#define ISPMO_CNF_TIM_t13       (26)
#define ISPMS_CNF_TIM_t13              (2)
#define ISPMO_CNF_TIM_t14       (28)
#define ISPMS_CNF_TIM_t14              (2)
#define ISPMO_CNF_TIM_t15       (30)
#define ISPMS_CNF_TIM_t15              (2)
#define ISPMO_CNF_TIM_t16       (32)
#define ISPMS_CNF_TIM_t16              (2)
#define ISPMO_CNF_TIM_t17       (34)
#define ISPMS_CNF_TIM_t17              (2)
#define ISPMO_CNF_TIM_t18       (36)
#define ISPMS_CNF_TIM_t18              (2)
#define ISPMO_CNF_TIM_t19       (38)
#define ISPMS_CNF_TIM_t19              (2)
#define ISPMO_CNF_TIM_t20       (40)
#define ISPMS_CNF_TIM_t20              (2)
#define ISPMO_CNF_TIM_t21       (42)
#define ISPMS_CNF_TIM_t21              (2)
#define ISPMO_CNF_TIM_t22       (44)
#define ISPMS_CNF_TIM_t22              (2)
#define ISPMO_CNF_TIM_t23       (46)
#define ISPMS_CNF_TIM_t23              (2)
#define ISPMO_CNF_TIM_t24       (48)
#define ISPMS_CNF_TIM_t24              (2)
#define ISPMO_CNF_TIM_t25       (50)
#define ISPMS_CNF_TIM_t25              (2)
#define ISPMO_CNF_TIM_t26       (52)
#define ISPMS_CNF_TIM_t26              (2)
#define ISPMO_CNF_TIM_t27       (54)
#define ISPMS_CNF_TIM_t27              (2)
#define ISPMO_CNF_TIM_t28       (56)
#define ISPMS_CNF_TIM_t28              (2)
#define ISPMO_CNF_TIM_t29       (58)
#define ISPMS_CNF_TIM_t29              (2)
#define ISPMO_CNF_TIM_t30       (60)
#define ISPMS_CNF_TIM_t30              (2)
#define ISPMO_CNF_TIM_t31       (62)
#define ISPMS_CNF_TIM_t31              (2)
#define ISPMO_CNF_TIM_t32       (64)
#define ISPMS_CNF_TIM_t32              (2)
#define ISPMO_CNF_TIM_t33       (66)
#define ISPMS_CNF_TIM_t33              (2)
#define ISPMO_CNF_TIM_t34       (68)
#define ISPMS_CNF_TIM_t34              (2)
#define ISPMO_CNF_TIM_t35       (70)
#define ISPMS_CNF_TIM_t35              (2)
#define ISPMO_CNF_TIM_t36       (72)
#define ISPMS_CNF_TIM_t36              (2)
#define ISPMO_CNF_TIM_t37       (74)
#define ISPMS_CNF_TIM_t37              (2)
#define ISPMO_CNF_TIM_t38       (76)
#define ISPMS_CNF_TIM_t38              (2)
#define ISPMO_CNF_TIM_t39       (78)
#define ISPMS_CNF_TIM_t39              (2)
#define ISPMO_CNF_TIM_t103      (80)
#define ISPMS_CNF_TIM_t103             (2)
#define ISPMO_CNF_TIM_t104      (82)
#define ISPMS_CNF_TIM_t104             (2)

/*
 * Definitions of length, offset and size of
 * ISP_MSG_ERROR_IND message parameter fields:
 */
#define ISPMO_ERR_IND_info1	(0)
#define ISPMS_ERR_IND_info1		(2)
#define ISPMO_ERR_IND_info2	(2)
#define ISPMS_ERR_IND_info2		(2)

/*
 * Definitions of length, offset and size of
 * ISP_MSG_CGSC_REQ, ISP_MSG_CGSC_IND and
 * ISP_MSG_CGSC_CONF message parameter fields:
 */
#define ISPML_CGSC		(6)
#define ISPMO_CGSC_ptype	(0)
#define ISPMS_CGSC_ptype		(1)
#define ISPMO_CGSC_reserved	(1)
#define ISPMS_CGSC_reserved		(1)
#define ISPMO_CGSC_cic_mask	(2)
#define ISPMS_CGSC_cic_mask	 	(4)


/*
 * Definitions of length, offset and size of
 * ISP_MSG_CGSS_IND message parameter fields:
 */
#define ISPML_CGSS		(38)
#define ISPMO_CGSS_ptype	(0)
#define ISPMS_CGSS_ptype		(1)
#define ISPMO_CGSS_reserved	(1)
#define ISPMS_CGSS_reserved		(1)
#define ISPMO_CGSS_cic_mask	(2)
#define ISPMS_CGSS_cic_mask	 	(4)
#define ISPMO_CGSS_status  	(6)
#define ISPMS_CGSS_status  	 	(32)

/*
 * Definitions for ptype field in
 * ISP_MSG_CGSC_xxx message parameter fields:
 */
#define ISP_GCSCC_RESET		(0)
#define ISP_GCSCC_STOP_RESET	(1)
#define ISP_GCSCC_M_BLOCK	(2)
#define ISP_GCSCC_M_UNBLOCK	(3)
#define ISP_GCSCC_M_STOP	(4)
#define ISP_GCSCC_H_BLOCK	(5)
#define ISP_GCSCC_H_UNBLOCK	(6)
#define ISP_GCSCC_H_STOP	(7)
#define ISP_GCSCC_QUERY		(8)

/*
 * Values 9-11 are used by TUP module for
 * blocking.
 */
#define ISP_GCSCC_AVAILABLE     (12)
#define ISP_GCSCC_UNAVAILABLE   (13)
#define ISP_GCSCC_SET_IC_ACTIVE	(14)     /* Set cct state to incoming active */
#define ISP_GCSCC_SET_OC_ACTIVE	(15)     /* Set cct state to outgoing active */

/*
 * Definitions of length, offset and size of
 * ISP_MSG_CGSC_REQ, ISP_MSG_CGSC_IND and
 * ISP_MSG_CGSC_CONF message parameter fields:
 */
#define ISPML_STATUS            (6)
#define ISPMO_STATUS_spc        (0)
#define ISPMS_STATUS_spc                (4)
#define ISPMO_STATUS_cong       (4)
#define ISPMS_STATUS_cong               (2)

/*
 * Definition of the status field for ISP_MSG_STATUS.
 */
#define ISP_STATUS_RUU		(1)	/* Remote user unavailable */
#define ISP_STATUS_CONG		(2)	/* Remote user congested */


/*
 * Definitions of length, offset and size of
 * ISP_MSG_R_STATUS message parameter fields:
 */
#define ISPML_R_STATUS		(68)
#define ISPMO_R_STATUS_cic_mask		(0)
#define ISPMS_R_STATUS_cic_mask	 		(4)
#define ISPMO_R_STATUS_status		(4)
#define ISPMS_R_STATUS_status			(64)

/*
 * Definitions of length, offset and size of
 * ISP_MSG_R_GRPID message parameter fields:
 */
#define ISPML_R_GRPID		(320)
#define ISPMO_R_GRPID_gid_mask		(0)
#define ISPMS_R_GRPID_gid_mask	 		(320)

/*
 * Definitions of length, offset and size of
 * ISP_MSG_R_DATA message parameter fields
 */
#define ISPML_R_DATA		  (64)
#define ISPMO_R_DATA_opc       	  (0)
#define ISPMS_R_DATA_opc 		(4)
#define ISPMO_R_DATA_dpc       	  (4)
#define ISPMS_R_DATA_dpc 		(4)
#define ISPMO_R_DATA_base_cic	  (8)
#define ISPMS_R_DATA_base_cic 	 	(2)
#define ISPMO_R_DATA_base_cid	  (10)
#define ISPMS_R_DATA_base_cid 		(2)
#define ISPMO_R_DATA_cic_mask	  (12)
#define ISPMS_R_DATA_cic_mask 		(4)
#define ISPMO_R_DATA_options	  (16)
#define ISPMS_R_DATA_options 		(2)
#define ISPMO_R_DATA_ext_options  (18)
#define ISPMS_R_DATA_ext_options 	(2)
#define ISPMO_R_DATA_mngt_id	  (20)
#define ISPMS_R_DATA_mngt_id 		(1)
#define ISPMO_R_DATA_mngt_inst	  (21)
#define ISPMS_R_DATA_mngt_inst		(1)
#define ISPMO_R_DATA_user_id  	  (22)
#define ISPMS_R_DATA_user_id   		(1)
#define ISPMO_R_DATA_user_inst	  (23)
#define ISPMS_R_DATA_user_inst 		(1)
#define ISPMO_R_DATA_mtp_id  	  (24)
#define ISPMS_R_DATA_mtp_id      	(1)
#define ISPMO_R_DATA_mtp_inst	  (25)
#define ISPMS_R_DATA_mtp_inst		(1)
#define ISPMO_R_DATA_sccp_id  	  (26)
#define ISPMS_R_DATA_sccp_id   		(1)
#define ISPMO_R_DATA_sccp_inst	  (27)
#define ISPMS_R_DATA_sccp_inst 		(1)
#define ISPMO_R_DATA_maint_id 	  (28)
#define ISPMS_R_DATA_maint_id  		(1)
#define ISPMO_R_DATA_maint_inst   (29)
#define ISPMS_R_DATA_maint_inst  	(1)
#define ISPMO_R_DATA_sio	  (30)
#define ISPMS_R_DATA_sio	 	(1)
#define ISPMO_R_DATA_tim_tab_id   (31)
#define ISPMS_R_DATA_tim_tab_id  	(1)
#define ISPMO_R_DATA_ext_1_options (32)
#define ISPMS_R_DATA_ext_1_options     	(4)
#define ISPMO_R_DATA_variant	  (36)
#define ISPMS_R_DATA_variant		(1)
#define ISPMO_R_DATA_ihop_count   (37)
#define ISPMS_R_DATA_ihop_count		(1)
#define ISPMO_R_DATA_pad1	  (38)
#define ISPMS_R_DATA_pad1		(26)



/*
 * Definitions of length of CAL_MSG_HEARTBEAT
 */

#define ISPML_UISM_HB           (64)
#define ISPMO_UISM_HB_ID        (0)
#define ISPMS_UISM_HB_ID               (2)
#define ISPMO_UISM_HB_STATE     (2)
#define ISPMS_UISM_HB_STATE            (2)
#define ISPMO_UISM_HB_FLAGS     (4)
#define ISPMS_UISM_HB_FLAGS            (2)
#define ISPMO_UISM_HB_RESERVED  (6)
#define ISPMS_UISM_HB_RESERVED         (58)

#define UIHB_FLAGS_CGRPS_BLOCKED    (0x0001)
#define UIHB_FLAGS_HB_RQST          (0x0002)

/*
 * Definitions of length of ISP_MSG_S_ERROR_MASK,
 * ISP_MSG_S_MAINT_MASK and ISPML_S_SELTRACE_MASK:
 */
#define ISPML_S_ERROR_MASK		(9)
#define ISPML_S_MAINT_MASK              (9)
#define ISPML_S_SELTRACE_MASK           (9)

/*
 * Definitions for use with
 * ISP_MSG_MAINT_IND messages:
 */
#define CCm_BLR_Blocking        (1)	/* Remotely Blocked */
#define CCm_BLR_Unblocking      (2)     /* Remote Unblocked */
#define CCm_BLS_Blocking        (3)	/* User initiated blocking */
#define CCm_BLS_Blocked         (4)     /* Blocked via MGBS */

#define CCm_BLS_Reset       	(6)     /* Unblocked by reset */
#define CCm_BLS_T13_Expired     (7)     /* T13 expiry */
#define CCm_BLS_T13_Recovery    (8)     /* BLA rxd after T13 expiry */
#define CCm_BLS_T15_Expired     (9)     /* T15 expiry */
#define CCm_BLS_T15_Recovery    (10)    /* UBL rxd after T15 expiry */
#define CCm_BLS_Bad_BLA         (11)    /* Unexpected BLA */
#define CCm_BLS_Bad_UBA         (12)    /* Unexpected UBL */

#define CCm_MGBS_Blocking       (13) 	/* data = cic_mask */
#define CCm_MGBS_BLK_Reset      (14)    /* data = cic_mask */
#define CCm_MGBS_UBLK_Reset     (15)    /* data = cic_mask */
#define CCm_MGBS_T19_Expired    (16)    /* data = cic_mask */
#define CCm_MGBS_T21_Expired    (17)    /* data = cic_mask */
#define CCm_MGBS_T19_Recovery   (18)    /* data = cic_mask */
#define CCm_MGBS_T21_Recovery   (19)    /* data = cic_mask */
#define CCm_MGBR_Blocking       (20)    /* data = cic_mask */
#define CCm_MGBR_Unblocking     (21)    /* data = cic_mask */
#define CCm_HLB_Unblocking      (22)
#define CCm_HRB_Unblocking      (23)
#define CCm_HGBS_Blocking       (24) 	/* data = cic_mask */
#define CCm_HGBS_BLK_Reset      (25)    /* data = cic_mask */
#define CCm_HGBS_UBLK_Reset     (26)    /* data = cic_mask */
#define CCm_HGBS_T19_Expired    (27)    /* data = cic_mask */
#define CCm_HGBS_T21_Expired    (28)    /* data = cic_mask */
#define CCm_HGBS_T19_Recovery   (29)    /* data = cic_mask */
#define CCm_HGBS_T21_Recovery   (30)    /* data = cic_mask */
#define CCm_HGBR_Blocking       (31)    /* data = cic_mask */
#define CCm_CRS_T17_Recovery	(32)
#define CCm_CRS_T17_Expired     (33)
#define CCm_CRS_T23_Recovery	(34)
#define CCm_CRS_T23_Expired     (35)
#define CCm_CRCS_Check_failed   (36)
#define CCm_CRCS_Check_ok       (37)
#define CCm_CRCS_T5_Expired     (38)
#define CCm_CRCR_Failure        (39)
#define CCm_CRCR_Recovery       (40)
#define CCm_CPC_T5_Expired      (41)
#define CCm_MGBS_Bad_CGBA       (42)
#define CCm_MGBS_Bad_CGUA       (43)
#define CCm_MGBS_No_CGBA        (44)
#define CCm_MGBS_No_CGUA        (45)
#define CCm_HGBS_Bad_CGBA       (46)
#define CCm_HGBS_Bad_CGUA       (47)
#define CCm_HGBS_No_CGBA        (48)
#define CCm_HGBS_No_CGUA        (49)
#define CCm_MDSC_Bad_CIC        (50)
#define CCm_CQR_T28_Expired     (51)
#define CCm_SPRC_UCIC_rxd       (52)
#define CCm_SPRC_CFN_rxd        (53)
#define CCm_SPRC_Bad_mtype      (54)
#define CCm_GRS_Bad_cct		(55)
#define CCm_CGB_Bad_cct		(56)
#define CCm_CGU_Bad_cct		(57)
#define CCm_CQM_Bad_cct		(58)
#define CCm_CC_Zero_hops        (59)
#define CCm_HGBS_Unblocking     (60)    /* data = cic_mask */
#define CCm_MGBS_Unblocking     (61)    /* data = cic_mask */


/*
 * Definitions for use with
 * ISP_MSG_ERROR_IND messages:
 */
#define ISPe_SDLSIG_LOW     	(1)  /* Running short of SDL signals */
#define ISPe_NO_SDLSIG      	(2)  /* SDL signals exhausted */
#define ISPe_ISPM_LOW       	(3)  /* Running short of ISPM structures */
#define ISPe_NO_ISPM        	(4)  /* ISPM structures exhausted */
#define ISPe_BAD_MSG        	(5)  /* Unsupported message received */
#define ISPe_RECV_FAIL      	(6)  /* Format error in received message */
#define ISPe_TX_FMT_ERR     	(7)  /* Failed to format message for tx */
#define ISPe_TIM_START_ERR  	(8)  /* Attempt made to re-use active timer resource */
#define ISPe_PAR_FMT_ERR    	(9)  /* Format error in received parameter */
#define ISPe_PAR_UNEXPECTED 	(10) /* Unexpected parameter received in MSG from User */
#define ISPe_MSG_UNRECOG    	(11) /* Unrecognised message received from User */
#define ISPe_BAD_FMT_LABEL  	(12) /* Bad label format received */
#define ISPe_MSG_FMT_ERR    	(13) /* General message formatting errors */
#define ISPe_WRONG_CID      	(14) /* Incorrect cid value */
#define ISPe_UMSG_UNEXPECTED 	(15) /* Unexpected message received from User */

/*
 * Definitions for use with
 * ISP_MSG_SEL_TRACE messages:
 */
#define ISPt_Zero_Hops          (1)  /* Number of hops allowed has been exceeded. */
#define ISPt_Bad_Cic            (2)  /* Message has been received for a bad CIC. */
#define ISPt_SPRC_CFN_rxd       (3)  /* Confusion message received for specified circuit. */
#define ISPt_SPRC_Bad_mtype     (4)  /* Bad (unrecognised protocol message type received. */
#define ISPt_GRS_Bad_cct        (5)  /*
                                      * GRS or GRA received for cicruits that are not
                                      * configured or for an incorrect range.
                                      */
#define ISPt_CGB_Bad_cct        (6)  /*
                                      * CGB or CGBA received for cicruits that are not
                                      * configured or for an incorrect range.
                                      */
#define ISPt_CGU_Bad_cct        (7)  /*
                                      * CGU or CGUA received for cicruits that are not
                                      * configured or for an incorrect range.
                                      */
#define ISPt_CQM_Bad_cct        (8)  /*
                                      * CQM or CQR received for cicruits that are not
                                      * configured or for an incorrect range.
                                      */
#define ISPt_RECV_FAIL      	(9)  /* Format error in received message */
#define ISPt_PAR_FMT_ERR    	(10) /* Format error in received parameter */
#define ISPt_BAD_FMT_LABEL  	(11) /* Bad label format received */
#define ISPt_MSG_FMT_ERR    	(12) /* General message formatting errors */
#define ISPt_UMSG_UNEXPECTED 	(13) /* Unexpected message received from User */
#define ISPt_WRONG_CID          (14) /* Unknown cid*/



/*
 * Definitions used in status field
 * of confirmation messages:
 */
#define ISPE_BAD_ID     (1)
#define ISPE_BAD_STATE  (2)
#define ISPE_BAD_SIG    (3)
#define ISPE_UNEX_SIG   (4)
#define ISPE_BAD_MSG    (5)  /* Inappropriate or unrecognised message */
#define ISPE_BAD_PARAM  (6)  /* Invalid parameters contained in message */

/*
 * Macro definition for ANSI operation.
 */
#define ANSI       ((igrp->variant == ISPGVAR_ANSI) || (igrp->variant == ISPGVAR_ANSI_RLT) || (igrp->variant == ISPGVAR_ANSI95))

