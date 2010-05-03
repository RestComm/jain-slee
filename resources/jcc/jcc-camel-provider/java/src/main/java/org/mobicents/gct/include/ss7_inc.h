/*
		Copyright (C) 1992-2005 Intel Corporation.
              
 Name:		ss7_inc.h

 Description:	Include file for System 7 Software,
		applicable to all layers.

 -----  ---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------
   A	21-Sep-91
   B	13-Feb-92   - MTP linkset support added.
   1	14-Feb-92
   2	23-Apr-92   - MVIP driver (MVD) support added.
   3	15-May-92   - MGT_MSG_CONFIG1 added.
   4	02-Jul-92   - MTP_MALLOC_ERR added.
   5	25-Aug-92   - SS7_EVT_LINK_CONG, SS7_EVT_LINK_UNCONG,
		      SS7_MSG_REP_NEXT_SU & SS7F_SU_MINRATE added.
		      SS7_L2 Link Failure status values (S7F_***)
		      and SS7_MEVT_*** added. Driver set requests
		      DVR_MSG_SCC_REGS & DVR_MSG_SS7_CNF added.
   6	09-Sep-92   - MVD_MSG_PAC_INTR, MVD_MSG_CNFCAR,
		      MVD_MSG_QRYCARSTS added.
   7	26-Nov-92   - MTP_MSG_CNF_TIMERS .. MTP_MSG_GARBAGE, MTPEV_xxx
		      and MTP_xxxx codes added.
   8    02-Jun-93   - API_MSG_RTVD_MSG added.
   9    19-Jun-93   - SPE_MSG_xxx and MVD AIS Monitor messages added.
   10   27-Jun-93   - MVD_MSG_SETSTREAM & PCS73_MOD added.
   11   07-Aug-93   - API_MSG_RST_REQ, API_MSG_RST_IND and
		      API_MSG_TST_REQ added.
   12   21-Sep-93   - ISP_TASK_ID added.
   13   25-Mar-94   - Definitions added for parameter areas in MTP
		      messages. SCP_TASK_ID added. SDLSIG errors added.
   14   11-Aug-94   - TCP_TASK_ID added.
   15   15-Sep-94   - RMMA_TASK_ID and RMMB_TASK_ID added.
   16   02-Nov-94   - MTP_EVT_xxx constants made long. MTP_TIM_ERR added.
		      API_MSG_RTVD_MSG changed from 0x8f02 to 0x8f08
		      to allow operation with level 3 module.
   17   16-Nov-94   - RMMA_TASK_ID and RMMB_TASK_ID removed.
   18   22-Nov-94   - S7E_CONG_DISC added.
   19   26-May-95   - S7C_MCONG, MGT_MSG_SS7_EVENT, SS7_MEVT_SL_CONG,
		      SS7_MSG_R_STATE & S7G_xxx added.
   20   11-Apr-96   - S7C_REM_AERM, SS7F_LOC_AERM, SS7_MSG_AERM_xxx
		      SS7_MSG_AERM_FAIL & MGT_MSG_CONGESTION added.
   21   22-Nov-97   - MTP_RRT_OVRFLW added.
   22   26-Mar-98   - MTPTFR14 and MTPTFR24 added.
   23   21-May-98   - S7C_LSSU2 & S7C_MONITOR added.
   24   29-May-98   - API_MSG_CNF_IND & MGT_MSG_READ_ID added.
   25   26-Aug-98   - Additional definitions for MVIP/SCbus interface.
   26   09-Oct-98   - Additional module id's added.
   27   18-Nov-98   - SS7_MSG_CONTINUE, SS7_MSG_FLUSH, MTP_MSG_FLUSH_ACK,
		      MTP_EVT_FLUSH_ACK, MTP_EVT_CONTINUE, MTP_EVT_FLUSH,
		      MTP_FLUSH_L2, MTP_FLUSH_FAIL & S7C_WAIT_CONT added.
   28   16-Apr-99   - DPN2_TASK_ID and DPN3_TASK_ID added.
   29   29-May-99   - Generic SSD module and GEN_MSG_MOD_IDENT
		      definitions added.
   30   03-Feb-00   - Additional SPE definitions added.
   31   20-Mar-00   - MVD_MSG_SC_PERM, MVD_MSG_CLOCK_PRI, MVD_MSG_SC_FIXDATA
		      and associated definitions added.
   32   01-Apr-00   - MTPTFR16 added.
   33   01-Jun-00   - Definitions for monitoring functionality added.
   34   23-Jun-00   - MVD_MSG_LED_CTRL added.
   35   03-Nov-00   - ISDN_IF_ID added.
   36   24-May-01   - cause field added to MTP-STATUS indications.
		      MGT_MSG_SEL_TRACE added.
   37   20-Jun-01   - TUP_TASK_ID and NUP_TASK_ID added.
   38   26-Jun-01   - TUP_TASK_ID and NUP_TASK_ID corrected to 0x4a
		      (was 0x2a).
   39   20-Dec-01   - S7C_FSNX_SYNC added.
   40   04-May-02   - MTP_MSG_R_RT_STATUS & MTP_MSG_UPDATE_L4 added.
   41   07-Jul-02   - SS7_MSG_FAST_TICK added.
   42   21-Oct-02   - DTS_TASK_ID added. Copyright changed to Intel.
   43   22-Mar-03   - S7T_xxx and S7P_xxx values added.
   44   11-Jun-03   - Module definitions for SP0 .. SP3 added.
		      MVD_MSG_CLK_IND & L1 config/end messages added.
		      Additional SSD definitions added (support of
		      run_mode & SS7HD). CNFCLK2 definitions added.
   45   11-Jun-03   - MGT_MSG_ABORT added.
   46   15-Oct-03   - MVD_MSG_R_CLK_STATUS definitions added.
		      SSDMx_RST_BOARD 'old' definitions for PCCS3 added.
		      SSDSI_LIC_CRP added.
   47   08-Mar-04   - SS7_MSG_END_LINK & S7C_MON_FISU added.
   48   07-Jun-04   - MVD_ALT_TASK_ID, SSD_ALT_TASK_ID & MGMT_ALT_TASK_ID
		      added.
   49   21-Jun-04   - MTP_MSG_SRT_START, MTP_MSG_SRT_RESULT, MTP_SRT_FAIL,
                      MTP_SRT_RETRY, MTP_EVT_SRT_START, MTP_EVT_SRT_RESULT
                      & MTPSRTR_xxxx added.
   50   26-Jul-04   - Addition of per link options to L1_CONFIG msg
                    - Definitions for MTP per link options for L1_CONFIG msg. 
                      (No Auto FISU and Enable BBD monitor timestamping).
   51   31-Aug-04   - MTP_MSG_R_LK_STATUS added.
   52   29-Oct-04   - MGT_MSG_R_BRDINFO and associated definitions imported
		      from brdinfo.h Issue C. SYS_MSG_CONGESTION,
		      CONGESTION & NO_CONGESTION definitions added.
   53  25-Jan-05    - MH  - Add data_invert and clock_invert_serial fields 
                            to MGT_MSG_L1_CONFIG message

 */


/*
 * Module ID's (see also modules.h):
 */
#define DVR_TASK_ID     (0x00)  /* Enhanced driver module */
#define MVD_TASK_ID     (0x10)  /* Switch/Clock module */
#define MVD_ALT_TASK_ID (0x60)  /* Alternative Switch/Clock module */
#define DTS_TASK_ID     (0x30)  /* DTS module */
#define SSD_TASK_ID     (0x20)  /* SSD interface module */
#define SSD_ALT_TASK_ID (0x70)  /* SSD alternative module */
#define QDV_TASK_ID     (0x50)  /* Multi-link monitoring module */
#define RSI_TASK_ID     (0xb0)  /* RSI socket interface module */
#define DVR_SP0_TASK_ID (0x80)  /* Driver for SP0 */
#define DVR_SP1_TASK_ID (0x90)  /* Driver for SP1 */
#define DVR_SP2_TASK_ID (0xe0)  /* Driver for SP2 */
#define DVR_SP3_TASK_ID (0xf0)  /* Driver for SP3 */


