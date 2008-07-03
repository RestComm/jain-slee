
/*
		Copyright (C) 1993-2002 DataKinetics Ltd.

 Name:		nup_inc.h

 Description:	This file is the include file for use by modules
		which interface with the NUP Protocol Module
		Module. It contains definitions relating to
		inter-module messages.

 -----	---------  -----  ---------------------------------------------
 Issue	  Date	    By		      Changes
 -----	---------  -----  ---------------------------------------------
   A	15-Feb-95	  - Derived from sdc\inc\cnv_inc.h issue 4.
			    CNVS_IC_SPEECH & CNVS_OG_SPEECH added.
			    NUP_MSG_TX_REQ & NUP_MSG_RX_IND added.
			    sgrp config merged with add/remove ccts.
   B    02-Mar-95         - Removed message definitions not currently
			    supported.
   C    12-Sep-95         - Raised to issue C.
   C+   02-Oct-95         - Call control definitions added.
   D    29-Nov-95         - Raised to issue D.
   E    24-Feb-96         - NUP_MSG_CGSC_IND message and parameters added.
			    NUPPN_INFO_REQ defined.
   F    28-Aug-96         - NUPPPN_PARTIAL_CLI added.
   G	26-Sep-97         - Many definitions moved from cnv_def.h. New group
			    options NUPSGF_ICC_PRI and NUPSGF_PRI_CIC added.
   1	26-Sep-97   JER   - *** NUP Version 0.50 release ***
   2    14-Nov-97   JER	  - TO34 now runs for 120 seconds instead of 30.
   2	14-Nov-97   JER   - *** NUP Version 0.51 release ***
   			  - Added defines for NUP_GCSCC_M_BLOCK and
                            NUP_GCSCC_M_UNBLOCK.
   3	11-Dec-97   JER   - *** NUP Version 0.52 release ***
   4    18-Dec-97   JER   - Added NUPR_USRM_FMT.
			  - *** NUP Version 0.53 release ***
   5    09-Apr-98   JER   - Removed NUPNEV_INV.
                          - Added new option NUPSGF_IDLE.
			  - *** NUP Version 1.00 release ***
   6    01-Dec-98   JER   - Added NUPSGF_NEED13.
   7	23-Dec-98   JER   - Changes to support SIM interchange.
   8    13-Apr-99   JER   - Added new NUPSGF_EN_ST option.
   9    30-Apr-99   JER   - Added mngt_inst, mtp_id, and mtp_inst per cct group. 
   10   13-Jul-99   JER   - Increased length of module configuration message
                            and added new ext_options field.
                          - Added user_id and user_inst to configure group
                            request message.
                          - Some unused definitions moved to nup_def.h.
   11   03-Sep-99   JER   - Added NUPPPN_REDIRECTION_NUM.
   12	31-Jan-02   JER   - Added support for selective tracing.
 */

/*
 * Module id definition
 */
#define NUP_TASK_ID     (0x4a)  /* default module id */

/*
 * Message type values:
 */

/*
 * Message type definitions, (values for m->hdr.type)
 *
 * Types common to other telephony user parts :
 */
#ifndef CAL_MSG_TX_REQ
#define CAL_MSG_TX_REQ		(0xc700)  /* Transmit request from user */
#define CAL_MSG_RX_IND          (0x8701)  /* Receive indication to user */
#define CAL_MSG_CGSC_REQ        (0x7703)  /* Cct group supervision request */
#define CAL_MSG_CGSC_IND        (0x0708)  /* Cct group supervision indication */
#define CAL_MSG_CGSC_CONF       (0x0709)  /* Cct group supervision confirmation */
#endif

/*
 * Messages expected by the NUP Module:
 */
	/* Primitives */