#define CONG_TASK_ID    (0x21)  /* System congestion module */
#define TERM_IF_ID      (0x61)
#define SS7_TASK_ID     (0x71)  /* SS7 Level 2 (MTP2) */
#define MON_TASK_ID     (0x81)
#define SIM_TASK_ID     (0x91)
#define DPN2_TASK_ID    (0xa1)
#define SS7_SP0_TASK_ID (0x81)  /* MTP2 for SP0 */
#define SS7_SP1_TASK_ID (0x91)  /* MTP2 for SP1 */
#define SS7_SP2_TASK_ID (0xe1)  /* MTP2 for SP2 */
#define SS7_SP3_TASK_ID (0xf1)  /* MTP2 for SP3 */

#define MTP_TASK_ID     (0x22)  /* SS7 Level 3 */
#define RMM_TASK_ID     (0x32)  /* RMM module */
#define DPN3_TASK_ID    (0xa2)

#define ISP_TASK_ID     (0x23)  /* ISUP Module */
#define SCP_TASK_ID     (0x33)  /* SCCP Module */
#define TUP_TASK_ID     (0x4a)  /* TUP Module */
#define NUP_TASK_ID     (0x4a)  /* NUP Module */

#define TCP_TASK_ID     (0x14)  /* TCAP Module */

#define MAP_TASK_ID     (0x15)  /* MAP Module */
#define IS41_TASK_ID    (0x25)  /* IS41 Module */
#define INAP_TASK_ID    (0x35)  /* INAP Module */

#define MGMT_TASK_ID     (0x8e)  /* Management Module */
#define MGMT_ALT_TASK_ID (0x7e)  /* Alternative Management Module */
#define ISDN_IF_ID       (0xbe)  /* ISDN stack interface task */
#define L4_TASK_ID       (0x1f)
#define USER_IF_ID       (0x1f)
#define REM_API_ID       (0xef)  /* Remote API module (eg. TLD) */
#define BOOT_TASK_ID     (0x98)  /* Initialisation code (not really a module) */
#define MGMT_SP0_TASK_ID (0xce) /* Management Module for SP0 */
#define MGMT_SP1_TASK_ID (0xde) /* Management Module for SP1 */
#define MGMT_SP2_TASK_ID (0xee) /* Management Module for SP2 */
#define MGMT_SP3_TASK_ID (0xfe) /* Management Module for SP3 */

/*
 * Task ID's for user application tasks.
 */
#define APP0_TASK_ID    (0x0d)
#define APP1_TASK_ID    (0x1d)
#define APP2_TASK_ID    (0x2d)
#define APP3_TASK_ID    (0x3d)
#define APP4_TASK_ID    (0x4d)
#define APP5_TASK_ID    (0x5d)
#define APP6_TASK_ID    (0x6d)
#define APP7_TASK_ID    (0x7d)
#define APP8_TASK_ID    (0x8d)
#define APP9_TASK_ID    (0x9d)
#define APP10_TASK_ID   (0xad)
#define APP11_TASK_ID   (0xbd)
#define APP12_TASK_ID   (0xcd)
#define APP13_TASK_ID   (0xdd)
#define APP14_TASK_ID   (0xed)
#define APP15_TASK_ID   (0xfd)

/*
 * Module types
 */
#define EDVR_MOD    (0x0100)	/* Enhanced driver module */
#define S7L2_MOD    (0x0110)	/* S7L2 module */
#define MTP_MOD     (0x0120)	/* S7L3 module, minimum system, CCITT */
#define SMTP_MOD    (0x0121)	/* S7L3 module, MTP, CCITT/ANSI */

#define PCS73_MOD   (500)       /* PCCS3/PCS73 MTP CCITT/ANSI */

/*
 * Layer indicies in T_FRAMES for SS7 modules:
 */
#define L2_LAYER    (1)
#define L3_LAYER    (2)
#define MTP_LAYER   (2)
#define L4_LAYER    (3)

/*
 * Definitions for Layer flags in T_FRAME structure
 */
/* #define TX_POOL  (0x80)  (defined in msg.h) */
#define DATA_ODD    (0x40)  /* data buffer ends with padding byte */
#define HDR_ODD     (0x20)  /* header buffer ends with padding byte */
/* #define HDR_USED (0x10)  (defined in msg.h) */
#define NO_CRC	    (0x08)  /* driver should not append CRC on tx */
#define TX_REPT     (0x04)  /* driver should repeat SU until next SU */

#define SS7_L2_HDR_LEN	(3) /* length of L2 header ie BI, FI, & LI */
#define SS7_L3_HDR_LEN	(5) /* length of L3 header ie SIO & LABEL */

/*********************************************************************
 * General Purpose and Global definitions.
 ********************************************************************/

/*
 * Message type definitions.
 */
#define SYS_MSG_CONGESTION  (0x0001) /* Congestion status (from environment) */
#define SS7_MSG_R_VERSION   (0x6110) /* use GEN_MSG_MOD_IDENT instead */
#define GEN_MSG_MOD_IDENT   (0x6111)

/*
 * Definitions used in the 'status' field
 * of SYS_MSG_CONGESTION messages to indicate
 * current level of congestion within the
 * software environment.
 */
#define NO_CONGESTION   (0)
#define CONGESTION      (1)

/*
 * Definitions for fields in the parameter block of
 * the SS7_MSG_R_VERSION message:
 */
#define VERMSG_LENGTH	       (6)  /* length of the parameter block */

#define VERMSG_mod_type_OFF    (0)  /* Module type (see xxx_MOD above) */
#define VERMSG_mod_type_SIZ  (2)
#define VERMSG_maj_rev_OFF     (0)  /* Major revision number */
#define VERMSG_maj_rev_SIZ   (2)
#define VERMSG_min_rev_OFF     (0)  /* Minor revision number */
#define VERMSG_min_rev_SIZ   (2)

/*
 * Definitions of type length, offset and size of
 * GEN_MSG_MOD_IDENT message parameter field:
 */
#define GENML_MOD_IDENT         (28)
#define GENMO_MOD_IDENT_type    (0)
#define GENMS_MOD_IDENT_type            (2)
#define GENMO_MOD_IDENT_maj_rev (2)
#define GENMS_MOD_IDENT_maj_rev         (1)
#define GENMO_MOD_IDENT_min_rev (3)
#define GENMS_MOD_IDENT_min_rev         (1)
#define GENMO_MOD_IDENT_text    (4)
#define GENMS_MOD_IDENT_text            (24)

/*********************************************************************
 * Definitions for interfacing to the Enhanced Driver.
 ********************************************************************/

/*
 * Message type definitions for messages sent
 * from the SS7_L2 module to the driver.
 */
#define SS7_MSG_SUERM_START (0xc10d)
#define SS7_MSG_SUERM_STOP  (0xc10e)
#define SS7_MSG_SUERM_CHECK (0xc10f)
#define SS7_MSG_REP_NEXT_SU (0xc110)
#define SS7_MSG_AERM_START  (0xc111)
#define SS7_MSG_AERM_STOP   (0xc112)

/*
 * Additional Set Requests:
 */
#define DVR_MSG_SCC_REGS    (0x5106)
#define DVR_MSG_SS7_CNF     (0x5107)

/*
 * Bit definitions for ss7_flags field in driver work_area:
 * (All other bits are reserved and should be set to zero)
 */
#define SS7F_EXT_CLK	    (0x0001)	/* set if external clock in use */
#define SS7F_SU_MINRATE     (0x0002)	/* set if driver should indicate */
					/* link failure when no SU's */
					/* received between SUERM checks */
#define SS7F_LOC_AERM	    (0x0004)    /* Implement AERM driver */

/*
 * Definitions of length, offset and size of
 * SS7_MSG_AERM_START message parameter field:
 */
#define SS7ML_ST_AERM (2)
#define SS7MO_ST_AERM_identifier    (0)		/* cyclic identifier */
#define SS7MS_ST_AERM_identifier        (1)
#define SS7MO_ST_AERM_threshold	    (1)		/* AERM threshold */
#define SS7MS_ST_AERM_threshold		(1)

/*
 * Definitions of length, offset and size of
 * SS7_MSG_AERM_FAIL message parameter field:
 */
#define SS7ML_AERM_FAIL (1)
#define SS7MO_AERM_FAIL_identifier  (0)		/* cyclic identifier */
#define SS7MS_AERM_FAIL_identifier      (1)


/*********************************************************************
 * Definitions for interfacing to the MVIP/SCbus/Clock Drivers
 ********************************************************************/

#define MVD_MSG_RESETSWX    		(0x7e00)  /* RESET_SWITCH */
#define MVD_MSG_QRYSWXCAP   		(0x7e01)  /* QUERY_SWITCH_CAPS */
#define MVD_MSG_SETOUTPUT   		(0x7e10)  /* SET_OUTPUT */
#define MVD_MSG_QRYOUTPUT   		(0x7e11)  /* QUERY_OUTPUT */
#define MVD_MSG_SAMPINPUT   		(0x7e12)  /* SAMPLE_INPUT */
#define MVD_MSG_SETVERIFY   		(0x7e13)  /* SET_VERIFY */

#define MVD_MSG_SC_PERM                 (0x7e14)  /* Permanent SC-Connect */
#define MVD_MSG_SC_FIXDATA              (0x7e15)
#define MVD_MSG_SC_PATTERN              (0x7e16)
#define MVD_MSG_SC_LISTEN               (0x7e17)
#define MVD_MSG_SC_DRIVE_LIU            (0x7e18)
#define MVD_MSG_SC_MULTI_CONNECT        (0x7e19)  /* non-preferred */
#define MVD_MSG_LIU_CTRL                (0x7e1a)  /* obsolete - do not use */
#define MVD_MSG_WRITE_SEP               (0x7e1b)  /* non-preferred */
#define MVD_MSG_READ_SEP                (0x7e1c)  /* non-preferred */
#define MVD_MSG_SC_CONNECT_STREAM       (0x7e1d)  /* non-preferred */
#define MVD_MSG_CONNECT_STREAM          (0x7e1e)  /* non-preferred */
#define MVD_MSG_SC_CONNECT              (0x7e1f)

#define MVD_MSG_CNFCLOCK                (0x7e20)  /* CONFIG_CLOCK */
#define MVD_MSG_CLOCK_PRI               (0x7e21)  /* Ref clock priority */
#define MVD_MSG_CLK_IND                 (0x0e23)  /* Clock event indication */

#define MVD_MSG_CNFCAR	    		(0x7e30)  /* CONFIG_CARRIER */
#define MVD_MSG_QRYCARSTS   		(0x7e33)  /* QUERY_CARRIER_STATUS */

#define MVD_MSG_DUMPSWX     		(0x7e70)  /* DUMP_SWITCH */
#define MVD_MSG_SETTRACE    		(0x7e71)  /* SET_TRACE */
#define MVD_MSG_TRISWX	    		(0x7e72)  /* TRISTATE_SWITCH */

#define MVD_MSG_CONFIG	    		(0x7e80)
#define MVD_MSG_AIS_EN      		(0x7e81)  /* Enable AIS monitor */
#define MVD_MSG_AIS_DIS     		(0x7e82)  /* Disable AIS monitor */
#define MVD_MSG_R_AIS       		(0x6e83)  /* Read AIS monitor status */
#define MVD_MSG_SETSTREAM   		(0x7e84)  /* Condition stream */

#define MVD_MSG_LIU_STATUS  		(0x0e01)
#define MVD_MSG_PAC_INTR    		(0x0e02)  /* PAC Interrupt */
#define MVD_MSG_AIS_STATUS  		(0x0e03)  /* AIS monitor status ind */
#define MVD_MSG_R_CLK_STATUS 		(0x6e04)  /* Read current clock status */

#define MVD_MSG_LED_CTRL                (0x7e07)  /* LED Control */

/*
 * Definitions of length, offset and size of
 * MVD_MSG_SETOUTPUT message parameter field:
 */
#define MVDML_SETOUT                (12)
#define MVDMO_SETOUT_output_stream  (0)
#define MVDMS_SETOUT_output_stream          (2)
#define MVDMO_SETOUT_output_slot    (2)
#define MVDMS_SETOUT_output_slot            (2)
#define MVDMO_SETOUT_mode           (4)
#define MVDMS_SETOUT_mode                   (2)
#define MVDMO_SETOUT_input_stream   (6)
#define MVDMS_SETOUT_input_stream           (2)
#define MVDMO_SETOUT_input_slot     (8)
#define MVDMS_SETOUT_input_slot             (2)
#define MVDMO_SETOUT_message        (10)
#define MVDMS_SETOUT_message                (2)

/*
 * Definitions of length, offset and size of
 * MVD_MSG_SC_CONNECT message parameter field:
 */
#define MVDML_SCCON                 (16)
#define MVDMO_SCCON_local_stream    (0)
#define MVDMS_SCCON_local_stream            (2)
#define MVDMO_SCCON_local_slot      (2)
#define MVDMS_SCCON_local_slot              (2)
#define MVDMO_SCCON_mode            (4)
#define MVDMS_SCCON_mode                    (2)
#define MVDMO_SCCON_source_stream   (6)
#define MVDMS_SCCON_source_stream           (2)
#define MVDMO_SCCON_source_slot     (8)
#define MVDMS_SCCON_source_slot             (2)
#define MVDMO_SCCON_dest_stream     (10)
#define MVDMS_SCCON_dest_stream             (2)
#define MVDMO_SCCON_dest_slot       (12)
#define MVDMS_SCCON_dest_slot               (2)
#define MVDMO_SCCON_pattern         (14)
#define MVDMS_SCCON_pattern                 (2)

/*
 * Definitions of length, offset and size of
 * MVD_MSG_CNF_CLOCK message parameter field:
 */
#define MVDML_CNFCLK                (8)
#define MVDMO_CNFCLK_sc_speed       (0)
#define MVDMS_CNFCLK_sc_speed               (2)
#define MVDMO_CNFCLK_clk_mode       (2)
#define MVDMS_CNFCLK_clk_mode               (2)
#define MVDMO_CNFCLK_pll_src        (4)
#define MVDMS_CNFCLK_pll_src                (2)
#define MVDMO_CNFCLK_s8k_mode       (6)
#define MVDMS_CNFCLK_s8k_mode               (2)

/*
 * Definitions of length, offset and size of
 * MVD_MSG_CNF_CLOCK message parameter field
 * when used for SPCI or SS7HD boards:
 */
#define MVDML_CNFCLK2               (12)
#define MVDMO_CNFCLK2_bus_speed     (0)
#define MVDMS_CNFCLK2_bus_speed             (2)
#define MVDMO_CNFCLK2_clk_mode      (2)
#define MVDMS_CNFCLK2_clk_mode              (2)
#define MVDMO_CNFCLK2_pll_clk_src   (4)
#define MVDMS_CNFCLK2_pll_clk_src           (2)
#define MVDMO_CNFCLK2_ref1_mode     (6)
#define MVDMS_CNFCLK2_ref1_mode             (2)
#define MVDMO_CNFCLK2_ref2_mode     (8)
#define MVDMS_CNFCLK2_ref2_mode             (2)
#define MVDMO_CNFCLK2_clk_term      (10)
#define MVDMS_CNFCLK2_clk_term              (2)

/*
 * Definitions of length, offset and size of
 * MVD_MSG_R_CLK_STATUS message parameter field:
 */
#define MVDML_RCLKST                (10)
#define MVDMO_RCLKST_clk_mode       (0)
#define MVDMS_RCLKST_clk_mode               (2)
#define MVDMO_RCLKST_status_a       (2)
#define MVDMS_RCLKST_status_a               (2)
#define MVDMO_RCLKST_status_b       (4)
#define MVDMS_RCLKST_status_b               (2)
#define MVDMO_RCLKST_pll_state      (6)
#define MVDMS_RCLKST_pll_state              (2)
#define MVDMO_RCLKST_liu_rec        (8)
#define MVDMS_RCLKST_liu_rec                (2)

/*
 * Definitions of length, offset and size of
 * MVD_MSG_SC_DRIVE_LIU message parameter field:
 */
#define MVDML_SCLIU                 (10)
#define MVDMO_SCLIU_liu_id          (0)
#define MVDMS_SCLIU_liu_id                  (2)
#define MVDMO_SCLIU_sc_channel      (2)
#define MVDMS_SCLIU_sc_channel              (2)
#define MVDMO_SCLIU_ts_mask         (4)
#define MVDMS_SCLIU_ts_mask                 (4)
#define MVDMO_SCLIU_mode            (8)
#define MVDMS_SCLIU_mode                    (2)

/*
 * Definitions of length, offset and size of
 * MVD_MSG_SC_LISTEN message parameter field:
 */