#define NUP_MSG_TX_REQ          (CAL_MSG_TX_REQ)
#define NUP_MSG_RX_IND          (CAL_MSG_RX_IND)
#define NUP_MSG_BLOCK		(0x7605)
#define NUP_MSG_UNBLOCK		(0x7606)
#define NUP_MSG_BLK_IND		(0x0624)
#define NUP_MSG_UNBLK_IND	(0x0625)
#define NUP_MSG_CGSC_REQ        (CAL_MSG_CGSC_REQ)
#define NUP_MSG_CGSC_IND        (CAL_MSG_CGSC_IND)
#define NUP_MSG_CGSC_CONF       (CAL_MSG_CGSC_CONF)

	/* Non Primitives */
#define NUP_MSG_CONFIG	    	(0x7601)
#define NUP_MSG_CNF_GRP    	(0x7602)
#define NUP_MSG_END_GRP		(0x7603)
#define NUP_MSG_TIMERS	    	(0x5607)
#define NUP_MSG_R_STATUS        (0x7609)
#define NUP_MSG_TMASK		(0x5608)
#define NUP_MSG_EVENT	    	(0x0620)
#define NUP_MSG_REPORT	    	(0x0621)
#define NUP_MSG_S_SELTRACE_MASK (0x560a)


/*
 * Definitions of length, offset and size of
 * NUP_MSG_CONFIG message parameter field:
 */
#define NUPML_CONFIG		(32)
#define NUPML_CONFIG_EXT	(50)
#define NUPMO_CONFIG_module_id		(0)
#define NUPMS_CONFIG_module_id	    		(1)
#define NUPMO_CONFIG_mtp_id		(1)
#define NUPMS_CONFIG_mtp_id	    		(1)
#define NUPMO_CONFIG_user_id		(2)
#define NUPMS_CONFIG_user_id	    		(1)
#define NUPMO_CONFIG_mngt_id		(3)
#define NUPMS_CONFIG_mngt_id	    		(1)
#define NUPMO_CONFIG_opc		(4)
#define NUPMS_CONFIG_opc	    		(2)
#define NUPMO_CONFIG_dpc		(6)
#define NUPMS_CONFIG_dpc	    		(2)
#define NUPMO_CONFIG_num_ccts		(8)
#define NUPMS_CONFIG_num_ccts	    		(2)
#define NUPMO_CONFIG_num_grps		(10)
#define NUPMS_CONFIG_num_grps	    		(2)
#define NUPMO_CONFIG_options		(12)
#define NUPMS_CONFIG_options	    		(2)
#define NUPMO_CONFIG_sio		(14)
#define NUPMS_CONFIG_sio	    		(1)
#define NUPMO_CONFIG_trace_id		(15)
#define NUPMS_CONFIG_trace_id	    		(1)
#define NUPMO_CONFIG_num_tscan		(16)
#define NUPMS_CONFIG_num_tscan	    		(2)
#define NUPMO_CONFIG_sfrc_N		(18)
#define NUPMS_CONFIG_sfrc_N	    		(2)
#define NUPMO_CONFIG_sfrc_T		(20)
#define NUPMS_CONFIG_sfrc_T	    		(2)
#define NUPMO_CONFIG_N_blk		(22)
#define NUPMS_CONFIG_N_blk	    		(2)
#define NUPMO_CONFIG_N_ublk		(24)
#define NUPMS_CONFIG_N_ublk	    		(2)
#define NUPMO_CONFIG_bk_t1		(26)
#define NUPMS_CONFIG_bk_t1	    		(2)
#define NUPMO_CONFIG_bk_t2		(28)
#define NUPMS_CONFIG_bk_t2	    		(2)
#define NUPMO_CONFIG_bk_t3		(30)
#define NUPMS_CONFIG_bk_t3	    		(2)
#define NUPMO_CONFIG_ext_options	(32)
#define NUPMS_CONFIG_ext_options	    	(2)
#define NUPMO_CONFIG_reserved		(34)
#define NUPMS_CONFIG_reserved	    		(16)

/*
 * Bit definitions for the configuration message options field (note other
 * definitions exist in nup_def.h):
 */
#define NUP_OPT_VERSION2	(0x0001)  /* Interworking with V2 (default is V3) */
#define NUP_OPT_USE_CNA		(0x0002)  /* Use CNA instead of Release */
#define NUP_OPT_CGSC_IND	(0x0020)  /* Issue CGSC indications */
#define NUP_OPT_RUN_TO1		(0x0040)  /* Run Timer TO1 */
#define NUP_OPT_UBREL		(0x0080)  /* Release if unblocking fails */
#define NUP_OPT_GRP_PC		(0x0100)  /* OPC/DPC per circuit group if set */
#define NUP_OPT_MTP_STAT	(0x0200)  /* User informed of point code status changes */
#define NUP_OPT_ACR		(0x0400)  /* Wait for RLC from user if set */
#define NUP_OPT_NAI		(0x0800)  /* Send RLC on entering idle state if set */
#define NUP_OPT_CGSC_STD	(0x1000)  /* Use standard CGSC interface */
#define NUP_OPT_TFRM		(0x2000)  /* Issue MTP messages in T_FRAMEs */
#define NUP_OPT_NUP_FMT		(0x4000)  /* Issue user messages in generic NUP format */
#define NUP_OPT_PARAM		(0x8000)  /* Use common call control parameter names */

/*
 * Bit definitions for the configuration message extended options field:
 */
#define NUP_XOPT_DUAL		(0x0001)  /* Operate in dual NUP mode */

/*
 * Definitions of length, offset and size of
 * NUP_MSG_CNF_GRP message parameter field:
 */
#define NUPML_CNF_GRP		(32)
#define NUPMO_CNF_GRP_opc      	(0)
#define NUPMS_CNF_GRP_opc 		(2)
#define NUPMO_CNF_GRP_dpc      	(2)
#define NUPMS_CNF_GRP_dpc 		(2)
#define NUPMO_CNF_GRP_base_cic  (4)
#define NUPMS_CNF_GRP_base_cic 		(2)
#define NUPMO_CNF_GRP_base_cid  (6)
#define NUPMS_CNF_GRP_base_cid 		(2)
#define NUPMO_CNF_GRP_cic_mask  (8)
#define NUPMS_CNF_GRP_cic_mask 		(4)
#define NUPMO_CNF_GRP_options   (12)
#define NUPMS_CNF_GRP_options 		(2)
#define NUPMO_CNF_GRP_mngt_id	(14)
#define NUPMS_CNF_GRP_mngt_id		(1)
#define NUPMO_CNF_GRP_mngt_inst	(15)
#define NUPMS_CNF_GRP_mngt_inst		(1)
#define NUPMO_CNF_GRP_mtp_id	(16)
#define NUPMS_CNF_GRP_mtp_id		(1)
#define NUPMO_CNF_GRP_mtp_inst	(17)
#define NUPMS_CNF_GRP_mtp_inst		(1)
#define NUPMO_CNF_GRP_user_id	(18)
#define NUPMS_CNF_GRP_user_id		(1)
#define NUPMO_CNF_GRP_user_inst	(19)
#define NUPMS_CNF_GRP_user_inst		(1)
#define NUPMO_CNF_SGRP_reserved (20)
#define NUPMS_CNF_SGRP_reserved 	(12)

/*
 * Bit definitions for the configure group message options field (note other
 * definitions exist in nup_def.h):
 */
#define NUPSGF_ACTIVE	    (0x0001)  	/* signalling group enabled */
#define NUPSGF_OLI_REQ      (0x0002)  	/* Request OLI if not supplied */
#define NUPSGF_CBI          (0x0020)  	/* Set CLI blocking indicator */
#define NUPSGF_REQ_LDLI     (0x0040)  	/* Request LDLI for diverted calls */
#define NUPSGF_VPN          (0x0080)	/* Enable suport for VPN calls */
#define NUPSGF_SASUI        (0x0100)    /* If interworking use SASUI */
#define NUPSGF_IDLE	    (0x0200)	/* No unblock needed before use*/
#define NUPSGF_ICC_PRI	    (0x0400)    /* Incoming call priority */
#define NUPSGF_PRI_CIC      (0x0800)    /* Base priority on OPC, DPC & CIC */
#define NUPSGF_NEED13       (0x1000)    /* NEED msgs allowed if CPC=13 */
#define NUPSGF_SIM_VALID    (0x2000)    /* SIM msgs are valid */
#define NUPSGF_EN_ST        (0x4000)	/* run timer TO6 if set */