#define MVDML_SCLIS                 (6)
#define MVDMO_SCLIS_liu_id          (0)
#define MVDMS_SCLIS_liu_id                  (2)
#define MVDMO_SCLIS_timeslot        (2)
#define MVDMS_SCLIS_timeslot                (2)
#define MVDMO_SCLIS_sc_channel      (4)
#define MVDMS_SCLIS_sc_channel              (2)

/*
 * Definitions of length, offset and size of
 * MVD_MSG_SC_PATTERN message parameter field:
 */
#define MVDML_SCPAT                 (6)
#define MVDMO_SCPAT_pattern_id      (0)
#define MVDMS_SCPAT_pattern_id              (2)
#define MVDMO_SCPAT_sc_channel      (2)
#define MVDMS_SCPAT_sc_channel              (2)
#define MVDMO_SCPAT_pattern         (4)
#define MVDMS_SCPAT_pattern                 (2)

/*
 * Definitions of length, offset and size of
 * MVD_MSG_SC_FIXDATA message parameter field:
 */
#define MVDML_SCFIX                 (6)
#define MVDMO_SCFIX_liu_id          (0)
#define MVDMS_SCFIX_liu_id                  (2)
#define MVDMO_SCFIX_timeslot        (2)
#define MVDMS_SCFIX_timeslot                (2)
#define MVDMO_SCFIX_pattern         (4)
#define MVDMS_SCFIX_pattern                 (2)

/*
 * Definitions for values in the status field of
 * MVD_MSG_LIU_STATUS message:
 */
#define LIUS_SYNC_LOSS	    (10)
#define LIUS_IN_SYNC	    (11)
#define LIUS_AIS	    (12)
#define LIUS_AIS_CLRD	    (13)
#define LIUS_REM_ALARM	    (14)
#define LIUS_REM_ALM_CLRD   (15)
#define LIUS_IN_MFSYNC      (16)
#define LIUS_MFSYNC_LOSS    (17)
#define LIUS_TS16_AIS	    (18)
#define LIUS_TS16_NOAIS     (19)
#define LIUS_PCM_LOSS	    (20)
#define LIUS_PCM_OK	    (21)
#define LIUS_FRAME_SLIP     (22)
#define LIUS_ERR_X16	    (24)
#define LIUS_BER5_OCRD	    (25)
#define LIUS_BER5_CLRD	    (26)
#define LIUS_BER3_OCRD	    (27)
#define LIUS_BER3_CLRD	    (28)


/*********************************************************************
 * Definitions for interfacing to MTP2
 ********************************************************************/

/*
 * Bit definitions for options field in MTP2 link configuration message:
 */
#define S7C_PCR      	(0x0001) /* Preventive Cyclic Retransmission */
#define S7C_ANSI     	(0x0002) /* ANSI mode (default is CCITT Blue Book) */
#define S7C_4K8      	(0x0004) /* 4.8kb/s operation (default is 64kb/sec) */
#define S7C_MCONG    	(0x0008) /* Support multiple congestion levels */
#define S7C_REM_AERM 	(0x0010) /* AERM implemented remotely (in driver) */
#define S7C_LSSU2    	(0x0020) /* Generate 2 octet LSSUs */
#define S7C_WAIT_CONT	(0x0040) /* Wait for CONTINUE from MTP3 */
#define S7C_FSNX_SYNC   (0x0080) /* Invoke special NTT options */
#define S7C_MON_FISU    (0x0100) /* Include FISU when in monitor mode */
#define S7C_MONITOR  	(0x0200) /* Operate in monitor mode */
#define S7C_LOOP     	(0x8000) /* Operate in loopback mode */

/*
 * Definitions for event masks in ip_evt_mask
 */
#define SS7_EVT_MSG_FOR_TX	(0x0001)
#define SS7_EVT_RX_IND		(0x0002)
#define SS7_EVT_TM_EXP		(0x0004)
#define SS7_EVT_BUSY_IND	(0x0008)
#define SS7_EVT_START		(0x0010)
#define SS7_EVT_STOP		(0x0020)
#define SS7_EVT_RX_ERR		(0x0040)
#define SS7_EVT_EMGCY		(0x0080)
#define SS7_EVT_EMGCY_CLRD	(0x0100)
#define SS7_EVT_RTV_BSNT	(0x0200)
#define SS7_EVT_RTVL_REQ	(0x0400)
#define SS7_EVT_LOC_PR_OUT	(0x0800)
#define SS7_EVT_LOC_PR_OK	(0x1000)
#define SS7_EVT_L3_FAIL 	(0x2000)	/* obsolete */
#define SS7_EVT_FLUSH 		(0x2000)
#define SS7_EVT_DVR_RDY 	(0x4000)
#define SS7_EVT_SUERM_FAIL	(0x8000)

/*
 * Definitions for event masks in op_evt_mask
 */
#define SS7_EVT_RXD_MSG 	(0x0001)
#define SS7_EVT_IN_SVC		(0x0002)
#define SS7_EVT_OUT_SVC 	(0x0004)
#define SS7_EVT_REM_PR_OUT	(0x0008)
#define SS7_EVT_REM_PR_OK	(0x0010)
#define SS7_EVT_RXD_BSNT	(0x0020)
#define SS7_EVT_RTVD_MSG	(0x0040)
#define SS7_EVT_RTVL_COMPL	(0x0080)
#define SS7_EVT_XMIT		(0x0100)
#define SS7_EVT_SUERM_START	(0x0200)
#define SS7_EVT_SUERM_STOP	(0x0400)
#define SS7_EVT_SUERM_CHECK	(0x0800)
#define SS7_EVT_LINK_CONG	(0x1000)
#define SS7_EVT_LINK_UNCONG	(0x2000)
#define SS7_EVT_REP_NEXT_SU	(0x4000)
#define SS7_EVT_RTVL_NOT_POS	(0x8000)

/*
 * Definitions for event masks in mngt_evt_mask
 */
#define SS7_MEVT_STATE		 (0x0001)   /* Report changes of state */
#define SS7_MEVT_ERROR		 (0x0002)   /* Report errors */
#define SS7_MEVT_SL_FAIL	 (0x0004)   /* Report Q.791 failure events */
#define SS7_MEVT_SL_CONG	 (0x0008)   /* Report Q.791 cong events */
#define SS7_MEVT_SL_TEXP	 (0x0010)   /* Report Q.791 timer events */
#define SS7_MEVT_SL_PROV	 (0x0020)   /* Report Q.791 proving events */
#define SS7_MEVT_MON_TRACE	 (0x8000)   /* Trace rx SU's to monitor */

/*
 * Message type definitions for messages sent
 * to the SS7_L2 module.
 */
#define SS7_MSG_TX_REQ	    (0xc000)	/* TX_REQ */
#define SS7_MSG_RX_IND	    (0x8001)	/* RX_IND */
#define SS7_MSG_TM_EXP	    (0xc002)	/* TM_EXP */
#define SS7_MSG_BUSY_IND    (0x8003)	/* BUSY_IND */
#define SS7_MSG_START	    (0xc204)
#define SS7_MSG_STOP	    (0xc205)
#define SS7_MSG_RX_ERR	    (0x8006)	/* RX_ERR */
#define SS7_MSG_EMGCY	    (0xc207)
#define SS7_MSG_EMGCY_CLRD  (0xc208)
#define SS7_MSG_RTV_BSNT    (0xc209)
#define SS7_MSG_RTVL_REQ    (0xc20a)
#define SS7_MSG_LOC_PR_OUT  (0xc20b)
#define SS7_MSG_LOC_PR_OK   (0xc20c)
#define SS7_MSG_L3_FAIL     (0xc20d)
#define SS7_MSG_DVR_RDY     (0x820e)
#define SS7_MSG_SUERM_FAIL  (0x820f)
#define SS7_MSG_AERM_FAIL   (0x8210)
#define SS7_MSG_CONTINUE    (0xc211)
#define SS7_MSG_FLUSH       (0xc212)

/*
 * Action, Read and Set request messages.
 */
#define SS7_MSG_RESET	    (0x7200)	/* Action Requests */
#define SS7_MSG_CONFIG	    (0x7203)
#define SS7_MSG_END_LINK    (0x7217)
#define SS7_MSG_GARBAGE     (0x7210)
#define SS7_MSG_R_BSS	    (0x6211)	/* Read Requests */
#define SS7_MSG_R_CHILD     (0x6212)
#define SS7_MSG_R_STATS     (0x6214)
#define SS7_MSG_R_STATE     (0x6215)
#define SS7_MSG_TRACE_MASK  (0x5213)    /* Set Requests */
#define SS7_MSG_FAST_TICK   (0x0216)    /* Event Indication */

/*
 * SS7_L2 Link Status values:
 */
#define S7S_IN_SERVICE	    (0x01)
#define S7S_OUT_SERVICE     (0x02)
#define S7S_INIT_ALIGN	    (0x03)
#define S7S_ALIGN_NOT_RDY   (0x04)
#define S7S_ALIGN_READY     (0x05)
#define S7S_PROC_OUTAGE     (0x06)

/*
 * SS7_L2 Error code status values:
 */
#define S7E_RESET_ERR	    (0x31)  /* failed to reset module */
#define S7E_POOL_EMPTY	    (0x33)  /* failed to allocate from tx pool */
#define S7E_TX_FAIL	    (0x34)
#define S7E_HDR_ERR	    (0x35)
#define S7E_LEN_ERR	    (0x36)
#define S7E_MSU_SEND	    (0x37)
#define S7E_GARBAGE	    (0x38)
#define S7E_BAD_PRIM	    (0x39)
#define S7E_BAD_LLID	    (0x3a)
#define S7E_MEM_ERR	    (0x3b)
#define S7E_RTVL_ERR	    (0x3c)
#define S7E_CONG_DISC	    (0x3d)  /* No longer used */

/*
 * SS7_L2 Q.791 Link Failure event values:
 */
#define S7F_STOP	    (0x00)  /* Stop request received */
#define S7F_FIBR_BSNR	    (0x01)  /* Abnormal FIBR/BSNR */
#define S7F_EDA 	    (0x02)  /* Excessive delay of ack (T7 expired) */
#define S7F_SUERM	    (0x03)  /* Excessive error rate (SUERM) */
#define S7F_ECONG	    (0x04)  /* Excessive congestion (T6 expired) */
#define S7F_SIO_RXD	    (0x05)  /* Unexpected SIO received */
#define S7F_SIN_RXD	    (0x06)  /* Unexpected SIN received */
#define S7F_SIE_RXD	    (0x07)  /* Unexpected SIE received */
#define S7F_SIOS_RXD	    (0x08)  /* SIOS received */

/*
 * SS7_L2 Q.791 Congestion event values:
 */
#define S7G_CONG	    (0x10)  /* Congestion onset */
#define S7G_CONG_CLR	    (0x11)  /* Congestion abatement */
#define S7G_CONG_DIS	    (0x12)  /* Congestion event caused MSU discard */

/*
 * SS7_L2 Q.791 Timer event values:
 */
#define S7T_T1_EXP	    (0x20)  /* Timer T1 expiry */
#define S7T_T2_EXP	    (0x21)  /* Timer T2 expiry */
#define S7T_T3_EXP	    (0x22)  /* Timer T3 expiry */
/*
 * SS7_L2 Q.791 Proving event values:
 */
#define S7P_AERM	    (0x30)  /* Failed proving attempt (AERM) */


/*********************************************************************
 * Definitions for interfacing to the MTP3 module
 ********************************************************************/

/*
 * Message types for messages sent to the MTP module.
 */
#define MTP_MSG_TX_REQ		(0xc000)
/* #define SS7_MSG_RX_IND	(0x8001) */
/* #define SS7_MSG_TM_EXP	(0xc002) */
#define SS7_MSG_IN_SVC		(0x8303)
#define SS7_MSG_OUT_SVC 	(0x8304)
#define SS7_MSG_REM_PR_OUT	(0x8305)
#define SS7_MSG_REM_PR_OK	(0x8306)
#define SS7_MSG_RXD_BSNT	(0x8307)
#define SS7_MSG_RTVD_MSG	(0x8308)
#define SS7_MSG_RTVL_COMPL	(0x8309)
#define MTP_MSG_ACT_SL		(0xc30a)
#define MTP_MSG_DEACT_SL	(0xc30b)
#define MTP_MSG_SLTC_START	(0xc30c)
#define MTP_MSG_SP_RESTART	(0xc30d)    /* not currently supported */
#define MTP_MSG_ACT_LS		(0xc30e)
#define MTP_MSG_DEACT_LS	(0xc30f)
#define MTP_MSG_INHIB_SL	(0xc310)
#define MTP_MSG_UNINHIB_SL	(0xc311)
#define MTP_MSG_LINK_CONG	(0x8312)
#define MTP_MSG_LINK_UNCONG	(0x8313)
#define MTP_MSG_BSNT_UNAVAIL	(0x8314)
#define MTP_MSG_RTVL_NOT_POS	(0x8315)
#define MTP_MSG_FLUSH_ACK       (0x8316)
#define MTP_MSG_SRT_START       (0xc317)

/*
 * Message types for messages issued by the MTP module
 * to the layer 4 module:
 */
#define MTP_MSG_PAUSE		(0x8403)
#define MTP_MSG_RESUME		(0x8404)
#define MTP_MSG_STATUS		(0x8405)
#define MTP_MSG_RESTART 	(0x8406)

/*
 * Message types for messages issued by the MTP module
 * to the layer 3 management module:
 */
#define MTP_MSG_LINK_INHIB	(0x8380)
#define MTP_MSG_LINK_UNINHIB	(0x8381)
#define MTP_MSG_INHIB_DENIED	(0x8382)
#define MTP_MSG_UNINHIB_FAIL	(0x8383)
#define MTP_MSG_MSG_FOR_USP	(0x8384)

/*
 * Other message types for messages issued by the
 * MTP module:
 */
#define MTP_MSG_SRT_RESULT      (0x8321)

/*
 * Message types for Action, Read and Set Requests:
 */
#define MTP_MSG_RESET		(0x7300)
#define MTP_MSG_CONFIG		(0x7303)
#define MTP_MSG_CNF_LINKSET	(0x7310)
#define MTP_MSG_CNF_LINK	(0x7311)
#define MTP_MSG_CNF_ROUTE	(0x7312)
#define MTP_MSG_R_RAM		(0x6313)
#define MTP_MSG_R_LINKSET	(0x6314)
#define MTP_MSG_R_LINK		(0x6315)
#define MTP_MSG_TRACE_MASK	(0x5316)
#define MTP_MSG_CNF_TIMERS	(0x7317)
#define MTP_MSG_R_SP_STATS	(0x6318)
#define MTP_MSG_R_RT_STATS	(0x6319)
#define MTP_MSG_R_LS_STATS	(0x631a)
#define MTP_MSG_R_LK_STATS	(0x631b)
#define MTP_MSG_R_ROUTE 	(0x631c)
#define MTP_MSG_GARBAGE 	(0x731d)
#define MTP_MSG_R_RT_STATUS     (0x631e)
#define MTP_MSG_UPDATE_L4       (0x731f)
#define MTP_MSG_R_LK_STATUS     (0x6322)

/*
 * Definitions of length, offset and size of
 * MTP_MSG_PAUSE, MTP_MSG_RESUME, MTP_MSG_STATUS
 * and MTP_MSG_RESTART message parameter fields:
 */
#define MTPML_PAUSE		(4)
#define MTPMO_PAUSE_dpc		(0)
#define MTPMS_PAUSE_dpc	        (4)

#define MTPML_RESUME		(4)
#define MTPMO_RESUME_dpc	(0)
#define MTPMS_RESUME_dpc	(4)

#define MTPML_STATUS		(6)
#define MTPML_STATUS_EXT	(8)	/* Extended status message including cause */
#define MTPMO_STATUS_dpc	(0)
#define MTPMS_STATUS_dpc	    (4)
#define MTPMO_STATUS_cong	(4)
#define MTPMS_STATUS_cong	    (2)
#define MTPMO_STATUS_cause	(6)
#define MTPMS_STATUS_cause	    (2)

/*
 * Definitions of length, offset and size of
 * MTP_MSG_SRT_START message parameter fields:
 */
#define MTPML_SRTREQ            (4)
#define MTPMO_SRTREQ_dpc        (0)
#define MTPMS_SRTREQ_dpc        (4)

/*
 * Definitions for status field in
 * MTP_MSG_STATUS and MTP_MSG_RESTART
 * message status fields:
 */
#define MTP_STATUS_UPU		(1)
#define MTP_STATUS_CONG		(2)
#define MTP_RESTART_BEGIN 	(1)
#define MTP_RESTART_END 	(2)