/*
 * Definitions of length, offset and size of
 * NUP_MSG_TIMERS message parameter field:
 */
#define NUPML_TIMERS	(54)
#define NUPMO_TIMERS_TO1	(0)
#define NUPMS_TIMERS_TO1		(2)
#define NUPMO_TIMERS_TO2	(2)
#define NUPMS_TIMERS_TO2		(2)
#define NUPMO_TIMERS_TO3	(4)		/* not used */
#define NUPMS_TIMERS_TO3		(2)	/* not used */
#define NUPMO_TIMERS_TO4	(6)
#define NUPMS_TIMERS_TO4		(2)
#define NUPMO_TIMERS_TO5	(8)
#define NUPMS_TIMERS_TO5		(2)
#define NUPMO_TIMERS_TO6	(10)		/* not used */
#define NUPMS_TIMERS_TO6		(2)	/* not used */
#define NUPMO_TIMERS_TO7	(12)		/* not used */
#define NUPMS_TIMERS_TO7		(2)	/* not used */
#define NUPMO_TIMERS_TO8	(14)
#define NUPMS_TIMERS_TO8		(2)
#define NUPMO_TIMERS_TO9	(16)
#define NUPMS_TIMERS_TO9		(2)
#define NUPMO_TIMERS_TO10	(18)
#define NUPMS_TIMERS_TO10		(2)
#define NUPMO_TIMERS_TO11	(20)		/* not used */
#define NUPMS_TIMERS_TO11		(2)	/* not used */
#define NUPMO_TIMERS_TO12	(22)
#define NUPMS_TIMERS_TO12		(2)
#define NUPMO_TIMERS_TO13	(24)		/* not used */
#define NUPMS_TIMERS_TO13		(2)	/* not used */
#define NUPMO_TIMERS_TO14	(26)
#define NUPMS_TIMERS_TO14		(2)
#define NUPMO_TIMERS_TO15	(28)		/* not used */
#define NUPMS_TIMERS_TO15		(2)	/* not used */
#define NUPMO_TIMERS_TO16	(30)		/* not used */
#define NUPMS_TIMERS_TO16		(2)	/* not used */
#define NUPMO_TIMERS_TO17	(32)		/* not used */
#define NUPMS_TIMERS_TO17		(2)	/* not used */
#define NUPMO_TIMERS_TO18	(34)
#define NUPMS_TIMERS_TO18		(2)
#define NUPMO_TIMERS_TO19	(36)		/* not used */
#define NUPMS_TIMERS_TO19		(2)	/* not used */
#define NUPMO_TIMERS_TO20	(38)		/* not used */
#define NUPMS_TIMERS_TO20		(2)	/* not used */
#define NUPMO_TIMERS_TO21	(40)		/* not used */
#define NUPMS_TIMERS_TO21		(2)	/* not used */
#define NUPMO_TIMERS_TO22	(42)		/* not used */
#define NUPMS_TIMERS_TO22		(2)	/* not used */
#define NUPMO_TIMERS_TO30	(44)		/* not used */
#define NUPMS_TIMERS_TO30		(2)	/* not used */
#define NUPMO_TIMERS_TO31	(46)		/* not used */
#define NUPMS_TIMERS_TO31		(2)	/* not used */
#define NUPMO_TIMERS_TO32	(48)
#define NUPMS_TIMERS_TO32		(2)
#define NUPMO_TIMERS_TO33	(50)
#define NUPMS_TIMERS_TO33		(2)
#define NUPMO_TIMERS_TO34	(52)		/* not used */
#define NUPMS_TIMERS_TO34		(2)	/* not used */

/*
 * Default protocol timer values:
 * (Values in seconds - the user should ensure that the
 * module is configured with a protocol timer resolution
 * equal to one second).
 */
#define NUP_DEF_TO1	(120)
#define NUP_DEF_TO2	(120)
#define NUP_DEF_TO4	(120)
#define NUP_DEF_TO5     (45)
#define NUP_DEF_TO6     (4)		/* not used */
#define NUP_DEF_TO8	(30)
#define NUP_DEF_TO9	(30)
#define NUP_DEF_TO10	(25)
#define NUP_DEF_TO12	(25)
#define NUP_DEF_TO14	(30)
#define NUP_DEF_TO18	(20)
#define NUP_DEF_TO30	(10)		/* not used */
#define NUP_DEF_TO31	(45)		/* not used */
#define NUP_DEF_TO32	(12)
#define NUP_DEF_TO33	(15)
#define NUP_DEF_TO34	(120)		/* not used */

/*
 * Definitions of length, offset and size of
 * NUP_MSG_BLOCK message parameter field.
 *
 * These definitions are retained for backwards
 * compatibility and should no longer be used.
 */


#define NUPML_BLOCK	 (4)
#define NUPMO_BLOCK_cic_mask       (0)
#define NUPMS_BLOCK_cic_mask 	(4)

/*
 * Definitions of length, offset and size of
 * NUP_MSG_UNBLOCK message parameter field:
 *
 * These definitions are retained for backwards
 * compatibility and should no longer be used.
 */
#define NUPML_UNBLOCK	 (4)
#define NUPMO_UNBLOCK_cic_mask     (0)
#define NUPMS_UNBLOCK_cic_mask 	(4)

/*
 * Definitions of length, offset and size of
 * NUP_MSG_BLK_IND message parameter field:
 *
 * These definitions are retained for backwards
 * compatibility and should no longer be used.
 */
#define NUPML_BLK_IND	 (4)
#define NUPMO_BLK_IND_cic_mask      (0)
#define NUPMS_BLK_IND_cic_mask  (4)

/*
 * Definitions of length, offset and size of
 * NUP_MSG_UNBLK_IND message parameter field:
 *
 * These definitions are retained for backwards
 * compatibility and should no longer be used.
 */
#define NUPML_UNBLK_IND  (4)
#define NUPMO_UNBLK_IND_cic_mask     (0)
#define NUPMS_UNBLK_IND_cic_mask (4)

/*
 * Definitions of length, offset and size of
 * NUP_MSG_CGSC_REQ, NUP_MSG_CGSC_IND and
 * NUP_MSG_CGSC_CONF message parameter fields:
 * (NOTE: These definitions are identical to
 * the format used for ISUP and TUP and must
 * therefore not be modified).
 */
#define NUPML_CGSC		(6)
#define NUPMO_CGSC_ptype	(0)
#define NUPMS_CGSC_ptype		(1)
#define NUPMO_CGSC_reserved	(1)
#define NUPMS_CGSC_reserved		(1)
#define NUPMO_CGSC_cic_mask	(2)
#define NUPMS_CGSC_cic_mask	 	(4)

/*
 * Definitions for ptype field in
 * NUP_MSG_CGSC_xxx message parameter fields:
 */
#define NUP_GCSCC_RESET		(0)
#define NUP_GCSCC_M_BLOCK	(2)
#define NUP_GCSCC_M_UNBLOCK	(3)
#define NUP_GCSCC_H_BLOCK	(5)
#define NUP_GCSCC_H_UNBLOCK	(6)
#define NUP_GCSCC_SIG_RESUME	(12)
#define NUP_GCSCC_SIG_FAIL	(13)

/*
 * Definitions of length, offset and size of
 * NUP_MSG_TMASK message parameter field:
 */