/*
 * Definitions for cause values in
 * MTP_MSG_STATUS field:
 */
#define MTP_UPU_UNKNOWN		(0)
#define MTP_UPU_UNEQUIP		(1)
#define MTP_UPU_INACCESS	(2)

/*
 * Definitions of length, offset and size of
 * MTP_MSG_MSG_FOR_USP message parameter field:
 */
#define MTPML_MSG_FOR_USP	(4)
#define MTPMO_MSG_FOR_USP_dpc	(0)
#define MTPMS_MSG_FOR_USP_dpc	(4)

/*
 * Definitions for event masks in ip_evt_mask
 */
#define MTP_EVT_XFR_REQ 	(0x0001l)
#define MTP_EVT_RXD_MSG 	(0x0002l)
#define MTP_EVT_TM_EXP		(0x0004l)
#define MTP_EVT_IN_SVC		(0x0008l)
#define MTP_EVT_OUT_SVC 	(0x0010l)
#define MTP_EVT_REM_PR_OUT	(0x0020l)
#define MTP_EVT_REM_PR_OK	(0x0040l)
#define MTP_EVT_RXD_BSNT	(0x0080l)
#define MTP_EVT_RTVD_MSG	(0x0100l)
#define MTP_EVT_RTVL_COMPL	(0x0200l)
#define MTP_EVT_ACT_SL		(0x0400l)
#define MTP_EVT_DEACT_SL	(0x0800l)
#define MTP_EVT_SLTC_START	(0x1000l)
#define MTP_EVT_SP_RESTART	(0x2000l)
#define MTP_EVT_ACT_LS		(0x4000l)
#define MTP_EVT_DEACT_LS	(0x8000l)
#define MTP_EVT_INHIB_SL	(0x010000l)
#define MTP_EVT_UNINHIB_SL	(0x020000l)
#define MTP_EVT_LINK_CONG	(0x040000l)
#define MTP_EVT_LINK_UNCONG	(0x080000l)
#define MTP_EVT_BSNT_UNAVAIL	(0x100000l)
#define MTP_EVT_RTVL_NOT_POS	(0x200000l)
#define MTP_EVT_FLUSH_ACK       (0x400000l)
#define MTP_EVT_SRT_START       (0x800000l)

/*
 * Definitions for event masks in op_evt_mask
 */
#define MTP_EVT_XFR_IND 	(0x0001l)
#define MTP_EVT_PAUSE		(0x0002l)
#define MTP_EVT_RESUME		(0x0004l)
#define MTP_EVT_STATUS		(0x0008l)
#define MTP_EVT_RESTART 	(0x0010l)
#define MTP_EVT_TX_REQ		(0x0020l)
#define MTP_EVT_L2_START	(0x0040l)
#define MTP_EVT_L2_STOP 	(0x0080l)
#define MTP_EVT_L2_EM		(0x0100l)
#define MTP_EVT_L2_NO_EM	(0x0200l)
#define MTP_EVT_RTV_BSNT	(0x0400l)
#define MTP_EVT_RTVL_REQ	(0x0800l)
#define MTP_EVT_LOC_PR_OUT	(0x1000l)
#define MTP_EVT_LOC_PR_OK	(0x2000l)
#define MTP_EVT_LINK_INHIB	(0x4000l)
#define MTP_EVT_LINK_UNINHIB	(0x8000l)
#define MTP_EVT_INHIB_DENIED	(0x10000l)
#define MTP_EVT_UNINHIB_FAIL	(0x20000l)
#define MTP_EVT_MSG_FOR_USP	(0x40000l)
#define MTP_EVT_CONTINUE        (0x80000l)
#define MTP_EVT_FLUSH           (0x100000l)
#define MTP_EVT_SRT_RESULT      (0x200000l)

/*
 * MTP Error code status values:
 */
#define MTP_BAD_PRIM	(0x51)
#define MTP_POOL_EMPTY	(0x52)	   /* Transmit pool empty allocation failure */
#define MTP_TX_FAIL	(0x53)	   /* Failed to send MSU to lower layer */
#define MTP_LEN_ERR	(0x54)	   /* MTP MSU too long for buffer */
#define MTP_SLT_FAIL	(0x55)	   /* Signalling link test failure */
#define MTP_BAD_DPC	(0x56)	   /* Inaccessible destination point code */
#define MTP_TALLOC_ERR	(0x57)	   /* Unable to allocate T_FRAME */
#define MTP_BAD_ID	(0x58)	   /* Invalid id in message HDR */
#define MTP_MALLOC_ERR	(0x59)	   /* Unable to allocate MSG */
#define MTP_BSNT_FAIL	(0x5a)	   /* Failed to get BSNT from L2 */
#define MTP_RTV_FAIL	(0x5b)	   /* Failed to retrieve messages from L2 */
#define MTP_BAD_FSN	(0x5c)	   /* Erroneous FSN in received COA */
#define MTP_BAD_COO	(0x5d)	   /* COO received after changeover complete */
#define MTP_SNMM_ERR	(0x5e)	   /* S/W error - invalid SNM type for tx */
#define MTP_SLTM_ERR	(0x5f)	   /* S/W error - invalid SLT type for tx */
#define MTP_NO_COA	(0x60)	   /* Failed to receive COA */
#define MTP_NO_CBA	(0x61)	   /* Failed to receive CBA */
#define MTP_NO_LUA	(0x62)	   /* Failed to receive LUA */
#define MTP_NO_LUN	(0x63)	   /* Failed to receive LUN */
#define MTP_NO_LIA	(0x64)	   /* Failed to receive LIA */
#define MTP_GARBAGE	(0x65)	   /* Frames in garbage queue */
#define MTP_TIM_ERR	(0x66)	   /* Attempt to reuse active timer resource */
#define MTP_RRT_OVRFLW  (0x67)     /* Rerouting buffer overflow */
#define MTP_FLUSH_FAIL  (0x68)     /* Failed to receive Flush Ack from L2 */
#define MTP_FLUSH_L2    (0x69)     /* L2 transmission buffers flushed */
#define MTP_SRT_FAIL    (0x70)     /* SRT failure */
#define MTP_SRT_RETRY   (0x71)     /* SRT 2nd attempt commenced */

/*
 * MTP Q.791 event status values:
 */
#define MTPEV_CO	(0x01)	   /* Changeover	  (Q.791 1.10) */
#define MTPEV_CB	(0x02)	   /* Changeback	  (Q.791 1.11) */
#define MTPEV_REST	(0x03)	   /* Restoration	  (Q.791 1.12) */
#define MTPEV_RPO	(0x04)	   /* RPO		  (Q.791 2.10) */
#define MTPEV_RPO_CLR	(0x05)	   /* RPO cleared	  (Q.791 2.11) */
#define MTPEV_CONG	(0x06)	   /* SL Congestion	  (Q.791 3.6)  */
#define MTPEV_CONG_CLR	(0x07)	   /* Congestion cleared  (Q.791 3.9)  */
#define MTPEV_CONG_DIS	(0x08)	   /* MSU discarded	  (Q.791 3.11) */
#define MTPEV_LS_LOST	(0x09)	   /* Linkset failure	  (Q.791 4.3)  */
#define MTPEV_LS_OK	(0x0A)	   /* Linkset recovered   (Q.791 4.4)  */
#define MTPEV_TFP_SENT	(0x0B)	   /* TFP Broadcast	  (Q.791 4.5)  */
#define MTPEV_TFA_SENT	(0x0C)	   /* TFA Broadcast	  (Q.791 4.6)  */
#define MTPEV_DEST_LOST (0x0D)	   /* Dest unavailable	  (Q.791 4.11) */
#define MTPEV_DEST_OK	(0x0E)	   /* Dest available	  (Q.791 4.12) */
#define MTPEV_AJSP_LOST (0x0F)	   /* Adj SP inaccessible (Q.791 5.1)  */
#define MTPEV_AJSP_OK	(0x10)	   /* Adj SP accessible   (Q.791 5.4)  */

/*
 * Status values for use in 'status' field of
 * MTP_MSG_SRT_RESULT indication:
 */
#define MTPSRTR_SUCCESS         (0)     /* Success */
#define MTPSRTR_FAIL_T10_EXP    (1)     /* SLTC SRT Timer T10 expiry */
#define MTPSRTR_FAIL_BAD_TP     (2)     /* Invalid Test Pattern received */
#define MTPSRTR_FAIL_BAD_LINK   (3)     /* SRA received on wrong link */
#define MTPSRTR_FAIL_BAD_OPC    (4)     /* SRA contained wrong OPC */
#define MTPSRTR_FAIL_ABORT      (5)     /* SRT aborted internally */