#define NUPML_TMASK	(20)
#define NUPMO_TMASK_ip_evt  (0)
#define NUPMS_TMASK_ip_evt	(4)
#define NUPMO_TMASK_op_evt  (4)
#define NUPMS_TMASK_op_evt	(4)
#define NUPMO_TMASK_dbg_evt (8)			/* not used, set to 0 */
#define NUPMS_TMASK_dbg_evt	(4)		/* not used, set to 0 */
#define NUPMO_TMASK_nup_evt (12)		/* not used, set to 0 */
#define NUPMS_TMASK_nup_evt	(4)		/* not used, set to 0 */
#define NUPMO_TMASK_man_evt (16)
#define NUPMS_TMASK_man_evt	(4)

/*
 * Input event trace mask definitions:
 */
#define NUPIEV_TX_REQ		(0x0001)	/* User Message */
#define NUPIEV_MTP_TRFR_IND	(0x0002)	/* NUP Message */
#define NUPIEV_MTP_PAUSE	(0x0004)	/* MTP-Pause */
#define NUPIEV_MTP_RESUME	(0x0008)	/* MTP-Resume */
#define NUPIEV_CGSC_REQ		(0x0010)	/* CGSC Request Message */
#define NUPIEV_BLOCK		(0x0020)	/* NUP_MSG_BLOCK */
#define NUPIEV_UNBLOCK		(0x0040)	/* NUP_MSG_UNBLOCK */

/*
 * Output event trace mask definitions:
 */
#define NUPOEV_RX_IND		(0x0001)	/* User Message */
#define NUPOEV_MTP_TRFR_REQ	(0x0002)	/* NUP Message */
#define NUPOEV_BLK_IND 		(0x0004)	/* Local Blocking Ind */
#define NUPOEV_UNBLK_IND 	(0x0008)	/* Local Unblocking Ind */
#define NUPOEV_CGSC_IND		(0x0010)	/* CGSC Indication Message */
#define NUPOEV_CGSC_CONF	(0x0020)	/* CGSC Confirmation */

/*
 * Configuration and management trace masks definitions
 */
#define NUPMEV_CONFIG		(0x0001)	/* NUP_MSG_CONFIG */
#define NUPMEV_CNF_GRP		(0x0002)        /* NUP_MSG_CNF_GRP */
#define NUPMEV_END_GRP		(0x0004)        /* NUP_MSG_END_GRP */
#define	NUPMEV_TIMERS		(0x0008)        /* NUP_MSG_TIMERS */
#define NUPMEV_R_STATUS		(0x0010)        /* NUP_MSG_R_STATUS */
#define NUPMEV_EVENT		(0x0020)        /* NUP_MSG_EVENT */
#define NUPMEV_REPORT		(0x0040)        /* NUP_MSG_REPORT */
#define NUPMEV_TMASK		(0x0080)	/* NUP_MSG_TMASK */
#define NUPMEV_S_SELTRC_MASK	(0x0100)	/* NUP_MSG_S_SELTRACE */

/*
 * Definitions for use with
 * NUP_MSG_SEL_TRACE messages:
 */
#define NUPML_SELTRACE_MASK	(2)
#define NUPt_unex_nupm	(0)  /* Unexpected NUP message */
#define NUPt_unex_usrm	(1)  /* Unexpected user primitive */
#define NUPt_usrm_fmt   (2)  /* Badly formatted primitive from user */
#define NUPt_usrm_par   (3)  /* Invalid parameter contents received from user */
#define NUPt_nupm_fmt	(4)  /* Badly formatted NUP message received */
#define NUPt_nupm_label	(5)  /* Bad label in NUP message */
#define NUPt_nupm_cic	(6)  /* Bad label in NUP message */
#define NUPt_usrm_type	(7)  /* Unrecognised message type received from user */
#define NUPt_usrm_cid	(8)  /* Unassigned CIC received in message from user */

/*
 * Definitions of length, offset and size of
 * NUP_MSG_R_STATUS message parameter field:
 */