/*********************************************************************
 * Definitions for interfacing to the Level 2 Simulator
 ********************************************************************/

/*
 * None currently defined:
 */

/*********************************************************************
 * Definitions for interfacing to Management
 ********************************************************************/

/*
 * Message Types:
 */
#define MGT_MSG_TRACE_INTR  (0x0002)
#define MGT_MSG_TRACE_EV    (0x0003)
#define MGT_MSG_EVENT_IND   (0x0008)
#define MGT_MSG_SCC_INTR    (0x0111)
#define MGT_MSG_SS7_STATE   (0x0201)
#define MGT_MSG_SS7_FAIL    (0x0202)  /* Old mnemonic for backwards compatibility */
#define MGT_MSG_SS7_EVENT   (0x0202)  /* Level 2 Q.791 Event notification */
#define MGT_MSG_MTP_EVENT   (0x0301)  /* MTP Q.791 Event notification */
#define MGT_MSG_CONFIG0     (0x7f10)  /* PS-S71 configuration message */
#define MGT_MSG_CONFIG1     (0x7f11)  /* PS-S73 configuration message */
#define API_MSG_TX_REQ	    (0xcf00)
#define API_MSG_RX_IND	    (0x8f01)
#define API_MSG_RTVD_MSG    (0x8f08)
#define API_MSG_RESET       (0x7f00)
#define DBG_MSG_MEM_RD	    (0x7f01)
#define DBG_RSP_MEM_RD	    (0x3f01)
#define DBG_MSG_MEM_MOD     (0x7f02)
#define DBG_RSP_MEM_MOD     (0x3f02)
#define API_MSG_RST_REQ     (0x7f03)  /* Request Board Reset */
#define API_MSG_RST_IND     (0x0f04)  /* Board Reset Indication */
#define API_MSG_TST_REQ     (0x7f05)  /* API link test & watchdog */
#define MGT_MSG_CONGESTION  (0x0f06)  /* Congestion onset/abatement */
#define API_MSG_CNF_IND     (0x0f09)  /* Configuration status indication */
#define MGT_MSG_READ_ID     (0x7f0a)  /* Read identification */
#define MGT_MSG_MON_LINK    (0x7f0c)  /* Link config for monitor code file */
#define SYS_MNG_TICK        (0x0100)  /* Msg type sent to SYS_MNG */
#define SYS_MNG_INPUT	    (0x0200)  /* User text input msg type */
#define SYS_MNG_OUTPUT	    (0x0300)  /* User text output msg type */
#define SYS_MNG_DPM_INTR    (0x0400)  /* Dual Port Memory Interrupt */
#define SPE_MSG_PLAY_REQ    (0xcf03)
#define SPE_MSG_PLAY_CONF   (0x8f04)
#define SPE_MSG_MEM_MOD     (0x7f0b)
#define QDV_MSG_R_STATS     (0x6f14)  /* Link stats for monitor code file */
#define MGT_MSG_SEL_TRACE   (0x0f16)  /* Selective trace event indication */
#define MGT_MSG_L1_CONFIG   (0x7f17)  /* Layer 1 link config message */
#define MGT_MSG_L1_END      (0x7f18)  /* Layer 1 link end message */
#define MGT_MSG_ABORT       (0x7f19)  /* General purpose abort message */
#define MGT_MSG_R_BRDINFO   (0x6f0d)  /* Read Board Information Message */


/*********************************************************************
 * Definitions for interfacing to the SSD module
 ********************************************************************/

/*
 * Message types:
 */
#define SSD_MSG_RESET       (0x7680)    /* SSD module reset request */
#define SSD_MSG_RST_BOARD   (0x7681)    /* SSD board reset request */
#define SSD_MSG_STATE_IND   (0x06a0)    /* SSD board status indication */

/*
 * Definitions of length, offset and size of
 * SSD_MSG_RESET message parameter field:
 */
#define SSDML_RESET             (24)
#define SSDMO_RESET_module_id   (0)             /* SSD module id */
#define SSDMS_RESET_module_id           (1)
#define SSDMO_RESET_cnv_id      (1)             /* obsolete - set to zero */
#define SSDMS_RESET_cnv_id              (1)
#define SSDMO_RESET_scv_id      (2)             /* obsolete - set to zero */
#define SSDMS_RESET_scv_id              (1)
#define SSDMO_RESET_mgmt_id     (3)             /* management module_id */
#define SSDMS_RESET_mgmt_id             (1)
#define SSDMO_RESET_code_file   (4)             /* obsolete - set to zero */
#define SSDMS_RESET_code_file           (18)
#define SSDMO_RESET_num_boards  (22)            /* max number of boards */
#define SSDMS_RESET_num_boards          (2)
#define SSDMO_RESET_reserved    (24)
#define SSDMS_RESET_reserved            (0)

/*
 * Definitions of length, offset and size of
 * the 'old' format SSD_MSG_RST_BOARD message
 * parameter field:
 */
#define SSDML_RST_BOARD                 (22)     /* For use with PCCS3 only */
#define SSDMO_RST_BOARD_phy_id          (0)      /* For use with PCCS3 only */
#define SSDMS_RST_BOARD_phy_id              (4)  /* For use with PCCS3 only */
#define SSDMO_RST_BOARD_code_file       (4)      /* For use with PCCS3 only */
#define SSDMS_RST_BOARD_code_file           (18) /* For use with PCCS3 only */

/*
 * Definitions of length, offset and size of
 * SSD_MSG_RST_BOARD message parameter field:
 */
#define SSDML_RST_BOARD2                (24)
#define SSDML_RST_BOARD2_VER2           (26)
#define SSDMO_RST_BOARD2_board_type     (0)
#define SSDMS_RST_BOARD2_board_type             (2)
#define SSDMO_RST_BOARD2_phy_id         (2)
#define SSDMS_RST_BOARD2_phy_id                 (4)
#define SSDMO_RST_BOARD2_code_file      (6)
#define SSDMS_RST_BOARD2_code_file              (18)
#define SSDMO_RST_BOARD2_run_mode       (24)
#define SSDMS_RST_BOARD2_run_mode               (2)

/*
 * Definitions for use in board_type field
 * of SSD_MSG_RST_BOARD message (when SSD
 * supports more than 1 board type):
 */
#define SSD_PCCS3                       (0)
#define SSD_PCCS6                       (1)
#define SSD_SEPTELCP                    (2)
#define SSD_SS7HD                       (3)

/*
 * Values for status in SSD_MSG_STATE_IND messages:
 */
#define SSDSI_RESET     (0x60)  /* Processor successfully reset */
#define SSDSI_FAILURE   (0x62)  /* Processor Failure */
#define SSDSI_BRD_RMVD  (0x64)  /* Board removed (hot swap only) */
#define SSDSI_BRD_INS   (0x65)  /* Board inserted (hot swap only) */
#define SSDSI_LIC_FAIL  (0x66)  /* Licence validation failured */
#define SSDSI_LIC_CRP   (0x67)  /* License corruption */
#define SSDSI_BCONG_CLR (0x70)  /* Message congestion towards board cleared */
#define SSDSI_BCONG_ON  (0x71)  /* Message congestion towards board occurred */

/*
 * Error code definitions:
 */
#define SSD_BAD_MSG     (1)     /* Invalid message type received */
#define SSD_BAD_PARAM   (2)     /* Invalid parameters in message */
#define SSD_OP_FAIL     (3)     /* Operation failed or partially failed */


/*********************************************************************
 * Definitions for interfacing to SS7HD boards physical layer
 ********************************************************************/

/*
 * Definitions of length, offset and size of
 * MGT_MSG_L1_CONFIG message parameter field:
 */
#define MGTML_L1_CFG         (40)
#define MGTMO_L1_CFG_sp_id              (0)     /* Signalling processor id */
#define MGTMS_L1_CFG_sp_id                   (2)
#define MGTMO_L1_CFG_sp_channel         (2)     /* Signalling channel */
#define MGTMS_L1_CFG_sp_channel              (2)
#define MGTMO_L1_CFG_data_rate          (4)     /* Link operation */
#define MGTMS_L1_CFG_data_rate               (2)
#define MGTMO_L1_CFG_link_source        (6)     /* Signalling link source */
#define MGTMS_L1_CFG_link_source             (2)