#define NUPML_R_STATUS		(36)
#define NUPMO_R_STATUS_cic_mask	(0)
#define NUPMS_R_STATUS_cic_mask		(4)
#define NUPMO_R_STATUS_status   (4)
#define NUPMS_R_STATUS_status		(32)


/*
 * Call control interface definitions :
 * ==================================
 *
 * Primitive type tokens for requests:
 */
#define NUPPN_SETUP_REQ         (CCPT_IAM)	/* Setup request */
#define NUPPN_INFO_REQ		(CCPT_SAM)	/* Info request */
#define NUPPN_ALERT_REQ         (CCPT_ACM)	/* Alert request */
#define NUPPN_SETUP_RESP        (CCPT_ANM)	/* Setup response */
#define NUPPN_RELEASE_REQ       (CCPT_REL)	/* Release request */
#define NUPPN_NEED_REQ		(CCPT_NEED)	/* Nodal end to end data request */

/*
 * Primitive type tokens for indications:
 */
#define NUPPN_SETUP_IND         (CCPT_IAM)	/* Setup indication */
#define NUPPN_INFO_IND          (CCPT_SAM)	/* Info indication */
#define NUPPN_ALERT_IND         (CCPT_ACM)	/* Alert indication */
#define NUPPN_SETUP_CONF        (CCPT_ANM)	/* Setup confirmation */
#define NUPPN_RELEASE_IND       (CCPT_REL)	/* Release indication */
#define NUPPN_RELEASE_CONF	(CCPT_RLC)	/* Release confirmation */
#define NUPPN_CPG_IND		(CCPT_CPG)	/* Call progress indication */
#define NUPPN_NEED_IND		(CCPT_NEED)	/* Nodal end to end data indication */

/*
 * Parameter name tokens:
 */
#define NUPPPN_AT		(CCPN_at)		/* Access transport */
#define NUPPPN_BCI              (CCPN_bci)		/* Backward call indicators */
#define NUPPPN_CALLED_NUM       (CCPN_called_num)	/* Called party number */
#define NUPPPN_CALLING_NUM      (CCPN_calling_num)	/* Calling party number */
#define NUPPPN_CAUSEI           (CCPN_causei)		/* Cause indicators */
#define NUPPPN_CPC              (CCPN_cpc)		/* Calling party's category */
#define NUPPPN_TERMINATOR	(CCPN_eoop)		/* End of parameters token */
#define NUPPPN_FCI              (CCPN_fci)		/* Forward call indicators */
#define NUPPPN_NEED   		(CCPN_need)		/* Nodal end to end data */
#define NUPPPN_NOCI             (CCPN_noci)		/* Nature of connection indicators */
#define NUPPPN_NUP_EVENT_INF	(CCPN_nup_event_inf)	/* NUP event information */
#define NUPPPN_NUP_FCIND	(CCPN_nup_fci)		/* NUP forward call indicators */
#define NUPPPN_PARTIAL_CLI	(CCPN_partial_cli)	/* Partial CLI */
#define NUPPPN_REDIRECTING_NUM  (CCPN_redirecting_num)	/* Redirecting number */
#define NUPPPN_REDIRECTION_NUM  (CCPN_redirection_num)	/* Redirection number */
#define NUPPPN_REDIR_INF        (CCPN_redirection_inf)	/* Redirection information */
#define NUPPPN_SUBSQ_NUM        (CCPN_subsq_num)	/* Subsequent number */
#define NUPPPN_TMR              (CCPN_tmr)		/* Transmission medium requirement */
#define NUPPPN_USINF		(CCPN_usinf)		/* User service information */
#define NUPPPN_MAX		(254)			/* Maximum parameter token value */

/*
 * These old definitions are retained for backwards compatibility and
 * should not be used by new applications. (New applications should set
 * the NUP_OPT_PARAM module option.)
 */
#define NUPPPN_REDIR_NUM        (12)			/* Redirecting number */
#define NUPPPN_NUP_FCI		(254)			/* NUP forward call indicators */

/*
 * Digit definitions:
 */
#define ST_DIGIT	(0x0f)