#define MGTMO_L1_CFG_data_invert         (8)    /* Invert data on link */
#define MGTMS_L1_CFG_data_invert             (1)
#define MGTMO_L1_CFG_clock_invert_serial (9)    /* Invert serial clocks */
#define MGTMS_L1_CFG_clock_invert_serial     (1)

#define MGTMO_L1_CFG_serial_clocking    (10)    /* Clock for serial port */
#define MGTMS_L1_CFG_serial_clocking         (2)
#define MGTMO_L1_CFG_link_stream        (12)    /* Signalling stream */
#define MGTMS_L1_CFG_link_stream             (2)
#define MGTMO_L1_CFG_link_timeslot      (14)    /* Sinalling timeslot */
#define MGTMS_L1_CFG_link_timeslot           (2)
#define MGTMO_L1_CFG_link_ct_channel_tx (16)    /* CTbus channel for Tx TS */
#define MGTMS_L1_CFG_link_ct_channel_tx      (2)
#define MGTMO_L1_CFG_link_ct_channel_rx (18)    /* CTbus channel for Rx TS */
#define MGTMS_L1_CFG_link_ct_channel_rx      (2)
#define MGTMO_L1_CFG_options            (20)    /* Per link config options */
#define MGTMS_L1_CFG_options                 (4)
#define MGTMO_L1_CFG_reserved2          (24)    /* Reserved */
#define MGTMS_L1_CFG_reserved2               (16)

/*
 * Bit definitions for Layer 1 per-link configuration options
 */
#define L1OPT_NOAUTOFISU      (0x00000001)   /* don't auto. generate FISU's */
#define L1OPT_APPLYTIMESTAMPS (0x00000002)   /* apply time-stamps */

/*
 * Definitions of length, offset and size of
 * MGT_MSG_L1_END message parameter field:
 */
#define MGTML_L1_END         (4)
#define MGTMO_L1_END_sp_id              (0)     /* Signalling processor id */
#define MGTMS_L1_END_sp_id                   (2)
#define MGTMO_L1_END_sp_channel         (2)     /* Signalling channel */
#define MGTMS_L1_END_sp_channel              (2)


/*********************************************************************
 * Definitions for reading Board Information from a board
 ********************************************************************/

/*
 * Length, offset and size definitions for
 * use with the MGT_MSG_R_BRDINFO message.
 *
 */
#define MGTML_R_BRDINFO                 (60)
#define MGTMO_R_BRDINFO_board_type      (0)
#define MGTMS_R_BRDINFO_board_type              (1)
#define MGTMO_R_BRDINFO_board_rev       (1)
#define MGTMS_R_BRDINFO_board_rev               (1)
#define MGTMO_R_BRDINFO_rtb_type        (2)
#define MGTMS_R_BRDINFO_rtb_type                (1)
#define MGTMO_R_BRDINFO_swa             (3)        /* 'BOOT' switch (if applicable) */
#define MGTMS_R_BRDINFO_swa                     (1)
#define MGTMO_R_BRDINFO_swb             (4)        /* 'ADDR' switch (if applicable) */
#define MGTMS_R_BRDINFO_swb                     (1)
#define MGTMO_R_BRDINFO_rtb_switch      (5)
#define MGTMS_R_BRDINFO_rtb_switch              (1)
#define MGTMO_R_BRDINFO_h110_shelf      (6)
#define MGTMS_R_BRDINFO_h110_shelf              (1)
#define MGTMO_R_BRDINFO_h110_slot       (7)
#define MGTMS_R_BRDINFO_h110_slot               (1)
#define MGTMO_R_BRDINFO_prom_maj_rev    (8)
#define MGTMS_R_BRDINFO_prom_maj_rev            (1)
#define MGTMO_R_BRDINFO_prom_min_rev    (9)
#define MGTMS_R_BRDINFO_prom_min_rev            (1)
#define MGTMO_R_BRDINFO_esn             (10)       /* Electronic Serial Number*/
#define MGTMS_R_BRDINFO_esn                     (8)
#define MGTMO_R_BRDINFO_lsn             (18)       /* License Serial Number */
#define MGTMS_R_BRDINFO_lsn                     (8)
#define MGTMO_R_BRDINFO_dram_size       (26)
#define MGTMS_R_BRDINFO_dram_size               (4)
#define MGTMO_R_BRDINFO_bsn             (30)       /* Board Serial Number */
#define MGTMS_R_BRDINFO_bsn                     (20)
#define MGTMO_R_BRDINFO_lictype         (50)       /* Board licence type */
#define MGTMS_R_BRDINFO_lictype                 (2)

/*
 * Values used in MGT_MSG_R_BRDINFO message
 * in the BRDINFO_board_type field:
 */
#define BRDINFO_BTYPE_UNKNOWN           (0)
#define BRDINFO_BTYPE_CPM8              (1)
#define BRDINFO_BTYPE_SPCI              (2)
#define BRDINFO_BTYPE_SS7HDP            (3)
#define BRDINFO_BTYPE_SS7HDC            (4)


/*********************************************************************
 * Miscellaneous Definitions
 ********************************************************************/

/*
 * Definitions of length, offset and size of
 * SPE_MSG_PLAY_REQ & SPE_MSG_PLAY_CONF message
 * parameter field:
 */
#define SPEML_PLAY      (8)
#define SPEMO_PLAY_addr         (0)
#define SPEMS_PLAY_addr                 (4)
#define SPEMO_PLAY_num_bytes    (4)
#define SPEMS_PLAY_num_bytes            (8)

/*
 * Further error codes:
 */
#define SS7_SEND_ERR	    (0xf0)  /* failure to send message */

#define ERR_SDLSIG_LOW      (0x2f)  /* Running short of SDL signals */
#define ERR_NO_SDLSIG       (0x2e)  /* SDL signal queue exhausted */

/*
 * The following definitions are (optionally) used in API_MSG_TX_REQ,
 * API_MSG_RX_IND, SS7_MSG_TX_REQ and SS7_MSG_RX_IND to indicate point
 * code size:
 */
#define MTPTFR14        (0x00)        	/* 14-bit point code */
#define MTPTFR24        (0x01)        	/* 24-bit point code */
#define MTPTFR16        (0x02)          /* 16-bit point code */

/*
 * The following definitions are obsolete
 * and should not be used:
 */
#define SS7_MAX_EVENT	(0x10)      	/* Obsolete */
#define MTP_MAX_EVENT   (0x15)  	/* Obsolete */


/*********************************************************************
 * Definitions for interfacing to the monitoring code file
 ********************************************************************/

/*
 * Length, offset and size definitions for MGT_MSG_MON_LINK
 */
#define MGTML_MONLINK                   (12)
#define MGTMO_MONLINK_flags             (0)
#define MGTMS_MONLINK_flags                     (2)
#define MGTMO_MONLINK_link_id           (2)
#define MGTMS_MONLINK_link_id                   (2)
#define MGTMO_MONLINK_stream            (4)
#define MGTMS_MONLINK_stream                    (2)
#define MGTMO_MONLINK_timeslot          (6)
#define MGTMS_MONLINK_timeslot                  (2)
#define MGTMO_MONLINK_filter            (8)
#define MGTMS_MONLINK_filter                    (2)
#define MGTMO_MONLINK_user_module       (10)
#define MGTMS_MONLINK_user_module               (1)
#define MGTMO_MONLINK_phys_mask         (11)
#define MGTMS_MONLINK_phys_mask                 (1)

/*
 * Length, offset and size definitions for QDV_MSG_R_STATS
 */
#define QDVML_RSTATS                    (24)
#define QDVMO_RSTATS_period             (0)
#define QDVMS_RSTATS_period                     (4)
#define QDVMO_RSTATS_rx_frames          (4)
#define QDVMS_RSTATS_rx_frames                  (4)
#define QDVMO_RSTATS_user_frames        (8)
#define QDVMS_RSTATS_user_frames                (4)
#define QDVMO_RSTATS_cong_count         (12)
#define QDVMS_RSTATS_cong_count                 (4)
#define QDVMO_RSTATS_discard_frames     (16)
#define QDVMS_RSTATS_discard_frames             (4)
#define QDVMO_RSTATS_error_events       (20)
#define QDVMS_RSTATS_error_events               (4)