/*
 * Parameter value definitions:
 */
#define NUPEVT_BIT_NEED_AVAIL	(0x0001)  /* Event info - need available */

/*
 * Protocol events:
 * These are indicated using a NUP_MSG_EVENT indication which
 * conveys the event type and the cid (where appropriate).
 * No additional parameters are indicated in the message.
 */
#define NUPE_NO_CCTF	    (1)      /* Failed to receive CCTF */
#define NUPE_NO_CIM	    (2)      /* Failed to receive CIM */
#define NUPE_NO_BKA	    (3)      /* No response to Blocking */
#define NUPE_NO_UBKA	    (4)      /* No response to Unblocking */
#define NUPE_NUPM_FMT	    (5)      /* Invalid NUP message format */
#define NUPE_NUPM_LABEL     (6)      /* Bad label in NUP message */
#define NUPE_NUPM_CIC	    (7)      /* Unassigned CIC in NUP message */
#define NUPE_USRM_TYPE	    (9)      /* Bad type in user message */
#define NUPE_USRM_CID	    (10)     /* Unassigned CID in user message */

/*
 * Definitions of length, offset, and size of NUP_MSG_REPORT
 */
#define NUPML_REPORT	(2)
#define NUPMO_REPORT_info0	(0)
#define NUPMS_REPORT_info0		(1)
#define NUPMO_REPORT_info1	(1)
#define NUPMS_REPORT_info1		(1)

/*
 * Report types:
 * These used in the m->hdr.status field of NUP_MSG_REPORT
 * indications. NUP_MSG_REPORT messages differ from NUP_MSG_EVENT
 * messages in that they contain additional parameters depending
 * on the report type.
 */
#define NUPR_UNEX_NUPM	    (1)      /* Unexpected NUP message received */
#define NUPR_UNEX_USRM	    (2)      /* Unexpected user message received */
#define NUPR_USRM_FMT	    (3)      /* Invalid user message format */
#define NUPR_USRM_PAR	    (4)	     /* Invalid user parameter contents */

/*
 * The following definitions are retained for backwards compatibility:
 */

/*
 * Definitions of length, offset and size of
 * NUPR_UNEX_NUPM report parameter field:
 */
#define NUPRL_UNEX_NUPM	(NUPML_REPORT)
#define NUPRO_UNEX_NUPM_state 	(NUPMO_REPORT_info0)
#define NUPRS_UNEX_NUPM_state		(NUPMS_REPORT_info0)
#define NUPRO_UNEX_NUPM_mtype	(NUPMO_REPORT_info1)
#define NUPRS_UNEX_NUPM_mtype		(NUPMS_REPORT_info1)

/*
 * Definitions of length, offset and size of
 * NUPR_UNEX_USRM report parameter field:
 */
#define NUPRL_UNEX_USRM 	(NUPML_REPORT)
#define NUPRO_UNEX_USRM_state	(NUPMO_REPORT_info0)
#define NUPRS_UNEX_USRM_state	    (NUPMS_REPORT_info0)
#define NUPRO_UNEX_USRM_mtype	(NUPMO_REPORT_info1)
#define NUPRS_UNEX_USRM_mtype	    (NUPMS_REPORT_info1)

/*
 * Definitions of length, offset and size of
 * NUPR_USRM_FMT report parameter field:
 */
#define NUPRL_USRM_FMT	 	(NUPML_REPORT)
#define NUPRO_USRM_FMT_mtype	(NUPMO_REPORT_info0)
#define NUPRS_USRM_FMT_mtype	    (NUPMS_REPORT_info0)

/*
 * Error code definitions:
 */
#define NUP_BAD_MSG	    (1)     /* Invalid message type received */
#define NUP_BAD_PARAM	    (2)     /* Invalid parameters in message */
#define NUP_BLK_FAIL	    (3)     /* Failed to block */
#define NUP_UNBLK_FAIL      (4)     /* Failed to unblock */
#define NUP_NO_RESOURCE     (5)     /* No resources available */



